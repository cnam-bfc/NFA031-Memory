
import java.io.FileNotFoundException;
import java.io.IOException;

public class Programme {

    static final int TERMINAL_MINLENGTH = 75;
    static final int TERMINAL_MINHEIGHT = 9;
    static final int TERMINAL_CLEANUP_HEIGHT = 15;

    public static void main(String[] args) {
        // Au lancement du programme on démarre le jeu
        startGame();
    }

    // Démarre le jeu
    static void startGame() {
        // TODO Augmenter cette valeur
        // Au lancement du programme on affiche un faux écran de chargement
        showLoadingScreen(100, 25);

        // Après on initialise les statistiques
        String[][] stats = initStats();

        // Enfin on affiche le menu principal et si Quitter est sélectionné on reprend la main donc le programme se termine
        showMainMenu(stats);
    }

    // Affiche un faux écran de chargement, plus speed est proche de 0 plus le chargement est rapide
    static void showLoadingScreen(int speed, int length) {
        // On vérifie les paramètres en entrée
        if (speed < 0) {
            showErrorMessage("Écran de chargement", "-> Speed doit être supérieur à 0");
            return;
        } else if (length < 1) {
            showErrorMessage("Écran de chargement", "-> Length doit être supérieur à 0");
            return;
        }

        // Boucle des écrans de chargement
        for (int i = 0; i < length; i++) {
            // On efface la console
            clearConsole();

            String progressBar = "[";
            // Bourrage avant le curseur de chargement
            for (int j = 0; j < i; j++) {
                progressBar += "=";
            }

            // Curseur
            progressBar += "*";

            // Bourrage après le curseur de chargement
            for (int j = length - 1; j > i; j--) {
                progressBar += " ";
            }
            progressBar += "]";

            // On affiche l'écran de chargement sans titre (1er argument), avec ce contenu (chaque argument est une ligne)
            showBoundingBoxWithContent("", "Jeux de Mémoire", "", "", "", "Chargement...", "", progressBar);

            // On attend un temps aléatoire entre 0 et speed ms pour séparer les faux écrans de chargement dans le temps
            sleep(generateRandomInt(0, speed));
        }
    }

    // Affiche le menu principal
    static void showMainMenu(String[][] stats) {
        // Stocke la valeur de l'option sélectionné dans le menu qui sera affiché à l'utilisateur
        int menuCode;

        do {
            menuCode = showMenu("Menu principal", "", "Jouer", "Statistiques", "Quitter");

            switch (menuCode) {
                // Jouer selectionné
                case 1 ->
                    showGameMenu(stats);
                // Statistiques selectionné
                case 2 ->
                    showStats(stats);
                // Quitter selectionné
                case 3 ->
                    showQuitMenu(stats);
            }

            // Tant que 3 (Quitter) n'est pas selectionné
        } while (menuCode != 3);
    }

    // Affiche les statistiques avant de quitter le programme
    static void showQuitMenu(String[][] stats) {
        showStats(stats);
        clearConsole();
        showBoundingBoxWithContent("Jeux de Mémoire", "À bientôt...");
    }

    // Affiche le menu de sélection des jeux
    static void showGameMenu(String[][] stats) {
        // Stocke la valeur de l'option sélectionné dans le menu qui sera affiché à l'utilisateur
        int menuCode;

        do {
            menuCode = showMenu("Choix du jeu", "", "Série de mots", "Série de nombres", "Paires de mots", "Retour");

            switch (menuCode) {
                // Série de mots selectionné
                case 1 ->
                    launchSerieDeMotsGame(stats);
                // Série de nombres selectionné
                case 2 ->
                    launchSerieDeNombresGame(stats);
                // Paires de mots selectionné
                case 3 -> {
                    int pairesDeMotsMenuCode = showMenu("Choix du jeu", "", "Mode normal", "Mode inversé", "Mode aléatoire", "Retour");

                    switch (pairesDeMotsMenuCode) {
                        // Mode normal selectionné
                        case 1 ->
                            launchPairesDeMotsGame(stats, false, false);
                        // Mode inversé selectionné
                        case 2 ->
                            launchPairesDeMotsGame(stats, false, true);
                        // Mode aléatoire selectionné
                        case 3 ->
                            launchPairesDeMotsGame(stats, true, false);
                    }
                }
            }

            // On affiche le menu tant que 4 (retour) n'est pas sélectionné
        } while (menuCode != 4);
    }

