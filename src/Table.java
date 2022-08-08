import javax.swing.*;
import java.io.*;
import java.util.*;

public class Table{
    public final static int NBCOLONNE = 10;

    private Colonne[] table;
    private Deck talon;
    private int score = 500;
    private int nbTalon = 5;

    // Constructeur d'une table
    public Table(Deck talon) {
        table = new Colonne[NBCOLONNE];
        this.talon = talon;

        for(int i = 0; i < table.length; i++) {
            if(i < 4)
                table[i] = new Colonne(6, this.talon);
            else
                table[i] = new Colonne(5, this.talon);

            // On retourne la dernière carte de la colonne
            retournerCarte(i);
        }
    }

    public Colonne[] getTable() {
        return table;
    }

    public Deck getTalon() {
        return talon;
    }

    // retourne la colonne i
    public Colonne getColonneTable(int i) {
        return table[i];
    }

    public int getScore() {
        return score;
    }

    public int getNbTalon() {
        return nbTalon;
    }

    public void setTable(Colonne[] table) {
        this.table = table;
    }

    public void setTalon(Deck talon) {
        this.talon = talon;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void setNbTalon(int nbTalon) {
        this.nbTalon = nbTalon;
    }

    // retourne la dernière carte de la colonne indiquée en paramètre
    public void retournerCarte(int numeroColonne) {
        if(table[numeroColonne].getSize() != 0)
            getColonneTable(numeroColonne).getCol().get(table[numeroColonne].getSize() - 1).setFaceDecouverte(true);
    }

    // Distribue un talon, soit une carte à chaque colonne si aucune colonne n'est vide
    public void distributionTalon() {
        boolean colonneVide = false;
        for (int i = 0; i < table.length; i++)
            if(table[i].getSize() == 0)
                colonneVide = true;

        if(colonneVide)
            System.out.println("Vous ne pouvez pas faire cette action tant qu'une colonne est vide");
        else if(talon.cartesRestantes() != 0) {
            for (int i = 0; i < table.length; i++) {
                table[i].getCol().add(talon.distribuerCarte());
                retournerCarte(i);
            }

            score--;
            nbTalon--;
        }
        else
            System.out.println("Talon vide");
    }

    // Retourne vrai si le joueur a gagné
    public boolean victory() {
        for(int i = 0; i < table.length; i++)
            if(table[i].getSize() != 0) // Une colonne n'est pas vide
                return false;
        if(score <= 0 || nbMouvement() == 0) // Score inférieure à 0 ou plus de mouvement disponible
            return false;

        return talon.cartesRestantes() == 0;
    }

    // Retourne le nombre de mouvements possible pour le joueur
    public int nbMouvement() {
        int cpt = 0;

        for(int i = 0; i < getTable().length; i++) {
            for(int j = getColonneTable(i).getSize(); j > 0; j--) {
                ArrayList<Cards> selection = new ArrayList<Cards>();

                if(getColonneTable(i).selectionValide(j)) {
                    selection = getColonneTable(i).selectionCarte(j);

                    for(int k = 0; k < getTable().length; k++) {
                        if(k != i) {
                            Mouvement move = new Mouvement(this, (char) (i + 65), (char) (k + 65), j, selection, false);

                            if (move.mouvementValide())
                                cpt++;
                        }
                    }
                }
            }
        }

        return cpt;
    }

    // Retourne la liste des mouvements possibles
    /*public Map<Integer, Mouvement> listeMouvement() {
        Map<Integer, Mouvement> liste = new HashMap<>();
        int cpt = 0;
        for(int i = 0; i < getTable().length; i++) {
            for(int j = getColonneTable(i).getSize(); j > 0; j--) {
                ArrayList<Cards> selection = new ArrayList<Cards>();
                if(getColonneTable(i).selectionValide(j)) {
                    selection = getColonneTable(i).selectionCarte(j);
                    for(int k = 0; k < getTable().length; k++) {
                        if(k != i) {
                            Mouvement move = new Mouvement(this, (char) (i + 65), (char) (k + 65), j, selection, false);
                            if (move.mouvementValide()) {
                                liste.put(cpt, move);
                                cpt++;
                            }
                        }
                    }
                }
            }
        }

        return liste;
    }*/

    // Retourne l'état des colonnes, vrai si une colonne est vide
    public boolean[] colonneVide() {
        boolean[] colonneVide = new boolean[table.length];
        for(int i = 0; i < table.length; i++)
            colonneVide[i] = getColonneTable(i).getSize() == 0;

        return colonneVide;
    }

    // Retourne la longueur maximale d'une carte dans la colonne i, pour un affichage uniforme de la colonne i
    public int longueurMaxString(int i) {
        if(table[i].getCol().size() == 0) // Si aucune carte, on fixe la colonne à une longueur de 10 espaces
            return 10;
        else {
            // Recherche du max
            int longueurMax = table[i].getCol().get(0).lengthString();

            for (int j = 1; j < table[i].getCol().size(); j++)
                if (table[i].getCol().get(j).lengthString() > longueurMax)
                    longueurMax = table[i].getCol().get(j).lengthString();

            return longueurMax;
        }
    }

    // Retoune la hauteur maximale des colonnes sur la table
    public int longueurMaxColonne() {
        int longueurMaxColonne = table[0].getSize();

        for(int i = 1; i < table.length; i++)
            if(table[i].getSize() > longueurMaxColonne)
                longueurMaxColonne = table[i].getSize();

        return longueurMaxColonne;
    }

    // Affichage de la table de jeu
    @Override
    public String toString() {
        String str = "     "; // pour aligner les colonnes
        String strLigne;

        for(int i = 0; i < table.length; i++) { //boucle pour mettre les lettres
            strLigne = "";
            strLigne = strLigne.concat(String.valueOf((char)(i + 65))); //Lettres en majuscules commencent à 65 en ASCII
            while (strLigne.length() < longueurMaxString(i) + 3) //+3 pour " | "
                strLigne = strLigne.concat(" ");
            str = str.concat(strLigne);
        }
        str = str.concat("\n");

        for(int i = 0; i < longueurMaxColonne(); i++) {
            strLigne = "";

            if(i<9)
                str = str.concat((i + 1) + "  ");
            else
                str = str.concat((i + 1) + " "); // pour faire jolie

            for(int j = 0; j < table.length; j++) {
                String strColonne = "";
                boolean presenceCarte = false;

                if(i < table[j].getCol().size())
                    presenceCarte = table[j].getCol().get(i) != null;

                if(presenceCarte) {
                    strLigne = strLigne.concat("|");
                    strColonne = strColonne.concat(" ");
                    if (i < table[j].getCol().size() && !table[j].getCarteCol(i).getFaceDecouverte()) {
                        while (strColonne.length() - 1 < longueurMaxString(j))
                            strColonne = strColonne.concat("-");
                    } else if (i < table[j].getCol().size() && table[j].getCarteCol(i).getFaceDecouverte()) {
                        strColonne = strColonne.concat(table[j].getCol().get(i).toString());
                        while (strColonne.length() - 1 < longueurMaxString(j))
                            strColonne = strColonne.concat(" ");
                    }
                    strColonne = strColonne.concat(" ");
                    if(j == table.length - 1 || (j < table.length - 1 && i >= table[j+1].getCol().size()))
                        strColonne = strColonne.concat("|");
                } else {
                    if(j == 0)
                        strLigne = strLigne.concat(" ");

                    while(strColonne.length() < longueurMaxString(j) + 2)
                        strColonne = strColonne.concat(" ");

                    boolean presenceCarteSuivante = false;
                    if(j<table.length -1 && i < table[j + 1].getCol().size())
                        presenceCarteSuivante = table[j + 1].getCol().get(i) != null;
                    if(!presenceCarteSuivante) {
                        strColonne = strColonne.concat(" ");
                    }
                }
                strLigne = strLigne.concat(strColonne);
            }
            str = str.concat(strLigne + "\n");
        }

        return str + "\nVotre score est de : " + getScore() + "\nVous pouvez encore tirer " + getNbTalon() + " talon(s).";
    }
}