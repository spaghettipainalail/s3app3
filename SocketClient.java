import java.net.InetAddress;
import java.util.Arrays;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class SocketClient extends Couche {
    private String _adress;
    private int _port;

    public SocketClient(String InetAddress, int port) {
        _adress = InetAddress;
        _port = port;
    }

    @Override
    public void envoyer(Envoi data) {
        try {
            DatagramSocket socket = new DatagramSocket();
            InetAddress address = InetAddress.getByName(_adress);

            int restant = data._data.length;
            int i = 0;
            while (restant > 0) {

                socket.send(new DatagramPacket(Arrays.copyOfRange(data._data, i * 200, 200), 200, address, _port));

                restant -= 200;
                i++;
            }
            // DatagramPacket packet = new DatagramPacket(buf, buf.length, address, 4445);
            // socket.send(packet);

            // get response

            // display response
            // String received = new String(packet.getData(), 0, packet.getLength());
            // System.out.println("Quote of the Moment: " + received);

            socket.close();
        } catch (Exception e) {
            System.out.println(e);
        }

    }

    public void handle(Dataframe data) {
        try {
            DatagramSocket socket = new DatagramSocket();

            byte[] buf = new byte[200];
            InetAddress address = InetAddress.getByName("localhost");
            for (int i = 0; i < data.getNbPackets(); i++) {
                socket.send(new DatagramPacket(data.getPaquet(i).get_data(), data.getPaquet(i).get_data().length, address, 4445));
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
