import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Random;

public class LiaisonDeDonneesConverter {
    public byte[] AddCRC(byte[] data){
        String s = BytesToBinary(data);
        String crc = GetCRC(s);
        System.out.println();
        //nouveau packets avec crc au début
        byte[] newData = new byte[data.length+4];
        newData[0] = (byte)Integer.parseInt(crc.substring(0, 8), 2);
        newData[1] = (byte)Integer.parseInt(crc.substring(8, 16), 2);
        newData[2] = (byte)Integer.parseInt(crc.substring(16, 24), 2);
        newData[3] = (byte)Integer.parseInt(crc.substring(24, 32), 2);

        //long foo = BinaryToLong(crc);
        //System.out.println("maybe: "+Long.toBinaryString(foo));

        //les packets déjà présent
        for (int j = 0; j<data.length; j++){
            newData[j+4]=data[j];
        }
        return newData;
    }

    public static final byte[] longToByteArray(long value) {
        return new byte[] {
                (byte)(value >>> 24),
                (byte)(value >>> 16),
                (byte)(value >>> 8),
                (byte)value};
    }
    public static final byte[] intToByteArray(int value) {
        return new byte[] {
                (byte)(value >>> 24),
                (byte)(value >>> 16),
                (byte)(value >>> 8),
                (byte)value};
    }

    public long BinaryToLong(String binary) {
        char[] numbers = binary.toCharArray();
        long result = 0;
        for(int i=numbers.length - 1; i>=0; i--)
            if(numbers[i]=='1')
                result += Math.pow(2, (numbers.length-i - 1));
        return result;
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

    public boolean VerifyCRC(byte[] data, Boolean errors) {
        //retrouver le texte
        byte[] textArray = new byte[data.length-4];
        for (int i=0;i< textArray.length; i++){
            textArray[i]= data[i+4]; //textArray est le contenu sans le crc
        }
        String text = new String(textArray, StandardCharsets.UTF_8);  //texte en clair
        String binaryText = new BigInteger(text.getBytes()).toString(2);    //texte sous format "011101101011101001101001"

        //retrouver le crc
        byte[] crcArray = new byte[4]; //array qui contient les 4 bytes du CRC
        for (int i=0;i< 4; i++){
            crcArray[i]= data[i];
        }
        String s1=String.format("%8s", Integer.toBinaryString(crcArray[0] & 0xFF)).replace(' ', '0');
        String s2=String.format("%8s", Integer.toBinaryString(crcArray[1] & 0xFF)).replace(' ', '0');
        String s3=String.format("%8s", Integer.toBinaryString(crcArray[2] & 0xFF)).replace(' ', '0');
        String s4=String.format("%8s", Integer.toBinaryString(crcArray[3] & 0xFF)).replace(' ', '0');
        String bestCRC= new String(s1+s2+s3+s4); //le crc

        //génération d'erreurs au besoin
        if (errors) {
            Random rand = new Random();
            int n = rand.nextInt(10);
            if (n == 5) {
                return false;
            }
        }

        //Préparation à la vérification
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
        System.out.println("resultat du calcul: "+encoded);
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
