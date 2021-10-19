import java.io.*;

public class QuoteClient {
    public static void main(String[] args) throws IOException {

        // if (args.length != 1) {
        // System.out.println("Usage: java QuoteClient <hostname>");
        // return;
        // }s

        Application coucheApplication = Application.getInstance();
        Couche coucheTransport = new Transport();
        Couche coucheliaison = new LiaisonDeDonnees();

        coucheApplication.setSuivante(coucheTransport);
        coucheTransport.setSuivante(coucheliaison);

        Couche socketClient = new SocketClient("localhost", 4445);
        coucheliaison.setSuivante(socketClient);

        coucheApplication.starter(true, "C:\\fred.jpg");

    }
}
