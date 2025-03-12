public class Main {
    public static void main(String[] args) {
        //Cream profesorii
        Profesor prof1 = new Profesor("Ion", "1970-05-15", new Proiect[2]);
        Profesor prof2 = new Profesor("Vasile", "1980-09-22", new Proiect[2]);
        //Cream proiectele
        Proiect p1 = new Proiect("Complilator", tipuri.TEORETIC, prof1);
        Proiect p2 = new Proiect("Web Development", tipuri.PRACTIC, prof1);
        Proiect p3 = new Proiect("Cybersecurity", tipuri.TEORETIC, prof2);
        Proiect p4 = new Proiect("Interschem", tipuri.PRACTIC, prof2);
        //atribuim la profesori proiectele propuse de el
        prof1.getPropuneri()[0] = p1;
        prof1.getPropuneri()[1] = p2;
        prof2.getPropuneri()[0] = p3;
        prof2.getPropuneri()[1] = p4;
        //Cream liste de preferinte
        Proiect[] pref_s1={p1,p2};
        Proiect[] pref_s2={p2,p3};
        Proiect[] pref_s3={p3,p4};
        // Cream studenții și preferințele lor
        Student s1 = new Student("Alice", "2002-04-10", 123, 3, pref_s1);
        Student s2 = new Student("Bob", "2001-07-19", 456, 2, pref_s2);
        Student s3 = new Student("Mihai", "2002-09-25", 789, 1, pref_s3);
        Student s4 = new Student("Alice", "2002-04-10", 123, 3, pref_s1); // Student duplicat
        // Creăm obiectul Problema
        Student[] ss={s1,s2,s3,s4};
        Profesor[] prof={prof1,prof2};
        Proiect[] p={p1,p2,p3,p4};
        Problema problema = new Problema(p,ss,prof);
        Persoana[] a=problema.toate_persoanele();
        for (Persoana i : a) {  //afisam toate persoanele
            System.out.println(i);
        }
        System.out.println(" ");
        Solutie solution = new Solutie(problema);
        // Atribuim proiectele folosind metoda greedy
        solution.atribuire_proiecte();
        // Afișăm rezultatul
        for (Student s : problema.getStudenti()) {
            System.out.println(s);
        }
    }
}