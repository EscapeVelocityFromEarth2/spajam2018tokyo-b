package com.escapevelocityfromearth2.shohosan;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.escapevelocityfromearth2.shohosan.database.DbManager;
import com.escapevelocityfromearth2.shohosan.view.ScheduleListAdapter;

import java.util.ArrayList;

public class ScheduleActivity extends AppCompatActivity {

    ListView listView;

    ArrayList<DrugData> list = new ArrayList<DrugData>();
    ScheduleListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        listView = findViewById(R.id.schedule_list);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                ListView listView = (ListView) adapterView;
                // クリックされたアイテムを取得します
                String item = ((DrugData) listView.getItemAtPosition(i)).name;
                Toast.makeText(ScheduleActivity.this, "clicked item [" + item + "]", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        updateList();
    }

    private void updateList() {
        if (adapter == null) {
            list = Util.sortAlarmDataList(this);//DbManager.loadData(this , null);
            adapter = new ScheduleListAdapter(this, 0, list);
            listView.setAdapter(adapter);
        }
        adapter.notifyDataSetChanged();
    }
}
