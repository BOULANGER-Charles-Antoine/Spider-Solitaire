import java.util.Random;

public class Deck {
    public final static int NBCARTESDECK = 104; // 2 jeux de 52 cartes sont nécessaires
    public final static int NBCOULEURSCARTES = 4;
    public final static int NBCARTESPARCOULEURS = 14;

    private Cards[] deck;
    private int difficulty;
    private int cartesUtilisees;

    // Constructeur du deck, les cartes dépendent de la difficulté choisie par le joueur
    public Deck(int difficulty) {
        this.difficulty = difficulty; // 0 ou 1 ou 2
        cartesUtilisees = 0;
        deck = new Cards[NBCARTESDECK];

        switch(this.difficulty) {
            case 0 :
                for(int i = 0; i < deck.length; i++)
                    deck[i] = new Cards((i % (NBCARTESPARCOULEURS - 1)) + 1, 0);
                break;
            case 1 :
                for(int i = 0; i < deck.length; i++)
                    if(i<deck.length / 2) // Première moitié du deck sont des piques
                        deck[i] = new Cards((i % (NBCARTESPARCOULEURS - 1)) + 1, 0);
                    else // Deuxième moitité du deck sont des coeurs
                        deck[i] = new Cards((i % (NBCARTESPARCOULEURS - 1)) + 1, 1);
                break;
            case 2 :
                for(int i = 0; i < deck.length; i++)
                    if(i < deck.length / 4) // Premier quart du deck sont des piques
                        deck[i] = new Cards((i % (NBCARTESPARCOULEURS - 1)) + 1, 0);
                    else if(i < deck.length / 2) // Deuxième quart du deck sont des coeurs
                        deck[i] = new Cards((i % (NBCARTESPARCOULEURS - 1)) + 1, 1);
                    else if(i < deck.length / (4.0 / 3)) // Troisième quart du deck sont des carreaux
                        deck[i] = new Cards((i % (NBCARTESPARCOULEURS - 1)) + 1, 2);
                    else // Dernier quart du deck sont des trèfles
                        deck[i] = new Cards((i % (NBCARTESPARCOULEURS - 1)) + 1, 3);
                break;
            default :
                break;
        }
    }

    // Méthode effectuant le mélange du deck
    public void shuffle() {
        for(int i = 0; i < deck.length; i++) {
            Cards carteTemp;

            Random r = new Random();
            int numCarte = r.nextInt(deck.length);

            carteTemp = deck[i];
            deck[i] = deck[numCarte];
            deck[numCarte] = carteTemp;
        }
    }

    // Retourne la dernière carte du deck et l'enlève du deck
    public Cards distribuerCarte() {
        Cards carteTemp = deck[cartesRestantes() - 1];
        deck[cartesRestantes() - 1] = null;

        cartesUtilisees++;

        return carteTemp;
    }

    // Ajoute une carte passée en paramètre au deck
    public void addCards(Cards carteAjouter) {
        deck[cartesRestantes()] = carteAjouter;
        cartesUtilisees--;
    }

    // retourne le nombre de cartes restantes dans le deck
    public int cartesRestantes() {
        return deck.length - cartesUtilisees;
    }

    // Affiche le deck
    public String toString() {
        String str = "";
        for (int i = 0; i<cartesRestantes(); i++) {
            str = str.concat(deck[i] + "\n");
        }
        return str;
    }
}
