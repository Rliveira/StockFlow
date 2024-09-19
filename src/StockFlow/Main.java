package StockFlow;

import Beans.Estoque;
import Beans.Operacao;
import Beans.Produto;
import Repositorios.RepositorioOperacao;
import Threads.*;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static java.lang.Thread.sleep;

public class Main {

    // Constantes para configuração (por enquanto sem nada definido)
    private static final int QTD_THREADS_PREVISAO_DEMANDA = 3;
    private static final int QTD_THREADS_CALCULO_FINANCEIRO = 2;
    private static final int QTD_THREADS_CALCULO_ESGOTAMENTO = 1;

    public static void main(String[] args) {
        Estoque estoque = Estoque.getInstancia();
        RepositorioOperacao repositorioOperacao = RepositorioOperacao.getInstancia();

        menuPrincipal(estoque, repositorioOperacao);
    }

    private static void menuPrincipal(Estoque estoque, RepositorioOperacao repositorioOperacao)
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
                menuGerenciarEstoque(estoque, repositorioOperacao);
                break;

            case 3:
                menuRelatoriosEEstatisticas(estoque, repositorioOperacao);
                break;

            case 4:
                menuOperacoes(estoque, repositorioOperacao);
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
                menuPrincipal(estoque, RepositorioOperacao.getInstancia());
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
                menuGerenciarEstoque(estoque, repositorioOperacao);
                break;

            case 2:
                retirarProduto(estoque, RepositorioOperacao.getInstancia());
                menuGerenciarEstoque(estoque, repositorioOperacao);
                break;

            case 3:
                estoque.imprimirEstoque();
                menuGerenciarEstoque(estoque, repositorioOperacao);
                break;

            case 4:
                menuPrincipal(estoque, repositorioOperacao);
                break;

            default:
                System.out.println("Opção inválida, tente novamente.");
                menuGerenciarEstoque(estoque, repositorioOperacao);
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
                    verificarEstoqueBaixo(estoque, repositorioOperacao);
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

    public static void verificarEstoqueBaixo(Estoque estoque, RepositorioOperacao repositorioOperacao) {
        List<Produto> produtos = estoque.getProdutos();

        for (Produto produto : produtos)
        {
            double limiteEstoqueBaixo = repositorioOperacao.calcularLimiteEstoqueBaixo(produto);
            boolean opcaoInvalida = false;
            do {
                if (produto.getQuantidade() < limiteEstoqueBaixo) {
                    System.out.println("\nAlerta! O estoque do produto " + produto.getNome() + " está baixo: " + produto.getQuantidade() + " unidades.");
                    System.out.printf("O limite considerado baixo para este produto é: %.2f unidades.\n", limiteEstoqueBaixo);
                    System.out.print("Deseja repor o estoque deste produto?");
                    System.out.print("1. Sim");
                    System.out.print("2. Não");
                    System.out.println("Escolha uma opção: ");
                    Scanner scanner = new Scanner(System.in);
                    int resposta = scanner.nextInt();

                    switch (resposta){
                        case 1:
                            System.out.print("Digite a quantidade para repor: ");
                            int quantidadeRepor = scanner.nextInt();
                            scanner.nextLine();
                            System.out.print("Digite o valor unitário: ");
                            double valorUnitario = scanner.nextDouble();
                            scanner.nextLine();

                            //Reposição do estoque
                            estoque.repor(produto, quantidadeRepor, valorUnitario);
                            System.out.println("Estoque do produto '" + produto.getNome() + "' foi reposto com sucesso.");
                            break;
                        case 2:
                            //não faz nada
                            break;
                        default:
                            opcaoInvalida = true;
                    }
                }
            } while(opcaoInvalida);
        }
    }

    public static void menuRelatoriosEEstatisticas(Estoque estoque, RepositorioOperacao repositorioOperacao) {
        Scanner scanner = new Scanner(System.in);
        Thread[] threads = null;

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

        switch (opcao)
        {
            case 1:
                threads = criarThreads(estoque, repositorioOperacao, 1);
                preverDemanadaDeProdutosComThreads("Reposição", threads, estoque);
                menuRelatoriosEEstatisticas(estoque, repositorioOperacao);
                break;

            case 2:
                threads = criarThreads(estoque, repositorioOperacao, 1);
                preverDemanadaDeProdutosComThreads("Retirada", threads, estoque);
                menuRelatoriosEEstatisticas(estoque, repositorioOperacao);
                break;

            case 3:
                threads = criarThreads(estoque, repositorioOperacao, 2);
                calcularFinanceiroComThreads(threads, estoque);
                menuRelatoriosEEstatisticas(estoque , repositorioOperacao);
                break;

            case 4:
                threads = criarThreads(estoque, repositorioOperacao, 3);
                calcularEsgotamento(threads, estoque);
                menuRelatoriosEEstatisticas(estoque, repositorioOperacao);
                break;
            case 5:
                menuPrincipal(estoque, repositorioOperacao);
                break;

            default:
                System.out.println("Opção inválida, tente novamente.");
                menuRelatoriosEEstatisticas(estoque, repositorioOperacao);
        }
    }

    private static boolean jafoiCriadoThreadDemanda = false;
    private static boolean jafoiCriadoThreadFinanceiro = false;
    private static boolean jaFoiCriadoThreadEsgotamento = false;

    private static Thread[] threadsEsgotamento = new Thread[QTD_THREADS_CALCULO_ESGOTAMENTO];
    private static Thread[] threadsDemanda = new Thread[QTD_THREADS_PREVISAO_DEMANDA];
    private static Thread[] threadsFinanceiro = new Thread[QTD_THREADS_CALCULO_FINANCEIRO];


