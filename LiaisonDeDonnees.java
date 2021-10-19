import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
// import java.net.DatagramPacket;
// import java.net.DatagramSocket;
// import java.net.InetAddress;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
// import java.time.LocalDateTime;
// import java.util.List;
import java.util.Random;

public class LiaisonDeDonnees extends Couche {
    // ajouter des stats a la fin d'un transfert sur nombre packets transmis ou
    // recu, nb perdus et nb erreur CRC
    // done mettre des logs dans liasonDeDonnes.log de toutes les operations faite, avec le temps
    // done ajouter un fonction pour mettre des erreurs

    public void log(String s) {
        // creer le fichier au besoin
        try {
            File myObj = new File("liasonDeDonnes.log");
            if (myObj.createNewFile()) {
                System.out.println("File created: " + myObj.getName());
            } else {
                System.out.println("File already exists.");
            }
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        // write
        try {
            FileWriter myWriter = new FileWriter("liasonDeDonnes.log", true);
            myWriter.append(s).append("\n");
            myWriter.close();
            System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    @Override
    void envoyer(Envoi data) {
        try {
            DatagramSocket socket = new DatagramSocket();
            InetAddress address = InetAddress.getByName("localhost");
            byte[] buf = new byte[256];
            for(int i=0; i<data.get_data().length; i++){
                byte[] bytes = data.get_data();
                //calculer crc
                LiaisonDeDonneesConverter l = new LiaisonDeDonneesConverter();
                byte[] newPackets = l.AddCRC(bytes);
                //envoyer au serveur
                log("paquet #"+ i + " envoyé à: "+ LocalDateTime.now());
                socket.send(new DatagramPacket(newPackets, newPackets.length, address, 4445));

                //recevoir
                DatagramPacket packet = new DatagramPacket(buf, buf.length);
                socket.receive(packet);
            }
        } catch (Exception e){
            System.out.println(e);
        }

    }

    public static void main(String[] args) throws IOException {
        //tests
        String s="Salut toi";
        String binary = new BigInteger(s.getBytes()).toString(2);
        System.out.println("String binaire du texte: "+ binary);
        byte[] bytes = s.getBytes();
        System.out.println("b4 crc: ");
        for (int i=0; i<bytes.length; i++){
            System.out.print(bytes[i]+ ", "); //afficher sous forme 95, 81, 43
        }
        System.out.println();

        LiaisonDeDonneesConverter l = new LiaisonDeDonneesConverter();
        byte[] withCRC = l.AddCRC(bytes);
        System.out.println("with crc: ");
        for (int i=0; i<withCRC.length; i++){
            System.out.print(withCRC[i]+", "); //afficher sous forme 01100110 11001100
        }
        System.out.println();
        //trouver crc
        System.out.println("crc avec GetCRC() au debut: "+l.GetCRC(binary));
        String binairyData=l.BytesToBinary(withCRC);
        System.out.println("tableau de data avec crc en string binaire: "+ binairyData);
        System.out.println("verify crc: " + l.VerifyCRC(withCRC, false));
    }
}