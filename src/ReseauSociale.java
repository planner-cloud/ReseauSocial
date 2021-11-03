import  BD.JavaMySQL;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList; 
import java.util.Collections;
import java.util.Scanner;

import static java.lang.System.exit;
import static java.lang.System.in;


public class ReseauSociale {

    public static void menu(int id){
        System.out.println("\n -----------  MENU  -----------  \n");
        System.out.println(" 1 : crée une page ");
        System.out.println(" 2 : consulter vos pages ");
        System.out.println(" 3 : crée un groupe ");
        System.out.println(" 4 : consulter vos groupes ");
        System.out.println(" 5 : liker une page ");
        System.out.println(" 6 : rejoindre un groupe ");
        System.out.println(" 7 : créer une publication ");
        System.out.println(" 8 : ajouter un(e) ami(e) ");
        System.out.println(" 9 : listes des invitations ");
        System.out.println(" 10: suggestion d'amis ");
        System.out.println(" 11: se déconnecter ");
        System.out.println(" 0 : quitter ");

        Scanner sc = new Scanner(System.in);
        System.out.print("\nchoix = ");
        int x = sc.nextInt();
        while (x<0 || x>11){
            System.out.print("Le choix saisie est érroné, réeseyer : ");
            x = sc.nextInt();
        }
        switch (x) {
            case 1 : creerPage(id) ; break ;
            case 2 : consulterPages(id) ; break ;
            case 3 : creerGroupe(id) ; break ;
            case 4 : consulterGroupes(id) ; break ;
            case 5 : likerPage(id) ; break ;
            case 6 : rejoindreGroupe(id) ; break ;
            case 7 : creerPub(id) ; break ;
            case 8 : ajouterAmi(id) ;break ;
            case 9 : consulterlesdemandes(id) ; break ;
            case 10 : suggestionAmis(id); ; break ;
            case 11 : seDeconnecter() ; break ;
            case 0 : quitter() ; break ;
        }
    }

    public static void authentifier()           {
        try {
            JavaMySQL bd = new JavaMySQL();
            Connection cx = bd.openConnection();
            System.out.println("\nAUTHENTIFICATION");
            System.out.print("\nSaisir votre @ Email : ");
            Scanner sc = new Scanner(System.in);
            String email = sc.nextLine();
            System.out.print("Saisir votre Password : ");
            String password = sc.nextLine();
            ResultSet res = bd.selectQuery("select * from membre where email like '" + email + "' and password like '" + password + "';");
            if(res.next()){
                menu(Integer.parseInt(res.getString(1)));
            }else {
                System.out.println(" \nLes Données saisis sont  érronées  ");

                System.out.println("\n 1 : réessayer ");
                System.out.println(" 2 : creer un compte ");
                System.out.print("\nchoix = ");
                int x = sc.nextInt();
                while (x<1 || x>2){
                    System.out.print("Le choix saisie est érroné, réeseyer : ");
                    x = sc.nextInt();
                }
                if(x==1){
                    authentifier();
                }else if (x==2) {
                    creerCompte();
                }
            }
            bd.close();
        }
        catch (SQLException e){
            System.out.println(e.toString());
        }
    }

    public static void creerCompte()            {
        JavaMySQL bd = new JavaMySQL();
        Connection cx = bd.openConnection();
        Scanner sc = new Scanner(System.in);
        try {
            System.out.println("\nINSCRIPTION");
            System.out.print("\n Saisir votre Nom : ");
            String nom = sc.nextLine();
            System.out.print("\n Saisir votre Prenom : ");
            String prenom = sc.nextLine();
            System.out.print("\n Saisir votre @ Email : ");
            String email = sc.nextLine();
            System.out.print("\n Saisir votre Password : ");
            String password = sc.nextLine();
            System.out.print("\n Saisir votre Age : ");
            int age = sc.nextInt();

            int x = bd.updateQuery("insert into membre (nom,prenom,email,password,age) values ('" + nom + "','" + prenom + "','" + email + "','" + password + "'," + Integer.toString(age) + ");");
            if (x==1){
                System.out.println("\n Votre compte à été crée avec succés ! \n Vous êtes le BIENVENU :D ");
                authentifier();
            }
            else
            {
                System.out.print("\n insertion non valide !");
            }

        } catch (SQLException e) {
            System.out.println(e.toString());
        }
    }

    public static void seDeconnecter()          {

        authentifier();

    }

    public static void quitter()                {

        exit(0);

    }

