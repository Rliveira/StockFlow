import Beans.Estoque;
import Beans.Produto;


import java.util.Scanner;

public class Main {

    // Constantes para configuração
    private static final int NUMERO_THREADS_REPOSICAO = 10;
    private static final int NUMERO_THREADS_RETIRADA = 5;
    private static final int QUANTIDADE_REPOSICAO = 10;
    private static final int QUANTIDADE_VENDA = 20;

    Scanner scanner = new Scanner(System.in);


    public static void main(String[] args) {
        Estoque estoque = new Estoque();
        estoque.lerDeArquivo();

    }

    public void menuPrincipal(Estoque estoque){
        System.out.println("\n===== Menu Principal =====");
        System.out.println("1. Gerenciar Produtos");
        System.out.println("2. Gerenciar Estoque");
        System.out.println("3. Relatórios e Histórico");
        System.out.println("4. Sair");
        System.out.print("Escolha uma opção: ");

        int opcao = scanner.nextInt();
        scanner.nextLine(); // Consumir a nova linha

        switch (opcao) {
            case 1:
                menuGerenciarProdutos(estoque);
                break;
            case 2:
                menuGerenciarEstoque(estoque);
                break;
            case 3:
                menuRelatoriosHistorico(estoque);
                break;
            case 4:
                System.out.println("Saindo do sistema...");
                break;
            default:
                System.out.println("Opção inválida, tente novamente.");
        }
    }

        public void menuGerenciarProdutos(Estoque estoque) {
            boolean executando = true;

            while (executando) {
                System.out.println("\n===== Gerenciar Produtos =====");
                System.out.println("1. Adicionar Produto");
                System.out.println("2. Editar Produto");
                System.out.println("3. Remover Produto");
                System.out.println("4. Voltar ao Menu Principal");
                System.out.print("Escolha uma opção: ");

                int opcao = scanner.nextInt();
                scanner.nextLine(); // Consumir a nova linha

                switch (opcao) {
                    case 1:
                        adicionarProduto(estoque);
                        break;
                    case 2:
                        editarProduto();
                        break;
                    case 3:
                        removerProduto();
                        break;
                    case 4:
                        executando = false;
                        break;
                    default:
                        System.out.println("Opção inválida, tente novamente.");
                }
            }
        }

    private void adicionarProduto(Estoque estoque) {
        System.out.println("Digite o nome do produto:");
        String nome = scanner.nextLine();
    }

    private void editarProduto() {
        System.out.println("Editar Produto - Em desenvolvimento...");
    }

    private void removerProduto() {
        System.out.println("Remover Produto - Em desenvolvimento...");
    }

        private void menuGerenciarEstoque(Estoque estoque) {
            boolean executando = true;

            while (executando) {
                System.out.println("\n===== Gerenciar Estoque =====");
                System.out.println("1. Repor Produto");
                System.out.println("2. Retirar Produto");
                System.out.println("3. Voltar ao Menu Principal");
                System.out.print("Escolha uma opção: ");

                int opcao = scanner.nextInt();
                scanner.nextLine(); // Consumir a nova linha

                switch (opcao) {
                    case 1:
                        reporProduto();
                        break;
                    case 2:
                        retirarProduto();
                        break;
                    case 3:
                        executando = false;
                        break;
                    default:
                        System.out.println("Opção inválida, tente novamente.");
                }
            }
        }


    private void reporProduto() {
        System.out.println("Repor Produto - Em desenvolvimento...");
    }

    private void retirarProduto() {
        System.out.println("Retirar Produto - Em desenvolvimento...");
    }


    private void menuRelatoriosHistorico(Estoque estoque) {
            boolean executando = true;

            while (executando) {
                System.out.println("\n===== Relatórios e Histórico =====");
                System.out.println("1. Gerar Relatório de Estoque");
                System.out.println("2. Visualizar Histórico");
                System.out.println("3. Voltar ao Menu Principal");
                System.out.print("Escolha uma opção: ");

                int opcao = scanner.nextInt();
                scanner.nextLine(); // Consumir a nova linha

                switch (opcao) {
                    case 1:
                        // gerarRelatorios();
                        break;
                    case 2:
                        // visualizarHistorico();
                        break;
                    case 3:
                        executando = false;
                        break;
                    default:
                        System.out.println("Opção inválida, tente novamente.");
                }
            }
        }

    }
