import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class TesteGrafico {

    @Test
    void escreverGrafico1e2() throws FileNotFoundException {
        double[] populacao = {1730,2291};
        Projeto.EscreverGrafico1e2(1,populacao);
        //if(file.exists() && !file.isDirectory()) {
            // do something
        //}
    }

    @Test
    void escreverGrafico3e4() {
    }

    @Test
    void executarGrafico() {
    }

    @Test
    void salvarGrafico() {
    }

    @Test
    void perguntaGrafico() {
    }

    @Test
    boolean codigoGrafico3e4() throws IOException {
        String ylabel = "População";
        String titulo = "População Distribuida";
        int o=3;
        int e=1;
        String resultado=Projeto.CodigoGrafico3e4(1,ylabel, titulo);
        if("set xlabel 'Gerações'; set ylabel '" + ylabel + "' ; set title '"+titulo+"' font 'arial,20'; set palette rgb 7,5,15; plot 'valores.txt' u 1:2 w lp t 'Idade 0' lw 3 ,'valores.txt' u 1:" + o + "w lp t 'Idade "+e+"' lw 3"==resultado){
            return true;
        }
        return false;
    }
    @Test
    void graficosinterativo() {
    }

    @Test
    void graficonaointerativo() {
    }

    @Test
    void perguntaGraficoNaoInterativo() {
    }
}