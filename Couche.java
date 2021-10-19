import javax.xml.crypto.Data;

import jdk.jfr.BooleanFlag;

abstract class Couche {
    private Couche coucheSuivante;
    private Couche couchePrecedente;

    Couche() {
        coucheSuivante = null;
        couchePrecedente = null;
    }

    boolean recevoir(Envoi data) {
        couchePrecedente.recevoir(data);
        return false;
    }

    void envoyer(Envoi data) {
        coucheSuivante.envoyer(data);
    }

    void handle(Dataframe data) {
        coucheSuivante.handle(data);
    }

    void setSuivante(Couche coucheSuivante) {
        this.coucheSuivante = coucheSuivante;
    }

    void setPrecedente(Couche couchePrecedente) {
        this.couchePrecedente = couchePrecedente;
    }
}
