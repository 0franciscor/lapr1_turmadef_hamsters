import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class TestesCatarina {

    @Test
    void notCientifica() {
        boolean resultado = Projeto.NotCientifica(2);
        boolean expected = false;
        assertEquals(expected, resultado);
    }

    @Test
    void converterNotacaoCientifica() {
        String resultado = Projeto.ConverterNotacaoCientifica(1250);
        String expected = "1,25E3";
        assertEquals(expected, resultado);
    }

    @Test
    void doubleparaIntVer() {
        boolean resultado = Projeto.DoubleparaIntVer(2);
        boolean expected = true;
        assertEquals(expected, resultado);
    }

    @Test
    void doubleToInt() {
        String resultado = Projeto.DoubleToInt(100.00);
        String expected = String.valueOf(100);
        assertEquals(expected, resultado);
    }

    @Test
    void determinarDataCriacao() throws IOException {
        String resultado = Projeto.determinarDataCriacao();
        String expected = "17_01_2021";
        assertEquals(expected, resultado);
    }

    @Test
    void formatarData() {
    }

    @Test
    void eliminarFicheiroTextoGrafico() {
    }

    @Test
    void retirarExtensao() {
        String resultado = Projeto.RetirarExtensao("Hamsters.txt");
        String expected = "Hamsters";
        assertEquals(expected, resultado);
    }
}