    public static void creerPage(int id)        {
        JavaMySQL bd = new JavaMySQL();
        Connection cx = bd.openConnection();
        Scanner sc = new Scanner(System.in);
        ArrayList<Genre> genres = new ArrayList<Genre>();
        try {
            System.out.println("\nCREER UNE PAGE ");
            System.out.print("Saisir le Nom de la page : ");
            String nom = sc.nextLine();
            ResultSet res = bd.selectQuery("select * from genre;");
            while (res.next()){
                Genre g = new Genre(res.getInt(1), res.getString(2));
                genres.add(g);
            }
            for (int i=0 ; i<genres.size();i++)
                System.out.println( i+1 +" - "+ genres.get(i).libelle);
            System.out.print("Choisir le genre de votre page : ");
            int x =sc.nextInt();
            while (x<1 || x> genres.size()){
                System.out.print("choix érronée ! ressayer :");
                x = sc.nextInt();
            }

            LocalDateTime dt = LocalDateTime.now();
            int y = dt.getYear();
            int m = dt.getMonthValue();
            int d = dt.getDayOfMonth();
            int hr = dt.getHour();
            int mn = dt.getMinute();
            int sd = dt.getSecond();

            int u = bd.updateQuery("insert into page (nom,date,id_genre,id_createur) values ('" + nom + "','"  +Integer.toString(y)+":"+Integer.toString(m)+":"+Integer.toString(d)+" "+Integer.toString(hr)+":"+Integer.toString(mn)+":"+Integer.toString(sd)+  "'," + Integer.toString(genres.get(x-1).id) + ","+Integer.toString(id)+");");
            if (u==1){
                ResultSet resultSet = bd.selectQuery("select * from page where nom like '"+nom+"';");
                if (resultSet.next()){
                        u =  bd.updateQuery("insert into gerer_page (id_membre,id_page) values ("+Integer.toString(id)+","+Integer.toString(resultSet.getInt(1))+");");
                    System.out.println("\n Votre page à été crée avec succés !\n ");
                    menu(id);
                }else {
                    System.out.println("Erreur !");
                }
            }else {
                System.out.println("Erreur !");
            }
        }
        catch (SQLException e){

        }
    }

    public static void consulterGroupes(int id)   {
        JavaMySQL bd = new JavaMySQL();
        Connection cx = bd.openConnection();
        Scanner sc = new Scanner(System.in);
        ArrayList<Groupe> groupes   = new ArrayList<Groupe>();
        ArrayList<Groupe> groupes2   = new ArrayList<Groupe>();
        try {
            System.out.println("\nCONSULTER MES GROUPES ");
            ResultSet res = bd.selectQuery("select * from groupe g  ,membre m where id_createur = " + Integer.toString(id) + "  and g.id_createur=m.id;");
            while (res.next()) {
                Groupe g = new Groupe(res.getInt(1), res.getString(2), res.getString(3));
                groupes.add(g);
            }
            if (groupes.size() == 0) {
                System.out.println("Vous n'avez créer aucun groupe :( ");
            } else {
                System.out.println("\nLes Groupes Que J'ai Crée  ");
                for (int i = 0; i < groupes.size(); i++)
                    System.out.println(" " + i + 1 + " - " + groupes.get(i).nom + " - " + groupes.get(i).date);
            }
            ResultSet result = bd.selectQuery("SELECT * FROM rejoindre r, membre m , groupe g where r.id_membre=m.id and r.id_groupe=g.id and m.id=" + Integer.toString(id));
            while (result.next()) {
                Groupe g = new Groupe(result.getInt(10), result.getString(11), result.getString(12));
                groupes2.add(g);
            }
            if (groupes2.size() == 0) {
                System.out.println("Vous n'avez rejoidre aucun groupe :( ");
            } else {
                System.out.println("\nLes Groupes Que J'ai suivi  ");
                for (int i = 0; i < groupes2.size(); i++)
                    System.out.println(" " + i + 1 + " - " + groupes2.get(i).nom + " - " + groupes2.get(i).date);
            }

            System.out.println("\n");
            System.out.println(" 1 : ajouter un admin à un de vos groupes  ");
            System.out.println(" 2 : supprimer un admin à un de vos groupes ");
            System.out.println(" 3 : quitter un de vos groupes ");
            System.out.println(" autre chose pour quiter ");
            sc = new Scanner(System.in);
            System.out.print("Choix = ");
            int v = sc.nextInt();
            if (v == 1) {
                System.out.print("Choisir le numéro de groupe : ");
                int p = sc.nextInt();
                while (p < 1 || p > groupes.size()) {
                    System.out.print("Choisir le numéro de groupe : ");
                    p = sc.nextInt();
                }
                ResultSet resu = bd.selectQuery("select * from membre  where id <>" + Integer.toString(id) + " ;");
                ArrayList<Membre> membres = new ArrayList<Membre>();
                while (resu.next()) {
                    Membre m = new Membre(resu.getInt(1), resu.getString(2), resu.getString(3), resu.getString(4), resu.getString(5), resu.getInt(6));
                    membres.add(m);
                }
                for (int i = 0; i < membres.size(); i++)
                    System.out.println(i + 1 + " - " + membres.get(i).nom + "  " + membres.get(i).prenom);
                System.out.print("Choisir le nouveau admin : ");
                int x = sc.nextInt();
                while (x < 1 || x > membres.size()) {
                    System.out.print("choix érronée ! ressayer :");
                    x = sc.nextInt();
                }
                if (bd.updateQuery("insert into gerer_groupe (id_membre,id_groupe) values (" + Integer.toString(membres.get(x - 1).id) + " , " + Integer.toString(groupes.get(p - 1).id) + ");") == 1) {
                    System.out.println("\nAdmin ajouter avec Succée :D \n");
                    menu(id);
                }
            } else if (v == 2) {

            } else if (v == 3) {
                System.out.print("Choisir le numéro de groupe : ");
                int p = sc.nextInt();
                while (p < 1 || p > groupes.size()) {
                    System.out.print("Choisir le numéro de groupe : ");
                    p = sc.nextInt();
                }
                if (bd.updateQuery("delete from gerer_groupe where id_groupe = " + Integer.toString(groupes.get(p - 1).id) + " and id_membre="+ Integer.toString(id) + " ;") == 1) {
                    menu(id);
                }
            } else {

                menu(id);
            }
            }
            catch (SQLException e){
                System.out.println(e.toString());
            }
    }

