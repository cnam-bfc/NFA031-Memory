
import java.io.FileNotFoundException;
import java.io.IOException;

public class Programme {

    static final String GAME_NAME = "Jeux de Mémoire";
    static final int TERMINAL_MINLENGTH = 100;
    static final int TERMINAL_MINHEIGHT = 15;
    static final int TERMINAL_HEIGHT = 15;
    static final String[] GAMES_NAMES = {"Série de mots", "Série de nombres", "Liste de paires de mots"};

    public static void main(String[] args) {
        startGame();
    }

    // TODO Supprimer cette méthode à la fin du développement
    static void devMessage(String feature) {
        showMessage("", "En développement...");
    }

    // Démarre le jeu
    static void startGame() {
        // TODO Augmenter cette valeur
        showLoadingScreen(100);
        String[][] stats = initStats();
        showMainMenu(stats);
    }

    static String[][] initStats() {
        String[][] stats = new String[4][];
        // Numéro du jeu de la partie
        stats[0] = new String[0];
        // Difficulté de la partie
        stats[1] = new String[0];
        // Score de la partie
        stats[2] = new String[0];
        // Nom du joueur
        stats[3] = new String[0];
        return stats;
    }

    static void addStat(String[][] stats, String gameName, String difficulty, String score, String playerName) {
        stats[0] = addToTable(stats[0], gameName);
        stats[1] = addToTable(stats[1], difficulty);
        stats[2] = addToTable(stats[2], score);
        stats[3] = addToTable(stats[3], playerName);
    }

