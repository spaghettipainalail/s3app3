import java.io.IOException;
import java.math.BigInteger;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;

public class LiaisonDeDonnees extends Couche {
    public String StringToBinary(String s){
        String binary = new BigInteger(s.getBytes()).toString(2);
        return binary;
    }
    //  implementer un crc dans l'entete
    public String GetCRC (String s){
        String CRCGenerator = "1101";
        int generatorLength = CRCGenerator.length();
        StringBuilder encoded= new StringBuilder();
        encoded.append(s);

        //ajouter un nombre de 0 équivalent à generatorLength-1 (place du crc dans le message)
        for (int i=1; i<=generatorLength-1; i++){
            encoded.append("0");
        }
        //boucler dans les bits et faire les opérations XOR
        for (int i=0; i<=encoded.length()-generatorLength; ){
            for (int j=0; j<generatorLength; j++){
                if (encoded.charAt(i+j)==CRCGenerator.charAt(j)){
                    encoded.setCharAt(i+j, '0');
                }
                else {
                    encoded.setCharAt(i+j, '1');
                }
            }
            if (i<encoded.length() && encoded.charAt(i)!='1'){
                i++;
            }
        }
        return encoded.substring(encoded.length()-generatorLength+1);
    }

    public boolean VerifyCRC(String s, String crc){
        String CRCGenerator = "1101";
        int generatorLength = CRCGenerator.length();
        StringBuilder encoded = new StringBuilder();
        encoded.append(s).append(crc);

        for (int i=0; i<=encoded.length()-generatorLength; ){
            for (int j=0; j<generatorLength; j++){
                if (encoded.charAt(i+j)==CRCGenerator.charAt(j)){
                    encoded.setCharAt(i+j, '0');
                }
                else {
                    encoded.setCharAt(i+j, '1');
                }
            }
            if (i<encoded.length() && encoded.charAt(i)!='1'){
                i++;
            }
        }
        //vérifier si le calcul est bon
        for (int i = 0; i<encoded.length();i++){
            if (encoded.charAt(i)!='0'){
                System.out.println("erreur crc");
                return false;
            }
        }
        System.out.println("good crc");
        return true;
    }
    // le crc est sur toute sauf le crc 
    // polynome generateur IEEE802.3
    // ajouter des stats a la fin d'un transfert sur nombre packets transmis ou recu, nb perdus et nb erreur CRC
    // mettre des logs dans liasonDeDonnes.log de toutes les operations faite, avec le temps
    // ajouter un fonction pour mettre des erreurs

    //écrire header
    //lire header
    public static void main(String[] args) throws IOException {
        String s = "Salut toi";
        LiaisonDeDonnees l = new LiaisonDeDonnees();
        String b =  l.StringToBinary(s);
        System.out.println("As binary: "+b);

        s = "101101110";
        System.out.println("get crc: "+l.GetCRC(s));

        System.out.println("verify crc: "+l.VerifyCRC(s, "011"));

    }
}