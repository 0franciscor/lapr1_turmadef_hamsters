import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Projeto {

    static Scanner ler = new Scanner(System.in);

    public static void main(String[] args) throws FileNotFoundException {
        double populacaoInicial[] = tratamentoDados((leituraDados("hamsters.txt"/*args[0]*/, 0)));     //VETOR INICIAL
        double matrizLeslie[][] = new double [populacaoInicial.length][populacaoInicial.length];                           //DECLARAÇÃO MATRIZ LESLIE
        for(int i = 1; i<=2; i++){
            insercaoMatriz(matrizLeslie, leituraDados("hamsters.txt"/*args[0]*/, i), i);
        }

        System.out.println("Quais as gerações que pretende analisar?");
        double [] populaçõesEstimadas = new double[1000];
        double [] taxasDeVariação = new double[1000];
        int t=ler.nextInt(); // CALCULO DO NUMERO DE GERAÇOES
        int geração=-1;
        while (t>0){
            int u = t+1;
            geração=geração+1;
            dimensãoPopulação(matrizLeslie,populacaoInicial,t,populaçõesEstimadas,geração);
            dimensãoPopulação(matrizLeslie,populacaoInicial,u,populaçõesEstimadas,(geração+1));
            TaxaVariacao(populaçõesEstimadas,geração,taxasDeVariação);
            t=ler.nextInt();
        }
        for (int l=0; l<=geração;l++){
            System.out.printf("%.2f\n", populaçõesEstimadas[l]);
            System.out.printf("%.2f\n", taxasDeVariação[l]);
        }
    }

    public static String leituraDados(String nomeFicheiro, int numLinha) throws FileNotFoundException { //LEITURA EXCLUSIVA DO VETOR
        File ficheiro = new File(nomeFicheiro);
        Scanner leituraFicheiro = new Scanner(ficheiro);
        String input = "";
        for(int i = 0; i<=numLinha; i++)
            input = leituraFicheiro.nextLine();
        leituraFicheiro.close();

        return input;
    }

    public static double[] tratamentoDados(String input) {
        String[] dadosInseridos = input.split(", ");
        double [] valoresTratados = new double[dadosInseridos.length];

        for(int i = 0; i< dadosInseridos.length; i++) {
            String[] dadosInseridosTratados = dadosInseridos[i].split("=");
            double valorTratado = Double.parseDouble(dadosInseridosTratados[1]);
            valoresTratados[i] = valorTratado;
        }
        return valoresTratados;
    }

    public static double[][] insercaoMatriz (double[][] matrizLeslie, String input, int tipologia) throws FileNotFoundException { //LEITURA E INSERÇÃO DE DADOS NA MATRIZ DE LESLIE 0-SOBREVIVENCIA 1- FECUNDIDADE
        double[] valoresTratados = tratamentoDados(input);

        if (tipologia == 1) {
            int colunaCorrente = 0;
            for (int i = 1; i <= valoresTratados.length; i++) {
                matrizLeslie[i][colunaCorrente] = valoresTratados[i - 1];
                colunaCorrente++;
            }
        }
        else{
            for (int i = 0; i < valoresTratados.length; i++)
                matrizLeslie[0][i] = valoresTratados[i];
        }
        return matrizLeslie;
    }

    public static void dimensãoPopulação (double [][] matrizLeslie, double[] populacaoInicial, int t, double[] populaçõesEstimadas, int geração) throws FileNotFoundException{ //CALCULO DIMENSAO POPULACAO
        double Lesliemultiplicada[][] = new double[matrizLeslie.length][matrizLeslie.length];
        for(int i =0 ; i<matrizLeslie.length; i++){
            for(int j =0 ; j< matrizLeslie.length; j++){
                Lesliemultiplicada[i][j]=matrizLeslie[i][j];
            }
        }
        multiplicarmatrizes(matrizLeslie,Lesliemultiplicada,t);
        double total=0;
        int i,l;
        for (i=0;i<matrizLeslie.length;i++){
            double somas=0;
            for (l=0;l< matrizLeslie.length;l++){
                somas=somas+Lesliemultiplicada[i][l]*populacaoInicial[l];
            }
            total=total+somas;
        }
        populaçõesEstimadas[geração]=total;
    }

    public static void TaxaVariacao(double [] populaçõesEstimadas, int geração, double[] taxasDeVariação) {
        double variacao;
        variacao=populaçõesEstimadas[geração+1]/populaçõesEstimadas[geração];
        taxasDeVariação[geração]=variacao;
    }

    public static void multiplicarmatrizes (double [][] matriz1, double[][] matriz2, double t){
        double [] aux = new double [matriz1.length];
        int i,l,c,g;
        double soma;
        for (g=2;g<=t;g++){
            for (l=0;l<matriz1.length;l++){
                for (i=0;i<matriz1.length;i++){
                    soma=0;
                    for (c=0;c<matriz1.length;c++){
                        soma=soma+matriz2[l][c]*matriz1[c][i];
                    }
                    aux[i]=soma;
                }
                for (c=0;c<matriz1.length;c++){
                    matriz2[l][c]=aux[c];
                }
            }
        }
    }

}