package imtt96.com.lowlatencyconnection;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.opencsv.CSVReader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.ArrayList;

import imtt96.com.lowlatencyconnection.Global.Weights;

public class DataLoader extends AppCompatActivity {
    int testCnt = 23;
    private int[] testLabel;
    private ArrayList<BigDecimal[][]> testData;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        testLabel = new int[testCnt];
        testData = new ArrayList<>();


        Weights.getInstance().wih = new BigDecimal[9][9];
        Weights.getInstance().who = new BigDecimal[9][9];

        try {

            loadDataSet(getAssets().open("3_3_test.csv"));
            loadWeight(getAssets().open("wih.csv"),getAssets().open("who.csv"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        NN nn = new NN(9,9,9,0.3);
        nn.setWih(Weights.getInstance().wih);
        nn.setWho(Weights.getInstance().who);

        for(int i=0;i<testCnt;i++) {
            nn.Query(testData.get(i));
            Log.d("queryT",testLabel[i]+"L");
        }
    }

    public void loadWeight(InputStream wih, InputStream who) {
        InputStreamReader inputStreamReader;
        String[] record;

        try {
            inputStreamReader = new InputStreamReader(wih);
            BufferedReader reader = new BufferedReader(inputStreamReader);
            CSVReader read = new CSVReader(reader);

            int row = 0;
            while ((record = read.readNext()) != null) {
                int col = 0;
                for (String str : record) {
                    Weights.getInstance().wih[row][col++] = new BigDecimal(Double.valueOf(str));
                }
                row++;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            inputStreamReader = new InputStreamReader(who);
            BufferedReader reader = new BufferedReader(inputStreamReader);
            CSVReader read = new CSVReader(reader);

            int row = 0;
            while ((record = read.readNext()) != null) {
                int col = 0;
                for (String str : record) {
                    Weights.getInstance().who[row][col++] = new BigDecimal(Double.valueOf(str));
//                    Log.d("readCsvWho", this.who[row][col - 1] + "");
                }
                row++;
//                Log.d("readCsvWho", " / ");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadDataSet(InputStream test) {
        InputStreamReader inputStreamReader;
        String[] record;

        try {
            inputStreamReader = new InputStreamReader(test);
            BufferedReader reader = new BufferedReader(inputStreamReader);
            CSVReader read = new CSVReader(reader);

            int cases = 0;
            BigDecimal[][] temp;
            while ((record = read.readNext()) != null) {
                testLabel[cases] = Integer.valueOf(record[0]);
                temp = new BigDecimal[1][9];
                for (int i = 1; i <= 9; i++) {
                    // TODO: 2018-10-29 input
                    temp[0][i - 1] = new BigDecimal((Double.valueOf(record[i]) / 100.0 * 0.99) + 0.99);
                    Log.d("readCsvTest", temp[0][i - 1] + "");
                }
                testData.add(temp);
                cases++;
                Log.d("readCsvTest", " / ");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
