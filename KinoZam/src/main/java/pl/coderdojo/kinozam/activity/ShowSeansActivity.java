package pl.coderdojo.kinozam.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import org.apache.commons.lang.StringEscapeUtils;
import pl.coderdojo.kinozam.R;
import pl.coderdojo.kinozam.model.Seans;

import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;

/**
 * Created by luk on 22.05.13.
 */
public class ShowSeansActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_show_seans);

        final Seans seans = (Seans) getIntent().getSerializableExtra(Intent.ACTION_VIEW);

        Bitmap image = getIntent().getParcelableExtra("image");

        ImageView imageView = (ImageView) findViewById(R.id.imageView);
        imageView.setImageBitmap(image);

        WebView descriptionTextView = (WebView) findViewById(R.id.descriptionTextView);
        String html = "<html><head><meta content='text/html; charset=UTF-8' http-equiv='Content-Type'/></head><body>"
                + "<p align=\"justify\" style=\"color:white;\">"
                + StringEscapeUtils.escapeHtml(seans.getDescription())
                + "</p> "
                + "</body></html>";
        descriptionTextView.loadData(html, "text/html", "utf-8");
        descriptionTextView.setBackgroundColor(0x00000000);

        // TextViewJustify.justifyText(descriptionTextView, 250f);

        TextView titleTextView = (TextView) findViewById(R.id.titleTextView);
        titleTextView.setText(seans.getTitle());

        DateFormat dd = SimpleDateFormat.getDateInstance(DateFormat.LONG);
        DateFormat dt = SimpleDateFormat.getTimeInstance(DateFormat.SHORT);


        TextView timeTextView = (TextView) findViewById(R.id.timeTextView);
        timeTextView.setText(dt.format(seans.getDate()));
        java.util.Calendar cal = GregorianCalendar.getInstance();
        int biezMies = cal.get(java.util.Calendar.MONTH);
        int biezDzien = cal.get(java.util.Calendar.DAY_OF_MONTH);
        cal.setTime(seans.getDate());
        int seansMies = cal.get(java.util.Calendar.MONTH);
        int seansDzien = cal.get(java.util.Calendar.DAY_OF_MONTH);
        TextView dateTextView = (TextView) findViewById(R.id.dateTextView);

        if (biezMies == seansMies && biezDzien == seansDzien) {
            dateTextView.setText("Dzisiaj");
        }
        else
        {
        dateTextView.setText(dd.format(seans.getDate()));
        }



//        TextView linkTextView = (TextView) findViewById(R.id.linkTextView);
//        linkTextView.setText(Html.fromHtml("<a href=\""+seans.getUri()+"\">Zobacz na stronie</a>"));
//        linkTextView.setMovementMethod(LinkMovementMethod.getInstance());
        ImageButton linkButton = (ImageButton) findViewById(R.id.linkButton);
        linkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(seans.getUri().toString()));
                startActivity(browserIntent);
            }
        });
        ImageButton trailerButton = (ImageButton) findViewById(R.id.trailerButton);
        trailerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://m.youtube.com/#/results?search_query=" + URLEncoder.encode(seans.getTitle()) + "+trailer+zapowied%C5%BA+zwiastun"));
                startActivity(browserIntent);
            }
        });


    }
}
