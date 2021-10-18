import java.io.*;

public class QuoteServer {
    public static void main(String[] args) throws IOException {


        // TODO 
        Application coucheApplication = new Application();
        Couche coucheTransport = new Transport();
        Couche coucheliaison = new LiaisonDeDonnees();

        coucheApplication.setSuivante(coucheTransport);
        coucheTransport.setSuivante(coucheliaison);

        // coucheliaison.setSuivante(this);
        // la transmission ce lanceras ici ^

        coucheApplication.starter(true, "C:\\fred.jpg");
        new QuoteServerThread().start();
    }
}
