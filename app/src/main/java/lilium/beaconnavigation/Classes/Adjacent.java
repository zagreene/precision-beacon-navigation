package lilium.beaconnavigation.Classes;

/**
 * Created by Xia on 11/29/2016.
 * the adjacent relationship between room and hallway (or between hallway and hallway)
 */

public class Adjacent {
    public int id1; //the id of the first room or hallway
    public int id2; //the id of the second room of hallway

    public Location location; //the intersection point of the two adjacent object

    public Adjacent(int id1, int id2, Location location){
        this.id1=id1;
        this.id2=id2;
        this.location=location;
    }
}
