import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Projeto {

    static Scanner ler = new Scanner(System.in);

    public static void main(String[] args) throws FileNotFoundException {
        boolean existe = false;
        String nomeFicheiro = null;
        if(args.length != 0) {
            nomeFicheiro = args[0];
            File ficheiroVerificacao = new File(nomeFicheiro);
            existe = ficheiroVerificacao.exists();
        }

        double populacaoInicial[] = null; //VETOR INICIAL
        double matrizLeslie[][] = null;   //DECLARAÇÃO MATRIZ LESLIE

        if(existe) {
            populacaoInicial = tratamentoDados((leituraDados(nomeFicheiro, 0)));
            matrizLeslie = new double [populacaoInicial.length][populacaoInicial.length];
            for(int i = 1; i<=2; i++){
                insercaoMatriz(matrizLeslie, leituraDados(nomeFicheiro, i), i);
            }
        } else {
            System.out.println("Quantos intervalos de idade possui a populacao que pretende estudar?");
            int numIntervalos = ler.nextInt();

            populacaoInicial = new double [numIntervalos];
            insercaoDados(populacaoInicial, "Populacao");

            matrizLeslie = new double [populacaoInicial.length][populacaoInicial.length];
            double aux [] = new double [matrizLeslie.length];

            for(int i = 1; i<=2; i++){
                if(i==1)
                    insercaoManualMatriz(matrizLeslie, insercaoDados(aux, "Taxa de sobrevivencia"), i);
                if(i==2)
                    insercaoManualMatriz(matrizLeslie, insercaoDados(aux, "Taxa de fecundidade"), i);
            }
        }

        int n=matrizLeslie.length;

        System.out.println("Quais as geracoes que pretende que sejam estudadas? (Para terminar a introducao das geracoes a analisar digite -1)");

        int t=ler.nextInt(), geracao=-1;
        int [] geracoesEstimadas = new int [1000];
        double [] populacoesEstimadas = new double[1000];
        double [] taxasDeVariacao = new double[1000];
        double [][] Nt = new double[1000][matrizLeslie.length];
        double [][] distribuicaoNormalizada = new double[1000][matrizLeslie.length];

        while (t!=-1){
            geracao+=1;
            geracoesEstimadas [geracao]= t;
            dimensaoPopulacao(matrizLeslie,populacaoInicial,t,Nt,populacoesEstimadas,geracao);
            dimensaoPopulacao(matrizLeslie,populacaoInicial,(t+1),Nt,populacoesEstimadas,(geracao+1));
            TaxaVariacao(populacoesEstimadas,geracao,taxasDeVariacao);
            distribuicaoNormalizada(geracao,Nt,populacoesEstimadas,distribuicaoNormalizada,n);
            t=ler.nextInt();
        }

        imprimirAnaliseGeracoes(geracao,geracoesEstimadas,populacoesEstimadas,taxasDeVariacao,distribuicaoNormalizada,Nt,n);
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
        String input = "";

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

    public static double[][] insercaoMatriz (double[][] matrizLeslie, String input, int tipologia) throws FileNotFoundException { //LEITURA E INSERÇÃO DE DADOS NA MATRIZ DE LESLIE 0-SOBREVIVENCIA 1- FECUNDIDADE
        double[] valoresTratados = tratamentoDados(input);
        return insercaoManualMatriz(matrizLeslie, valoresTratados, tipologia);
    }

    public static double[][] insercaoManualMatriz (double[][] matrizLeslie, double[] valoresTratados, int tipologia) throws FileNotFoundException {
        if (tipologia == 1) {
            int colunaCorrente = 0;
            for (int i = 1; i <= (valoresTratados.length-1); i++) {
                matrizLeslie[i][colunaCorrente] = valoresTratados[i - 1];
                colunaCorrente++;
            }
        }
        else{
            for (int i = 0; i < valoresTratados.length; i++)
                matrizLeslie[0][i] = valoresTratados[i];
        }
        return matrizLeslie;
    }

    public static void dimensaoPopulacao (double [][] matrizLeslie, double[] populacaoInicial, int t, double[][] Nt,double[] populacoesEstimadas, int geracao) throws FileNotFoundException{ //CALCULO DIMENSAO POPULACAO
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

    public static void imprimirAnaliseGeracoes (int geracao,int [] geracoesEstimadas,double[] populacoesEstimadas,double[] taxasDeVariacao,double [][] distribuicaoNormalizada, double[][] Nt, int n){
        for (int l=0; l<=geracao;l++){
            System.out.print("\nA populacao total na geracao " + geracoesEstimadas[l] +" e ");
            System.out.printf("%.15f\n", populacoesEstimadas[l]);
            System.out.print("Sendo a taxa de variacao nesta geracao de ");
            System.out.printf("%.4f\n", taxasDeVariacao[l]);
            System.out.print("A distribuicao da populacao e a seguinte: ");
            for (int j=0; j<n;j++){
                System.out.printf("%.2f ", Nt[l][j]);
            }
            System.out.print("\nA distribuicao normalizada da populacao e a seguinte: ");
            for (int j=0; j<n;j++){
                System.out.printf("%.3f ", distribuicaoNormalizada[l][j]);
            }
            System.out.println("\n");
        }
    }
}