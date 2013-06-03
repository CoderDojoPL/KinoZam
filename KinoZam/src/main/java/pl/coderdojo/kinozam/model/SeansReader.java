package pl.coderdojo.kinozam.model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import net.fortuna.ical4j.data.CalendarBuilder;
import net.fortuna.ical4j.data.ParserException;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.component.VEvent;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import pl.coderdojo.kinozam.util.net.DoneHandlerInputStream;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.*;
import java.util.*;

/**
 * Created by luk on 19.05.13.
 */
public class SeansReader {

    private final static Map<String, String> descriptionCache = new HashMap<String, String>();
    private final static Map<String, Bitmap> imagecache = new HashMap<String, Bitmap>();

    private Seans createSeans(VEvent event) throws IOException, URISyntaxException {
        final String title = event.getSummary().getValue();
        final Date date = event.getStartDate().getDate();
        final URI uri = event.getUrl().getUri();
        Seans seans = new Seans(title, date, uri);
        return seans;
    }

    public void readExtraData(Seans seans) throws IOException, URISyntaxException {
        String title = seans.getTitle();
        if (descriptionCache.containsKey(title)) {
            seans.setDescription(descriptionCache.get(title));
            seans.setImage(imagecache.get(title));
            return;
        }
        Document doc = Jsoup.connect(seans.getUri().toString()).get();

        Element content = doc.getElementById("content");
        Elements imgs = content.getElementsByTag("img");
        if (imgs.size() > 0) {
            String imgUrl = imgs.get(0).attr("src");
            int offset = imgUrl.lastIndexOf('/') + 1;
            String filename = imgUrl.substring(offset);
            String encoded = URLEncoder.encode(filename);
            imgUrl = imgUrl.substring(0, offset) + encoded;
            imgUrl = imgUrl.replaceFirst("\\.jpg", "-150x150.jpg");
            seans.setImageUri(new URI(imgUrl));
            seans.setImage(loadImage(seans.getImageUri()));
            imagecache.put(title, seans.getImage());
        }
        Elements pars = content.getElementsByTag("p");
        if (pars.size() > 2) {
            seans.setDescription(pars.get(2).text());
            descriptionCache.put(title, seans.getDescription());
        }
    }
    protected Bitmap loadImage(URI uri) {

        Bitmap mIcon11 = null;
        try {
            InputStream in = uri.toURL().openStream();
            mIcon11 = BitmapFactory.decodeStream(in);
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }
        return mIcon11;
    }

    public List<Seans> read() {
        List<Seans> seanse = new ArrayList<Seans>();
        URL url;
        try {
            url = new URL("http://kino.mokzambrow.pl/?ec3_ical");
        } catch (MalformedURLException e) {
            System.out.println("Błąd w adresie kalendarza");
            throw new RuntimeException(e);
        }
        BufferedReader in = null;
        try {
            in = new BufferedReader(
                    new InputStreamReader(new DoneHandlerInputStream(url.openStream())));
        } catch (IOException e) {
            System.out.println("Błąd odczytu kalendarza");
            throw new RuntimeException(e);
        }

        CalendarBuilder builder = new CalendarBuilder();

        try {
            Calendar calendar = builder.build(in);
            for (Object o : calendar.getComponents()) {
                VEvent event = (VEvent) o;
                Seans seans = createSeans(event);

                if (seans.getDate().compareTo(new Date()) > 0)
                    seanse.add(seans);

            }
        } catch (ParserException e) {
            System.out.println("Błąd parsowania kalendarza");
            throw new RuntimeException(e);
        } catch (URISyntaxException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            throw new RuntimeException(e);
        } catch (IOException e) {
            e.printStackTrace();

            throw new RuntimeException(e);
        }
        return seanse;
    }
}
