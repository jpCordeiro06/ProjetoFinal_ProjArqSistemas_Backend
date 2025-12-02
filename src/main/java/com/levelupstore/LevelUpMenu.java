package com.levelupstore.cli;

import com.levelupstore.estoque.model.*;
import com.levelupstore.estoque.repository.ProdutoRepository;
import com.levelupstore.estoque.service.EstoqueService;
import com.levelupstore.pagamentos.*;
import com.levelupstore.pdv.model.CupomDesconto;
import com.levelupstore.pdv.model.ItemVenda;
import com.levelupstore.pdv.model.StatusVenda;
import com.levelupstore.pdv.model.Venda;
import com.levelupstore.pdv.repository.CupomDescontoRepository;
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
import java.time.LocalDate;
import java.util.Scanner;
import java.util.UUID;

@Component
public class LevelUpMenu implements CommandLineRunner {

    @Autowired private ProdutoRepository produtoRepository;
    @Autowired private EstoqueService estoqueService;
    @Autowired private VendaService vendaService;
    @Autowired private UsuarioRepository usuarioRepository;
    @Autowired private ClienteRepository clienteRepository;
    @Autowired private StatusVendaRepository statusVendaRepository;
    @Autowired private RelatorioService relatorioService;
    @Autowired private CupomDescontoRepository cupomDescontoRepository;

    @Override
    public void run(String... args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        int opcaoPrincipal = 0;

        System.out.println("=========================================");
        System.out.println("      SISTEMA LEVEL UP STORE v1.0        ");
        System.out.println("=========================================");

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
        Long idUsuarioLogado = 1L; // Simula Admin

        while (opcaoAdmin != 9) {
            System.out.println("\n--- PAINEL ADMINISTRADOR ---");
            System.out.println("1 - Realizar Nova Venda");
            System.out.println("2 - Realizar Troca (Trade-In)");
            System.out.println("3 - Consultar Faturamento Total");
            System.out.println("4 - Relatório Financeiro de Estoque");
            System.out.println("5 - Listar Produtos");
            System.out.println("6 - Cadastrar Novo Produto");
            System.out.println("7 - Cadastrar Novo Cliente");
            System.out.println("9 - Logout");
            System.out.print("Opção: ");

            opcaoAdmin = scanner.nextInt();
            scanner.nextLine();

            switch (opcaoAdmin) {
                case 1 -> realizarVendaReal(scanner, idUsuarioLogado);
                case 2 -> realizarTroca(scanner);
                case 3 -> exibirFaturamentoVendas();
                case 4 -> exibirRelatorioFinanceiro();
                case 5 -> listarProdutosReais();
                case 6 -> cadastrarProdutoRapido(scanner);
                case 7 -> cadastrarCliente(scanner);
                case 9 -> System.out.println("Saindo do Admin...");
                default -> System.out.println("Opção inválida.");
            }
        }
    }

    // MENU VENDEDOR
    private void menuVendedor(Scanner scanner) {
        int opcaoVendedor = 0;
        Long idUsuarioLogado = 2L; // Simula Vendedor

        while (opcaoVendedor != 9) {
            System.out.println("\n--- PAINEL VENDEDOR ---");
            System.out.println("1 - Realizar Nova Venda");
            System.out.println("2 - Realizar Troca (Trade-In)");
            System.out.println("3 - Consultar Preços");
            System.out.println("4 - Cadastrar Novo Cliente");
            System.out.println("9 - Logout");
            System.out.print("Opção: ");

            opcaoVendedor = scanner.nextInt();
            scanner.nextLine();

            switch (opcaoVendedor) {
                case 1 -> realizarVendaReal(scanner, idUsuarioLogado);
                case 2 -> realizarTroca(scanner);
                case 3 -> listarProdutosReais();
                case 4 -> cadastrarCliente(scanner);
                case 9 -> System.out.println("Saindo do Vendedor...");
                default -> System.out.println("Opção inválida.");
            }
        }
    }

