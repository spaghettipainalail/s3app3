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
    public int totalRecus = 0;
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
        verify = false;
        ByteBuffer wrapped = ByteBuffer.wrap(envoi._data); // big-endian by default

        //Todo: bon numéro de paquet
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
}