    // Affiche un faux écran de chargement, plus speed est proche de 0 plus le chargement est rapide
    static void showLoadingScreen(int speed) {
        if (speed <= 0) {
            showErrorMessage("Écran de chargement", "-> Speed doit être supérieur à 0");
            return;
        }
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
            showBoundingBoxWithContent("", GAME_NAME, "", "", "", "Chargement...", "", progressBar);
            sleep(generateRandomInt(0, speed));
        }
    }

    // Affiche le menu principal
    static void showMainMenu(String[][] stats) {
        int menuCode;
        do {
            menuCode = showMenu("Menu principal", "", "Jouer", "Statistiques", "Paramètres", "Quitter");
            switch (menuCode) {
                case 1 ->
                    showGameMenu(stats);
                case 2 -> {
                    int statsMenuCode;
                    do {
                        statsMenuCode = showMenu("Statistiques", "De quel jeu voulez-vous voir les statistiques?", GAMES_NAMES[0], GAMES_NAMES[1], GAMES_NAMES[2], "Retour");
                        if (statsMenuCode != 4) {
                            showStatsMenu(stats, GAMES_NAMES[statsMenuCode - 1]);
                        }
                    } while (statsMenuCode != 4);
                }
                case 3 ->
                    showSettingsMenu(stats);
                case 4 ->
                    showQuitMenu(stats);
            }
        } while (menuCode != 4);
    }

    // Affiche le menu des statistiques quand on quitte le programme
    static void showQuitMenu(String[][] stats) {
        clearConsole();
        showBoundingBoxWithContent(GAME_NAME, "À bientôt...");
    }

    // Affiche le menu des paramètres
    static void showSettingsMenu(String[][] stats) {
        devMessage("Paramètres");
    }

    // Affiche le menu des statistiques d'un jeu en particulié
    static void showStatsMenu(String[][] stats, String gameID) {
        String[] message = new String[0];
        int gameNameLength = getMaximumLength(addToTable(stats[0], "  Jeux  "));
        int difficultyLength = getMaximumLength(addToTable(stats[1], "  Difficulté  "));
        int scoreLength = getMaximumLength(addToTable(stats[2], "  Score  "));
        int playerNameLength = getMaximumLength(addToTable(stats[3], "  Nom du joueur  "));
        message = addToTable(message, centerText("Jeux", '-', ' ', gameNameLength)
                + centerText("Difficulté", '-', ' ', difficultyLength)
                + centerText("Score", '-', ' ', scoreLength)
                + centerText("Nom du joueur", '-', ' ', playerNameLength));
        for (int i = 0; i < stats[0].length; i++) {
            if (stats[0][i].equals(gameID)) {
                message = addToTable(message, centerText(stats[0][i], ' ', ' ', gameNameLength)
                        + centerText(stats[1][i], ' ', ' ', difficultyLength)
                        + centerText(stats[2][i], ' ', ' ', scoreLength)
                        + centerText(stats[3][i], ' ', ' ', playerNameLength));
            }
        }
        showMessage("Statistiques", message);
    }

    // Affiche le menu de sélection des jeux
    static void showGameMenu(String[][] stats) {
        boolean showAgain = true;
        while (showAgain) {
            int menuCode = showMenu("Choix du jeu", "", GAMES_NAMES[0], GAMES_NAMES[1], GAMES_NAMES[2], "Retour");
            switch (menuCode) {
                case 1 ->
                    showAgain = !launchSerieDeMotsGame(stats);
                case 2 ->
                    showAgain = !launchSerieDeNombresGame(stats);
                case 3 ->
                    showAgain = !launchPairesDeMotsGame(stats);
                case 4 ->
                    showAgain = false;
            }
        }
    }

    // Lance le jeu de série de mots
    // Retourne vrai si tout s'est bien passé
    static boolean launchSerieDeMotsGame(String[][] stats) {
        String gameName = GAMES_NAMES[0];
        String difficulty = askDifficulty();
        if (difficulty.equals("")) {
            return false;
        }
        String[] mots = readText("Extrait_texte");
        boolean replay = true;
        while (replay) {

            addStat(stats, gameName, difficulty, "0", askString("Quel est votre pseudo?"));
            showStatsMenu(stats, gameName);
            replay = askReplay();
            if (replay && !askReplayWithSameData()) {
                mots = readText("Extrait_texte");
            }
        }
        return true;
    }

    // Lance le jeu de série de nombres
    // Retourne vrai si tout s'est bien passé
    static boolean launchSerieDeNombresGame(String[][] stats) {
        devMessage("Série de nombres");
        return false;
    }

    // Lance le jeu de paires de mots
    // Retourne vrai si tout s'est bien passé
    static boolean launchPairesDeMotsGame(String[][] stats) {
        devMessage("Paires de mots");
        return false;
    }

    static boolean askReplay() {
        int menuCode = showMenu("Rejouer", "Voulez-vous rejouer au même jeu?", "Oui", "Non");
        return menuCode == 1;
    }

    static boolean askReplayWithSameData() {
        int menuCode = showMenu("Rejouer", "Voulez-vous rejouer avec les mêmes données?", "Oui", "Non");
        return menuCode == 1;
    }

    // Retourne la difficulté demandé au joueur
    static String askDifficulty() {
        int menuCode = showMenu("Difficulté", "", "Facile", "Normal", "Difficile", "Personnalisé", "Retour");
        switch (menuCode) {
            case 1 -> {
                // Facile
                return convertDifficultyToString(3, 2, 5);
            }
            case 2 -> {
                // Normal
                return convertDifficultyToString(5, 3, 6);
            }
            case 3 -> {
                // Difficile
                return convertDifficultyToString(7, 4, 10);
            }
            case 4 -> {
                // Personnalisé
                int nbTermes = askInteger("Saisissez le nombre de termes désiré", 1, 100);
                int minLength = askInteger("Saisissez le minimum de caractères d'un terme", 1, 10);
                int maxLength = askInteger("Saisissez le maximum de caractères d'un terme", minLength, 30);
                return convertDifficultyToString(nbTermes, minLength, maxLength);
            }
            default -> {
                // Retour
                return "";
            }
        }
    }

    // Converti la difficulté en String
    static String convertDifficultyToString(int nbTermes, int minLength, int maxLength) {
        return nbTermes + "/" + minLength + "/" + maxLength;
    }

    // Retourne le nombre de termes de la difficulté
    static int getNbTermesDifficulty(String difficulty) {
        return Integer.parseInt(difficulty.split("/")[0]);
    }

    // Retourne le minimum de la longueur des termes de la difficulté
    static int getMinLengthDifficulty(String difficulty) {
        return Integer.parseInt(difficulty.split("/")[1]);
    }

    // Retourne le maximum de la longueur des termes de la difficulté
    static int getMaxLengthDifficulty(String difficulty) {
        return Integer.parseInt(difficulty.split("/")[2]);
    }

    // Affiche un menu à l'utilisateur avec un titre, un sous-titre et les choix passés en paramètre
    // Retourne le numéro du choix de l'utilisateur
    static int showMenu(String title, String subTitle, String... choices) {
        int item = 0;
        try {
            clearConsole();
            String[] lines = new String[0];
            if (!subTitle.equals("")) {
                lines = addToTable(lines, subTitle);
            }
            for (int i = 0; i < choices.length; i++) {
                String choice = choices[i];
                lines = addToTable(lines, i + 1 + ". " + choice);
            }
            showBoundingBoxWithContent(title, lines);
            System.out.print("Choix > ");
            item = EConsole.lireInt();
        } catch (IOException | NumberFormatException ex) {

        }
        if (item < 1 || item > choices.length) {
            showWarningMessage("Veuillez saisir un entier entre 1 et " + choices.length);
            item = showMenu(title, subTitle, choices);
        }
        return item;
    }

    // Retourne la chaine de caractères saisi par l'utilisateur
    static String askString(String question) {
        try {
            clearConsole();
            showBoundingBoxWithContent("Demande de chaine de caractères", question);
            System.out.print("Saisie > ");
            return EConsole.lireString();
        } catch (IOException ex) {
            showErrorMessage("Demande de chaine de caractères", "-> Impossible de lire la chaine de caractères, veuillez réessayer");
            return askString(question);
        }
    }

    // Retourne l'entier saisi par l'utilisateur
    // L'entier saisi doit être compris entre min (inclus) et max (inclus)
    static int askInteger(String question, int min, int max) {
        int integer;
        do {
            integer = askInteger(question);
            if (integer < min || integer > max) {
                showWarningMessage("Demande d'entier", "-> Veuillez saisir un entier entre " + min + " et " + max);
            }
        } while (integer < min || integer > max);
        return integer;
    }

    // Retourne l'entier saisi par l'utilisateur
    static int askInteger(String question) {
        try {
            clearConsole();
            showBoundingBoxWithContent("Demande d'entier", question);
            System.out.print("Saisie > ");
            return EConsole.lireInt();
        } catch (NumberFormatException ex) {
            showWarningMessage("Demande d'entier", "-> Veuillez saisir un entier");
            return askInteger(question);
        } catch (IOException ex) {
            showErrorMessage("Demande d'entier", "-> Impossible de lire l'entier, veuillez réessayer");
            return askInteger(question);
        }
    }

    // Affiche chaque élément du tableau sur une ligne indépendante, le tout entouré d'un cadre et si indiqué un titre
    static void showBoundingBoxWithContent(String title, String... lines) {
        // On défini la longueur par défaut si aucune n'est spécifié
        showBoundingBoxWithContent(title, TERMINAL_MINLENGTH, lines);
    }

    // Affiche chaque élément du tableau sur une ligne indépendante, le tout entouré d'un cadre et si indiqué un titre
    static void showBoundingBoxWithContent(String title, int minLength, String... lines) {
        // On défini la hauteur par défaut si aucune n'est spécifié
        showBoundingBoxWithContent(title, minLength, TERMINAL_MINHEIGHT, lines);
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
        System.out.print(centerText(title, '-', separator, minLength));
        System.out.println("\\");

        // Bourage avec des lignes vide pour qu'il y ai au moins minHeight lignes dans la boite
        int paddingHeight = minHeight - lines.length;
        for (int i = 0; i < paddingHeight / 2; i++) {
            System.out.print("|");
            System.out.print(centerText("", ' ', ' ', minLength));
            System.out.println("|");
        }

        // Lignes au milieu
        for (String str : lines) {
            System.out.print("|");
            System.out.print(centerText(str, ' ', ' ', minLength));
            System.out.println("|");
        }

        // Bourage avec des lignes vide + une si la division de paddingHeight à un reste
        for (int i = 0; i < paddingHeight / 2 + paddingHeight % 2; i++) {
            System.out.print("|");
            System.out.print(centerText("", ' ', ' ', minLength));
            System.out.println("|");
        }

        // Dernière ligne
        System.out.print("\\");
        System.out.print(centerText("", '-', minLength));
        System.out.println("/");
    }

    // Met le programme en pause pendant x ms
    static void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException ex) {
            showErrorMessage("Attendre", "-> Impossible d'attendre " + millis + "ms");
        }
    }

    // Affiche un message d'avertissement à l'utilisateur
    static void showWarningMessage(String... message) {
        showMessage("ATTENTION", message);
    }

    // Affiche un message d'erreur à l'utilisateur
    static void showErrorMessage(String... message) {
        showMessage("ERREUR", message);
    }

    // Affiche un message à l'utilisateur
    static void showMessage(String title, String... message) {
        showMessage(title, TERMINAL_MINLENGTH, message);
    }

    // Affiche un message à l'utilisateur
    static void showMessage(String title, int minLength, String... message) {
        showMessage(title, minLength, TERMINAL_MINHEIGHT, message);
    }

    // Affiche un message à l'utilisateur
    static void showMessage(String title, int minLength, int minHeight, String... message) {
        try {
            clearConsole();
            message = addToTable(message, "");
            message = addToTable(message, "Appuyez sur Entrée pour continuer...");
            showBoundingBoxWithContent(title, minLength, minHeight, message);
            EConsole.lireString();
        } catch (IOException ex) {
            System.out.println("ERROR");
        }
    }

    // Retourne la chaine de caractères passé en paramètre centré avec des caractères passé en paramètre
    // Au moins un spacer est retourné de chaque côté du text
    static String centerText(String text, char spacer, int length) {
        return centerText(text, spacer, spacer, length);
    }

    // Retourne la chaine de caractères passé en paramètre centré avec des caractères passé en paramètre
    // Les spacer ne sont pas forcément retournés mais separator est retourné 1 fois de chaque côté du text
    static String centerText(String text, char spacer, char separator, int length) {
        String result = "";
        // Longueur du text + 2 pour les 2 separator
        int minimumLength = text.length() + 2;
        if (minimumLength > length) {
            length = minimumLength;
        }

        // Bourage avec des spacer pour qu'il y ai au moins length caractères dans la ligne
        // Longeur à retourner - La longueur du texte (text + les 2 separator) obligatoire à retourner
        int paddingLength = length - text.length() - 2;
        for (int i = 0; i < paddingLength / 2; i++) {
            result += spacer;
        }

        result += separator;
        result += text;
        result += separator;

        // Bourage avec des spacer pour qu'il y ai au moins length caractères dans la ligne + un si la division de paddingLength à un reste
        for (int i = 0; i < paddingLength / 2 + paddingLength % 2; i++) {
            result += spacer;
        }

        return result;
    }

    // Efface le contenu de la console
    // Source: https://stackoverflow.com/a/32295974
    static void clearConsole() {
        for (int i = 0; i < TERMINAL_HEIGHT; i++) {
            System.out.println();
        }
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

    // Retourne un tableau des mots d'un fichier texte
    // Retourne un tableau de 0 valeurs si une exception est levée
    static String[] readFile(String filePath) {
        try {
            String texte = Lecteur.LireTexte(filePath);
            return convertTextToWords(texte);
        } catch (IOException err1) {
            showErrorMessage("Erreur lors de la lecture du fichier,", "-> Fichier: " + filePath);
            // On retourne un tableau vide puisqu'une exception a été levée
            return new String[0];
        }
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

    // Retourne un tableau de mots à partir d'une chaine de caractères
    // sans doublons, sans ponctuation, sans espaces et sans lettres seules
    // (exemple pour "c'était", seulement "était" est ajouté dans le tableau)
    static String[] convertTextToWords(String text) {
        String[] mots = new String[0];
        // On met le texte en minuscules
        text = text.toLowerCase();
        // On concatène les caractères les uns à la suite des autre dans cette chaine de caractères
        String mot = "";

        // On parcours le texte caractère par caractère
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            // On enlève les accents des lettres
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

            // On concatène le caractère seulement si c'est une lettre
            if (c >= 'a' && c <= 'z') {
                mot += c;
                // On traite aussi les 2 caractères spéciaux "æ" et "œ"
            } else if (c == 'æ') {
                mot += "ae";
            } else if (c == 'œ') {
                mot += "oe";
            } else {
                // On ajoute le mot seulement si le mot n'est pas déjà contenu dans le tableau et si ce n'est pas une lettre seule
                // (exemple: le mot "c" ne sera pas ajouté dans le tableau puisque c'est une lettre seule)
                if (mot.length() > 1 && !isIncludedInTable(mot, mots)) {
                    mots = addToTable(mots, mot);
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

    // Retourne un tableau au contenu identique à celui entré en paramètres mais avec item en plus en dernier index
    static String[] addToTable(String[] table, String item) {
        String[] result = enlargeTable(table);
        result[result.length - 1] = item;
        return result;
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

    // Retourne un entier pseudo-aléatoirement entre min (inclus) et max (inclus)
    // Source: https://stackoverflow.com/a/363732
    static int generateRandomInt(int min, int max) {
        return min + (int) (Math.random() * (max - min + 1));
    }
}
