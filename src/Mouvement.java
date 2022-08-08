import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Map;

public class Mouvement {
    private Table table;
    private char colonneDepart;
    private char colonneArrivee;
    private int numLigne;
    private ArrayList<Cards> selection;
    private ArrayList<Cards> suite;
    private boolean distributionTalon;
    private boolean faceCardsUnder;
    private boolean faceCardsUnderSuite;
    private boolean[] suiteAfterTalon;
    private Map<Integer, ArrayList<Cards>> suiteTalon;

    // Constructeur
    public Mouvement(Table table, char colonneDepart, char colonneArrivee, int numLigne, ArrayList<Cards> selection, boolean distributionTalon) {
        this.table = table;
        this.colonneDepart = colonneDepart;
        this.colonneArrivee = colonneArrivee;
        this.numLigne = numLigne;
        this.selection = selection;
        this.distributionTalon = distributionTalon;
    }

    public int getColonneDepart() {
        return colonneDepart - 65;
    }

    public int getColonneArrivee() {
        return colonneArrivee - 65;
    }

    public int getNumLigne() {
        return numLigne;
    }

    public ArrayList<Cards> getSelection() {
        return selection;
    }

    public ArrayList<Cards> getSuite() {
        return suite;
    }

    public boolean isDistributionTalon() {
        return distributionTalon;
    }

    public boolean isFaceCardsUnder() {
        return faceCardsUnder;
    }

    public boolean isFaceCardsUnderSuite() {
        return faceCardsUnderSuite;
    }

    // Renvoie true si on a une suite automatiquement après avoir distribué un talon
    public boolean[] getSuiteAfterTalon() {
        suiteAfterTalon = new boolean[]{false, false, false, false, false, false, false, false, false, false};

        if(isDistributionTalon()) {
            for(int i = 0; i < table.getTable().length; i++) {
                suiteAfterTalon[i] = presenceSuite(i);
                if(suiteAfterTalon[i]) {
                    ArrayList<Cards> suiteSelection = new ArrayList<Cards>();
                    for(int j = table.getColonneTable(i).getSize() - Deck.NBCARTESPARCOULEURS - 1; j < table.getColonneTable(i).getSize(); j++)
                        suiteSelection.add(table.getColonneTable(i).getCarteCol(j));
                    suiteTalon.put(i, suiteSelection);
                    table.getColonneTable(i).removeInColonne(suiteSelection);
                }
            }
        }

        return suiteAfterTalon;
    }

    // Renvoie true si on a une suite valide, même couleur
    public boolean presenceSuite(int numeroColonne) {
        if(table.getColonneTable(numeroColonne).getSize() > 12 && table.getColonneTable(numeroColonne).getCarteCol(table.getColonneTable(numeroColonne).getSize() - 1).getValeur() == 1) {
            for(int i = table.getColonneTable(numeroColonne).getSize() - 1 - 1; i > table.getColonneTable(numeroColonne).getSize() - 13 - 1; i--) {
                if(!table.getColonneTable(numeroColonne).getCarteCol(i).getFaceDecouverte())
                    return false;
                if (table.getColonneTable(numeroColonne).getCarteCol(i).getCouleur() != table.getColonneTable(numeroColonne).getCarteCol(i + 1).getCouleur())
                    return false;
                if (table.getColonneTable(numeroColonne).getCarteCol(i).getValeur() != table.getColonneTable(numeroColonne).getCarteCol(i + 1).getValeur() + 1)
                    return false;
            }

            return true;
        }

        return false;
    }

    // renvoie true si le mouvement est valide
    public boolean mouvementValide() {
        if(getColonneDepart() >= 10 || getColonneArrivee() >= 10)
            return false;
        if(getNumLigne() - 1 >= table.getColonneTable(getColonneDepart()).getSize())
            return false;
        if(table.getColonneTable(getColonneArrivee()).getSize() == 0)
            return true;
        if(table.getColonneTable(getColonneDepart()).selectionValide(getNumLigne()))
            return table.getColonneTable(getColonneDepart()).getCarteCol(getNumLigne() - 1).getValeur() + 1 == table.getColonneTable(getColonneArrivee()).getCarteCol(table.getColonneTable(getColonneArrivee()).getSize() - 1).getValeur();

        return false;
    }