    // --- LÓGICA DE VENDAS ---
    private void realizarVendaReal(Scanner scanner, Long idVendedor) {
        try {
            System.out.println("\n--- NOVA VENDA ---");

            Usuario vendedor = usuarioRepository.findById(idVendedor).orElseThrow();

            // Listar clientes para facilitar a escolha
            listarClientes();
            System.out.print("Digite o ID do Cliente: ");
            Long idCliente = scanner.nextLong();

            Cliente cliente = clienteRepository.findById(idCliente)
                    .orElseThrow(() -> new RuntimeException("Cliente não encontrado!"));

            System.out.println(">> Cliente Selecionado: " + cliente.getNome());

            Venda venda = new Venda();
            venda.setVendedor(vendedor);
            venda.setCliente(cliente);
            venda.setStatusVenda(statusVendaRepository.findById(2L).orElse(null));

            // Carrinho
            listarProdutosReais(); // Mostra produtos
            while (true) {
                System.out.print("\nID do Produto (0 para fechar carrinho): ");
                Long idProd = scanner.nextLong();
                if (idProd == 0) break;

                System.out.print("Quantidade: ");
                int qtd = scanner.nextInt();

                Produto prod = produtoRepository.findById(idProd).orElse(null);
                if (prod != null) {
                    venda.adicionarItem(new ItemVenda(prod, qtd));
                    System.out.println("Adicionado: " + prod.getNome());
                } else {
                    System.out.println("Produto não encontrado.");
                }
            }

            if (venda.getItens().isEmpty()) {
                System.out.println("Venda cancelada (carrinho vazio).");
                return;
            }

            // Cupom
            System.out.print("Possui cupom? (S/N): ");
            String temCupom = scanner.next();
            if (temCupom.equalsIgnoreCase("S")) {
                System.out.print("Código: ");
                String cod = scanner.next();
                CupomDesconto cp = new CupomDesconto();
                cp.setCodigo(cod);
                venda.setCupomDesconto(cp);
            }

            // Checkout e Pagamento
            venda.calcularTotal();
            System.out.println("Total Bruto: R$ " + venda.getValorTotal());

            // AQUI ESTÁ A CORREÇÃO: Chama o método que pergunta as parcelas
            TipoPagamento pagamento = selecionarFormaPagamento(scanner);
            venda.setTipoPagamento(pagamento);

            Venda concluida = vendaService.realizarVenda(venda);

            System.out.println("=================================");
            System.out.println("✅ VENDA CONCLUÍDA! ID: " + concluida.getId());
            System.out.println("👤 Cliente: " + concluida.getCliente().getNome());

            if (concluida.getCupomDesconto() != null) {
                System.out.println("🏷️ Cupom Aplicado: " + concluida.getCupomDesconto().getCodigo());
            }

            System.out.println("💰 Valor Final: R$ " + concluida.getValorTotal());
            System.out.println("💳 Forma Pagamento: " + concluida.getTipoPagamento().getDescricao());

            // Se for crédito, mostra parcelas (dá um cast seguro só pra exibir)
            if (concluida.getTipoPagamento() instanceof CartaoCredito cc) {
                System.out.println("📅 Parcelas: " + cc.getNumeroParcelas() + "x");
            }
            System.out.println("=================================");

        } catch (Exception e) {
            System.out.println("❌ Erro na venda: " + e.getMessage());
        }
    }

    // --- SELEÇÃO DE PAGAMENTO COM INTERAÇÃO ---
    private TipoPagamento selecionarFormaPagamento(Scanner scanner) {
        System.out.println("\n--- FORMA DE PAGAMENTO ---");
        System.out.println("1 - Dinheiro");
        System.out.println("2 - PIX");
        System.out.println("3 - Cartão de Crédito");
        System.out.println("4 - Cartão de Débito");
        System.out.print("Opção: ");
        int op = scanner.nextInt();

        return switch (op) {
            case 2 -> {
                Pix p = new Pix();
                p.setTxid("pix-" + System.currentTimeMillis());
                yield p;
            }
            case 3 -> {
                CartaoCredito c = new CartaoCredito();
                c.setBandeira("Visa"); // Simulação
                c.setNumeroTransacao("NSU-123");

                System.out.print("Número de Parcelas: ");
                int parc = scanner.nextInt();
                c.setNumeroParcelas(parc);

                yield c;
            }
            case 4 -> {
                CartaoDebito d = new CartaoDebito();
                d.setBandeira("Mastercard");
                d.setNumeroTransacao("NSU-987");
                d.setTipoConta("Corrente");
                yield d;
            }
            default -> new Dinheiro();
        };
    }

    // --- OUTROS MÉTODOS AUXILIARES ---

    private void listarClientes() {
        System.out.println("\n--- LISTA DE CLIENTES ---");
        var clientes = clienteRepository.findAll();
        System.out.printf("%-5s | %-30s | %-15s%n", "ID", "NOME", "CPF");
        for (Cliente c : clientes) {
            System.out.printf("%-5d | %-30s | %-15s%n", c.getId(), c.getNome(), c.getCpf());
        }
        System.out.println("---------------------------");
    }

    private void cadastrarCliente(Scanner scanner) {
        System.out.println("\n--- CADASTRO DE CLIENTE ---");
        try {
            scanner.nextLine();
            System.out.print("Nome: ");
            String nome = scanner.nextLine();
            System.out.print("CPF: ");
            String cpf = scanner.nextLine();
            System.out.print("Email: ");
            String email = scanner.nextLine();

            Cliente novo = new Cliente();
            novo.setNome(nome);
            novo.setCpf(cpf);
            novo.setEmail(email);

            clienteRepository.save(novo);
            System.out.println("✅ Cliente cadastrado com sucesso!");
        } catch (Exception e) {
            System.out.println("Erro ao cadastrar cliente: " + e.getMessage());
        }
    }