    public static void consulterPages(int id)   {
        JavaMySQL bd = new JavaMySQL();
        Connection cx = bd.openConnection();
        Scanner sc = new Scanner(System.in);
        ArrayList<Page> pages = new ArrayList<Page>();
        ArrayList<Page> pages2 = new ArrayList<Page>();
        try {
            System.out.println("\nCONSULTER MES PAGES ");
            ResultSet res = bd.selectQuery("select * from page p , genre g ,membre m where id_createur = "+Integer.toString(id)+" and p.id_genre=g.id and p.id_createur=m.id;");
            while (res.next()){
                Page p = new Page (res.getInt(1), res.getString(2),res.getString(3),res.getString(7),res.getString(9),res.getString(10));
                pages.add(p);
            }
            if (pages.size()==0){
                System.out.println("Vous n'avez créer aucune page :( ");
            }else {
                System.out.println("\nLes Pages Que J'ai Crée  ");
                for (int i=0 ; i<pages.size();i++)
                    System.out.println(" "+ i+1 +" - "+ pages.get(i).nom +" - "+ pages.get(i).date +" - "+ pages.get(i).libelle +" - "+ pages.get(i).nomC +" - "+ pages.get(i).prenomC );
            }
            ResultSet result = bd.selectQuery("select * from page p , aime a, membre m where p.id_createur=m.id and a.id = "+Integer.toString(id)+" and p.id=a.id ;");
            while (result.next()){
                Page p = new Page (result.getInt(1), result.getString(2),result.getString(3),"",result.getString(10),result.getString(11));
                pages2.add(p);
            }
            if (pages2.size()==0){
                System.out.println("\nVous n'avez aimée aucune page :( ");
            }else {
                System.out.println("\nLes Pages Que J'ai Aimé  ");
                for (int i=0 ; i<pages2.size();i++)
                    System.out.println(" "+ i+1 +" - "+ pages2.get(i).nom +" - "+ pages2.get(i).date  +" - "+ pages2.get(i).nomC +" - "+ pages2.get(i).prenomC );
            }
            System.out.println("\n");
            System.out.println(" 1 : ajouter un admin à une de vos page  ");
            System.out.println(" 2 : supprimer un admin à une de vos page ");
            System.out.println(" autre chose pour quiter ");
            sc=new Scanner(System.in);
            System.out.print("Choix = ");
            int v = sc.nextInt();
            if (v == 1){
                System.out.print("Choisir le numéro de page : ");
                int p = sc.nextInt();
                while (p<1 || p>pages.size()){
                    System.out.print("Choisir le numéro de page : ");
                    p = sc.nextInt();
                }
                //requette a modifier ( doit etre ami et membre de la page et n'est pas déja un admin )
                ResultSet resu = bd.selectQuery("select * from membre  where id <>"+Integer.toString(id)+" ;")  ;
                ArrayList<Membre> membres = new ArrayList<Membre>();
                while (resu.next()){
                    Membre m = new Membre(resu.getInt(1), resu.getString(2), resu.getString(3), resu.getString(4), resu.getString(5), resu.getInt(6));
                    membres.add(m);
                }
                for (int i=0 ; i<membres.size();i++)
                    System.out.println( i+1 +" - "+ membres.get(i).nom+"  "+ membres.get(i).prenom);
                System.out.print("Choisir le nouveau admin : ");
                int x =sc.nextInt();
                while (x<1 || x> membres.size()){
                    System.out.print("choix érronée ! ressayer :");
                    x = sc.nextInt();
                }
                if (bd.updateQuery("insert into gerer_page (id_membre,id_page) values ("+Integer.toString(membres.get(x-1).id)+" , "+ Integer.toString(pages.get(p-1).id)+");")==1){
                    System.out.println("\nAdmin ajouter avec Succée :D \n");
                    menu(id);
                }
            }else if(v==2){

                menu(id);
            }else {

                menu(id);
            }
        }
        catch (SQLException e){
            System.out.println(e.toString());
        }
    }

