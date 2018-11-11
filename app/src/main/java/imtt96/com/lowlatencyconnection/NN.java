package imtt96.com.lowlatencyconnection;

import android.util.Log;

import java.math.BigDecimal;

public class NN {
    private int inputNodes;
    private int hiddenNodes;
    private int outputNodes;

    private double learingRate;

    private BigDecimal[][] wih;

    public void setWih(BigDecimal[][] wih) {
        this.wih = wih;
    }

    public void setWho(BigDecimal[][] who) {
        this.who = who;
    }

    private BigDecimal[][] who;


    public NN(int inputNodes, int hiddenNodes, int outputNodes, double learingRate) {
        this.inputNodes = inputNodes;
        this.hiddenNodes = hiddenNodes;
        this.outputNodes = outputNodes;
        this.learingRate = learingRate;
    }

    void ListLog(BigDecimal[][] data, String tag) {
        StringBuilder log = new StringBuilder("[");
        for (int i = 0; i < data.length; i++) {
            log.append("[");
            for (int j = 0; j < data[0].length; j++) {
                log.append(data[i][j]);
                log.append(",");
            }
            log.append("]\n");
        }
        log.append(']');

        Log.d(tag, log.toString());
    }

    public void Query(BigDecimal[][] inputs) {
        //make 1*9 to 9*1
        BigDecimal[][] inputLayer = transepose(inputs);
//        inputLayer = sigmoidActivation(inputLayer);
        ListLog(inputLayer, "inputL");


        BigDecimal[][] hiddenLayer = matrixMultiplication(wih, inputLayer);
        hiddenLayer = sigmoidActivation(hiddenLayer);

        BigDecimal[][] outputLayer = matrixMultiplication(who, hiddenLayer);
        outputLayer = sigmoidActivation(outputLayer);

        Log.d("queryT", findMax(outputLayer) + "");
    }

    public void Train(BigDecimal[][] inputs, BigDecimal[][] targets) {
        //make 1*9 to 9*1
        BigDecimal[][] inputLayer = transepose(inputs);
        BigDecimal[][] target = transepose(targets);

        BigDecimal[][] hiddenLayer = matrixMultiplication(wih, inputLayer);
        hiddenLayer = sigmoidActivation(hiddenLayer);

        BigDecimal[][] outputLayer = matrixMultiplication(who, hiddenLayer);
        outputLayer = sigmoidActivation(outputLayer);

        BigDecimal[][] outputLayerError = matrixSubtraction(target, outputLayer);
        BigDecimal[][] hiddenLayerError = matrixMultiplication(transepose(who), outputLayerError);

        who = matrixAddition(who,
                matrixMultiplication(
                        matrixMultiplication(
                                multiplication(outputLayerError, outputLayer, subtraction(1.0, outputLayer)),transepose(hiddenLayer)),
                        learingRate));

        wih = matrixAddition(wih,
                matrixMultiplication(
                        matrixMultiplication(
                                multiplication(hiddenLayerError, hiddenLayer, subtraction(1.0, hiddenLayer)),transepose(inputLayer)),
                        learingRate));
    }

    private int findMax(BigDecimal[][] arr) {
        int max = 0;
        for (int i = 1; i < arr.length; i++) {
            if (arr[max][0].doubleValue() < arr[i][0].doubleValue()) {
                max = i;
            }
        }
        return max;
    }

    private BigDecimal[][] sigmoidActivation(BigDecimal[][] layer) {
        int rows = layer.length;
        int cols = layer[0].length;

        BigDecimal[][] result = new BigDecimal[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                result[i][j] = BigDecimal.valueOf(1.0 / (1.0 + Math.pow(Math.E, (-1.0 * layer[i][j].doubleValue()))));
            }
        }
        return result;
    }

    public BigDecimal[][] transepose(BigDecimal[][] matrix) {
        int rows = matrix.length;
        int cols = matrix[0].length;

        BigDecimal[][] result = new BigDecimal[cols][rows];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                result[j][i] = matrix[i][j];
            }
        }

        return result;
    }

    private BigDecimal[][] matrixAddition(BigDecimal[][] A, BigDecimal[][] B) {
        int aRows = A.length;
        int aColumns = A[0].length;
        int bRows = B.length;
        int bColumns = B[0].length;

        if (aRows != bRows || aColumns != bColumns) {
            throw new IllegalArgumentException("A:Rows: " + aColumns + " did not match B:Columns " + bRows + ".on Addition");
        }

        BigDecimal[][] result = new BigDecimal[aRows][bColumns];

        for (int i = 0; i < aRows; i++) {
            for (int j = 0; j < aColumns; j++) {
                result[i][j] = A[i][j].add(B[i][j]);
            }
        }

        return result;
    }

    private BigDecimal[][] matrixSubtraction(BigDecimal[][] A, BigDecimal[][] B) {
        int aRows = A.length;
        int aColumns = A[0].length;
        int bRows = B.length;
        int bColumns = B[0].length;

        if (aRows != bRows || aColumns != bColumns) {
            throw new IllegalArgumentException("A:Rows: " + aColumns + " did not match B:Columns " + bRows + ".on Subtraction");
        }

        BigDecimal[][] result = new BigDecimal[aRows][bColumns];

        for (int i = 0; i < aRows; i++) {
            for (int j = 0; j < aColumns; j++) {
                result[i][j] = A[i][j].subtract(B[i][j]);
            }
        }

        return result;
    }

    private BigDecimal[][] subtraction(double A, BigDecimal[][] B) {
        int bRows = B.length;
        int bColumns = B[0].length;

        BigDecimal[][] result = new BigDecimal[bRows][bColumns];

        for (int i = 0; i < bRows; i++) {
            for (int j = 0; j < bColumns; j++) {
                result[i][j] = new BigDecimal(A).subtract(B[i][j]);
            }
        }

        return result;
    }

    private BigDecimal[][] multiplication(BigDecimal[][] A, BigDecimal[][] B, BigDecimal[][] C) {
        int aRows = A.length;
        int aColumns = A[0].length;

        BigDecimal[][] result = new BigDecimal[aRows][aColumns];

        for (int i = 0; i < aRows; i++) { // aRow
            for (int j = 0; j < aColumns; j++) { // bColumn
                result[i][j] = A[i][j].multiply(B[i][j].multiply(C[i][j]));
            }
        }

        return result;
    }

    private BigDecimal[][] matrixMultiplication(BigDecimal[][] A, double B) {
        int aRows = A.length;
        int aColumns = A[0].length;

        BigDecimal[][] result = new BigDecimal[aRows][aColumns];

        for (int i = 0; i < aRows; i++) { // aRow
            for (int j = 0; j < aColumns; j++) { // bColumn
                result[i][j] = A[i][j].multiply(new BigDecimal(B));
            }
        }

        return result;
    }

    private BigDecimal[][] matrixMultiplication(BigDecimal[][] A, BigDecimal[][] B) {
        int aRows = A.length;
        int aColumns = A[0].length;
        int bRows = B.length;
        int bColumns = B[0].length;


        if (aColumns != bRows) {
            throw new IllegalArgumentException("A:Rows: " + aColumns + " did not match B:Columns " + bRows + ".on Multiplication");
        }

        BigDecimal[][] result = new BigDecimal[aRows][bColumns];

        for (int i = 0; i < aRows; i++) { // aRow
            for (int j = 0; j < bColumns; j++) { // bColumn
                for (int k = 0; k < aColumns; k++) { // aColumn
                    result[i][j] = A[i][k].multiply(B[k][j]);
                }
            }
        }

        return result;
    }
}
