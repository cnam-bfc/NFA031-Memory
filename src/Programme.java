
import java.io.FileNotFoundException;
import java.io.IOException;

public class Programme {

    static final String GAME_NAME = "Jeux de Mémoire";
    static final int TERMINAL_MINLENGTH = 50;
    static final int TERMINAL_MINHEIGHT = 5;
    static final int TERMINAL_CLEANUP_HEIGHT = 15;
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
            menuCode = showMenu("Menu principal", "", "Jouer", "Statistiques", "Quitter");
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
                    showQuitMenu(stats);
            }
        } while (menuCode != 3);
    }

    // Affiche le menu des statistiques quand on quitte le programme
    static void showQuitMenu(String[][] stats) {
        clearConsole();
        showBoundingBoxWithContent(GAME_NAME, "À bientôt...");
    }

    // Affiche le menu des statistiques d'un jeu en particulié
    static void showStatsMenu(String[][] stats, String gameName) {
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
            if (stats[0][i].equals(gameName)) {
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
    // Retourne faux si il faut réafficher le menu de sélection des jeux
    static boolean launchSerieDeMotsGame(String[][] stats) {
        String gameName = GAMES_NAMES[0];
        String difficulty = askDifficulty();
        // Si retour est sélectionné
        if (difficulty.equals("")) {
            return false;
        }
        boolean replay = true;
        while (replay) {
            int nbTermes = getNbTermesDifficulty(difficulty);
            String[] mots = pickRandomWordsFromText("Extrait_texte", difficulty);
            if (mots.length < nbTermes) {
                showErrorMessage(gameName + "\n-> Pas assez le mots pour démarrer le jeu");
                return true;
            }

            for (int i = 0; i < mots.length; i++) {
                String newMot = mots[i];
                showMessage(gameName, "Mots à mémoriser", "", newMot);
                String saisie = askString("Quel est le mot?");
                if (!newMot.equalsIgnoreCase(saisie)) {
                    // On sauvegarde les statistiques de la partie et on en profite pour lui afficher
                    addStat(stats, gameName, difficulty, "0", askString("Quel est votre pseudo?"));
                    showStatsMenu(stats, gameName);
                    break;
                }
            }

            // On demande au joueur si il veut rejouer
            replay = askReplay();
            // Si il veut rejouer et qu'il veut une difficulté différente, on lui demande la nouvelle difficulté et on la change
            if (replay && !askReplayWithSameDifficulty()) {
                difficulty = askDifficulty();
            }
        }
        return true;
    }

    // Lance le jeu de série de nombres
    // Retourne faux si il faut réafficher le menu de sélection des jeux
    static boolean launchSerieDeNombresGame(String[][] stats) {
        devMessage("Série de nombres");
        return false;
    }

    // Lance le jeu de paires de mots
    // Retourne faux si il faut réafficher le menu de sélection des jeux
    static boolean launchPairesDeMotsGame(String[][] stats) {
        devMessage("Paires de mots");
        return false;
    }

    static boolean askReplay() {
        int menuCode = showMenu("Rejouer", "Voulez-vous rejouer au même jeu?", "Oui", "Non");
        return menuCode == 1;
    }

    static boolean askReplayWithSameDifficulty() {
        int menuCode = showMenu("Rejouer", "Voulez-vous rejouer avec la même difficulté?", "Oui", "Non");
        return menuCode == 1;
    }

    // Retourne la difficulté qui est demandé au joueur
    // Retourne un string vide ("") si retour est selectionné
    static String askDifficulty() {
        return askDifficulty(true);
    }

    // Retourne la difficulté qui est demandé au joueur
    // Retourne un string vide ("") si retour est selectionné
    static String askDifficulty(boolean backButton) {
        int menuCode;
        if (backButton) {
            menuCode = showMenu("Difficulté", "", "Facile", "Normal", "Difficile", "Personnalisé", "Retour");
        } else {
            menuCode = showMenu("Difficulté", "", "Facile", "Normal", "Difficile", "Personnalisé");
        }
        switch (menuCode) {
            case 1 -> {
                // Facile
                return convertDifficultyToString(10, 3, 5);
            }
            case 2 -> {
                // Normal
                return convertDifficultyToString(20, 4, 7);
            }
            case 3 -> {
                // Difficile
                return convertDifficultyToString(30, 5, 10);
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

    // Affiche un message d'avertissement à l'utilisateur
    static void showWarningMessage(String... message) {
        showMessage("ATTENTION", message);
    }

    // Affiche un message d'erreur à l'utilisateur
    static void showErrorMessage(String... message) {
        showMessage("ERREUR", message);
    }

    // Affiche un message à l'utilisateur avec un cadre muni d'un titre et le cadre fera une longueur et hauteur par défaut défini par des constantes
    static void showMessage(String title, String... message) {
        showMessage(title, TERMINAL_MINLENGTH, message);
    }

    // Affiche un message à l'utilisateur avec un cadre muni d'un titre et le cadre fera au moins minLength de longueur et une hauteur par défaut défini dans une constante
    static void showMessage(String title, int minLength, String... message) {
        showMessage(title, minLength, TERMINAL_MINHEIGHT, message);
    }

    // Affiche un message à l'utilisateur avec un cadre muni d'un titre et le cadre fera au moins minLength de longueur et minHeight de hauteur
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
        // " " Pour avoir au moins un tiret (spacer) de chaque côté du titre si il est le plus grand par rapport aux Strings de lines
        // car les spacer ne sont pas forcément retourné dans la méthode getCenteredText() qui est utilisé plus tard dans cette méthode
        String[] tableauTemp = addToTable(lines, " " + title + " ");

        // + 2 pour avoir au moins 1 spacer de chaque côté du titre
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

    // Retourne la chaine de caractères passé en paramètre centré avec des caractères passé en paramètre
    // Au moins un spacer est retourné de chaque côté du text
    static String centerText(String text, char spacer, int length) {
        return centerText(text, spacer, spacer, length);
    }

    // Retourne la chaine de caractères passé en paramètre centré avec des caractères passé en paramètre
    // Les spacer ne sont pas forcément retournés mais separator est obligatoirement retourné 1 fois de chaque côté du text
    static String centerText(String text, char spacer, char separator, int length) {
        String result = "";

        // Longueur du text + 2 pour les 2 separator (qui doivent êtres retourn obligatoirement)
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
        // On imprime des retour à la ligne au cas où le terminal ne supporte pas le scroll ci-après
        for (int i = 0; i < TERMINAL_CLEANUP_HEIGHT; i++) {
            System.out.println();
        }

        // On scroll le terminal
        System.out.print("\033[H\033[2J");
        // On vide le buffer d'écriture dans le terminal si des caractères n'était pas encore affichés
        System.out.flush();
    }

    // Retourne la liste des mots du texte correspondant à la difficulté
    static String[] pickRandomWordsFromText(String fileName, String difficulty) {
        String[] result = new String[0];

        // On récupère les caractéristiques de la difficulté
        int nbTermes = getNbTermesDifficulty(difficulty);
        int min = getMinLengthDifficulty(difficulty);
        int max = getMaxLengthDifficulty(difficulty);

        // On parcours les mots du texte qui on été mélangés et on les ajoute au résultat si ils correspondent à la difficulté
        for (String word : shuffleTable(readText(fileName))) {
            // On arrête d'ajouter des mots si on a assez de mots pour la difficulté choisi
            if (result.length >= nbTermes) {
                break;
            }

            // On ajoute le mot si il correspond aux caractéristiques de difficulté
            if (word.length() >= min && word.length() <= max) {
                result = addToTable(result, word);
            }
        }

        return result;
    }

    // Cherche le fichier dans plusieurs dossiers et le lit si il est trouvé
    // Retourne un tableau des mots du fichier
    // Retourne un tableau de 0 valeurs si le fichier n'est pas trouvé
    static String[] readText(String fileName) {
        // Dossiers possibles où l'on peut trouver le fichier texte dont le nom est passé en paramètre
        String[] folders = {".\\", ".\\src\\", ".\\textes\\"};

        for (String folder : folders) {
            // Si le fichier existe, on lit le fichier et on retourne les mots du fichier sans doublons, ponctuation, ect...
            if (fileExists(folder + fileName + ".txt")) {
                return readFile(folder + fileName + ".txt");
            }
        }

        return new String[0];
    }

    // Retourne le tableau des mots du fichier texte sans doublons, ni ponctuations, ni de lettres seules
    // Retourne un tableau de 0 valeurs si une exception est levée
    static String[] readFile(String filePath) {
        try {
            String texte = Lecteur.LireTexte(filePath);
            return convertTextToWords(texte);
        } catch (IOException err1) {
            showErrorMessage("Erreur lors de la lecture du fichier,", "-> Fichier: " + filePath, "-> Erreur: " + err1.getMessage());
            // On retourne un tableau vide puisqu'une exception a été levée
            return new String[0];
        }
    }

    // Retourne vrai si le fichier existe
    // Retourne faux si il n'existe pas
    static boolean fileExists(String filePath) {
        try {
            Lecteur.LireTexte(filePath);
            // Pas d'exception déclanché donc le fichier existe
            return true;
        } catch (FileNotFoundException ex) {
            // Le fichier n'existe pas
            return false;
        } catch (IOException ex) {
            // Le fichier existe mais erreur de lecture
            return true;
        }
    }

    // Retourne un tableau de mots à partir d'une chaine de caractères
    // sans doublons, sans ponctuation, sans espaces et sans lettres seules
    // (exemple pour "c'était", seulement "était" est ajouté dans le tableau)
    static String[] convertTextToWords(String text) {
        String[] mots = new String[0];
        // On met le texte en minuscules
        text = text.toLowerCase();

        // On va concaténer les caractères les uns à la suite des autres dans cette chaine de caractères
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

                // Le dernier caractère du mot a été atteint donc on réinitialise mot pour le prochain mot
                mot = "";
            }
        }

        return mots;
    }

    // Retourne le tableau passé en paramètre mélangé
    static String[] shuffleTable(String[] table) {
        for (int i = 0; i < table.length; i++) {
            int j = generateRandomInt(0, table.length - 1);
            String temp = table[j];
            table[j] = table[i];
            table[i] = temp;
        }
        return table;
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

    // Retourne la longueur maximal des chaines de caractères contenu dans le tableau
    static int getMaximumLength(String[] table) {
        int max = 0;

        for (String str : table) {
            if (str.length() > max) {
                max = str.length();
            }
        }

        return max;
    }

    // Met le programme en pause pendant x ms
    static void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException ex) {
            showErrorMessage("Attendre", "-> Impossible d'attendre " + millis + "ms", "-> Erreur: " + ex.getMessage());
        }
    }

    // Retourne un entier pseudo-aléatoire entre min (inclus) et max (inclus)
    // Source: https://stackoverflow.com/a/363732
    static int generateRandomInt(int min, int max) {
        return min + (int) (Math.random() * (max - min + 1));
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
}