    public static void likerPage (int id )      {
        JavaMySQL bd = new JavaMySQL();
        Connection cx = bd.openConnection();
        Scanner sc = new Scanner(System.in);
        ArrayList<Page> pages = new ArrayList<Page>();
        try{
            System.out.println("Saisir le nom du page à aimer : ");
            String nom = sc.nextLine();
            System.out.println("Voilà la liste des pages : ");
            ResultSet res = bd.selectQuery("select * from page where nom like '" + nom + "';");
            while (res.next()) {
                Page page = new Page(res.getInt(1), res.getString(2) , res.getString(3) );
                pages.add(page);
            }
            for (int i = 0; i < pages.size(); i++)
                System.out.println(i + 1 + " - " + pages.get(i).nom);
            System.out.print("Choisir la page : ");
            int x = sc.nextInt();
            while (x < 1 || x > pages.size()) {
                System.out.print("choix érronée ! ressayer :");
                x = sc.nextInt();
            }

            int r = bd.updateQuery("insert into aime (id_membre,id_page) values ("+Integer.toString(id)+
                    ","+  Integer.toString(pages.get(x-1).id) +");" );

            if (r ==1){
                System.out.println("\n Vous avez aimé cette page !\n ");
                menu(id);
            }else {
                System.out.println("Erreur !");
            }

        }
        catch (SQLException e){
            System.out.println("erreur");
        }
    }

    public static void creerGroupe(int id)      {
        JavaMySQL bd = new JavaMySQL();
        Connection cx = bd.openConnection();
        Scanner sc = new Scanner(System.in);
        ArrayList<Genre> genres = new ArrayList<Genre>();
        try {
            System.out.println("\nCREER UN GROUPE \n");
            System.out.print(" Saisir le Nom du groupe : ");
            String nom = sc.nextLine();
            ResultSet res = bd.selectQuery("select * from genre;");
            while (res.next()){
                Genre g = new Genre(res.getInt(1), res.getString(2));
                genres.add(g);
            }
            for (int i=0 ; i<genres.size();i++)
                System.out.println( i+1 +" - "+ genres.get(i).libelle);
            System.out.print(" Choisir le genre de votre groupe : ");
            int x =sc.nextInt();
            while (x<1 || x> genres.size()){
                System.out.print("choix érronée ! ressayer :");
                x = sc.nextInt();
            }

            LocalDateTime dt = LocalDateTime.now();
            int y = dt.getYear();
            int m = dt.getMonthValue();
            int d = dt.getDayOfMonth();
            int hr = dt.getHour();
            int mn = dt.getMinute();
            int sd = dt.getSecond();

            int u = bd.updateQuery("insert into groupe (nom,date,genre,id_createur) values ('" + nom +
                    "','"  +Integer.toString(y)+":"+Integer.toString(m)+":"+Integer.toString(d)+" " +
                    ""+Integer.toString(hr)+":"+Integer.toString(mn)+":"+Integer.toString(sd)+  "'," +
                    Integer.toString(genres.get(x-1).id) + ","+Integer.toString(id)+");");

            if (u==1){
                ResultSet resultSet = bd.selectQuery("select * from groupe where nom like '"+nom+"';");
                if (resultSet.next()){
                    System.out.println(Integer.toString(resultSet.getInt(1)));
                    u =  bd.updateQuery("insert into gerer_groupe (id_membre,id_groupe) values ("+Integer.toString(id)+","+Integer.toString(resultSet.getInt(1))+");");
                    System.out.println("\n Votre groupe à été crée avec succés !\n ");
                    menu(id);
                }else {
                    System.out.println("Erreur !");
                }
            }else {
                System.out.println("Erreur !");
            }
        }
        catch (SQLException e){

        }
    }

