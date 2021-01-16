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
        double[][] populacao=new double[1][4];
        double[] resultado=new double[1];
        populacao[0][0]=1000;populacao[0][1]=300;populacao[0][2]=330;populacao[0][3]=100;
        Projeto.totalPopulacao(0,resultado,populacao);
        assertEquals(1730,resultado[0]);
    }

    @Test
    void copiarMatriz() {
        double [][] matriz=new double[2][2];
        double [][] resultado=new double[matriz.length][matriz[0].length];
        Boolean informacao=false;
        matriz[0][0]=1;matriz[0][1]=2;matriz[1][0]=3;matriz[1][1]=2;
        Projeto.copiarMatriz(matriz,resultado);
        for (int i=0;i< matriz.length;i++){
            for (int c=0;c<matriz[0].length;c++){
                if (matriz[i][c]==resultado[i][c]){
                    informacao=true;
                }else{
                    informacao=false;
                }
            }
        }
        assertTrue(informacao);
    }

    @Test
    void taxaVariacao() {
        double[]populacao=new double[2];
        double[] resultado=new double[1];
        populacao[0]=1000;populacao[1]=1500;
        Projeto.taxaVariacao(populacao,0,resultado);
        assertEquals(1.5,resultado[0]);
    }

    @Test
    void multiplicarMatrizesQuadradas() {
        boolean informacao=false;
        double [][] matriz=new double[2][2];
        double [][] matriz2=new double[2][2];
        double[][] resultado=new double[matriz.length][matriz.length];
        matriz[0][0]=1;matriz[0][1]=2;matriz[1][0]=3;matriz[1][1]=2;
        matriz2[0][0]=1;matriz2[0][1]=2;matriz2[1][0]=3;matriz2[1][1]=2;
        resultado[0][0]=7;resultado[0][1]=6;resultado[1][0]=9;resultado[1][1]=10;
        Projeto.multiplicarMatrizesQuadradas(matriz,matriz2);
        for (int i=0;i< matriz.length;i++){
            for (int c=0;c<matriz[0].length;c++){
                if (matriz2[i][c]==resultado[i][c]){
                    informacao=true;
                }else{
                    informacao=false;
                }
            }
        }
        assertTrue(informacao);
    }

    @Test
    void distribuicaoNormalizada() {
        boolean informacao=false;
        double[]populacoes=new double[1];
        populacoes[0]=1000;
        double[][]Nt=new double[1][4];
        double[][]resultado=new double[Nt.length][Nt[0].length];
        Nt[0][0]=500;Nt[0][1]=200;Nt[0][2]=200;Nt[0][3]=100;
        double[][]esperado=new double[Nt.length][Nt[0].length];
        esperado[0][1]=50;esperado[0][1]=20;esperado[0][2]=20;esperado[0][3]=10;
        Projeto.distribuicaoNormalizada(0,Nt,populacoes,resultado);
        for (int i=0;i< Nt.length;i++){
            for (int c=0;c<Nt[0].length;c++){
                if (esperado[i][c]==resultado[i][c]){
                    informacao=true;
                }else{
                    informacao=false;
                }
            }
        }
        assertTrue(informacao);
    }

    @Test
    void calcularMaiorValorProprio() {
        double[][] matriz=new double[3][3];
        matriz[0][0]=4;matriz[1][1]=3;matriz[2][2]=1;
        int coluna=Projeto.calcularMaiorValorProprio(matriz);
        assertEquals(0,coluna);
    }

    @Test
    void normalizarVetorProprio() {
        double [] vetor=new double[3];
        vetor[0]=0.9;vetor[1]=0.6;vetor[2]=0.5;
        double [] esperado=new double[vetor.length];
        esperado[0]=45;esperado[1]=30;esperado[2]=25;
        Projeto.normalizarVetorProprio(vetor);
        assertArrayEquals(esperado,vetor);
    }

    @Test
    void distribuicaoPopulacao() {
        int geracao=2;
        boolean informacao=false;
        double[][]matriz=new double[3][3];
        matriz[0][1]=1;matriz[0][2]=1;matriz[1][0]=0.5;matriz[2][1]=0.5;
        double[] populacaoInicial=new double[matriz.length];
        populacaoInicial[0]=populacaoInicial[1]=populacaoInicial[2]=100;
        double [][] distribuicao=new double[geracao+1][matriz.length];
        Projeto.distribuicaoPopulacao(matriz,populacaoInicial,distribuicao,geracao);
        double [][]esperado=new double[geracao+1][matriz.length];
        esperado[0][0]=esperado[0][1]=esperado[0][2]=100;
        esperado[1][0]=200;esperado[1][1]=esperado[1][2]=50;
        esperado[2][0]=esperado[2][1]=100;esperado[2][2]=25;
        for (int i=0;i< distribuicao.length;i++){
            for (int c=0;c<distribuicao[0].length;c++){
                if (esperado[i][c]==distribuicao[i][c]){
                    informacao=true;
                }else{
                    informacao=false;
                }
            }
        }
        assertTrue(informacao);

    }

    @Test
    void calcularVetorValorProprio() {
        double[][] matriz=new double[3][3];
        double[] vetor=new double[matriz.length];
        matriz[0][0]=1;matriz[1][1]=1;matriz[1][0]=0.5;matriz[2][0]=-1;matriz[2][1]=2;matriz[2][2]=2;
        double maior=Projeto.calcularVetorValorProprio(matriz,vetor);
        assertEquals(2,maior);
    }
}