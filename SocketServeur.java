import java.net.InetAddress;
import java.net.SocketException;
import java.util.Arrays;
import java.io.BufferedReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class SocketServeur extends Couche {
    private int _port;

    public SocketServeur(int port) {
        _port = port;
    }

    public void starterEcouter() throws SocketException {
        int erreurEnvois = 0;
        boolean moreQuotes = true;
        DatagramSocket socket = new DatagramSocket(4445);
        System.out.println("Server started ! port 4445");
        while (moreQuotes) {
            try {
                // todo plus grand
                byte[] buf = new byte[250];

                // receive request
                DatagramPacket packet = new DatagramPacket(buf, buf.length);
                socket.receive(packet);

                System.out.println(new String(packet.getData()));

                Envoi envoi = new Envoi(packet.getData());
                Envoi retour;

                boolean resultat = super.recevoir(envoi);
                // figure out response
                // TODO
                if (resultat){
                    //confirmer par paquet vide
                    byte[] tab = null;
                    retour = new Envoi(tab);
                }
                else{
                    erreurEnvois += 1;
                    if (erreurEnvois == 3){
                        moreQuotes=false;
                    }
                    //redemander un nouveau packet
                    envoi.decompresser(4); //enlever crc
                    envoi.decompresser(4); //get le numero de paquet
                    retour = new Envoi(envoi._header);
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

    public void handle(Dataframe data) {
        try {
            DatagramSocket socket = new DatagramSocket();

            byte[] buf = new byte[200];
            InetAddress address = InetAddress.getByName("localhost");
            for (int i = 0; i < data.getNbPackets(); i++) {
                socket.send(new DatagramPacket(data.getPaquet(i).get_data(), data.getPaquet(i).get_data().length,
                        address, 4445));
                DatagramPacket packet = new DatagramPacket(buf, buf.length);
                socket.receive(packet);
                String received = new String(packet.getData(), 0, packet.getLength());
                System.out.println("Quote of the Moment: " + received);
            }
            socket.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

}
