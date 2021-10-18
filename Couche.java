abstract class Couche {
    private Couche coucheSuivante;
    private Couche couchePrecedente;

    Couche() {
        coucheSuivante = null;
        couchePrecedente = null;
    }

    void suivant() {
        if (coucheSuivante != null) {
            coucheSuivante.handle();
        }
    }

    void precedente() {
        if (couchePrecedente != null) {
            couchePrecedente.handle();
        }

    }

    boolean handle() {
        return false;

    }

    void setSuivante(Couche suivante) {
        this.coucheSuivante = suivante;
    }
    void setPrecedante(Couche precedante)
    {
        this.couchePrecedente = precedante;
    }
}
