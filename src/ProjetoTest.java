import org.junit.jupiter.api.Test;

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
        double expectd=Projeto.valorModulo(-1.5);
        assertEquals((1.5),expectd);
    }
    @Test
    void valorModulo_numeroPositivo() {
        double experado=Projeto.valorModulo(1.5);
        assertEquals((1.5),experado);
    }

    @Test
    void totalPopulacao() {
        double[][] populacao=new double[1][4];
        double[] experado=new double[1];
        populacao[0][0]=1000;populacao[0][1]=300;populacao[0][2]=330;populacao[0][3]=100;
        Projeto.totalPopulacao(0,experado,populacao);
        assertEquals(1730,experado[0]);
    }
}