    //Ajouter recherche(backtraking) avant de rejoindre
    public static void rejoindreGroupe(int id)  {
        JavaMySQL bd = new JavaMySQL();
        Connection cx = bd.openConnection();
        Scanner sc = new Scanner(System.in);
        ArrayList<Groupe> groupes = new ArrayList<Groupe>();
        try{
            System.out.println("Saisir le nom du groupe à rejoindre : ");
            String nom = sc.nextLine();

            ResultSet res = bd.selectQuery("select * from groupe where nom like '" + nom + "';");


            while (res.next()) {
                Groupe groupe = new Groupe(res.getInt(1), res.getString(2), res.getString(3));
                groupes.add(groupe);
            }

            if(groupes.size()==0) {
                System.out.println("Pas de groupe avec ce nom !");
                rejoindreGroupe(id);
            }else {
                System.out.println("Voilà la liste des groupes : ");

                for (int i = 0; i < groupes.size(); i++) {

                    System.out.println(i + 1 + " - " + groupes.get(i).nom + "  crée le  " + groupes.get(i).date);
                }

                System.out.print("Choisir le groupe : ");
                int x = sc.nextInt();
                while (x < 1 || x > groupes.size()) {
                    System.out.print("choix érronée ! ressayer :");
                    x = sc.nextInt();
                }

                int r = bd.updateQuery("insert into rejoindre (id_membre,id_groupe) values (" + Integer.toString(id) +
                        "," + Integer.toString(groupes.get(x - 1).id) + ");");


                if (r == 1) {
                    System.out.println("\n Vous avez rejoint ce groupe !\n ");
                    menu(id);
                } else {
                    System.out.println("Erreur !");
                }
            }
        }
        catch (SQLException e){
            System.out.println("erreur");
        }
    }

    //Ajouter recherche(backtraking) avant de creer
    public static void creerPub (int id )       {
        JavaMySQL bd = new JavaMySQL();
        Connection cx = bd.openConnection();
        Scanner sc = new Scanner(System.in);
        ArrayList<Membre> membres = new ArrayList<Membre>();
        try {
            System.out.println("\n PUBLIER SUR LE MUR DE VOTRE AMI ");
            System.out.print("prenom d'ami : ");
            String prenom = sc.nextLine();
            ResultSet res = bd.selectQuery("select * from membre where nom LIKE '" + prenom + "'  or prenom LIKE '" + prenom + "' and id <> " + Integer.toString(id) + ";");

            while (res.next()) {

                Membre membre = new Membre(res.getInt(1), res.getString(2), res.getString(3), res.getString(4), res.getString(5), res.getInt(6));
                membres.add(membre);

            }
            System.out.println("le quel d'aprés cette liste :");
            for (int j = 0; j < membres.size(); j++) {
                System.out.println(Integer.toString(j+1)+ " : " + membres.get(j).nom + " - " + membres.get(j).prenom);
            }
            int i = sc.nextInt();
            System.out.println("Que voulez-vous dire ? ");
            Scanner scc = new Scanner(System.in);
            String pub = scc.nextLine();

            LocalDateTime dt = LocalDateTime.now();
            int y = dt.getYear();
            int m = dt.getMonthValue();
            int d = dt.getDayOfMonth();
            int hr = dt.getHour();
            int mn = dt.getMinute();
            int sd = dt.getSecond();

            int u = bd.updateQuery("insert into poste (date,contenu,id_emetteur,id_recepteur) values ('" + Integer.toString(y) + ":" + Integer.toString(m) + ":" + Integer.toString(d) + " " + Integer.toString(hr) + ":" + Integer.toString(mn) + ":" + Integer.toString(sd) + "','" + pub + "'," + Integer.toString(id) + "," + Integer.toString(membres.get(i-1).id) + ");");
            if (u == 1) {
                System.out.println("\n Votre message est publié  !\n ");
                menu(id);
            } else {
                System.out.println("Erreur !");
            }
        } catch (SQLException e) {
            System.out.println(e.toString());
        }
    }

