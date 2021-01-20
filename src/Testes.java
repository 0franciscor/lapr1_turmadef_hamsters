import java.io.FileNotFoundException;
import java.io.IOException;

public class Testes {
    public static void main(String[] args) throws IOException {
        double[][] populacaoInicial= new double [][]{{1000, 300, 330, 100}};
        double[][] populacaoInicial2= new double [][]{{100, 100, 100}};
        double[] popInicial= new double []{100, 100, 100};
        double [][] popgeracao=new double[][]{{0,0,0},{200,50,50},{0,0,0}};
        double [][] popgeracao2=new double[][]{{0,0,0},{0,0,0},{100,100,25}};
        double[][] matriz=new double[][]{{1,2},{3,2}};
        double[][] matriz2=new double[][]{{1,2},{3,2}};
        double[][] matrizaux=new double[][]{{2,2},{3,2}};
        double[][] matrizaux2=new double[][]{{2,2},{1,2}};
        double[] taxas1 = new double[]{1000,1500};
        double[] taxas2 = new double[]{2000,1250};
        double[][] multiplicacao=new double[][]{{7,6},{9,10}};
        double[][] multiplicacao2=new double[][]{{10,8},{8,6}};
        double[][]distribuicoNormalizadaesperada=new double[][]{{50,20,20,10}};
        double[][] valorProprio=new double[][]{{4,0,0},{0,3,0},{0,0,1}};
        double[][] valorProprio2=new double[][]{{1,7,0},{0,3,0},{0,0,1}};
        double [][] matrizLeslie=new double[][] {{0,1,1},{0.5,0,0},{0,0.5,0}};

        System.out.println("Testes introdução de dados\n\n" + modoInterativo());
        System.out.println("modoNInterativo: "+modoNInterativo());
        System.out.println("verificaInteiro: "+verificaInteiro());
        System.out.println("existeFicheiro: "+existeFicheiro());
        System.out.println("vetorAuto: "+vetorAuto());
        System.out.println("matrizAuto: "+matrizAuto());
        System.out.println("leituraDados: "+leituraDados());
        System.out.println("leituraDadosAuxiliar: "+leituraDadosAuxiliar());
        System.out.println("tratamentoDados: "+tratamentoDados() + "\n\nTestes de matemática\n");

        System.out.println("valorModulo_numeroNegativo: "+valorModulo_numeroNegativo(1.5,-1.5));
        System.out.println("valorModulo_numeroNegativo: "+valorModulo_numeroNegativo(7,-7));
        System.out.println("valorModulo_numeroPositivo: "+valorModulo_numeroPositivo(1.5,1.5));
        System.out.println("valorModulo_numeroPositivo: "+valorModulo_numeroPositivo(7,7));
        System.out.println("totalPopulacao: "+totalPopulacao(1730,populacaoInicial));
        System.out.println("totalPopulacao: "+totalPopulacao(300,populacaoInicial2));
        System.out.println("copiarMatriz: "+copiarMatriz(matriz));
        System.out.println("copiarMatriz: "+copiarMatriz(matrizaux));
        System.out.println("taxaVariacao: "+taxaVariacao(1.5,taxas1));
        System.out.println("taxaVariacao: "+taxaVariacao(0.625,taxas2));
        System.out.println("multiplicarMatrizesQuadradas: "+multiplicarMatrizesQuadradas(matriz,matriz2,multiplicacao));
        System.out.println("multiplicarMatrizesQuadradas: "+multiplicarMatrizesQuadradas(matrizaux,matrizaux2,multiplicacao2));
        System.out.println("distribuicaoNormalizada: "+distribuicaoNormalizada(distribuicoNormalizadaesperada));
        System.out.println("calcularMaiorValorProprio: "+calcularMaiorValorProprio(0,valorProprio));
        System.out.println("calcularMaiorValorProprio: "+calcularMaiorValorProprio(1,valorProprio2));
        System.out.println("normalizarVetorProprio: "+normalizarVetorProprio());
        System.out.println("distribuicaoPopulacao: "+distribuicaoPopulacao(1,matrizLeslie,popInicial,popgeracao));
        System.out.println("distribuicaoPopulacao: "+distribuicaoPopulacao(2,matrizLeslie,popInicial,popgeracao2));
        System.out.println("calcularVetorValorProprio: "+calcularVetorValorProprio(4,valorProprio));
        System.out.println("calcularVetorValorProprio: "+calcularVetorValorProprio(3,valorProprio2));
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

    public static boolean valorModulo_numeroNegativo(double esperado,double num) {
        double resultado=Projeto.valorModulo(num);
        if (esperado==resultado){
            return true;
        }else{
            return false;
        }
    }

    public static boolean valorModulo_numeroPositivo(double esperado, double num) {
        double resultado=Projeto.valorModulo(num);
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

    public static boolean taxaVariacao(double esperado,double[]taxas) {
        double[] resultado=new double[1];
        Projeto.taxaVariacao(taxas,0,resultado);
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

    public static boolean distribuicaoNormalizada(double[][]esperado) {
        double[][]resultado=new double[1][4];
        Projeto.distribuicaoNormalizada(0,new double[][]{{500,200,200,100}},new double[]{1000},resultado);
        return comparaMatrizes(esperado,resultado);
    }

    public static boolean calcularMaiorValorProprio(int esperado,double[][]matriz) {
        int coluna=Projeto.calcularMaiorValorProprio(matriz);
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

    public static boolean distribuicaoPopulacao(int geracao, double[][]matriz,double[]popInicial,double[][]esperado) {
        double [][] distribuicao=new double[3][3];
        Projeto.distribuicaoPopulacao(matriz,popInicial,distribuicao,geracao);
        return comparaMatrizes(esperado,distribuicao);
    }

    public static boolean calcularVetorValorProprio(double esperado,double[][] matriz) {
        double maior=Projeto.calcularVetorValorProprio(matriz,new double[3]);
        if (esperado==maior){
            return true;
        }else{
            return false;
        }
    }
}
