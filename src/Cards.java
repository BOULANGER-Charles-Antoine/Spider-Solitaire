import java.util.Objects;

public class Cards {
    // Code pour les 4 couleurs
    public static final int PIQUES = 0,
                            COEURS = 1,
                            TREFLES = 2,
                            CARREAUX = 3;
    // Code pour les honneurs
    public static final int AS = 1,
                            VALET = 11,
                            DAME = 12,
                            ROI = 13;
    private int valeur,
                couleur;
    private boolean faceDecouverte;

    // Initialise une carte avec les paramètres passés, chaque carte est au début face non découverte
    public Cards(int valeur, int couleur) {
        this.valeur = valeur;
        this.couleur = couleur;
        faceDecouverte = false;
    }

    public int getValeur() { return valeur; }

    public int getCouleur() { return couleur; }

    public void setValeur(int valeur) {
        this.valeur = valeur;
    }

    public void setCouleur(int couleur) {
        this.couleur = couleur;
    }

    public boolean getFaceDecouverte() {
        return faceDecouverte;
    }

    public void setFaceDecouverte(boolean faceDecouverte) {
        this.faceDecouverte = faceDecouverte;
    }

    // Accesseur convertissant la valeur de la carte en chaine de caractere
    public String getValeurCommeChaine() {
        switch(getValeur()) {
            case 1 :
                return "AS";
            case 2 : case 3 : case 4 : case 5 : case 6 : case 7 : case 8 : case 9 : case 10 :
                return "" + getValeur();
            case 11 :
                return "VALET";
            case 12 :
                return "DAME";
            case 13 :
                return "ROI";
            default :
                return "??";
        }
    }

    // Accesseur convertissant la couleur de la carte en chaine de caractere
    public String getCouleurCommeChaine() {
        switch(getCouleur()) {
            case 0 :
                return "PIQUES";
            case 1 :
                return "COEURS";
            case 2 :
                return "TREFLES";
            case 3 :
                return "CARREAUX";
            default :
                return "??";
        }
    }

    // Retourne la taille de l'affichage de la carte
    public int lengthString() {
        return toString().length();
    }

    // Affichage de la carte
    public String toString () {
        return getValeurCommeChaine() + " de " + getCouleurCommeChaine();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cards cards = (Cards) o;
        return valeur == cards.valeur &&
                couleur == cards.couleur &&
                faceDecouverte == cards.faceDecouverte;
    }

    @Override
    public int hashCode() {
        return Objects.hash(valeur, couleur, faceDecouverte);
    }
}
