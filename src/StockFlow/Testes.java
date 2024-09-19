package StockFlow;

import Beans.Estoque;
import Beans.Produto;
import Repositorios.RepositorioOperacao;
import Threads.ThreadCalculoFinanceiro;
import Threads.ThreadCalculoTempoEsgotamento;
import Threads.ThreadPrevisaoDemanda;
import com.sun.management.OperatingSystemMXBean;

import java.lang.management.ManagementFactory;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class Testes {
    private static List<Produto> listaProdutosTeste;
    private static Testes instanciaUnica;


    private static final int QTD_TESTES = 1;


    private Testes() {
        this.listaProdutosTeste = new ArrayList<>();
    }

    //SINGLETON:
    public static Testes getInstancia()
    {
        if (instanciaUnica == null) {
            instanciaUnica = new Testes();
        }
        return instanciaUnica;
    }


    public static void main(String[] args) {
        Testes testes = Testes.getInstancia();

        // Inicializar estoque e criar threads de previsão de demanda
        Estoque estoque = Estoque.getInstancia();
        RepositorioOperacao repositorioOperacao = RepositorioOperacao.getInstancia();

        // Métricas
        long tempoTotalPorExecucao = 0;
        long tempoTotalDoTeste = 0;
        double usoCpuInicialTotal = 0;
        double usoCpuFinalTotal = 0;

        Instant inicioTeste = Instant.now(); // Inicia um cronômetro com alta precisão (nanoTime)

        for (int teste = 1; teste <= QTD_TESTES; teste++) {

            System.out.println("iniciando teste " + teste );
            Instant inicio = Instant.now(); // Inicia um cronômetro com alta precisão (nanoTime)

            //Metodo analisado: calcular Financeiro
            //todo: se for mudar o tipo de thread a ser analisada mude o valor
            //  do caminho e atualize o método a ser chamado pelo main na linha 96.
            Thread[] threads = Main.criarThreads(estoque, repositorioOperacao, 3);
            Main.calcularEsgotamento(threads, estoque);

            // Parar cronômetro e calcular tempo de execução em segundos
            Instant fim = Instant.now();
            Duration duracao = Duration.between(inicio, fim);
            tempoTotalPorExecucao += duracao.toSeconds();

            // Capturar uso de CPU após o teste
            double usoCpuFinal = getUsoCpu();
            usoCpuFinalTotal += usoCpuFinal;
        }

        Instant fimTeste = Instant.now();
        Duration duracao = Duration.between(inicioTeste, fimTeste);
        tempoTotalDoTeste += duracao.toSeconds();

        int tamanhoListaProdutosEstoque = estoque.getProdutos().size();

        // Calcular a média de uso de memória e CPU
        double mediaUsoCpuInicial = usoCpuInicialTotal / QTD_TESTES;
        double mediaUsoCpuFinal = usoCpuFinalTotal / QTD_TESTES;

        // Calcular média de tempo de execução
        long mediaTempoExecucao = tempoTotalPorExecucao / QTD_TESTES;

        // Exibir as métricas uma única vez ao final
        System.out.println("Teste de desempenho finalizado.\n");
        System.out.println("----------------------------------------------------------------------------");
        System.out.println("Métricas de desempenho: \n" );
        System.out.println("Quantidade de threads utilizadas: " + Main.getQtd_threadsEsgotamento());
        System.out.println("Quantidade de testes realizados: " + QTD_TESTES);
        System.out.println("Média de tempo de execução: " + mediaTempoExecucao + " s");
        System.out.println("Tempo total do teste: " + tempoTotalDoTeste + " s");
        System.out.println("Uso de CPU (média inicial): " + mediaUsoCpuInicial + " %");
        System.out.printf("Uso de CPU (média final): %.02f %%\n", mediaUsoCpuFinal);

        System.out.println("----------------------------------------------------------------------------");
        System.out.println("Tamanho da lista de produtos: " + tamanhoListaProdutosEstoque);
        System.out.println("Quantidade de produtos analisados: " + QTD_TESTES * listaProdutosTeste.size());
    }

    private static double getUsoCpu() {
        // Obtém o MXBean do sistema operacional
        OperatingSystemMXBean osBean = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();

        // Uso da CPU como um valor percentual (0 a 100)
        double usoCpu = osBean.getProcessCpuLoad() * 100;

        // Retorna o uso de CPU arredondado a 2 casas decimais
        return Math.round(usoCpu * 100.0) / 100.0;
    }


   public List<Produto> getListaProdutosTeste() {
        return listaProdutosTeste;
    }

    public void setListaProdutosTeste(List<Produto> listaProdutosTeste) {
        this.listaProdutosTeste = listaProdutosTeste;
    }
}