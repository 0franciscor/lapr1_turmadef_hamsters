import org.la4j.Matrix;
import org.la4j.decomposition.EigenDecompositor;
import org.la4j.matrix.dense.Basic2DMatrix;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Scanner;

public class Projeto {

    static Scanner ler = new Scanner(System.in);

    public static void main(String[] args) throws IOException {
        boolean existe = false, naoInterativo = false;
        String nomeFicheiro = null;
        int[] opcoesExecucao = new int [5];
        int numCiclos = 0, erro = 0; //ERRO 0 - Interativo; ERRO 1- NAO INTERATIVO; ERRO 2- VERDADEIRO ERRO

        //RESPOSAVEL POR VERIFICAR SE O CODIGO ESTA A CORRER EM MODO NAO INTERATIVO
        erro = modoNInterativo(opcoesExecucao, args, erro);
        if(erro == 1) {
            naoInterativo = true;
            existe = true;
            numCiclos = opcoesExecucao[0];
            nomeFicheiro = args[(args.length - 2)];
        }
        //TERMINA AQUI E COMEÇA PARA O MODO INTERATIVO COM INTRODUÇAO DE FICHEIRO

        else if(args.length != 0 && erro !=2){
            existe = modoInterativo(args);
            nomeFicheiro = args[1];
        }
        //TERMINA AQUI

        double[] populacaoInicial; //VETOR INICIAL
        double[][] matrizLeslie;   //DECLARAÇÃO MATRIZ LESLIE
        if(erro != 2) {
            //matrizAuto(existe, nomeFicheiro);
            if (existe) {
                populacaoInicial = tratamentoDados((leituraDados(nomeFicheiro, 0)));
                matrizLeslie = new double[populacaoInicial.length][populacaoInicial.length];

                for (int i = 1; i <= 2; i++) {
                    double[] array = tratamentoDados(leituraDados(nomeFicheiro, i));

                    if (array.length == (populacaoInicial.length - 1))
                        insercaoMatriz(matrizLeslie, array, 1, true);
                    else
                        insercaoMatriz(matrizLeslie, array, 2, true);
                }

            } else {
                System.out.println("Quantos intervalos de idade possui a populacao que pretende estudar?");
                int numIntervalos = ler.nextInt();

                populacaoInicial = new double[numIntervalos];
                insercaoDados(populacaoInicial, "Populacao");

                matrizLeslie = new double[populacaoInicial.length][populacaoInicial.length];
                double[] aux = new double[matrizLeslie.length];

                for (int i = 1; i <= 2; i++) {
                    if (i == 1)
                        insercaoMatriz(matrizLeslie, insercaoDados(aux, "Taxa de sobrevivencia"), i, false);
                    if (i == 2)
                        insercaoMatriz(matrizLeslie, insercaoDados(aux, "Taxa de fecundidade"), i, false);
                }
            }

            int n = matrizLeslie.length, t = -1, geracao = -1;

            int[] geracoesEstimadas = new int[1000];
            double[] populacoesEstimadas = new double[1000];
            double[] taxasDeVariacao = new double[1000];
            double[][] Nt = new double[1001][matrizLeslie.length];
            double[][] distribuicaoNormalizada = new double[1000][matrizLeslie.length];

            if (naoInterativo) {
                while ((geracao + 1) <= numCiclos) {
                    t++;
                    geracao++;
                    procedimentoCalculoGeracoes(Nt, geracao, geracoesEstimadas, matrizLeslie, populacaoInicial, t, populacoesEstimadas, taxasDeVariacao, distribuicaoNormalizada, n);
                }
            } else {
                System.out.println("Quais as geracoes que pretende que sejam estudadas?");
                int aux = ler.nextInt();
                while ((geracao + 1) <= aux) {
                    t++;
                    geracao++;
                    procedimentoCalculoGeracoes(Nt, geracao, geracoesEstimadas, matrizLeslie, populacaoInicial, t, populacoesEstimadas, taxasDeVariacao, distribuicaoNormalizada, n);
                }

            }

            double[] vetor = new double[matrizLeslie.length];
            double valorProprio;
            valorProprio = calcularVetorValorProprio(matrizLeslie, vetor);
            normalizarVetorProprio(vetor);

            if (naoInterativo) {
                escreverParaFicheiro(opcoesExecucao, geracao, geracoesEstimadas, populacoesEstimadas, taxasDeVariacao, Nt, distribuicaoNormalizada, n, valorProprio, vetor, args);

            } else {
                interfaceUtilizador(geracao, geracoesEstimadas, populacoesEstimadas, taxasDeVariacao, Nt, distribuicaoNormalizada, n, valorProprio, vetor);
            }
        }
    }

