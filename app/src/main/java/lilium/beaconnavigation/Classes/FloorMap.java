package lilium.beaconnavigation.Classes;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Xia on 11/9/2016.
 * Create a indoor map object, which include rooms, hallways, elevators
 */

public class FloorMap {
    public List<Room> officeList;
    public List<Room> labList;
    public List<Room> restRoomsList;
    public List<Elevator> elevatorList;
    public List<HallWay> hallWayList;
    public List<Adjacent> room_hw_adj; //the adjacent between rooms and hallways
    public List<Adjacent> hw_adj; //the adjacent between hallways

    public FloorMap(){
        officeList=new ArrayList<>();
        labList=new ArrayList<>();
        restRoomsList=new ArrayList<>();
        elevatorList=new ArrayList<>();
        hallWayList=new ArrayList<>();
        room_hw_adj=new ArrayList<>();
        hw_adj=new ArrayList<>();
    }

    public void buildMap(){
        addOffice();
        addLab();
        addRestroom();
        addElevator();
        addHallway();
        addRoomHwAdj();
        addHwAdj();
    }

    public void addHwAdj(){
        hw_adj.add(new Adjacent(81,84,new Location(5,20)));
        hw_adj.add(new Adjacent(81,85,new Location(130,20)));
        hw_adj.add(new Adjacent(82,84,new Location(5,51)));
        hw_adj.add(new Adjacent(82,85,new Location(130,51)));
        hw_adj.add(new Adjacent(83,84,new Location(5,118)));
        hw_adj.add(new Adjacent(83,85,new Location(130,118)));
//        hw_adj.add(new Adjacent(84,81,new Location(5,20)));
//        hw_adj.add(new Adjacent(84,82,new Location(5,51)));
//        hw_adj.add(new Adjacent(84,83,new Location(5,118)));
//        hw_adj.add(new Adjacent(85,81,new Location(148,20)));
//        hw_adj.add(new Adjacent(85,82,new Location(148,51)));
//        hw_adj.add(new Adjacent(85,83,new Location(148,118)));
    }

    public void addRoomHwAdj(){
        room_hw_adj.add(new Adjacent(0,83,new Location(120,118)));
        room_hw_adj.add(new Adjacent(1,83,new Location(111,118)));
        room_hw_adj.add(new Adjacent(2,83,new Location(102,118)));
        room_hw_adj.add(new Adjacent(3,83,new Location(93,118)));
        room_hw_adj.add(new Adjacent(4,83,new Location(84,118)));
        room_hw_adj.add(new Adjacent(5,83,new Location(75,118)));
        room_hw_adj.add(new Adjacent(6,83,new Location(66,118)));
        room_hw_adj.add(new Adjacent(7,83,new Location(57,118)));
        room_hw_adj.add(new Adjacent(8,83,new Location(48,118)));
        room_hw_adj.add(new Adjacent(9,83,new Location(38,118)));
        room_hw_adj.add(new Adjacent(10,83,new Location(29,118)));
        room_hw_adj.add(new Adjacent(11,83,new Location(20,118)));

        room_hw_adj.add(new Adjacent(50,83,new Location(46,118)));
        room_hw_adj.add(new Adjacent(51,83,new Location(64,118)));
        room_hw_adj.add(new Adjacent(52,83,new Location(82,118)));
        room_hw_adj.add(new Adjacent(53,83,new Location(100,118)));
        room_hw_adj.add(new Adjacent(54,83,new Location(118,118)));

        room_hw_adj.add(new Adjacent(65,82,new Location(118,51)));
        room_hw_adj.add(new Adjacent(66,82,new Location(100,51)));
        room_hw_adj.add(new Adjacent(67,82,new Location(82,51)));
        room_hw_adj.add(new Adjacent(68,82,new Location(64,51)));
        room_hw_adj.add(new Adjacent(69,82,new Location(46,51)));
        room_hw_adj.add(new Adjacent(70,82,new Location(28,51)));

        room_hw_adj.add(new Adjacent(18,81,new Location(39,20)));
        room_hw_adj.add(new Adjacent(19,81,new Location(48,20)));
        room_hw_adj.add(new Adjacent(20,81,new Location(56,20)));
        room_hw_adj.add(new Adjacent(21,81,new Location(65,20)));
        room_hw_adj.add(new Adjacent(22,81,new Location(74,20)));
        room_hw_adj.add(new Adjacent(23,81,new Location(83,20)));
        room_hw_adj.add(new Adjacent(24,81,new Location(92,20)));
        room_hw_adj.add(new Adjacent(25,81,new Location(101,20)));
        room_hw_adj.add(new Adjacent(26,81,new Location(110,20)));
        room_hw_adj.add(new Adjacent(27,81,new Location(114,20)));

        room_hw_adj.add(new Adjacent(71,81,new Location(118,20)));
        room_hw_adj.add(new Adjacent(72,81,new Location(100,20)));
        room_hw_adj.add(new Adjacent(73,81,new Location(82,20)));
        room_hw_adj.add(new Adjacent(74,81,new Location(64,20)));
        room_hw_adj.add(new Adjacent(75,81,new Location(46,20)));
        room_hw_adj.add(new Adjacent(76,81,new Location(28,20)));
    }

