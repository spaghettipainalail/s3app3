public class Paquet {
    private int _numPaquet;
    private int _numPaquetFin;
    private byte[] _data;
    private int _size;

    public Paquet(int numPaquet, int numPaquetFin, byte[] data, int size) {
        this._numPaquet = numPaquet;
        this._numPaquetFin = numPaquetFin;
        this._data = data;
        this._size = size;
    }

    public byte[] getData() {
        return _data;
    }

    public byte[] get_data() {
        return _data;
    }

    public void set_data(byte[] _data) {
        this._data = _data;
    }
}
