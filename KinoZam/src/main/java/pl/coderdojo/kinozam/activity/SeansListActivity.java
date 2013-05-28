package pl.coderdojo.kinozam.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import pl.coderdojo.kinozam.R;
import pl.coderdojo.kinozam.activity.task.ReadExtraDataSeansTask;
import pl.coderdojo.kinozam.activity.task.ReadSeanseTask;
import pl.coderdojo.kinozam.model.Seans;

import java.util.HashMap;

public class SeansListActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seans_list);
        ListView listView = (ListView) findViewById(R.id.listView);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                HashMap<String, ?> fields = (HashMap<String, ?>) parent.getItemAtPosition(position);
                Seans seans = (Seans) fields.get("seans");

                Context context = getApplicationContext();
                CharSequence text = "Trwa ładowanie...";
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(context, text, duration);
                toast.show();

                new ReadExtraDataSeansTask(SeansListActivity.this).execute(seans);
            }
        });

        View.OnClickListener openDojoListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://coder-dojo.pl"));
                startActivity(browserIntent);
            }
        };
        findViewById(R.id.dojoLogoView).setOnClickListener(openDojoListener);
        findViewById(R.id.linkView).setOnClickListener(openDojoListener);
        findViewById(R.id.dojoTextView).setOnClickListener(openDojoListener);

        initReadList();

    }

    private void initReadList() {
        new ReadSeanseTask().execute(this);
        Context context = getApplicationContext();
        CharSequence text = "Pobieranie...";
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        initReadList();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.seans_list, menu);
        return true;
    }
    
}
