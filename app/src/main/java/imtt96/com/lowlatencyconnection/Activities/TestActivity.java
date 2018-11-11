package imtt96.com.lowlatencyconnection.Activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.io.IOException;

import imtt96.com.lowlatencyconnection.NeuralNet;
import imtt96.com.lowlatencyconnection.NeuralNetwork;
import imtt96.com.lowlatencyconnection.R;

public class TestActivity extends AppCompatActivity {
    private NeuralNetwork neuralNetwork ;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.connect_activity);

//        try {
//            Log.d("testAct","try");
//            neuralNetwork = new NeuralNetwork(9,9,9,getAssets().open("wih.csv"),getAssets().open("who.csv"));
//            neuralNetwork.loadDataSet(getAssets().open("3_3_test.csv"));
//        } catch (IOException e) {
//            Log.d("testAct","error");
//            e.printStackTrace();
//        }
//
//        neuralNetwork.testQuery();

        NeuralNet neuralNet;
        try {
            neuralNet = new NeuralNet(9,9,9,0.3);
            neuralNet.loadData(getAssets().open("wih.csv"),getAssets().open("who.csv"),getAssets().open("3_3_test.csv"));
            neuralNet.TestQuery();
        } catch (IOException e) {
            e.printStackTrace();
        }



    }
}