    public static void ajouterAmi(int id){
        JavaMySQL bd = new JavaMySQL();
        Connection cx = bd.openConnection();
        Scanner sc = new Scanner(System.in);
        ArrayList<Membre> amis = new ArrayList<Membre>();
        ArrayList<Membre> mesAmis = new ArrayList<Membre>();
        mesAmis = getAmis(id,bd);
        try {
            System.out.println("\nAJOUTER UN AMI \n");
            ResultSet r = bd.selectQuery("select * from membre where  id <> "+Integer.toString(id)+";");
            while (r.next()){
                Membre m = new Membre(r.getInt(1), r.getString(2), r.getString(3), r.getString(4), r.getString(5), r.getInt(6));
                if(!exist(m.id,mesAmis)){
                    amis.add(m);
                }
            }
            for (int i=0 ; i<amis.size();i++)
                System.out.println( i+1 +" - "+ amis.get(i).nom+" - " +amis.get(i).prenom+" - "+amis.get(i).email+" - "+amis.get(i).age );
            System.out.print("\n Choisir le numéro de l'ami que vous voulez ajouter ' : ");
            int x =sc.nextInt();
            while (x<1 || x> amis.size()){
                System.out.print("Le choix saisie est érroné, réeseyer : ");
                x = sc.nextInt();
            }
            int id_r =amis.get(x-1).id;
            int u = bd.updateQuery("insert into demande (etat,id_recepteur,id_emetteur) values (0 ,"  +Integer.toString(id_r)+","+Integer.toString(id) +");");
            if (u==1){
                System.out.println("\n Votre demande à été effectuée avec succés !\n ");
                menu(id);
            }else {
                System.out.println("Erreur !");
            }
        }
        catch (SQLException e){
            System.out.println(e.toString());
        }
    }

    public static void consulterlesdemandes(int id){
        JavaMySQL bd    = new JavaMySQL();
        Connection cx   = bd.openConnection();
        Scanner sc      = new Scanner(System.in);
        ArrayList<Demande> demandes = new ArrayList<Demande>();

        try {
            System.out.println("\nCONSULTER LES DEMANDES ");
            ResultSet res = bd.selectQuery("select * from demande d,membre m where d.etat=0 and m.id=d.id_emetteur  and d.id_recepteur="+Integer.toString(id)+";");
            while (res.next()){
                Demande m = new Demande(res.getInt(2),res.getInt(3),res.getString(6),res.getString(7));
                demandes.add(m);
            }
            for (int i=0 ; i<demandes.size();i++){
                System.out.println( i+1 +" - " +demandes.get(i).nom+" "+demandes.get(i).prenom  );
            }
            if(demandes.size()==0){
                System.out.println( "\n vous n'avez aucune demande" );
                menu(id);
            }else {
                System.out.print("\n Taper le numéro de la demande à traiter :");
                int x = sc.nextInt();
                while (x < 1 || x > demandes.size()) {
                    System.out.print("Le choix saisie est érroné, réeseyer : ");
                    x = sc.nextInt();
                }
                int id_e = demandes.get(x - 1).id_emetteur;
                ResultSet res1 = bd.selectQuery("select * from membre where id=" + Integer.toString(id_e) + ";");
                String n = "";
                String p = "";
                if (res1.next()) {
                    n = res1.getString(2);
                    p = res1.getString(3);
                    System.out.print(" 1 : accepter / 2: ignorer  L' invitation de " + n + " " + p + " ? : ");
                }

                int réponse = sc.nextInt();
                while (réponse > 2 || réponse < 1) {
                    System.out.print("choix érronée ! ressayer :");
                    réponse = sc.nextInt();
                }
                if (réponse == 1) {
                    if (bd.updateQuery("update demande set etat=" + Integer.toString(1) + " where id_recepteur=" + Integer.toString(id) + " and id_emetteur=" + Integer.toString(id_e) + "  ;") == 1)
                        System.out.print("\nvous avez accepté l'invitation de " + n + " " + p + "!\n\n");
                } else if (réponse == 2) {
                    if (bd.updateQuery("update demande set etat=" + Integer.toString(-1) + " where id_recepteur=" + Integer.toString(id) + " and id_emetteur=" + Integer.toString(id_e) + "  ;") == 1)
                        System.out.print("\nvous avez ignoré l'invitation de " + n + " " + p + "\n\n");
                }
                menu(id);
            }
        }
        catch (SQLException e){
            System.out.println(e.toString());
        }
    }


