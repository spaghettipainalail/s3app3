import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.Random;

public class LiaisonDeDonneesConverter {
    public byte[] AddCRC(byte[] data){
        String s = BytesToBinary(data);
        String crc = GetCRC(s);
        //nouveau packets avec crc au début
        byte[] newData = new byte[data.length+4];
        newData[0] = (byte)Integer.parseInt(s.substring(0, 8), 2);
        newData[1] = (byte)Integer.parseInt(s.substring(8, 16), 2);
        newData[2] = (byte)Integer.parseInt(s.substring(16, 24), 2);
        newData[3] = (byte)Integer.parseInt(s.substring(24, 32), 2);
        //les packets déjà présent
        for (int j = 0; j<data.length; j++){
            newData[j+4]=data[j];
        }
        return newData;
    }
    public String StringToBinary(String s) {
        String binary = new BigInteger(s.getBytes()).toString(2);
        return binary;
    }

    public String BytesToString(byte[] bytes) {
        String s = new String(bytes, StandardCharsets.UTF_8);
        return s;
    }

    public String BytesToBinary(byte[] bytes) {
        String s = new String(bytes, StandardCharsets.UTF_8); //byte[] to string
        String binary = new BigInteger(s.getBytes()).toString(2); //string to binary string: "10000110110110101"
        return binary;
    }

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
}
