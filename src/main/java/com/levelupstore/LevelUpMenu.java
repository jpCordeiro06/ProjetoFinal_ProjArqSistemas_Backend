package com.levelupstore;

import com.levelupstore.estoque.model.*;
import com.levelupstore.estoque.repository.ProdutoRepository;
import com.levelupstore.estoque.service.EstoqueService;
import com.levelupstore.pagamentos.*;
import com.levelupstore.pdv.model.ItemVenda;
import com.levelupstore.pdv.model.StatusVenda;
import com.levelupstore.pdv.model.Venda;
import com.levelupstore.pdv.repository.StatusVendaRepository;
import com.levelupstore.pdv.service.VendaService;
import com.levelupstore.relatorios.dto.RelatorioEstoqueDTO;
import com.levelupstore.relatorios.service.RelatorioService;
import com.levelupstore.usuarios.model.Cliente;
import com.levelupstore.usuarios.model.Usuario;
import com.levelupstore.usuarios.repository.ClienteRepository;
import com.levelupstore.usuarios.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Scanner;

@Component
public class LevelUpMenu implements CommandLineRunner {

    @Autowired private ProdutoRepository produtoRepository;
    @Autowired private EstoqueService estoqueService;
    @Autowired private VendaService vendaService;
    @Autowired private UsuarioRepository usuarioRepository;
    @Autowired private ClienteRepository clienteRepository;
    @Autowired private StatusVendaRepository statusVendaRepository;
    @Autowired private RelatorioService relatorioService;

    @Override
    public void run(String... args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        int opcaoPrincipal = 0;

        System.out.println("BEM-VINDO À LEVEL UP STORE");

        while (opcaoPrincipal != 9) {
            System.out.println("\n--- TELA DE LOGIN ---");
            System.out.println("1 - Acesso Vendedor");
            System.out.println("2 - Acesso Administrador");
            System.out.println("9 - Sair");
            System.out.print("Opção: ");

            if (scanner.hasNextInt()) {
                opcaoPrincipal = scanner.nextInt();
                scanner.nextLine();

                switch (opcaoPrincipal) {
                    case 1 -> menuVendedor(scanner);
                    case 2 -> menuAdministrador(scanner);
                    case 9 -> System.out.println("Encerrando...");
                    default -> System.out.println("Opção inválida.");
                }
            } else {
                scanner.next();
            }
        }
    }

    // MENU ADMINISTRADOR
    private void menuAdministrador(Scanner scanner) {
        int opcaoAdmin = 0;
        Long idUsuarioLogado = 1L; // Simula Admin ID 1

        while (opcaoAdmin != 9) {
            System.out.println("\n--- PAINEL ADMINISTRADOR ---");
            System.out.println("1 - Realizar Nova Venda");
            System.out.println("2 - Consultar Faturamento Total");
            System.out.println("3 - Relatório Financeiro de Estoque");
            System.out.println("4 - Listar Produtos");
            System.out.println("5 - Cadastrar Novo Produto");
            System.out.println("9 - Logout");
            System.out.print("Opção: ");

            opcaoAdmin = scanner.nextInt();
            scanner.nextLine();

            switch (opcaoAdmin) {
                case 1 -> realizarVendaReal(scanner, idUsuarioLogado);
                case 2 -> exibirFaturamentoVendas();
                case 3 -> exibirRelatorioFinanceiro();
                case 4 -> listarProdutosReais();
                case 5 -> cadastrarProdutoRapido(scanner);
                case 9 -> System.out.println("Saindo do Admin...");
                default -> System.out.println("Opção inválida.");
            }
        }
    }

    // MENU VENDEDOR
    private void menuVendedor(Scanner scanner) {
        int opcaoVendedor = 0;
        Long idUsuarioLogado = 2L; // Simula Vendedor ID 2

        while (opcaoVendedor != 9) {
            System.out.println("\n--- PAINEL VENDEDOR ---");
            System.out.println("1 - Realizar Nova Venda");
            System.out.println("2 - Consultar Preços");
            System.out.println("9 - Logout");
            System.out.print("Opção: ");

            opcaoVendedor = scanner.nextInt();
            scanner.nextLine();

            switch (opcaoVendedor) {
                case 1 -> realizarVendaReal(scanner, idUsuarioLogado);
                case 2 -> listarProdutosReais();
                case 9 -> System.out.println("Saindo do Vendedor...");
                default -> System.out.println("Opção inválida.");
            }
        }
    }

