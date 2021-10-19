import java.util.Arrays;

public class Envoi {
    private byte[] _data;
    private byte[] _header;

    public Envoi() {

    }

    public void Compresser() {
        if (_header != null) {
            byte[] newData = new byte[_header.length + _data.length];
            System.arraycopy(_header, 0, newData, 0, _header.length);
            System.arraycopy(_data, 0, newData, _header.length, _data.length);
            _header = null;
            _data = newData;
        }
    }

    public void decompresser(int tailleHeader) {
        if (_data.length > tailleHeader) {
            _header = Arrays.copyOf(_data, tailleHeader);
            _data = Arrays.copyOfRange(_data, tailleHeader, _data.length);
        }
    }

    public byte[] get_data() {
        return _data;
    }

    public void set_data(byte[] _data) {
        this._data = _data;
    }
}
