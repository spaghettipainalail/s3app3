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

    @Override
    boolean envoyer(Envoi data) {
        boolean retour = true;
        int nbPaquetsRequis = data._data.length;
        if (originalData == null)
            originalData = data;

        nbPaquetsRequis = (int) Math.ceil(nbPaquetsRequis / 200);
        if (nbPaquetsRequis == 0)
            nbPaquetsRequis++;

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
                i--;

            }

        }
        return retour;
    }

    @Override
    boolean recevoir(Envoi data) {
        data.reception(42);

        Paquet packetRecu = new Paquet(data);
        if ((lastPacketId) == packetRecu.get_numPaquet()) {
            lastPacketId = packetRecu.get_numPaquet() + 1;
            return super.recevoir(data);
        } else
            return false;
    }
}
