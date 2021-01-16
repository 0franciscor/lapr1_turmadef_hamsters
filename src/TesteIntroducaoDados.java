import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class TesteIntroducaoDados {

    @Test
    void modoInterativo() {
        String [] argumentos = new String[]{"-n", "Hamsters.txt"};
        boolean resultado = Projeto.modoInterativo(argumentos);
        assertEquals(true, resultado);
    }

    @Test
    void modoNInterativo() {
        int[] opcoesExecucao = new int[5];
        int resultado = Projeto.modoNInterativo(opcoesExecucao, new String[]{"-t", "3", "Hamsters.txt", "Teste.txt"}, 0);
        assertEquals(1, resultado);
    }

    @Test
    void verificaInteiro() {
        boolean resultado = Projeto.verificaInteiro("33");
        assertEquals(true, resultado);
    }

    @Test
    void existeFicheiro() {
        boolean resultado = Projeto.existeFicheiro("Hamsters.txt");
        assertEquals(true, resultado);
    }

    @Test
    void vetorAuto() throws FileNotFoundException {
        double[] resultado = Projeto.vetorAuto("Hamsters.txt");
        assertArrayEquals(new double[]{1000, 300, 330, 100}, resultado);
    }

    @Test
    void matrizAuto() throws IOException {
        double[][] matrizLeslie = new double[4][4];
        Projeto.matrizAuto(matrizLeslie, "Hamsters.txt");
        assertArrayEquals(new double[][]{{0,3,3.17,0.39},{0.11,0,0,0},{0,0.29,0,0},{0,0,0.33,0}}, matrizLeslie);
    }

    @Test
    void leituraDados() throws FileNotFoundException {
        String resultado = Projeto.leituraDados("Hamsters.txt", 0);
        assertEquals("x00=1000, x01=300, x02=330, x03=100", resultado);
    }

    @Test
    void leituraDadosAuxiliar() {
        String resultado = Projeto.leituraDadosAuxiliar(0);
        assertEquals("x", resultado);
    }

    @Test
    void tratamentoDados() {
        double[] resultado = Projeto.tratamentoDados("x00=1000, x01=300, x02=330, x03=100");
        assertArrayEquals(new double[]{1000,300,330,100}, resultado);
    }

    @Test
    void insercaoMatriz() {
        //Parte específica da inserção automática. São iguais.
    }
}