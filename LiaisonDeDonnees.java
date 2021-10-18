import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;
import java.util.Random;

public class LiaisonDeDonnees extends Couche {
    public String StringToBinary(String s) {
        String binary = new BigInteger(s.getBytes()).toString(2);
        return binary;
    }

    // implementer un crc dans l'entete
    public String GetCRC(String s) {
        String CRCGenerator = "100000100110000010001110110110111";
        int generatorLength = CRCGenerator.length();
        StringBuilder encoded = new StringBuilder();
        encoded.append(s);

        // ajouter un nombre de 0 équivalent à generatorLength-1 (place du crc dans le
        // message)
        for (int i = 1; i <= generatorLength - 1; i++) {
            encoded.append("0");
        }
        // boucler dans les bits et faire les opérations XOR
        for (int i = 0; i <= encoded.length() - generatorLength;) {
            for (int j = 0; j < generatorLength; j++) {
                if (encoded.charAt(i + j) == CRCGenerator.charAt(j)) {
                    encoded.setCharAt(i + j, '0');
                } else {
                    encoded.setCharAt(i + j, '1');
                }
            }
            if (i < encoded.length() && encoded.charAt(i) != '1') {
                i++;
            }
        }
        return encoded.substring(encoded.length() - generatorLength + 1);
    }

    public boolean VerifyCRC(String s, String crc, Boolean errors) {
        if (errors) {
            Random rand = new Random();
            int n = rand.nextInt(10);
            if (n == 5) {
                return false;
            }
        }
        String CRCGenerator = "100000100110000010001110110110111";
        int generatorLength = CRCGenerator.length();
        StringBuilder encoded = new StringBuilder();
        encoded.append(s).append(crc);

        for (int i = 0; i <= encoded.length() - generatorLength;) {
            for (int j = 0; j < generatorLength; j++) {
                if (encoded.charAt(i + j) == CRCGenerator.charAt(j)) {
                    encoded.setCharAt(i + j, '0');
                } else {
                    encoded.setCharAt(i + j, '1');
                }
            }
            if (i < encoded.length() && encoded.charAt(i) != '1') {
                i++;
            }
        }
        // vérifier si le calcul est bon
        for (int i = 0; i < encoded.length(); i++) {
            if (encoded.charAt(i) != '0') {
                System.out.println("erreur crc");
                return false;
            }
        }
        System.out.println("good crc");
        return true;
    }
    // done le crc est sur toute sauf le crc
    // done polynome generateur IEEE802.3
    // ajouter des stats a la fin d'un transfert sur nombre packets transmis ou
    // recu, nb perdus et nb erreur CRC
    // done mettre des logs dans liasonDeDonnes.log de toutes les operations faite,
    // avec le temps
    // done ajouter un fonction pour mettre des erreurs

    @Override
    void handle(Dataframe data) {
        try {
            DatagramSocket socket = new DatagramSocket();

            byte[] buf = new byte[256];
            InetAddress address = InetAddress.getByName("localhost");
            for (int i = 0; i < data.getNbPackets(); i++) {
                socket.send(new DatagramPacket(data.getPaquet(i).getData(), 200, address, 4445));
            }
            // DatagramPacket packet = new DatagramPacket(buf, buf.length, address, 4445);
            // socket.send(packet);

            // get response
            packet = new DatagramPacket(buf, buf.length);
            socket.receive(packet);

            // display response
            String received = new String(packet.getData(), 0, packet.getLength());
            System.out.println("Quote of the Moment: " + received);

            socket.close();
        } catch (Exception e) {
            System.out.println(e);
        }

    }

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

    // écrire header
    // lire header
    public static void main(String[] args) throws IOException {
        String s = "Salut toi";
        LiaisonDeDonnees l = new LiaisonDeDonnees();
        String b = l.StringToBinary(s);
        System.out.println("As binary: " + b);

        s = "10100110110000101101100011101010111010000100000011101000110111101101001";
        System.out.println("get crc: " + l.GetCRC(s));

        System.out.println("verify crc: " + l.VerifyCRC(s, "11110111000111110100101100010011", false));
        // exemple`101101110 devrait avoir 011 comme crc
        // inverse:System.out.println(Byte.parseByte("01100110", 2));
        l.log("yeeta");
    }
}