    public static boolean modoInterativo(String[] args){
        boolean existe = false;
        if (args.length != 0 && args[0].equals("-n")) {
            String nomeFicheiro = args[1];
            existe = existeFicheiro(nomeFicheiro);
        }
        return existe;
    }

    public static int modoNInterativo(int[] opcoesExecucao, String[] args, int erro) {

        if (args.length != 0 && args[0].equals("-t")) {
            String nomeFicheiro = args[(args.length - 2)];
            boolean existe = existeFicheiro(nomeFicheiro);

            if (existe) {
                erro = 1;
                opcoesExecucao[0] = Integer.parseInt(args[1]);
                for (int i = 2; i <= (args.length - 2); i++) {
                    if (args[i].equals("-g")) {
                        if (args[i + 1].equals(String.valueOf(1)) || args[i + 1].equals(String.valueOf(2)) || args[i + 1].equals(String.valueOf(3))) {
                            for (int j = 1; j <= 3; j++) {
                                if (args[i + 1].equals(String.valueOf(j)))
                                    opcoesExecucao[1] = j;
                            }
                        }
                    }
                    if (args[i].equals("-e")) {
                        opcoesExecucao[2] = 1;
                    }
                    if (args[i].equals("-v")) {
                        opcoesExecucao[3] = 1;
                    }
                    if (args[i].equals("-r")) {
                        opcoesExecucao[4] = 1;
                    }
                }
            } else {
                System.out.println("A síntaxe do comando não é a correta. Verifique a mesma ou a inserção dos ficheiros.");
                erro = 2;
            }
        }
        return erro;
    }

    /*public static void matrizAuto(boolean existe, String nomeFicheiro) throws IOException {
        if (existe) {
            double[] populacaoInicial = tratamentoDados((leituraDados(nomeFicheiro, 0)));
            double[][] matrizLeslie = new double[populacaoInicial.length][populacaoInicial.length];

            for (int i = 1; i <= 2; i++) {
                double[] array = tratamentoDados(leituraDados(nomeFicheiro, i));

                if (array.length == (populacaoInicial.length - 1))
                    insercaoMatriz(matrizLeslie, array, 1, true);
                else
                    insercaoMatriz(matrizLeslie, array, 2, true);
            }
        }
    }*/

    public static boolean existeFicheiro(String nomeFicheiro){
        File ficheiroVerificacao = new File(nomeFicheiro);
        return ficheiroVerificacao.exists();
    }
    public static String leituraDados(String nomeFicheiro, int numLinha) throws FileNotFoundException { //LEITURA EXCLUSIVA DO VETOR
        File ficheiro = new File(nomeFicheiro);
        Scanner leituraFicheiro = new Scanner(ficheiro);
        String letra = leituraDadosAuxiliar(numLinha);
        String input;
        boolean stringCerta = false;
        int aux = 0;
        do{
            input = leituraFicheiro.nextLine();
            if (String.valueOf(input.charAt(0)).equals(letra))
                stringCerta = true;
            aux++;
        } while(aux < ficheiro.length() && !stringCerta);
        leituraFicheiro.close();

        return input;
    }

