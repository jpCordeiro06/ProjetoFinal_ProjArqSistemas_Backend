package com.levelupstore.pdv.service;

import com.levelupstore.exception.BusinessRuleException;
import com.levelupstore.estoque.model.Estoque;
import com.levelupstore.estoque.model.Produto;
import com.levelupstore.estoque.repository.EstoqueRepository;
import com.levelupstore.estoque.repository.ProdutoRepository;
import com.levelupstore.pdv.model.CupomDesconto;
import com.levelupstore.pdv.model.ItemVenda;
import com.levelupstore.pdv.model.StatusVenda;
import com.levelupstore.pdv.model.Venda;
import com.levelupstore.pdv.repository.CupomDescontoRepository;
import com.levelupstore.pdv.repository.StatusVendaRepository;
import com.levelupstore.pdv.repository.VendaRepository;
import com.levelupstore.usuarios.repository.ClienteRepository;
import com.levelupstore.usuarios.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
public class VendaService {

    private final VendaRepository vendaRepository;
    private final ProdutoRepository produtoRepository;
    private final EstoqueRepository estoqueRepository; // Novo!
    private final UsuarioRepository usuarioRepository;
    private final ClienteRepository clienteRepository;
    private final StatusVendaRepository statusVendaRepository; // Novo!
    private final CupomDescontoRepository cupomDescontoRepository; // Novo!

    public VendaService(VendaRepository vendaRepository, ProdutoRepository produtoRepository,
                        EstoqueRepository estoqueRepository, UsuarioRepository usuarioRepository,
                        ClienteRepository clienteRepository, StatusVendaRepository statusVendaRepository,
                        CupomDescontoRepository cupomDescontoRepository) {
        this.vendaRepository = vendaRepository;
        this.produtoRepository = produtoRepository;
        this.estoqueRepository = estoqueRepository;
        this.usuarioRepository = usuarioRepository;
        this.clienteRepository = clienteRepository;
        this.statusVendaRepository = statusVendaRepository;
        this.cupomDescontoRepository = cupomDescontoRepository;
    }

    @Transactional
    public Venda realizarVenda(Venda venda) {
        // 1. Validações Básicas (Usuario/Cliente)
        if (venda.getVendedor() == null || !usuarioRepository.existsById(venda.getVendedor().getId())) {
            throw new BusinessRuleException("Vendedor inválido.");
        }
        if (venda.getCliente() == null || !clienteRepository.existsById(venda.getCliente().getId())) {
            throw new BusinessRuleException("Cliente inválido.");
        }

        // 2. Definir Status Inicial (Se não vier, assume FINALIZADA para facilitar teste)
        if (venda.getStatusVenda() == null) {
            // Tenta buscar status 'FINALIZADA' ou cria um default se não existir no banco ainda
            StatusVenda status = statusVendaRepository.findByDescricao("FINALIZADA")
                    .orElseThrow(() -> new BusinessRuleException("Status de venda não cadastrado no sistema."));
            venda.setStatusVenda(status);
        }

        // 3. Validar e Processar Cupom
        if (venda.getCupomDesconto() != null && venda.getCupomDesconto().getCodigo() != null) {
            CupomDesconto cupom = cupomDescontoRepository.findByCodigo(venda.getCupomDesconto().getCodigo())
                    .orElseThrow(() -> new BusinessRuleException("Cupom inválido: " + venda.getCupomDesconto().getCodigo()));

            if (cupom.getValidade() != null && cupom.getValidade().isBefore(LocalDate.now())) {
                throw new BusinessRuleException("Cupom vencido.");
            }

            if (cupom.getCliente() != null) {
                // Se o cupom tem dono, verifica se é o mesmo da venda
                if (!cupom.getCliente().getId().equals(venda.getCliente().getId())) {
                    throw new BusinessRuleException("Este cupom pertence a outro cliente!");
                }
            }

            venda.setCupomDesconto(cupom);
        }

        // 4. Processar Itens e Estoque
        BigDecimal totalBruto = BigDecimal.ZERO;

        for (ItemVenda item : venda.getItens()) {
            Produto produto = produtoRepository.findById(item.getProduto().getId())
                    .orElseThrow(() -> new BusinessRuleException("Produto não encontrado."));

            // Acessa o estoque separado
            Estoque estoque = produto.getEstoque();
            if (estoque == null || estoque.getQuantidade() < item.getQuantidade()) {
                throw new BusinessRuleException("Estoque insuficiente para: " + produto.getNome());
            }

            // Baixa e Atualiza
            estoque.setQuantidade(estoque.getQuantidade() - item.getQuantidade());
            estoqueRepository.save(estoque); // Salva a tabela separada

            item.setPrecoUnitario(produto.getPreco());
            item.setVenda(venda);

            totalBruto = totalBruto.add(item.getSubtotal());
        }

        // 5. Calcular Total com Desconto
        BigDecimal valorFinal = totalBruto;
        if (venda.getCupomDesconto() != null) {
            CupomDesconto cupom = venda.getCupomDesconto();
            BigDecimal valorDesconto;

            // Se o código começa com TROCA, o campo 'percentual' é na verdade VALOR FIXO
            if (cupom.getCodigo().startsWith("TROCA-")) {
                valorDesconto = cupom.getPercentual(); // Aqui é valor monetário
            } else {
                // Caso contrário, é porcentagem (ex: 10 para 10%)
                valorDesconto = totalBruto.multiply(cupom.getPercentual())
                        .divide(new BigDecimal(100));
            }

            valorFinal = totalBruto.subtract(valorDesconto);

            // Garante que não fique negativo (a loja não paga para o cliente levar)
            if (valorFinal.compareTo(BigDecimal.ZERO) < 0) {
                valorFinal = BigDecimal.ZERO;
            }
        }

        venda.setValorTotal(valorFinal);

        return vendaRepository.save(venda);
    }
}