import java.net.InetAddress;
import java.net.SocketException;
import java.util.Arrays;
import java.io.BufferedReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class SocketServeur extends Couche {
    private String _adress;
    private int _port;

    public SocketServeur(int port) {
        _port = port;
    }

    public void starterEcouter() throws SocketException {
        boolean moreQuotes = true;
        DatagramSocket socket = new DatagramSocket(4445);
        System.out.println("Server started ! port 4445");
        while (moreQuotes) {
            try {
                // todo plus grand
                byte[] buf = new byte[200];

                // receive request
                DatagramPacket packet = new DatagramPacket(buf, buf.length);
                socket.receive(packet);

                System.out.println(new String(packet.getData()));

                super.recevoir(new Envoi(packet.getData()));
                // figure out response

                // send the response to the client at "address" and "port"
                InetAddress address = packet.getAddress();
                int port = packet.getPort();
                packet = new DatagramPacket(buf, buf.length, address, port);
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
