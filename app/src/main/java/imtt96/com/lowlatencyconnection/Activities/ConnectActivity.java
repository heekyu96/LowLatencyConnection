package imtt96.com.lowlatencyconnection.Activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import imtt96.com.lowlatencyconnection.DataForm.SsidTable;
import imtt96.com.lowlatencyconnection.Global.Global;
import imtt96.com.lowlatencyconnection.Global.NetworkConnector;
import imtt96.com.lowlatencyconnection.R;
import imtt96.com.lowlatencyconnection.Services.LearningService;
import imtt96.com.lowlatencyconnection.Services.WifiSearchService;

public class ConnectActivity extends AppCompatActivity {
    private TextView textView;

    private WifiScanResultReceiver wifiScanResultReceiver;
    private static final IntentFilter wifiScanResultFilter = new IntentFilter("WifiScanResult");

    private LearingResultReceiver learingResultReceiver;
    private static final IntentFilter learningResultFilter = new IntentFilter("LearningResult");

    private WifiStateReciever wifiStateReciever;
    private static final IntentFilter stateFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);

    private String currentSSID;

    ConnectivityManager connectivityManager;
    WifiManager wifiManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.connect_activity);
        textView = findViewById(R.id.rssi);

        Global.getInstance().getMapingTable().getApSsidMap().put(1, "cclabtestwifi");
        Global.getInstance().getMapingTable().getApSsidMap().put(2, "cclabtestwifi2");
        Global.getInstance().getMapingTable().getApSsidMap().put(3, "CBNU_CCLAB_OPEN");

        Intent intent = new Intent(getApplicationContext(), WifiSearchService.class);
        startService(intent);

        connectivityManager = (ConnectivityManager) this.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        wifiManager = (WifiManager) this.getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        wifiScanResultReceiver = new WifiScanResultReceiver();
        registerReceiver(wifiScanResultReceiver, wifiScanResultFilter);
        learingResultReceiver = new LearingResultReceiver();
//        registerReceiver(learingResultReceiver,learningResultFilter);
        LocalBroadcastManager.getInstance(this).registerReceiver(learingResultReceiver, learningResultFilter);

        wifiStateReciever = new WifiStateReciever();
        registerReceiver(wifiStateReciever, stateFilter);

    }

    public void connect(String ssid) {
        String TAG = "ConnectWifi";
        Log.d(TAG, "Inside addWifiConfig...");
        currentSSID = ssid;

        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        if (wifiInfo.getSSID().equals("\"" + ssid + "\"")) return;

        WifiConfiguration conf = new WifiConfiguration();
        conf.SSID = ssid;
        conf.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
        conf.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.NONE);

        //Connect to the network
        int networkId = wifiManager.addNetwork(conf);
        Log.v(TAG, "Add result " + networkId);
        List<WifiConfiguration> list = wifiManager.getConfiguredNetworks();
        for (WifiConfiguration i : list) {
            if (i.SSID != null && i.SSID.equals("\"" + ssid + "\"")) {
                Log.v(TAG, "WifiConfiguration SSID " + i.SSID);
                boolean isDisconnected = wifiManager.disconnect();
                Log.v(TAG, "isDisconnected : " + isDisconnected);
                boolean isEnabled = wifiManager.enableNetwork(i.networkId, true);
                Log.v(TAG, "isEnabled : " + isEnabled);
                boolean isReconnected = wifiManager.reconnect();
                Log.v(TAG, "isReconnected : " + isReconnected);
                break;
            }
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Intent intent = new Intent(getApplicationContext(), WifiSearchService.class);
        stopService(intent);
        intent = new Intent(getApplicationContext(), LearningService.class);
        stopService(intent);
    }

    private class WifiScanResultReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("wifiScanReceiver", "start");
            Intent learingService = new Intent(getApplicationContext(), LearningService.class);
            learingService.setAction("rssiResult");
            textView.setText(" ");
            textView.append(intent.getIntExtra("AP1", -100) + "/");
            textView.append(intent.getIntExtra("AP2", -100) + "/");
            textView.append(intent.getIntExtra("AP3", -100) + "/");
            textView.append(intent.getIntExtra("AP9", -100) + "/");

            learingService.putExtra("AP1", intent.getIntExtra("AP1", -100));
            learingService.putExtra("AP2", intent.getIntExtra("AP2", -100));
            learingService.putExtra("AP3", intent.getIntExtra("AP3", -100));
            learingService.putExtra("AP4", intent.getIntExtra("AP4", -100));
            learingService.putExtra("AP5", intent.getIntExtra("AP5", -100));
            learingService.putExtra("AP6", intent.getIntExtra("AP6", -100));
            learingService.putExtra("AP7", intent.getIntExtra("AP7", -100));
            learingService.putExtra("AP8", intent.getIntExtra("AP8", -100));
            learingService.putExtra("AP9", intent.getIntExtra("AP9", -100));
            startService(learingService);
        }
    }

    private class LearingResultReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            int result = intent.getIntExtra("learningResult", 0);
            if (Global.getInstance().getMapingTable().getApSsidMap().containsKey(result)) {
                String rst = Global.getInstance().getMapingTable().getApSsidMap().get(result);
                Toast.makeText(context, result + rst, Toast.LENGTH_SHORT).show();
                connect(rst);
            } else {
                Toast.makeText(context, result + "UNKNOWN", Toast.LENGTH_SHORT).show();
            }
            Log.d("testLRR", result + "");
        }

    }

    private class WifiStateReciever extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Toast.makeText(context, "connectivity", Toast.LENGTH_SHORT).show();
            NetworkInfo wifi = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            Log.d("TestSR", wifi.isConnected() + "");
            if (wifi.isConnected()) {
                WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                TimeStamp timeStamp = new TimeStamp();
                timeStamp.execute(wifiInfo.getSSID());
            }
        }
    }


    private class TimeStamp extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... strings) {
            String url = Global.getInstance().getUrl() + "timestamp.php";
            url = url + "?AP=" + strings[0];
            Log.d("url", url);
            String result = NetworkConnector.getInstance().get(url);
            Log.d("Sending ", result);
            return null;
        }
    }
}
