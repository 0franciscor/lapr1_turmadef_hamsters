import java.util.Scanner;

public class CalculoNt {
    static Scanner ler = new Scanner(System.in);
    public static void main(String[] args) {
        double [][] matrizLeslie=new double[4][4];
        matrizLeslie[0][1]=0;
        matrizLeslie[0][2]=0.8;
        matrizLeslie[0][3]=0.6;
        matrizLeslie[1][0]=0.95;
        matrizLeslie[2][1]=0.9;
        matrizLeslie[3][2]=0.75;
        double [] população = new double [4];
        população[0]=0;
        população[1]=100;
        população[2]=100;
        população[3]=100;
        double [] Nt = new double [4];
        int t;
        double populaçãoTotal;
        t=ler.nextInt();
        populaçãoTotal=dimensãoPopulação(matrizLeslie,população,Nt,t);
        imprimir(Nt,populaçãoTotal);
    }
    public static double dimensãoPopulação ( double [][] matrizLeslie, double [] população, double [] Nt, int t){
        int i,l,g;
        double total=0;
        for (g=1;g<=t;g++){
            for (i=0;i<4;i++){
                double soma=0;
                for (l=0;l<4;l++){
                    soma=soma+matrizLeslie[i][l]*população[l];
                }
                Nt[i]=soma;
            }
            total=0;
            for (i=0;i<4;i++){
                população[i]=Nt[i];
                total=total+população[i];
            }
        }
        return total;
    }
    public static void imprimir(double [] Nt, double total){
        int  l;
        System.out.println(total);
        for(l=0;l<4;l++){
            System.out.println(Nt[l]);
        }
    }
}
