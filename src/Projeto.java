import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Projeto {

    public static void main(String[] args) throws FileNotFoundException {
        //leituraVetor(args[0]);
        int populacaoInicial[] = leituraVetor("hamsters.txt");
        double matrizLeslie[][] = leituraMatriz(populacaoInicial, "hamsters.txt");

        for(int i =0; i< matrizLeslie.length; i++){
            if(i!=0)
                System.out.println("");
            for(int j =0 ; j< matrizLeslie.length; j++)
                System.out.print(matrizLeslie[i][j] + " ");
        }
    }

    public static int[] leituraVetor(String nomeFicheiro) throws FileNotFoundException {
        File ficheiro = new File(nomeFicheiro);
        Scanner leituraFicheiro = new Scanner(ficheiro);
       //while (leituraFicheiro.hasNext()) {
            String input = leituraFicheiro.nextLine();
            String[] dadosInseridos = input.split(", ");

        //}
        leituraFicheiro.close();
        return insercaoVetor(dadosInseridos);
    }

    public static int[] insercaoVetor(String[] dadosInseridos) {
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

    public static double[][] leituraMatriz (int[]dadosInseridos2, String nomeFicheiro) throws FileNotFoundException {
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
}