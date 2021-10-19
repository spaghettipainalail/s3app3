import java.math.BigInteger;
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
        System.out.println("crc dans addCRC: "+crc);
        System.out.println("newdata1: "+newData[0]);
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

    public boolean VerifyCRC(byte[] data, Boolean errors) {
        String binairyData=BytesToBinary(data);
        System.out.println("data dans classe: "+ binairyData);

        byte[] textArray = new byte[data.length-4];
        for (int i=0;i< textArray.length; i++){
            textArray[i]= data[i+4]; //textArray est le contenu sans le crc
        }
        String text = new String(textArray, StandardCharsets.UTF_8);  //le texte sous format "Salut"
        System.out.println("reprise tu texte dans la classe: "+ text);
        String binaryText = new BigInteger(text.getBytes()).toString(2);    //texte sous format "011101101011101001101001"
        System.out.println("string binaire du texte: "+ binaryText);

        byte[] crcArray = new byte[4];
        for (int i=0;i< 4; i++){
            crcArray[i]= data[i];
            System.out.println("crc array: "+ crcArray[i]);
        }
        byte[] test = {-90, -62, -40, -22};
        String test2 = BytesToBinary(test);
        System.out.println("test2: "+test2);
        String bruh = BytesToBinary(crcArray);
        System.out.println("bruh: "+ bruh);
        String realCrcBinaryText = new BigInteger(crcArray).toString(2);
        System.out.println("real crc: "+ realCrcBinaryText);

        String dataString = BytesToBinary(textArray);
        System.out.println("datastring: "+ dataString);
        String testcrc = GetCRC(dataString);
        System.out.println("crc avec get CRC: "+ testcrc);

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

        encoded.append(binaryText).append(testcrc);

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
        System.out.println("encoded: "+encoded);
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
