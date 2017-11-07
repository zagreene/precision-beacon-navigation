package lilium.beaconnavigation.Interfaces;


import java.util.Queue;

/**
 * Created by boylec on 1/29/17.
 */

public interface Beacon {
    String getMac();
    float getX();
    float getY();
    long getLastUpdate();
    Queue<Integer> getRssiQueue();
    void setLastUpdate(long lastUpdate);
    void setRssi(int rssi);

    double distance();
    void addRssi(Integer Rssi);
    double smoothRssi();

    @Override
    boolean equals(Object obj);

    @Override
    String toString();
}
