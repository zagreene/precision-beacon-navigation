package lilium.beaconnavigation.Classes;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import lilium.beaconnavigation.Services.DBManager;

/**
 * Created by Xia on 11/29/2016.
 */

public class MapGraph {
    Map<Integer,Node> nodes;
    List<Room> rooms;
    List<HallWay> hallWays;
    List<Adjacent> room_hw_adj;
    List<Adjacent> hw_adj;
    DBManager manager;

    public MapGraph(DBManager manager){
        this.manager=manager;
        nodes=new HashMap<>();
        rooms=manager.queryRoom();
        hallWays=manager.queryHallway();
        room_hw_adj=manager.queryRoom_HW_Adj();
        hw_adj=manager.queryHW_Adj();
    }

    public void buildGraph(){
        for(Room room:rooms)
            nodes.put(room.id,new Node(room.id,false));

        for(HallWay hallWay:hallWays)
            nodes.put(hallWay.id,new Node(hallWay.id,true));

        for(Adjacent adjacent:room_hw_adj){
            Node room=nodes.get(adjacent.id1);
            Node hw=nodes.get(adjacent.id2);
            room.adjList.add(hw);
            hw.adjList.add(room);
        }

        for(Adjacent adjacent:hw_adj){
            Node hw1=nodes.get(adjacent.id1);
            Node hw2=nodes.get(adjacent.id2);
            hw1.adjList.add(hw2);
            hw2.adjList.add(hw1);
        }
    }

    public List<Location> getPath(int id1, int id2){
        List<Location> route=new ArrayList<>();
        List<Integer> path=findPath(id1,id2);
        int prev=path.get(0);
        for(int i=1;i<path.size();i++){
            int cur=path.get(i);
            List<Location> points=manager.queryAdj(prev,cur);
            if(points.size()!=1) {
                Log.d("MapGraph","error");
                break;
            }
            prev=cur;

            route.addAll(points);
        }

        return route;
    }

    private List<Integer> findPath(int id1, int id2){
        Map<Integer,Integer> map=new HashMap<>();
        Set<Integer> set=new HashSet<>();
        Stack<Node> stack=new Stack<>();
        Node node1=nodes.get(id1);
        Node node2=nodes.get(id2);

        Node start=node1;
        map.put(start.id,null);
        set.add(start.id);
        stack.push(start);
        while(!stack.isEmpty()){
            Node cur=stack.pop();

            if(cur==node2)
                break;
            for(Node node:cur.adjList){
                if(set.contains(node.id))
                    continue;
                set.add(node.id);
                map.put(node.id,cur.id);
                stack.push(node);
            }
        }

        List<Integer> path=new LinkedList<>();
        Integer cur=new Integer(id2);
        while(cur!=null){
            path.add(0,cur);
            cur=map.get(cur);
        }

        return path;
    }
}
