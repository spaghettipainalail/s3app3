import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.Random;

public class LiaisonDeDonneesConverter {

    /**
     * Retourne un CRC
     * @param data byte[] auquel ajouter un CRC
     * @return un byte[] contenant le CRC
     */
    public byte[] AddCRC(byte[] data) {
        String s = BytesToBinary(data);
        String crc = GetCRC(s);
        byte[] newData = new byte[4];
        newData[0] = (byte) Integer.parseInt(crc.substring(0, 8), 2);
        newData[1] = (byte) Integer.parseInt(crc.substring(8, 16), 2);
        newData[2] = (byte) Integer.parseInt(crc.substring(16, 24), 2);
        newData[3] = (byte) Integer.parseInt(crc.substring(24, 32), 2);

        return newData;
    }

    /**
     * Change des byte[] en string binaire
     * @param bytes le data à changer
     * @return  la string binaire
     */
    public String BytesToBinary(byte[] bytes) {
        String s = new String(bytes, StandardCharsets.UTF_8); // byte[] to string
        String binary = new BigInteger(s.getBytes()).toString(2); // string to binary string: "10000110110110101"
        return binary;
    }

    /**
     * Get un CRC
     * @param s String binaire
     * @return le CRC
     */
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

    /**
     * Vérificaiton du CRC
     * @param data  le data comprenant le CRC au début
     * @param errors si on veut inclure des erreurs aléatoires représentant des problèmes physiques
     * @return true si le CRC est valide
     */
    public boolean VerifyCRC(byte[] data, Boolean errors) {
        // retrouver le texte
        byte[] textArray = new byte[data.length - 4];
        for (int i = 0; i < textArray.length; i++) {
            textArray[i] = data[i + 4]; // textArray est le contenu sans le crc
        }
        String text = new String(textArray, StandardCharsets.UTF_8); // texte en clair
        String binaryText = new BigInteger(text.getBytes()).toString(2); // texte sous format "011101101011101001101001"

        // retrouver le crc
        byte[] crcArray = new byte[4]; // array qui contient les 4 bytes du CRC
        for (int i = 0; i < 4; i++) {
            crcArray[i] = data[i];
        }
        String s1 = String.format("%8s", Integer.toBinaryString(crcArray[0] & 0xFF)).replace(' ', '0');
        String s2 = String.format("%8s", Integer.toBinaryString(crcArray[1] & 0xFF)).replace(' ', '0');
        String s3 = String.format("%8s", Integer.toBinaryString(crcArray[2] & 0xFF)).replace(' ', '0');
        String s4 = String.format("%8s", Integer.toBinaryString(crcArray[3] & 0xFF)).replace(' ', '0');
        String bestCRC = s1 + s2 + s3 + s4; // le crc

        // génération d'erreurs au besoin
        if (errors) {
            Random rand = new Random();
            int n = rand.nextInt(10);
            if (n == 5) {
                return false;
            }
        }

        // Préparation à la vérification
        String CRCGenerator = "100000100110000010001110110110111";
        int generatorLength = CRCGenerator.length();
        StringBuilder encoded = new StringBuilder();
        encoded.append(binaryText).append(bestCRC);

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
}
