import java.util.Random;

public class Lab1 {
    private int[][] a;
    private int n,k;
    public static void main(String[] args) {
        Lab1 lab1 = new Lab1();
        lab1.compulsory();
        lab1.homework(7,4);
    }
    void compulsory() {
        System.out.println("Hello World!");

        String languages[] = {"C", "C++", "C#", "Python", "Go", "Rust", "JavaScript", "PHP", "Swift", "Java"};

        int n = (int) (Math.random() * 1_000_000);
        System.out.println(n);

        n = n * 3;
        n = n + 0b10101;
        n = n + 0xFF;
        n = n * 6;
        System.out.println(n);

        int rez = 0;
        if (n < 10) rez = n;
        while (n >= 10) {
            rez = 0;
            while (n != 0) {
                rez = rez + n % 10;
                n = n / 10;
            }
            n = rez;
        }
        System.out.println(rez);

        System.out.println("Willy-nilly, this semester I will learn " + languages[rez]);
    }
    void homework(/*String args[]*/ int n, int k) {
        /*if(args.length<2) {
            System.out.println( "Nu ai destule argumente!");
            System.exit(-1);
        }
        this.n= Integer.parseInt(args[0]);
        this.k=Integer.parseInt(args[1]);*/
        if(k>n){
            System.out.println( "Nu putem avea o clica de mai mult de n noduri!");
            System.exit(-1);
        }
        this.a=new int[n+1][n+1];

        int[] clica=new int[n+1]; /*pentru a face clica*/
        int[] viz=new int[n+1];
        for(int I=1;I<=n;I++){ /*initializam vectorul de vizitat cu 0*/
            viz[I]=0;
        }
        Random rand = new Random();
        int i=1;
        while(i<=k)
        {
            int nr=rand.nextInt(n)+1;
            if(viz[nr]==0){
                viz[nr]=1;
                clica[i]=nr;
                i++;
            }
        }
        for(int I=1;I<=k;I++){
            System.out.println(clica[I]);
        }
    }
}