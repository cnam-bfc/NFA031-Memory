package nfa031.memory;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Lecteur {
    public static String LireTexte(String filename) throws FileNotFoundException, IOException {
        FileReader fichierTexte = new FileReader(filename);
        String texte = "";
        BufferedReader entree = new BufferedReader(fichierTexte);

        String ligne;
        try {
            do {
                ligne = entree.readLine();
                if (ligne != null) {
                    texte = texte + ligne + "\n";
                }
            } while (ligne != null);
        } catch (Throwable var7) {
            try {
                entree.close();
            } catch (Throwable var6) {
                var7.addSuppressed(var6);
            }

            throw var7;
        }

        entree.close();
        return texte;
    }
}
