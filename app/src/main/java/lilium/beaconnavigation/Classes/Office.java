package lilium.beaconnavigation.Classes;


/**
 * Created by Xia on 11/9/2016.
 */

public class Office extends Room {
    public String name;

    public Office(int id, Location location, String name){
        super(id,location,name);
        this.name=name;
    }
}
