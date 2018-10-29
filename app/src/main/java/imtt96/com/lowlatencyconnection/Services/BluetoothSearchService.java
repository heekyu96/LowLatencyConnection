package imtt96.com.lowlatencyconnection.Services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.estimote.coresdk.observation.region.beacon.BeaconRegion;
import com.estimote.coresdk.recognition.packets.Beacon;
import com.estimote.coresdk.service.BeaconManager;

import java.util.List;
import java.util.UUID;

public class BluetoothSearchService extends Service {

    private BeaconManager beaconManager;
    private BeaconRangingListener beaconRangingListener;
    private BeaconRegion beaconRegion;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("BluetoothService", "OnCreate");
        beaconManager = new BeaconManager(getApplicationContext());
        beaconRangingListener = new BeaconRangingListener();
        beaconRegion = new BeaconRegion("monitored region", UUID.fromString("e2c56db5-dffb-48d2-b060-d0f5a71096e0"), null, null);
        beaconManager.setRangingListener(beaconRangingListener);
        beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
            @Override
            public void onServiceReady() {
                beaconManager.startRanging(beaconRegion);
            }
        });
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("BluetoothService", "OnStartConmmand");
        beaconManager.startRanging(beaconRegion);

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        beaconManager.stopRanging(beaconRegion);
        super.onDestroy();
    }

    private class BeaconRangingListener implements BeaconManager.BeaconRangingListener {

        @Override
        public void onBeaconsDiscovered(BeaconRegion beaconRegion, List<Beacon> beacons) {
            for (int i = 0; i < beacons.size(); i++) {
                Beacon result = beacons.get(i);
                Log.d("ScanResultLog", (i + 1) + ". SSID : " + result.getMinor()
                        + " RSSI : " + result.getRssi() + " dBm\n");

            }




        }
    }
}
