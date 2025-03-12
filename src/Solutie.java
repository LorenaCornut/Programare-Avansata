public class Solutie {
    private Problema problema;

    /**
     * Constructor pentru problema
     * @param problema
     */
    public Solutie(Problema problema) {
        this.problema = problema;
    }

    /**
     * Metoda care atribuie fiecarui student un proiect plecand de le lista de preferinte cu metoda greedy
     */
    public void atribuire_proiecte(){
        Student[] studenti=problema.getStudenti();
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
}