    // Lance le jeu de série de mots
    static void launchSerieDeMotsGame(String[][] stats) {
        String gameName = "Série de mots";
        // difficulty[0] = nbTermes
        // difficulty[1] = minLength
        // difficulty[2] = maxLength
        int[] difficulty = askDifficulty();

        // Si Retour est sélectionné on arrête le jeu en cours
        if (difficulty.length == 0) {
            return;
        }

        boolean replay;
        do {
            // On récupère des mots aléatoires issue du texte Extrait_texte qui correspondent à la difficulté
            String[] mots = pickRandomWordsFromText("Extrait_texte", difficulty[0], difficulty[1], difficulty[2]);
            // Si il n'y a pas assez de mots on arrête le jeu
            if (mots.length < difficulty[0]) {
                showErrorMessage(gameName, "-> Pas assez le mots pour démarrer le jeu");
                return;
            }

            int roundFinished = 0;
            // True si on veut arrêter la partie en cours
            boolean stopGame = false;

            // Pour chaque round de la partie
            for (int i = 1; i <= mots.length; i++) {
                String memorizeTitle;
                // Texte différent si on est au premier round et qu'il y a seulement 1 mot à mémoriser
                if (i == 1) {
                    memorizeTitle = "Mot à mémoriser";
                } else {
                    memorizeTitle = "Mots à mémoriser";
                }

                String[] wordsToMemorize = new String[i];

                // On fait la liste des mots à deviner dans l'ordre
                for (int j = 0; j < i; j++) {
                    wordsToMemorize[j] = mots[j];
                }

                // On affiche les mots à deviner à l'utilisateur + un titre sépéré des mots par une ligne vide
                showMessage(gameName + " - Round " + i + "/" + mots.length, addOnTopOfTable(addOnTopOfTable(wordsToMemorize, ""), memorizeTitle));

                // On demande à l'utilisateur de restituer les mots à mémoriser un par un
                for (int j = 0; j < i; j++) {
                    String[] wordsMemorized = new String[j + 1];

                    // On fait la liste des mots qu'il a déjà correctement restitué
                    for (int k = 0; k < j; k++) {
                        wordsMemorized[k] = mots[k];
                    }

                    // On ajoute une question qui sera afficher à l'utilisateur pour lui demander de restituer le nième mot
                    wordsMemorized[wordsMemorized.length - 1] = "Quel est le " + convertIntToFrenchString(j + 1) + " mot?";

                    // On stocke ce qu'a répondu l'utilisateur
                    String saisie = askString(gameName + " - Round " + i + "/" + mots.length, wordsMemorized);

                    // Si le mot restitué n'est pas correct, on lui affiche un message et on arrête la partie en cours
                    if (!mots[j].equalsIgnoreCase(saisie)) {
                        showMessage(gameName + " - Round " + i + "/" + mots.length, "Mince...", "", "Vous vous êtes trompé,", "Vous avez tapé \"" + saisie + "\" alors que \"" + mots[j] + "\" était attendu");
                        stopGame = true;
                        break;
                    }
                }

                // Si la partie a demandé a être arrêter, on ne commence pas les prochains rounds
                if (stopGame) {
                    break;
                    // Sinon on valide ce round
                } else {
                    roundFinished++;
                }
            }

            // On affiche un message de fécilitation au joueur si il a réussi à terminer tous les rounds
            if (!stopGame) {
                showMessage(gameName, "Bravo !", "", "Vous avez terminé la partie sans vous tromper !", "Un bonus de points vous a été attribué !");
            }

            // On calcule les statistiques de la partie en cours
            float scoreMultiplier = calculateScoreMutiplier(difficulty[1], difficulty[2]);
            String difficultyName = getDifficultyName(scoreMultiplier);
            int finalScore = calculateScore(scoreMultiplier, roundFinished, difficulty[0], !stopGame);

            String pseudo = askString("Statistiques - " + gameName, "Quel est votre pseudo?");

            // On sauvegarde et affiche les stats de la partie en cours
            addStat(stats, gameName, difficultyName, finalScore + "", roundFinished + "", pseudo);
            showMessage("Statistiques - " + gameName, "Joueur", pseudo, "", "Difficulté", difficultyName, "", "Score", finalScore + "", "", "Mots correctement restitués", roundFinished + "");

            // On demande au joueur si il veut rejouer
            replay = askReplay();
            // Si il veut rejouer et qu'il veut une difficulté différente, on lui demande la nouvelle difficulté et on la change
            if (replay && !askReplayWithSameDifficulty()) {
                difficulty = askDifficulty();

                // Si Retour est sélectionné on arrête le jeu en cours
                if (difficulty.length == 0) {
                    return;
                }
            }
        } while (replay);
    }

