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

    public byte[] getDataInBytes() {

        byte[] size, numPaquet, numPaquetFin = new byte[14];
        numPaquet = ("nump:" + _numPaquet).getBytes();
        size = ("size:" + _data.length).getBytes();
        numPaquetFin = ("numf:" + _numPaquetFin).getBytes();

        byte[] newData = new byte[(14 * 3) + _data.length];

        System.arraycopy(numPaquet, 0, newData, 0, 14);
        System.arraycopy(numPaquetFin, 0, newData, 14, 14);
        System.arraycopy(size, 0, newData, 28, 14);
        System.arraycopy(_data, 0, newData, 42, _data.length);

        return newData;
    }

    public int get_numPaquet() {
        return _numPaquet;
    }

    public int get_numPaquetFin() {
        return _numPaquetFin;
    }

    public int get_size() {
        return _size;
    }
}
