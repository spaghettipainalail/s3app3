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
    public int getTotalSizeOfBytes()
    {
        return this._data.length;
    }
    public String getFilename()
    {
        return this._filename;
    }
    
    // TODO
    public byte[] getBytesArray(int start, int end){
        return this._data;
    }
}