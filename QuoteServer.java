import java.io.*;

public class QuoteServer {
    public static void main(String[] args) throws IOException {

        Couche coucheApplication = new Application(true, "fred.jpg");

        Couche coucheTransport = new Transport();
        coucheApplication.setSuivante(coucheTransport);
        coucheTransport.setPrecedante(coucheApplication);

        Couche coucheliaison = new LiaisonDeDonnees();
        coucheTransport.setSuivante(coucheliaison);
        coucheliaison.setPrecedante(coucheTransport);

        // coucheliaison.setSuivante(this);
        // la transmission ce lanceras ici ^

        new QuoteServerThread().start();
    }
}
