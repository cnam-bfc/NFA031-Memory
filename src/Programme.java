
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Programme {

    static final String MENU_TOP_LEFT = "/";
    static final String MENU_TOP_RIGHT = "\\";
    static final String MENU_BOTTOM_LEFT = "\\";
    static final String MENU_BOTTOM_RIGHT = "/";
    static final String MENU_VERTICAL = "|";
    static final String MENU_HORIZONTAL = "-";

    public static void main(String[] args) {
        startGame();
    }

    // Démarre le jeu
    static void startGame() {
        showLoadingScreen();
        showMainMenu();
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
            showBoundingBoxWithContent("", "", "      Jeux de Mémoire      ", "", progressBar, "");
            sleep(generateRandomInt(0, 1000));
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
        String[] tableauTemp = enlargeTable(lines);
        tableauTemp[tableauTemp.length - 1] = " " + title + " ";
        int longueur = getMaximumLength(tableauTemp);
        // Première ligne
        System.out.print(MENU_TOP_LEFT);
        if (title.equals("")) {
            for (int i = 0; i < longueur + 2; i++) {
                System.out.print(MENU_HORIZONTAL);
            }
        } else {
            int longueurMoitie = (longueur + 2) / 2 - (title.length() + 2) / 2;
            for (int i = 0; i < longueurMoitie + (longueur + 2) % 2 - (title.length() + 2) % 2; i++) {
                System.out.print(MENU_HORIZONTAL);
            }
            System.out.print(" ");
            System.out.print(title);
            System.out.print(" ");
            for (int i = 0; i < longueurMoitie; i++) {
                System.out.print(MENU_HORIZONTAL);
            }
        }
        System.out.println(MENU_TOP_RIGHT);
        // Lignes au milieu
        for (String str : lines) {
            System.out.print(MENU_VERTICAL);
            System.out.print(" ");
            System.out.print(str);
            for (int i = 0; i < longueur - str.length(); i++) {
                System.out.print(" ");
            }
            System.out.print(" ");
            System.out.println(MENU_VERTICAL);
        }
        // Dernière ligne
        System.out.print(MENU_BOTTOM_LEFT);
        for (int i = 0; i < longueur + 2; i++) {
            System.out.print(MENU_HORIZONTAL);
        }
        System.out.println(MENU_BOTTOM_RIGHT);
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

    // Retourne un entier pseudo-aléatoirement entre min et max
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