    // Lance le jeu de série de nombres
    static void launchSerieDeNombresGame(String[][] stats) {
        String gameName = "Série de nombres";
        // difficulty[0] = nbTermes
        // difficulty[1] = minLength
        // difficulty[2] = maxLength
        int[] difficulty = askDifficulty();

        // Si Retour est sélectionné on arrête le jeu en cours
        if (difficulty.length == 0) {
            return;
        }

        boolean replay;
        do {
            // On récupère des nombres pseudo-aléatoire qui correspondent à la difficulté
            int[] nombres = generateRandomInts(difficulty[0], difficulty[1], difficulty[2]);
            // Si il y a eu une erreur à la génération des nombres aléatoire
            if (nombres.length == 0) {
                showErrorMessage(gameName, "-> Une erreur est survenue à la génération des nombres aléatoires", "-> Veuillez réessayer avec des longueurs de nombres différentes");
                return;
            }

            int roundFinished = 0;
            // True si on veut arrêter la partie en cours
            boolean stopGame = false;

            // Pour chaque round de la partie
            for (int i = 1; i <= nombres.length; i++) {
                String memorizeTitle;
                // Texte différent si on est au premier round et qu'il y a seulement 1 nombre à mémoriser
                if (i == 1) {
                    memorizeTitle = "Nombre à mémoriser";
                } else {
                    memorizeTitle = "Nombres à mémoriser";
                }

                String[] numbersToMemorize = new String[i];

                // On fait la liste des nombres à deviner dans l'ordre
                for (int j = 0; j < i; j++) {
                    numbersToMemorize[j] = nombres[j] + "";
                }

                // On affiche les nombres à deviner à l'utilisateur + un titre sépéré des nombres par une ligne vide
                showMessage(gameName + " - Round " + i + "/" + nombres.length, addOnTopOfTable(addOnTopOfTable(numbersToMemorize, ""), memorizeTitle));

                // On demande à l'utilisateur de restituer les nombres un par un
                for (int j = 0; j < i; j++) {
                    String[] numbersMemorized = new String[j + 1];

                    // On fait la liste des nombres qu'il a déjà correctement restitué
                    for (int k = 0; k < j; k++) {
                        numbersMemorized[k] = nombres[k] + "";
                    }

                    // On ajoute une question qui sera afficher à l'utilisateur pour lui demander de restituer le nième nombre
                    numbersMemorized[numbersMemorized.length - 1] = "Quel est le " + convertIntToFrenchString(j + 1) + " nombre?";

                    // On stocke ce qu'a répondu l'utilisateur
                    int saisie = askInteger(gameName + " - Round " + i + "/" + nombres.length, numbersMemorized);

                    // Si le nombre restitué n'est pas correct, on lui affiche un message et on arrête la partie en cours
                    if (nombres[j] != saisie) {
                        showMessage(gameName + " - Round " + i + "/" + nombres.length, "Mince...", "", "Vous vous êtes trompé,", "Vous avez tapé \"" + saisie + "\" alors que \"" + nombres[j] + "\" était attendu");
                        stopGame = true;
                        break;
                    }
                }

                // Si la partie a demandé a être arrêter, on ne commence pas les prochains rounds
                if (stopGame) {
                    break;
                    // Sinon on valide ce round
                } else {
                    roundFinished++;
                }
            }

            // On affiche un message de fécilitation au joueur si il a réussi à terminer tous les rounds
            if (!stopGame) {
                showMessage(gameName, "Bravo !", "", "Vous avez terminé la partie sans vous tromper !", "Un bonus de points vous a été attribué !");
            }

            // On calcule les statistiques de la partie en cours
            float scoreMultiplier = calculateScoreMutiplier(difficulty[1], difficulty[2]);
            String difficultyName = getDifficultyName(scoreMultiplier);
            int finalScore = calculateScore(scoreMultiplier, roundFinished, difficulty[0], !stopGame);

            String pseudo = askString("Statistiques - " + gameName, "Quel est votre pseudo?");

            // On sauvegarde et affiche les stats de la partie en cours
            addStat(stats, gameName, difficultyName, finalScore + "", roundFinished + "", pseudo);
            showMessage("Statistiques - " + gameName, "Joueur", pseudo, "", "Difficulté", difficultyName, "", "Score", finalScore + "", "", "Nombres correctement restitués", roundFinished + "");

            // On demande au joueur si il veut rejouer
            replay = askReplay();
            // Si il veut rejouer et qu'il veut une difficulté différente, on lui demande la nouvelle difficulté et on la change
            if (replay && !askReplayWithSameDifficulty()) {
                difficulty = askDifficulty();

                // Si Retour est sélectionné on arrête le jeu en cours
                if (difficulty.length == 0) {
                    return;
                }
            }
        } while (replay);
    }

