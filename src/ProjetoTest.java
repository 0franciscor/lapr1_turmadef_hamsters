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
    
}