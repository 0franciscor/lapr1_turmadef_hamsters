import org.la4j.Matrix;
import org.la4j.decomposition.EigenDecompositor;
import org.la4j.matrix.dense.Basic2DMatrix;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Scanner;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;

public class Projeto {
    public static final double maximo = 99999;
    public static final double minimo = 0.005;
    public static final int constante = 1;
    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd_MM_yyyy");
    static Scanner ler = new Scanner(System.in);

    public static void main(String[] args) throws IOException, InterruptedException {
        boolean existe = false, naoInterativo = false;
        String nomeFicheiro = null;
        int[] opcoesExecucao = new int[5];
        int numCiclos = 0, erro = 0; //ERRO 0 - Interativo; ERRO 1- NAO INTERATIVO; ERRO 2- VERDADEIRO ERRO
        File novofich = new File("valores.txt");

        //RESPOSAVEL POR VERIFICAR SE O CODIGO ESTA A CORRER EM MODO NAO INTERATIVO
        erro = modoNInterativo(opcoesExecucao, args, erro);
        if (erro == 1) {
            naoInterativo = true;
            existe = true;
            numCiclos = opcoesExecucao[0];
            nomeFicheiro = args[(args.length - 2)];
        }
        //TERMINA AQUI E COMEÇA PARA O MODO INTERATIVO COM INTRODUÇAO DE FICHEIRO

        else if (erro != 2 && args.length == 2) {
            existe = modoInterativo(args);
            nomeFicheiro = args[1];
        }
        //TERMINA AQUI

        double[] populacaoInicial; //DECLARAÇÃO VETOR INICIAL
        double[][] matrizLeslie;  //DECLARAÇÃO MATRIZ LESLIE

        if (erro != 2) {
            if (existe) {
                populacaoInicial = vetorAuto(nomeFicheiro);
                matrizLeslie = new double[populacaoInicial.length][populacaoInicial.length];
                matrizAuto(matrizLeslie, nomeFicheiro);

            } else {
                populacaoInicial = vetorManual();
                matrizLeslie = matrizManual(populacaoInicial);
            }

            int geracao = -1;

            double[] populacoesEstimadas = new double[1000];
            double[] taxasDeVariacao = new double[1000];
            double[][] Nt = new double[1001][matrizLeslie.length];
            double[][] distribuicaoNormalizada = new double[1000][matrizLeslie.length];
            double[] vetor = new double[matrizLeslie.length];
            double valorProprio;
            valorProprio = calcularVetorValorProprio(matrizLeslie, vetor);

            if (naoInterativo) {
                dadosGeracoes(existe, geracao, numCiclos,Nt, matrizLeslie, populacaoInicial, populacoesEstimadas, taxasDeVariacao, distribuicaoNormalizada, valorProprio, vetor, naoInterativo, opcoesExecucao, args, nomeFicheiro);
            } else {
                System.out.println("Quais as geracoes que pretende que sejam estudadas?");
                numCiclos = ler.nextInt();
                dadosGeracoes(existe, geracao, numCiclos, Nt, matrizLeslie, populacaoInicial, populacoesEstimadas, taxasDeVariacao, distribuicaoNormalizada, valorProprio, vetor, naoInterativo, opcoesExecucao, args, nomeFicheiro);
            }
        }
        EliminarFicheiroTextoGrafico(novofich);
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
        if (args.length>2) {
            String nomeFicheiro = args[(args.length - 2)];
            boolean existe = existeFicheiro(nomeFicheiro);

            if (existe) {
                erro = 1;
                opcoesExecucao[0] = Integer.parseInt(args[1]);
                for (int i = 2; i <= (args.length - 2); i++) {
                    if(args[i].equals("-t")){
                        if(Integer.parseInt(args[i+1]) <= 999 && 0 <= Integer.parseInt(args[i+1])){
                            opcoesExecucao[0] = Integer.parseInt(args[i+1]);
                        }
                    }
                    if (args[i].equals("-g")) {
                        if (0 < Integer.parseInt(args[i + 1]) && Integer.parseInt(args[i+1]) <= 3){
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

    public static boolean existeFicheiro(String nomeFicheiro){
        File ficheiroVerificacao = new File(nomeFicheiro);
        return ficheiroVerificacao.exists();
    }

    public static double[] vetorAuto(String nomeFicheiro) throws FileNotFoundException {
        return tratamentoDados((leituraDados(nomeFicheiro, 0)));
    }

    public static void matrizAuto(double[][] matrizLeslie, String nomeFicheiro) throws IOException {
        for (int i = 1; i <= 2; i++) {
            double[] array = tratamentoDados(leituraDados(nomeFicheiro, i));

            if (array.length == (matrizLeslie.length - 1))
                insercaoMatriz(matrizLeslie, array, 1, true);
            else
                insercaoMatriz(matrizLeslie, array, 2, true);
        }
    }

    public static double[] vetorManual() {
        double[] array  = new double[1000];

        return insercaoDados1(array);
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
        System.out.println("A aplicação está a ser executada em modo interativo com inserção manual de ficheiros. Assim, para interromper a inserção de dados, deve digitar <-1> aquando da inserção.");
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

    public static void dadosGeracoes(boolean existe, int geracao,int numCiclos,double [][] Nt,double[][]matrizLeslie,double[] populacaoInicial,double[]populacoesEstimadas,double[]taxasDeVariacao,double[][]distribuicaoNormalizada,double valorProprio,double[]vetor,boolean naoInterativo, int[]opcoesExecucao,String[]args, String nomepop) throws IOException, InterruptedException {
        while ((geracao + 1) <= numCiclos) {
            geracao++;
            procedimentoCalculoGeracoes(Nt, geracao, matrizLeslie, populacaoInicial, populacoesEstimadas, distribuicaoNormalizada);
        }
        for (int n=0;n<=geracao;n++) {
            TaxaVariacao(populacoesEstimadas, n, taxasDeVariacao);
        }
        if (naoInterativo) {
            escreverParaFicheiro(opcoesExecucao, geracao, populacoesEstimadas, taxasDeVariacao, Nt, distribuicaoNormalizada, valorProprio, vetor, args);
            Graficonaointerativo(opcoesExecucao,geracao,populacoesEstimadas,taxasDeVariacao,Nt,distribuicaoNormalizada,nomepop);
        } else {
            interfaceUtilizador(existe, numCiclos, matrizLeslie,geracao, populacoesEstimadas, taxasDeVariacao, Nt, distribuicaoNormalizada, valorProprio, vetor, nomepop, args);
        }
    }

    public static void interfaceUtilizador(boolean existe, int numCiclos, double[][] matrizLeslie, int geracao, double[] populacoesEstimadas, double[] taxasDeVariacao, double[][] Nt, double[][] distribuicaoNormalizada, double valorProprio, double[] vetorProprio, String nomepop, String[] args) throws IOException, InterruptedException {
        int leitura;
        boolean naoInterativo=false;
        do {
            int [] opcoesVisualizacao = new int[6];
            System.out.println("\nQuais os dados que gostaria de visualizar? (Insira os números associados a cada parâmetro e prima Enter. Em parâmetros isolados, apenas insira o número que pretende.)");
            System.out.println("<1> - População total a cada geração.");
            System.out.println("<2> - Taxa de variação.");
            System.out.println("<3> - Distribuição da população.");
            System.out.println("<4> - Distribuição normalizada da população.");
            System.out.println("<5> - Comportamento assimtótico associado ao maior valor próprio.");
            System.out.println("<6> - Gráficos.");
            System.out.println("<0> - Quando não quiser inserir mais parâmetros.");
            System.out.println(("<10> - Modificar dados de entrada. Parâmetro isolado"));
            System.out.println("<-1> - Para sair do programa. Parâmetro isolado.");
            do{
                leitura = ler.nextInt();
                if (leitura>0 && leitura !=10){
                    opcoesVisualizacao[leitura-1]=1;
                } else if (leitura==10){
                    double[] populacaoInicial;
                    if (existe) {
                        System.out.print("Qual o nome do novo ficheiro? ");
                        String nomeFicheiro = ler.next();
                        populacaoInicial = vetorAuto(nomeFicheiro);
                        matrizAuto(matrizLeslie, nomeFicheiro);
                    } else {
                        populacaoInicial = vetorManual();
                        matrizLeslie = matrizManual(populacaoInicial);
                    }

                    valorProprio = calcularVetorValorProprio(matrizLeslie, vetorProprio);
                    geracao = -1;
                    dadosGeracoes(existe, geracao,numCiclos,Nt,matrizLeslie, populacaoInicial,populacoesEstimadas,taxasDeVariacao,distribuicaoNormalizada,valorProprio,vetorProprio,naoInterativo,opcoesVisualizacao,args,nomepop);
                }
            } while(leitura > 0);

            escreverParaConsola(geracao, populacoesEstimadas, taxasDeVariacao, Nt, distribuicaoNormalizada, valorProprio, vetorProprio,opcoesVisualizacao, nomepop);

        } while(leitura != -1);
    }

    public static void procedimentoCalculoGeracoes(double[][] Nt, int geracao, double[][] matrizLeslie, double[] populacaoInicial, double[] populacoesEstimadas, double[][] distribuicaoNormalizada){
        distribuicaoPopulacao(matrizLeslie, populacaoInicial, Nt, geracao);
        totalPopulacao(geracao,populacoesEstimadas,Nt);
        distribuicaoNormalizada(geracao, Nt, populacoesEstimadas, distribuicaoNormalizada);

    }

    public static void distribuicaoPopulacao (double [][] matrizLeslie, double[] populacaoInicial, double[][] Nt,int geracao) { //CALCULO DIMENSAO POPULACAO
        double [][] Lesliemultiplicada = new double[matrizLeslie.length][matrizLeslie.length];
        copiarMatriz(matrizLeslie,Lesliemultiplicada);

        for(int temp = geracao; temp>=2;temp--){
            multiplicarmatrizes(matrizLeslie,Lesliemultiplicada);
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

    public static void distribuicaoNormalizada (int geracao,double[][] Nt, double [] populacoesEstimadas, double[][] distribuicaoNormalizada){
        for (int i=0;i<distribuicaoNormalizada[0].length;i++){
            distribuicaoNormalizada[geracao][i]=Nt[geracao][i]*100/populacoesEstimadas[geracao];
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
        for (int i=0;i<vetor.length;i++){
            soma+=vetor[i];
        }
        if (soma!=1 || soma!=100){
            for (int i=0;i<vetor.length;i++){
                vetor[i]=(vetor[i]/soma)*100;
            }
        }
    }

    public static void escreverParaFicheiro (int[] opcoesExecucao, int geracao, double [] populacoesEstimadas, double [] taxasDeVariacao, double [][] Nt, double [][] distribuicaoNormalizada,double valorProprio, double[] vetorProprio, String [] args) throws FileNotFoundException {
        File file = new File(args[args.length-1]);
        PrintWriter out = new PrintWriter(file);
        boolean flag;
        int c;
        out.println("De acordo com os dados inseridos foram obtidos os seguintes resultados:");
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
                        out.printf("%.2f", Nt[j][c]);
                        out.print("\n");
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
            out.println("Este valor indica-nos a taxa de crescimento da população.");
            String comportamento = "igual", analise = "o que significa que a população vai-se manter constante ao longo dos anos.";
            if (valorProprio > constante) {
                comportamento = "maior";
                analise = " isto significa que a população irá aumentar ao longo dos anos.";
            }
            if (valorProprio < constante) {
                comportamento = "menor";
                analise = " isto significa que a população irá diminuir ao longo dos anos.";
            }
            out.println("Como o valor próprio é " + comportamento + " que 1," + analise);
            out.println("\nO vetor próprio associado ao maior valor próprio indica-nos as proporções populacionais constantes.");
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

    public static void escreverParaConsola (int geracao, double [] populacoesEstimadas, double [] taxasDeVariacao, double [][] Nt, double [][] distribuicaoNormalizada,double valorProprio, double[] vetorProprio,int[] opcoesVisualizaco, String nomepop) throws IOException {
        int c;
        boolean flag;
        if (opcoesVisualizaco[0]==1){
            for (int j = 0; j <= geracao; j++) {
                System.out.print("\nO número total de indivíduos da geração " + j + " é ");
                flag = NotCientifica(populacoesEstimadas[j]);
                if (flag) {
                    System.out.print(ConverterNotacaoCientifica(populacoesEstimadas[j]));
                } else {
                    flag = DoubleparaIntVer(populacoesEstimadas[j]);
                    if (flag) {
                        System.out.print(DoubleToInt(populacoesEstimadas[j]));
                    } else {
                        System.out.printf("%.2f", populacoesEstimadas[j]);
                    }
                }
            }
        }
        System.out.print("\n");
        if (opcoesVisualizaco[1]==1){
            for (int j = 0; j < geracao; j++) {
                System.out.print("\nA taxa de variação para a geração " + j + " é ");
                flag = NotCientifica(taxasDeVariacao[j]);
                if (flag) {
                    System.out.print(ConverterNotacaoCientifica(taxasDeVariacao[j]));
                } else {
                    flag = DoubleparaIntVer(taxasDeVariacao[j]);
                    if (flag) {
                        System.out.print(DoubleToInt(taxasDeVariacao[j]));
                    } else {
                        System.out.printf("%.2f", taxasDeVariacao[j]);
                    }
                }
            }
        }
        System.out.print("\n");
        if (opcoesVisualizaco[2]==1) {
            System.out.println("\nDistribuição da população:");
            for (int j = 0; j <= geracao; j++) {
                System.out.print("\nA população na geração " + j + " encontra-se distribuída da seguinte forma:\n");
                for (c = 0; c < Nt[0].length; c++) {
                    System.out.print("Idade " + c + ": ");
                    flag = NotCientifica(Nt[j][c]);
                    if (flag) {
                        System.out.print(ConverterNotacaoCientifica(Nt[j][c]) + "\n");
                    } else {
                        flag = DoubleparaIntVer(Nt[j][c]);
                        if (flag) {
                            System.out.print(DoubleToInt(Nt[j][c]) + "\n");
                        } else {
                            System.out.printf("%.2f", Nt[j][c]);
                            System.out.print("\n");
                        }
                    }
                }
            }
        }
        if (opcoesVisualizaco[3]==1) {
            System.out.print("\nDistribuição normalizada:\n");
            for (int j = 0; j <= geracao; j++) {
                System.out.print("\nA distribuição normalizada da geração " + j + " está representada pelas várias faixas etárias:\n");
                for (c = 0; c < Nt[0].length; c++) {
                    System.out.print("Idade " + c + ": ");
                    flag = NotCientifica(distribuicaoNormalizada[j][c]);
                    if (flag) {
                        System.out.print(ConverterNotacaoCientifica(distribuicaoNormalizada[j][c]) + "\n");
                    } else {
                        flag = DoubleparaIntVer(distribuicaoNormalizada[j][c]);
                        if (flag) {
                            System.out.print(DoubleToInt(distribuicaoNormalizada[j][c]) + "\n");
                        } else {
                            System.out.printf("%.2f", distribuicaoNormalizada[j][c]);
                            System.out.print("%\n");
                        }
                    }
                }
            }
        }
        if (opcoesVisualizaco[4]==1){
            System.out.println("\nComportamento Assintótico da população associado ao maior valor próprio.");
            System.out.printf("\nO valor próprio de módulo máximo é aproximadamente: %.4f\n", valorProprio);
            System.out.println("Este valor indica-nos a taxa de crescimento da população.");
            String comportamento="igual", analise="o que significa que a população vai-se manter constante ao longo dos anos.";
            if (valorProprio>constante){
                comportamento="maior";
                analise="isto significa que a população irá aumentar ao longo dos anos.";
            }
            if (valorProprio<constante){
                comportamento="menor";
                analise="isto significa que a população irá diminuir ao longo dos anos.";
            }
            System.out.println("Como o valor próprio é " + comportamento + " que 1, " + analise);
            System.out.println("\nO vetor próprio associado ao maior valor próprio indica-nos as percentagens populacionais constantes.");
            for (c = 0; c < Nt[0].length; c++) {
                System.out.print("Idade " + c + ": ");
                flag = NotCientifica(vetorProprio[c]);
                if (flag) {
                    System.out.print(ConverterNotacaoCientifica(vetorProprio[c]) + "\n");
                } else {
                    flag = DoubleparaIntVer(vetorProprio[c]);
                    if (flag) {
                        System.out.print(DoubleToInt(vetorProprio[c]) + "\n");
                    } else {
                        System.out.printf("%.2f", vetorProprio[c]);
                        System.out.print("\n");
                    }
                }
            }
        }
        if (opcoesVisualizaco[5]==1){
            System.out.println("Que gráfico quer representar?");
            System.out.println("<1>-Evolução da População Total;\n<2>-Evolução da taxa de variação;\n<3>-Distribuição da População;\n<4>-Distribuição normalizada da população.");
            int num = ler.nextInt();
            Graficos(geracao,populacoesEstimadas,taxasDeVariacao,Nt,distribuicaoNormalizada,num, nomepop);
        }
    }

    public static void PopulacaoTotal(int geracao,double[] populacoesEstimadas) throws FileNotFoundException {
        File file = new File("valores.txt");
        PrintWriter out = new PrintWriter(file);
        for (int l = 0; l <= geracao; l++) {
            out.print(l + " " + populacoesEstimadas[l]+"\n");
        }

        out.close();
    }

    public static void PopulacaoDistribuida(int n,double[][] Nt,int geracao) throws FileNotFoundException {
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

    public static void Graficopopulacao(String d) throws IOException {
        Runtime  rt = Runtime.getRuntime();
        rt.exec("gnuplot -p -e \""+d+"\"");
    }

    public static void SalvarGrafico(String s,String d,String terminal) throws IOException {
        Runtime  rt = Runtime.getRuntime();
        rt.exec("gnuplot -e \"set terminal "+terminal+"; set output '"+s+"'; "+d+"\"");
    }

    public static void PerguntaGrafico(String s,String d, String nomepop) throws IOException {
        int resposta;
        String tempo = determinarDataCriacao();
        nomepop = RetirarExtensao(nomepop);
        System.out.println("Deseja Salvar o Gráfico?(1- Sim; 2- Não)");
        resposta = ler.nextInt();
        if (resposta == 1) {
            System.out.println("Qual o formato do ficheiro?");
            System.out.println("1- Formato PNG;\n2- Formato TXT;\n3- Formato EPS.");
            resposta = ler.nextInt();
            switch (resposta) {
                case 1:
                    SalvarGrafico(s+ nomepop + "_" + tempo + ".png", d, "png");
                    break;
                case 2:
                    SalvarGrafico(s+ nomepop + "_" + tempo +".txt", d, "dumb");
                    break;
                case 3:
                    SalvarGrafico(s+ nomepop + "_" + tempo +".eps", d, "eps");
                    break;
            }
        }
    }

    public static String DistribuidaNormalizada(int n,String ylabel,String titulo) throws IOException {
        int o=3;
        String s = "set xlabel 'Gerações'; set ylabel '"+ylabel+"' ; set title '"+titulo+"' font 'arial,20'; set palette rgb 7,5,15; plot 'valores.txt' u 1:2 w lp t 'Idade 0' lw 3";
        for(int e=1;e<n+1;e++) {
            String d = " ,'valores.txt' u 1:" + o + "w lp t 'Idade "+e+"' lw 3";
            s = s+d;
            o++;
        }
        return s;
    }

    public static void Graficos(int geracao,double[] populacoesEstimadas,double[] taxasDeVariacao,double[][] Nt,double[][] distribuicaoNormalizada,int num, String nomepop) throws IOException {
        String s;
        switch(num){
            case 1:
                PopulacaoTotal(geracao,populacoesEstimadas);
                Graficopopulacao("plot 'valores.txt' title 'População' with lines lc 'blue' lw 3; set xlabel 'Gerações'; set ylabel 'População' ; set title 'População total' font 'arial,20'");
                PerguntaGrafico("PopulaçãoTotal_","set xlabel 'Gerações'; set ylabel 'População' ; set title 'População total' font 'arial,20'; plot 'valores.txt' title 'População Total' with lines lc 'blue' lw 3", nomepop);
                break;
            case 2:
                PopulacaoTotal((geracao-1), taxasDeVariacao);
                Graficopopulacao("plot 'valores.txt' title 'Taxa de Variação' with lines lc 'red' lw 3; set xlabel 'Gerações'; set ylabel 'Taxa de Variação' ; set title 'Taxa de Variação' font 'arial,20'");
                PerguntaGrafico("TaxadeVariação_","set xlabel 'Gerações'; set ylabel 'Taxa de Variação' ; set title 'Taxa de Variação' font 'arial,20'; plot 'valores.txt' title 'Taxa de Variação' with lines lc 'red' lw 3", nomepop);
                break;
            case 3:
                PopulacaoDistribuida(Nt[0].length,Nt,geracao);
                s=DistribuidaNormalizada(Nt[0].length,"População","População Distribuida");
                Graficopopulacao(s);
                PerguntaGrafico("PopulaçãoDistribuida_",s, nomepop);
                break;
            case 4:
                PopulacaoDistribuida(Nt[0].length,distribuicaoNormalizada,geracao);
                s=DistribuidaNormalizada(Nt[0].length,"Distribuição","Distribuição Normalizada");
                Graficopopulacao(s);
                PerguntaGrafico("PopulaçãoNormalizada_",s, nomepop);
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
    public static String determinarDataCriacao() throws IOException {
        String nomeFich = "./";
        Path file = Paths.get(nomeFich);
        BasicFileAttributes time = Files.readAttributes(file, BasicFileAttributes.class);
        FileTime filetime = time.lastModifiedTime();
        String data = formatDateTime(filetime);

        return data;
    }
    public static String formatDateTime (FileTime fileTime) {
        LocalDateTime tempo = fileTime.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

        return tempo.format(DATE_FORMATTER);
    }
    public static void EliminarFicheiroTextoGrafico(File file) {
        file.delete();
    }
    public static String RetirarExtensao(String palavra) {
        palavra = palavra.replace(".txt", "");
        return palavra;
    }
    public static void Graficonaointerativo(int[] opcoesExecucao,int geracao,double[] populacoesEstimadas,double[] taxasDeVariacao,double[][] Nt,double[][] distribuicaoNormalizada,String nomepop) throws IOException, InterruptedException {
        int l=opcoesExecucao[1];
        String s;
        PopulacaoTotal(geracao, populacoesEstimadas);
        PerguntaGraficoNaoInterativo("População Total","set xlabel 'Gerações'; set ylabel 'População' ; set title 'População total' font 'arial,20'; plot 'valores.txt' title 'População Total' with lines lc 'blue' lw 3",l,nomepop);
        TimeUnit.MILLISECONDS.sleep(250);
        PopulacaoTotal((geracao-1), taxasDeVariacao);
        PerguntaGraficoNaoInterativo("Taxa de Variação","set xlabel 'Gerações'; set ylabel 'Taxa de Variação' ; set title 'Taxa de Variação' font 'arial,20'; plot 'valores.txt' title 'Taxa de Variação' with lines lc 'red' lw 3",l,nomepop);
        TimeUnit.MILLISECONDS.sleep(250);
        PopulacaoDistribuida(Nt[0].length,Nt,geracao);
        s=DistribuidaNormalizada(Nt[0].length,"População","População Distribuida");
        PerguntaGraficoNaoInterativo("PopulaçãoDistribuida_",s,l,nomepop);
        TimeUnit.MILLISECONDS.sleep(250);
        PopulacaoDistribuida(Nt[0].length,distribuicaoNormalizada,geracao);
        s=DistribuidaNormalizada(Nt[0].length,"Distribuição","Distribuição Normalizada");
        PerguntaGraficoNaoInterativo("PopulaçãoNormalizada_",s,l,nomepop);
        TimeUnit.MILLISECONDS.sleep(250);
    }
    public static void PerguntaGraficoNaoInterativo(String s,String d,int l,String nomepop) throws IOException {
        String tempo = determinarDataCriacao();
        nomepop = RetirarExtensao(nomepop);
            switch (l) {
                case 1:
                    SalvarGrafico(s+ nomepop + "_" + tempo + ".png", d, "png");
                    break;
                case 2:
                    SalvarGrafico(s+ nomepop + "_" + tempo +".txt", d, "dumb");
                    break;
                case 3:
                    SalvarGrafico(s+ nomepop + "_" + tempo +".eps", d, "eps");
                    break;
            }
    }
}