    // Lance le jeu de paires de mots
    static void launchPairesDeMotsGame(String[][] stats, boolean random, boolean reverse) {
        String gameName = "Paires de mots";
        if (random) {
            gameName += " (aléatoire)";
        } else if (reverse) {
            gameName += " (inversé)";
        }
        // difficulty[0] = nbTermes
        // difficulty[1] = minLength
        // difficulty[2] = maxLength
        int[] difficulty = askDifficulty();

        // Si Retour est sélectionné on arrête le jeu en cours
        if (difficulty.length == 0) {
            return;
        }

        boolean replay;
        do {
            // On récupère des mots aléatoires issue du texte Extrait_texte qui correspondent à la difficulté (2x plus car on va former des paires de mots)
            String[] mots = pickRandomWordsFromText("Extrait_texte", difficulty[0] * 2, difficulty[1], difficulty[2]);
            // Si il n'y a pas assez de mots on arrête le jeu
            if (mots.length < difficulty[0]) {
                showErrorMessage(gameName, "-> Pas assez le mots pour démarrer le jeu");
                return;
            }

            int roundFinished = 0;
            // True si on veut arrêter la partie en cours
            boolean stopGame = false;

            String[] seriesToMemorize = new String[difficulty[0]];
            int[] seriesIndex = new int[difficulty[0]];

            // On fait la liste des séries de mots à deviner avec la liste de l'index de chacune de ces séries
            for (int i = 0; i < mots.length; i += 2) {
                seriesToMemorize[i / 2] = mots[i] + " => " + mots[i + 1];
                seriesIndex[i / 2] = i;
            }

            // On mélange la liste des index des séries pour quelles apparaissent aléatoirement
            seriesIndex = shuffleTable(seriesIndex);

            // On affiche les séries de mots à deviner à l'utilisateur + un titre sépéré des séries de mots par une ligne vide
            showMessage(gameName, addOnTopOfTable(addOnTopOfTable(seriesToMemorize, ""), "Séries de mots à mémoriser"));

            // Pour chaque round de la partie
            for (int i = 1; i <= difficulty[0]; i++) {
                String[] seriesMemorized = new String[i];

                // On fait la liste des séries de mots qu'il a déjà correctement restitué
                for (int j = 0; j < i - 1; j++) {
                    int index = seriesIndex[j];
                    seriesMemorized[j] = mots[index] + " => " + mots[index + 1];
                }

                boolean inverse = reverse;

                // Si le jeu est demandé en mode aléatoire on inverse les question et mots de façon aléatoire
                if (random && generateRandomInt(0, 1) == 1) {
                    inverse = !inverse;
                }

                int index = seriesIndex[i - 1];
                String question;
                String answer;

                if (inverse) {
                    question = mots[index + 1];
                    answer = mots[index];
                } else {
                    question = mots[index];
                    answer = mots[index + 1];
                }

                // On ajoute une question qui sera afficher à l'utilisateur pour lui demander de restituer la nième série de mots
                seriesMemorized[seriesMemorized.length - 1] = "Quel est le mot associé à " + question + "?";

                // On stocke ce qu'a répondu l'utilisateur
                String saisie = askString(gameName + " - Round " + i + "/" + difficulty[0], seriesMemorized);

                // Si le mot restitué n'est pas correct, on lui affiche un message et on arrête la partie en cours
                if (!answer.equalsIgnoreCase(saisie)) {
                    showMessage(gameName + " - Round " + i + "/" + difficulty[0], "Mince...", "", "Vous vous êtes trompé,", "Vous avez tapé \"" + saisie + "\" alors que \"" + answer + "\" était attendu");
                    stopGame = true;
                    break;
                    // Sinon on valide ce round
                } else {
                    roundFinished++;
                }
            }

            // On affiche un message de fécilitation au joueur si il a réussi à terminer tous les rounds
            if (!stopGame) {
                showMessage(gameName, "Bravo !", "", "Vous avez terminé la partie sans vous tromper !", "Un bonus de points vous a été attribué !");
            }

            // On calcule les statistiques de la partie en cours
            float scoreMultiplier = calculateScoreMutiplier(difficulty[1], difficulty[2]);
            String difficultyName = getDifficultyName(scoreMultiplier);
            int finalScore = calculateScore(scoreMultiplier, roundFinished, difficulty[0], !stopGame);

            String pseudo = askString("Statistiques - " + gameName, "Quel est votre pseudo?");

            // On sauvegarde et affiche les stats de la partie en cours
            addStat(stats, gameName, difficultyName, finalScore + "", roundFinished + "", pseudo);
            showMessage("Statistiques - " + gameName, "Joueur", pseudo, "", "Difficulté", difficultyName, "", "Score", finalScore + "", "", "Séries de mots correctement restitués", roundFinished + "");

            // On demande au joueur si il veut rejouer
            replay = askReplay();
            // Si il veut rejouer et qu'il veut une difficulté différente, on lui demande la nouvelle difficulté et on la change
            if (replay && !askReplayWithSameDifficulty()) {
                difficulty = askDifficulty();

                // Si Retour est sélectionné on arrête le jeu en cours
                if (difficulty.length == 0) {
                    return;
                }
            }
        } while (replay);
    }

