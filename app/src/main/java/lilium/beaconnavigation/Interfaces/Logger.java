package lilium.beaconnavigation.Interfaces;

import lilium.beaconnavigation.Interfaces.Beacon;
import android.content.Context;

/**
 * Created by Saber on 4/6/2017.
 */


public interface Logger {
    void log(String logString);
    void cleanUp();
}
