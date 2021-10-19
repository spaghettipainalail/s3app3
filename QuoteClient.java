import java.io.*;

public class QuoteClient {
    public static void main(String[] args) throws IOException {

        // if (args.length != 1) {
        // System.out.println("Usage: java QuoteClient <hostname>");
        // return;
        // }s
        DataInputStream in = new DataInputStream(System.in);
        BufferedReader d = new BufferedReader(new InputStreamReader(in));
        System.out.println("Enter file name: ");
        String file = d.readLine();

        Application coucheApplication = Application.getInstance();
        Couche coucheTransport = Transport.getInstance();
        Couche coucheliaison = LiaisonDeDonnees.getInstance();

        coucheApplication.setSuivante(coucheTransport);
        coucheTransport.setSuivante(coucheliaison);

        Couche socketClient = new SocketClient("localhost", 4445);
        coucheliaison.setSuivante(socketClient);

        coucheApplication.envoyerFichier(file);
        //"C:\\fred.jpg"
    }
}
