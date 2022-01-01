package nfa031.memory;

import java.io.IOException;

public class Programme {

    public static void main(String[] args) {
        for (String str : lireTexte(".\\src\\nfa031\\memory\\Ressources\\Extrait_texte.txt")) {
            System.out.println(str);
        }
    }

    // Retourne un tableau des mots d'un fichier texte sans doublons, sans
    // ponctuation, sans espaces et sans lettres seules 
    // (exemple pour "c'était", seulement "était" est ajouté dans le tableau)
    // Retourne un tableau de 0 valeurs si une exception est levée
    static String[] lireTexte(String cheminDuFichier) {
        try {
            String[] mots = new String[0];
            // On lit le texte et on le met directement en minuscules
            String texte = Lecteur.LireTexte(cheminDuFichier).toLowerCase();
            // On concatène les caractères les uns à la suite des autre dans cette chaine de caractères
            String temp = "";
            // On parcours le texte caractère par caractère
            for (int i = 0; i < texte.length(); i++) {
                char c = texte.charAt(i);
                // On enlève les accents des lettres et on concatène le caractère seulement si c'est une lettre
                // On traite aussi les 2 caractères spéciaux "æ" et "œ"
                // Source: https://fr.wikipedia.org/wiki/Diacritiques_utilis%C3%A9s_en_fran%C3%A7ais#:~:text=%C2%AB%20D%C3%A8s%20lors%20les%20voyelles%20et,%2D%20%C3%BC%20%2D%20%C3%BF%20%2D%20%C3%A7.
                switch (c) {
                    case 'à', 'â', 'ä' ->
                        c = 'a';
                    case 'é', 'è', 'ê', 'ë' ->
                        c = 'e';
                    case 'î', 'ï' ->
                        c = 'i';
                    case 'ô', 'ö' ->
                        c = 'o';
                    case 'ù', 'û', 'ü' ->
                        c = 'u';
                    case 'ÿ' ->
                        c = 'y';
                    case 'ç' ->
                        c = 'c';
                }
                if (c >= 'a' && c <= 'z') {
                    temp += c;
                } else if (c == 'æ') {
                    temp += "ae";
                } else if (c == 'œ') {
                    temp += "oe";
                } else {
                    // On ajoute le mot seulement si ce n'est pas une lettre seule
                    // (exemple: le mot "c" ne sera pas ajouté dans le tableau puisque c'est une lettre seule)
                    if (temp.length() > 1) {
                        mots = ajouterPlaceTableau(mots);
                        mots[mots.length - 1] = temp;
                    }
                    temp = "";
                }
            }
            return mots;
        } catch (IOException err1) {
            System.out.println("Erreur lors de la lecture du fichier");
            // On retourne un tableau vide puisqu'une exception a été levée
            return new String[0];
        }
    }

    static String[] ajouterPlaceTableau(String[] tableau) {
        String[] result = new String[tableau.length + 1];
        for (int i = 0; i < tableau.length; i++) {
            result[i] = tableau[i];
        }
        return result;
    }
}
