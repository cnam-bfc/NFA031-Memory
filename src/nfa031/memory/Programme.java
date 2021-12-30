package nfa031.memory;

import java.io.IOException;

public class Programme {
    public static void main(String[] args) {
        for (String str : lireTexte(".\\src\\nfa031\\memory\\Ressources\\Extrait_texte.txt")) {
            System.out.println(str);
        }
    }

    // Méthode retournant tout les mots d'un fichier texte sans doublons, sans
    // ponctuation et sans espaces
    // Retourne un tableau de 0 valeurs si une exception est levée
    static String[] lireTexte(String cheminDuFichier) {
        try {
            String[] mots = new String[0];
            String texte = Lecteur.LireTexte(cheminDuFichier).toLowerCase();
            String temp = "";
            System.out.println(texte);
            for (int i = 0; i < texte.length(); i++) {
                char c = texte.charAt(i);
                switch (c) {
                case 'é', 'è', 'ê' -> c = 'e';
                case 'à', 'â' -> c = 'a';
                }
                if (c >= 'a' && c <= 'z')
                    temp += c;
                else {
                    System.out.println("Debug: " + c);
                    if (!temp.equals("")) {
                        mots = ajouterPlaceTableau(mots);
                        mots[mots.length - 1] = temp;
                        temp = "";
                    }
                }
            }
            return mots;
        } catch (IOException err1) {
            System.out.println("Erreur lors de la lecture du fichier");
            // TODO Enlever commantaire
            // return new String[0];
        }
        // TODO Enlever ça
        return new String[0];
    }

    static String[] ajouterPlaceTableau(String[] tableau) {
        String[] result = new String[tableau.length + 1];
        for (int i = 0; i < tableau.length; i++)
            result[i] = tableau[i];
        return result;
    }
}