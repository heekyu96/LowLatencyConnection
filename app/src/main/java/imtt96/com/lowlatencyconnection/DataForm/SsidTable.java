package imtt96.com.lowlatencyconnection.DataForm;

import android.annotation.SuppressLint;

import java.util.HashMap;

public class SsidTable {
    private HashMap<Integer,String > apSsidMap;

    @SuppressLint("UseSparseArrays")
    public SsidTable() {
        this.apSsidMap = new HashMap<>();
    }

    public HashMap<Integer, String> getApSsidMap() {
        return apSsidMap;
    }
}