    // Affiche les statistiques des jeux
    static void showStats(String[][] stats) {
        // On va stocker les statistiques par jeu et difficulté dans ce tableau à 2 dimensions
        String[][] gamesStats = new String[5][];
        // On va stocker le nom du jeu
        gamesStats[0] = new String[0];
        // On va stocker la difficulté du jeu
        gamesStats[1] = new String[0];
        // On va stocker le nombre de parties joués
        gamesStats[2] = new String[0];
        // On va stocker le score moyen
        gamesStats[3] = new String[0];
        // On va stocker le meilleur score (avec le nom du joueur qui l'a obtenu)
        gamesStats[4] = new String[0];

        // On fait la liste de tous les noms des différents jeux (et sous-jeux)
        String[] gamesList = {"Série de mots", "Série de nombres", "Paires de mots", "Paires de mots (inversé)", "Paires de mots (aléatoire)"};
        // On fait la liste des différentes difficultés possible
        String[] difficultyList = {"Facile", "Normal", "Difficile", "Extrême"};

        // On récupère les statistiques pour chaque jeu et chaque difficulté du jeu
        for (String game : gamesList) {
            for (String difficulty : difficultyList) {
                // On récupère les stats du jeu et de la difficulté
                String[] gameStats = getStat(stats, game, difficulty);

                // Si le tableau est vide cela veut dire qu'aucune partie n'a été joué donc on passe aux stats suivantes
                if (gameStats.length == 0) {
                    continue;
                }

                // On ajoute aux stats les stats de ce jeu avec cette difficulté
                // Nom du jeu
                gamesStats[0] = addOnBottomOfTable(gamesStats[0], gameStats[0]);
                // Difficulté
                gamesStats[1] = addOnBottomOfTable(gamesStats[1], gameStats[1]);
                // Nombre de paties joués
                gamesStats[2] = addOnBottomOfTable(gamesStats[2], gameStats[2]);
                // Score moyen
                gamesStats[3] = addOnBottomOfTable(gamesStats[3], gameStats[3]);
                // Meilleur score (+ nom du joueur)
                gamesStats[4] = addOnBottomOfTable(gamesStats[4], gameStats[4]);
            }
        }

        // On calcule pour chaque attribut des statistiques quel sera la chaine de caractère la plus longue pour que l'on puisse centrer le texte par la suite
        int gameNameLength = getMaximumLength(addOnBottomOfTable(gamesStats[0], "  Jeux  "));
        int difficultyLength = getMaximumLength(addOnBottomOfTable(gamesStats[1], "  Difficulté  "));
        int gamesPlayedLength = getMaximumLength(addOnBottomOfTable(gamesStats[2], "  Parties joués  "));
        int scoreAverageLength = getMaximumLength(addOnBottomOfTable(gamesStats[3], "  Score moyen  "));
        int bestScoreLength = getMaximumLength(addOnBottomOfTable(gamesStats[4], "  Meilleur score  "));

        // On fait le tableau des chaines de caractères qui seront affichés au joueur
        String[] message = new String[0];

        // On commence par rentrer la première ligne qui contient les titres des attributs
        message = addOnBottomOfTable(message, centerText("Jeux", '-', ' ', gameNameLength)
                + centerText("Difficulté", '-', ' ', difficultyLength)
                + centerText("Parties joués", '-', ' ', gamesPlayedLength)
                + centerText("Score moyen", '-', ' ', scoreAverageLength)
                + centerText("Meilleur score", '-', ' ', bestScoreLength));

        // On ajoute chaque attribut de la statistique centré sur la même ligne
        for (int i = 0; i < gamesStats[0].length; i++) {
            message = addOnBottomOfTable(message, centerText(gamesStats[0][i], ' ', ' ', gameNameLength)
                    + centerText(gamesStats[1][i], ' ', ' ', difficultyLength)
                    + centerText(gamesStats[2][i], ' ', ' ', gamesPlayedLength)
                    + centerText(gamesStats[3][i], ' ', ' ', scoreAverageLength)
                    + centerText(gamesStats[4][i], ' ', ' ', bestScoreLength));
        }

        // Enfin on affiche le tableau des statistique sur la console
        showMessage("Statistiques", message);
    }

