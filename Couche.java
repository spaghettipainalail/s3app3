abstract class Couche {
    private Couche coucheSuivante;
    private Couche couchePrecedante;

    Couche() {
        coucheSuivante = null;
        couchePrecedante = null;
    }

    void suivant() {
        if (coucheSuivante != null) {
            coucheSuivante.handle();
        }
    }

    void precedante() {
        if (couchePrecedante != null) {
            couchePrecedante.handle();
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
        this.couchePrecedante = precedante;
    }
}
