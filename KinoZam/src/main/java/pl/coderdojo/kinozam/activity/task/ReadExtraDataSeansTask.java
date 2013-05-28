package pl.coderdojo.kinozam.activity.task;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;
import pl.coderdojo.kinozam.activity.ShowSeansActivity;
import pl.coderdojo.kinozam.model.Seans;
import pl.coderdojo.kinozam.model.SeansReader;

/**
 * Created by luk on 22.05.13.
 */
public class ReadExtraDataSeansTask extends AsyncTask<Seans, Void, Seans> {

    private Exception exception;
    private Activity activity;

    public ReadExtraDataSeansTask(Activity activity) {
        this.activity = activity;
    }

    @Override
    protected Seans doInBackground(Seans... seanses) {
        try {
            Seans seans = seanses[0];
            new SeansReader().readExtraData(seans);
            return seans;
        } catch (Exception e) {
            this.exception = e;
            return null;
        }
    }

    @Override
    protected void onPostExecute(Seans seans) {
        if (this.exception != null) {
            Context context = activity.getApplicationContext();
            CharSequence text = "Błąd podczas pobierania informacji o seansie: " + exception.getLocalizedMessage();
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
            return;
        }


        Intent intent = new Intent(activity.getApplicationContext(), ShowSeansActivity.class);
        intent.putExtra(Intent.ACTION_VIEW, seans);
        intent.putExtra("image", seans.getImage());
        activity.startActivity(intent);
    }


}