    public void addOffice(){
        officeList.add(new Office(0,new Location(120,120),"3.201"));
        officeList.add(new Office(1,new Location(111,120),"3.202"));
        officeList.add(new Office(2,new Location(102,120),"3.203"));
        officeList.add(new Office(3,new Location(93,120),"3.204"));
        officeList.add(new Office(4,new Location(84,120),"3.205"));
        officeList.add(new Office(5,new Location(75,120),"3.206"));
        officeList.add(new Office(6,new Location(66,120),"3.207"));
        officeList.add(new Office(7,new Location(57,120),"3.208"));
        officeList.add(new Office(8,new Location(48,120),"3.209"));
        officeList.add(new Office(9,new Location(38,120),"3.210"));
        officeList.add(new Office(10,new Location(29,120),"3.211"));
        officeList.add(new Office(11,new Location(20,120),"3.212"));
/*        officeList.add(new Office(12,new Location(0,0),"3.227"));
        officeList.add(new Office(13,new Location(0,0),"3.228"));
        officeList.add(new Office(14,new Location(0,0),"3.229"));
        officeList.add(new Office(15,new Location(0,0),"3.230"));
        officeList.add(new Office(16,new Location(0,0),"3.231"));
        officeList.add(new Office(17,new Location(0,0),"3.232"));*/
        officeList.add(new Office(18,new Location(39,18),"3.402"));
        officeList.add(new Office(19,new Location(48,18),"3.403"));
        officeList.add(new Office(20,new Location(56,18),"3.404"));
        officeList.add(new Office(21,new Location(65,18),"3.405"));
        officeList.add(new Office(22,new Location(74,18),"3.406"));
        officeList.add(new Office(23,new Location(83,18),"3.407"));
        officeList.add(new Office(24,new Location(92,18),"3.408"));
        officeList.add(new Office(25,new Location(101,18),"3.409"));
        officeList.add(new Office(26,new Location(110,18),"3.410"));
        officeList.add(new Office(27,new Location(114,18),"3.411"));
/*        officeList.add(new Office(28,new Location(0,0),"3.602"));
        officeList.add(new Office(29,new Location(0,0),"3.603"));
        officeList.add(new Office(30,new Location(0,0),"3.604"));
        officeList.add(new Office(31,new Location(0,0),"3.605"));
        officeList.add(new Office(32,new Location(0,0),"3.606"));
        officeList.add(new Office(33,new Location(0,0),"3.607"));
        officeList.add(new Office(34,new Location(0,0),"3.608"));
        officeList.add(new Office(35,new Location(0,0),"3.609"));
        officeList.add(new Office(36,new Location(0,0),"3.610"));
        officeList.add(new Office(37,new Location(0,0),"3.611"));
        officeList.add(new Office(38,new Location(0,0),"3.701"));
        officeList.add(new Office(39,new Location(0,0),"3.702"));
        officeList.add(new Office(40,new Location(0,0),"3.703"));
        officeList.add(new Office(41,new Location(0,0),"3.704"));
        officeList.add(new Office(42,new Location(0,0),"3.705"));
        officeList.add(new Office(43,new Location(0,0),"3.706"));
        officeList.add(new Office(44,new Location(0,0),"3.707"));
        officeList.add(new Office(45,new Location(0,0),"3.708"));
        officeList.add(new Office(46,new Location(0,0),"3.801"));
        officeList.add(new Office(47,new Location(0,0),"3.908"));
        officeList.add(new Office(48,new Location(0,0),"3.909"));
        officeList.add(new Office(49,new Location(0,0),"3.910"));*/
    }