    public static String leituraDadosAuxiliar(int numLinha){
        String letra = null;

        if(numLinha==0)
            letra = "x";
        else if(numLinha==1)
            letra = "s";
        else if(numLinha==2)
            letra = "f";

        return letra;
    }

    public static double[] insercaoDados(double[] array, String elemento){
        int limite;

        if(elemento.equalsIgnoreCase("Taxa de sobrevivencia"))
            limite= (array.length-1);
        else
            limite = array.length;

        for(int i=0; i< limite; i++){
            System.out.println("Insira o valor n" + (i+1) + " da " + elemento);
            array[i] = ler.nextDouble();
        }
        return array;
    }


    public static double[] tratamentoDados(String input) {
        String[] dadosInseridos = input.split(", ");
        double [] valoresTratados = new double[dadosInseridos.length];

        for(int i = 0; i< dadosInseridos.length; i++) {
            String[] dadosInseridosTratados = dadosInseridos[i].split("=");
            double valorTratado = Double.parseDouble(dadosInseridosTratados[1]);
            valoresTratados[i] = valorTratado;
        }
        return valoresTratados;
    }

    public static void insercaoMatriz (double[][] matrizLeslie, double[] valoresTratados, int tipologia, boolean automatico) {
        if (tipologia == 1) {
            int limite;
            if(automatico)
                limite = valoresTratados.length;
            else
                limite = (valoresTratados.length-1);

            for (int i = 1; i <= limite; i++) {
                matrizLeslie[i][i-1] = valoresTratados[i - 1];
            }
        }
        else{
            for (int i = 0; i < valoresTratados.length; i++)
                matrizLeslie[0][i] = valoresTratados[i];
        }
    }

    public static void interfaceUtilizador(int geracao, int [] geracoesEstimadas, double [] populacoesEstimadas, double [] taxasDeVariacao, double [][] Nt, double [][] distribuicaoNormalizada, int n,double valorProprio, double[] vetorProprio) throws IOException {
        int leitura;
        do {
            int [] opcoesVisualizacao = new int[6];
            System.out.println("Quais os dados que gostaria de visualizar? (Insira os números associados a cada parâmetro e prima Enter)");
            System.out.println("1 - População total a cada geração.");
            System.out.println("2 - Taxa de variação.");
            System.out.println("3 - Distribuição da população.");
            System.out.println("4 - Distribuição normalizada da população.");
            System.out.println("5 - Comportamento assimtótico associado ao maior valor próprio.");
            System.out.println("6 - Gráficos.");
            System.out.println("0 - Quando não quiser inserir mais parâmetros.");
            System.out.println("-1 - Para sair do programa. Parâmetro isolado");
            do{
                leitura = ler.nextInt();
                if (leitura>0){
                    opcoesVisualizacao[leitura-1]=1;
                }
            } while(leitura > 0);

            escreverParaConsola(geracao, geracoesEstimadas, populacoesEstimadas, taxasDeVariacao, Nt, distribuicaoNormalizada, n, valorProprio, vetorProprio,opcoesVisualizacao);

        } while(leitura != -1);
    }

    public static void procedimentoCalculoGeracoes(double[][] Nt, int geracao, int[] geracoesEstimadas, double[][] matrizLeslie, double[] populacaoInicial, int t, double[] populacoesEstimadas, double[] taxasDeVariacao, double[][] distribuicaoNormalizada, int n){
        geracoesEstimadas[geracao] = t;
        distribuicaoPopulacao(matrizLeslie, populacaoInicial, t, Nt, populacoesEstimadas, geracao);
        distribuicaoPopulacao(matrizLeslie, populacaoInicial, (t + 1), Nt, populacoesEstimadas, (geracao + 1));
        TaxaVariacao(populacoesEstimadas, geracao, taxasDeVariacao);
        distribuicaoNormalizada(geracao, Nt, populacoesEstimadas, distribuicaoNormalizada, n);

    }

