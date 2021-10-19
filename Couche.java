
abstract class Couche {
    private Couche coucheSuivante;
    private Couche couchePrecedente;

    Couche() {
        coucheSuivante = null;
        couchePrecedente = null;
    }

    boolean recevoir(Envoi data) {
        if (couchePrecedente == null)
            return false;
        couchePrecedente.recevoir(data);
        return true;

    }

    boolean envoyer(Envoi data) {
        if (coucheSuivante == null)
            return false;
        coucheSuivante.envoyer(data);
        return true;
    }

    // void handle(Dataframe data) {
    // coucheSuivante.handle(data);
    // }

    void setSuivante(Couche coucheSuivante) {
        this.coucheSuivante = coucheSuivante;
    }

    void setPrecedente(Couche couchePrecedente) {
        this.couchePrecedente = couchePrecedente;
    }
}
