package lilium.beaconnavigation.Classes;

/**
 * Created by Xia on 11/7/2016.
 */

public class Room {
    public int id;
    public String name;
    public Location location;

    public Room(int id, Location location, String name){
        this.id=id;
        this.location=location;
        this.name=name;
    }
}
