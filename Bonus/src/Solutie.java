import java.util.Random;

public class Solutie {
    private Problema problema;
    private int[] x;
    private Student[] studenti; //studentii din problema
    int ok=1;
    private Proiect[] proiecte;

    /**
     * Constructor pentru a crea o instanta problema in care ma folosesc de alte doua functii pentru a crea vectorul de studenti si cel de proiecte
     * @param nrStudenti
     * @param nrProiecte
     */
    public Solutie(int nrStudenti, int nrProiecte) {
        this.problema = new Problema(genereazaStudenti(nrStudenti, nrProiecte), genereazaProiecte(nrProiecte));
        this.studenti = problema.getStudenti();
        this.proiecte = problema.getProiecte();
    }

    /**
     *Metoda care genereaza un vector random de proiecte pentru care se cunoaste nr de elemente
     * @param nrProiecte
     * @return
     */
    public static Proiect[] genereazaProiecte(int nrProiecte) { //primeste ca parametri nr de proiecte pe care vreau sa il generez
        Proiect[] proiecte = new Proiect[nrProiecte];
        Random rand = new Random();
        for (int i = 0; i < nrProiecte; i++) {
            int codtip = rand.nextInt(2);
            if (codtip == 0) {proiecte[i] = new Proiect("p" + i,tipuri.TEORETIC);}
            else {proiecte[i] = new Proiect("p" + i,tipuri.PRACTIC);}
        }
        return proiecte;
    }

    /**
     * Metoda care genereaza random un vector de studenti
     * @param nrStudenti
     * @param nrProiecte
     * @return
     */
    public static Student[] genereazaStudenti(int nrStudenti, int nrProiecte) {
        Student[] studenti = new Student[nrStudenti];
        Proiect[] proiecte = genereazaProiecte(nrProiecte);
        Random rand = new Random();

        for (int i = 0; i < nrStudenti; i++) {
            studenti[i] = new Student("s" + i,i, nrProiecte);
            int nrPreferinte = rand.nextInt(nrProiecte) + 1; //generez random cate elem vreau sa aiba lista de pref
            for (int j = 0; j < nrPreferinte; j++) {
                int proiectIndex = rand.nextInt(proiecte.length); //generez random indexul proiectului pe care vreau sa il ad
                studenti[i].adaugaPreferinta(proiecte[proiectIndex]);
            }
        }
        return studenti;
    }

    /**
     * Metoda care realizeaza atribuirea proiectelor cu greedy
     */
    public void atribuire_proiecte(){
        studenti=problema.getStudenti();
        for(Student student : studenti){ //parcurg studentii
            for(Proiect proiect : student.getPreferinte()) { //ii parcurg lista de preferinte
                boolean luat=false; //ca sa verific daca acest proiect este deja luat
                for(Student stud : studenti){
                    if(stud.getProiect()!=null && stud.getProiect().equals(proiect)){ //daca am gasit un student care sa aiba atribuit acest proiect
                        luat=true;
                        break;
                    }
                }
                if(!luat){ //daca am gasit in lista sa de preferinte un proiect care sa nu fie luat deja i-l atribuim
                    student.setProiect(proiect);
                    break;
                }
            }
        }
    }

    /**
     * Metoda pentru rezolvare
     */
    public void rezolvare(){
        long startTime = System.nanoTime(); ///iau moemntul de incepere
        Runtime runtime = Runtime.getRuntime(); ///obtinem instanta runtime a JVM
        long usedMemoryBefore = runtime.totalMemory() - runtime.freeMemory(); ///calculez memoria utilizata inainte de executie

        studenti = new Student[problema.getStudenti().length+1];
        Student[] studenti1=problema.getStudenti();
        for(int i=0;i<studenti1.length;i++){studenti[i+1]=studenti1[i];}
        for (Student s : studenti) {
            if(s!=null) {System.out.println(s);}
        }
        x = new int[problema.getStudenti().length+1];
        x[0]=0;
        submultimi(1);
        if(ok==0) {System.out.println("Nu se poate realiza asocierea");}
        else {System.out.println("Asociere posibila");}

        long endTime = System.nanoTime(); ///momentul de final
        long executionTime = endTime - startTime; ///calculez durata prin diferenta
        long usedMemoryAfter = runtime.totalMemory() - runtime.freeMemory(); ///masor memoria utilizata la final
        long memoryUsed = usedMemoryAfter - usedMemoryBefore; ///masor memoria care a fost consumata

        System.out.println("Execution time: " + executionTime);
        System.out.println("Memory used (bytes): " + memoryUsed);
    }
    public Student[] getStudent(){
        return studenti;
    }

    /**
     * Metoda care verifica daca pentru submultimea curenta sunt indeplinite conditiile teoremei lui Hall
     * @param k
     * @return
     */
    public boolean Valid(int k) {
        System.out.println(k);
        Proiect[] proiectevecine = new Proiect[problema.getProiecte().length+1];
        int ct = 0;
        // Parcurgem studenții alesi pana acum din vectorul x
        for (int i = 1; i <= k; i++) {
            int studentIndex = x[i];
            Student student = studenti[studentIndex];
            // Parcurgem preferințele studentului curent
            for (Proiect project : student.getPreferinte()) {
                if(project!=null) {
                    boolean exists = false;
                    // Verific daca a mai avut pana acum cineva acest vecin
                    for (int j = 0; j < ct; j++) {
                        if (proiectevecine[j].equals(project)) {
                            exists = true;
                            break;
                        }
                    }
                    if (!exists) {
                        proiectevecine[ct++] = project;
                    }
                }
            }
        }
        //ct+1 pentru ca acest vector este indexat de la 0
        return ct+1 >= k; // Daca numarul de proiecte >= numarul de studenți
    }

    /**
     * Metoda de tip backTracking care genereaza toate submultimile si le verifica
     * @param k
     */
    public void submultimi(int k){
        System.out.println(studenti.length);
        for(int i=x[k-1]+1; i<studenti.length && ok==1; i++){
            x[k]=i;
            if(Valid(k)==false)
            {
                ok=0;
            }
            else if(k<studenti.length-1)
            {
                submultimi(k+1);
            }
        }
    }
}