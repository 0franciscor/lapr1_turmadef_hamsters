import java.io.FileNotFoundException;
import java.io.IOException;

public class Testes {
    public static void main(String[] args) throws IOException {
        double[][] populacaoInicial= new double [][]{{1000, 300, 330, 100}};
        double[][] matriz=new double[][]{{1,2},{3,2}};
        double[][] matriz2=new double[][]{{1,2},{3,2}};
        double[][] multiplicacao=new double[][]{{7,6},{9,10}};

        System.out.println("Testes introdução de dados\n\n" + modoInterativo());
        System.out.println(modoNInterativo());
        System.out.println(verificaInteiro());
        System.out.println(existeFicheiro());
        System.out.println(vetorAuto());
        System.out.println(matrizAuto());
        System.out.println(leituraDados());
        System.out.println(leituraDadosAuxiliar());
        System.out.println(tratamentoDados() + "\n\nTestes de matemática\n");

        System.out.println(valorModulo_numeroNegativo(1.5));
        System.out.println(valorModulo_numeroPositivo(1.5));
        System.out.println(totalPopulacao(1730,populacaoInicial));
        System.out.println(copiarMatriz(matriz));
        System.out.println(taxaVariacao(1.5));
        System.out.println(multiplicarMatrizesQuadradas(matriz,matriz2,multiplicacao));
        System.out.println(distribuicaoNormalizada());
        System.out.println(calcularMaiorValorProprio(0));
        System.out.println(normalizarVetorProprio());
        System.out.println(distribuicaoPopulacao());
        System.out.println(calcularVetorValorProprio(2));
    }

    //COMPARAÇÃO

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

    //TESTES INTRODUÇÃO DADOS

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

    //TESTES DE MATEMATICA

    public static boolean valorModulo_numeroNegativo(double esperado) {
        double resultado=Projeto.valorModulo(-1.5);
        if (esperado==resultado){
            return true;
        }else{
            return false;
        }
    }

    public static boolean valorModulo_numeroPositivo(double esperado) {
        double resultado=Projeto.valorModulo(1.5);
        if (esperado==resultado){
            return true;
        }else{
            return false;
        }
    }

    public static boolean totalPopulacao(double esperado,double[][]populacaoInicial) {
        double[] resultado = new double[1];
        Projeto.totalPopulacao(0,resultado,populacaoInicial);
        if (esperado==resultado[0]){
            return true;
        }else{
            return false;
        }
    }

    public static boolean copiarMatriz(double[][]matriz) {
        double [][] resultado=new double[matriz.length][matriz[0].length];
        Projeto.copiarMatriz(matriz,resultado);
        return comparaMatrizes(matriz,resultado);
    }

    public static boolean taxaVariacao(double esperado) {
        double[] resultado=new double[1];
        Projeto.taxaVariacao(new double[]{1000,1500},0,resultado);
        if (esperado==resultado[0]){
            return true;
        }else{
            return false;
        }
    }

    public static boolean multiplicarMatrizesQuadradas(double[][] matriz1,double[][]matriz2,double[][]resultado) {
        Projeto.multiplicarMatrizesQuadradas(matriz1,matriz2);
        return comparaMatrizes(matriz2,resultado);
    }

    public static boolean distribuicaoNormalizada() {
        double[][]resultado=new double[1][4];
        double[][]esperado=new double[][]{{50,20,20,10}};
        Projeto.distribuicaoNormalizada(0,new double[][]{{500,200,200,100}},new double[]{1000},resultado);
        return comparaMatrizes(esperado,resultado);
    }

    public static boolean calcularMaiorValorProprio(int esperado) {
        int coluna=Projeto.calcularMaiorValorProprio(new double[][]{{4,0,0},{0,3,0},{0,0,1}});
        if (esperado==coluna){
            return true;
        }else{
            return false;
        }
    }

    public static boolean normalizarVetorProprio() {
        double [] vetor=new double[]{0.9,0.6,0.5};
        Projeto.normalizarVetorProprio(vetor);
        double[]esperado=new double[]{45,30,25};
        int aux=0;
        for (int l=0;l< vetor.length;l++){
            if (esperado[l]!=vetor[l]){
                aux++;
            }
        }
        if (aux==0){
            return true;
        }else{
            return false;
        }
    }

    public static boolean distribuicaoPopulacao() {
        double [][] distribuicao=new double[3][3];
        double [][] matriz=new double[][] {{0,1,1},{0.5,0,0},{0,0.5,0}};
        double [][] esperado=new double[][]{{0,0,0},{0,0,0},{100,100,25}};
        Projeto.distribuicaoPopulacao(matriz,new double[]{100,100,100},distribuicao,2);
        return comparaMatrizes(esperado,distribuicao);
    }

    public static boolean calcularVetorValorProprio(double esperado) {
        double maior=Projeto.calcularVetorValorProprio(new double[][]{{1,0,0},{0.5,1,0},{-1,2,2}},new double[3]);
        if (esperado==maior){
            return true;
        }else{
            return false;
        }
    }
}
