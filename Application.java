import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Base64;

public class Application extends Couche {
    private static Application _instance;
    private ArrayList<Envoi> listeDesChosesRecus;

    private Application() {
        listeDesChosesRecus = new ArrayList<Envoi>();
    }

    public static Application getInstance() {
        if (_instance == null)
            _instance = new Application();
        return _instance;
    }

    public void envoyerFichier(String Filename) {

        InputStream inStream = null;
        byte[] stream = null;
        try {
            inStream = new FileInputStream(Filename);
            BufferedInputStream in = new BufferedInputStream(inStream);
            stream = in.readAllBytes();
            //  stream = Base64.getEncoder().encode(stream);
            in.close();

        } catch (FileNotFoundException e1) {
            System.out.println("file not found !");
            System.exit(1);
        } catch (IOException e) {
            System.out.println("file not readable !");
            System.exit(1);

        }

        // this will write or read the file complety and will pass a pointer to the
        // layer transport.

        this.envoyer(new Envoi(stream, Filename.getBytes()));
    }

    @Override
    boolean recevoir(Envoi data) {
        listeDesChosesRecus.add(data);
        Paquet packetRecu = new Paquet(data);
        if (packetRecu.get_numPaquetFin() == packetRecu.get_numPaquet()) {
            // String text = new String(bytes, StandardCharsets.UTF_8);
            // TODO FAIRE data =null si infos complet et si connecxtion ferme

            Paquet paquet1 = new Paquet(listeDesChosesRecus.get(0));
            String nomFichierRecu = new String(paquet1.get_data()).replaceAll("\0", "");

            try {
                FileOutputStream writer = new FileOutputStream(nomFichierRecu);

                byte[] grosChunk = new byte[0];
                for (Envoi envoi : listeDesChosesRecus) {
                    byte[] newGrosChunk = new byte[grosChunk.length + envoi._data.length];
                    System.arraycopy(grosChunk, 0, newGrosChunk, 0, grosChunk.length);
                    System.arraycopy(envoi._data, 0, newGrosChunk, grosChunk.length, envoi._data.length);

                    grosChunk = newGrosChunk;

                }
                // grosChunk = Base64.getDecoder().decode(grosChunk);
                writer.write(grosChunk);
                writer.close();
                System.out.println("Fichier bien recu !");
                System.exit(0);

            } catch (Exception e) {
                System.out.println("Erreur ecriture du fichier recu");
            }

        }

        return true;
    }
}