    public static ArrayList<Membre> getAmis         (int id, JavaMySQL bd)  {
        ArrayList <Membre> tAmis = new ArrayList<Membre>();
        try {
            //récuperer mes amis de la base de données
            ResultSet res = bd.selectQuery("select * from demande d where (d.id_emetteur = " + Integer.toString(id) + " or d.id_recepteur =" + Integer.toString(id) +" ) ;");
            while (res.next()){
                if (res.getInt(3)==id){
                    //mon ami c'est le recepteur : récuperer ces informations
                    ResultSet r = bd.selectQuery("select * from membre where id = "+Integer.toString(res.getInt(4))+";");
                    if (r.next()){
                        Membre m = new Membre(r.getInt(1), r.getString(2), r.getString(3), r.getString(4), r.getString(5), r.getInt(6));
                        tAmis.add(m);
                    }
                }else {
                    //mon ami c'est le emmetteur : récuperer ces informations
                    ResultSet r = bd.selectQuery("select * from membre where id = "+Integer.toString(res.getInt(3))+";");
                    if (r.next()){
                        Membre m = new Membre(r.getInt(1), r.getString(2), r.getString(3), r.getString(4), r.getString(5), r.getInt(6));
                        tAmis.add(m);
                    }
                }
            }
        }
        catch (SQLException e){
            System.out.println(e.toString() );
        }
        return tAmis;
    }

    public static ArrayList<Page>   getPages        (int id, JavaMySQL bd)  {
        ArrayList <Page> tPages = new ArrayList<Page>();
        try {
            //récuperer de la base de données les pages que j'aime
            ResultSet res = bd.selectQuery("SELECT * FROM aime a, page p,genre g,membre m  where p.id_genre=g.id and p.id_createur=m.id and p.id=a.id_page and a.id_membre =" + Integer.toString(id) + " ;");
            while (res.next()) {
                Page p = new Page (res.getInt(4), res.getString(5),res.getString(6),res.getString(10),res.getString(12),res.getString(13));
                tPages.add(p);
            }
        }
        catch (SQLException e){
            System.out.println(e.toString());
        }
        return tPages;
    }

    public static ArrayList<Membre> amisCommun      (int id,JavaMySQL bd)   {
        //la liste de mes amis
        ArrayList <Membre> tAmis = new ArrayList<Membre>();
        //va contenir la liste des amis en commun
        ArrayList <Membre> tCommun = new ArrayList<Membre>();
        //récuperer mes amis
        tAmis = getAmis(id,bd);
        //récuperer les amis de mes amis
        for (int n=0;n<tAmis.size();n++ ){
            tAmis.get(n).amis = getAmis(tAmis.get(n).id,bd);
            //récuperer les amis des amis de mes amis
            for (int m=0;m<tAmis.get(n).amis.size();m++ ){
                tAmis.get(n).amis.get(m).amis = getAmis(tAmis.get(n).amis.get(m).id,bd);
            }
        }
        //affichage pour tester
        /*for (int i=0 ; i<tAmis.size();i++){
            System.out.println(tAmis.get(i).nom+" " +tAmis.get(i).prenom);
            for (int j=0;j<tAmis.get(i).amis.size();j++){
                System.out.println(" 1--- "+tAmis.get(i).amis.get(j).nom+" "+tAmis.get(i).amis.get(j).prenom);
                for (int k=0;k<tAmis.get(i).amis.get(j).amis.size();k++){
                    System.out.println(" ---2--- "+tAmis.get(i).amis.get(j).amis.get(k).nom+" "+tAmis.get(i).amis.get(j).amis.get(k).prenom);
                }
            }
        }*/

        for (int i =0;i<tAmis.size();i++){
            for (int j=0;j<tAmis.get(i).amis.size();j++){
                //verifier qu'il n'est pas déjà ajouter dans tCommun, n'est pas ni un ami direct ni moi et il m'a pas envoyé une demande
                if (!exist(tAmis.get(i).amis.get(j).id,tCommun)&&(tAmis.get(i).amis.get(j).id!=id)&&!exist(tAmis.get(i).amis.get(j).id,tAmis)){
                    tAmis.get(i).amis.get(j).nbr=0;
                    tCommun.add(tAmis.get(i).amis.get(j));
                        for (int x=0;x<tAmis.get(i).amis.get(j).amis.size();x++){
                            if(exist(tAmis.get(i).amis.get(j).amis.get(x).id,tAmis)){
                                tCommun.get(tCommun.size()-1).nbr += 1;
                            }
                        }
                }
            }
        }
        Collections.sort(tCommun,Collections.reverseOrder());
        return tCommun;
    }

