import java.io.FileNotFoundException;
import java.io.IOException;

public class TestesIntroducaoDados {
    public static void main(String[] args) throws IOException {
        System.out.println(modoInterativo());
        System.out.println(modoNInterativo());
        System.out.println(verificaInteiro());
        System.out.println(existeFicheiro());
        System.out.println(vetorAuto());
        System.out.println(matrizAuto());
        System.out.println(leituraDados());
        System.out.println(leituraDadosAuxiliar());
        System.out.println(tratamentoDados());
    }

    public static boolean modoInterativo() {
        String [] argumentos = new String[]{"-n", "Hamsters.txt"};
        boolean resultado = Projeto.modoInterativo(argumentos);
        if(true == resultado)
            return true;
        else
            return false;
    }

    public static boolean modoNInterativo() {
        int[] opcoesExecucao = new int[5];
        int resultado = Projeto.modoNInterativo(opcoesExecucao, new String[]{"-t", "3", "Hamsters.txt", "Teste.txt"}, 0);
        if(1 == resultado)
            return true;
        else
            return false;
    }

    public static boolean verificaInteiro() {
        boolean resultado = Projeto.verificaInteiro("33");
        if(true == resultado)
            return true;
        else
            return false;
    }

    public static boolean existeFicheiro() {
        boolean resultado = Projeto.existeFicheiro("Hamsters.txt");
        if(true == resultado)
            return true;
        else
            return false;
    }

    public static boolean vetorAuto() throws FileNotFoundException {
        double[] resultado = Projeto.vetorAuto("Hamsters.txt");
        return comparaVetores(new double[]{1000, 300, 330, 100}, resultado);
    }

    public static boolean matrizAuto() throws IOException {
        double[][] matrizLeslie = new double[4][4];
        Projeto.matrizAuto(matrizLeslie, "Hamsters.txt");
        return comparaMatrizes(new double[][]{{0,3,3.17,0.39},{0.11,0,0,0},{0,0.29,0,0},{0,0,0.33,0}}, matrizLeslie);
    }

    public static boolean leituraDados() throws FileNotFoundException {
        String resultado = Projeto.leituraDados("Hamsters.txt", 0);
        if("x00=1000, x01=300, x02=330, x03=100".equals(resultado))
            return true;
        else
            return false;
    }

    public static boolean leituraDadosAuxiliar() {
        String resultado = Projeto.leituraDadosAuxiliar(0);
        if("x" == resultado)
            return true;
        else
            return false;

    }

    public static boolean tratamentoDados() {
        double[] resultado = Projeto.tratamentoDados("x00=1000, x01=300, x02=330, x03=100");
        return comparaVetores(new double[]{1000,300,330,100}, resultado);
    }

    public static boolean comparaVetores(double[] vetor1, double[] vetor2){
        boolean aux = true;
        for(int i = 0; i<vetor1.length; i++){
            if(vetor1[i]!=vetor2[i])
                aux = false;
        }
        return aux;
    }

    public static boolean comparaMatrizes(double[][] matriz1, double[][] matriz2){
        int aux=0;
        for (int l=0;l< matriz1.length;l++){
            for (int c=0;c<matriz1[0].length;c++){
                if (matriz1[l][c]!=matriz2[l][c]){
                    aux++;
                }
            }
        }
        if (aux==0){
            return true;
        }else{
            return false;
        }
    }
}
