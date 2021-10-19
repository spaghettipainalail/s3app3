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
    public String StringToBinary(String s) {
        String binary = new BigInteger(s.getBytes()).toString(2);
        return binary;
    }

    public String BytesToString(byte[] bytes) {
        String s = new String(bytes, StandardCharsets.UTF_8);
        return s;
    }

    public String BytesToBinary(byte[] bytes) {
        String s = new String(bytes, StandardCharsets.UTF_8); // byte[] to string
        String binary = new BigInteger(s.getBytes()).toString(2); // string to binary string: "10000110110110101"
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

    void handle(Dataframe data) {
        for (int i = 0; i < data.getPaquets().size(); i++) {
            // calculer crc
            byte[] bytes = data.getPaquets().get(i).get_data();
            byte[] numeroPaquet = ByteBuffer.allocate(4).putInt(data.getPaquets().get(i).get_numPaquet()).array();
            byte[] numeroPaquetFin = ByteBuffer.allocate(4).putInt(data.getPaquets().get(i).get_numPaquetFin()).array();
            String s = BytesToBinary(bytes);
            String crc = GetCRC(s);
            // nouveau packets avec crc au début
            byte[] newPackets = new byte[bytes.length + 4];
            newPackets[0] = (byte) Integer.parseInt(s.substring(0, 8), 2);
            newPackets[1] = (byte) Integer.parseInt(s.substring(8, 16), 2);
            newPackets[2] = (byte) Integer.parseInt(s.substring(16, 24), 2);
            newPackets[3] = (byte) Integer.parseInt(s.substring(24, 32), 2);
            // les packets déjà présent
            for (int j = 0; j < bytes.length; j++) {
                newPackets[j + 4] = bytes[j];
            }
            // update pour set le nouveau data avec le crc
            data.getPaquets().get(i).set_data(newPackets);
            // envoyer au serveur
            log("paquet #" + i + " envoyé à: " + LocalDateTime.now());

    @Override
    void envoyer(Envoi data) {
        try {
            DatagramSocket socket = new DatagramSocket();
            InetAddress address = InetAddress.getByName("localhost");
            byte[] buf = new byte[256];
            for (int i = 0; i < data.get_data().length; i++) {
                byte[] bytes = data.get_data();
                // calculer crc
                LiaisonDeDonneesConverter l = new LiaisonDeDonneesConverter();
                byte[] newPackets = l.AddCRC(bytes);
                // envoyer au serveur
                log("paquet #" + i + " envoyé à: " + LocalDateTime.now());
                socket.send(new DatagramPacket(newPackets, newPackets.length, address, 4445));

                // recevoir
                DatagramPacket packet = new DatagramPacket(buf, buf.length);
                socket.receive(packet);
            }
        } catch (Exception e) {
            System.out.println(e);
        }

    }

    public static void main(String[] args) throws IOException {
        // tests
        String s = "Salut toi";
        String binary = new BigInteger(s.getBytes()).toString(2);
        System.out.println("String binaire du texte: " + binary);
        byte[] bytes = s.getBytes();
        System.out.println("b4 crc: ");
        for (int i = 0; i < bytes.length; i++) {
            System.out.print(bytes[i] + ", "); // afficher sous forme 95, 81, 43
        }
        System.out.println();

        LiaisonDeDonneesConverter l = new LiaisonDeDonneesConverter();
        byte[] withCRC = l.AddCRC(bytes);
        System.out.println("with crc: ");
        for (int i = 0; i < withCRC.length; i++) {
            System.out.print(withCRC[i] + ", "); // afficher sous forme 01100110 11001100
        }
        System.out.println();
        // trouver crc
        System.out.println("crc avec GetCRC() au debut: " + l.GetCRC(binary));
        String binairyData = l.BytesToBinary(withCRC);
        System.out.println("tableau de data avec crc en string binaire: " + binairyData);
        System.out.println("verify crc: " + l.VerifyCRC(withCRC, false));
    }
}