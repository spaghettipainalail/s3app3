import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.ByteBuffer;
import java.time.LocalDateTime;

public class LiaisonDeDonnees extends Couche {
    // ajouter des stats a la fin d'un transfert sur nombre packets transmis ou
    // recu, nb perdus et nb erreur CRC
    // done mettre des logs dans liasonDeDonnes.log de toutes les operations faite,
    // avec le temps
    Logger logger = new Logger();

    @Override
    void envoyer(Envoi data) {
        DatagramSocket socket;
        try {
            socket = new DatagramSocket();

            InetAddress address = InetAddress.getByName("localhost");
            byte[] buf = new byte[256];

            byte[] bytes = data.get_data();
            // calculer crc
            LiaisonDeDonneesConverter l = new LiaisonDeDonneesConverter();
            byte[] newPackets = l.AddCRC(bytes);
            // envoyer au serveur
            logger.logClient("paquet" + " envoyé à: " + LocalDateTime.now());
            socket.send(new DatagramPacket(newPackets, newPackets.length, address, 4445));

            // recevoir
            DatagramPacket packet = new DatagramPacket(buf, buf.length);
            socket.receive(packet);

            socket.close();
        } catch (Exception e) {
            System.out.println(e);

        }
        // TODO close socket
    }

    // recevoir
    // verify
    @Override
    public boolean recevoir(Envoi envoi) {
        LiaisonDeDonneesConverter l = new LiaisonDeDonneesConverter();
        boolean verify = l.VerifyCRC(envoi.get_data(), false);
        envoi.decompresser(4); // enlever crc
        envoi.decompresser(4); // get le numero de paquet
        ByteBuffer wrapped = ByteBuffer.wrap(envoi._data); // big-endian by default
        int num = wrapped.getInt();
        if (verify) {
            boolean transportOk = super.recevoir(envoi);
            if (!transportOk) {
                // logger.logServer("paquet #" + num + " contient un erreur de numéro de
                // paquet");
            } else {
                // logger.logServer("paquet #" + num + " bien envoyé");
            }
            return transportOk;

        } else {
            // logger.logServer("paquet #" + num + " contient un erreur de CRC");
            return false;
        }
    }

    // public static void main(String[] args) throws IOException {
    // //tests
    // String s="Salut toi comment ca va tu bien colisss";
    // String binary = new BigInteger(s.getBytes()).toString(2);
    // byte[] bytes = s.getBytes();
    // System.out.println("b4 crc: ");
    // for (int i=0; i<bytes.length; i++){
    // System.out.print(bytes[i]+ ", "); //afficher sous forme 95, 81, 43
    // }

    // LiaisonDeDonneesConverter l = new LiaisonDeDonneesConverter();
    // byte[] withCRC = l.AddCRC(bytes);
    // System.out.println("with crc: ");
    // for (int i=0; i<withCRC.length; i++){
    // System.out.print(withCRC[i]+", "); //afficher sous forme 01100110 11001100
    // }
    // System.out.println();
    // //trouver crc
    // System.out.println("crc avec GetCRC() au debut: "+l.GetCRC(binary));
    // String binairyData=l.BytesToBinary(withCRC);
    // System.out.println("verify crc: " + l.VerifyCRC(withCRC, false));
    // }
}