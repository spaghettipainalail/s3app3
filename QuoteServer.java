import java.io.*;

public class QuoteServer {
    public static void main(String[] args) throws IOException {

        Couche liaison = LiaisonDeDonnees.getInstance();
        Couche transport = Transport.getInstance();
        Couche application = Application.getInstance();
        Couche socketServeur = new SocketServeur(4445);

        socketServeur.setPrecedente(liaison);
        liaison.setPrecedente(transport);
        transport.setPrecedente(application);

        new QuoteServerThread((SocketServeur) socketServeur).start();
    }
}