    public void addLab(){
        labList.add(new Lab(50,new Location(46,116),"3.213"));
        labList.add(new Lab(51,new Location(64,116),"3.214"));
        labList.add(new Lab(52,new Location(82,116),"3.215"));
        labList.add(new Lab(53,new Location(100,116),"3.216"));
        labList.add(new Lab(54,new Location(118,116),"3.217"));
/*        labList.add(new Lab(55,new Location(0,0),"3.218"));
        labList.add(new Lab(56,new Location(0,0),"3.219"));
        labList.add(new Lab(57,new Location(0,0),"3.220"));
        labList.add(new Lab(58,new Location(0,0),"3.221"));
        labList.add(new Lab(59,new Location(0,0),"3.222"));
        labList.add(new Lab(60,new Location(0,0),"3.613"));
        labList.add(new Lab(61,new Location(0,0),"3.614"));
        labList.add(new Lab(62,new Location(0,0),"3.615"));
        labList.add(new Lab(63,new Location(0,0),"3.616"));
        labList.add(new Lab(64,new Location(0,0),"3.617"));*/
        labList.add(new Lab(65,new Location(118,53),"3.618"));
        labList.add(new Lab(66,new Location(100,53),"3.619"));
        labList.add(new Lab(67,new Location(82,53),"3.620"));
        labList.add(new Lab(68,new Location(64,53),"3.621"));
        labList.add(new Lab(69,new Location(46,53),"3.622"));
        labList.add(new Lab(70,new Location(28,53),"3.623"));
        labList.add(new Lab(71,new Location(118,22),"3.412"));
        labList.add(new Lab(72,new Location(100,22),"3.413"));
        labList.add(new Lab(73,new Location(82,22),"3.414"));
        labList.add(new Lab(74,new Location(64,22),"3.415"));
        labList.add(new Lab(75,new Location(46,22),"3.416"));
        labList.add(new Lab(76,new Location(28,22),"3.417"));
    }

    public void addRestroom(){
        restRoomsList.add(new RestRoom(77,new Location(10,91),"3.3R1","F"));
        restRoomsList.add(new RestRoom(78,new Location(10,98),"3.3R2","M"));
    }

    public void addElevator(){
        elevatorList.add(new Elevator(79,new Location(156,66)));
        elevatorList.add(new Elevator(80,new Location(160,66)));
    }

    public void addHallway(){
        hallWayList.add(new HallWay(81,"EW",new Location(0,20),new Location(153,20)));
        hallWayList.add(new HallWay(82,"EW",new Location(0,51),new Location(153,51)));
        hallWayList.add(new HallWay(83,"EW",new Location(0,118),new Location(153,118)));
        hallWayList.add(new HallWay(84,"NS",new Location(5,0),new Location(5,138)));
        hallWayList.add(new HallWay(85,"NS",new Location(130,0),new Location(130,138)));
    }
}
