package imtt96.com.lowlatencyconnection.DataForm;

import java.util.ArrayList;

public class FeatureMapData {

    private ArrayList<int[]> mapData;

    public FeatureMapData() {
        this.mapData = new ArrayList<>();
    }

    public ArrayList<int[]> getMap() {
        return mapData;
    }

    public void addMapData(int[] mapData) {
        this.mapData.add(mapData);
    }
}
