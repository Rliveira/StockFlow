import Beans.Estoque;
import Beans.Operacao;
import Beans.Produto;
import Repositorios.RepositorioOperacao;
import Threads.ThreadCalculoLucro;
import Threads.ThreadPrevisaoDemanda;


import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    // Constantes para configuração (por enquanto sem nada definido)
    private static final int QTD_THREADS_PREVISAO_DEMANDA = 3;
    private static final int QTD_THREADS_CALCULO_LUCRO = 2;

    private static Thread[][] matrizDeThreads;

    public static void main(String[] args) {
        Estoque estoque = Estoque.getInstancia();
        RepositorioOperacao repositorioOperacao = RepositorioOperacao.getInstancia();

        Thread[] threadsPrevisaoDemanda = new Thread[QTD_THREADS_PREVISAO_DEMANDA];
        Thread[] threadsCalculoLucro = new Thread[QTD_THREADS_CALCULO_LUCRO];

        //criação das threads de ThreadPrevisãoDemanda
        for (int i = 0; i < QTD_THREADS_PREVISAO_DEMANDA; i++){
            threadsPrevisaoDemanda[i] = new ThreadPrevisaoDemanda((i + 1) + ". T-PD" , repositorioOperacao);
        }

        //criação das threads de ThreadCalculoLucro
        for (int i = 0; i < QTD_THREADS_CALCULO_LUCRO; i++){
            threadsCalculoLucro[i] = new ThreadCalculoLucro((i + 1) + ". T-CL" , repositorioOperacao);
        }

        //todo: fazer a criação das outras threads aqui.

        matrizDeThreads = new Thread[2][]; // se for criar mais de 2 arrays de threads precisa atualizar esse valor 2 aqui
        matrizDeThreads[0] = threadsPrevisaoDemanda;
        matrizDeThreads[1] = threadsCalculoLucro;

        //todo: adicionar os outros arrays criados de threads na matriz de threads aqui

        menuPrincipal(estoque);
    }

    private static void menuPrincipal(Estoque estoque){
        Scanner scanner = new Scanner(System.in);

        System.out.println("\n===== Menu Principal =====");
        System.out.println("1 - Gerenciar Produtos");
        System.out.println("2 - Gerenciar Estoque");
        System.out.println("3 - Relatórios e estatísticas");
        System.out.println("4 - Operações");
        System.out.println("5 - Sair");
        System.out.print("Escolha uma opção: ");

        int opcao = scanner.nextInt();

        switch (opcao) {
            case 1:
                menuGerenciarProdutos(estoque);
                break;
            case 2:
                menuGerenciarEstoque(estoque);
                break;
            case 3:
                menuRelatoriosEEstatisticas(estoque, matrizDeThreads);
                break;
            case 4:
                RepositorioOperacao repositorioOperacao = RepositorioOperacao.getInstancia();
                menuOperacoes(estoque, repositorioOperacao);
                break;
            case 5:
                System.out.println("Saindo do sistema...");
                break;
            default:
                System.out.println("Opção inválida, tente novamente.");
        }
    }

    public static void menuGerenciarProdutos(Estoque estoque) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("\n===== Gerenciar Produtos =====");
        System.out.println("1 - Adicionar Produto");
        System.out.println("2 - Editar Produto");
        System.out.println("3 - Remover Produto");
        System.out.println("4 - Voltar ao Menu Principal");
        System.out.print("Escolha uma opção: ");

        int opcao = scanner.nextInt();

        switch (opcao) {
            case 1:
                adicionarProduto(estoque);
                menuGerenciarProdutos(estoque);
                break;

            case 2:
                editarProduto(estoque);
                menuGerenciarProdutos(estoque);
                break;

            case 3:
                removerProduto(estoque);
                menuGerenciarProdutos(estoque);
                break;

            case 4:
                menuPrincipal(estoque);
                break;

            default:
                System.out.println("Opção inválida, tente novamente.");
                menuGerenciarProdutos(estoque);
        }

    }

    private static void adicionarProduto(Estoque estoque) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Digite o nome do produto: ");
        String nome = scanner.nextLine();

        Produto produto = new Produto(nome);
        estoque.adicionarProduto(produto);
    }

    private static void editarProduto(Estoque estoque) {
        Scanner scanner = new Scanner(System.in);

        List<Produto> produtos = estoque.getProdutos();

        imprimirlistaProdutos(produtos);
        System.out.println("Digite o valor correspondente ao produto que você deseja editar: ");
        int indice = scanner.nextInt();

        Produto produtoSelecionado = buscarProdutoPorIndice(indice, produtos);

        if(produtoSelecionado == null){
            System.out.println("Digite um valor válido.");
        } else{
            System.out.println("Digite o novo nome do produto:");
            String novoNome = scanner.next();
            estoque.editarProduto(produtoSelecionado, novoNome);
        }
    }

    private static void removerProduto(Estoque estoque) {
        Scanner scanner = new Scanner(System.in);

        List<Produto> produtos = estoque.getProdutos();

        imprimirlistaProdutos(produtos);
        System.out.println("Digite o valor correspondente ao produto que você deseja remover: ");
        int indice = scanner.nextInt();

        Produto produtoSelecionado = buscarProdutoPorIndice(indice, produtos);

        if(produtoSelecionado == null){
            System.out.println("Digite um valor válido.");
        } else{
            estoque.removerProduto(produtoSelecionado);
        }
    }

    private static void menuGerenciarEstoque(Estoque estoque) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("\n===== Gerenciar Estoque =====");
        System.out.println("1 - Repor Produto");
        System.out.println("2 - Retirar Produto");
        System.out.println("3 - mostrar o estoque");
        System.out.println("4 - Voltar ao Menu Principal");
        System.out.print("Escolha uma opção: ");

        int opcao = scanner.nextInt();

        switch (opcao) {
            case 1:
                reporProduto(estoque);
                menuGerenciarEstoque(estoque);
                break;
            case 2:
                retirarProduto(estoque);
                menuGerenciarEstoque(estoque);

                break;
            case 3:
                //todo: chamar a função de imprimir o estoque aqui:

                menuGerenciarEstoque(estoque);
                break;
            case 5:
                menuPrincipal(estoque);
            default:
                System.out.println("Opção inválida, tente novamente.");
        }
    }

    private static void reporProduto(Estoque estoque) {
        Scanner scanner = new Scanner(System.in);
        List<Produto> produtos = estoque.getProdutos();

        imprimirlistaProdutos(produtos);
        System.out.println("Digite o valor correspondente ao produto que você deseja repor o estoque: ");
        int indice = scanner.nextInt();

        Produto produtoSelecionado = buscarProdutoPorIndice(indice, produtos);

        if(produtoSelecionado == null){
            System.out.println("Digite um valor válido.");
        } else{
            System.out.println("Digite a quantidade que você deseja repor: ");
            int quantidade = scanner.nextInt();
            System.out.println("Digite o preço unitário do produto reposto: ");
            double valorUnitario = scanner.nextDouble();

            estoque.repor(produtoSelecionado, quantidade, valorUnitario);
        }
    }

    private static void retirarProduto(Estoque estoque) {
        Scanner scanner = new Scanner(System.in);
        List<Produto> produtos = estoque.getProdutos();

        imprimirlistaProdutos(produtos);
        System.out.println("Digite o valor correspondente ao produto que você deseja repor o estoque: ");
        int indice = scanner.nextInt();

        Produto produtoSelecionado = buscarProdutoPorIndice(indice, produtos);

        if (produtoSelecionado == null) {
            System.out.println("Digite um valor válido.");
        } else {
            System.out.println("Digite a quantidade que você deseja retirar: ");
            int quantidade = scanner.nextInt();
            System.out.println("Digite o preço unitário do produto retirado: ");
            double valorUnitario = scanner.nextDouble();

            estoque.retirar(produtoSelecionado, quantidade, valorUnitario);
        }
    }

    public static void menuRelatoriosEEstatisticas(Estoque estoque, Thread[][] matrizDeThreads) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("\n===== Relatórios e estatísticas =====");
        System.out.println("1 - Prever demanda de reposição de todos os produtos.");
        System.out.println("2 - Prever demanda de retirada de todos os produtos.");
        System.out.println("3 - ");
        System.out.println("4 - Voltar ao menu principal");
        System.out.print("Escolha uma opção: ");

        int opcao = scanner.nextInt();

        Thread[] threadsPrevisaoDemanda = matrizDeThreads[0];
        Thread[] threadsCalculoLucro = matrizDeThreads[1];
        //todo: declarar os arrays de threads abaixo


        switch (opcao) {
            case 1:
                preverDemanadaDeProdutosComThreads("Reposição", threadsPrevisaoDemanda, estoque);
                menuRelatoriosEEstatisticas(estoque, matrizDeThreads);
                break;

            case 2:
                preverDemanadaDeProdutosComThreads("Retirada", threadsPrevisaoDemanda, estoque);
                menuRelatoriosEEstatisticas(estoque, matrizDeThreads);
                break;

            case 3:
                calcularLucroComThreads(threadsCalculoLucro, estoque);
                menuRelatoriosEEstatisticas(estoque, matrizDeThreads);
                break;

            case 4:
                menuPrincipal(estoque);
                break;

            default:
                System.out.println("Opção inválida, tente novamente.");
                menuGerenciarProdutos(estoque);
        }
    }

    public static void preverDemanadaDeProdutosComThreads(String tipoOperacao, Thread[] threads, Estoque estoque) {
        List<Produto> produtosCopia = new ArrayList<>(estoque.getProdutos()); // faz uma cópia da lista de produtos
        int numThreads = threads.length;

        //atualiza o tipo de operacão a ser realizada pelas threads
        for(int i = 0; i < numThreads - 1; i++){
            ((ThreadPrevisaoDemanda) threads[i]).setTipoOperacao(tipoOperacao);
        }

        //loop principal
        while (!produtosCopia.isEmpty()) {

            // loop para iniciar as threads com a lista de produtos
            for (int i = 0; i < numThreads && !produtosCopia.isEmpty(); i++) {
                Produto produto = produtosCopia.getFirst();
                produtosCopia.removeFirst();

                if (threads[i] instanceof ThreadPrevisaoDemanda) {
                    ((ThreadPrevisaoDemanda) threads[i]).setProduto(produto);
                }
                threads[i].start();
            }

            // Aguarda todas as threads terminarem de trabalhar para continuar o loop principal
            for (Thread thread : threads) {
                try {
                    thread.join();
                } catch (InterruptedException e) {
                    System.out.println("Erro ao aguardar a conclusão das threads: " + e.getMessage());
                }
            }
        }
    }

    public static void calcularLucroComThreads(Thread[] threads, Estoque estoque) {
        List<Produto> produtosCopia = new ArrayList<>(estoque.getProdutos()); // faz uma cópia da lista de produtos
        int numThreads = threads.length;

        //loop principal
        while (!produtosCopia.isEmpty()) {

            // loop para iniciar as threads com a lista de produtos
            for (int i = 0; i < numThreads && !produtosCopia.isEmpty(); i++) {
                Produto produto = produtosCopia.getFirst();
                produtosCopia.removeFirst();

                if (threads[i] instanceof ThreadCalculoLucro) {
                    ((ThreadCalculoLucro) threads[i]).setProduto(produto);
                }
                threads[i].start();
            }

            // Aguarda todas as threads terminarem de trabalhar para continuar o loop principal
            for (Thread thread : threads) {
                try {
                    thread.join();
                } catch (InterruptedException e) {
                    System.out.println("Erro ao aguardar a conclusão das threads: " + e.getMessage());
                }
            }
        }
    }

    private static void menuOperacoes(Estoque estoque, RepositorioOperacao repositorioOperacao) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("\n===== Operações e Histórico =====");
        System.out.println("1 - listar todas as reposições de produtos");
        System.out.println("2 - listar todas as retiradas de produtos");
        System.out.println("3 - listar todas operações de um produto ");
        System.out.println("4. Voltar ao Menu Principal");
        System.out.print("Escolha uma opção: ");

        int opcao = scanner.nextInt();

        switch (opcao) {
            case 1:
                List<Operacao> operacoesDeReposicao = repositorioOperacao.listarOperacoesPorTipo("Reposição");
                imprimirListaOperacoes(operacoesDeReposicao, estoque);
                menuOperacoes(estoque, repositorioOperacao);
                break;

            case 2:
                List<Operacao> operacoesDeRetirada = repositorioOperacao.listarOperacoesPorTipo("Retirada");
                imprimirListaOperacoes(operacoesDeRetirada, estoque);
                menuOperacoes(estoque, repositorioOperacao);
                break;

            case 3:
                List<Produto> produtos = estoque.getProdutos();
                imprimirlistaProdutos(produtos);

                System.out.println("Selecione o produto que você deseja ver todas as operações: ");
                int indice = scanner.nextInt();
                Produto produtoSelecionado = buscarProdutoPorIndice(indice, produtos);

                if(produtoSelecionado == null){
                    System.out.println("Selecione uma opção válida.");
                } else{
                    List<Operacao> operacoes = repositorioOperacao.listarOperacoesPorProduto(produtoSelecionado.getId());
                    imprimirListaOperacoes(operacoes, estoque);
                    menuOperacoes(estoque, repositorioOperacao);
                }
                break;
            case 4:
                menuPrincipal(estoque);
                break;
            default:
                System.out.println("Opção inválida, tente novamente.");
        }

    }

    private static void imprimirlistaProdutos(List<Produto> produtos){
        for(int i = 0; i < produtos.size(); i++){
            System.out.println(i + " - " + produtos.get(i).getNome());
        }
    }

    private static Produto buscarProdutoPorIndice(int indice, List<Produto> produtos){
        Produto produto = null;

        if(indice >= 0  && indice <= produtos.size() - 1) {
            produto = produtos.get(indice);
        }

        return produto;
    }

    private static void imprimirListaOperacoes(List<Operacao> operacoes, Estoque estoque){
        for (Operacao operacao : operacoes){
            Produto produto = estoque.buscarProduto(operacao.getIdProduto());

            System.out.println(
                    "ID: " + operacao.getIdOperacao() + '\n' +
                    "Tipo da operacao: " + operacao.getTipoOperacao() + '\n' +
                    "Produto: " + produto.getNome() + '\n' +
                    "Quantidade: " + operacao.getQuantidade() + '\n' +
                    "Data: " + operacao.getDataOperacao() + '\n' +
                    "Valor unitário: " + operacao.getValorUnitario() + '\n'
                    );
        }
    }
}
