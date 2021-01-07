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

        System.out.println("Qual é o número de gerações que pretende calcular?");
        double t=ler.nextDouble(); // CALCULO DO NUMERO DE GERAÇOES
        double populaçãoTotal;
        double [] Nt = new double [matrizLeslie.length];
        while (t>0){
            populaçãoTotal=dimensãoPopulação(matrizLeslie,populacaoInicial,Nt,t);
            imprimir(Nt,populaçãoTotal);
            double u = t+1;
            double populacaoTotalMais1;
            populacaoTotalMais1 = dimensãoPopulação(matrizLeslie,populacaoInicial,Nt,u);
            double variacao;
            variacao = TaxaVariacao(populaçãoTotal, populacaoTotalMais1);
            imprimir(variacao);// vai ser para guardar num vetor e não imprimir logo
            t=ler.nextDouble();
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

    public static double dimensãoPopulação (double [][] matrizLeslie, double[] populacaoInicial, double [] Nt, double t) throws FileNotFoundException{ //CALCULO DIMENSAO POPULACAO
        double novaLeslie[][] = new double[matrizLeslie.length][matrizLeslie.length];
        for(int i =0 ; i<matrizLeslie.length; i++){
            for(int j =0 ; j< matrizLeslie.length; j++){
                novaLeslie[i][j]=matrizLeslie[i][j];
            }
        }
        double [] aux = new double [matrizLeslie.length];
        int n= matrizLeslie.length;
        int i,l,c,g;
        double total=0,soma;
        for (g=2;g<=t;g++){
            for (l=0;l<n;l++){
                for (i=0;i<n;i++){
                    soma=0;
                    for (c=0;c<n;c++){
                        soma=soma+novaLeslie[l][c]*matrizLeslie[c][i];
                    }
                    aux[i]=soma;
                }
                for (c=0;c<n;c++){
                    novaLeslie[l][c]=aux[c];
                }
            }
        }
        for (i=0;i<n;i++){
            double somas=0;
            for (l=0;l<n;l++){
                somas=somas+novaLeslie[i][l]*populacaoInicial[l];
            }
            Nt[i]=somas;
        }
        total=0;
        for (i=0;i<n;i++){
            total=total+Nt[i];
        }
        return total;
    }

    public static void imprimir(double [] Nt, double total){
        int  l;
        System.out.printf("%.2f\n",total);
        for(l=0;l<4;l++){
            System.out.printf("%.2f\n", Nt[l]);
        }
    }
    public static double TaxaVariacao(double populacaoT, double populacaoTMAIS1) {
        double variacao;
        variacao = populacaoTMAIS1 / populacaoT;

        return variacao;
    }
    public static void imprimir(double variacao) {
        System.out.printf("%.2f%%\n", variacao);
    }

}