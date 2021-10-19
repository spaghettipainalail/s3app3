import java.nio.ByteBuffer;
import java.time.LocalDateTime;

public class LiaisonDeDonnees extends Couche {
    private static LiaisonDeDonnees _instance;

    private LiaisonDeDonnees() {
    }

    public static LiaisonDeDonnees getInstance() {
        if (_instance == null)
            _instance = new LiaisonDeDonnees();
        return _instance;
    }

    // ajouter des stats a la fin d'un transfert sur nombre packets transmis ou
    // recu, nb perdus et nb erreur CRC
    // done mettre des logs dans liasonDeDonnes.log de toutes les operations faite,
    // avec le temps
    Logger logger = new Logger();
    public int totalEnvois = 0;
    public int totalReçus = 0;
    public int totalErreurs = 0;

    @Override
    boolean envoyer(Envoi data) {
        try {

            // calculer crc
            LiaisonDeDonneesConverter lconv = new LiaisonDeDonneesConverter();
            data._header = lconv.AddCRC(data._data);

            // envoyer au serveur
            data.Compresser();
            super.envoyer(data);

            logger.logClient("paquet" + " envoyé à: " + LocalDateTime.now());
            totalEnvois += 1;

        } catch (Exception e) {
            System.out.println(e);

        }
        return true;
    }

    // recevoir
    // verify
    @Override
    public boolean recevoir(Envoi envoi) {
        LiaisonDeDonneesConverter l = new LiaisonDeDonneesConverter();
        // _data toute sauf le crc
        // _header = crc

        boolean verify = l.VerifyCRC(envoi._data, false);
        ByteBuffer wrapped = ByteBuffer.wrap(envoi._data); // big-endian by default
        int num = wrapped.getInt();
        if (verify) {
            envoi.decompresser(4); // enlever crc
            boolean transportOk = super.recevoir(envoi);
            if (!transportOk) {
                logger.logServer("paquet #" + num + " contient un erreur de numéro de paquet");
            } else {
                logger.logServer("paquet #" + num + " bien envoyé");
            }
            return transportOk;

        } else {
            logger.logServer("paquet #" + num + " contient un erreur de CRC");
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