    public static void distribuicaoPopulacao (double [][] matrizLeslie, double[] populacaoInicial, int t, double[][] Nt,double[] populacoesEstimadas, int geracao) { //CALCULO DIMENSAO POPULACAO
        double [][] Lesliemultiplicada = new double[matrizLeslie.length][matrizLeslie.length];
        copiarMatriz(matrizLeslie,Lesliemultiplicada);

        for(int temp = t; temp>=2;temp--){
            multiplicarmatrizes(matrizLeslie,Lesliemultiplicada);
        }

        if (t==0){
            for (int i=0;i<matrizLeslie.length;i++){
                Nt[geracao][i]=populacaoInicial[i];
            }
        } else {
            for (int l=0;l<matrizLeslie.length;l++){
                Nt[geracao][l]=0;
                for (int c=0;c< matrizLeslie.length;c++){
                    Nt[geracao][l]+=Lesliemultiplicada[l][c]*populacaoInicial[c];
                }
            }
        }

        populacoesEstimadas[geracao]=0;
        for (int i=0;i< matrizLeslie.length;i++){
            populacoesEstimadas[geracao]+=Nt[geracao][i];
        }

    }

    public static void copiarMatriz (double[][] matrizLeslie, double[][] copia){
        for(int i =0 ; i<matrizLeslie.length; i++){
            for(int j =0 ; j< matrizLeslie.length; j++){
                copia[i][j]=matrizLeslie[i][j];
            }
        }
    }

    public static void TaxaVariacao(double [] populacoesEstimadas, int geracao, double[] taxasDeVariacao) {
        double variacao=populacoesEstimadas[geracao+1]/populacoesEstimadas[geracao];
        taxasDeVariacao[geracao]=variacao;
    }

    public static void multiplicarmatrizes (double [][] matriz1, double[][] matriz2){
        double [] aux = new double [matriz2.length];

        for (int l=0;l<matriz1.length;l++){
            for (int i=0;i<matriz1.length;i++){
                double soma=0;
                for (int c=0;c<matriz1.length;c++){
                    soma+=matriz2[l][c]*matriz1[c][i];
                }
                aux[i]=soma;
            }
            for (int c=0;c<matriz2.length;c++){
                matriz2[l][c]=aux[c];
            }
        }
    }

    public static void distribuicaoNormalizada (int geracao,double[][] Nt, double [] populacoesEstimadas, double[][] distribuicaoNormalizada, int n){
        for (int i=0;i<n;i++){
            distribuicaoNormalizada[geracao][i]=Nt[geracao][i]/populacoesEstimadas[geracao];
        }
    }

    public static double calcularVetorValorProprio (double[][] matrizLeslie,double[] vetor) {
        Matrix a = new Basic2DMatrix(matrizLeslie);
        EigenDecompositor eigenD = new EigenDecompositor(a);
        Matrix[] mattD = eigenD.decompose();
        double[][] vetoraux = mattD[0].toDenseMatrix().toArray();
        double[][] valor = mattD[1].toDenseMatrix().toArray();

        int coluna = calcularMaiorValorProprio(valor);
        double maior = valor[coluna][coluna];

        for (int i = 0; i < matrizLeslie.length; i++) {
            vetor[i] = vetoraux[i][coluna];
        }
        return maior;
    }

