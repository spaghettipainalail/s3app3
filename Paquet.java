public class Paquet {
    //header tcp
    private int _numPaquet;
    private int _numPaquetFin;
    private int _size;
    //data
    private byte[] _data;

    public Paquet(int numPaquet, int numPaquetFin, byte[] data) {
        this._numPaquet = numPaquet;
        this._numPaquetFin = numPaquetFin;
        this._data = data;
    }

    public byte[] getDataInBytes() {

        byte[] size = new byte[14];
        byte[] numPaquet = new byte[14];
        byte[] numPaquetFin = new byte[14];
        System.arraycopy(("nump:" + _numPaquet).getBytes(), 0, numPaquet, 0, ("nump:" + _numPaquet).getBytes().length);
        System.arraycopy(("size:" + _data.length).getBytes(), 0, size, 0, ("size:" + _data.length).getBytes().length);
        System.arraycopy(("numf:" + _numPaquetFin).getBytes(), 0, numPaquetFin, 0, ("numf:" + _numPaquetFin).getBytes().length);

        byte[] newData = new byte[(14 * 3) + _data.length];

        System.arraycopy(numPaquet, 0, newData, 0, 14);
        System.arraycopy(numPaquetFin, 0, newData, 14, 14);
        System.arraycopy(size, 0, newData, 28, 14);
        System.arraycopy(_data, 0, newData, 42, _data.length);

        return newData;
    }

    public Paquet(Envoi data) {

        byte[] size = new byte[14];
        byte[] numPaquet = new byte[14];
        byte[] numPaquetFin = new byte[14];
        this._data = data._data;

        System.arraycopy(numPaquet, 0, data._header, 0, 14);
        System.arraycopy(numPaquetFin, 0,  data._header, 14, 14);
        System.arraycopy(size, 0,  data._header, 28, 14);

        

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
