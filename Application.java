import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class Application extends Couche {
    private static Application _instance;
    private ArrayList<Envoi> listeDesChosesRecus;

    private Application() {
        ArrayList<Envoi> listeDesChosesRecus = new ArrayList<Envoi>();
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
            in.close();

        } catch (FileNotFoundException e1) {
            System.out.println("file not found !");
        } catch (IOException e) {
            System.out.println("file not readable !");
        }

        // this will write or read the file complety and will pass a pointer to the
        // layer transport.

        this.envoyer(new Envoi(stream, Filename.getBytes()));
    }

    @Override
    boolean recevoir(Envoi data) {
        listeDesChosesRecus = new ArrayList<Envoi>();
        listeDesChosesRecus.add(data);

        if (data == null) {
            // String text = new String(bytes, StandardCharsets.UTF_8);
            // TODO FAIRE data =null si infos complet et si connecxtion ferme

            String nomFichierRecu = "cobol.txt";
            try {
                FileOutputStream writer = new FileOutputStream(nomFichierRecu);
                for (Envoi envoi : listeDesChosesRecus) {
                    writer.write(envoi._data);
                }
                writer.close();

            } catch (IOException e) {
                System.out.println("Erreur ecriture du fichier");
            }

        }
        // TODO CHECK this ?? at the end ?
        return true;
    }
}