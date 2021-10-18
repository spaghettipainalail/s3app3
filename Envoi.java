import java.util.ArrayList;

public class Envoi {
    private byte[] _data;
    private byte[] _header;

    public Envoi() {

    }

    public void Compresser() {
        int aLen = _data.length;
        int bLen = _header.length;

        byte[] newData = new byte[_header.length +_data.length]; 
        System.arraycopy(_header, 0, newData, 0, _header.length);
        System.arraycopy(_data, 0, newData, _header.length, _data.length);
    }
}
