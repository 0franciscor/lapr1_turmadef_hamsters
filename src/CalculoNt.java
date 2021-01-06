import java.util.Scanner;

public class CalculoNt {
    static Scanner ler = new Scanner(System.in);
    public static void main(String[] args) {
        double [][] matrizLeslie=new double[4][4];
        matrizLeslie[0][1]=3;
        matrizLeslie[0][2]=3.17;
        matrizLeslie[0][3]=0.39;
        matrizLeslie[1][0]=0.11;
        matrizLeslie[2][1]=0.29;
        matrizLeslie[3][2]=0.33;
        double [] população = new double [4];
        população[0]=1000;
        população[1]=300;
        população[2]=330;
        população[3]=100;
        double [] Nt = new double [4];
        int t,n=4;
        double populaçãoTotal;
        t=ler.nextInt();
        populaçãoTotal=dimensãoPopulação(matrizLeslie,população,Nt,t,n);
        imprimir(Nt,populaçãoTotal);
    }
    public static double dimensãoPopulação ( double [][] matrizLeslie, double [] população, double [] Nt, int t,int n){
        double [][] novaLeslie=new double[4][4];
        novaLeslie[0][1]=3;
        novaLeslie[0][2]=3.17;
        novaLeslie[0][3]=0.39;
        novaLeslie[1][0]=0.11;
        novaLeslie[2][1]=0.29;
        novaLeslie[3][2]=0.33;
        double [] aux = new double [4];
        int i,l,c,g;
        double total=0,soma;
        for (g=2;g<=t;g++){
            for (l=0;l<n;l++){
                for (i=0;i<n;i++){
                    soma=0;
                    for (c=0;c<n;c++){
                        soma=soma+novaLeslie[l][c]*matrizLeslie[c][i];
                    }
                    aux[i]=soma;
                }
                for (c=0;c<n;c++){
                    novaLeslie[l][c]=aux[c];
                }
            }
        }
        for (i=0;i<n;i++){
            double somas=0;
            for (l=0;l<n;l++){
                somas=somas+novaLeslie[i][l]*população[l];
            }
            Nt[i]=somas;
        }
        total=0;
        for (i=0;i<n;i++){
            total=total+Nt[i];
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
