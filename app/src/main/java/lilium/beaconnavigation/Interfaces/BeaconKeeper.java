package lilium.beaconnavigation.Interfaces;
import java.util.ArrayList;

import lilium.beaconnavigation.Interfaces.Beacon;

/**
 * Created by boylec on 1/29/17.
 */

public interface BeaconKeeper {
    void stop();

    void start();

    ArrayList<Beacon> clonePlaced();

    ArrayList<Beacon> cloneUnplaced();

    void placeBeacon(Beacon beacon);

    void removeBeacon(Beacon beacon);

    void wipeBeacons();

    void async_updateBeacon(String mac, int rssi);

    Beacon nearestBeacon(float x, float y);
}