    // Retourne les statistiques d'un jeu avec une difficulté
    static String[] getStat(String[][] stats, String gameName, String difficulty) {
        int gamesPlayed = 0;
        int sumScore = 0;
        int bestScore = 0;
        String playerName = "";

        // On regarde dans toutes les statistiques celle qui correspondent au jeu et difficulté passé en paramètre puis on fait la somme des parties jouées, ect...
        for (int i = 0; i < stats[0].length; i++) {
            if (stats[0][i].equalsIgnoreCase(gameName) && stats[1][i].equalsIgnoreCase(difficulty)) {
                int score = Integer.parseInt(stats[2][i]);

                // Si le score est mieux que le meilleur score on le remplace, ainsi que le nom du joueur
                if (score > bestScore) {
                    bestScore = score;
                    playerName = stats[4][i];
                }

                // On ajoute le score à la somme des scores pour que l'on puisse faire la moyenne des scores
                sumScore += score;
                gamesPlayed++;
            }
        }

        // Si au moins une partie a été joué on retourne les stats du jeu sinon on retourne un tableau vide
        if (gamesPlayed != 0) {
            return createTable(gameName, difficulty, gamesPlayed + "", (sumScore / gamesPlayed) + "", bestScore + " -> " + playerName);
        } else {
            return new String[0];
        }
    }

    // Ajoute les statistiques d'une partie au tableau des satistiques
    static void addStat(String[][] stats, String gameName, String difficulty, String score, String roundFinished, String playerName) {
        stats[0] = addOnBottomOfTable(stats[0], gameName);
        stats[1] = addOnBottomOfTable(stats[1], difficulty);
        stats[2] = addOnBottomOfTable(stats[2], score);
        stats[3] = addOnBottomOfTable(stats[3], roundFinished);
        stats[4] = addOnBottomOfTable(stats[4], playerName);
    }

    // Retourne le tableau initialisé des statistiques
    static String[][] initStats() {
        String[][] stats = new String[5][];
        // Numéro du jeu de la partie
        stats[0] = new String[0];
        // Difficulté de la partie
        stats[1] = new String[0];
        // Score de la partie
        stats[2] = new String[0];
        // Round terminés de la partie
        stats[3] = new String[0];
        // Nom du joueur
        stats[4] = new String[0];
        return stats;
    }

    // Retourne si le joueur veut rejouer au même jeu
    static boolean askReplay() {
        int menuCode = showMenu("Rejouer", "Voulez-vous rejouer au même jeu?", "Oui", "Non");
        return menuCode == 1;
    }

    // Retourne si le joueur veut rejouer avec la même difficulté
    static boolean askReplayWithSameDifficulty() {
        int menuCode = showMenu("Rejouer", "Voulez-vous rejouer avec la même difficulté?", "Oui", "Non");
        return menuCode == 1;
    }

    // Retourne la difficulté qui est demandé au joueur
    // Retourne un string vide ("") si retour est selectionné
    static int[] askDifficulty() {
        int menuCode = showMenu("Difficulté", "", "Facile", "Normal", "Difficile", "Personnalisé", "Retour");
        switch (menuCode) {
            case 1 -> {
                // Facile
                return createTable(5, 3, 5);
            }
            case 2 -> {
                // Normal
                return createTable(10, 4, 7);
            }
            case 3 -> {
                // Difficile
                return createTable(15, 5, 10);
            }
            case 4 -> {
                // Personnalisé
                int nbTermes = askInteger("Difficulté", 1, 100, "Saisissez le nombre de termes désiré");
                int minLength = askInteger("Difficulté", 1, 10, "Saisissez le minimum de caractères d'un terme");
                int maxLength = askInteger("Difficulté", minLength, 30, "Saisissez le maximum de caractères d'un terme");
                return createTable(nbTermes, minLength, maxLength);
            }
            default -> {
                // Retour
                return new int[0];
            }
        }
    }