    public static ArrayList<Membre> pageCommune     (int id,JavaMySQL bd)   {
        ArrayList <Membre> tAmis = new ArrayList<Membre>();
        tAmis = getAmis(id,bd);
        ArrayList <Page> tPages = new ArrayList<Page>();
        ArrayList <Membre> tCommun = new ArrayList<Membre>();
        tPages=getPages(id,bd);
        for(int i=0; i< tPages.size();i++){
            try {
                ResultSet res = bd.selectQuery("select * from aime a , membre m where a.id_membre=m.id and a.id_page ="+Integer.toString(tPages.get(i).id)+";");
                while (res.next()){
                    Membre m = new Membre(res.getInt(4), res.getString(5), res.getString(6), res.getString(7), res.getString(8), res.getInt(9));
                    tPages.get(i).pAime.add(m);
                    for (int j=0;j<tPages.get(i).pAime.size();j++){
                        tPages.get(i).pAime.get(j).pages = getPages(tPages.get(i).pAime.get(j).id,bd);
                    }
                }
            }catch (SQLException e){
                System.out.println(e.toString());
            }
        }
        for (int i =0;i<tPages.size();i++){
            for (int j=0;j<tPages.get(i).pAime.size();j++){
                if (!exist(tPages.get(i).pAime.get(j).id,tCommun)&&(tPages.get(i).pAime.get(j).id!=id)&&!exist(tPages.get(i).pAime.get(j).id,tAmis)){
                    tPages.get(i).pAime.get(j).nbr=0;
                    tCommun.add(tPages.get(i).pAime.get(j));
                        for (int x=0;x<tPages.get(i).pAime.get(j).pages.size();x++){
                            if(existP(tPages.get(i).pAime.get(j).pages.get(x).id,tPages)){
                                tCommun.get(tCommun.size()-1).nbr += 1;
                            }
                        }
                }
            }
        }
        Collections.sort(tCommun,Collections.reverseOrder());
        return tCommun;
    }

    public static ArrayList<Membre> plusCourtChemin (int id,JavaMySQL bd)   {

        return null;
    }

    public static void      suggestionAmis(int id)  {
        JavaMySQL bd = new JavaMySQL();
        Connection cx = bd.openConnection();
        System.out.println("\nLes critères de suggestion possible : ");
        System.out.println(" 1 : Nombre d'amis en commun");
        System.out.println(" 2 : Nombre de pages communes");
        System.out.println(" 3 : Le plus court chemin");
        System.out.print("\n selectionner le critère : ");
        Scanner scc = new Scanner(System.in);
        int s=scc.nextInt();
        while (s<0 || s>3){
            System.out.print("Le choix saisie est érroné, réeseyer : ");
            s=scc.nextInt();
        }
        ArrayList <Membre> tCommun = new ArrayList<Membre>();
        if (s==1){
            tCommun = amisCommun(id,bd);
            affichage("amis en commun",tCommun,id,bd);
        }else if(s==2){
            tCommun = pageCommune(id,bd);
            affichage("pages commune",tCommun,id,bd);
        }else {
            tCommun = plusCourtChemin(id,bd);
            affichage("plus court chemin",tCommun,id,bd);
        }
    }

    public static void      affichage(String s,ArrayList<Membre> tCommun,int id,JavaMySQL bd){
        System.out.println("\nSuggestions d'amis :\n");
        for (int i=0;i<tCommun.size();i++){
            System.out.println( i+1 +" : " +tCommun.get(i).nom+" "+tCommun.get(i).prenom+" ( "+tCommun.get(i).nbr +" "+s+" )");
        }
        System.out.print("\nVoulez vous ajouter un(e) ami(e) ?  son numéro, autre sinon ! : ");
        Scanner sc = new Scanner(System.in);
        int v = sc.nextInt();
        if ((v<1) || (v>tCommun.size()) ){
            menu(id);
        }else {
            try {
                if (bd.updateQuery("insert into demande (etat,id_emetteur,id_recepteur) values (0,"+Integer.toString(id)+","+Integer.toString(tCommun.get(v-1).id)+");")==1){
                    System.out.println("\nDemande envoyé avec succées :) \n");
                    menu(id);
                }

            }catch (SQLException e){
                System.out.println(e.toString());
            }
        }
    }

    public static boolean   exist(int id,ArrayList<Membre> tAmis)   {
        boolean ok=false;
        int x=0;
        while (x<tAmis.size() && !ok){
            ok= tAmis.get(x).id == id ;
            x++;
        }
        return ok;
    }

    public static boolean   existP(int id,ArrayList<Page> tPages)   {
        boolean ok=false;
        int x=0;
        while (x<tPages.size() && !ok){
            ok= tPages.get(x).id == id ;
            x++;
        }
        return ok;
    }

    public static void      main(String[] args) throws SQLException {

        System.out.println("\n\nBienvenue dans votre Réseau Social :)  \n\n  1 : s'authentifier \n  2 : créer un compte ");
        Scanner sc = new Scanner(System.in);
        System.out.print("\nchoix = ");
        int x = sc.nextInt();
        while (x<1 || x>2){
            System.out.print("Le choix saisie est érroné, réeseyer : ");
            x = sc.nextInt();
        }
        if(x == 1 ){
            authentifier();
        }else {
            creerCompte();
        }
    }
}