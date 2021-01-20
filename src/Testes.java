import java.io.FileNotFoundException;
import java.io.IOException;

public class Testes {
    public static void main(String[] args) throws IOException {

        System.out.println("Testes introdução de dados\n\n" + "modoInterativo: "+modoInterativo());
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
        System.out.println("totalPopulacao: "+totalPopulacao(1730,new double [][]{{1000, 300, 330, 100}}));
        System.out.println("totalPopulacao: "+totalPopulacao(300,new double [][]{{100, 100, 100}}));
        System.out.println("copiarMatriz: "+copiarMatriz(new double[][]{{1,2},{3,2}}));
        System.out.println("copiarMatriz: "+copiarMatriz(new double[][]{{2,2},{3,2}}));
        System.out.println("taxaVariacao: "+taxaVariacao(1.5,new double[]{1000,1500}));
        System.out.println("taxaVariacao: "+taxaVariacao(0.625,new double[]{2000,1250}));
        System.out.println("multiplicarMatrizesQuadradas: "+multiplicarMatrizesQuadradas(new double[][]{{1,2},{3,2}},new double[][]{{1,2},{3,2}},new double[][]{{7,6},{9,10}}));
        System.out.println("multiplicarMatrizesQuadradas: "+multiplicarMatrizesQuadradas(new double[][]{{2,2},{3,2}},new double[][]{{2,2},{1,2}},new double[][]{{10,8},{8,6}}));
        System.out.println("distribuicaoNormalizada: "+distribuicaoNormalizada(0,new double[][]{{50,20,20,10}},new double[][]{{500,200,200,100}},new double[]{1000}));
        System.out.println("distribuicaoNormalizada: "+distribuicaoNormalizada(0,new double[][]{{60,10,20,10}},new double[][]{{600,100,200,100}},new double[]{1000}));
        System.out.println("calcularMaiorValorProprio: "+calcularMaiorValorProprio(0,new double[][]{{4,0,0},{0,3,0},{0,0,1}}));
        System.out.println("calcularMaiorValorProprio: "+calcularMaiorValorProprio(1,new double[][]{{1,7,0},{0,3,0},{0,0,1}}));
        System.out.println("normalizarVetorProprio: "+normalizarVetorProprio(new double[]{0.9,0.6,0.5},new double[]{45,30,25}));
        System.out.println("normalizarVetorProprio: "+normalizarVetorProprio(new double[]{0.7,0.5,0.8},new double[]{35,25,40}));
        System.out.println("distribuicaoPopulacao: "+distribuicaoPopulacao(1,new double[][] {{0,1,1},{0.5,0,0},{0,0.5,0}},new double []{100, 100, 100},new double[][]{{0,0,0},{200,50,50}}));
        System.out.println("distribuicaoPopulacao: "+distribuicaoPopulacao(2,new double[][] {{0,1,1},{0.5,0,0},{0,0.5,0}},new double []{100, 100, 100},new double[][]{{0,0,0},{0,0,0},{100,100,25}}));
        System.out.println("calcularVetorValorProprio: "+calcularVetorValorProprio(4,new double[][]{{4,0,0},{0,3,0},{0,0,1}}));
        System.out.println("calcularVetorValorProprio: "+calcularVetorValorProprio(3,new double[][]{{1,7,0},{0,3,0},{0,0,1}}));
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

    public static boolean distribuicaoNormalizada(int geracao,double[][]esperado,double[][]população,double[]total) {
        double[][]resultado=new double[1][4];
        Projeto.distribuicaoNormalizada(geracao,população,total,resultado);
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

    public static boolean normalizarVetorProprio(double []vetor,double[]esperado) {
        Projeto.normalizarVetorProprio(vetor);
        return comparaVetores(vetor,esperado);
    }

    public static boolean distribuicaoPopulacao(int geracao, double[][]matriz,double[]popInicial,double[][]esperado) {
        double [][] distribuicao=new double[(geracao+2)][(geracao+2)];
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
