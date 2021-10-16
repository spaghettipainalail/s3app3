public abstract class Couche {
    private Couche coucheSuivante;
    private Couche couchePrecedante;

    public Couche(Couche precedante, Couche suivante) {
        this.couchePrecedante = precedante;
        this.coucheSuivante = suivante;
    }

    public void suivant()
    {

    }

    public void precedante()
    {

        
    }
}