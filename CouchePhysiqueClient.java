import java.net.InetAddress;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.Objects;

public class CouchePhysiqueClient extends Couche {
    private final String _adress;
    private final int _port;
    private final Logger logger = new Logger();

    public CouchePhysiqueClient(String InetAddress, int port) {
        _adress = InetAddress;
        _port = port;
    }

    @Override
    public boolean envoyer(Envoi data) {
        try {
            DatagramSocket socket = new DatagramSocket();
            InetAddress address = InetAddress.getByName(_adress);

            socket.send(new DatagramPacket(data._data, data._data.length, address, _port));
            // DatagramPacket packet = new DatagramPacket(buf, buf.length, address, 4445);
            // socket.send(packet);

            // get response
            byte[] buf = new byte[246];
            byte[] buf2 = new byte[246];
            DatagramPacket packet = new DatagramPacket(buf, buf.length);
            socket.receive(packet);
            if (Arrays.equals(packet.getData(), buf2)){
                logger.logClient("paquet bien envoyé à: " + LocalDateTime.now());
                return true;
            }


            // display response
            // String received = new String(packet.getData(), 0, packet.getLength());
            // System.out.println("Quote of the Moment: " + received);

            socket.close();
        } catch (Exception e) {
            System.out.println(e);
            logger.logClient("Exception à: " + LocalDateTime.now());
            return false;
        }
        logger.logClient("Mauvaise réception: " + LocalDateTime.now());
        return false;
    }
}
