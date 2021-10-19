import java.nio.charset.StandardCharsets;

public class Paquet {
    // header tcp
    private int _numPaquet;
    private int _numPaquetFin;
    private int _size;
    // data
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
        try {
            System.arraycopy(("nump:" + _numPaquet).getBytes("UTF-8"), 0, numPaquet, 0,
                    ("nump:" + _numPaquet).getBytes().length);
            System.arraycopy(("size:" + _data.length).getBytes("UTF-8"), 0, size, 0,
                    ("size:" + _data.length).getBytes().length);
            System.arraycopy(("numf:" + _numPaquetFin).getBytes("UTF-8"), 0, numPaquetFin, 0,
                    ("numf:" + _numPaquetFin).getBytes().length);
        } catch (Exception e) {
        }

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

        System.arraycopy(data._header, 0, numPaquet, 0, 14);
        System.arraycopy(data._header, 14, numPaquetFin, 0, 14);
        System.arraycopy(data._header, 28, size, 0, 14);

        numPaquet = new String(numPaquet).replaceAll("\0", "").getBytes();
        numPaquetFin = new String(numPaquetFin).replaceAll("\0", "").getBytes();
        size = new String(size).replaceAll("\0", "").getBytes();

        _size = Integer.parseInt(new String(size, StandardCharsets.UTF_8).split(":")[1]);
        _numPaquet = Integer.parseInt(new String(numPaquet, StandardCharsets.UTF_8).split(":")[1]);
        _numPaquetFin = Integer.parseInt(new String(numPaquetFin, StandardCharsets.UTF_8).split(":")[1]);
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

    public byte[] get_data() {
        return _data;
    }
}
