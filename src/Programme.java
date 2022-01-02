
import java.io.IOException;

public class Programme {

    public static void main(String[] args) {
        String[] mots = readFile(".\\src\\Extrait_texte.txt");
        for (String str : mots) {
            System.out.println(str);
        }
        System.out.println("Taille: " + mots.length);
        clearConsole();
        String[] test = new String[1];
        test[0] = "kfgjsjhgjkfkjlqsfjhksgdjsfejlk";
        showBoundingBoxWithContent("", test);
        showBoundingBoxWithContent("azerty", test);
    }

    // Affiche chaque élément du tableau sur une ligne indépendante, le tout entouré d'un cadre
    // Source des caractères graphiques: https://en.wikipedia.org/wiki/Box-drawing_character#Box_Drawing
    static void showBoundingBoxWithContent(String title, String... lines) {
        String[] tableauTemp = enlargeTable(lines);
        tableauTemp[tableauTemp.length - 1] = "  " + title + "  ";
        int longueur = getMaximumLength(tableauTemp);
        // Première ligne
        System.out.print("/");
        if (title.equals("")) {
            for (int i = 1; i < longueur - 1; i++) {
                System.out.print("-");
            }
        } else {
            for (int i = 1; i < longueur / 2 - 1; i++) {
                System.out.print("-");
            }
            System.out.print(title);
            for (int i = 1; i < longueur / 2 - 1; i++) {
                System.out.print("-");
            }
        }
        System.out.print("\\");
        System.out.println();
    }

    // Retourne la longueur la plus meximal des chaines de caractères contenu dans le tableau
    static int getMaximumLength(String[] table) {
        int max = 0;
        for (String str : table) {
            if (str.length() > max) {
                max = str.length();
            }
        }
        return max;
    }

    // Efface le contenu de la console
    // Source: https://stackoverflow.com/a/32295974
    static void clearConsole() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    // Retourne un tableau des mots d'un fichier texte
    // Retourne un tableau de 0 valeurs si une exception est levée
    static String[] readFile(String filePath) {
        try {
            String texte = Lecteur.LireTexte(filePath);
            return formatTextToWords(texte);
        } catch (IOException err1) {
            System.out.println("Erreur lors de la lecture du fichier");
            // On retourne un tableau vide puisqu'une exception a été levée
            return new String[0];
        }
    }

    // Retourne un tableau de mots à partir d'une chaine de caractères
    // sans doublons, sans ponctuation, sans espaces et sans lettres seules
    // (exemple pour "c'était", seulement "était" est ajouté dans le tableau)
    static String[] formatTextToWords(String text) {
        String[] mots = new String[0];
        // On met le texte en minuscules
        text = text.toLowerCase();
        // On concatène les caractères les uns à la suite des autre dans cette chaine de caractères
        String mot = "";
        // On parcours le texte caractère par caractère
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
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
                if (mot.length() > 1 && !isIncludedInTable(mot, mots)) {
                    mots = enlargeTable(mots);
                    mots[mots.length - 1] = mot;
                }
                mot = "";
            }
        }
        
        return mots;
    }

    // Retourne vrai si la chaine de caractères est contenu dans le tableau
    // Retourne faux si elle n'y est pas
    static boolean isIncludedInTable(String search, String[] table) {
        for (String str : table) {
            if (str.equals(search)) {
                return true;
            }
        }
        return false;
    }

    // Retourne un tableau au contenu identique à celui entré en paramètres mais avec un emplacement vide en dernier index
    static String[] enlargeTable(String[] table) {
        String[] result = new String[table.length + 1];
        for (int i = 0; i < table.length; i++) {
            result[i] = table[i];
        }
        return result;
    }
}
