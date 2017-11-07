package lilium.beaconnavigation.Enums;

import java.util.Map;
import java.util.TreeMap;

/**
 * Created by boylec on 1/29/17.
 */

public enum ActivityRequestCodeEnum {
    BlueToothActivity,ImageSelectActivity;

    private static Map<Integer, ActivityRequestCodeEnum> ss = new TreeMap<Integer,ActivityRequestCodeEnum>();
    private static final int START_VALUE = 1;
    private int value;

    static {
        for(int i=0;i<values().length;i++)
        {
            values()[i].value = START_VALUE + i;
            ss.put(values()[i].value, values()[i]);
        }
    }

    public static ActivityRequestCodeEnum fromInt(int i) {
        return ss.get(i);
    }

    public int value() {
        return value;
    }
}

