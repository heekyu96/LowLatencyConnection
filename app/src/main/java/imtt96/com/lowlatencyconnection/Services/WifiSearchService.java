package imtt96.com.lowlatencyconnection.Services;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.gson.JsonArray;
import com.google.gson.annotations.JsonAdapter;

import java.util.List;

import imtt96.com.lowlatencyconnection.Global.Global;
import imtt96.com.lowlatencyconnection.Global.NetworkConnector;

public class WifiSearchService extends Service {
    private WifiManager wifiManager;
    private WifiScanReceiver wifiScanReceiver;
    private List<ScanResult> scanResults;

    private static final IntentFilter intentFilter = new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);

    private int D_AP1;
    private int D_AP2;
    private int target;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("WifiService", "OnCreate");

        wifiManager = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
        wifiScanReceiver = new WifiScanReceiver();
        getApplicationContext().registerReceiver(wifiScanReceiver, intentFilter);

        Global.getInstance().getMapingTable().getApSsidMap().put(1,"cclabtestwifi");
        Global.getInstance().getMapingTable().getApSsidMap().put(2,"cclabtestwifi2");
        Global.getInstance().getMapingTable().getApSsidMap().put(3,"CBNU_CCLAB_OPEN");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("WifiService", "OnStartConmmand");
//        D_AP1 = Integer.valueOf(intent.getStringExtra("D_AP1"));
//        D_AP2 = Integer.valueOf(intent.getStringExtra("D_AP2"));
//        target = Integer.valueOf(intent.getStringExtra("target"));
//        Log.d("IntentTest", D_AP1 + "/" + D_AP2 + " : " + target);

        getApplicationContext().registerReceiver(wifiScanReceiver, intentFilter);
        wifiManager.startScan();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        getApplicationContext().unregisterReceiver(wifiScanReceiver);
        super.onDestroy();
        Log.d("ServiceDistoy", "");
    }

    private class WifiScanReceiver extends BroadcastReceiver {
        int AP1Rssi;
        int AP2Rssi;
        int AP3Rssi;

        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();

            AP1Rssi = -100;
            AP2Rssi = -100;
            AP3Rssi = -100;

            if (action.equals(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)) {
                scanResults = wifiManager.getScanResults();
                for (int i = 0; i < scanResults.size(); i++) {
                    ScanResult result = scanResults.get(i);
                    if (result.SSID.equals("cclabtestwifi")) {
                        Log.d("ScanResultLog", (i + 1) + ". SSID : " + result.SSID
                                + "RSSI : " + result.level + " dBm\n");
                        AP1Rssi = result.level;
                    }

                    if (result.SSID.equals("cclabtestwifi2")) {
                        Log.d("ScanResultLog", (i + 1) + ". SSID : " + result.SSID
                                + "RSSI : " + result.level + " dBm\n");
                        AP2Rssi = result.level;
                    }

                    if (result.SSID.equals("CBNU_CCLAB_OPEN")) {
                        Log.d("ScanResultLog", (i + 1) + ". SSID : " + result.SSID
                                + " RSSI : " + result.level + " dBm\n");
                        AP3Rssi = result.level;
                    }

                }

            }
            //Data for Server
//            CollectingData collectingData = new CollectingData();
//            collectingData.execute(target + "", AP1Rssi + "", AP2Rssi + "", AP3Rssi + "");
//            wifiManager.startScan();
            //Data for DL
            Intent local  = new Intent();
            local.setAction("WifiScanResult");
            local.putExtra("AP1",AP1Rssi);
            local.putExtra("AP2",AP2Rssi);
            local.putExtra("AP3",AP3Rssi);
            context.sendBroadcast(local);
        }

    }

    private class CollectingData extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... strings) {
            String url = Global.getInstance().getUrl() + "CollectData.php";
            url = url + "?Target=" + strings[0] + "&AP1=" + strings[1] + "&AP2=" + strings[2] + "&AP3=" + strings[3];
            Log.d("url", url);
            String result = NetworkConnector.getInstance().get(url);
            Log.d("Sending ", result);
            return null;
        }
    }

}
