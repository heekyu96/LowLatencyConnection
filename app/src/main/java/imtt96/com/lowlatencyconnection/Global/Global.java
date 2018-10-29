package imtt96.com.lowlatencyconnection.Global;

import imtt96.com.lowlatencyconnection.DataForm.SsidTable;

/**
 * Created by imtt9 on 2018-07-10.
 */

public class Global {
    private final static Global instance = new Global();

    public static Global getInstance() {
        return instance;
    }

    public String getUrl() {
        return "http://203.255.81.72/heegis/latency/";
    }

    private SsidTable mapingTable = new SsidTable();

    public SsidTable getMapingTable() {
        return mapingTable;
    }
}
