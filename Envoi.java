import java.util.Arrays;

public class Envoi {
    public byte[] _data;
    public byte[] _header;

    public Envoi() {
    }

    public Envoi(byte[] data) {
        _data = data;
    }

    public Envoi(byte[] data, byte[] header) {
        _data = data;
        _header = header;
    }

    public void transmission() {
        if (_header != null) {
            byte[] newData = new byte[_header.length + _data.length];
            System.arraycopy(_header, 0, newData, 0, _header.length);
            System.arraycopy(_data, 0, newData, _header.length, _data.length);
            _header = null;
            _data = newData;
        }
    }

    public void reception(int tailleHeader) {
        if (_data.length > tailleHeader) {
            _header = Arrays.copyOf(_data, tailleHeader);
            _data = Arrays.copyOfRange(_data, tailleHeader, _data.length);
        }
    }

    public byte[] getBytesArray(int start, int end) {
        return Arrays.copyOfRange(_data, start, end);
    }
}
