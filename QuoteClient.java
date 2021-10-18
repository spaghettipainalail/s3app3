import java.io.*;
import java.net.*;
public class QuoteClient {
    public static void main(String[] args) throws IOException {

        // if (args.length != 1) {
        //      System.out.println("Usage: java QuoteClient <hostname>");
        //      return;
        // }s

        Application coucheApplication = new Application();
        Couche coucheTransport = new Transport();
        Couche coucheliaison = new LiaisonDeDonnees();

        coucheApplication.setSuivante(coucheTransport);
        coucheTransport.setSuivante(coucheliaison);

        coucheApplication.starter(true, "C:\\fred.jpg");

    }
}
