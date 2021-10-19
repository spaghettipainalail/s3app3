
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
        return couchePrecedente.recevoir(data);

    }

    boolean envoyer(Envoi data) {
        if (coucheSuivante == null)
            return false;
        return coucheSuivante.envoyer(data);
    }

    void setSuivante(Couche coucheSuivante) {
        this.coucheSuivante = coucheSuivante;
    }

    void setPrecedente(Couche couchePrecedente) {
        this.couchePrecedente = couchePrecedente;
    }
}
