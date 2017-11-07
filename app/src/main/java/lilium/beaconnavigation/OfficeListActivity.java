package lilium.beaconnavigation;

import android.app.ListActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lilium.beaconnavigation.Classes.Office;
import lilium.beaconnavigation.Services.DBManager;

public class OfficeListActivity extends ListActivity {
    private ListView listView;
    private DBManager _db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        _db = DBManager.getDBManager(this);
        setContentView(R.layout.activity_office_list);
        showItems();
 //       listView=(ListView)findViewById(R.id.list);
    }

    private void showItems(){
        List<Office> offices = _db.queryOffice();
        Log.d("OfficeListActivity",offices.size()+"");
        ArrayList<Map<String, Integer>> list = new ArrayList<Map<String, Integer>>();
        Log.d("starting showing office","");
        for (Office office : offices) {
            HashMap<String, Integer> map = new HashMap<String, Integer>();
            Log.d(office.name,office.id+"");
            map.put(office.name, office.id);
            list.add(map);
        }
        SimpleAdapter adapter = new SimpleAdapter(this, list, android.R.layout.simple_list_item_2,
                new String[]{"name", "id"}, new int[]{android.R.id.text1, android.R.id.text2});
        setListAdapter(adapter);
    }
}
