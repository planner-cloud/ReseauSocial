import java.util.ArrayList;

public class Membre implements  Comparable<Membre> {
    int id ;
    String nom ;
    String prenom ;
    String email ;
    String password ;
    int age ;
    int nbr;
    ArrayList <Membre> amis = new ArrayList<Membre>();
    ArrayList <Page> pages = new ArrayList<Page>();


    public Membre(int id, String nom, String prenom, String email, String password, int age) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.password = password;
        this.age = age;
    }

    @Override
    public int compareTo(Membre o) {
        return (this.nbr - o.nbr);
    }
}