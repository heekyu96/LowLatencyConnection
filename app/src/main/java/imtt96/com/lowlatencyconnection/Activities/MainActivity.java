package imtt96.com.lowlatencyconnection.Activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import imtt96.com.lowlatencyconnection.R;
import imtt96.com.lowlatencyconnection.Services.BluetoothSearchService;
import imtt96.com.lowlatencyconnection.Services.WifiSearchService;

public class MainActivity extends AppCompatActivity {

    private Button stopButton;
    private Button scanButton;

//    private EditText AP1;
//    private EditText AP2;
    private EditText target;

    private int AP1Rssi;
    private int AP2Rssi;
    private int AP3Rssi;

    private ServiceDataReceiver serviceDataReceiver;
    private static final IntentFilter intentFilter = new IntentFilter("WifiScanResult");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        AP1 = findViewById(R.id.AP1);
//        AP2 = findViewById(R.id.AP2);
        target = findViewById(R.id.target);

        stopButton = findViewById(R.id.stopButton);
        scanButton = findViewById(R.id.scanButton);

        stopButton.setVisibility(View.INVISIBLE);
        scanButton.setVisibility(View.VISIBLE);

        serviceDataReceiver= new ServiceDataReceiver();
        registerReceiver(serviceDataReceiver,intentFilter);

        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopButton.setVisibility(View.VISIBLE);
                scanButton.setVisibility(View.INVISIBLE);
                Intent intent = new Intent(getApplicationContext(), WifiSearchService.class);
//                intent.putExtra("D_AP1", AP1.getText().toString());
//                intent.putExtra("D_AP2", AP2.getText().toString());
                intent.putExtra("target", target.getText().toString());
                startService(intent);
            }
        });

        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopButton.setVisibility(View.INVISIBLE);
                scanButton.setVisibility(View.VISIBLE);
                Intent intent = new Intent(getApplicationContext(), WifiSearchService.class);
                stopService(intent);

            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(serviceDataReceiver);
        Intent intent = new Intent(getApplicationContext(), WifiSearchService.class);
        stopService(intent);
    }

    private class ServiceDataReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            AP1Rssi = intent.getIntExtra("AP1", -90);
            AP2Rssi = intent.getIntExtra("AP2", -90);
            AP3Rssi = intent.getIntExtra("AP3", -90);
            Toast.makeText(context, AP1Rssi + "/" + AP2Rssi + "/" + AP3Rssi, Toast.LENGTH_SHORT).show();
            Log.d("receiveTest", AP1Rssi + "/" + AP2Rssi + "/" + AP3Rssi);
        }
    }
}
