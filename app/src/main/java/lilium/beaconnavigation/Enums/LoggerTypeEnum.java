package lilium.beaconnavigation.Enums;

import java.util.Map;
import java.util.TreeMap;

/**
 * Created by boylec on 1/29/17.
 */

public enum LoggerTypeEnum {
    RssiReceived, PositionUpdater, StandardTrilaterationFunction, NonLinearLeastSquaresSolver, WalkToggle;

    private static Map<Integer, LoggerTypeEnum> ss = new TreeMap<Integer,LoggerTypeEnum>();
    private static final int START_VALUE = 1;
    private int value;

    static {
        for(int i=0;i<values().length;i++)
        {
            values()[i].value = START_VALUE + i;
            ss.put(values()[i].value, values()[i]);
        }
    }

    public static LoggerTypeEnum fromInt(int i) {
        return ss.get(i);
    }

    public int value() {
        return value;
    }
}

