package imtt96.com.lowlatencyconnection;

import android.util.Log;

import com.opencsv.CSVReader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class NeuralNet {
    private int inputNode;
    private int hiddenNode;
    private int outputNode;

    private Double learningRate;

    private String[][] wih;
    private String[][] who;

    private ArrayList<String[][]> testSet;
    private int[] testLabel;

    public NeuralNet(int inputNode, int hiddenNode, int outputNode, double learningRate) {
        this.inputNode = inputNode;
        this.hiddenNode = hiddenNode;
        this.outputNode = outputNode;
        this.learningRate = learningRate;

        wih = weightInit(inputNode, hiddenNode);
        who = weightInit(hiddenNode, outputNode);
    }

    public void loadData(InputStream wih, InputStream who, InputStream test){
        loadDataSet(test);
        loadWeight(wih,who);
    }

    public void TestQuery() {
        int cnt=0;
        for(int i =0;i<testLabel.length;i++){
            if(testLabel[i] == 1+query(testSet.get(i))){

                cnt++;
            }
            Log.d("tttest",query(testSet.get(i))+"");
        }
        Log.d("testQuery",cnt+"/"+testLabel.length+"/"+((double)cnt/testLabel.length));

    }

    private String[][] weightInit(int layer1, int layer2) {
        String[][] temp = new String[layer1][layer2];

        for (int i = 0; i < layer1; i++) {
            for (int j = 0; j < layer2; j++) {
                temp[i][j] = Double.valueOf(new Random().nextGaussian() * 0.3).toString();
            }
        }

        return temp;
    }

    public int query(String[][] input) {
        String[][] hiddenLayer = matrixMultiplication(wih, transepose(input));
        Log.d("hddenSize",hiddenLayer.length+"/"+hiddenLayer[0].length);
        for(int i=0;i<hiddenLayer.length;i++){
            Log.d("queryOutIn",hiddenLayer[i][0]+"/");
        }
        hiddenLayer = sigmoidActivation(hiddenLayer);
        for(int i=0;i<hiddenLayer.length;i++){
            Log.d("queryOutSig",hiddenLayer[i][0]+"/");
        }

        String[][] outputLayer = matrixMultiplication(who, hiddenLayer);
        outputLayer = sigmoidActivation(outputLayer);

        for(int i=0;i<outputNode;i++){
            Log.d("queryOut",outputLayer[i][0]+"/");
        }
        Log.d("queryOut","//////////////");

        return findMax(outputLayer);
    }

    private int findMax(String[][] arr) {
        int max = 0;
        for (int i = 1; i < arr.length; i++) {
            if (Double.valueOf(arr[max][0]) < Double.valueOf(arr[i][0])) {
                max = i;
            }
        }
        return max;
    }

    public String[][] transepose(String[][] matrix) {
        int rows = matrix.length;
        int cols = matrix[0].length;

        String[][] result = new String[cols][rows];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                result[j][i] = matrix[i][j];
            }
        }

        return result;
    }

    private String[][] sigmoidActivation(String[][] layer) {
        int layerRows = layer.length;
        int layerColumns = layer[0].length;
        String[][] result = new String[layerRows][layerColumns];

        for (int i = 0; i < layerRows; i++) {
            for (int j = 0; j < layerColumns; j++) {
                result[i][j] = new BigDecimal(1.0 / (1.0 + Math.pow(Math.E, (-1.0 * new BigDecimal(layer[i][j]).doubleValue())))).toString();
            }
        }

        return result;
    }

    private String[][] matrixMultiplication(String[][] A, String[][] B) {
        int aRows = A.length;
        int aColumns = A[0].length;
        int bRows = B.length;
        int bColumns = B[0].length;


        if (aColumns != bRows) {
            throw new IllegalArgumentException("A:Rows: " + aColumns + " did not match B:Columns " + bRows + ".");
        }

        String[][] result = new String[aRows][bColumns];

        for (int i = 0; i < aRows; i++) { // aRow
            for (int j = 0; j < bColumns; j++) { // bColumn
                for (int k = 0; k < aColumns; k++) { // aColumn
                    BigDecimal tempA = new BigDecimal(A[i][k]);
                    BigDecimal tempB = new BigDecimal(B[k][j]);
                    result[i][j] = tempA.multiply(tempB).toString();
                }
            }
        }

        return result;
    }

    public void loadDataSet(InputStream test) {
        InputStreamReader inputStreamReader;
        String[] record;
        testSet = new ArrayList<>();
        testLabel = new int[23];
        try {
            inputStreamReader = new InputStreamReader(test);
            BufferedReader reader = new BufferedReader(inputStreamReader);
            CSVReader read = new CSVReader(reader);

            int cases = 0;

            while ((record = read.readNext()) != null) {
                String[][] temp = new String[1][inputNode];
                testLabel[cases++] = Integer.valueOf(record[0]);
                for (int i = 1; i <= inputNode; i++) {
                    // TODO: 2018-10-29 input
                    temp[0][i-1] = new BigDecimal((Double.valueOf(record[i]) / 100.0 * 0.99) + 0.99).round(new MathContext(4)).toString();

                }
                testSet.add(temp);
                Log.d("readCsvTest", Arrays.deepToString(testSet.get(cases - 1)) +"");
                Log.d("readCsvTest", " / ");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadWeight(InputStream wih, InputStream who) {
        InputStreamReader inputStreamReader ;
        String[] record ;

        try {
            inputStreamReader = new InputStreamReader(wih);
            BufferedReader reader = new BufferedReader(inputStreamReader);
            CSVReader read = new CSVReader(reader);

            int row =0;
            while ((record = read.readNext()) != null) {
                int col=0;
                for (String str : record) {
                    this.wih[row][col++]=Double.valueOf(str).toString();
                    Log.d("readCsvWih", this.wih[row][col-1]+"");
                }
                row++;
                Log.d("readCsvWih"," / ");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            inputStreamReader = new InputStreamReader(who);
            BufferedReader reader = new BufferedReader(inputStreamReader);
            CSVReader read = new CSVReader(reader);

            int row =0;
            while ((record = read.readNext()) != null) {
                int col=0;
                for (String str : record) {
                    this.who[row][col++]=Double.valueOf(str).toString();
                    Log.d("readCsvWho", this.who[row][col-1]+"");
                }
                row++;
                Log.d("readCsvWho"," / ");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}