    // LÓGICA DE VENDAS
    private void realizarVendaReal(Scanner scanner, Long idVendedor) {
        try {
            System.out.println("\n--- NOVA VENDA ---");

            Usuario vendedor = usuarioRepository.findById(idVendedor).orElseThrow();
            Cliente cliente = clienteRepository.findById(1L).orElseThrow(); // Cliente fixo para teste

            Venda venda = new Venda();
            venda.setVendedor(vendedor);
            venda.setCliente(cliente);

            // Define status inicial como FINALIZADA (ID 2)
            StatusVenda status = statusVendaRepository.findById(2L).orElse(null);
            venda.setStatusVenda(status);

            // Adicionar itens ao carrinho
            while (true) {
                System.out.print("ID do Produto (0 para finalizar): ");
                Long idProd = scanner.nextLong();
                if (idProd == 0) break;

                System.out.print("Quantidade: ");
                int qtd = scanner.nextInt();

                Produto prod = produtoRepository.findById(idProd).orElse(null);
                if (prod != null) {
                    venda.adicionarItem(new ItemVenda(prod, qtd));
                    System.out.println("Item adicionado.");
                } else {
                    System.out.println("Produto não encontrado.");
                }
            }

            if (venda.getItens().isEmpty()) {
                System.out.println("Venda cancelada (vazia).");
                return;
            }

            // Checkout
            venda.calcularTotal();
            System.out.println("Total: R$ " + venda.getValorTotal());

            TipoPagamento pagamento = selecionarFormaPagamento(scanner);
            venda.setTipoPagamento(pagamento);

            // Processar no Service
            Venda concluida = vendaService.realizarVenda(venda);

            System.out.println("VENDA CONCLUÍDA! ID: " + concluida.getId());
            System.out.println("Valor Final: R$ " + concluida.getValorTotal());

        } catch (Exception e) {
            System.out.println("Erro na venda: " + e.getMessage());
        }
    }

    // SELEÇÃO DE PAGAMENTO
    private TipoPagamento selecionarFormaPagamento(Scanner scanner) {
        System.out.println("Forma de Pagamento:");
        System.out.println("1-Dinheiro | 2-PIX | 3-Crédito | 4-Débito");
        System.out.print("Opção: ");
        int op = scanner.nextInt();

        switch (op) {
            case 1: return new Dinheiro();
            case 2:
                Pix pix = new Pix();
                pix.setTxid("pix-console-123");
                return pix;
            case 3:
                CartaoCredito cc = new CartaoCredito();
                cc.setBandeira("Visa");
                cc.setNumeroTransacao("NSU-123");
                System.out.print("Parcelas: ");
                cc.setNumeroParcelas(scanner.nextInt());
                return cc;
            case 4:
                CartaoDebito cd = new CartaoDebito();
                cd.setBandeira("Mastercard");
                cd.setNumeroTransacao("NSU-987");
                cd.setTipoConta("Corrente");
                return cd;
            default: return new Dinheiro();
        }
    }

    // RELATÓRIOS
    private void exibirRelatorioFinanceiro() {
        System.out.println("\n--- RELATÓRIO DE ESTOQUE ---");
        RelatorioEstoqueDTO dados = relatorioService.gerarRelatorioEstoque();
        System.out.println("Total Itens: " + dados.getTotalItens());
        System.out.println("Item Mais Caro: " + dados.getProdutoMaisCaro());
        System.out.println("VALOR TOTAL IMOBILIZADO: R$ " + dados.getValorTotalEstoque());
    }

    private void exibirFaturamentoVendas() {
        System.out.println("\n--- FATURAMENTO TOTAL ---");
        BigDecimal total = relatorioService.calcularTotalVendas();
        System.out.println("Total Vendido: R$ " + total);
    }

    // LISTAR ESTOQUE
    private void listarProdutosReais() {
        System.out.println("\n--- ESTOQUE ATUAL ---");
        var produtos = produtoRepository.findAll();
        System.out.printf("%-5s | %-30s | %-10s | %-5s%n", "ID", "NOME", "PREÇO", "QTD");
        for (Produto p : produtos) {
            // Acessa quantidade através da entidade Estoque
            int qtd = (p.getEstoque() != null) ? p.getEstoque().getQuantidade() : 0;
            System.out.printf("%-5d | %-30s | R$ %-7.2f | %-5d%n",
                    p.getId(), p.getNome(), p.getPreco(), qtd);
        }
    }

    // CADASTRAR PRODUTO
    private void cadastrarProdutoRapido(Scanner scanner) {
        System.out.println("\n--- CADASTRO RÁPIDO ---");
        try {
            scanner.nextLine();
            System.out.print("Nome: ");
            String nome = scanner.nextLine();
            System.out.print("Preço: ");
            double preco = scanner.nextDouble();
            System.out.print("Qtd Inicial: ");
            int qtd = scanner.nextInt();

            Jogo novoJogo = new Jogo();
            novoJogo.setNome(nome);
            novoJogo.setPreco(new BigDecimal(preco));

            // Define IDs fixos para categorias/condições (simplificação)
            Categoria cat = new Categoria(); cat.setId(1L);
            Condicao cond = new Condicao(); cond.setId(1L);
            novoJogo.setCategoria(cat);
            novoJogo.setCondicao(cond);
            novoJogo.setPlataforma("Multi");

            Estoque est = new Estoque();
            est.setQuantidade(qtd);
            novoJogo.setEstoque(est);

            estoqueService.salvarProduto(novoJogo);
            System.out.println("Produto cadastrado!");
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }
}