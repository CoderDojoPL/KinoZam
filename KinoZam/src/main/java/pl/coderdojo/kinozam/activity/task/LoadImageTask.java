package pl.coderdojo.kinozam.activity.task;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.InputStream;
import java.net.URI;


/**
 * Created by luk on 24.05.13.
 */
// show The Image
/*new DownloadImageTask((ImageView) findViewById(R.id.imageView1))
        .execute("http://java.sogeti.nl/JavaBlog/wp-content/uploads/2009/04/android_icon_256.png");
        */

public class LoadImageTask extends AsyncTask<URI, Void, Bitmap> {
    ImageView bmImage;

    public LoadImageTask(ImageView bmImage) {
        this.bmImage = bmImage;
    }

    protected Bitmap doInBackground(URI... uris) {

        Bitmap mIcon11 = null;
        try {
            InputStream in = uris[0].toURL().openStream();
            mIcon11 = BitmapFactory.decodeStream(in);
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }
        return mIcon11;
    }

    protected void onPostExecute(Bitmap result) {
        bmImage.setImageBitmap(result);
    }
}