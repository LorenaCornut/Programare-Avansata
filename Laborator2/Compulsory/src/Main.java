public class Main {
    public static void main(String[] args) {
        Proiect proiect_teoretic=new Proiect("Complilator", tipuri.TEORETIC);
        Proiect proiect_practic=new Proiect("Interschem", tipuri.PRACTIC);
        Proiect[] pref_s1={proiect_teoretic,proiect_practic};
        Proiect[] pref_s2={proiect_practic,proiect_teoretic};
        Student s1=new Student("Maria", 1, proiect_practic,pref_s1);
        Student s2=new Student("Mihai", 2, proiect_teoretic,pref_s2);
        System.out.println(s1);
        System.out.println(s2);
        System.out.println(proiect_practic);
        System.out.println(proiect_teoretic);
    }
}