    private void realizarTroca(Scanner scanner) {
        System.out.println("\n--- MÓDULO DE TROCAS ---");
        try {
            // Pergunta o cliente para vincular o crédito
            listarClientes();
            System.out.print("ID do Cliente que trouxe o item: ");
            Long idCliente = scanner.nextLong();
            Cliente cliente = clienteRepository.findById(idCliente)
                    .orElseThrow(() -> new RuntimeException("Cliente não encontrado!"));

            scanner.nextLine();
            System.out.println("Cliente: " + cliente.getNome());

            System.out.print("Nome do Jogo/Console recebido: ");
            String nome = scanner.nextLine();

            System.out.print("Valor de Avaliação (Crédito): ");
            double valorAvaliacao = scanner.nextDouble();

            Jogo jogoUsado = new Jogo();
            jogoUsado.setNome(nome);
            jogoUsado.setPreco(new BigDecimal(valorAvaliacao * 1.5));

            Categoria cat = new Categoria(); cat.setId(1L);
            Condicao cond = new Condicao(); cond.setId(2L); // Usado

            jogoUsado.setCategoria(cat);
            jogoUsado.setCondicao(cond);
            jogoUsado.setPlataforma("Trade-In");

            Estoque est = new Estoque();
            est.setQuantidade(1);
            jogoUsado.setEstoque(est);

            estoqueService.salvarProduto(jogoUsado);
            System.out.println("Produto adicionado ao estoque.");

            String codigoCupom = "TROCA-" + UUID.randomUUID().toString().substring(0, 5).toUpperCase();
            CupomDesconto credito = new CupomDesconto();
            credito.setCodigo(codigoCupom);
            credito.setPercentual(new BigDecimal(valorAvaliacao));
            credito.setValidade(LocalDate.now().plusYears(1));
            credito.setAtivo(true);
            credito.setCliente(cliente); // Vínculo com cliente

            cupomDescontoRepository.save(credito);
            System.out.println("CRÉDITO GERADO: " + codigoCupom + " (Valor: R$ " + valorAvaliacao + ")");
            System.out.println("Válido apenas para o cliente: " + cliente.getNome());

        } catch (Exception e) {
            System.out.println("Erro na troca: " + e.getMessage());
        }
    }

    private void listarProdutosReais() {
        System.out.println("\n--- ESTOQUE ATUAL ---");
        var produtos = produtoRepository.findAll();
        System.out.printf("%-5s | %-30s | %-10s | %-10s | %-5s%n", "ID", "NOME", "CONDIÇÃO", "PREÇO", "QTD");
        for (Produto p : produtos) {
            int qtd = (p.getEstoque() != null) ? p.getEstoque().getQuantidade() : 0;
            String cond = (p.getCondicao() != null) ? p.getCondicao().getNome() : "-";
            System.out.printf("%-5d | %-30s | %-10s | R$ %-7.2f | %-5d%n", p.getId(), p.getNome(), cond, p.getPreco(), qtd);
        }
    }

    private void cadastrarProdutoRapido(Scanner scanner) {
        System.out.println("\n--- CADASTRO PRODUTO ---");
        try {
            scanner.nextLine();
            System.out.print("Nome: ");
            String nome = scanner.nextLine();
            System.out.print("Preço Venda: ");
            double preco = scanner.nextDouble();
            System.out.print("Qtd: ");
            int qtd = scanner.nextInt();

            System.out.print("Condição (1-Novo, 2-Usado): ");
            Long idCond = scanner.nextLong();

            Jogo novoJogo = new Jogo();
            novoJogo.setNome(nome);
            novoJogo.setPreco(new BigDecimal(preco));
            Categoria cat = new Categoria(); cat.setId(1L);
            Condicao cond = new Condicao(); cond.setId(idCond);
            novoJogo.setCategoria(cat);
            novoJogo.setCondicao(cond);
            novoJogo.setPlataforma("Multi");

            Estoque est = new Estoque();
            est.setQuantidade(qtd);
            novoJogo.setEstoque(est);

            estoqueService.salvarProduto(novoJogo);
            System.out.println("✅ Produto cadastrado!");
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    private void exibirRelatorioFinanceiro() {
        RelatorioEstoqueDTO d = relatorioService.gerarRelatorioEstoque();
        System.out.println("Total Itens: " + d.getTotalItens() + " | Valor: R$ " + d.getValorTotalEstoque());
    }
    private void exibirFaturamentoVendas() {
        System.out.println("Total Vendido: R$ " + relatorioService.calcularTotalVendas());
    }
}