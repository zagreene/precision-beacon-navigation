package lilium.beaconnavigation.Services;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import lilium.beaconnavigation.Classes.Adjacent;
import lilium.beaconnavigation.Classes.FloorMap;
import lilium.beaconnavigation.Classes.HallWay;
import lilium.beaconnavigation.Classes.Lab;
import lilium.beaconnavigation.Classes.Location;
import lilium.beaconnavigation.Classes.Office;
import lilium.beaconnavigation.Classes.Room;
import lilium.beaconnavigation.Implementations.RssiAveragingBeacon;
import lilium.beaconnavigation.Interfaces.Beacon;

/**
 * Created by Xia on 11/7/2016.
 * provide interfaces to manage the DB
 */

public class DBManager {
    private static DBHelper helper;
    private static SQLiteDatabase db;
    private static FloorMap floorMap;

    private static DBManager mgr;

    private DBManager(Context context) {
        helper = new DBHelper(context);
        db = helper.getReadableDatabase();

/*        Cursor c=db.rawQuery("SELECT * FROM room",null);
        while (c.moveToNext()) {
            Log.d("DBManager",c.getString(c.getColumnIndex("name")));
        }
        c.close();*/
    }

    public static DBManager getDBManager(Context context){
        if(mgr==null)
            mgr=new DBManager(context);
        return mgr;
    }

    public List<Room> queryRoom() {
        ArrayList<Room> rooms = new ArrayList<Room>();
        Cursor c = db.rawQuery("SELECT * FROM room", null);
        while (c.moveToNext()) {
            Room room = new Room(c.getInt(c.getColumnIndex("_id")),new Location(c.getInt(c.getColumnIndex("x")),c.getInt(c.getColumnIndex("y"))),c.getString(c.getColumnIndex("name")));
            rooms.add(room);
        }
        c.close();
        return rooms;
    }

    public List<HallWay> queryHallway(){
        ArrayList<HallWay> hallWays=new ArrayList<>();
        Cursor c=db.rawQuery("SELECT * FROM hallway",null);
        while(c.moveToNext()){
            HallWay hallWay=new HallWay(c.getInt(c.getColumnIndex("_id")),c.getString(c.getColumnIndex("direction")),new Location(c.getInt(c.getColumnIndex("start_x")),c.getInt(c.getColumnIndex("start_y"))),new Location(c.getInt(c.getColumnIndex("end_x")),c.getInt(c.getColumnIndex("end_y"))));
            hallWays.add(hallWay);
        }
        c.close();

        return hallWays;
    }

    public List<Office> queryOffice() {
        ArrayList<Office> offices = new ArrayList<Office>();
        Cursor c = db.rawQuery("SELECT * FROM office INNER JOIN room ON office._id=room._id", null);
        while (c.moveToNext()) {
            Office office = new Office(c.getInt(c.getColumnIndex("_id")),new Location(c.getInt(c.getColumnIndex("x")),c.getInt(c.getColumnIndex("y"))),c.getString(c.getColumnIndex("name")));
            offices.add(office);
        }
        c.close();
        return offices;
    }

    public Cursor queryOfficeCursor() {
        Cursor c = db.rawQuery("SELECT * FROM office INNER JOIN room ON office._id=room._id", null);
        return c;
    }

    public List<Lab> queryLab() {
        ArrayList<Lab> labs = new ArrayList<>();
        Cursor c = db.rawQuery("SELECT * FROM lab INNER JOIN room ON lab._id=room._id", null);
        while (c.moveToNext()) {
            Lab lab = new Lab(c.getInt(c.getColumnIndex("_id")),new Location(c.getInt(c.getColumnIndex("x")),c.getInt(c.getColumnIndex("y"))),c.getString(c.getColumnIndex("name")));
            labs.add(lab);
        }
        c.close();
        return labs;
    }

