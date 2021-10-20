import java.io.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Client {
    public static void main(String[] args) throws IOException {

      String currentFolder =  System.getProperty("user.dir");

        DataInputStream in = new DataInputStream(System.in);
        BufferedReader d = new BufferedReader(new InputStreamReader(in));
        System.out.println("Enter file name: ");
        String file = d.readLine();
        file = currentFolder + "\\" + file;
        System.out.println(file);
        Application coucheApplication = Application.getInstance();
        Couche coucheTransport = Transport.getInstance();
        Couche coucheliaison = LiaisonDeDonnees.getInstance();

        coucheApplication.setSuivante(coucheTransport);
        coucheTransport.setSuivante(coucheliaison);

        Couche socketClient = new CouchePhysiqueClient("localhost", 4445);
        coucheliaison.setSuivante(socketClient);

        coucheApplication.envoyerFichier(file);
    }
}
