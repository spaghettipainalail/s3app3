public class Transport extends Couche {
    private static Transport _instance;
    private int lastPacketId;
    private Envoi originalData;

    private Transport() {
        lastPacketId = 0;
    }

    public static Transport getInstance() {
        if (_instance == null)
            _instance = new Transport();
        return _instance;
    }

    /**
     * Sépare l'information en petits paquets, ajoute une entête au data et l'envoie à liaisonDeDonneés. S'occupe de renvoyer les packets au besoin
     * @param data du data
     * @return  le retour du handler suivant.
     */
    @Override
    boolean envoyer(Envoi data) {
        boolean retour = true;
        int nbPaquetsRequis = data._data.length;
        if (originalData == null)
            originalData = data;

        nbPaquetsRequis = (int) Math.ceil(nbPaquetsRequis / 200);

        // paquet du filename
        byte[] dataFilename = new byte[200];

        System.arraycopy(data._header, 0, dataFilename, 0, data._header.length);
        Paquet filenamePacket = new Paquet(0, nbPaquetsRequis, dataFilename);

        Envoi envoiNomFichier = new Envoi(filenamePacket.getDataInBytes(), null);
        super.envoyer(envoiNomFichier);

        for (int i = 0; i < nbPaquetsRequis; i++) {
            Envoi envoi = new Envoi();
            Paquet packToSend = new Paquet(i + 1, nbPaquetsRequis, originalData.getBytesArray(i * 200, (i + 1) * 200));
            envoi._data = packToSend.getDataInBytes();
            envoi._header = null;
            retour = super.envoyer(envoi);
            if (retour == false) {
                envoi = new Envoi();
                packToSend = new Paquet(i + 1, nbPaquetsRequis, originalData.getBytesArray(i * 200, (i + 1) * 200));
                envoi._data = packToSend.getDataInBytes();
                envoi._header = null;
            }

        }
        return retour;
    }

    /**
     * Valide selon l'ordre des packets recus
     * @param data du data
     * @return  return false si erreur d'ordre
     */
    @Override
    boolean recevoir(Envoi data) {
        data.reception(42);
        Paquet packetRecu = new Paquet(data);
        System.out.println("lastPacketId: " + lastPacketId);
        System.out.println("getnumpacket: " + packetRecu.get_numPaquet());
        if ((lastPacketId) == packetRecu.get_numPaquet()) {
            lastPacketId = packetRecu.get_numPaquet() + 1;
            return super.recevoir(data);
        } else
            return false;
    }

    // recoit un pointer vers le chunk de donnes (cote du sender)
    // 1. send packets
    // 1.1 CLIENT: send first packet with filename without directory (current
    // folder)
    // 1.2 homemade frame (trame) with only a header wtih clear text name and fix
    // size
    // 1.3 tcp equivalent (ajouter dans lentete le num du packet et la numerotation)
    // aussi le mecanisme pour la non repetition, le acknowledge, et le re-send des
    // packets perdus
    // 1.4 acknowledge contient rien et un num du packet recu
    // 1.5 un paquet (4) est perdu si 3 et 5 sont recus sans avoir le num 4
    // 1.6 la demande de re-send un paquet est un paquet vide avec des infos dans le
    // headers (le paquet re-sended porteras le meme num que celui demander)
    // 1.7 si plus que trois erreurs, serveur declare connection fail et la couche
    // transport du serveur ce renitialise et envoie un exception
    // TransmissionErrorException a la couche application
    // 1.8 le client coupe le flux de donnees en 200 octets ou moins et le header
    // contient la taille de celui-ci (< 200)
    // 1.9 chaque paquet contient le num du premier et du dernier paquet
}
