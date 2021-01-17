import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ProjetoTesteParteMatemática {
    @Test
    void valorModulo_numeroNegativo() {
        double resultado=Projeto.valorModulo(-1.5);
        assertEquals((1.5),resultado);
    }
    @Test
    void valorModulo_numeroPositivo() {
        double resultado=Projeto.valorModulo(1.5);
        assertEquals((1.5),resultado);
    }
    @Test
    void totalPopulacao() {
        double[] resultado = new double[1];
        Projeto.totalPopulacao(0,resultado,new double[][]{{1000,300,330,100}});
        assertEquals(1730,resultado[0]);
    }
    @Test
    void copiarMatriz() {
        double [][] matriz=new double[2][2];
        double [][] resultado=new double[matriz.length][matriz[0].length];
        matriz[0][0]=1;matriz[0][1]=2;matriz[1][0]=3;matriz[1][1]=2;
        Projeto.copiarMatriz(matriz,resultado);
        assertArrayEquals(matriz,resultado);
    }
    @Test
    void taxaVariacao() {
        double[] resultado=new double[1];
        Projeto.taxaVariacao(new double[]{1000,1500},0,resultado);
        assertEquals(1.5,resultado[0]);
    }
    @Test
    void multiplicarMatrizesQuadradas() {
        double [][] matriz2=new double[2][2];
        matriz2[0][0]=1;matriz2[0][1]=2;matriz2[1][0]=3;matriz2[1][1]=2;
        Projeto.multiplicarMatrizesQuadradas(new double[][]{{1,2},{3,2}},matriz2);
        assertArrayEquals(new double[][]{{7,6},{9,10}},matriz2);
    }
    @Test
    void distribuicaoNormalizada() {
        double[][]resultado=new double[1][4];
        Projeto.distribuicaoNormalizada(0,new double[][]{{500,200,200,100}},new double[]{1000},resultado);
        assertArrayEquals(new double[][]{{50,20,20,10}},resultado);
    }
    @Test
    void calcularMaiorValorProprio() {
        int coluna=Projeto.calcularMaiorValorProprio(new double[][]{{4,0,0},{0,3,0},{0,0,1}});
        assertEquals(0,coluna);
    }
    @Test
    void normalizarVetorProprio() {
        double [] vetor=new double[3];
        vetor[0]=0.9;vetor[1]=0.6;vetor[2]=0.5;
        Projeto.normalizarVetorProprio(vetor);
        assertArrayEquals(new double[]{45,30,25},vetor);
    }
    @Test
    void distribuicaoPopulacao() {
        double [][] distribuicao=new double[3][3];
        Projeto.distribuicaoPopulacao(new double[][] {{0,1,1},{0.5,0,0},{0,0.5,0}},new double[]{100,100,100},distribuicao,2);
        assertArrayEquals(new double[][]{{0,0,0},{0,0,0},{100,100,25}},distribuicao);

    }
    @Test
    void calcularVetorValorProprio() {
        double maior=Projeto.calcularVetorValorProprio(new double[][]{{1,0,0},{0.5,1,0},{-1,2,2}},new double[3]);
        assertEquals(2,maior);
    }
}