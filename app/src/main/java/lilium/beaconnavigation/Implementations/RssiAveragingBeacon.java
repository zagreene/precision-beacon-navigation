package lilium.beaconnavigation.Implementations;

import java.util.LinkedList;
import java.util.Queue;

import lilium.beaconnavigation.AppConfig;
import lilium.beaconnavigation.Interfaces.Beacon;

public class RssiAveragingBeacon implements Beacon {
    private String mac;
    private float x, y;
    private long lastUpdate;
    private int rssi;

    private Queue<Integer> rssiQueue;

    public RssiAveragingBeacon(String mac, int rssi, float x, float y){
        rssiQueue = new LinkedList<>();
        this.mac = mac;
        lastUpdate = System.currentTimeMillis();
        rssiQueue.add(rssi);
        this.rssi = rssi;
        this.x = x;
        this.y = y;
    }

    @Override
    public Queue<Integer> getRssiQueue(){return rssiQueue;}

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !RssiAveragingBeacon.class.isAssignableFrom(obj.getClass())) return false;
        return ((RssiAveragingBeacon) obj).mac.equals(mac);
    }

    @Override
    public String toString(){
        return mac;
    }

    @Override
    public String getMac() {
        return mac;
    }

    @Override
    public float getX() {
        return x;
    }

    @Override
    public float getY() {
        return y;
    }

    @Override
    public long getLastUpdate() {
        return lastUpdate;
    }

//    @Override
//    public int getRssi() {
//        return rssi;
//    }

    @Override
    public void setLastUpdate(long lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    @Override
    public void setRssi(int rssi) {
        this.rssi = rssi;
    }

    public double distance(){
        //we want linear distances, the distance readings don't have to be accurate
        //they just need to be consistent across all beacons
        //because the trilateration function uses them as relative to each other

        //-61 is the callibrated RSSI reported by the beacons
        //ratio_db = calibratedRSSI - RSSI
        //ratio_db = -61 - (beacons.get(i).cummulativeRssi / beacons.get(i).numRssi)
        //Convert to linear Math.pow(10.0, (ratio_db) / 10.0))
        //2 / x inverse because the results were backwards.
        return 10d / Math.pow(10.0d, (-61d - Math.min(-55d, smoothRssi())) / 10.0d);
    }

    public synchronized void addRssi(Integer Rssi){
        int maxLength = AppConfig.get_beacon_advert_queue_max_length();
        while(rssiQueue.size() >= maxLength)
        {
            rssiQueue.remove();
        }
        rssiQueue.add(Rssi);
    }


    public synchronized double smoothRssi(){
        Integer sum = 0;
        for(Integer i: rssiQueue){
            sum += i;
        }

        return (double)sum / (double)rssiQueue.size();
    }
}