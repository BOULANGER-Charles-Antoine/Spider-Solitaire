import java.util.ArrayList;

public class Colonne{
    private ArrayList<Cards> col;
    private int capaciteInitial;
    private Deck deck;

    // Constructeur d'une colonne de carte de la table
    public Colonne(int capaciteInitial, Deck deck) {
        this.capaciteInitial = capaciteInitial;
        col = new ArrayList<Cards>(capaciteInitial);
        for(int i = 0; i<capaciteInitial; i++)
            col.add(i, deck.distribuerCarte());
    }

    public int getSize() {
        return col.size();
    }

    public ArrayList<Cards> getCol() {
        return col;
    }

    public Cards getCarteCol(int i) {
        return col.get(i);
    }

    // Ajoute une carte en bas de la colonne
    public void addInColonne(ArrayList<Cards> pack) {
        col.addAll(pack);
    }

    // Retire un ensemble de carte de la colonne
    public void removeInColonne(ArrayList<Cards> pack) {
        col.subList(col.size()-pack.size(), col.size()).clear();
    }

    // Retourne la validité de la sélection du joueur
    public boolean selectionValide(int numeroLigne) {
        for(int i = numeroLigne - 1; i < getSize(); i++) {
            // Il faut que toutes les cartes de la sélection soit face découverte
            if(!getCarteCol(i).getFaceDecouverte())
                return false;
            // Il faut que les cartes soit de la même couleur et forme une suite
            if(i != numeroLigne - 1 &&  (getCarteCol(i - 1).getCouleur() != getCarteCol(i).getCouleur() || getCarteCol(i - 1).getValeur() != getCarteCol(i).getValeur() + 1))
                return false;
        }

        return true;
    }

    //  retourne la sélection complète de carte à partir d'une ligne passée en paramètres, de la ligne jusqu'en bas de la colonne
    public ArrayList<Cards> selectionCarte(int numeroLigne) {
        ArrayList<Cards> selection = new ArrayList<Cards>();

        if(selectionValide(numeroLigne))
            for(int i = numeroLigne - 1; i < getSize(); i++)
                selection.add(getCarteCol(i));

        return selection;
    }

    // Affiche la colonne de la table
    public String toString() {
        String str = "";
        for(int i = 0; i<col.size(); i++) {
            if(i<col.size()-1)
                str = str.concat("| - |\n");
            else
                str = str.concat("| " + col.get(i) + " |\n");
        }

        return str;
    }
}
