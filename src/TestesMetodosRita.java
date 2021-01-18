public class TestesMetodosRita {
    public static void main(String[] args) {
        boolean informacao;
        double[][] populacaoInicial= new double [][]{{1000, 300, 330, 100}};
        double[][] matriz=new double[][]{{1,2},{3,2}};
        double[][] matriz2=new double[][]{{1,2},{3,2}};
        double[][] multiplicacao=new double[][]{{7,6},{9,10}};
        informacao=valorModulo_numeroNegativo(1.5);
        System.out.println(informacao);
        informacao=valorModulo_numeroPositivo(1.5);
        System.out.println(informacao);
        informacao=totalPopulacao(1730,populacaoInicial);
        System.out.println(informacao);
        informacao=copiarMatriz(matriz);
        System.out.println(informacao);
        informacao=taxaVariacao(1.5);
        System.out.println(informacao);
        informacao=multiplicarMatrizesQuadradas(matriz,matriz2,multiplicacao);
        System.out.println(informacao);
        informacao=distribuicaoNormalizada();
        System.out.println(informacao);
        informacao=calcularMaiorValorProprio(0);
        System.out.println(informacao);
        informacao=normalizarVetorProprio();
        System.out.println(informacao);
        informacao=distribuicaoPopulacao();
        System.out.println(informacao);
        informacao=calcularVetorValorProprio(2);
        System.out.println(informacao);
    }
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