    public static Thread[] criarThreads(Estoque estoque, RepositorioOperacao repositorioOperacao, int caminho){
        Thread[] threads = null;

        switch (caminho){
            case 1:
                threads = new Thread[QTD_THREADS_PREVISAO_DEMANDA];

                if(!jafoiCriadoThreadDemanda){
                    //criação das threads
                    for (int i = 0; i < QTD_THREADS_PREVISAO_DEMANDA; i++) {
                        threads[i] = new ThreadPrevisaoDemanda((i + 1) + "- TPD" , repositorioOperacao);
                        threadsDemanda[i] = threads[i];
                    }
                    //chama o start para elas ficarem em estado de espera.
                    for (int i = 0; i < QTD_THREADS_PREVISAO_DEMANDA; i++) {
                        threads[i].start();
                    }
                    jafoiCriadoThreadDemanda = true; //flag para evitar a recriação das mesmas threads no futuro;
                }
                else{
                    for(int i = 0; i < threadsDemanda.length; i++){
                        threads[i] = threadsDemanda[i]; //copía as referências das threads criadas.
                    }
                }
                break;

            case 2:
                threads = new Thread[QTD_THREADS_CALCULO_FINANCEIRO];

                if(!jafoiCriadoThreadFinanceiro){
                    //criação das threads
                    for (int i = 0; i < QTD_THREADS_CALCULO_FINANCEIRO; i++) {
                        threads[i] = new ThreadCalculoFinanceiro((i + 1) + "- TCF" , repositorioOperacao);
                        threadsFinanceiro[i] = threads[i];
                    }
                    //chama o start para elas ficarem em estado de espera.
                    for (int i = 0; i < QTD_THREADS_CALCULO_FINANCEIRO; i++) {
                        threads[i].start();
                    }
                    jafoiCriadoThreadFinanceiro = true; //seta flag para evitar a recriação das mesmas threads no futuro;
                }
                else{
                    for(int i = 0; i < threadsFinanceiro.length; i++){
                        threads[i] = threadsFinanceiro[i]; //copía as referências das threads criadas.
                    }
                }
                break;

            case 3:
                threads = new Thread[QTD_THREADS_CALCULO_ESGOTAMENTO];

                if(!jaFoiCriadoThreadEsgotamento)
                {
                    for(int i = 0; i < QTD_THREADS_CALCULO_ESGOTAMENTO; i++)
                    {
                        threads[i] = new ThreadCalculoTempoEsgotamento((i + 1) + "-TCE", estoque, repositorioOperacao);
                        threadsEsgotamento[i] = threads[i];
                    }

                    for(int i =0; i< QTD_THREADS_CALCULO_ESGOTAMENTO; i++)
                    {
                        threads[i].start();
                    }
                }
                else
                {
                    for(int i = 0; i < QTD_THREADS_CALCULO_ESGOTAMENTO; i++)
                    {
                        threads[i] = threadsEsgotamento[i];
                    }
                }
                break;
        }
        return threads;
    }

    public static void preverDemanadaDeProdutosComThreads(String tipoOperacao, Thread[] threads, Estoque estoque) {
        List<Produto> produtosCopia = new ArrayList<>(estoque.getProdutos()); // faz uma cópia da lista de produtos
        int numThreads = threads.length;

        //atualiza o tipo de operacão a ser realizada pelas threads
        for(int i = 0; i < numThreads; i++){
            ((ThreadPrevisaoDemanda) threads[i]).setTipoOperacao(tipoOperacao);
        }

        if(!produtosCopia.isEmpty()){
            processarThreads(produtosCopia, numThreads, threads);
        } else{
            System.out.println("Não tem nenhum produto cadastrado");
        }
    }

    public static void calcularFinanceiroComThreads(Thread[] threads, Estoque estoque) {
        List<Produto> produtosCopia = new ArrayList<>(estoque.getProdutos()); // faz uma cópia da lista de produtos
        int numThreads = threads.length;

        if(!produtosCopia.isEmpty()){
            processarThreads(produtosCopia, numThreads, threads);
        } else{
            System.out.println("Não tem nenhum produto cadastrado");
        }
    }

    public static void calcularEsgotamento(Thread[] threads, Estoque estoque)
    {
        List<Produto> produtosCopia = new ArrayList<>(estoque.getProdutos()); // faz uma cópia da lista de produtos
        int numThreads = threads.length;

        if(!produtosCopia.isEmpty()){
            processarThreads(produtosCopia, numThreads, threads);
        } else{
            System.out.println("Não tem nenhum produto cadastrado");
        }
    }

    private static void processarThreads(List<Produto> produtosCopia, int numThreads, Thread[] threads){
        // loop principal
        while (!produtosCopia.isEmpty()) {

            // loop para atribuir as threads com a lista de produtos
            for (int i = 0; i < numThreads && !produtosCopia.isEmpty(); i++) {
                Produto produto = produtosCopia.getFirst();
                produtosCopia.removeFirst();

                synchronized (threads[i]) {
                    ((ThreadProcessamento) threads[i]).setProduto(produto); //cast para a interface
                    threads[i].notify(); // Notifica a thread para começar a processar o produto
                }
            }

            // Aguarda um tempo para todas as threads processarem, para continuar com o loop principal
            try {
                sleep((long) (Math.random() * 100) + 5);
            } catch (InterruptedException ie) {//excessão inutil
            }
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
                menuPrincipal(estoque, repositorioOperacao);
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

    //funções auxiliares para a classe testes
    public static int getQtd_threadsEsgotamento(){
        return QTD_THREADS_CALCULO_ESGOTAMENTO;
    }

    public static int getQtd_threadsFinanceiro(){
        return QTD_THREADS_CALCULO_FINANCEIRO;
    }

    public static int getQtd_threadsPrevisaoDemanda(){
        return QTD_THREADS_PREVISAO_DEMANDA;
    }
}
