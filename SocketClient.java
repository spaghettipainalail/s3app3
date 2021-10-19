import java.net.InetAddress;
import java.util.Arrays;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.Objects;

public class SocketClient extends Couche {
    // TODO rename couche physique
    private String _adress;
    private int _port;

    public SocketClient(String InetAddress, int port) {
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
            if (Arrays.equals(packet.getData(), buf2))
                return true;

            // display response
            // String received = new String(packet.getData(), 0, packet.getLength());
            // System.out.println("Quote of the Moment: " + received);

            socket.close();
        } catch (Exception e) {
            System.out.println(e);
            return false;
        }
        return true;
    }
}