    public List<Location> queryAdj(int id1,int id2){
        if(id1>id2){
            int tmp=id1;
            id1=id2;
            id2=tmp;
        }

        ArrayList<Location> points = new ArrayList<>();
        Cursor c = db.rawQuery("SELECT * FROM room_hw_adj WHERE room_id="+id1+" AND hw_id="+id2, null);
        while(c!=null&&c.moveToNext()){
            Location point=new Location(c.getInt(c.getColumnIndex("x")),c.getInt(c.getColumnIndex("y")));
            points.add(point);
        }

        c = db.rawQuery("SELECT * FROM hw_adj WHERE hw_id1="+id1+" AND hw_id2="+id2, null);
        while(c!=null&&c.moveToNext()){
            Location point=new Location(c.getInt(c.getColumnIndex("x")),c.getInt(c.getColumnIndex("y")));
            points.add(point);
        }

        return points;
    }

    public List<Adjacent> queryRoom_HW_Adj() {
        ArrayList<Adjacent> room_hw_adj=new ArrayList<>();
        Cursor c = db.rawQuery("SELECT * FROM room_hw_adj", null);
        while (c.moveToNext()) {
            Adjacent adjacent=new Adjacent(c.getInt(c.getColumnIndex("room_id")),c.getInt(c.getColumnIndex("hw_id")),new Location(c.getInt(c.getColumnIndex("x")),c.getInt(c.getColumnIndex("y"))));
            room_hw_adj.add(adjacent);
        }
        c.close();
        return room_hw_adj;
    }

    public List<Adjacent> queryHW_Adj(){
        ArrayList<Adjacent> hw_adj=new ArrayList<>();
        Cursor c = db.rawQuery("SELECT * FROM hw_adj", null);
        while (c.moveToNext()) {
            Adjacent adjacent=new Adjacent(c.getInt(c.getColumnIndex("hw_id1")),c.getInt(c.getColumnIndex("hw_id2")),new Location(c.getInt(c.getColumnIndex("x")),c.getInt(c.getColumnIndex("y"))));
            hw_adj.add(adjacent);
        }
        c.close();
        return hw_adj;
    }

    public Cursor queryTheCursor() {
        Cursor c = db.rawQuery("SELECT * FROM room", null);
        return c;
    }

    public ArrayList<Beacon> getBeacons(){
        Cursor c = db.query("beacons",new String[] {"mac","x","y"},null,null,null, null, null);
        if (c.getCount() > 0){
            ArrayList<Beacon> beaconList = new ArrayList<Beacon>();
            c.moveToFirst();
            for (int i = 0; i < c.getCount(); i++) {
                beaconList.add(new RssiAveragingBeacon(c.getString(c.getColumnIndex("mac")), -999, c.getFloat(c.getColumnIndex("x")), c.getFloat(c.getColumnIndex("y"))));
            }
            c.close();
            return beaconList;
        }
        c.close();
        return null;
    }

    public Beacon selectBeacon(String mac, int rssi){
        Cursor c = db.query("beacons",new String[] {"mac","x","y"},"mac = ?",new String [] {mac},null, null, null);
        if (c.getCount() > 0){
            c.moveToFirst();
            Beacon beacon = new RssiAveragingBeacon(mac, rssi, c.getFloat(c.getColumnIndex("x")), c.getFloat(c.getColumnIndex("y")));
            c.close();
            return beacon;
        }
        c.close();
        return null;
    }

    public void wipeBeacons(){
        db.execSQL("DELETE FROM beacons");
    }

    public void addBeacon(String mac, float x, float y){
        SQLiteStatement insertStatement = db.compileStatement("INSERT INTO beacons (mac, x, y) VALUES (?, ?, ?);");
        insertStatement.bindString(1,mac);
        insertStatement.bindDouble(2,x);
        insertStatement.bindDouble(3,y);
        insertStatement.execute();
    }


    public void removeBeacon(Beacon beacon){
        int i = db.delete("beacons","mac = ?",new String[] {beacon.getMac()});
        Log.d("dataHandler",String.valueOf(i));
    }

    public void removeBeacon(String mac){
        db.delete("beacons","mac = ?",new String[] {mac});
    }

    public void closeDB() {
        db.close();
    }
}
