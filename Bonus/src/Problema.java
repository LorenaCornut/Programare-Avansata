public class Problema {
    private Proiect[] proiecte;
    private Student[] studenti;
    private Profesor[] profesori;

    /**
     * Constructor pentru a crea o instanta de tip problema care contine un vector de proiecte si un vector de studenti
     * @param studenti
     * @param proiecte
     */
    public Problema(Student[] studenti, Proiect[] proiecte) {
        this.proiecte=Adauga_proiect(proiecte);
        this.studenti=Adauga_studenti(studenti);
    }

    /**
     * Metoda care primeste un vector de proiecte si le elimina pe cele care se repeta
     * @param proiecte
     * @return
     */
    private Proiect[] Adauga_proiect(Proiect[] proiecte) {
        Proiect[] rez=new Proiect[proiecte.length];
        int ct=0; //cate proiecte diferite adaug
        for(int i=0;i<proiecte.length;i++) {
            boolean gasit=false; //daca am gasit sau nu proectul curent in cele adaigate deja
            for(int j=0;j<ct;j++) {
                if(proiecte[i].equals(rez[j])) {
                    gasit=true;
                    break;
                }
            }
            if(!gasit) {
                rez[ct++]=proiecte[i];
            }
        }
        return java.util.Arrays.copyOf(rez, ct);
    }

    /**
     * Metoda care primeste un vector de studenti si elimina duplicatele
     * @param studenti
     * @return
     */
    private Student[] Adauga_studenti(Student[] studenti) {
        Student[] rez1=new Student[studenti.length];
        int ct=0; //cate proiecte diferite adaug
        for(int i=0;i<studenti.length;i++) {
            boolean gasit=false; //daca am gasit sau nu proectul curent in cele adaigate deja
            for(int j=0;j<ct;j++) {
                if(studenti[i].equals(rez1[j])) {
                    gasit=true;
                    break;
                }
            }
            if(!gasit) {
                rez1[ct++]=studenti[i];
            }
        }
        Student[] rez=new Student[rez1.length];
        for(int i=0;i<ct;++i) {rez[i]=rez1[i];}
        return java.util.Arrays.copyOf(rez, ct);
    }

    /**
     * Metoda care primeste un vector de profesori si elimina duplicatele
     * @param profesori
     * @return
     */
    private Profesor[] Adauga_profesori(Profesor[] profesori) {
        Profesor[] rez=new Profesor[profesori.length];
        int ct=0; //cate proiecte diferite adaug
        for(int i=0;i<profesori.length;i++) {
            boolean gasit=false; //daca am gasit sau nu proectul curent in cele adaigate deja
            for(int j=0;j<ct;j++) {
                if(profesori[i].equals(rez[j])) {
                    gasit=true;
                    break;
                }
            }
            if(!gasit) {
                rez[ct++]=profesori[i];
            }
        }
        return java.util.Arrays.copyOf(rez, ct);
    }

    /**
     * Metoda care creaza un vector cu toate persoanele (studenti si profesori)
     * @return
     */
    public Persoana[] toate_persoanele() {
        Persoana[] persoanele = new Persoana[studenti.length + profesori.length];
        System.arraycopy(studenti, 0, persoanele, 0, studenti.length);
        System.arraycopy(profesori, 0, persoanele, studenti.length, profesori.length);
        return persoanele;
    }
    public Profesor[] getProfesori(){
        return profesori;
    }
    public Student[] getStudenti(){
        return studenti;
    }
    public Proiect[] getProiecte(){
        return proiecte;
    }
    public void setProiecte(Proiect[] proiecte) {
        this.proiecte = proiecte;
    }
    public void setStudenti(Student[] studenti) {
        this.studenti = studenti;
    }
    public void setProfesori(Profesor[] profesori){
        this.profesori = profesori;
    }
}
