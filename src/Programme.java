
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Programme {

    public static void main(String[] args) {
        startGame();
    }

    // Démarre le jeu
    static void startGame() {
        showLoadingScreen();
        String[] stats = initStats();
        showMainMenu();
    }

    static String[] initStats() {
        return new String[0];
    }

    // Affiche un faux écran de chargement
    static void showLoadingScreen() {
        for (int i = 0; i < 24; i++) {
            clearConsole();
            String progressBar = "[";
            for (int j = 0; j < i; j++) {
                progressBar += "=";
            }
            progressBar += "*";
            for (int j = 24; j > i; j--) {
                progressBar += " ";
            }
            progressBar += "]";
            showBoundingBoxWithContent("", "Jeux de Mémoire", "", "", "", "Chargement...", "", progressBar);
            sleep(generateRandomInt(100, 200));
        }
    }

    // Affiche le menu principal
    static void showMainMenu() {
        showMenu("Menu principal", "", "Jouer", "Statistiques", "Quitter");
    }

    // Affiche un menu à l'utilisateur avec un titre, une sous-titre et les choises passés en paramètre
    // Retourne le numéro du choix de l'utilisateur
    static int showMenu(String title, String subTitle, String... choices) {
        try {
            clearConsole();
            String[] lines = new String[0];
            if (!subTitle.equals("")) {
                lines = enlargeTable(lines);
                lines[lines.length - 1] = subTitle;
            }
            for (int i = 0; i < choices.length; i++) {
                String choice = choices[i];
                lines = enlargeTable(lines);
                lines[lines.length - 1] = i + 1 + ". " + choice;
            }
            showBoundingBoxWithContent(title, lines);
            System.out.print("Choix > ");
            return EConsole.lireInt();
        } catch (IOException | NumberFormatException ex) {
            return 0;
        }
    }

    // Affiche chaque élément du tableau sur une ligne indépendante, le tout entouré d'un cadre et si indiqué un titre
    static void showBoundingBoxWithContent(String title, String... lines) {
        showBoundingBoxWithContent(title, 100, lines);
    }

    // Affiche chaque élément du tableau sur une ligne indépendante, le tout entouré d'un cadre et si indiqué un titre
    static void showBoundingBoxWithContent(String title, int minLength, String... lines) {
        showBoundingBoxWithContent(title, minLength, 15, lines);
    }

    // Affiche chaque élément du tableau sur une ligne indépendante, le tout entouré d'un cadre et si indiqué un titre
    static void showBoundingBoxWithContent(String title, int minLength, int minHeight, String... lines) {
        String[] tableauTemp = enlargeTable(lines);
        // " " Pour avoir au moins un tiret (spacer) de chaque côté du titre si il est le plus grand par rapport aux Strings de lines
        // car les spacer ne sont pas forcément retourné dans la méthode getCenteredText() qui est utilisé plus tard dans cette méthode
        tableauTemp[tableauTemp.length - 1] = " " + title + " ";
        int maxLength = getMaximumLength(tableauTemp) + 2;
        if (maxLength > minLength) {
            minLength = maxLength;
        }
        int maxHeight = lines.length;
        if (maxHeight > minHeight) {
            minHeight = maxHeight;
        }

        // Première ligne
        System.out.print("/");
        char separator = '-';
        if (!title.equals("")) {
            separator = ' ';
        }
        System.out.print(getCenteredText(title, '-', separator, minLength));
        System.out.println("\\");

        // Bourage avec des lignes vide pour qu'il y ai au moins minHeight lignes dans la boite
        int halfMinHeight = minHeight / 2 - lines.length / 2;
        for (int i = 0; i < halfMinHeight; i++) {
            System.out.print("|");
            System.out.print(getCenteredText("", ' ', ' ', minLength));
            System.out.println("|");
        }

        // Lignes au milieu
        for (String str : lines) {
            System.out.print("|");
            System.out.print(getCenteredText(str, ' ', ' ', minLength));
            System.out.println("|");
        }

        // Bourage avec des lignes vide + une si la division de halfMinHeight à un reste
        for (int i = 0; i < halfMinHeight + minHeight % 2 - lines.length % 2; i++) {
            System.out.print("|");
            System.out.print(getCenteredText("", ' ', ' ', minLength));
            System.out.println("|");
        }

        // Dernière ligne
        System.out.print("\\");
        System.out.print(getCenteredText("", '-', minLength));
        System.out.println("/");
    }

    // Retourne la chaine de caractères passé en paramètre centré avec des caractères passé en paramètre
    // Au moins un spacer est retourné de chaque côté du text
    static String getCenteredText(String text, char spacer, int length) {
        return getCenteredText(text, spacer, spacer, length);
    }

    // Retourne la chaine de caractères passé en paramètre centré avec des caractères passé en paramètre
    // Les spacer ne sont pas forcément retournés mais separator est retourné 1 fois de chaque côté du text
    static String getCenteredText(String text, char spacer, char separator, int length) {
        String result = "";
        // Longueur du text + 2 pour les 2 separator
        int minimumLength = text.length() + 2;
        if (minimumLength > length) {
            length = minimumLength;
        }

        // Moitié de la longeur disponible - Moitié de la longueur du texte (text + les 2 separator) obligatoire à retourner
        int halfLength = length / 2 - (text.length() + 2) / 2;
        for (int i = 0; i < halfLength; i++) {
            result += spacer;
        }

        result += separator;
        result += text;
        result += separator;

        // En plus des spacer à imprimer ou pas on en imprime un de plus si les 2 divisions au dessus ont un reste
        for (int i = 0; i < halfLength + length % 2 - (text.length() + 2) % 2; i++) {
            result += spacer;
        }

        return result;
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

    // Cherche le fichier dans plusieurs dossiers et le lit si il est trouvé
    // Retourne un tableau des mots du fichier
    // Retourne un tableau de 0 valeurs si aucun fichiers n'est trouvé
    static String[] readText(String fileName) {
        String[] folders = {".\\", ".\\src\\", ".\\textes\\"};

        for (String folder : folders) {
            if (fileExists(folder + fileName + ".txt")) {
                return readFile(folder + fileName + ".txt");
            }
        }

        return new String[0];
    }

    // Retourne vrai si le fichier existe
    // Retourne faux si il n'existe pas
    static boolean fileExists(String filePath) {
        try {
            Lecteur.LireTexte(filePath);
            return true;
        } catch (FileNotFoundException ex) {
            return false;
        } catch (IOException ex) {
            return false;
        }
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
    @SuppressWarnings("ManualArrayToCollectionCopy")
    static String[] enlargeTable(String[] table) {
        String[] result = new String[table.length + 1];

        for (int i = 0; i < table.length; i++) {
            result[i] = table[i];
        }

        return result;
    }

    // Retourne un entier pseudo-aléatoirement entre min (inclus) et max (inclus)
    // Source: https://stackoverflow.com/a/363732
    static int generateRandomInt(int min, int max) {
        return min + (int) (Math.random() * (max - min + 1));
    }

    // Met le programme en pause pendant x ms
    static void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException ex) {
            Logger.getLogger(Programme.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