    public static int calcularMaiorValorProprio (double[][] valor){
        double maior = 0;
        int coluna = 0;
        for (int l = 0; l < valor.length; l++) {
            if (valor[l][l] >= 0) {
                if (valor[l][l] > maior) {
                    maior = valor[l][l];
                    coluna = l;
                }
            } else {
                if ((-valor[l][l]) > maior) {
                    maior = (-valor[l][l]);
                    coluna = l;
                }
            }
        }
        return coluna;
    }
    public static void normalizarVetorProprio (double[] vetor){
        double soma=0;
        for (int i=0;i<vetor.length;i++){
            soma+=vetor[i];
        }
        if (soma!=1){
            normalizar(vetor,soma);
        }
    }
    public static void normalizar(double[] vetor, double soma){
        for (int i=0;i<vetor.length;i++){
            vetor[i]/=soma;
        }
    }
    public static void escreverParaFicheiro (int[] opcoesExecucao, int geracao, int [] geracoesEstimadas, double [] populacoesEstimadas, double [] taxasDeVariacao, double [][] Nt, double [][] distribuicaoNormalizada, int n,double valorProprio, double[] vetorProprio, String [] args) throws FileNotFoundException {
        File file = new File(args[args.length-1]);
        PrintWriter out = new PrintWriter(file);
        NumberFormat formatter;
        formatter = new DecimalFormat("0.###E0");
        int c;
            out.println("De acordo com os dados inseridos foram obtidos os seguintes resultados:");
            for (int j = 0; j <= geracao; j++) {
                if (opcoesExecucao[3] == 1) {
                    out.print("\nO número total de indivíduos da geração " + geracoesEstimadas[j] + " é ");
                    if (populacoesEstimadas[j] > 99999 || populacoesEstimadas[j] < 0.001) {
                        out.printf(formatter.format(populacoesEstimadas[j]));
                    } else {
                        out.printf("%.2f.", populacoesEstimadas[j]);
                    }
                }
            }
            out.println("");
            for (int j = 0; j <= geracao; j++) {
                if (opcoesExecucao[4] == 1) {
                    out.print("\nA taxa de variação para a geração " + geracoesEstimadas[j] + " é ");
                    if (taxasDeVariacao[j] > 99999 || taxasDeVariacao[j] < 0.001) {
                        out.printf(formatter.format(taxasDeVariacao[j]));
                    } else {
                        out.printf("%.2f.", taxasDeVariacao[j]);
                    }
                }
            }
            out.print("\n");
            out.println("\nSegue-se a distribuição da população, isto é, quantos indivíduos existem nas diferentes faixas etárias.");
            for (int j = 0; j <= geracao; j++) {
                out.println("\nA população na geração " + geracoesEstimadas[j] + " encontra-se distribuída da seguinte forma:");
                for (c = 0; c < n - 1; c++) {
                    out.print("Idade " + c + ": ");
                    if (Nt[j][c] > 99999 || Nt[j][c] < 0.001) {
                        out.printf(formatter.format(Nt[j][c]));
                    } else {
                        out.printf("%.2f", Nt[j][c]);
                    }
                    out.print("\n");
                }
                out.print("Idade " + c + ": ");
                if (Nt[j][c] > 99999 || Nt[j][c] < 0.001) {
                    out.printf(formatter.format(Nt[j][n-1]));
                } else {
                    out.printf("%.2f", Nt[j][n - 1]);
                }
                out.print("\n");
            }
            out.print("\nA distribuição normalizada resulta da divisão da dimensão da população em cada faixa etária pela população total.\n");
            for (int j = 0; j <= geracao; j++) {
                out.print("\nA distribuição normalizada da geração " + geracoesEstimadas[j] + " está representada pelas várias faixas etárias:\n");
                for (c = 0; c < n - 1; c++) {
                    out.print("Idade " + c + ": ");
                    if (distribuicaoNormalizada[j][c] > 99999 || distribuicaoNormalizada[j][c] < 0.001) {
                        out.printf(formatter.format(distribuicaoNormalizada[j][c]));
                    } else {
                        out.printf("%.2f", distribuicaoNormalizada[j][c]);
                    }
                    out.print("\n");
                }
                out.print("Idade " + c + ": ");
                if (distribuicaoNormalizada[j][c] > 99999 || distribuicaoNormalizada[j][c] < 0.001) {
                    out.printf(formatter.format(distribuicaoNormalizada[j][n-1]));
                } else {
                    out.printf("%.2f", distribuicaoNormalizada[j][n - 1]);
                }
                out.print("\n");
            }
            if (opcoesExecucao[2] == 1) {
                out.println("\nComportamento Assintótico da população associado ao maior valor próprio.");
                out.printf("\nO valor próprio de módulo máximo é aproximadamente: %.3f\n", valorProprio);
                out.println("Este valor indica-nos a taxa de crescimento da população.");
                String comportamento = "igual", analise = "o que significa que a população se irá manter constante ao longo dos anos.";
                if (valorProprio > 1) {
                    comportamento = "maior";
                    analise = " isto significa que a população irá aumentar ao longo dos anos.";
                }
                if (valorProprio < 1) {
                    comportamento = "menor";
                    analise = " isto significa que a população irá diminuir ao longo dos anos.";
                }
                out.println("Como o valor próprio é " + comportamento + " que 1," + analise);
                out.println("\nO vetor próprio associado ao maior valor próprio indica-nos as proporções populacionais constantes.");
                for (c = 0; c < n; c++) {
                    out.print("Idade " + c + ": ");
                    if (vetorProprio[c] > 99999 || vetorProprio[c] < 0.001) {
                        out.printf(formatter.format(vetorProprio[c]));
                    } else {
                        out.printf("%.3f\n", vetorProprio[c]);
                    }
                }
            }
            out.print("\nEncontra-se concluída a apresentação dos resultados do programa da evolução das espécies.");
            out.close();
    }
    public static void escreverParaConsola (int geracao, int [] geracoesEstimadas, double [] populacoesEstimadas, double [] taxasDeVariacao, double [][] Nt, double [][] distribuicaoNormalizada, int n,double valorProprio, double[] vetorProprio,int[] opcoesVisualizaco) throws IOException {
        int c;
        if (opcoesVisualizaco[0]==1){
            for (int j = 0; j <= geracao; j++) {
                System.out.printf("\nO número total de indivíduos na geracao " + geracoesEstimadas[j] + " é %.2f.", populacoesEstimadas[j]);
            }
            System.out.println();
        }
        if (opcoesVisualizaco[1]==1){
            for (int a = 0; a <= geracao; a++) {
                if (a < geracao) {
                    System.out.printf("\nA taxa de variação para a geração " + geracoesEstimadas[a] + " da população ao longo dos anos é %.2f.", taxasDeVariacao[a]);
                }
            }
            System.out.println();
        }
        if (opcoesVisualizaco[2]==1){
            System.out.println("\nA distribuição da população, isto é, quantos indivíduos existem nas diferentes faixas etárias.");
            for (int j = 0; j <= geracao; j++) {
                System.out.print("\nA população na geração " + geracoesEstimadas[j] + " encontra-se distribuída da seguinte forma:\n");
                for (c = 0; c < n - 1; c++) {
                    System.out.printf("Idade " + c + ": %.2f\n", Nt[j][c]);
                }
                System.out.printf("Idade " + c + ": %.2f\n", Nt[j][n - 1]);
            }
        }
        if (opcoesVisualizaco[3]==1){
            System.out.print("\nA distribuição normalizada resulta da divisão da dimensão da população em cada faixa etária pela população total.\n");
            for (int j = 0; j <= geracao; j++) {
                System.out.print("\nA distribuição normalizada da geração " + geracoesEstimadas[j] + " está representada pelas várias faixas etárias:\n");
                for (c = 0; c < n - 1; c++) {
                    System.out.printf("Idade " + c + ": %.2f\n", distribuicaoNormalizada[j][c]);
                }
                System.out.printf("Idade " + c + ": %.2f\n", distribuicaoNormalizada[j][n - 1]);
            }
        }
        if (opcoesVisualizaco[4]==1){
            System.out.println("\nComportamento Assintotico da populacao associado ao maior valor proprio.");
            System.out.printf("\nO valor proprio de modulo maximo e aproximadamente: %.3f\n", valorProprio);
            System.out.println("Este valor indica-nos a taxa de crescimento da populacao.");
            String comportamento="igual", analise="o que significa que a populacao se ira manter constante ao longo dos anos.";
            if (valorProprio>1){
                comportamento="maior";
                analise="isto significa que a populacao ira aumentar ao longo dos anos.";
            }
            if (valorProprio<1){
                comportamento="menor";
                analise="isto significa que a populacao ira diminuir ao longo dos anos.";
            }
            System.out.println("Como o valor proprio e " + comportamento + " que 1, " + analise);
            System.out.println("\nO vetor proprio associado ao maior valor proprio indica-nos as proporcoes populacionais constantes.");
            for (c = 0; c < n; c++) {
                System.out.printf("Idade " + c + ": %.3f\n", vetorProprio[c]);
            }
        }
        if (opcoesVisualizaco[5]==1){
            int lol;
            System.out.println("Que gráfico quer representar?");
            System.out.println("1-Evolução da População Total;\n2-Evolução da taxa de variação;\n3-Distribuição da População;\n4-Distribuição normalizada da população.");
            lol = ler.nextInt();
            if (lol == 1) {
                PopulacaoTotal(geracao, geracoesEstimadas, populacoesEstimadas);
                Graficopopulacao("'valores.txt' title 'População' with lines lc 'blue' lw 3");
                PerguntaGrafico("População", "blue");
            } else {
                PopulacaoTotal(geracao, geracoesEstimadas, taxasDeVariacao);
                Graficopopulacao("'valores.txt' title 'População' with lines lc 'red' lw 3");
                PerguntaGrafico("Taxa de Variação", "red");
            }
        }
    }
    public static void PopulacaoTotal(int geracao,int [] geracoesEstimadas,double[] populacoesEstimadas) throws FileNotFoundException {
        File file = new File("valores.txt");
        PrintWriter out = new PrintWriter(file);
        for (int l = 0; l <= geracao; l++) {
            out.print(geracoesEstimadas[l] + " " + populacoesEstimadas[l]+"\n");
        }

        out.close();
    }

