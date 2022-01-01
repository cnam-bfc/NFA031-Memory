package nfa031.memory;

import java.io.IOException;

public class Programme {

    static int TERMINAL_LENGTH = 100;
    static int TERMINAL_HEIGHT = 10;

    public static void main(String[] args) {
        String[] mots = lireTexte(".\\src\\nfa031\\memory\\Ressources\\Extrait_texte.txt");
        for (String str : mots) {
            System.out.println(str);
        }
        System.out.println("Taille: " + mots.length);
        effacerConsole();
    }

    // Efface le contenu de la console
    // Source: https://stackoverflow.com/a/32295974
    static void effacerConsole() {
        // On utilise cette boucle pour afficher des lignes vides au cas où le terminal utilisé ne supporterai pas le code suivant cette boucle
        for (int i = 0; i < TERMINAL_HEIGHT; i++) {
            System.out.println();
        }
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    // Retourne un tableau des mots d'un fichier texte
    // Retourne un tableau de 0 valeurs si une exception est levée
    static String[] lireTexte(String cheminDuFichier) {
        try {
            String texte = Lecteur.LireTexte(cheminDuFichier);
            return transformerTexteEnMots(texte);
        } catch (IOException err1) {
            System.out.println("Erreur lors de la lecture du fichier");
            // On retourne un tableau vide puisqu'une exception a été levée
            return new String[0];
        }
    }

    // Retourne un tableau de mots à partir d'une chaine de caractères
    // sans doublons, sans ponctuation, sans espaces et sans lettres seules
    // (exemple pour "c'était", seulement "était" est ajouté dans le tableau)
    static String[] transformerTexteEnMots(String texte) {
        String[] mots = new String[0];
        // On met le texte en minuscules
        texte = texte.toLowerCase();
        // On concatène les caractères les uns à la suite des autre dans cette chaine de caractères
        String mot = "";
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
                mot += c;
            } else if (c == 'æ') {
                mot += "ae";
            } else if (c == 'œ') {
                mot += "oe";
            } else {
                // On ajoute le mot seulement si le mot n'est pas déjà contenu dans le tableau et si ce n'est pas une lettre seule
                // (exemple: le mot "c" ne sera pas ajouté dans le tableau puisque c'est une lettre seule)
                if (mot.length() > 1 && !estContenuDansTableau(mot, mots)) {
                    mots = ajouterUnePlaceDansTableau(mots);
                    mots[mots.length - 1] = mot;
                }
                mot = "";
            }
        }
        return mots;
    }

    // Retourne vrai si la chaine de caractères est contenu dans le tableau
    // Retourne faux si elle n'y est pas
    static boolean estContenuDansTableau(String search, String[] tableau) {
        for (String str : tableau) {
            if (str.equals(search)) {
                return true;
            }
        }
        return false;
    }

    // Retourne un tableau au contenu identique à celui entré en paramètres mais avec un emplacement vide en dernier index
    static String[] ajouterUnePlaceDansTableau(String[] tableau) {
        String[] result = new String[tableau.length + 1];
        for (int i = 0; i < tableau.length; i++) {
            result[i] = tableau[i];
        }
        return result;
    }
}
