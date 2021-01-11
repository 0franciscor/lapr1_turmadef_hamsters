import org.la4j.Matrix;
import org.la4j.decomposition.EigenDecompositor;
import org.la4j.matrix.dense.Basic2DMatrix;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

public class Projeto {

    static Scanner ler = new Scanner(System.in);

    public static void main(String[] args) throws FileNotFoundException,IOException {
        boolean existe, naoInterativo;
        String nomeFicheiro;
        int[] opcoesExecucao = new int [5];
        int numCiclos = 0;

        //RESPOSAVEL POR VERIFICAR SE O CODIGO ESTA A CORRER EM MODO NAO INTERATIVO
        naoInterativo = modoNInterativo(opcoesExecucao, args);
        existe = naoInterativo;

        if(existe) {
            numCiclos = opcoesExecucao[0];
            nomeFicheiro = args[(args.length - 2)];
        }
        //TERMINA AQUI E COMEÇA PARA O MODO INTERATIVO COM INTRODUÇAO DE FICHEIRO

        else {
            existe = modoInterativo(args);
            nomeFicheiro = args[1];
        }
        //TERMINA AQUI

        double[] populacaoInicial; //VETOR INICIAL
        double[][] matrizLeslie;   //DECLARAÇÃO MATRIZ LESLIE

        if (existe) {
            populacaoInicial = tratamentoDados((leituraDados(nomeFicheiro, 0)));
            matrizLeslie = new double[populacaoInicial.length][populacaoInicial.length];
            for (int i = 1; i <= 2; i++) {
                insercaoMatriz(matrizLeslie, tratamentoDados(leituraDados(nomeFicheiro, i)), i, true);
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

        int n = matrizLeslie.length, t =-1, geracao=-1;

        int [] geracoesEstimadas = new int [1000];
        double [] populacoesEstimadas = new double[1001];
        double [] taxasDeVariacao = new double[1000];
        double [][] Nt = new double[1001][matrizLeslie.length];
        double [][] distribuicaoNormalizada = new double[1000][matrizLeslie.length];

        if(naoInterativo) {
            while ((geracao+1) <= numCiclos){
                t++;
                geracao++;
                procedimentoCalculoGeracoes(Nt, geracao, geracoesEstimadas, matrizLeslie, populacaoInicial, t, populacoesEstimadas, taxasDeVariacao, distribuicaoNormalizada, n);
            }
        }
        else {
            System.out.println("Quais as geracoes que pretende que sejam estudadas?");
            int aux = ler.nextInt();
            while ((geracao+1) <= aux) {
                t++;
                geracao++;
                procedimentoCalculoGeracoes(Nt, geracao, geracoesEstimadas, matrizLeslie, populacaoInicial, t, populacoesEstimadas, taxasDeVariacao, distribuicaoNormalizada, n);
            }
        }

        double [] vetor = new double[matrizLeslie.length];
        double valorProprio;
        valorProprio=calcularVetorValorProprio(matrizLeslie,vetor);

        if (naoInterativo){
            escreverParaFicheiro(geracao, geracoesEstimadas, populacoesEstimadas, taxasDeVariacao, Nt, distribuicaoNormalizada, n,valorProprio,args);
        }
        escreverParaConsola(geracao, geracoesEstimadas, populacoesEstimadas, taxasDeVariacao, Nt, distribuicaoNormalizada, n,valorProprio,vetor);
        PopulacaoTotal(geracao,geracoesEstimadas,populacoesEstimadas,args);
        Graficopopulacao();
    }

    public static boolean modoInterativo(String[] args){
        boolean existe = false;
        if (args.length != 0 && args[0].equals("-n")) {
            String nomeFicheiro = args[1];
            existe = existeFicheiro(nomeFicheiro);
        }
        return existe;
    }

    public static boolean modoNInterativo(int[] opcoesExecucao, String[] args) {
        boolean naoInterativo = false;

        if (args.length != 0 && args[0].equals("-t")) {
            String nomeFicheiro = args[(args.length - 2)];
            boolean existe = existeFicheiro(nomeFicheiro);

            if (existe) {
                naoInterativo = true;
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
                System.out.println("O ficheiro inserido não existe. Assim, o programa será executado em modo interativo com inserção manual de dados.");
            }
        }
        return naoInterativo;
    }

    public static void procedimentoCalculoGeracoes(double[][] Nt, int geracao, int[] geracoesEstimadas, double[][] matrizLeslie, double[] populacaoInicial, int t, double[] populacoesEstimadas, double[] taxasDeVariacao, double[][] distribuicaoNormalizada, int n){
        geracoesEstimadas[geracao] = t;
        dimensaoPopulacao(matrizLeslie, populacaoInicial, t, Nt, populacoesEstimadas, geracao);
        dimensaoPopulacao(matrizLeslie, populacaoInicial, (t + 1), Nt, populacoesEstimadas, (geracao + 1));
        TaxaVariacao(populacoesEstimadas, geracao, taxasDeVariacao);
        distribuicaoNormalizada(geracao, Nt, populacoesEstimadas, distribuicaoNormalizada, n);

    }

    public static boolean existeFicheiro(String nomeFicheiro){
        File ficheiroVerificacao = new File(nomeFicheiro);
        return ficheiroVerificacao.exists();
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

    public static String leituraDados(String nomeFicheiro, int numLinha) throws FileNotFoundException { //LEITURA EXCLUSIVA DO VETOR
        File ficheiro = new File(nomeFicheiro);
        Scanner leituraFicheiro = new Scanner(ficheiro);
        String input = null;

        for(int i = 0; i<=numLinha; i++)
            input = leituraFicheiro.nextLine();
        leituraFicheiro.close();

        return input;
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
            int colunaCorrente = 0;
            for (int i = 1; i <= limite; i++) {
                matrizLeslie[i][colunaCorrente] = valoresTratados[i - 1];
                colunaCorrente++;
            }
        }
        else{
            for (int i = 0; i < valoresTratados.length; i++)
                matrizLeslie[0][i] = valoresTratados[i];
        }
    }

    public static void dimensaoPopulacao (double [][] matrizLeslie, double[] populacaoInicial, int t, double[][] Nt,double[] populacoesEstimadas, int geracao) { //CALCULO DIMENSAO POPULACAO
        double [][] Lesliemultiplicada = new double[matrizLeslie.length][matrizLeslie.length];

        for(int i =0 ; i<matrizLeslie.length; i++){
            for(int j =0 ; j< matrizLeslie.length; j++){
                Lesliemultiplicada[i][j]=matrizLeslie[i][j];
            }
        }

        for(int temp = t; temp>=2;temp--){
            multiplicarmatrizes(matrizLeslie,Lesliemultiplicada);
        }

        double total=0;
        if (t==0){
            for (int a=0;a<matrizLeslie.length;a++){
                Nt[geracao][a]=populacaoInicial[a];
            }
        } else {
            for (int i=0;i<matrizLeslie.length;i++){
                double somas=0;
                for (int l=0;l< matrizLeslie.length;l++){
                    somas=somas+Lesliemultiplicada[i][l]*populacaoInicial[l];
                }
                Nt[geracao][i]=somas;
            }
        }
        for (int i=0;i< matrizLeslie.length;i++){
            total+=Nt[geracao][i];
        }
        populacoesEstimadas[geracao]=total;
    }

    public static void TaxaVariacao(double [] populacoesEstimadas, int geracao, double[] taxasDeVariacao) {
        double variacao=populacoesEstimadas[geracao+1]/populacoesEstimadas[geracao];
        taxasDeVariacao[geracao]=variacao;
    }

    public static void multiplicarmatrizes (double [][] matriz1, double[][] matriz2){
        double [] aux = new double [matriz2.length];
        int c;

        for (int l=0;l<matriz1.length;l++){
            for (int i=0;i<matriz1.length;i++){
                double soma=0;
                for (c=0;c<matriz1.length;c++){
                    soma+=matriz2[l][c]*matriz1[c][i];
                }
                aux[i]=soma;
            }
            for (c=0;c<matriz2.length;c++){
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
    public static void escreverParaFicheiro (int geracao, int [] geracoesEstimadas, double [] populacoesEstimadas, double [] taxasDeVariacao, double [][] Nt, double [][] distribuicaoNormalizada, int n,double valorProprio, String [] args) throws FileNotFoundException {
        File file = new File(args[args.length-1]);
        PrintWriter out = new PrintWriter(file);
        int c;
        if (geracao == 0) {
            for (int i = 0; i <= geracao; i++) {
                out.print("Foi pedido ao programa para ser estudada a geração " + geracoesEstimadas[i] + ". \n");
            }
        }
        if (geracao >= 1) {
            out.print("Foi pedido ao programa para serem estudadas as gerações " + geracoesEstimadas[0] + ", ");
            for (int i = 1; i <= geracao; i++) {
                out.print(geracoesEstimadas[i]);
                if (i < geracao - 1) {
                    out.print(", ");
                }
                if (i == geracao - 1)
                    out.print(" e ");
            }
            out.print(". ");
        }

            out.println("De acordo com os dados inseridos foram concluídos os seguintes resultados:");
            for (int j = 0; j <= geracao; j++) {
                out.print("O número total de indivíduos da geração " + geracoesEstimadas[j] + " é ");
                out.printf("%.2f", populacoesEstimadas[j]);
                out.print(". \n");
            }
            out.print("\nDesta forma, através do número total de indivíduos, procedeu-se à taxa de variação da população ao longo dos anos.\n");
            for (int j = 0; j <= geracao; j++) {
                out.print("A taxa de variação para a geração " + geracoesEstimadas[j] + " é ");
                out.printf("%.2f", taxasDeVariacao[j]);
                out.print(". \n");
            }
            out.println("\nSabendo a população total e a taxa de variação em cada geração introduzida, segue-se a distribuição da população, isto é, quantos indivíduos existem nas diferentes faixas etárias.");
            for (int j = 0; j <= geracao; j++) {
                out.println("A população na geração " + geracoesEstimadas[j] + " encontra-se distribuída da seguinte forma:");
                for (c = 0; c < n - 1; c++) {
                    out.print("Idade " + c + ": ");
                    out.printf("%.2f", Nt[j][c]);
                    out.print("; \n");
                }
                out.print("Idade " + c + ": ");
                out.printf("%.2f", Nt[j][n - 1]);
                out.print(". \n");
                out.print("\n");
            }
            out.println("Apresentamos, de seguida, a população normalizada distribuída pelas várias gerações. Esta resulta da divisão da dimensão da população em cada faixa etária pela população total.");
            for (int j = 0; j <= geracao; j++) {
                out.print("A distribuição normalizada da geração " + geracoesEstimadas[j] + "está representada pelas várias faixas etárias: \n");
                for (c = 0; c < n - 1; c++) {
                    out.print("Idade " + c + ": ");
                    out.printf("%.2f", distribuicaoNormalizada[j][c]);
                    out.print("; \n");
                }
                out.print("Idade " + c + ": ");
                out.printf("%.2f", distribuicaoNormalizada[j][n - 1]);
                out.print(". \n");
                out.print("\n");
            }
            out.println("Comportamento Assintotico da populacao associado ao maior valor proprio.");
            out.printf("\nO valor próprio de módulo máximo é aproximadamente: %.3f\n", valorProprio);
            out.println("Este valor indica-nos a taxa de crescimento da população.");
            String comportamento="igual", analise="o que significa que a população se irá manter constante ao longo dos anos.";
            if (valorProprio>1){
                comportamento="maior";
                analise="isto significa que a população irá aumentar ao longo dos anos.";
            }
            if (valorProprio<1){
                comportamento="menor";
                analise="isto significa que a população irá diminuir ao longo dos anos.";
            }
            out.println("Como o valor próprio é " + comportamento + " que 1," + analise);
            out.print("Encontra-se concluída a apresentação dos resultados do programa da evolução das espécies.");

            out.close();

    }


    public static void escreverParaConsola (int geracao, int [] geracoesEstimadas, double [] populacoesEstimadas, double [] taxasDeVariacao, double [][] Nt, double [][] distribuicaoNormalizada, int n,double valorProprio, double[] vetorProprio) {
        int c;
        System.out.println("");
        System.out.print("Foi pedido ao programa para serem estudadas as geracoes: " + geracoesEstimadas[0] + " ");
        for (int i = 1; i <= geracao; i++) {
            System.out.print(geracoesEstimadas[i]+"");
        }
        System.out.println("");
        System.out.println("De acordo com os dados inseridos foram concluidos os seguintes resultados:");
        for (int j = 0; j <= geracao; j++) {
            System.out.print("O numero total de individuos da geracao " + geracoesEstimadas[j] + " é ");
            System.out.printf("%.2f", populacoesEstimadas[j]);
            System.out.print(". \n");
        }
        System.out.print("\nDesta forma, atraves do numero total de individuos, procedeu-se a taxa de variacao da populacao ao longo dos anos.\n");
        for (int j = 0; j <= geracao; j++) {
            System.out.print("A taxa de variacao para a geracao " + geracoesEstimadas[j] + " e ");
            System.out.printf("%.2f", taxasDeVariacao[j]);
            System.out.print(". \n");
        }
        System.out.println("");
        for (int j = 0; j <= geracao; j++) {
            System.out.println("A populacao na geracao " + geracoesEstimadas[j] + " encontra-se distribuida da seguinte forma:");
            for (c = 0; c < n - 1; c++) {
                System.out.print("Idade " + c + ": ");
                System.out.printf("%.2f", Nt[j][c]);
                System.out.print("; \n");
            }
            System.out.print("Idade " + c + ": ");
            System.out.printf("%.2f", Nt[j][n - 1]);
            System.out.print(". \n");
            System.out.print("\n");
        }
        System.out.println("Apresentamos, de seguida, a populacao normalizada distribuida pelas varias geracoes. Esta resulta da divisao da dimensao da populacao em cada faixa etaria pela populacao total.");
        for (int j = 0; j <= geracao; j++) {
            System.out.print("A distribuicao normalizada da geracao " + geracoesEstimadas[j] + "esta representada pelas varias faixas etarias: \n");
            for (c = 0; c < n - 1; c++) {
                System.out.print("Idade " + c + ": ");
                System.out.printf("%.2f", distribuicaoNormalizada[j][c]);
                System.out.print("; \n");
            }
            System.out.print("Idade " + c + ": ");
            System.out.printf("%.2f", distribuicaoNormalizada[j][n - 1]);
            System.out.print(". \n");
            System.out.print("\n");
        }
        System.out.println("Comportamento Assintotico da populacao associado ao maior valor proprio.");
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
        System.out.println("O vetor proprio associado ao maior valor proprio indica-nos as proporcoes populacionais constantes.");
        for (c = 0; c < n; c++) {
            System.out.print("Idade " + c + ": ");
            System.out.printf("%.3f", vetorProprio[c]);
            System.out.print("; \n");
        }

        System.out.println("");
        System.out.print("Encontra-se concluida a apresentacao dos resultados do programa da evolucao das especies.");
        System.out.println("");
    }
    public static void PopulacaoTotal(int geracao,int [] geracoesEstimadas,double[] populacoesEstimadas,String [] args) throws FileNotFoundException {
        File file = new File(args[args.length-1]);
        PrintWriter out = new PrintWriter("populacao.txt");
        for (int l = 0; l <= geracao; l++) {
            out.print(geracoesEstimadas[l] + " " + populacoesEstimadas[l]+"\n");
        }

        out.close();
    }

    public static void Graficopopulacao() throws IOException {
        Runtime  rt = Runtime.getRuntime();
        Process prcs = rt.exec("gnuplot -e \"set terminal png; set output 'Populaçãototal.png'; load 'populacao'\"");
    }
}