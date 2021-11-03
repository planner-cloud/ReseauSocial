import java.sql.Date;
import java.util.ArrayList;

public class Page {
    public  int   id;
    public String nom;
    public String date;
    public String libelle;
    public String nomC;
    public String prenomC;
    ArrayList<Membre> pAime = new ArrayList<Membre>();

    public Page(int id, String nom, String date, String libelle, String nomC, String prenomC) {
        this.id = id;
        this.nom = nom;
        this.date = date;
        this.libelle = libelle;
        this.nomC = nomC;
        this.prenomC = prenomC;
    }

    public Page(int id, String nom, String date) {
        this.id = id;
        this.nom = nom;
        this.date = date;
    }
}
