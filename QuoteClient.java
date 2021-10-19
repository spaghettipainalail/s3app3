import java.io.*;

public class QuoteClient {
    public static void main(String[] args) throws IOException {

        // if (args.length != 1) {
        // System.out.println("Usage: java QuoteClient <hostname>");
        // return;
        // }
        int a = Integer.MAX_VALUE;
        String f = "size:" + a;
        int longueur = f.getBytes().length;
        System.out.println(longueur);
        Application coucheApplication = Application.getInstance();
        Couche coucheTransport = Transport.getInstance();
        Couche coucheliaison = new LiaisonDeDonnees();

        coucheApplication.setSuivante(coucheTransport);
        coucheTransport.setSuivante(coucheliaison);

        Couche socketClient = new SocketClient("localhost", 4445);
        coucheliaison.setSuivante(socketClient);

        coucheApplication.envoyerFichier("C:\\fred.jpg");

    }
}