    // Effectue le mouvement choisi par le joueur et modifie le score du joueur
    public void mouvement() {
        if(mouvementValide()) {
            table.getColonneTable(getColonneArrivee()).addInColonne(getSelection());
            table.getColonneTable(getColonneDepart()).removeInColonne(getSelection());
            if(table.getColonneTable(getColonneDepart()).getSize() > 0)
                faceCardsUnder = table.getColonneTable(getColonneDepart()).getCarteCol(table.getColonneTable(getColonneDepart()).getSize() - 1).getFaceDecouverte();
            else
                faceCardsUnder = true;
            table.retournerCarte(getColonneDepart());

            suite = new ArrayList<Cards>();
            if(presenceSuite(getColonneArrivee())) {
                table.setScore(table.getScore() + 100);
                getSuite().clear();

                for(int i = table.getColonneTable(getColonneArrivee()).getSize() - 13; i < table.getColonneTable(getColonneArrivee()).getSize(); i++)
                    getSuite().add(table.getColonneTable(getColonneArrivee()).getCarteCol(i));
                table.getColonneTable(getColonneArrivee()).removeInColonne(getSuite());

                if(table.getColonneTable(getColonneArrivee()).getSize() - 1 > 0)
                    faceCardsUnderSuite = table.getColonneTable(getColonneArrivee()).getCarteCol(table.getColonneTable(getColonneArrivee()).getSize() - 1).getFaceDecouverte();
                else
                    faceCardsUnderSuite = true;

                table.retournerCarte(getColonneArrivee());
            } else
                table.setScore(table.getScore() - 1);
        } else {
            System.out.println("\nMouvement invalide !\n\n");
        }
    }

    // Effectue un retour en arrière d'un mouvement
    public void retourEnArriere(boolean distributionTalon) {
        // cas du talon
        if(distributionTalon) {
            for(int i = 0; i < table.getTable().length; i++) {
                // cas de la suite après distribution du talon
                if(getSuiteAfterTalon()[i]) {
                    table.getColonneTable(i).addInColonne(suiteTalon.get(i));
                    table.setScore(table.getScore() - 100);
                }
            }

            for(int i = table.getTable().length - 1; i >= 0; i--) {
                ArrayList<Cards> carteAEnlever = new ArrayList<Cards>();
                carteAEnlever.add(table.getColonneTable(i).getCarteCol(table.getColonneTable(i).getSize() - 1));
                table.getColonneTable(i).getCarteCol(table.getColonneTable(i).getSize() - 1).setFaceDecouverte(false);
                table.getTalon().addCards(table.getColonneTable(i).getCarteCol(table.getColonneTable(i).getSize() - 1));
                table.getColonneTable(i).removeInColonne(carteAEnlever);
            }
            table.setScore(table.getScore() - 1);
            table.setNbTalon(table.getNbTalon() + 1);
        } else {
            if(getSuite().size() != 0) {
                table.getColonneTable(getColonneArrivee()).addInColonne(getSuite());

                if(!isFaceCardsUnderSuite())
                    table.getColonneTable(getColonneArrivee()).getCarteCol(table.getColonneTable(getColonneArrivee()).getSize() - getSuite().size() - 1).setFaceDecouverte(false);
                table.setScore(table.getScore() - 100);
            }
            else
                table.setScore(table.getScore() - 1);

            table.getColonneTable(getColonneDepart()).addInColonne(getSelection());
            table.getColonneTable(getColonneArrivee()).removeInColonne(getSelection());

            if(!isFaceCardsUnder())
                table.getColonneTable(getColonneDepart()).getCarteCol(table.getColonneTable(getColonneDepart()).getSize() - getSelection().size() - 1).setFaceDecouverte(false);
        }
    }
}
