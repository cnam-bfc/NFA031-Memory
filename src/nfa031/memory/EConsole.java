package nfa031.memory;

/* La classe Console fourni des méthodes permettant de lire des données au clavier
Méthode lireString () --> Lecture d'une chaine
Méthode lireFloat () --> Lecture d'une variable de type float
Méthode lireDouble () --> Lecture d'une variable de type double
Méthode lireInt () --> Lecture d'une variable de type int
Méthode lireLong () --> Lecture d'une variable de type long
Méthode lireShort () --> Lecture d'une variable de type short
Méthode lirebyte () --> Lecture d'une variable de type byte
Méthode lireChar () --> Lecture d'une variable de type char

 */
import java.io.*;

public class EConsole {

// classe fournissant des fonctions de lecture au clavier -
    public static String lireString() throws IOException // lecture d'une chaine
    {
        String ligne_lue = null;
        InputStreamReader lecteur = new InputStreamReader(System.in);
        BufferedReader entree = new BufferedReader(lecteur);
        ligne_lue = entree.readLine();

        return ligne_lue;
    }

    public static float lireFloat() throws IOException, NumberFormatException // lecture d'un float
    {
        float x = 0;   // valeur a lire		
        String ligne_lue = lireString();
        x = Float.parseFloat(ligne_lue);

        return x;
    }

    public static double lireDouble() throws IOException, NumberFormatException // lecture d'un double
    {
        double x = 0;   // valeur a lire
        String ligne_lue = lireString();
        x = Double.parseDouble(ligne_lue);

        return x;
    }

    public static int lireInt() throws IOException, NumberFormatException // lecture d'un int
    {
        int n = 0;   // valeur a lire
        String ligne_lue = lireString();
        n = Integer.parseInt(ligne_lue);

        return n;
    }

    public static long lireLong() throws IOException, NumberFormatException // lecture d'un long
    {
        long l = 0;   // valeur a lire
        String ligne_lue = lireString();
        l = Long.parseLong(ligne_lue);

        return l;
    }

    public static short lireShort() throws IOException, NumberFormatException // lecture d'un short
    {
        short s = 0;   // valeur a lire
        String ligne_lue = lireString();
        s = Short.parseShort(ligne_lue);

        return s;
    }

    public static byte lireByte() throws IOException, NumberFormatException // lecture d'un byte
    {
        byte b = 0;   // valeur a lire
        String ligne_lue = lireString();
        b = Byte.parseByte(ligne_lue);

        return b;
    }

    public static char lireChar() throws IOException, NumberFormatException // lecture d'un char
    {
        char c = '\0';   // valeur a lire        
        String ligne_lue = lireString();
        c = ligne_lue.charAt(0);

        return c;
    }
}
