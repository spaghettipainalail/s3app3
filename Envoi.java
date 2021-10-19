import java.util.Arrays;

public class Envoi {
    public byte[] _data;
    public byte[] _header;

    public Envoi() {

    }

    public Envoi(byte[] data) {
        _data = data;
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
}
