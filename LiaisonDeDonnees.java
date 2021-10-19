import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
// import java.net.DatagramPacket;
// import java.net.DatagramSocket;
// import java.net.InetAddress;
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
    void handle(Dataframe data) {
        for(int i=0; i<data.getPaquets().size(); i++){
            byte[] bytes = data.getPaquets().get(i).get_data();
            //calculer crc
            LiaisonDeDonneesConverter l = new LiaisonDeDonneesConverter();
            byte[] newPackets = l.AddCRC(bytes);
            //update pour set le nouveau data avec le crc
            data.getPaquets().get(i).set_data(newPackets);
            //envoyer au serveur
            log("paquet #"+ i + " envoyé à: "+ LocalDateTime.now());
        }
        super.handle(data);
    }

    public static void main(String[] args) throws IOException {
        //tests
        String s="Salut toi";
        byte[] bytes = s.getBytes();
        for (int i=0; i<bytes.length; i++){
            System.out.println(bytes[i]); //afficher sous forme 01100110 11001100
        }
        System.out.println("^^^^^^^^^^ b4 crc ^^^^^^^^^^");
        System.out.println("avec");
        LiaisonDeDonneesConverter l = new LiaisonDeDonneesConverter();
        byte[] withCRC = l.AddCRC(bytes);
        for (int i=0; i<withCRC.length; i++){
            System.out.println(withCRC[i]); //afficher sous forme 01100110 11001100
        }
        //trouver crc
        System.out.println("crc: "+l.GetCRC(s));
        System.out.println("verify crc: " + l.VerifyCRC(s, "10100010111100110011011001101000", false));
    }
}