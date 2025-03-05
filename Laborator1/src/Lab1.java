import java.util.Random;

public class Lab1 {
    private int[][] a;
    private int n,k;
    private int[] x; //pentru a calcula combinarile la bonus
    public static void main(String[] args) {
        Lab1 lab1 = new Lab1();
        lab1.compulsory();
        lab1.homework(args);
        lab1.x = new int[lab1.k + 1];
        lab1.x[0]=0;
        boolean[] ok=new boolean[1];
        ok[0]=false;
        lab1.bonus(1,ok,lab1.a);
        if(ok[0]==true) {System.out.println("Avem clica!");}
        else {System.out.println("Nu avem clica!");}
        int[][] comp=new int[lab1.n+1][lab1.n+1];
        lab1.fac_complement(comp, lab1.n);
        ok[0]=false;
        lab1.bonus(1,ok,comp);
        if(ok[0]==true) {System.out.println("Avem multime stabila!");}
        else {System.out.println("Nu avem multime stabila!");}
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
    void homework(String args[] ) {

        long startTime = System.nanoTime(); //luam momentul de timp care care incep sa fac operatiile

        if(args.length<2) {
            System.out.println( "Nu ai destule argumente!");
            System.exit(-1);
        }
        this.n= Integer.parseInt(args[0]);
        this.k=Integer.parseInt(args[1]);
        if(2*k>n){
            System.out.println( "Nu putem avea o clica de mai mult de n noduri!");
            System.exit(-1);
        }
        this.a = new int[n + 1][n + 1];
        //initializam matricea cu 0
        for (int i = 1; i <= n; i++)
            for (int j = 1; j <= n; j++) a[i][j] = 0;
        /*fac clica*/
        int[] clica = new int[n + 1]; /*pentru a face clica*/
        int[] viz_c = new int[n + 1]; /*vizitat pentru clica*/
        for (int I = 1; I <= n; I++) { /*initializam vectorul de vizitat cu 0*/
            viz_c[I] = 0;
        }
        Random rand = new Random();
        int i = 1;
        while (i <= k) {
            int nr = rand.nextInt(n) + 1; /*aleg un nr random intre 1 si n*/
            if (viz_c[nr] == 0) { /* ma asigur ca nu iau ac nr de mai multe ori*/
                viz_c[nr] = 1;
                clica[i] = nr;
                i++;
            }
        }
        System.out.println("Clica:");
        for (int I = 1; I <= k; I++) { /* afisare partiala elemnte clica*/
            System.out.println(clica[I]);
        }
        /*fac multimea stabila*/
        int[] stabil = new int[n + 1];
        int[] viz_s = new int[n + 1];
        for (int I = 1; I <= k; I++) { /*initializam vectorul de vizitat cu 0*/
            viz_s[I] = 0;
        }
        i = 1;
        while (i <= k) {
            int nr = rand.nextInt(n) + 1; /*aleg un nr random intre 1 si n*/
            if (viz_c[nr] == 0 && viz_s[nr] == 0) { /* ma asigur ca nu iau ac nr de mai multe ori si ca nu e in clica*/
                viz_s[nr] = 1;
                stabil[i] = nr;
                i++;
            }
        }
        System.out.println("Multimea stabila:");
        for (int I = 1; I <= k; I++) { /* afisare partiala elemnte multime stabila*/
            System.out.println(stabil[I]);
        }
        //completez clica
        for (int I = 1; I <= k; I++)
            for (int J = 1; J <= k; ++J)
                a[clica[I]][clica[J]] = 1;
        //completez multimea stabila cu -1
        for (int I = 1; I <= k; ++I)
            for (int J = 1; J <= k; ++J)
                a[stabil[I]][stabil[J]] = -1;
        //restul matricii
        for (int I = 1; I <= n; ++I)
            for (int J = I + 1; J <= n; ++J)
                if (a[I][J] == 0) a[I][J] = a[J][I] = rand.nextInt(2);
        //penttru a schimba -1 cu 0 + diag principala cu 0
        for (int I = 1; I <= n; ++I)
            for (int J = 1; J <= n; ++J)
                if (a[I][J] == -1) a[I][J] = 0;
                else if (I == J) a[I][J] = 0; //pun 1 pe diag principala
        //afisare matrice in forma draguta
        if (n < 30000) {
            for (int I = 1; I <= n; ++I) {
                for (int J = 1; J <= n; ++J) //System.out.print(a[I][J]);
                {
                    if (a[I][J] == 1) {
                        System.out.print("\u25CF ");
                    } else {
                        System.out.print("\u25CB ");
                    }
                }
                System.out.println();
            }
        }
        //nr de muchii
        int nr_muchii = 0;
        for (int I = 1; I <= n; ++I)
            for (int J = 1; J <= n; ++J)
                if (a[I][J] == 1) nr_muchii++;
        nr_muchii = nr_muchii / 2;
        System.out.println(nr_muchii);

        //gradul maxim si gradul minim cu litere grecesti
        int maxim = 0, minim = 100000;
        for (int I = 1; I <= n; ++I) {
            int sum = 0;
            for (int J = 1; J <= n; ++J)
                sum = sum + a[I][J];
            if (sum > maxim) maxim = sum;
            if (sum < minim) minim = sum;
        }
        System.out.print("\u03B4" + "(G)=");
        System.out.println(minim);
        System.out.print("\u0394" + "(G)=");
        System.out.println(maxim);

        //suma gradelor = 2m
        int suma_grade = 0;
        for (int I = 1; I <= n; ++I) {
            int sum = 0;
            for (int J = 1; J <= n; ++J)
                sum = sum + a[I][J];
            suma_grade = suma_grade + sum;
        }
        if (suma_grade == 2 * nr_muchii) {
            System.out.println("Suma gradelor este egala cu 2*m");
        } else {
            System.out.println("Suma gradelor nu este egala cu 2*m");
        }
        long endTime = System.nanoTime(); //iau momentul de timp cand termin operatiile
        if(n>=30000) { //daca n este mai mare de 30000 calc durata
            // Afișează timpul de execuție
            long duration = endTime - startTime; //durata va fi timp stop - timp start
            System.out.println("Timpul de execuție: " + duration + " nanosecunde");
        }
    }
    boolean eclica(int[] x, int[][] a)
    {
        for(int i=1;i<=k;++i)
            for(int j=1;j<=k;++j)
                if(i!=j && a[x[i]][x[j]]==0) return false;
        return true;
    }
    void bonus(int K, boolean[] ok, int[][] a)
    {
        for(int i=x[K-1]+1;i<=n-k+K && ok[0]==false;i=i+1)
        {
            x[K]=i;
            if(K==k)
            {
                if(eclica(x, a)==true) {ok[0]=true;}
            }
            else { bonus(K+1,ok,a);}
        }
    }
    void fac_complement(int[][] complement,int n)
    {
        for(int i=1;i<=n;++i)
            for(int j=1;j<=n;++j)
            {
                if(a[i][j]==1) complement[i][j]=0;
                if(a[i][j]==0) complement[i][j]=1;
                if(i==j) complement[i][j]=0;
            }
    }
}
