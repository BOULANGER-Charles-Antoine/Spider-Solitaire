import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Partie {
    private static final Scanner pause = new Scanner(System.in);

    // Donne le choix au joueur entre démarrer une nouvelle partie, lire les règles ou quitter le jeu
    public void spiderSolitaire() {
        Scanner sc = new Scanner(System.in);
        char choixMenu;

        do {
            System.out.println("~~~~~~~~~~ SPIDER SOLITAIRE ~~~~~~~~~~\n");
            System.out.println("\t1- Nouvelle Partie\n\t2- Regles\n\t3- Quitter\n");
            System.out.print("Que voulez-vous faire ? (Entrez un nombre entre 1 et 3) : ");

            choixMenu = sc.next().charAt(0);
            while (choixMenu != '1' && choixMenu != '2' && choixMenu != '3') {
                System.out.println("Veuillez entrer un chiffre entre 1 et 3 : ");
                choixMenu = sc.next().charAt(0);
            }

            // Choix de la difficulté et lancement d'une partie
            switch(choixMenu) {
                case '1':
                    System.out.println("\n\n\n\n\n~~~~~~~~~~ SPIDER SOLITAIRE ~~~~~~~~~~\n");
                    System.out.println("Choix de la difficulté");
                    System.out.println("\t1- Facile\n\t2- Moyen\n\t3- Difficile\n");
                    System.out.print("Quelle difficulté choississez-vous ? (Entrez un nombre entre 1 et 3) : ");

                    char choixDifficulte = sc.next().charAt(0);
                    while (choixDifficulte != '1' && choixDifficulte != '2' && choixDifficulte != '3') {
                        System.out.println("Veuillez entrer un chiffre entre 1 et 3 : ");
                        choixDifficulte = sc.next().charAt(0);
                    }
                    Deck deck = new Deck(Character.getNumericValue(choixDifficulte) - 1);
                    deck.shuffle();

                    System.out.println("\n\n\n\n\n");
                    partieDeJeu(deck);
                    pause();

                    break;
                case '2':
                    System.out.println("\n\n\n\n\n");
                    regles();
                    pause();

                    break;
                case '3':
                    System.out.println("\nMerci d'avoir joué, au revoir !");
                    pause();

                    break;
                default:
                    break;
            }
        } while(choixMenu != '3');
    }

    public void partieDeJeu (Deck deck) {
        Scanner sc = new Scanner(System.in);

        Table tableDeJeu = new Table(deck);
        ArrayList<Mouvement> registreMove = new ArrayList<Mouvement>();
        Mouvement move;
        System.out.println(tableDeJeu);
        System.out.println("Vous pouvez faire " + tableDeJeu.nbMouvement() + " mouvement(s).");
        char choixTalon = 'o';
        char choixAbandon = 'o';

        do {
            // Choix pour faire un ou plusieurs talons
            if(tableDeJeu.getNbTalon() != 0) {
                do {
                    System.out.print("\nVoulez-vous faire une distribution de talon (O/N) : ");
                    choixTalon = sc.next().charAt(0);
                } while (choixTalon != 'o' && choixTalon != 'O' && choixTalon != 'n' && choixTalon != 'N');

                if (choixTalon == 'o' || choixTalon == 'O') {
                    tableDeJeu.distributionTalon();
                    move = new Mouvement(tableDeJeu, 'A', 'A', 0, null, true);
                    registreMove.add(move);
                    System.out.println("\n\n\n\n\n" + tableDeJeu);

                    do {
                        do {
                            System.out.print("\nVoulez-vous faire un retour en arrière (O/N) : ");
                            choixAbandon = sc.next().charAt(0);
                        } while (choixAbandon != 'o' && choixAbandon != 'O' && choixAbandon != 'n' && choixAbandon != 'N');

                        if (choixAbandon == 'o' || choixAbandon == 'O') {
                            registreMove.get(registreMove.size() - 1).retourEnArriere(registreMove.get(registreMove.size() - 1).isDistributionTalon());//  break;
                            registreMove.remove(registreMove.size() - 1);
                            System.out.println(tableDeJeu);
                            System.out.println("Vous pouvez faire " + tableDeJeu.nbMouvement() + " mouvement");
                        }
                    } while((choixAbandon == 'o' || choixAbandon == 'O') && registreMove.size() != 0);
                }
            }
        } while((choixTalon == 'o' || choixTalon == 'O') && tableDeJeu.getNbTalon() != 0);

        do {
            System.out.print("\nSélectionner la colonne désirée (une lettre entre A et J) : ");
            char colonneDepart = sc.next().charAt(0);

            while ((colonneDepart < 'A' || colonneDepart > 'J') && (colonneDepart < 'a' || colonneDepart > 'j') ||
                    (((colonneDepart >= 'A' && colonneDepart <= 'J') || (colonneDepart >= 'a' && colonneDepart <= 'j')) && tableDeJeu.getColonneTable(Character.toUpperCase(colonneDepart) - 65).getSize()==0)) {
                if(((colonneDepart >= 'A' && colonneDepart <= 'J') || (colonneDepart >= 'a' && colonneDepart <= 'j')) && tableDeJeu.getColonneTable(Character.toUpperCase(colonneDepart) - 65).getSize() == 0)
                    System.out.println("Colonne vide, veuillez en choisir une autre.");
                System.out.println("Veuillez entrer une lettre entre A et J : ");
                colonneDepart = sc.next().charAt(0);
            }
            colonneDepart = Character.toUpperCase(colonneDepart);

            System.out.print("\nSélectionner une ligne ? (Entrez un nombre entre 1 et " + tableDeJeu.getColonneTable(colonneDepart - 65).getSize() + ") : ");
            int ligne = sc.nextInt();
            sc.nextLine();

            while (ligne < 1 || ligne > tableDeJeu.longueurMaxColonne()) {
                System.out.println("Veuillez entrer un chiffre entre 1 et " + tableDeJeu.getColonneTable(colonneDepart - 65).getSize() + " : ");
                ligne = sc.nextInt();
                sc.nextLine();
            }

            System.out.print("\nSélectionner la colonne d'arrivée désirée (une lettre entre A et J) : ");
            char colonneArrivee = sc.next().charAt(0);
            while ((colonneArrivee < 'A' || colonneArrivee > 'J') && (colonneArrivee < 'a' || colonneArrivee > 'j')) {
                System.out.println("Veuillez entrer une lettre entre A et J : ");
                colonneArrivee = sc.next().charAt(0);
            }
            colonneArrivee = Character.toUpperCase(colonneArrivee);

            ArrayList<Cards> selection = tableDeJeu.getColonneTable(colonneDepart - 65).selectionCarte(ligne);
            move = new Mouvement(tableDeJeu, colonneDepart, colonneArrivee, ligne, selection, false);
            registreMove.add(move);
            move.mouvement();

            System.out.println("\n\n\n\n\n" + tableDeJeu);
            System.out.println("Vous pouvez faire " + tableDeJeu.nbMouvement() + " mouvement(s).");

            if(!tableDeJeu.victory()) {
                do {
                    do {
                        System.out.print("\nVoulez-vous faire un retour en arrière (O/N) : ");
                        choixAbandon = sc.next().charAt(0);
                    } while (choixAbandon != 'o' && choixAbandon != 'O' && choixAbandon != 'n' && choixAbandon != 'N');

                    if (choixAbandon == 'o' || choixAbandon == 'O') {
                        registreMove.get(registreMove.size() - 1).retourEnArriere(registreMove.get(registreMove.size() - 1).isDistributionTalon());
                        registreMove.remove(registreMove.size() - 1);
                        System.out.println(tableDeJeu);
                    }
                } while((choixAbandon == 'o' || choixAbandon == 'O') && registreMove.size() != 0);

                if(tableDeJeu.getNbTalon() != 0) {
                    do {
                        do {
                            System.out.print("Voulez-vous faire une distribution de talon (O/N) : ");
                            choixTalon = sc.next().charAt(0);
                        } while (choixTalon != 'o' && choixTalon != 'O' && choixTalon != 'n' && choixTalon != 'N');

                        if (choixTalon == 'o' || choixTalon == 'O') {
                            tableDeJeu.distributionTalon();
                            move = new Mouvement(tableDeJeu, 'A', 'A', 0, null, true);
                            registreMove.add(move);

                            System.out.println("\n\n\n\n\n" + tableDeJeu);
                            System.out.println("Vous pouvez faire " + tableDeJeu.nbMouvement() + " mouvement");

                            do {
                                do {
                                    System.out.print("Voulez-vous faire un retour en arrière (O/N) : ");
                                    choixAbandon = sc.next().charAt(0);
                                } while (choixAbandon != 'o' && choixAbandon != 'O' && choixAbandon != 'n' && choixAbandon != 'N');

                                if (choixAbandon == 'o' || choixAbandon == 'O') {
                                    registreMove.get(registreMove.size() - 1).retourEnArriere(registreMove.get(registreMove.size() - 1).isDistributionTalon());
                                    registreMove.remove(registreMove.size() - 1);
                                    System.out.println(tableDeJeu);
                                }
                            } while((choixAbandon == 'o' || choixAbandon == 'O') && registreMove.size() != 0);
                        }
                    } while ((choixTalon == 'o' || choixTalon == 'O') && tableDeJeu.getNbTalon() != 0);
                }
            }
        } while(!tableDeJeu.victory());

        if(tableDeJeu.victory())
            System.out.println("\nBravo vous avez gagné avec un score de " + tableDeJeu.getScore() + "\n");
        else
            System.out.println("\nDommage vous avez perdu avec un score de " + tableDeJeu.getScore() + "\n");
    }

    // Affiche les règles
    public void regles () {
        String rules = "~~~~~~~~~~ SPIDER SOLITAIRE ~~~~~~~~~~\n\n";
        rules += "Ce jeu utilise 2 paquets de 52 cartes et consiste à faire toutes les suites de couleur (du Roi à l'As).\n\n";
        rules += "Table de jeu :\n\t- Au début, il y a 4 colonnes de 6 cartes puis 6 colonnes de 4 cartes, seule la plus basse est face découverte.";
        rules += "\n\t- 5 tirages de talon sont disponible, un tirage de talon rajoutera une carte face visible en bas de chaque colonne.";
        rules += "\n\nNiveaux de difficulté :\n\t- Facile : 1 couleur\n\t- Moyen : 2 couleurs\n\t- Difficile : 4 couleurs";
        rules += "\n\nDéroulement du jeu : \n\t- Le joueur peut uniquement faire des suite (mettre un 5 sur un 6).";
        rules += "\n\t- Le joueur peut bouger uniquement un ensemble de carte de la même couleur.";
        rules += "\n\t- Le joueur peut placer des cartes d'une couleur sur une autre";
        rules += "\n\t- Le but est d'enlever toutes les cartes de la table en faisant des suites du Roi à l'As de la même couleur, ce qui enlève des cartes une fois la suite faites.";
        rules += "\n\t- Le joueur peut faire des retours en arrières.";
        rules += "\n\t- Le score de partie baisse de 1 à chaque mouvement et augmente de 100 à chaque suite finie.";
        rules += "\n\t- La partie est perdue si votre score est en dessous de 0 ou si le joueur ne peut plus faire de mouvement.";
        rules += "\n\nBon jeu à tous !\n";

        System.out.println(rules);
    }

    // Méthode pause
    public static void pause() {
        System.out.println("Appuyer sur entrer pour continuer...");
        pause.nextLine();
    }
}
