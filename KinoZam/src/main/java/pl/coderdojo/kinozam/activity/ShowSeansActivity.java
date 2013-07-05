package pl.coderdojo.kinozam.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
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
import java.util.Date;

/**
 * Aktywność wyświetlająca szczegóły wybranego seansu
 */
public class ShowSeansActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //ustawiamy layout okna
        setContentView(R.layout.activity_show_seans);

        //pobieramy obiekt seansu z intencji, która spowodowała otwarcie tej aktywności
        //w miejscu tworzenia intencji zapisalismy do niej dodatkowe dane
        final Seans seans = (Seans) getIntent().getSerializableExtra(Intent.ACTION_VIEW);

        //oprócz obiektu seansu zapisalismy tez obraz miniatury
        Bitmap image = getIntent().getParcelableExtra("image");

        //pobieramy kontrolke imageView i wyswietlamy w niej miniaturke
        ImageView imageView = (ImageView) findViewById(R.id.imageView);
        imageView.setImageBitmap(image);

        //pobieramy kontrolke descriptionTextView, słuzaca do wyswietlania opisu filmu
        //musielismy uzyc WebView, aby zrobic justowania, bo w TextView nie ma takiej
        //mozliwosci. Justowanie jest zrobione przy pomocy html-a (<p align="justify">)
        WebView descriptionTextView = (WebView) findViewById(R.id.descriptionTextView);
        String html = "<html><head><meta content='text/html; charset=UTF-8' http-equiv='Content-Type'/></head><body>"
                + "<p align=\"justify\" style=\"color:white;\">"
                + StringEscapeUtils.escapeHtml(seans.getDescription())
                + "</p> "
                + "</body></html>";
        descriptionTextView.loadData(html, "text/html", "utf-8");
        //ustawiamy kolor tla
        descriptionTextView.setBackgroundColor(0x00000000);

        //pobieramy kontrolke do wyswietlania tytulu seansu
        TextView titleTextView = (TextView) findViewById(R.id.titleTextView);
        //i ustawiamy w niej do wyświetlenia tytuł seansu
        titleTextView.setText(seans.getTitle());

        //Tworzymy obiekt formatujący datę, czyli konwertujący obiekt typu Date
        //na tekst (String) w odpowiednium formacie. W tym przypadku będzie to DD-miesiec-RRRR
        DateFormat dd = SimpleDateFormat.getDateInstance(DateFormat.LONG);

        //Tworzymy obiekt formatujący czas z daty, czyli konwertujący obiekt typu Date
        //na tekst (String) w odpowiednium formacie. W tym przypadku będzie to GG:mm
        DateFormat dt = SimpleDateFormat.getTimeInstance(DateFormat.SHORT);

        //pobieramy kontrolkę do wyswietlania godziny seansu
        TextView timeTextView = (TextView) findViewById(R.id.timeTextView);
        //wyswietlamy sformatowany czas z daty seansu
        timeTextView.setText(dt.format(seans.getDate()));

        Date d1 = seans.getDate();
        Date d2 = new Date();
        Long czas = (d1.getTime() - d2.getTime()) / 1000;//8000
        Long godziny = czas / 3600;// 8000/3600 = 2
        Long minuty = (czas - godziny * 3600) / 60;//8000-2*3600 = 800/60godziny


        //pobieramy kontrolke do wyswietlania daty seansu
        TextView dateTextView = (TextView) findViewById(R.id.dateTextView);

        //jesli seans jest dzisiaj, to zamiast daty wyświetlamy po prostu "Dzisiaj"
        if (seans.isToday()) {
            dateTextView.setText(seans.zaIleCzasu());
        } else {//w przeciwnym wypadku formatujemy date formaterem dd i wyswietlamy w pobranej kontrolce
            dateTextView.setText(dd.format(seans.getDate()));
        }

        //pobieramy przycisk do przechodzenia na stronę filmu
        ImageButton linkButton = (ImageButton) findViewById(R.id.linkButton);

        //po klikniecu ne niego jest tworzona intencja otwierająca przeglądarkę z podanym adresem internetowy
        //niezbedna jest konwersja miedzy typami adresu. W obiekcie seansu używamy typu URI, natomiast intencje
        //wykorzystuje typu Uri,
        linkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(seans.getUri().toString()));
                //odpalamy intencje
                startActivity(browserIntent);
            }
        });
        //pobieramy przycisk do wyszukiwania trailerow
        ImageButton trailerButton = (ImageButton) findViewById(R.id.trailerButton);

        //po kliknieciu na niego jest tworzona intencja wyswietlająca stronę youtube z hasłem do wyszukiwania/
        //bedacym zlaczeniem nazwy seansu oraz słow "trailer zapowiedź zwiastun"
        trailerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //korzystamy z metody URLEncoder.encode(String s), aby zakodować poprawnie polskie znaki w adresie url
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://m.youtube.com/#/results?search_query=" + URLEncoder.encode(seans.getTitle()) + "+trailer+zapowied%C5%BA+zwiastun"));
                //odpalamy intencje
                startActivity(browserIntent);
            }
        });

        //pobieramy przycisk do wyszukiwania trailerow
        ImageButton recenzjaButton = (ImageButton) findViewById(R.id.recenzjaButton);

        //po kliknieciu na niego jest tworzona intencja wyswietlająca stronę youtube z hasłem do wyszukiwania/
        //bedacym zlaczeniem nazwy seansu oraz słow "trailer zapowiedź zwiastun"
        recenzjaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), RecenzjaActivity.class);
                intent.putExtra(Intent.ACTION_VIEW, seans);
                startActivity(intent);

            }
        });
    }
}
