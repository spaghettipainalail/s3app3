import java.io.*;
import java.net.Socket;

public class QuoteServer {
    public static void main(String[] args) throws IOException {

        Couche liaison = new LiaisonDeDonnees();
        Couche transport = new Transport();
        Couche application = new Application();
        Couche socketServeur = new SocketServeur(4445);

        socketServeur.setPrecedente(liaison);
        liaison.setPrecedente(transport);
        transport.setPrecedente(application);

        new QuoteServerThread((SocketServeur) socketServeur).start();
    }
}
