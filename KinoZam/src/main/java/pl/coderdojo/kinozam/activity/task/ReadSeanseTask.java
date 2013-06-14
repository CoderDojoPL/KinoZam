package pl.coderdojo.kinozam.activity.task;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import pl.coderdojo.kinozam.R;
import pl.coderdojo.kinozam.activity.SeansListActivity;
import pl.coderdojo.kinozam.model.Seans;
import pl.coderdojo.kinozam.model.SeansReader;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by luk on 22.05.13.
 */
public class ReadSeanseTask extends AsyncTask<SeansListActivity, Void, List<Seans>> {

    private Exception exception;
    private SeansListActivity activity;

    @Override
    protected List<Seans> doInBackground(SeansListActivity... activities) {
        try {
            this.activity = activities[0];
            return new SeansReader().read();
        } catch (Exception e) {
            this.exception = e;
            return null;
        }
    }

    @Override
    protected void onPostExecute(List<Seans> seanse) {
        if (this.exception != null) {
            Context context = activity.getApplicationContext();
            CharSequence text = "Błąd podczas pobierania seansów: " + exception.getLocalizedMessage();
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
            return;
        }
        ListView listView = (ListView) activity.findViewById(R.id.listView);
        activity.seanse = seanse;
        DateFormat dt = SimpleDateFormat.getTimeInstance(DateFormat.SHORT);
        DateFormat dd = SimpleDateFormat.getDateInstance(DateFormat.LONG);
        List<Map<String, ?>> data = new ArrayList<Map<String, ?>>();
        EditText searchText = (EditText) activity.findViewById(R.id.searchText);
        for (Seans item : seanse) {
            if (item.getTitle().toLowerCase().contains(searchText.getText().toString().toLowerCase())) {
                Map<String, Object> datum = new HashMap<String, Object>(3);
                datum.put("title", item.getTitle());
                datum.put("date", dd.format(item.getDate()));
                datum.put("time", dt.format(item.getDate()));
                datum.put("seans", item);

                data.add(datum);
            }
        }
        SimpleAdapter adapter = new SimpleAdapter(activity, data,
                R.layout.seans_item,
                new String[]{"title", "date", "time"},
                new int[]{R.id.titleTextView,
                        R.id.dateTextView,
                        R.id.timeTextView});
        listView.setAdapter(adapter);

        Context context = activity.getApplicationContext();
        CharSequence text = "Pobrano " + seanse.size() + " seansów";
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }
}