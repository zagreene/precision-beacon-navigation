package lilium.beaconnavigation.Classes;


/**
 * Created by Xia on 11/9/2016.
 */

public class HallWay {
    public int id;
    public String direction;
    public Location start;
    public Location end;

    public HallWay(int id, String direction, Location start, Location end){
        this.id=id;
        this.direction=direction;
        this.start=start;
        this.end=end;
    }
}
