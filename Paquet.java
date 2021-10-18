public class Paquet {
    private int _numPaquet;
    private int _numPaquetFin;
    private byte[] _data;

    public Paquet(int numPaquet, int numPaquetFin, byte[] data) {
        this._numPaquet = numPaquet;
        this._numPaquetFin = numPaquetFin;
        this._data = data;
    }
}
