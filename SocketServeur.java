import java.net.InetAddress;
import java.net.SocketException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class SocketServeur extends Couche {
    private int _port;
    Logger logger = new Logger();

    public SocketServeur(int port) {
        _port = port;
    }

    public void starterEcouter() throws SocketException {
        int erreurEnvois = 0;
        int envoisTotal = 0;
        boolean moreQuotes = true;
        DatagramSocket socket = new DatagramSocket(_port);
        System.out.println("Server started ! port 4445");
        while (moreQuotes) {
            try {
                // todo plus grand
                byte[] buf = new byte[246];

                // receive request
                DatagramPacket packet = new DatagramPacket(buf, buf.length);
                socket.receive(packet);

                // System.out.println(new String(packet.getData()));

                // donnees recu par le socket (246 octets)
                Envoi dataRecu = new Envoi(packet.getData());

                Envoi retour;

                boolean resultat = super.recevoir(dataRecu);
                // figure out response
                // TODO
                if (resultat) {
                    logger.logServer("Packet bien recu: envoie d'un packet vide");
                    // confirmer par paquet vide
                    byte[] tab = new byte[246];
                    retour = new Envoi(tab);
                } else {
                    logger.logServer("Erreur de réception, envoie d'un packet pas vide");
                    erreurEnvois += 1;
                    if (erreurEnvois == 3) {
                        moreQuotes = false;
                    }
                    // redemander un nouveau packet
                    dataRecu.decompresser(4); // enlever crc
                    dataRecu.decompresser(4); // get le numero de paquet
                    retour = new Envoi(dataRecu._header);
                }
                // send the response to the client at "address" and "port"
                InetAddress address = packet.getAddress();
                int port = packet.getPort();
                packet = new DatagramPacket(retour._data, retour._data.length, address, port);
                socket.send(packet);
            } catch (Exception e) {
                e.printStackTrace();
                moreQuotes = false;
            }
        }
        socket.close();
    }
}
