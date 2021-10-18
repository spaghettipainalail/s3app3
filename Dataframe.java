import java.lang.reflect.Array;
import java.net.DatagramPacket;
import java.util.Arrays;
import java.util.List;

public class Dataframe {
    private String _filename;
    private byte[] _data;
    private List<Paquet> _paquets;

    public Dataframe(byte[] data, String filename) {
        this._data = data;
        this._filename = filename;
    }

    public String setFilename(String filename) {
        return this._filename;
    }

    public byte[] setData(byte[] data) {
        return this._data;
    }

    public void setPaquets(List<Paquet> paquets) {
        this._paquets = paquets;
    }

    public List<Paquet> getPaquets() {
        return this._paquets;
    }

    public int getTotalSizeOfBytes() {
        return this._data.length;
    }

    public String getFilename() {
        return this._filename;
    }

    // TODO
    public byte[] getBytesArray(int start, int end) {
        return Arrays.copyOfRange(_data, start, end);
    }

    public int getNbPackets() {
        return _paquets.size();
    }

    public Paquet getPaquet(int i) {
        return _paquets.get(i);
    }
}