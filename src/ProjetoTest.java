import org.junit.jupiter.api.Test;

import javax.print.DocFlavor;

import static org.junit.jupiter.api.Assertions.*;

class ProjetoTest {

    @org.junit.jupiter.api.Test
    void modoInterativo_parametrosVazios() {
        boolean resultado = Projeto.modoInterativo(new String[]{});
        assertFalse(resultado);
    }
    @org.junit.jupiter.api.Test
    void modoInterativo_ficheiroNaoExiste() {
        String [] teste = new String[]{"-n", "Hamsters.txt"};
        boolean resultado = Projeto.modoInterativo(teste);
        assertTrue(resultado);
    }

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
}