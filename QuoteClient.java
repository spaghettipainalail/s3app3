import java.io.*;
import java.net.*;
public class QuoteClient {
    public static void main(String[] args) throws IOException {

        // if (args.length != 1) {
        //      System.out.println("Usage: java QuoteClient <hostname>");
        //      return;
        // }s

        DatagramSocket socket = new DatagramSocket();


        byte[] buf = new byte[256];
        InetAddress address = InetAddress.getByName("localhost");
        DatagramPacket packet = new DatagramPacket(buf, buf.length, address, 4445);
        socket.send(packet);
    
            // get response
        packet = new DatagramPacket(buf, buf.length);
        socket.receive(packet);

	    // display response
        String received = new String(packet.getData(), 0, packet.getLength());
        System.out.println("Quote of the Moment: " + received);
    
        socket.close();
    }
}
