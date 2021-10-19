import java.net.InetAddress;
import java.util.Arrays;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

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

    public void handle(Dataframe data) {
        try {
            DatagramSocket socket = new DatagramSocket();

            byte[] buf = new byte[200];
            InetAddress address = InetAddress.getByName("localhost");
            for (int i = 0; i < data.getNbPackets(); i++) {
                byte[] pack = data.getPaquet(i).getDataInBytes();
                socket.send(new DatagramPacket(pack, pack.length, address, 4445));
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
