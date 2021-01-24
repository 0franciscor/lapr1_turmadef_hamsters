import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Testes {
    public static void main(String[] args) throws IOException {

        System.out.println("Testes de introdução de dados\n");

        System.out.println("modoInterativo: " + modoInterativo(new String[]{"-n", "hamsters.txt"}));
        System.out.println("modoInterativo_2: " + modoInterativo(new String[]{"-n", "gaivotas.txt"}));
        System.out.println("modoNInterativo: " + modoNInterativo(new int[5], new String[]{"-t", "3", "-g", "2", "Hamsters.txt", "Teste.txt"}));
        System.out.println("modoNInterativo_2: " + modoNInterativo(new int[5], new String[]{"-t", "1", "-g", "3", "gaivotas.txt", "Teste2.txt"}));
        System.out.println("verificaInteiro: " + verificaInteiro("33"));
        System.out.println("verificaInteiro_2: " + verificaInteiro("89"));
        System.out.println("existeFicheiro: " + existeFicheiro("Hamsters.txt"));
        System.out.println("existeFicheiro_2: " + existeFicheiro("Gaivotas.txt"));
        System.out.println("vetorAuto: " + vetorAuto(new double[]{1000, 300, 330, 100}, "Hamsters.txt"));
        System.out.println("vetorAuto_2: " + vetorAuto(new double[]{600, 200, 130, 40}, "Gaivotas.txt"));
        System.out.println("matrizAuto: " + matrizAuto(new double[][]{{0,3,3.17,0.39},{0.11,0,0,0},{0,0.29,0,0},{0,0,0.33,0}}, "Hamsters.txt"));
        System.out.println("matrizAuto_2: " + matrizAuto(new double[][]{{0, 3.5, 1.5, 0.39},{0.4, 0, 0, 0},{0,0.6,0,0},{0,0,0.5,0}}, "Gaivotas.txt"));
        System.out.println("leituraDados: " + leituraDados("x00=1000, x01=300, x02=330, x03=100", "Hamsters.txt" ));
        System.out.println("leituraDados_2: " + leituraDados("x00=600, x01=200, x02=130, x03=40", "Gaivotas.txt" ));
        System.out.println("leituraDadosAuxiliar: " + leituraDadosAuxiliar("x"));
        System.out.println("leituraDadosAuxiliar_2: " + leituraDadosAuxiliar("x"));
        System.out.println("tratamentoDados: " + tratamentoDados(new double[]{1000, 300, 330, 100}, "x00=1000, x01=300, x02=330, x03=100"));
        System.out.println("tratamentoDados_2: " + tratamentoDados(new double[]{600, 200, 130, 40}, "x00=600, x01=200, x02=130, x03=40"));

        System.out.println("\nTestes de matemática\n");

        System.out.println("valorModulo_numeroNegativo: " + valorModulo_numeroNegativo(1.5, -1.5));
        System.out.println("valorModulo_numeroNegativo: " + valorModulo_numeroNegativo(7, -7));
        System.out.println("valorModulo_numeroPositivo: " + valorModulo_numeroPositivo(1.5, 1.5));
        System.out.println("valorModulo_numeroPositivo: " + valorModulo_numeroPositivo(7, 7));
        System.out.println("totalPopulacao: " + totalPopulacao(1730, new double[][]{{1000, 300, 330, 100}}));
        System.out.println("totalPopulacao: " + totalPopulacao(300, new double[][]{{100, 100, 100}}));
        System.out.println("copiarMatriz: " + copiarMatriz(new double[][]{{1, 2}, {3, 2}}));
        System.out.println("copiarMatriz: " + copiarMatriz(new double[][]{{2, 2}, {3, 2}}));
        System.out.println("taxaVariacao: " + taxaVariacao(1.5, new double[]{1000, 1500}));
        System.out.println("taxaVariacao: " + taxaVariacao(0.625, new double[]{2000, 1250}));
        System.out.println("multiplicarMatrizesQuadradas: " + multiplicarMatrizesQuadradas(new double[][]{{1, 2}, {3, 2}}, new double[][]{{1, 2}, {3, 2}}, new double[][]{{7, 6}, {9, 10}}));
        System.out.println("multiplicarMatrizesQuadradas: " + multiplicarMatrizesQuadradas(new double[][]{{2, 2}, {3, 2}}, new double[][]{{2, 2}, {1, 2}}, new double[][]{{10, 8}, {8, 6}}));
        System.out.println("distribuicaoNormalizada: " + distribuicaoNormalizada(0, new double[][]{{50, 20, 20, 10}}, new double[][]{{500, 200, 200, 100}}, new double[]{1000}));
        System.out.println("distribuicaoNormalizada: " + distribuicaoNormalizada(0, new double[][]{{60, 10, 20, 10}}, new double[][]{{600, 100, 200, 100}}, new double[]{1000}));
        System.out.println("calcularMaiorValorProprio: " + calcularMaiorValorProprio(0, new double[][]{{4, 0, 0}, {0, 3, 0}, {0, 0, 1}}));
        System.out.println("calcularMaiorValorProprio: " + calcularMaiorValorProprio(1, new double[][]{{1, 7, 0}, {0, 3, 0}, {0, 0, 1}}));
        System.out.println("normalizarVetorProprio: " + normalizarVetorProprio(new double[]{0.9, 0.6, 0.5}, new double[]{45, 30, 25}));
        System.out.println("normalizarVetorProprio: " + normalizarVetorProprio(new double[]{0.7, 0.5, 0.8}, new double[]{35, 25, 40}));
        System.out.println("distribuicaoPopulacao: " + distribuicaoPopulacao(1, new double[][]{{0, 1, 1}, {0.5, 0, 0}, {0, 0.5, 0}}, new double[]{100, 100, 100}, new double[][]{{0, 0, 0}, {200, 50, 50}}));
        System.out.println("distribuicaoPopulacao: " + distribuicaoPopulacao(2, new double[][]{{0, 1, 1}, {0.5, 0, 0}, {0, 0.5, 0}}, new double[]{100, 100, 100}, new double[][]{{0, 0, 0}, {0, 0, 0}, {100, 100, 25}}));
        System.out.println("calcularVetorValorProprio: " + calcularVetorValorProprio(4, new double[][]{{4, 0, 0}, {0, 3, 0}, {0, 0, 1}}));
        System.out.println("calcularVetorValorProprio: " + calcularVetorValorProprio(3, new double[][]{{1, 7, 0}, {0, 3, 0}, {0, 0, 1}}));

        System.out.println("\nTestes Ficheiro de Saída\n");

        System.out.println("VerificarNotaçãoCientifica: " + NotCientifica(2));
        System.out.println("VerificarNotaçãoCientifica: " + NotCientifica(50));
        System.out.println("ConversãoemNotaçãoCientifica: " + ConverterNotacaoCientifica(1250, "1,25E3"));
        System.out.println("ConversãoemNotaçãoCientifica: " + ConverterNotacaoCientifica(13500, "1,35E4"));
        System.out.println("VerificarDouble: " + DoubleparaIntVer(2));
        System.out.println("VerificarDouble: " + DoubleparaIntVer(10));
        System.out.println("ConverterEmInteiro: " + DoubleToInt(100.00, "100"));
        System.out.println("ConverterEmInteiro: " + DoubleToInt(250.00, "250"));
        System.out.println("EliminarFicheiro: " + EliminarFicheiroTextoGrafico("valores.txt"));
        System.out.println("EliminarFicheiro: " + EliminarFicheiroTextoGrafico("ratinho.txt"));
        System.out.println("RetirarExtensão: " + RetirarExtensao("Hamsters.txt", "Hamsters"));
        System.out.println("RetirarExtensão: " + RetirarExtensao("ratinho.txt", "ratinho"));

        System.out.println("\nTestes Gráficos\n");

        System.out.println("codigoGrafico3e4: " + codigoGrafico3e4("População","População Distribuida",1,"set xlabel 'Gerações'; set ylabel 'População' ; set title 'População Distribuida' font 'arial,20'; set palette rgb 7,5,15; plot 'valores.txt' u 1:2 w lp t 'Idade 0' lw 3 ,'valores.txt' u 1:3w lp t 'Idade 1' lw 3"));
        System.out.println("codigoGrafico3e4: " + codigoGrafico3e4("População","População Distribuida",2,"set xlabel 'Gerações'; set ylabel 'População' ; set title 'População Distribuida' font 'arial,20'; set palette rgb 7,5,15; plot 'valores.txt' u 1:2 w lp t 'Idade 0' lw 3 ,'valores.txt' u 1:3w lp t 'Idade 1' lw 3 ,'valores.txt' u 1:4w lp t 'Idade 2' lw 3"));
        System.out.println("Criarpasta: "+Criarpasta("output"));
    }

    //---------------------COMPARAÇÃO-------------------------

    public static boolean comparaVetores(double[] vetor1, double[] vetor2) {
        for (int i = 0; i < vetor1.length; i++) {
            if (vetor1[i] != vetor2[i])
                return false;
        }
        return true;
    }

    public static boolean comparaMatrizes(double[][] matriz1, double[][] matriz2) {
        for (int i = 0; i < matriz1.length; i++) {
            for (int j = 0; j < matriz1.length; j++) {
                if (matriz1[i][j] != matriz2[i][j])
                    return false;
            }
        }
        return true;
    }

    //-------------------------------TESTES INTRODUÇÃO DADOS------------------------------------

    public static boolean modoInterativo(String[] argumentos) {
        return Projeto.modoInterativo(argumentos);
    }

    public static boolean modoNInterativo(int[] opcoesExecucao, String[] argumentos) {
        if (1 == Projeto.modoNInterativo(opcoesExecucao, argumentos, 0))
            return true;
        else
            return false;
    }

    public static boolean verificaInteiro(String teste) {
        return Projeto.verificaInteiro(teste);
    }

    public static boolean existeFicheiro(String nomeFicheiro){ //new double[][]{{0,3,3.17,0.39},{0.11,0,0,0},{0,0.29,0,0},{0,0,0.33,0}}
        return Projeto.existeFicheiro(nomeFicheiro);
    }

    public static boolean vetorAuto(double[] populacao, String nomeFicheiro) throws FileNotFoundException {
        return comparaVetores(populacao, Projeto.vetorAuto(nomeFicheiro));
    }

    public static boolean matrizAuto(double[][] matrizEsperada, String nomeFicheiro) throws IOException {
        double[][] matrizLeslie = new double[matrizEsperada.length][matrizEsperada.length];
        Projeto.matrizAuto(matrizLeslie, nomeFicheiro);
        return comparaMatrizes(matrizEsperada, matrizLeslie);
    }

    public static boolean leituraDados(String linha, String nomeFicheiro) throws FileNotFoundException {
        return linha.equals(Projeto.leituraDados(nomeFicheiro, 0));
    }

    public static boolean leituraDadosAuxiliar(String letra) {
        return letra.equals(Projeto.leituraDadosAuxiliar(0));
    }

    public static boolean tratamentoDados(double[] populacao, String linha) {
        return comparaVetores(populacao, Projeto.tratamentoDados(linha));
    }

    //--------------------------------TESTES DE MATEMATICA---------------------------------

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

    public static boolean distribuicaoNormalizada(int geracao,double[][]esperado,double[][]populacao,double[]total) {
        double[][]resultado=new double[1][4];
        Projeto.distribuicaoNormalizada(geracao,populacao,total,resultado);
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

    //-------------------------------------Testes Ficheiro de Saída--------------------------------------------------------------

    public static boolean NotCientifica(double teste) {
        boolean resultado = Projeto.NotCientifica(teste);
        boolean expected = false;
        if (resultado == expected) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean ConverterNotacaoCientifica(double teste, String esperado) {
        String resultado = Projeto.ConverterNotacaoCientifica(teste);
        if (resultado.equals(esperado)) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean DoubleparaIntVer(double teste) {
        boolean resultado = Projeto.DoubleparaIntVer(teste);
        boolean expected = true;
        if (resultado == expected) {
            return true;
        }else {
            return false;
        }
    }

    public static boolean DoubleToInt(double teste, String expected) {
        String resultado = Projeto.DoubleToInt(teste);
        if (resultado.equals(expected)) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean EliminarFicheiroTextoGrafico(String nome) {
        File file = new File(nome);
        Projeto.EliminarFicheiroTextoGrafico(file);
        if (file.exists()) {
            return false;
        } else {
            return true;
        }
    }

    public static boolean RetirarExtensao(String teste, String expected) {
        String resultado = Projeto.RetirarExtensao(teste);
        if (resultado.equals(expected)) {
            return true;
        }else {
            return false;
        }
    }

    //-------------------------------------Testes Gráficos--------------------------------------------

    public static boolean codigoGrafico3e4(String ylabel,String titulo,int n,String expected) throws IOException {
        String resultado = Projeto.CodigoGrafico3e4(n, ylabel, titulo);
        if (expected.equals(resultado)) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean Criarpasta(String nome) {
        File file = new File(nome);
        Projeto.Criarpasta(nome);
        if (file.exists()) {
            return true;
        } else {
            return false;
        }
    }

}