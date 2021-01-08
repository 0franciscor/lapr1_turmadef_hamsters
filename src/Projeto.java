import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Projeto {

    static Scanner ler = new Scanner(System.in);

    public static void main(String[] args) throws FileNotFoundException {
        double populacaoInicial[] = tratamentoDados((leituraDados(args[0], 0)));     //VETOR INICIAL
        double matrizLeslie[][] = new double [populacaoInicial.length][populacaoInicial.length];                           //DECLARAÇÃO MATRIZ LESLIE
        for(int i = 1; i<=2; i++){
            insercaoMatriz(matrizLeslie, leituraDados(args[0], i), i);
        }

        System.out.println("Quais as gerações que pretende analisar?");
        int t=ler.nextInt();
        int geracao=-1;
        int [] geracoesEstimadas = new int [1000];
        double [] populacoesEstimadas = new double[1000];
        double [] taxasDeVariacao = new double[1000];
        double [][] distribuicaoNormalizada = new double[1000][matrizLeslie.length];
        double [][] Nt = new double[1000][matrizLeslie.length];

        while (t>0){
            int u = t+1;
            geracao=geracao+1;
            geracoesEstimadas [geracao]= t;
            dimensaoPopulacao(matrizLeslie,populacaoInicial,t,Nt,populacoesEstimadas,geracao,distribuicaoNormalizada);
            dimensaoPopulacao(matrizLeslie,populacaoInicial,u,Nt,populacoesEstimadas,(geracao+1),distribuicaoNormalizada);
            TaxaVariacao(populacoesEstimadas,geracao,taxasDeVariacao);
            t=ler.nextInt();
        }
        // colocar esta informação a ser imprimida num método
        for (int l=0; l<=geracao;l++){
            System.out.print("A população total na geração " + geracoesEstimadas[l] +" é ");
            System.out.printf("%.2f\n", populacoesEstimadas[l]);
            System.out.print("Sendo a taxa de variação nesta geração de ");
            System.out.printf("%.2f\n", taxasDeVariacao[l]);
            System.out.print("A distribuição da população é a seguinte: ");
            for (int j=0; j< matrizLeslie.length;j++){
                System.out.printf("%.2f ", Nt[l][j]);
            }
            System.out.println("");
            System.out.print("A distribuição normalizada da população é a seguinte: ");
            for (int j=0; j< matrizLeslie.length;j++){
                System.out.printf("%.3f ", distribuicaoNormalizada[l][j]);
            }
            System.out.println("");
            System.out.println("///////////////////////////// ");

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

    public static void dimensaoPopulacao (double [][] matrizLeslie, double[] populacaoInicial, int t, double[][] Nt,double[] populacoesEstimadas, int geracao, double[][] distribuicaoNormalizada) throws FileNotFoundException{ //CALCULO DIMENSAO POPULACAO
        // tentar criar esta nova matriz de outra maneira
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
            Nt[geracao][i]=somas;
        }
        for (i=0;i< matrizLeslie.length;i++){
            total=total+Nt[geracao][i];
        }
        populacoesEstimadas[geracao]=total;
        for (i=0;i< matrizLeslie.length;i++){
            distribuicaoNormalizada[geracao][i]=Nt[geracao][i]/populacoesEstimadas[geracao];
        }
    }

    public static void TaxaVariacao(double [] populacoesEstimadas, int geracao, double[] taxasDeVariacao) {
        double variacao;
        variacao=populacoesEstimadas[geracao+1]/populacoesEstimadas[geracao];
        taxasDeVariacao[geracao]=variacao;
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