    // Affiche un menu à l'utilisateur avec un titre, un sous-titre et les choix passés en paramètre
    // Retourne le numéro du choix de l'utilisateur
    static int showMenu(String title, String subTitle, String... choices) {
        int item = 0;
        try {
            clearConsole();
            String[] lines = new String[choices.length];
            for (int i = 0; i < choices.length; i++) {
                lines[i] = i + 1 + ". " + choices[i];
            }
            if (!subTitle.equals("")) {
                lines = addOnTopOfTable(lines, subTitle);
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
    static String askString(String title, String... lines) {
        try {
            clearConsole();
            showBoundingBoxWithContent(title, lines);
            System.out.print("Saisie > ");
            return EConsole.lireString();
        } catch (IOException ex) {
            showErrorMessage("", "-> Impossible de lire la chaine de caractères, veuillez réessayer");
            return askString(title, lines);
        }
    }

    // Retourne l'entier saisi par l'utilisateur
    // L'entier saisi doit être compris entre min (inclus) et max (inclus)
    static int askInteger(String title, int min, int max, String... lines) {
        int integer;
        do {
            integer = askInteger(title, lines);
            if (integer < min || integer > max) {
                showWarningMessage("", "-> Veuillez saisir un entier entre " + min + " et " + max);
            }
        } while (integer < min || integer > max);
        return integer;
    }

    // Retourne l'entier saisi par l'utilisateur
    static int askInteger(String title, String... lines) {
        try {
            clearConsole();
            showBoundingBoxWithContent(title, lines);
            System.out.print("Saisie > ");
            return EConsole.lireInt();
        } catch (NumberFormatException ex) {
            showWarningMessage("", "-> Veuillez saisir un entier");
            return askInteger(title, lines);
        } catch (IOException ex) {
            showErrorMessage("", "-> Impossible de lire l'entier, veuillez réessayer");
            return askInteger(title, lines);
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
            message = addOnBottomOfTable(message, "");
            message = addOnBottomOfTable(message, "Appuyez sur Entrée pour continuer...");
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
        String[] tableauTemp = addOnBottomOfTable(lines, " " + title + " ");

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

        // Longueur du text + 2 pour les 2 separator (qui doivent êtres retourne obligatoirement)
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

    // Retourne une liste de nombres pseudo-aléatoire correspondant à la difficulté
    // Retourne un tableau vide si pas assez de nombres peuvent être générés
    static int[] generateRandomInts(int difficultyNbTermes, int difficultyMinLength, int difficultyMaxLength) {
        // Math.pow(nombre, exposant) retourne nombre puissance exposant
        int min = (int) (Math.pow(10, difficultyMinLength - 1));// Min: 10^(difficultyMinLength - 1) ; ex: pour 2: 10^(2 - 1) = 10^1 = 10 donc 2 de longueur
        int max = (int) (Math.pow(10, difficultyMaxLength) - 1);// Max: (10^difficultyMaxLength) - 1 ; ex: pour 5: (10^5) - 1 = 100000 - 1 = 99999 donc 5 de longueur ; Math.pow(); retourne une la valeur maximum de Integer (Integer.MAX_VALUE) quand x puissance y dépasse MAX_VALUE donc cette valeur ne pourra jamais dépasser Integer.MAX_VALUE

        if (max - min < difficultyNbTermes) {
            // Retourne un tableau vide pour spécifier à la fonction appelante qu'il n'y a pas assez de nombres
            return new int[0];
        }

        int[] result = new int[difficultyNbTermes];

        for (int i = 0; i < difficultyNbTermes; i++) {
            int randomInt = generateRandomInt(min, max);

            // Si le nombre généré n'est déjà pas dans le tableau on l'ajoute
            if (!isIncludedInTable(randomInt, result)) {
                result[i] = randomInt;
                // Sinon on génère un nombre différent au prochain tour
            } else {
                i--;
            }
        }

        return result;
    }

    // Retourne une liste de mots du texte pseudo-aléatoire correspondant à la difficulté
    static String[] pickRandomWordsFromText(String fileName, int difficultyNbTermes, int difficultyMinLength, int difficultyMaxLength) {
        String[] result = new String[0];

        // On parcours les mots du texte qui on été mélangés et on les ajoute au résultat si ils correspondent à la difficulté
        for (String word : shuffleTable(readText(fileName))) {
            // On arrête d'ajouter des mots si on a assez de mots pour la difficulté choisi
            if (result.length >= difficultyNbTermes) {
                break;
            }

            // On ajoute le mot si il correspond aux caractéristiques de difficulté
            if (word.length() >= difficultyMinLength && word.length() <= difficultyMaxLength) {
                result = addOnBottomOfTable(result, word);
            }
        }

        return result;
    }

    // Retourne le nom de la difficulté en fonction du multiplicateur de score
    static String getDifficultyName(float difficultyMultiplier) {
        if (difficultyMultiplier < 1.0f) {
            return "Facile";
        } else if (difficultyMultiplier < 1.4f) {
            return "Normal";
        } else if (difficultyMultiplier < 1.8f) {
            return "Difficile";
        } else {
            return "Extrême";
        }
    }

    // Calcule le score final à partir du score, du multiplicateur de score ; Un bonus de points est ajouté si le joueur est arrivé à terminer tous les rounds
    static int calculateScore(float multiplier, int score, int difficultyNbTermes, boolean gameFinished) {
        float result = multiplier * score;

        if (gameFinished) {
            result += difficultyNbTermes / 2;
        }

        return (int) (result);
    }

    // Retourne le multiplicateur de la difficulté qui pourra ensuite être appliqué au score
    static float calculateScoreMutiplier(int difficultyMinLength, int difficultyMaxLength) {
        return 1 + (0.15f * (-4 + difficultyMinLength)) + (0.10f * (-7 + difficultyMaxLength));
    }

    // Cherche le fichier dans plusieurs dossiers et le lit si il est trouvé
    // Retourne un tableau des mots du fichier
    // Retourne un tableau de 0 valeurs si le fichier n'est pas trouvé
    static String[] readText(String fileName) {
        // Dossiers possibles où l'on peut trouver le fichier texte dont le nom est passé en paramètre
        String[] folders = {".\\", ".\\src\\", ".\\textes\\"};

        for (String folder : folders) {
            // Si le fichier existe, on lit le fichier et on retourne les mots du fichier sans doublons et sans ponctuation
            if (fileExists(folder + fileName + ".txt")) {
                return readFile(folder + fileName + ".txt");
            }
        }

        return new String[0];
    }

    // Retourne le tableau des mots du fichier texte sans doublons, ni ponctuations
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
    // sans doublons, sans ponctuation et sans espaces
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
                // On traite les 2 caractères spéciaux "æ" et "œ"
            } else if (c == 'æ') {
                mot += "ae";
            } else if (c == 'œ') {
                mot += "oe";
            } else {
                // On ajoute le mot seulement si mot n'est pas vide et que mot n'est pas déjà contenu dans le tableau
                if (mot.length() != 0 && !isIncludedInTable(mot, mots)) {
                    mots = addOnBottomOfTable(mots, mot);
                }

                // Le dernier caractère du mot a été atteint donc on réinitialise mot pour le prochain mot
                mot = "";
            }
        }

        return mots;
    }

    // Variante de la méthode ci-après, retourne le tableau de chaines de caractères passé en paramètres (utilisé pour alléger le code)
    static String[] createTable(String... content) {
        return content;
    }

    // Retourne le tableau de int passé en paramètres (utilisé pour alléger le code)
    static int[] createTable(int... content) {
        return content;
    }

    // Variante de la méthode ci-après avec des nombres au lieu de chaines de caractères
    static int[] shuffleTable(int[] table) {
        for (int i = 0; i < table.length; i++) {
            int j = generateRandomInt(0, table.length - 1);
            int temp = table[j];
            table[j] = table[i];
            table[i] = temp;
        }
        return table;
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

    // Variante de la méthode ci-après avec des entiers et non des chaines de caractères
    static boolean isIncludedInTable(int search, int[] table) {
        for (int i : table) {
            if (i == search) {
                return true;
            }
        }

        return false;
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

    // Retourne un tableau au contenu identique à celui entré en paramètres mais avec item en plus en premier index
    static String[] addOnTopOfTable(String[] table, String item) {
        String[] result = new String[table.length + 1];

        // On ajoute le premier index
        result[0] = item;

        // On copie l'ancien tableau dans le nouveau
        for (int i = table.length; i > 0; i--) {
            result[i] = table[i - 1];
        }

        return result;
    }

    // Retourne un tableau au contenu identique à celui entré en paramètres mais avec item en plus en dernier index
    static String[] addOnBottomOfTable(String[] table, String item) {
        String[] result = new String[table.length + 1];

        // On copie l'ancien tableau dans le nouveau
        for (int i = 0; i < table.length; i++) {
            result[i] = table[i];
        }

        // On ajoute le dernier index
        result[result.length - 1] = item;

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

    // Retourne le nombre passé en paramètre concaténé avec son suffix
    // (exemple pour 1 -> 1er ou pour 2 -> 2ème)
    static String convertIntToFrenchString(int nb) {
        if (nb > 1) {
            return nb + "ème";
        } else if (nb == 1) {
            return nb + "er";
        } else {
            return nb + "";
        }
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
}
