import Beans.Estoque;
import Beans.Operacao;
import Beans.Produto;
import Repositorios.RepositorioOperacao;
import Threads.ThreadAlertaEstoqueBaixo;
import Threads.ThreadCalculoLucro;
import Threads.ThreadCalculoTempoEsgotamento;
import Threads.ThreadPrevisaoDemanda;


import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    // Constantes para configuração (por enquanto sem nada definido)
    private static final int QTD_THREADS_PREVISAO_DEMANDA = 3;
    private static final int QTD_THREADS_CALCULO_LUCRO = 2;
    private static final int QTD_THREADS_ESTOQUE_BAIXO = 1;
    private static final int  QTD_THREADS_CALCULO_ESGOTAMENTO = 1;

    private static Thread[][] matrizDeThreads;

    public static void main(String[] args) {
        Estoque estoque = Estoque.getInstancia();
        RepositorioOperacao repositorioOperacao = RepositorioOperacao.getInstancia();

        Thread[] threadsPrevisaoDemanda = new Thread[QTD_THREADS_PREVISAO_DEMANDA];
        Thread[] threadsCalculoLucro = new Thread[QTD_THREADS_CALCULO_LUCRO];
        Thread[] threadsAlertaEstoqueBaixo = new Thread[QTD_THREADS_ESTOQUE_BAIXO];
        Thread[] threadsCalculoEsgotamento = new Thread[QTD_THREADS_CALCULO_ESGOTAMENTO];

        //criação das threads de ThreadPrevisãoDemanda
        for (int i = 0; i < QTD_THREADS_PREVISAO_DEMANDA; i++){
            threadsPrevisaoDemanda[i] = new ThreadPrevisaoDemanda((i + 1) + ". T-PD" , repositorioOperacao);
        }

        //criação das threads de ThreadCalculoLucro
        for (int i = 0; i < QTD_THREADS_CALCULO_LUCRO; i++){
            threadsCalculoLucro[i] = new ThreadCalculoLucro((i + 1) + ". T-CL" , repositorioOperacao);
        }

        //criação das threads de ThreadAlertaEstoqueBaixo
        for (int i = 0; i < QTD_THREADS_ESTOQUE_BAIXO; i++){
            threadsAlertaEstoqueBaixo[i] = new ThreadAlertaEstoqueBaixo((i + 1) + ". T-EB" ,repositorioOperacao, estoque);
        }

        //criação das threads de ThreadCalculoEsgotamento
        for (int i = 0; i < QTD_THREADS_CALCULO_ESGOTAMENTO; i++){
            threadsAlertaEstoqueBaixo[i] = new ThreadCalculoTempoEsgotamento((i + 1) + ".T-CE", estoque, repositorioOperacao);
        }

        //todo: fazer a criação das outras threads aqui.

        matrizDeThreads = new Thread[4][]; // se for criar mais de 2 arrays de threads precisa atualizar esse valor 2 aqui
        matrizDeThreads[0] = threadsPrevisaoDemanda;
        matrizDeThreads[1] = threadsCalculoLucro;
        matrizDeThreads[2] = threadsAlertaEstoqueBaixo;
        matrizDeThreads[3] = threadsCalculoEsgotamento;

        //todo: adicionar os outros arrays criados de threads na matriz de threads aqui

        menuPrincipal(estoque);
    }

    private static void menuPrincipal(Estoque estoque)
    {
        Scanner scanner = new Scanner(System.in);

        System.out.println("+===================================================+");
        System.out.println("|                   Menu Principal                  |");
        System.out.println("+===================================================+");
        System.out.println("| 1 | Gerenciar Produtos                            |");
        System.out.println("| 2 | Gerenciar Estoque                             |");
        System.out.println("| 3 | Relatórios e Estatísticas                     |");
        System.out.println("| 4 | Operações                                     |");
        System.out.println("| 5 | Sair                                          |");
        System.out.println("+===================================================+");
        System.out.print("\nEscolha uma opção: ");

        int opcao = scanner.nextInt();

        switch (opcao)
        {
            case 1:
                menuGerenciarProdutos(estoque);
                break;
            case 2:
                menuGerenciarEstoque(estoque, RepositorioOperacao.getInstancia());
                break;
            case 3:
                menuRelatoriosEEstatisticas(estoque, matrizDeThreads);
                break;
            case 4:
                menuOperacoes(estoque, RepositorioOperacao.getInstancia());
                break;
            case 5:
                System.out.println("Saindo do sistema...");
                break;
            default:
                System.out.println("Opção inválida, tente novamente.");
        }
    }

    public static void menuGerenciarProdutos(Estoque estoque)
    {
        Scanner scanner = new Scanner(System.in);

        System.out.println("+===================================================+");
        System.out.println("|                Gerenciar Produtos                 |");
        System.out.println("+===================================================+");
        System.out.println("| 1 | Adicionar Produto                             |");
        System.out.println("| 2 | Editar Produto                                |");
        System.out.println("| 3 | Remover Produto                               |");
        System.out.println("| 4 | Voltar ao Menu Principal                      |");
        System.out.println("+===================================================+");
        System.out.print("\nEscolha uma opção: ");

        int opcao = scanner.nextInt();

        switch (opcao)
        {
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

    private static void editarProduto(Estoque estoque)
    {
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

    private static void removerProduto(Estoque estoque)
    {
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

    private static void menuGerenciarEstoque(Estoque estoque, RepositorioOperacao repositorioOperacao)
    {
        Scanner scanner = new Scanner(System.in);

        System.out.println("+===================================================+");
        System.out.println("|                 Gerenciar Estoque                 |");
        System.out.println("+===================================================+");
        System.out.println("| 1 | Repor Produto                                 |");
        System.out.println("| 2 | Retirar Produto                               |");
        System.out.println("| 3 | Mostrar o Estoque                             |");
        System.out.println("| 4 | Voltar ao Menu Principal                      |");
        System.out.println("+===================================================+");
        System.out.print("\nEscolha uma opção: ");

        int opcao = scanner.nextInt();

        switch (opcao)
        {
            case 1:
                reporProduto(estoque);
                menuGerenciarEstoque(estoque, RepositorioOperacao.getInstancia());
                break;

            case 2:
                retirarProduto(estoque, RepositorioOperacao.getInstancia());
                menuGerenciarEstoque(estoque, RepositorioOperacao.getInstancia());
                break;

            case 3:
                estoque.imprimirEstoque();
                menuGerenciarEstoque(estoque, RepositorioOperacao.getInstancia());
                break;

            case 4:
                menuPrincipal(estoque);
                break;

            default:
                System.out.println("Opção inválida, tente novamente.");
                menuGerenciarEstoque(estoque, RepositorioOperacao.getInstancia());
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

    private static void retirarProduto(Estoque estoque, RepositorioOperacao repositorioOperacao) {
        Scanner scanner = new Scanner(System.in);
        List<Produto> produtos = estoque.getProdutos();

        estoque.imprimirEstoque();
        System.out.println("\n\n");
        imprimirlistaProdutos(produtos);
        System.out.println("Digite o valor correspondente ao produto que você deseja retirar do estoque: ");
        int indice = scanner.nextInt();

        Produto produtoSelecionado = buscarProdutoPorIndice(indice, produtos);

        if (produtoSelecionado == null) {
            System.out.println("Digite um valor válido.");
        } else {
            // Buscar a última reposição para esse produto
            Operacao ultimaReposicao = buscarUltimaReposicao(produtoSelecionado);

            if (ultimaReposicao == null) {
                System.out.println("Nenhuma reposição foi realizada para este produto ainda.");
            } else {
                System.out.println("Digite a quantidade que você deseja retirar: ");
                int quantidade = scanner.nextInt();
                System.out.println("Digite o preço unitário do produto retirado: ");
                double valorUnitario = scanner.nextDouble();

                if (valorUnitario > ultimaReposicao.getValorUnitario()) {
                    estoque.retirar(produtoSelecionado, quantidade, valorUnitario);
                    verificarEstoqueBaixo(matrizDeThreads[2], estoque, repositorioOperacao);
                    System.out.println("Retirada realizada com sucesso.");
                } else {
                    System.out.println("Erro: o valor unitário inserido deve ser maior do que o valor unitário" +
                            " da última reposição (" +
                            ultimaReposicao.getValorUnitario() + ").");
                }
            }
        }
    }

    public static Operacao buscarUltimaReposicao(Produto produto)
    {
        List<Operacao> operacoes = RepositorioOperacao.getInstancia().listarOperacoesPorProduto(produto.getId());
        Operacao ultimaReposicao = null;

        // Percorrer as operações e verificar qual é a última reposição
        for (Operacao operacao : operacoes)
        {
            if (operacao.getTipoOperacao().equals("Reposição"))
            {
                if (ultimaReposicao == null || operacao.getDataOperacao().isAfter(ultimaReposicao.getDataOperacao()))
                {
                    ultimaReposicao = operacao;
                }
            }
        }
        return ultimaReposicao;
    }


    public static void menuRelatoriosEEstatisticas(Estoque estoque, Thread[][] matrizDeThreads) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("+===============================================================+");
        System.out.println("|                 Relatórios e Estatísticas                     |");
        System.out.println("+===============================================================+");
        System.out.println("| 1 | Prever demanda de reposição de todos os produtos.         |");
        System.out.println("| 2 | Prever demanda de retirada de todos os produtos.          |");
        System.out.println("| 3 | Calcular lucro total a partir das operações.              |");
        System.out.println("| 4 | Calcular tempo de esgotamento dos produtos.               |");
        System.out.println("| 5 | Voltar ao menu principal.                                 |");
        System.out.println("+===============================================================+");
        System.out.print("\nEscolha uma opção: ");

        int opcao = scanner.nextInt();

        Thread[] threadsPrevisaoDemanda = matrizDeThreads[0];
        Thread[] threadsCalculoLucro = matrizDeThreads[1];
        Thread[] threadsCalculoEsgotamento = matrizDeThreads[3]; // Adicione outros arrays de threads se necessário
        // Adicione outros arrays de threads se necessário

        switch (opcao)
        {
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
                calcularEsgotamento();
                menuRelatoriosEEstatisticas(estoque, matrizDeThreads);
                break;

            default:
                System.out.println("Opção inválida, tente novamente.");
                menuRelatoriosEEstatisticas(estoque, matrizDeThreads);
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

    private static void calcularEsgotamento() {
        for (Thread thread : matrizDeThreads[3]) {
            if (thread instanceof ThreadCalculoTempoEsgotamento) {
                thread.start();
                try {
                    thread.join();
                } catch (InterruptedException e) {
                    System.out.println("Erro ao aguardar conclusão da thread de esgotamento: " + e.getMessage());
                }
            }
        }
    }

    public static void verificarEstoqueBaixo(Thread[] threads, Estoque estoque, RepositorioOperacao repositorioOperacao) {
        threads[0].start();

        try {
            threads[0].join(); // Espera a thread terminar antes de continuar
        } catch (InterruptedException e) {
            System.out.println("Erro ao aguardar a conclusão da verificação de estoque: " + e.getMessage());
        }
    }



    private static void menuOperacoes(Estoque estoque, RepositorioOperacao repositorioOperacao)
    {
        Scanner scanner = new Scanner(System.in);

        System.out.println("+===============================================================+");
        System.out.println("|                Operações e Histórico                          |");
        System.out.println("+===============================================================+");
        System.out.println("| 1 | Listar todas as reposições de produtos                    |");
        System.out.println("| 2 | Listar todas as retiradas de produtos                     |");
        System.out.println("| 3 | Listar todas as operações de um produto                   |");
        System.out.println("| 4 | Voltar ao Menu Principal                                  |");
        System.out.println("+===============================================================+");
        System.out.print("\nEscolha uma opção: ");

        int opcao = scanner.nextInt();

        switch (opcao)
        {
            case 1:
                List<Operacao> operacoesDeReposicao = repositorioOperacao.listarOperacoesPorTipo("Reposição");
                imprimirListaOperacoes(operacoesDeReposicao);
                menuOperacoes(estoque, repositorioOperacao);
                break;

            case 2:
                List<Operacao> operacoesDeRetirada = repositorioOperacao.listarOperacoesPorTipo("Retirada");
                imprimirListaOperacoes(operacoesDeRetirada);
                menuOperacoes(estoque, repositorioOperacao);
                break;

            case 3:
                List<Produto> produtos = estoque.getProdutos();
                imprimirlistaProdutos(produtos);

                System.out.println("Selecione o produto que você deseja ver todas as operações: ");
                int indice = scanner.nextInt();
                Produto produtoSelecionado = buscarProdutoPorIndice(indice, produtos);

                if (produtoSelecionado == null)
                {
                    System.out.println("Selecione uma opção válida.");
                } else
                {
                    List<Operacao> operacoes = repositorioOperacao.listarOperacoesPorProduto(produtoSelecionado.getId());
                    imprimirListaOperacoes(operacoes);
                    menuOperacoes(estoque, repositorioOperacao);
                }
                break;

            case 4:
                menuPrincipal(estoque);
                break;

            default:
                System.out.println("Opção inválida, tente novamente.");
                menuOperacoes(estoque, repositorioOperacao);
        }
    }

    private static void imprimirlistaProdutos(List<Produto> produtos) {
        System.out.println("+------------------------------------------------------------+");
        for (int i = 0; i < produtos.size(); i++) {
            Produto produto = produtos.get(i);
            Operacao ultimaReposicao = buscarUltimaReposicao(produto);

            // Se não houver preço de compra ainda, então retorna "N/A"
            String precoUnitario = (ultimaReposicao != null) ? String.format("%.2f", ultimaReposicao.getValorUnitario()) : "N/A";

            System.out.printf("| %d - %-28s | Preço Unitário: %-7s |\n", i, produto.getNome(), precoUnitario);
        }
        System.out.println("+------------------------------------------------------------+");
    }


    private static Produto buscarProdutoPorIndice(int indice, List<Produto> produtos){
        Produto produto = null;

        if(indice >= 0  && indice <= produtos.size() - 1) {
            produto = produtos.get(indice);
        }

        return produto;
    }

    public static void imprimirListaOperacoes(List<Operacao> operacoes)
    {
        final int largura = 60;

        for (Operacao operacao : operacoes) 
        {
            String idOperacao = operacao.getIdOperacao().toString();
            String tipoOperacao = operacao.getTipoOperacao();
            String idProduto = operacao.getIdProduto().toString();
            String quantidade = String.valueOf(operacao.getQuantidade());
            String dataOperacao = operacao.getDataOperacao().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
            String valorUnitario = String.format("%.2f", operacao.getValorUnitario());
            String valorTotal = String.format("%.2f", operacao.getValorUnitario() * operacao.getQuantidade());

            System.out.println("+" + "-".repeat(largura - 2) + "+");
            System.out.printf("| %-17s : %-36s |\n", "ID da Operação", idOperacao);
            System.out.printf("| %-17s : %-36s |\n", "Tipo de Operação", tipoOperacao);
            System.out.printf("| %-17s : %-36s |\n", "ID do Produto", idProduto);
            System.out.printf("| %-17s : %-36s |\n", "Quantidade", quantidade);
            System.out.printf("| %-17s : %-36s |\n", "Data da Operação", dataOperacao);
            System.out.printf("| %-17s : %-36s |\n", "Valor Unitário", valorUnitario);
            System.out.printf("| %-17s : %-36s |\n", "Valor Total", valorTotal);
            System.out.println("+" + "-".repeat(largura - 2) + "+");
        }
    }

}