    public static void populacaodistribuida(int n,double[][] Nt,String s,int geracao,int [] geracoesEstimadas,String [] args) throws FileNotFoundException {
        File file = new File(args[args.length-1]);
        PrintWriter out = new PrintWriter(s);
        for (int l = 0; l <= geracao; l++) {
            out.print(geracoesEstimadas[l] + " ");
            for (int c = 0; c < n; c++) {
                out.print(Nt[l][c]+" ");
            }
            out.print("\n");
        }
        out.close();
    }

    public static void Graficopopulacao(String d) throws IOException {
        Runtime  rt = Runtime.getRuntime();
        rt.exec("gnuplot -p -e \"plot "+d+"\"");
    }

    public static void SalvarGrafico(String s,String d,String terminal) throws IOException {
        Runtime  rt = Runtime.getRuntime();
        rt.exec("gnuplot -e \"set terminal "+terminal+"; set output '"+s+"'; plot "+d+"\"");
    }

    public static void PerguntaGrafico(String s,String d) throws IOException {
        int resposta;
        System.out.println("Deseja Salvar o Gráfico?(1- Sim;2- Não)");
        resposta = ler.nextInt();
        if (resposta == 1) {
            System.out.println("Qual o formato do ficheiro?");
            System.out.println("1- Formato PNG\n2- Formato TXT\n3- Formato EPS");
            resposta = ler.nextInt();
            switch (resposta) {
                case 1:
                    SalvarGrafico(s+".png", "'valores.txt' title '"+s+"' with lines lc '"+d+"' lw 3", "png");
                    break;
                case 2:
                    SalvarGrafico(s+".txt", "'valores.txt' title '"+s+"' with lines lc '"+d+"' lw 3", "txt");
                    break;
                case 3:
                    SalvarGrafico(s+".eps", "'valores.txt' title '"+s+"' with lines lc '"+d+"' lw 3", "eps");
                    break;
            }
        }
    }
}