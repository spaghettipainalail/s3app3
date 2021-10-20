import java.time.LocalDateTime;

public class LiaisonDeDonnees extends Couche {
    private static LiaisonDeDonnees _instance;
    Logger logger = new Logger();
    public int totalEnvois = 0;
    public int totalRecus = 0;
    public int totalErreurs = 0;

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

    @Override
    boolean envoyer(Envoi data) {
        try {

            // calculer crc
            LiaisonDeDonneesConverter lconv = new LiaisonDeDonneesConverter();
            data._header = lconv.AddCRC(data._data);

            // envoyer au serveur
            data.transmission();
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

        // TODO: bon numéro de packet

        byte[] numPaquetBytes = new byte[14];
        System.arraycopy(envoi._data, 18, numPaquetBytes, 0, 14);
        String _numPaquet = new String(numPaquetBytes).replaceAll("\0", "");
        int num = Integer.parseInt((_numPaquet.split(":")[1]));
        if (verify) {
            envoi.reception(4); // enlever crc
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