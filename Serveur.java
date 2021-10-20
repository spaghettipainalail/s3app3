import java.io.*;

public class Serveur {
    public static void main(String[] args) throws IOException {

        Couche liaison = LiaisonDeDonnees.getInstance();
        Couche transport = Transport.getInstance();
        Couche application = Application.getInstance();
        Couche socketServeur = new CouchePhysiqueServeur(4445);

        socketServeur.setPrecedente(liaison);
        liaison.setPrecedente(transport);
        transport.setPrecedente(application);

        new ServeurThread((CouchePhysiqueServeur) socketServeur).start();
    }
}
