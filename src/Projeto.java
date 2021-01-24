import org.la4j.Matrix;
import org.la4j.decomposition.EigenDecompositor;
import org.la4j.matrix.dense.Basic2DMatrix;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class Projeto {
    public static final double maximo = 99999;
    public static final double minimo = 0.005;
    public static final int formatarVetores = 1;
    public static final int formatarMatrizes = 2;
    public static final int tempoespera1=800;
    public static final int tempoespera2=250;
    public static final int tempoespera3=500;

    public static final int pararIntroducao = 0;
    public static final int totalPopulacao = 1;
    public static final int taxaVariacao = 2;
    public static final int distribuicaoPop= 3;
    public static final int distribuicaoNormalizadaPop = 4;
    public static final int comportamenteAssintotico = 5;
    public static final int graficos = 7;
    public static final int leslie = 6;
    public static final int todaInformacaoSGrafico = 8;
    public static final int todaInformacaoCGrafico = 9;
    public static final int alterar = 10;
    public static final int sair = -1;

    public static final int modointerativo=0;
    public static final int modonaoInterativo=1;
    public static final int detetadoErro=2;
    public static final int tamanhoComando=2;

    public static final int maximoGeracoes=1000;

    static Scanner ler = new Scanner(System.in);

    public static void main(String[] args) throws IOException, InterruptedException {
        boolean existe = false, naoInterativo = false, ordem;
        String nomeFicheiro = null,nomepop=null;
        int[] opcoesExecucao = new int[5];
        int numCiclos = 0, erro = modointerativo; //ERRO 0 - Interativo; ERRO 1- NAO INTERATIVO; ERRO 2- VERDADEIRO ERRO

        if(args.length != 0) {
            if ((args.length == tamanhoComando && !(args[0].equals("-n"))) || (args.length != tamanhoComando && (args[0].equals("-n")))) {
                erro = detetadoErro;
                erro();
            }

            if (args.length>tamanhoComando && !(args[0].equals("-n"))){
                erro = modoNInterativo(opcoesExecucao, args, erro);
            }
        }

        if(erro != detetadoErro) {
            if (erro == modonaoInterativo) {
                naoInterativo = true;
                existe = true;
                numCiclos = opcoesExecucao[0];
                nomeFicheiro = args[(args.length - 2)];
                nomepop=RetirarExtensao(nomeFicheiro);
            }
            //TERMINA AQUI E COMEÇA PARA O MODO INTERATIVO COM INTRODUÇAO DE FICHEIRO

            else if (erro != detetadoErro && args.length == tamanhoComando && args[0].equals("-n")) {
                existe = modoInterativo(args);
                if (existe) {
                    nomeFicheiro = args[1];
                    nomepop=RetirarExtensao(nomeFicheiro);
                }else {
                    erro = detetadoErro;
                    erro();
                }
            }
            //TERMINA AQUI

            double[] populacaoInicial = null; //DECLARAÇÃO VETOR INICIAL
            double[][] matrizLeslie = null;  //DECLARAÇÃO MATRIZ LESLIE

            if (erro != detetadoErro) {
                if (existe) {
                    populacaoInicial = vetorAuto(nomeFicheiro);

                    ordem = tratamentoDadosOrdem((leituraDados(nomeFicheiro, 0)));
                    if (!ordem)
                        erro = detetadoErro;

                    matrizLeslie = new double[populacaoInicial.length][populacaoInicial.length];
                    ordem = matrizAuto(matrizLeslie, nomeFicheiro);

                    if (!ordem || !analisaMatriz(matrizLeslie))
                        erro = detetadoErro;

                    analisaMatriz(matrizLeslie);

                } else {
                    if(args.length==0) {
                        populacaoInicial = vetorManual();
                        if(populacaoInicial != null) {
                            matrizLeslie = matrizManual(populacaoInicial);
                            nomepop = nomeManual();
                            System.out.println();
                        }
                        else {
                            erro = detetadoErro;
                            System.out.println("O vetor populacional está vazio.");
                        }
                    }
                }
                if(nomeFicheiro == null && nomepop == null)
                    erro = detetadoErro;
                if (erro != detetadoErro) {
                    int geracao = -1;

                    double[] populacoesEstimadas = new double[maximoGeracoes+1];
                    double[] taxasDeVariacao = new double[maximoGeracoes+1];
                    double[][] Nt = new double[maximoGeracoes+1][matrizLeslie.length];
                    double[][] distribuicaoNormalizada = new double[maximoGeracoes+1][matrizLeslie.length];
                    double[] vetor = new double[matrizLeslie.length];

                    if (naoInterativo) {
                        nomepop=RetirarExtensao(nomeFicheiro);
                        dadosGeracoes(existe, geracao, numCiclos, Nt, matrizLeslie, populacaoInicial, populacoesEstimadas, taxasDeVariacao, distribuicaoNormalizada, vetor, naoInterativo, opcoesExecucao, args, nomepop);
                    } else {
                        System.out.print("Quantas geracoes pretende que sejam estudadas? ");
                        String nCiclos = ler.next();
                        if (!verificaInteiro(nCiclos)) {
                            do {
                                erro();
                                nCiclos = ler.next();

                            } while (!verificaInteiro(nCiclos));
                        }
                        numCiclos = Integer.parseInt(nCiclos);
                        dadosGeracoes(existe, geracao, numCiclos, Nt, matrizLeslie, populacaoInicial, populacoesEstimadas, taxasDeVariacao, distribuicaoNormalizada, vetor, naoInterativo, opcoesExecucao, args, nomepop);
                    }
                }
                else{
                    if(erro==detetadoErro) {
                        erro();
                    }
                }
            }
        }
    }

    public static void erro(){
        System.out.println("A síntaxe do comando/parâmetros está incorreta ou os ficheiros requisitados não existem.");
    }

    public static boolean modoInterativo(String[] args){
        return existeFicheiro(args[1]);
    }

    public static int modoNInterativo(int[] opcoesExecucao, String[] args, int erro) {
        if (args.length>tamanhoComando && !(args[0].equals("-n"))) {
            String nomeFicheiro = args[(args.length - 2)];
            boolean existe = existeFicheiro(nomeFicheiro);

            if (existe) {
                erro = modonaoInterativo;
                for (int i = 0; i <= (args.length - 2); i++) {
                    if (args[i].equals("-t")) {
                        if (verificaInteiro(args[i + 1])) {
                            if (0 <= Integer.parseInt(args[i + 1]) && Integer.parseInt(args[i + 1]) <= 999) {
                                opcoesExecucao[0] = Integer.parseInt(args[i + 1]);
                            }
                        } else {
                            erro = detetadoErro;
                        }
                    }

                    if (args[i].equals("-g") && erro != detetadoErro) {
                        if (verificaInteiro(args[i + 1])) {
                            if (0 < Integer.parseInt(args[i + 1]) && Integer.parseInt(args[i + 1]) <= 3) {
                                for (int j = 1; j <= 3; j++) {
                                    if (args[i + 1].equals(String.valueOf(j)))
                                        opcoesExecucao[1] = j;
                                }
                            }
                        } else {
                            erro = detetadoErro;
                        }
                    }

                    if (erro != detetadoErro) {
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
                }
            }
        } else{
            erro = detetadoErro;
        }
        if(opcoesExecucao[0] == 0 || opcoesExecucao[1] == 0)  {
            erro = detetadoErro;
        }

        if(erro == detetadoErro)
            erro();

        return erro;
    }

    public static boolean verificaInteiro(String input){
        for(int j = 0; j<input.length(); j++) {
            boolean verificaChar = false;
            for (int i = 0; i < 10; i++) {
                if (input.charAt(j) == String.valueOf(i).charAt(0)) {
                    verificaChar = true;
                }
            }
            if(!verificaChar)
                return false;
        }
        return true;
    }

    public static boolean existeFicheiro(String nomeFicheiro){
        File ficheiroVerificacao = new File(nomeFicheiro);
        boolean existe = ficheiroVerificacao.exists();

        if(existe) {
            if (nomeFicheiro.contains("ç") || nomeFicheiro.contains("ã") || nomeFicheiro.contains("õ")) {
                System.out.println("(O ficheiro deve conter um nome sem caracteres especiais.)");
                return false;
            }
        }

        return existe;
    }

    public static double[] vetorAuto(String nomeFicheiro) throws FileNotFoundException {
        return tratamentoDados((leituraDados(nomeFicheiro, 0)));
    }

    public static boolean matrizAuto(double[][] matrizLeslie, String nomeFicheiro) throws IOException {
        boolean ordem = true;
        for (int i = 1; i <= 2; i++) {
            double[] array = tratamentoDados(leituraDados(nomeFicheiro, i));
            ordem = tratamentoDadosOrdem((leituraDados(nomeFicheiro, i)));
            if(!ordem)
                return false;

            if (array.length == (matrizLeslie.length - 1))
                insercaoMatriz(matrizLeslie, array, 1, true);

            else
                insercaoMatriz(matrizLeslie, array, 2, true);
        }
        return ordem;
    }

    public static boolean analisaMatriz(double[][] matrizLeslie){
        int j = 0;
        for(int i = 1; i<matrizLeslie.length; i++){
            if(matrizLeslie[i][j]<0 || matrizLeslie[i][j]>1)
                return false;
        }
        return true;
    }

    public static double[] vetorManual() {
        double[] array  = new double[1000];
        double[] temporario = insercaoDados1(array);
        int aux = 0;
        for(int i = 0; i<temporario.length; i++){
            if(temporario[i] == 0){
                aux++;
            }
        }
        if(aux == temporario.length)
            return null;
        else
            return temporario;

    }

    public static double[][] matrizManual(double[] populacaoInicial){
        double[][] matrizLeslie = new double[populacaoInicial.length][populacaoInicial.length];
        double[] aux = new double[matrizLeslie.length];

        for (int i = 1; i <= 2; i++) {
            if (i == 1)
                insercaoMatriz(matrizLeslie, insercaoDados2(aux, "Taxa de Sobrevivência"), i, false);
            if (i == 2)
                insercaoMatriz(matrizLeslie, insercaoDados2(aux, "Taxa de Fecundidade"), i, false);
        }

        return matrizLeslie;
    }

    public static String nomeManual(){
        System.out.print("(Para que o gráfico em formato .eps seja devidamente criado, o nome não deve conter caracteres especiais)\nQual é o nome da população que pretende estudar? ");
        String nomePop = ler.next();
        if (nomePop.contains("ç") || nomePop.contains("ã") || nomePop.contains("õ")) {
            do {
                System.out.print("\nNome com caracteres inválidos. Insira outro, por favor: ");
                nomePop = ler.next();
            } while (nomePop.contains("ç") || nomePop.contains("ã") || nomePop.contains("õ"));
        }
        return nomePop;
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
            if(!input.equals("")) {
                if (String.valueOf(input.charAt(0)).equals(letra)) {
                    stringCerta = true;
                }
            }
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

    public static double[] insercaoDados1(double[] array){
        int i = 0;
        double valorPop;
        System.out.println("\nA aplicação está a ser executada em modo interativo com inserção manual de informações. \nPara interromper a inserção de dados da população inicial deve digitar <-1>.\nO elemento diferenciador da parte inteira da decimal tem de ser a vírgula <,>.");
        System.out.println();
        do{
            System.out.print("Insira o valor nº" + (i + 1) + " da População: ");
            valorPop = ler.nextDouble();
            if(valorPop != -1) {
                array[i] = valorPop;
                i++;
            }
            System.out.println();
        } while(valorPop!= -1);

        double [] arrayNovo = new double[i];

        for(int j = 0; j<i; j++) {
            arrayNovo[j] = array[j];
        }
        return arrayNovo;
    }

    public static double[] insercaoDados2(double[] array, String elemento) {
        int limite;
        if(elemento.equalsIgnoreCase("Taxa de Sobrevivência"))
            limite= (array.length-1);
        else
            limite = array.length;

        for(int i = 0; i<limite; i++) {
            System.out.print("Insira o valor nº" + (i + 1) + " da " + elemento + ": ");
            array[i] = ler.nextDouble();

            if(elemento.equalsIgnoreCase("Taxa de Sobrevivência") && (array[i]<0 || array[i]>1)) {
                do {
                    System.out.println("Parâmetro não compatível. Insira um número entre 0 e 1.");
                    System.out.print("Insira o valor nº" + (i + 1) + " da " + elemento + ": ");
                    array[i] = ler.nextDouble();
                } while(array[i]<0 || array[i]>1);
            }

            System.out.println();
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

    public static boolean tratamentoDadosOrdem(String input){
        String[] dadosInseridos = input.split(", ");
        double [] valoresTratados = new double[dadosInseridos.length];
        int [] ordemInseridos = new int[valoresTratados.length];

        for(int i = 0; i< dadosInseridos.length; i++) {
            String[] dadosInseridosTratados = dadosInseridos[i].split("=");
            String construida = "", aux = dadosInseridosTratados[0];

            for(int j = 1; j<aux.length(); j++){
                construida += String.valueOf(dadosInseridosTratados[0].charAt(j));
            }
            ordemInseridos[i] = Integer.parseInt(construida);
        }
        int aux = 0;
        for(int i = 1; i<=ordemInseridos.length; i++){
            if(ordemInseridos[i-1] == aux)
                aux++;
            else
                return false;
        }
        return true;
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

    public static void dadosGeracoes(boolean existe, int geracao,int numCiclos,double [][] Nt,double[][]matrizLeslie,double[] populacaoInicial,double[]populacoesEstimadas,double[]taxasDeVariacao,double[][]distribuicaoNormalizada,double[]vetor,boolean naoInterativo, int[]opcoesExecucao,String[]args, String nomepop) throws IOException, InterruptedException {
        while ((geracao + 1) <= numCiclos) {
            geracao++;
            distribuicaoPopulacao(matrizLeslie, populacaoInicial, Nt, geracao);
            totalPopulacao(geracao,populacoesEstimadas,Nt);
            distribuicaoNormalizada(geracao, Nt, populacoesEstimadas, distribuicaoNormalizada);
        }
        for (int n=0;n<=geracao;n++) {
            taxaVariacao(populacoesEstimadas, n, taxasDeVariacao);
        }
        double valorProprio = calcularVetorValorProprio(matrizLeslie, vetor);
        if (naoInterativo) {
            String output=Criaroutput(nomepop);
            Criarpasta(output);
            escreverParaFicheiro(matrizLeslie,opcoesExecucao, geracao, populacoesEstimadas, taxasDeVariacao, Nt, distribuicaoNormalizada, valorProprio, vetor,output,args);
            Graficonaointerativo(opcoesExecucao,geracao,populacoesEstimadas,taxasDeVariacao,Nt,distribuicaoNormalizada,output);
        } else {
            interfaceUtilizador(existe, numCiclos, matrizLeslie,geracao, populacoesEstimadas, taxasDeVariacao, Nt, distribuicaoNormalizada, valorProprio, vetor, nomepop, args,populacaoInicial);
        }
    }

    public static void interfaceUtilizador(boolean existe, int numCiclos, double[][] matrizLeslie, int geracao, double[] populacoesEstimadas, double[] taxasDeVariacao, double[][] Nt, double[][] distribuicaoNormalizada, double valorProprio, double[] vetorProprio, String nomepop, String[] args,double[] populacaoInicial) throws IOException, InterruptedException {
        int aux=0,leitura;
        boolean naoInterativo=false, ordem;
        do {
            int [] opcoesVisualizacao = new int[todaInformacaoSGrafico-1];
            if(aux == 0){
                infomacao();
            }
            do{
                leitura = ler.nextInt();
                if (leitura>pararIntroducao && leitura <todaInformacaoSGrafico){
                    opcoesVisualizacao[leitura-1]=1;
                } else if (leitura==alterar){
                    aux=1;
                    System.out.println("O que deseja alterar?");
                    System.out.println("<1> - A população em estudo.");
                    System.out.println("<2> - O número de gerações em análise.");
                    int informacao= ler.nextInt();

                    if (informacao==1){
                        if (existe) {
                            System.out.print("Qual o nome do novo ficheiro? ");
                            String nomeFicheiro = ler.next();
                            nomepop=RetirarExtensao(nomeFicheiro);
                            if(!existeFicheiro(nomeFicheiro)){
                                do{
                                    erro();
                                    System.out.print("Insira outro ficheiro: ");
                                    nomeFicheiro = ler.next();
                                    nomepop=RetirarExtensao(nomeFicheiro);
                                    System.out.println();

                                } while(!existeFicheiro(nomeFicheiro));
                            }

                            boolean naoCrescente = false;
                            ordem = tratamentoDadosOrdem((leituraDados(nomeFicheiro, 0)));
                            if(!ordem)
                                naoCrescente = true;
                            populacaoInicial = vetorAuto(nomeFicheiro);
                            ordem = matrizAuto(matrizLeslie, nomeFicheiro);
                            if(!ordem || !analisaMatriz(matrizLeslie))
                                naoCrescente = true;
                            if (naoCrescente) {
                                System.out.print("A síntaxe do comando está incorreta ou os ficheiros requisitados não existem. Por favor insira outro: ");
                                do {
                                    nomeFicheiro = ler.next();
                                    nomepop=RetirarExtensao(nomeFicheiro);
                                    System.out.println();
                                } while (!tratamentoDadosOrdem((leituraDados(nomeFicheiro, 0))) && !matrizAuto(matrizLeslie, nomeFicheiro));
                            }

                        } else {
                            populacaoInicial = vetorManual();
                            matrizLeslie = matrizManual(populacaoInicial);
                            nomepop = nomeManual();
                        }
                    } else if (informacao==2){
                        System.out.print("Quantas gerações deseja analisar? ");
                        numCiclos= ler.nextInt();
                    }
                    geracao = -1;
                    dadosGeracoes(existe, geracao,numCiclos,Nt,matrizLeslie, populacaoInicial,populacoesEstimadas,taxasDeVariacao,distribuicaoNormalizada,vetorProprio,naoInterativo,opcoesVisualizacao,args,nomepop);
                }else if (leitura==todaInformacaoSGrafico){
                    for (int i=0;i<(todaInformacaoSGrafico-2);i++){
                        opcoesVisualizacao[i]=1;
                    }

                } else if (leitura==todaInformacaoCGrafico){
                    for (int i=0;i<(todaInformacaoCGrafico-2);i++){
                        opcoesVisualizacao[i]=1;
                    }
                } else if (leitura!=sair && leitura!=pararIntroducao){
                    System.out.println("O número inserido não corresponde a nenhum parâmetro.\n" + "Insira os números consoante o que deseja visualizar.");
                }
            } while(leitura != todaInformacaoSGrafico && leitura!=alterar && leitura > pararIntroducao && leitura!=todaInformacaoCGrafico);
            escreverParaConsola(matrizLeslie,geracao, populacoesEstimadas, taxasDeVariacao, Nt, distribuicaoNormalizada, valorProprio, vetorProprio,opcoesVisualizacao, nomepop);
        } while(leitura != sair && aux==0);
    }

    public static void infomacao(){
        System.out.println("\nQuais os dados que gostaria de visualizar?\n\nInsira os números associados a cada parâmetro e prima Enter.\nEm parâmetros isolados, apenas insira o número que pretende e prima Enter.\n");
        System.out.println("Exemplo de introdução:\n2\n5\n0\n");
        System.out.println("Exemplo de introdução de parâmetros isolados:\n10\n");
        System.out.println("<"+totalPopulacao+"> - População total a cada geração.");
        System.out.println("<"+taxaVariacao+"> - Taxa de variação.");
        System.out.println("<"+distribuicaoPop+"> - Distribuição da população.");
        System.out.println("<"+distribuicaoNormalizadaPop+"> - Distribuição normalizada da população.");
        System.out.println("<"+comportamenteAssintotico+"> - Comportamento assimtótico associado ao maior valor próprio.");
        System.out.println("<"+leslie+"> - Visualizar dados inseridos (Matriz Leslie).");
        System.out.println("<"+graficos+"> - Gráficos.");
        System.out.println("<"+todaInformacaoSGrafico+"> - Toda a informação excluindo Gráficos. Parâmetro isolado");
        System.out.println("<"+todaInformacaoCGrafico+"> - Toda a informação incluindo Gráficos. Parâmetro isolado");
        System.out.println("<"+pararIntroducao+"> - Quando não quiser inserir mais parâmetros.\n");
        System.out.println(("<"+alterar+"> - Modificar dados de entrada (População em estudo ou Gerações). Parâmetro isolado"));
        System.out.println("<"+sair+"> - Para sair do programa. Parâmetro isolado.");
    }

    public static void distribuicaoPopulacao (double [][] matrizLeslie, double[] populacaoInicial, double[][] Nt,int geracao) { //CALCULO DIMENSAO POPULACAO
        double [][] Lesliemultiplicada = new double[matrizLeslie.length][matrizLeslie.length];
        copiarMatriz(matrizLeslie,Lesliemultiplicada);

        for(int temp = geracao; temp>=2;temp--){
            multiplicarMatrizesQuadradas(matrizLeslie,Lesliemultiplicada);
        }

        if (geracao==0){
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
    }

    public static void totalPopulacao (int geracao, double[]populacoesEstimadas,double [][] Nt){
        populacoesEstimadas[geracao]=0;
        for (int i=0;i< Nt[0].length;i++){
            populacoesEstimadas[geracao]+=Nt[geracao][i];
        }
    }

    public static void copiarMatriz (double[][] matrizLeslie, double[][] copia){
        for(int i =0 ; i<matrizLeslie.length; i++){
            for(int j =0 ; j< matrizLeslie[0].length; j++){
                copia[i][j]=matrizLeslie[i][j];
            }
        }
    }

    public static void taxaVariacao(double [] populacoesEstimadas, int geracao, double[] taxasDeVariacao) {
        double variacao=populacoesEstimadas[geracao+1]/populacoesEstimadas[geracao];
        taxasDeVariacao[geracao]=variacao;
    }

    public static void multiplicarMatrizesQuadradas (double [][] matriz1, double[][] matriz2){
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

    public static void distribuicaoNormalizada (int geracao,double[][] Nt, double [] populacoesEstimadas, double[][] distribuicaoNormalizada){
        for (int i=0;i<distribuicaoNormalizada[0].length;i++){
            distribuicaoNormalizada[geracao][i]=Nt[geracao][i]*100/populacoesEstimadas[geracao];
        }
    }

    public static double calcularVetorValorProprio (double[][] matrizLeslie,double[] vetor) {
        Matrix matriz = new Basic2DMatrix(matrizLeslie);
        EigenDecompositor eigenD = new EigenDecompositor(matriz);
        Matrix[] decomposicaoMatriz = eigenD.decompose();
        double[][] vetoraux = decomposicaoMatriz[0].toDenseMatrix().toArray();
        double[][] valor = decomposicaoMatriz[1].toDenseMatrix().toArray();

        int coluna = calcularMaiorValorProprio(valor);
        double maior = valor[coluna][coluna];

        for (int i = 0; i < matrizLeslie.length; i++) {
            vetor[i] = vetoraux[i][coluna];
        }
        normalizarVetorProprio(vetor);
        return maior;
    }

    public static int calcularMaiorValorProprio (double[][] valor){
        double maior = 0;
        int coluna = 0;
        for (int l = 0; l < valor.length; l++) {
            valor[l][l]=valorModulo(valor[l][l]);
            if (valor[l][l] > maior) {
                maior = valor[l][l];
                coluna = l;
            }
        }
        return coluna;
    }

    public static double valorModulo(double num){
        if (num< 0) {
            num=(-num);
        }
        return num;
    }

    public static void normalizarVetorProprio (double[] vetor){
        double soma=0;
        for (int i=0;i<vetor.length;i++) {
            soma += vetor[i];
        }
        for (int i=0;i<vetor.length;i++){
            vetor[i]=(vetor[i]/soma)*100;
        }
    }

    public static void escreverParaFicheiro (double[][] matrizLeslie, int[] opcoesExecucao, int geracao, double [] populacoesEstimadas, double [] taxasDeVariacao, double [][] Nt, double [][] distribuicaoNormalizada,double valorProprio, double[] vetorProprio,String output,String [] args) throws IOException {
        String nomesaida = args[args.length-1];
        File file = new File(output+"/"+nomesaida);
        PrintWriter out = new PrintWriter(file);
        boolean flag;
        int c;
        out.println("De acordo com os dados inseridos foram obtidos os seguintes resultados:");
        out.println("Matriz Leslie");
        for (int l=0;l<matrizLeslie.length;l++){
            for (c=0;c<matrizLeslie[0].length;c++){
                out.print(matrizLeslie[l][c]+" ");
            }
            out.println();
        }
        for (int j = 0; j <= geracao; j++) {
            if (opcoesExecucao[3] == 1) {
                out.print("\nO número total de indivíduos da geração " + j + " é ");
                flag = NotCientifica(populacoesEstimadas[j]);
                if (flag) {
                    out.printf(ConverterNotacaoCientifica(populacoesEstimadas[j]));
                } else {
                    flag = DoubleparaIntVer(populacoesEstimadas[j]);
                    if (flag) {
                        out.printf(DoubleToInt(populacoesEstimadas[j]));
                    } else {
                        out.printf("%.2f", populacoesEstimadas[j]);
                    }
                }
            }
        }
        out.print("\n");
        for (int j = 0; j < geracao; j++) {
            if (opcoesExecucao[4] == 1) {
                out.print("\nA taxa de variação para a geração " + j + " é ");
                flag = NotCientifica(taxasDeVariacao[j]);
                if (flag) {
                    out.printf(ConverterNotacaoCientifica(taxasDeVariacao[j]));
                } else {
                    flag = DoubleparaIntVer(taxasDeVariacao[j]);
                    if (flag) {
                        out.printf(DoubleToInt(taxasDeVariacao[j]));
                    } else {
                        out.printf("%.2f", taxasDeVariacao[j]);
                    }
                }
            }
        }
        out.print("\n");
        out.println("\nDistribuição da população:");
        for (int j = 0; j <= geracao; j++) {
            out.println("\nA população na geração " + j + " encontra-se distribuída da seguinte forma:");
            for (c = 0; c < Nt[0].length; c++) {
                out.print("Idade " + c + ": ");
                flag = NotCientifica(Nt[j][c]);
                if (flag) {
                    out.printf(ConverterNotacaoCientifica(Nt[j][c]) + "\n");
                } else {
                    flag = DoubleparaIntVer(Nt[j][c]);
                    if (flag) {
                        out.printf(DoubleToInt(Nt[j][c]) + "\n");
                    } else {
                        out.printf("%.2f\n", Nt[j][c]);
                    }
                }
            }
        }
        out.print("\nDistribuição normalizada:\n");
        for (int j = 0; j <= geracao; j++) {
            out.print("\nA distribuição normalizada da geração " + j + " está representada pelas várias faixas etárias:\n");
            for (c = 0; c < Nt[0].length; c++) {
                out.print("Idade " + c + ": ");
                flag = NotCientifica(distribuicaoNormalizada[j][c]);
                if (flag) {
                    out.printf(ConverterNotacaoCientifica(distribuicaoNormalizada[j][c]) + "\n");
                } else {
                    flag = DoubleparaIntVer(distribuicaoNormalizada[j][c]);
                    if (flag) {
                        out.printf(DoubleToInt(distribuicaoNormalizada[j][c]) + "\n");
                    } else {
                        out.printf("%.2f", distribuicaoNormalizada[j][c]);
                        out.print("%\n");
                    }
                }
            }
        }
        if (opcoesExecucao[2] == 1) {
            out.println("\nComportamento Assintótico da população associado ao maior valor próprio.");
            out.printf("\nO valor próprio de módulo máximo é aproximadamente: %.4f\n", valorProprio);
            out.println("\nO vetor próprio associado ao maior valor próprio é:");
            for (c = 0; c < Nt[0].length; c++) {
                out.print("Idade " + c + ": ");
                flag = NotCientifica(vetorProprio[c]);
                if (flag) {
                    out.printf(ConverterNotacaoCientifica(vetorProprio[c]) + "\n");
                } else {
                    flag = DoubleparaIntVer(vetorProprio[c]);
                    if (flag) {
                        out.printf(DoubleToInt(vetorProprio[c]) + "\n");
                    } else {
                        out.printf("%.2f", vetorProprio[c]);
                        out.print("\n");
                    }
                }
            }
        }
        out.print("\nEncontra-se concluída a apresentação dos resultados do programa da evolução das espécies.");
        out.close();
    }

    public static void escreverParaConsola (double[][] matrizLeslie,int geracao, double [] populacoesEstimadas, double [] taxasDeVariacao, double [][] Nt, double [][] distribuicaoNormalizada,double valorProprio, double[] vetorProprio,int[] opcoesVisualizaco, String nomepop) throws IOException, InterruptedException {
        int c, num;
        if (opcoesVisualizaco[totalPopulacao-1]==1){
            for (int j = 0; j <= geracao; j++) {
                System.out.print("\nO número total de indivíduos da geração " + j + " é ");
                Escrever(populacoesEstimadas[j], formatarVetores);
            }
        }
        System.out.print("\n");
        if (opcoesVisualizaco[taxaVariacao-1]==1){
                for (int j = 0; j < geracao; j++) {
                    System.out.print("\nA taxa de variação para a geração " + j + " é ");
                    Escrever(taxasDeVariacao[j], formatarVetores);
                }
        }
        System.out.print("\n");
        if (opcoesVisualizaco[distribuicaoPop-1]==1) {
            System.out.println("\nDistribuição da população:");
            for (int j = 0; j <= geracao; j++) {
                System.out.print("\nA população na geração " + j + " encontra-se distribuída da seguinte forma:\n");
                for (c = 0; c < Nt[0].length; c++) {
                    System.out.print("Idade " + c + ": ");
                    Escrever(Nt[j][c], formatarMatrizes);
                }
            }
        }
        if (opcoesVisualizaco[distribuicaoNormalizadaPop-1]==1) {
            System.out.print("\nDistribuição normalizada:\n");
            for (int j = 0; j <= geracao; j++) {
                System.out.print("\nA distribuição normalizada da geração " + j + " está representada pelas várias faixas etárias:\n");
                for (c = 0; c < Nt[0].length; c++) {
                    System.out.print("Idade " + c + ": ");
                    Escrever(distribuicaoNormalizada[j][c], formatarMatrizes);
                }
            }
        }
        if (opcoesVisualizaco[comportamenteAssintotico-1]==1){
            System.out.println("\nComportamento Assintótico da população associado ao maior valor próprio.");
            System.out.printf("\nO valor próprio de módulo máximo é aproximadamente: %.4f\n", valorProprio);
            System.out.println("\nO vetor próprio associado ao maior valor próprio é:");
            for (c = 0; c < Nt[0].length; c++) {
                System.out.print("Idade " + c + ": ");
                Escrever(vetorProprio[c], formatarMatrizes);
            }
        }
        if (opcoesVisualizaco[leslie-1]==1){
            System.out.println("\nMatriz Leslie");
            for (int l=0;l<matrizLeslie.length;l++){
                for (c=0;c<matrizLeslie[0].length;c++){
                    System.out.print(matrizLeslie[l][c]+" ");
                }
                System.out.println();
            }
        }
        System.out.println();
        if (opcoesVisualizaco[graficos-1]==1){
            System.out.println("Que gráfico quer representar?");
            System.out.println("<1>-Evolução da População Total;\n<2>-Evolução da taxa de variação;\n<3>-Distribuição da População;\n<4>-Distribuição normalizada da população.");
            num = ler.nextInt();
            if(num<1 || num>4) {
                do {
                    System.out.println("O número inserido não corresponde a nenhum parâmetro.\nInsira um número consoante o gráfico que deseja visualizar.");
                    num = ler.nextInt();
                } while (num < 1 || num > 4);
            }
            Graficosinterativo(matrizLeslie,geracao,populacoesEstimadas,taxasDeVariacao,Nt,distribuicaoNormalizada,num, nomepop,valorProprio,vetorProprio);
        }
    }

    public static void EscreverGrafico1e2(int geracao,double[] populacoesEstimadas) throws FileNotFoundException {
        File file = new File("valores.txt");
        PrintWriter out = new PrintWriter(file);
        for (int l = 0; l <= geracao; l++) {
            out.print(l + " " + populacoesEstimadas[l]+"\n");
        }

        out.close();
    }

    public static void EscreverGrafico3e4(int n,double[][] Nt,int geracao) throws FileNotFoundException {
        File file = new File("valores.txt");
        PrintWriter out = new PrintWriter(file);
        for (int l = 0; l <= geracao; l++) {
            out.print(l + " ");
            for (int c = 0; c < n; c++) {
                out.print(Nt[l][c]+" ");
            }
            out.print("\n");
        }
        out.close();
    }

    public static void SalvarGrafico(String codigognuplot,String terminal,String nomegrafico) throws IOException {
        Runtime  rt = Runtime.getRuntime();
        rt.exec("gnuplot -e \"set terminal "+terminal+"; set output '"+nomegrafico+"'; "+codigognuplot+"\"");
    }

    public static void PerguntaGrafico(String codigognuplot,int geracao,int geracao1,double[]populacoesEstimadas,double[]taxasDeVariacao,double[][] Nt,double [][] distribuicaoNormalizada,double valorProprio, double[] vetorProprio,double[][]matrizLeslie,String nomepop,String output,String nome) throws IOException, InterruptedException {
        int resposta;
        String nomegrafico=nome+geracao1+"G_"+output;
        File graficopng = new File("Grafico.png");
        TimeUnit.MILLISECONDS.sleep(tempoespera1);
        System.out.println("Deseja Salvar o Gráfico?(1- Sim; 2- Não)");
        resposta = ler.nextInt();
        if (resposta == 1) {
            Criarpasta(output);
            System.out.println("Qual o formato do ficheiro?");
            System.out.println("1- Formato PNG;\n2- Formato TXT;\n3- Formato EPS.");
            do {
                resposta = ler.nextInt();
                switch (resposta) {
                    case 1:
                        gerarGrafico(nomegrafico,"png","png",output,codigognuplot);
                        break;
                    case 2:
                        gerarGrafico(nomegrafico,"dumb","txt",output,codigognuplot);
                        break;
                    case 3:
                        gerarGrafico(nomegrafico,"eps","eps",output,codigognuplot);
                        break;
                    default:
                        System.out.println("O número inserido não corresponde a nenhum parãmetro.\n" + "Insira um número consoante o que deseja realizar.");
                }
            }while(resposta<1 || resposta>3);
        } else if (resposta == 2) {
        }else{
            System.out.println("O número inserido não corresponde a nenhum parãmetro.\n" + "Insira um número consoante o que deseja realizar.");
        }
        VisualizarMaisAlgumGrafico(matrizLeslie,geracao, populacoesEstimadas, taxasDeVariacao, Nt, distribuicaoNormalizada, valorProprio, vetorProprio, nomepop);
        EliminarFicheiroTextoGrafico(graficopng);
    }

    public static void VisualizarMaisAlgumGrafico (double[][] matrizLeslie,int geracao, double[]populacoesEstimadas,double[]taxasDeVariacao,double[][]Nt,double[][] distribuicaoNormalizada,double valorProprio,double[]vetorProprio,String nomepop) throws IOException, InterruptedException {
        int resposta;
        System.out.println("Deseja visualizar mais algum gráfico? (1- Sim; 2- Não)");
        do {
            resposta= ler.nextInt();
            if (resposta == 1) {
                int[] opcoesVisualizacao = new int[7];
                opcoesVisualizacao[6] = 1;
                escreverParaConsola(matrizLeslie,geracao, populacoesEstimadas, taxasDeVariacao, Nt, distribuicaoNormalizada, valorProprio, vetorProprio, opcoesVisualizacao, nomepop);
            } else if (resposta == 2) {
            } else {
                System.out.println("O número inserido não corresponde a nenhum parâmetro.\n" + "Insira um número consoante o que deseja realizar.");
            }
        }while(resposta<1 ||resposta>2);
    }

    public static String CodigoGrafico3e4(int n,String ylabel,String titulo){
        int o=3;
        String s = "set xlabel 'Gerações'; set ylabel '"+ylabel+"' ; set title '"+titulo+"' font 'arial,20'; set palette rgb 7,5,15; plot 'valores.txt' u 1:2 w lp t 'Idade 0' lw 3";
        for(int e=1;e<n+1;e++) {
            String d = " ,'valores.txt' u 1:" + o + "w lp t 'Idade "+e+"' lw 3";
            s = s+d;
            o++;
        }
        return s;
    }

    public static void Criarpng(String d) throws IOException, InterruptedException {
        SalvarGrafico(d,"png","Grafico.png");
        File file = new File("Grafico.png");
        TimeUnit.MILLISECONDS.sleep(tempoespera1);
        Desktop desktop = Desktop.getDesktop();
        if(file.exists()) desktop.open(file);
    }

    public static void Criarpasta(String output) {
        File file = new File(output);
        file.mkdir();
    }

    public static void Graficosinterativo(double[][] matrizLeslie,int geracao,double[] populacoesEstimadas,double[] taxasDeVariacao,double[][] Nt,double[][] distribuicaoNormalizada,int num, String nomepop,double valorProprio,double[] vetorProprio) throws IOException, InterruptedException {
        String output = Criaroutput(nomepop);
        File novofich = new File("valores.txt");
        int [] opcoesVisualizacao = new int[todaInformacaoSGrafico-1];
        switch(num){
            case 1:
                String codigognuplot="set xlabel 'Gerações'; set ylabel 'População' ; set title 'População total' font 'arial,20'; plot 'valores.txt' title 'População Total' with lines lc 'blue' lw 3";
                Grafico1e2interativo(matrizLeslie,geracao,populacoesEstimadas,taxasDeVariacao,Nt,distribuicaoNormalizada,nomepop,valorProprio,vetorProprio,opcoesVisualizacao,output,novofich,codigognuplot,populacoesEstimadas,totalPopulacao,"PopulacaoTotal_",geracao);
                break;
            case 2:
                int geracao1=geracao-1;
                codigognuplot="set xlabel 'Gerações'; set ylabel 'Taxa de Variação' ; set title 'Taxa de Variação' font 'arial,20'; plot 'valores.txt' title 'Taxa de Variação' with lines lc 'red' lw 3";
                Grafico1e2interativo(matrizLeslie,geracao,populacoesEstimadas,taxasDeVariacao,Nt,distribuicaoNormalizada,nomepop,valorProprio,vetorProprio,opcoesVisualizacao,output,novofich,codigognuplot,taxasDeVariacao,taxaVariacao,"TaxadeVariacao_",geracao1);
                break;
            case 3:
                Grafico3e4interativo(matrizLeslie,geracao,geracao,populacoesEstimadas,taxasDeVariacao,Nt,distribuicaoNormalizada,nomepop,valorProprio,vetorProprio,opcoesVisualizacao,output,novofich,distribuicaoPop,"População","População Distribuida","PopulacaoDistribuida_",Nt);
                break;
            case 4:
                Grafico3e4interativo(matrizLeslie,geracao,geracao,populacoesEstimadas,taxasDeVariacao,Nt,distribuicaoNormalizada,nomepop,valorProprio,vetorProprio,opcoesVisualizacao,output,novofich,distribuicaoNormalizadaPop,"Distribuição","Distribuição Normalizada","PopulacaoNormalizada_",distribuicaoNormalizada);
                break;
        }
    }

    public static boolean NotCientifica(double numero) {
        boolean flag=false;
        if (numero > maximo || numero <= minimo) {
            flag = true;
        }
        return flag;
    }

    public static String ConverterNotacaoCientifica(double numero) {
        NumberFormat formatter;
        formatter = new DecimalFormat("0.##E0");
        return formatter.format(numero);
    }

    public static boolean DoubleparaIntVer(double num) {
        boolean flag=false;
        if(num % 1 == 0) {
            flag = true;
        }
        return flag;
    }

    public static String DoubleToInt(double num) {
        String s = String.valueOf(num);
        DecimalFormat decimalFormat = new DecimalFormat("0.#####");
        return decimalFormat.format(Double.valueOf(s));
    }

    public static String determinarDataCriacao(){
        String nomeFich = "./";
        File file = new File(nomeFich);
        SimpleDateFormat formato = new SimpleDateFormat("dd_MM_yyyy");
        return formato.format(file.lastModified());
    }

    public static void EliminarFicheiroTextoGrafico(File file) {
        file.delete();
    }

    public static String RetirarExtensao(String palavra) {
        StringBuilder builder = new StringBuilder(palavra);
        for (int i = 1; i <= 4; i++) {
            builder.deleteCharAt(palavra.length()-i);
        }
        palavra = builder.toString();
        return palavra;
    }

    public static void Graficonaointerativo(int[] opcoesExecucao,int geracao,double[] populacoesEstimadas,double[] taxasDeVariacao,double[][] Nt,double[][] distribuicaoNormalizada,String output) throws IOException, InterruptedException {
        int num=opcoesExecucao[1];
        File novofich = new File("valores.txt");
        Criarpasta(output);
        if (opcoesExecucao[3]==1) {
            String codigognuplot = "set xlabel 'Gerações'; set ylabel 'População' ; set title 'População total' font 'arial,20'; plot 'valores.txt' title 'População Total' with lines lc 'blue' lw 3";
            Grafico1e2naointerativo(codigognuplot,geracao,populacoesEstimadas,num,output,"PopulacaoTotal_");
        }
        if (opcoesExecucao[4]==1) {
            String codigognuplot = "set xlabel 'Gerações'; set ylabel 'Taxa de Variação' ; set title 'Taxa de Variação' font 'arial,20'; plot 'valores.txt' title 'Taxa de Variação' with lines lc 'red' lw 3";
            Grafico1e2naointerativo(codigognuplot,(geracao-1),taxasDeVariacao,num,output,"TaxadeVariacao_");
        }
        Grafico3e4naointerativo(geracao,num,output,Nt,"População","População Distribuida","PopulacaoDistribuida_");
        Grafico3e4naointerativo(geracao,num,output,distribuicaoNormalizada,"Distribuição","Distribuição Normalizada","DistribuicaoNormalizada_");
        EliminarFicheiroTextoGrafico(novofich);
    }

    public static void PerguntaGraficoNaoInterativo(String codigognuplot,int num,String output,String nome, int geracao) throws IOException, InterruptedException {
        String nomegrafico=nome+ geracao + "G_" + output;
        switch (num) {
            case 1:
                gerarGrafico(nomegrafico,"png","png",output,codigognuplot);
                break;
            case 2:
                gerarGrafico(nomegrafico,"dumb","txt",output,codigognuplot);
                break;
            case 3:
                gerarGrafico(nomegrafico,"eps","eps",output,codigognuplot);
                break;
        }
    }

    public static void Escrever(double numero, int n) {
        boolean flag;
        flag = NotCientifica(numero);
        if (flag) {
            if (n == formatarVetores) {
                System.out.print(ConverterNotacaoCientifica(numero));
            } else {
                System.out.print(ConverterNotacaoCientifica(numero) + "\n");
            }
        } else {
            flag = DoubleparaIntVer(numero);
            if (flag) {
                if (n == formatarVetores) {
                    System.out.print(DoubleToInt(numero));
                } else {
                    System.out.print(DoubleToInt(numero) + "\n");
                }
            } else {
                if (n == formatarVetores) {
                    System.out.printf("%.2f", numero);
                } else {
                    System.out.printf("%.2f\n", numero);
                }
            }
        }
    }

    public static String Criaroutput(String nomepop){
        String tempo = determinarDataCriacao();
        return nomepop + "_" + tempo;
    }

    public static void Moverficheiro(String nomegrafico,String output) throws IOException {
        String deficheiro = nomegrafico;
        String paraficheiro = output+"\\"+nomegrafico;
        Path original = Paths.get(deficheiro);
        Path pasta = Paths.get(paraficheiro);
        Files.move(original,pasta,StandardCopyOption.REPLACE_EXISTING);
    }

    public static void Grafico1e2interativo(double[][] matrizLeslie,int geracao,double[] populacoesEstimadas,double[] taxasDeVariacao,double[][] Nt,double[][] distribuicaoNormalizada, String nomepop,double valorProprio,double[] vetorProprio,int [] opcoesVisualizacao,String output,File novofich,String codigognuplot,double[] variavel,int totalPopulacao,String nome,int geracao1) throws IOException, InterruptedException {
        opcoesVisualizacao[totalPopulacao-1]=1;
        EscreverGrafico1e2(geracao1,variavel);
        Criarpng(codigognuplot);
        escreverParaConsola(matrizLeslie,geracao,populacoesEstimadas,taxasDeVariacao,Nt,distribuicaoNormalizada,valorProprio,vetorProprio,opcoesVisualizacao,nomepop);
        PerguntaGrafico(codigognuplot,geracao,geracao1,populacoesEstimadas,taxasDeVariacao,Nt,distribuicaoNormalizada,valorProprio,vetorProprio,matrizLeslie,nomepop,output,nome);
        EliminarFicheiroTextoGrafico(novofich);
    }

    public static void Grafico3e4interativo(double[][] matrizLeslie,int geracao,int geracao1,double[] populacoesEstimadas,double[] taxasDeVariacao,double[][] Nt,double[][] distribuicaoNormalizada,String nomepop,double valorProprio,double[] vetorProprio,int [] opcoesVisualizacao,String output,File novofich,int variavel,String ylabel,String titulo,String nome,double[][] Escrever1) throws IOException, InterruptedException {
        opcoesVisualizacao[variavel-1]=1;
        EscreverGrafico3e4(Nt[0].length,Escrever1,geracao);
        String s=CodigoGrafico3e4(Nt[0].length,ylabel,titulo);
        Criarpng(s);
        escreverParaConsola(matrizLeslie,geracao,populacoesEstimadas,taxasDeVariacao,Nt,distribuicaoNormalizada,valorProprio,vetorProprio,opcoesVisualizacao,nomepop);
        PerguntaGrafico(s,geracao,geracao1,populacoesEstimadas,taxasDeVariacao,Nt,distribuicaoNormalizada,valorProprio,vetorProprio,matrizLeslie,nomepop,output,nome);
        EliminarFicheiroTextoGrafico(novofich);
    }

    public static void Grafico1e2naointerativo(String codigognuplot,int geracao,double[] populacoesEstimadas,int num,String output,String nome) throws InterruptedException, IOException {
        EscreverGrafico1e2(geracao, populacoesEstimadas);
        PerguntaGraficoNaoInterativo(codigognuplot, num, output, nome, geracao);
        TimeUnit.MILLISECONDS.sleep(tempoespera2);
    }

    public static void Grafico3e4naointerativo(int geracao,int num,String output,double[][] Nt,String ylabel,String titulo,String nome) throws InterruptedException, IOException {
        EscreverGrafico3e4(Nt[0].length,Nt,geracao);
        String s=CodigoGrafico3e4(Nt[0].length,ylabel,titulo);
        PerguntaGraficoNaoInterativo(s,num,output,nome, geracao);
        TimeUnit.MILLISECONDS.sleep(tempoespera2);
    }

    public static void gerarGrafico(String nomegrafico,String terminal,String extensao,String output,String codigognuplot) throws InterruptedException, IOException {
        String nomegrafico2=nomegrafico+"."+extensao;
        SalvarGrafico(codigognuplot, terminal,nomegrafico2);
        TimeUnit.MILLISECONDS.sleep(tempoespera3);
        Moverficheiro(nomegrafico2,output);
    }
}