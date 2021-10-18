import javax.xml.crypto.Data;

abstract class Couche {
    private Couche coucheSuivante;
    private Couche couchePrecedente;

    Couche() {
        coucheSuivante = null;
        couchePrecedente = null;
    }

    void handle(Dataframe data) {
        coucheSuivante.handle(data);
    }

    public void setSuivante(Couche coucheSuivante) {
        this.coucheSuivante = coucheSuivante;
    }
}
