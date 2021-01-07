import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Projeto {

    static Scanner ler = new Scanner(System.in);

    public static void main(String[] args) throws FileNotFoundException {
        int populacaoInicial[] = leituraVetor("hamsters.txt"/*args[0]*/);
        double matrizLeslie[][] = leituraMatriz(populacaoInicial, "hamsters.txt"/*args[0]*/);

        System.out.println("Qual é o número de gerações que pretende calcular?");
        int t=ler.nextInt(); // CALCULO DO NUMERO DE GERAÇOES

        double populaçãoTotal;
        double [] Nt = new double [matrizLeslie.length];
        populaçãoTotal=dimensãoPopulação(matrizLeslie,populacaoInicial,Nt,t);
        imprimir(Nt,populaçãoTotal);

        int u;
        u = t+1;
        double populacaoTotalMais1;
        populacaoTotalMais1 = dimensãoPopulação(matrizLeslie,populacaoInicial,Nt,u);
        double variacao;
        variacao = TaxaVariacao(populaçãoTotal, populacaoTotalMais1);
        imprimir(variacao);

    }

    public static int[] leituraVetor(String nomeFicheiro) throws FileNotFoundException { //LEITURA EXCLUSIVA DO VETOR
        File ficheiro = new File(nomeFicheiro);
        Scanner leituraFicheiro = new Scanner(ficheiro);
       //while (leituraFicheiro.hasNext()) {
            String input = leituraFicheiro.nextLine();
            String[] dadosInseridos = input.split(", ");

        //}
        leituraFicheiro.close();
        return insercaoVetor(dadosInseridos);
    }

    public static int[] insercaoVetor(String[] dadosInseridos) { //INSERÇÃO DA POPULAÇAO ATUAL NO VETOR
        int[] populacaoInicial = new int[dadosInseridos.length];
            for (int i = 0; i < dadosInseridos.length; i++) {
                String valorIdades = "";
                for (int j = 4; j <= ((String.valueOf(dadosInseridos[i])).length() - 1); j++) {
                    valorIdades += String.valueOf(dadosInseridos[i].charAt(j));
                }
                populacaoInicial[i] = Integer.valueOf(valorIdades);
            }
        return populacaoInicial;
    }

    public static double[][] leituraMatriz (int[]dadosInseridos2, String nomeFicheiro) throws FileNotFoundException { //LEITURA E INSERÇÃO DE DADOS NA MATRIZ DE LESLIE
        double[][] matrizLeslie = new double[dadosInseridos2.length][dadosInseridos2.length];

        File ficheiro = new File(nomeFicheiro);
        Scanner leituraFicheiro2 = new Scanner(ficheiro);

        while (leituraFicheiro2.hasNext()) {
            String input = leituraFicheiro2.nextLine();
            String[] dadosInseridos = input.split(", ");
            if (String.valueOf(dadosInseridos[0].charAt(0)).equalsIgnoreCase("s")) {
                int coluna = 0;
                for (int i = 1; i <= dadosInseridos.length; i++) {
                    String taxaSobrevivencia = "";
                    for (int j = 3; j <= ((String.valueOf(dadosInseridos[i - 1])).length() - 1); j++) {
                        taxaSobrevivencia += String.valueOf(dadosInseridos[i - 1].charAt(j));
                    }
                    matrizLeslie[i][coluna] = Double.valueOf(taxaSobrevivencia);
                    coluna++;
                }
            } else if (String.valueOf(dadosInseridos[0].charAt(0)).equalsIgnoreCase("f")) {
                for (int i = 0; i < dadosInseridos.length; i++) {
                    String taxaFecundidade = "";
                    for (int j = 3; j <= ((String.valueOf(dadosInseridos[i])).length() - 1); j++) {
                        taxaFecundidade += String.valueOf(dadosInseridos[i].charAt(j));
                    }
                    matrizLeslie[0][i] = Double.valueOf(taxaFecundidade);
                }
            }
        }
        return matrizLeslie;
    }

    public static double dimensãoPopulação ( double [][] matrizLeslie, int[] populacaoInicial, double [] Nt, int t) throws FileNotFoundException{ //CALCULO DIMENSAO POPULACAO
        double novaLeslie[][] = leituraMatriz(populacaoInicial, "hamsters.txt");
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