import Beans.Produto;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ScriptCadastroProdutos {
    private static final String[] NOMES_BASE = {
            "Abacaxi", "Limão", "Molho", "Uva", "Laranja", "Maçã", "Banana", "Manga", "Cenoura", "Batata",
            "Tomate", "Pepino", "Alface", "Espinafre", "Brócolis", "Rúcula", "Couve", "Abobrinha", "Berinjela", "Pimentão",
            "Batata-doce", "Chuchu", "Milho", "Cebola", "Alho", "Gengibre", "Pimenta", "Cebola-roxa", "Salsão", "Rabanete",
            "Acelga", "Nabo", "Jiló", "Lentilha", "Feijão", "Ervilha", "Grão-de-bico", "Quinoa", "Arroz", "Macarrão",
            "Farinha", "Açúcar", "Óleo", "Sal", "Pimenta-do-reino", "Canela", "Cravo", "Cominho", "Alcaravia", "Noz-moscada",
            "Alecrim", "Manjericão", "Orégano", "Tomilho", "Salsinha", "Cebolinha", "Hortelã", "Coentro", "Endívia", "Rúcula",
            "Manteiga", "Creme de leite", "Leite", "Iogurte", "Queijo", "Presunto", "Peito de frango", "Carne moída", "Linguiça", "Bacon",
            "Pão", "Manteiga de amendoim", "Mel", "Chá", "Café", "Suco de laranja", "Refrigerante", "Vinho", "Cerveja", "Cerveja artesanal",
            "Cachaça", "Vodka", "Rum", "Whisky", "Champanhe", "Tapioca", "Aveia", "Granola", "Iogurte grego", "Sorvete",
            "Chocolate", "Biscoito", "Bolacha", "Pipoca", "Batata frita", "Amendoim", "Castanha", "Nozes", "Amêndoas", "Pistache",
            "Coco", "Cabelos", "Xampu", "Condicionador", "Sabonete", "Creme hidratante", "Desodorante", "Perfume", "Papel higiênico",
            "Detergente", "Limpador multiuso", "Esponja", "Panela", "Frigideira", "Talheres", "Pratos", "Copos", "Tigelas",
            "Café em grão", "Café solúvel", "Chá de camomila", "Chá verde", "Chá preto", "Chá de hibisco", "Chá de hortelã", "Chá de gengibre", "Chá de erva-doce", "Chá de capim-limão",
            "Açúcar mascavo", "Açúcar demerara", "Açúcar cristal", "Açúcar de confeiteiro", "Mel de abelha", "Mel de flor", "Pasta de amendoim", "Pasta de caju", "Pasta de avelã", "Geleia de morango",
            "Geleia de uva", "Geleia de framboesa", "Geleia de laranja", "Geleia de pêssego", "Molho de tomate", "Molho de soja", "Molho barbecue", "Molho de pimenta", "Molho de alho", "Vinagre balsâmico",
            "Vinagre de maçã", "Vinagre de vinho tinto", "Vinagre de arroz", "Creme de milho", "Creme de cogumelos", "Creme de tomate", "Caldo de galinha", "Caldo de carne", "Caldo de legumes", "Caldo de peixe",
            "Pasta de tomate", "Pasta de alho", "Pasta de pimenta", "Sopa de abóbora", "Sopa de lentilhas", "Sopa de legumes", "Sopa de cebola", "Sopa de ervilha", "Sopa de batata", "Sopa de frango",
            "Cereal matinal", "Granola com frutas", "Granola com castanhas", "Granola com chocolate", "Granola com mel", "Bolacha de leite", "Bolacha integral", "Bolacha recheada", "Bolacha de chocolate", "Bolacha de água e sal",
            "Biscoito de polvilho", "Biscoito amanteigado", "Biscoito de queijo", "Biscoito de chocolate", "Biscoito de maizena", "Pipoca doce", "Pipoca salgada", "Pipoca com caramelo", "Pipoca com queijo", "Pipoca com chocolate",
            "Amendoim torrado", "Amendoim salgado", "Amendoim com casca", "Castanha de caju", "Castanha-do-pará", "Noz pecã", "Noz de macadâmia", "Noz de castanha", "Amêndoa crua", "Amêndoa torrada",
            "Pistache salgado", "Pistache doce", "Coco ralado", "Coco fresco", "Coco em pedaços", "Cabelos de milho", "Cabelos de milho para canjica", "Xampu anticaspa", "Xampu hidratante", "Xampu para cabelos oleosos", "Xampu para cabelos secos",
            "Condicionador anti-frizz", "Condicionador reparador", "Condicionador volumizador", "Condicionador para cabelos coloridos", "Sabonete líquido", "Sabonete em barra", "Sabonete esfoliante", "Sabonete antibacteriano", "Sabonete com fragrância", "Creme hidratante para o corpo",
            "Creme anti-idade", "Creme para mãos", "Creme para pés", "Desodorante roll-on", "Desodorante aerosol", "Desodorante em creme", "Desodorante natural", "Perfume floral", "Perfume amadeirado", "Perfume cítrico",
            "Papel toalha", "Papel alumínio", "Papel filme", "Papel para embrulho", "Papel para limpeza", "Detergente neutro", "Detergente de limão", "Detergente para louça", "Limpador para vidros", "Limpador de superfícies",
            "Esponja macia", "Esponja para limpeza pesada", "Panela de pressão", "Panela antiaderente", "Panela de ferro", "Panela de cerâmica", "Frigideira de alumínio", "Frigideira de ferro", "Frigideira antiaderente", "Talheres de inox",
            "Pratos de porcelana", "Pratos de plástico", "Copos de vidro", "Copos de plástico", "Tigelas de cerâmica", "Tigelas de vidro", "Tigelas de plástico", "Tigelas de inox", "Potes de vidro", "Potes de plástico",
            "Potes herméticos", "Potes para temperos", "Potes para alimentos", "Forma de bolo", "Forma de torta", "Forma de pudim", "Forma de cupcake", "Forma de pão", "Assadeira", "Rolo de massa", "Corta-massa",
            "Tábua de cortar", "Descascador", "Ralador", "Faca de chef", "Faca de pão", "Faca de legumes", "Tesoura de cozinha", "Pinça de cozinha", "Escorredor de macarrão", "Escorredor de arroz",
            "Jogo de panelas", "Jogo de talheres", "Jogo de pratos", "Jogo de copos", "Jogo de tigelas", "Jogo de utensílios de cozinha", "Ralador de queijo", "Liquidificador", "Processador de alimentos", "Batedeira",
            "Espremedor de frutas", "Centrífuga", "Panela elétrica", "Cafeteira", "Chaleira elétrica", "Moinho de café", "Torradeira", "Sanduicheira", "Grill", "Assador",
            "Pipoca de micro-ondas", "Pipoca de cinema", "Pipoca gourmet", "Pipoca com manteiga", "Pipoca com caramelo", "Pipoca com cheddar", "Amendoim doce", "Amendoim caramelado", "Amendoim com mel", "Castanha doce",
            "Castanha salgada", "Noz caramelada", "Noz salgada", "Amêndoa com mel", "Amêndoa torrada e salgada", "Pistache doce e salgado", "Pistache com caramelo", "Coco fresco ralado", "Coco em pedaços doce", "Cabelos de milho para curau",
            "Cabelos de milho para canjica", "Xampu para cabelos coloridos", "Xampu anti-frizz", "Xampu fortificante", "Xampu para cabelos lisos", "Condicionador para cabelos crespos", "Condicionador hidratante", "Condicionador anti-frizz", "Sabonete para pele sensível",
            "Sabonete com extrato de aloe vera", "Sabonete com vitamina E", "Sabonete com óleo de coco", "Creme anti-estrias", "Creme para rugas", "Creme para acne", "Desodorante sem álcool", "Desodorante com fragrância floral", "Desodorante com fragrância cítrica", "Desodorante com fragrância amadeirada",
            "Perfume com notas de baunilha", "Perfume com notas de sândalo", "Perfume com notas de jasmin", "Perfume com notas de rosa", "Perfume com notas de lavanda", "Papel toalha absorvente", "Papel toalha decorado", "Papel alumínio para churrasco", "Papel alumínio para cozinha", "Papel filme para alimentos",
            "Papel para limpeza", "Detergente para limpeza pesada", "Detergente para limpeza leve", "Limpador de inox", "Limpador de madeira", "Limpador de mármore", "Esponja macia", "Esponja para limpeza pesada", "Esponja para limpeza leve", "Panela de aço inox",
            "Panela de alumínio", "Panela de cobre", "Panela de ferro fundido", "Frigideira de cobre", "Frigideira de cerâmica", "Frigideira de aço inox", "Talheres de plástico", "Pratos descartáveis", "Copos descartáveis", "Tigelas descartáveis", "Potes descartáveis",
            "Forma de silicone", "Forma de vidro", "Forma de metal", "Forma de alumínio", "Forma de cerâmica", "Assadeira antiaderente", "Assadeira de vidro", "Assadeira de metal", "Assadeira de alumínio", "Rolo de massa para pizza", "Rolo de massa para bolos", "Rolo de massa para tortas", "Tábua de madeira",
            "Tábua de plástico", "Tábua de vidro", "Descascador de legumes", "Descascador de frutas", "Ralador de legumes", "Ralador de queijo", "Faca de chef profissional", "Faca de cozinha", "Faca de cortar carnes", "Tesoura para alimentos", "Pinça para alimentos",
            "Escorredor de frutas", "Escorredor de legumes", "Jogo de panelas de alumínio", "Jogo de panelas de inox", "Jogo de panelas de cerâmica", "Jogo de talheres de inox", "Jogo de talheres de plástico", "Jogo de pratos de porcelana", "Jogo de pratos de plástico", "Jogo de copos de vidro", "Jogo de copos de plástico",
            "Jogo de tigelas de vidro", "Jogo de tigelas de plástico", "Jogo de potes herméticos", "Jogo de potes para alimentos", "Jogo de formas de bolo", "Jogo de formas de torta", "Jogo de formas de pudim", "Jogo de formas de cupcake", "Jogo de formas de pão", "Jogo de assadeiras", "Jogo de rolos de massa",
            "Jogo de tábuas de cortar", "Jogo de descascadores", "Jogo de raladores", "Jogo de facas", "Jogo de tesouras", "Jogo de pinças", "Jogo de escorredores", "Jogo de potes de vidro", "Jogo de potes de plástico", "Jogo de potes herméticos"
    };

    private static final int QUANTIDADE_MAXIMA = 100;

    public static void main(String[] args) {
        List<Produto> produtos = gerarProdutos(20000); // Gera 10.000 produtos
        salvarProdutosEmArquivo(produtos, "Estoque20000.txt");
    }

    private static List<Produto> gerarProdutos(int quantidade) {
        List<Produto> produtos = new ArrayList<>();
        Random random = new Random();

        for (int i = 0; i < quantidade; i++) {
            String nome = gerarNomeProduto();
            int quantidadeProduto = 0;
            Produto produto = new Produto(nome);
            produto.setQuantidade(quantidadeProduto);
            produtos.add(produto);
        }

        return produtos;
    }

    private static String gerarNomeProduto() {
        Random random = new Random();
        String base = NOMES_BASE[random.nextInt(NOMES_BASE.length)];
        String modificador = "";
        if (random.nextBoolean()) {
            modificador = " " + NOMES_BASE[random.nextInt(NOMES_BASE.length)];
        }
        return base + modificador;
    }

    private static void salvarProdutosEmArquivo(List<Produto> produtos, String nomeArquivo) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(nomeArquivo))) {
            for (Produto produto : produtos) {
                writer.write(produto.getId() + "," + produto.getNome() + "," + produto.getQuantidade());
                writer.newLine();
            }
            System.out.println("Produtos salvos com sucesso em " + nomeArquivo);
        } catch (IOException e) {
            System.out.println("Erro ao salvar os produtos: " + e.getMessage());
        }
    }
}
