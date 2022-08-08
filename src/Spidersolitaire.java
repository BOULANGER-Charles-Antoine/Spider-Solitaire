/*
Nom du Programme : SpiderSolitaire
But : Jouer au jeu du SpiderSolitaire
Auteur : Boulanger Charles Antoine
Date : 17/06/2020
Version : V0.0
*/

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Spidersolitaire {
    public static void main(String[] args){
        Partie game = new Partie();
        game.spiderSolitaire();
    }
}
