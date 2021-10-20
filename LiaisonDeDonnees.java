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

    /**
     * Ajoute un CRC au packet, passer au prochain handler
     * @param data un envoi contenant un byte[]
     * @return le bool de l'envoi par le prochain handler ou false sur exception.
     */
    @Override
    boolean envoyer(Envoi data) {
        try {
            // calculer crc
            LiaisonDeDonneesConverter lconv = new LiaisonDeDonneesConverter();
            data._header = lconv.AddCRC(data._data);

            // envoyer au serveur
            data.transmission();
            boolean result = super.envoyer(data);
            if (result){
                logger.logClient("paquet" + " envoyé à: " + LocalDateTime.now());
                totalEnvois += 1;
                return true;
            }
            logger.logClient("erreur de paquet" + " envoyé à: " + LocalDateTime.now());
            return false;

        } catch (Exception e) {
            System.out.println(e);
            return false;
        }
    }

    /**
     * Recoit de l'information, vérifie le CRC
     * @param envoi envoie contenant un byte[]
     * @return le retour du prochain handle de la chaine de responsabilité ou false si CRC fail
     */
    @Override
    public boolean recevoir(Envoi envoi) {
        LiaisonDeDonneesConverter l = new LiaisonDeDonneesConverter();
        // _data toute sauf le crc
        // _header = crc

        boolean verify = l.VerifyCRC(envoi._data, false);  /**********    MODIFIER ICI      *********/
        // TODO: bon numéro de packet

        byte[] numPaquetBytes = new byte[14];
        System.arraycopy(envoi._data, 4, numPaquetBytes, 0, 14);
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