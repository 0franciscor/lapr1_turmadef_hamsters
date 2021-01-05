public class CalculoNt {
    public static void main(String[] args) {
        double [][] matrizLeslie=new double[4][4];
        matrizLeslie[0][1]=3.00;
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
        dimensãoPopulação(matrizLeslie,população,Nt);
        imprimir(Nt);
    }
    public static void dimensãoPopulação ( double [][] matrizLeslie, double [] população, double [] Nt){
        int i,l;
        for (i=0;i<4;i++){
            double soma=0;
            for (l=0;l<4;l++){
                soma=soma+matrizLeslie[i][l]*população[l];
            }
            Nt[i]=soma;
        }
    }
    public static void imprimir(double [] Nt){
        int  l;
        for(l=0;l<4;l++){
            System.out.println(Nt[l]);
        }
    }
}
