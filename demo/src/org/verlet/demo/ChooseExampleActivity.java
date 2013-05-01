package org.verlet.demo;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.verlet_demo.R;

public class ChooseExampleActivity extends ListActivity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String[] values = new String[] {getString(R.string.shapes_example), getString(R.string.spiderweb_example)};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, values);
        setListAdapter(adapter);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Intent intent = null;
        switch (position) {
            case 0:
                intent = new Intent(this, ShapesExampleActivity.class);
                break;
            case 1:
                intent = new Intent(this, SpiderExampleActivity.class);
                break;
        }
        startActivity(intent);
    }
}