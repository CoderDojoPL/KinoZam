package pl.coderdojo.kinozam.model;

import android.graphics.Bitmap;

import java.io.Serializable;
import java.net.URI;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by luk on 19.05.13.
 */
public class Seans implements Serializable {
    private final URI miniImgUri;
    private String title;
    private Date date;
    private URI uri;
    private String description = "";
    private URI imageUri;
    private transient Bitmap image;
    private transient Bitmap miniImage;

    public Seans(String title, Date date, URI uri, URI miniImgUri) {
        this.title = title;
        this.date = date;
        this.uri = uri;
        this.miniImgUri = miniImgUri;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public Bitmap getMiniImage() {
        return miniImage;
    }

    public void setMiniImage(Bitmap miniImage) {
        this.miniImage = miniImage;
    }

    public Date getDate() {
        return date;
    }

    public URI getUri() {
        return uri;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public URI getImageUri() {
        return imageUri;
    }

    public void setImageUri(URI imageUri) {
        this.imageUri = imageUri;
    }

    public URI getMiniImgUri() {
        return miniImgUri;
    }

    /**
     * Sprawdza czy seans jest dzisiaj
     *
     * @return true - jeśli seans jest dziś, false - w przeciwnym wypadku
     */
    public boolean isToday() {
        java.util.Calendar cal = GregorianCalendar.getInstance();
        int biezMies = cal.get(java.util.Calendar.MONTH);
        int biezDzien = cal.get(java.util.Calendar.DAY_OF_MONTH);
        cal.setTime(getDate());
        int seansMies = cal.get(java.util.Calendar.MONTH);
        int seansDzien = cal.get(java.util.Calendar.DAY_OF_MONTH);
        return biezMies == seansMies && biezDzien == seansDzien;

    }

    @Override
    public String toString() {
        return "Seans{" +
                "title='" + title + '\'' +
                ", date=" + date +
                ", uri=" + uri +
                ", description='" + description + '\'' +
                ", imageUri=" + imageUri +
                '}';
    }

    /**
     * Sprawdza czy seans jest przyszłości
     *
     * @return true - jeśli jest w przyszłości, false - w przeciwnym wypadku
     */
    public boolean isInFuture() {
        return getDate().compareTo(new Date()) > 0;
    }

    public String zaIleCzasuDzisiaj() {
        Date d1 = getDate();
        Date d2 = new Date();
        Long czas = (d1.getTime() - d2.getTime()) / 1000;//8000
        Long godziny = czas / 3600;// 8000/3600 = 2
        Long minuty = (czas - godziny * 3600) / 60;//8000-2*3600 = 800/60godziny
        return "Dzisiaj za " + godziny + " h " + minuty + " min";
    }
   /* public String zaIleCzasuJutro() {
        Date d1 = getDate();
        Date d2 = new Date();
        Long czas = (d1.getTime() - d2.getTime()) / 1000;//8000
        Long godziny = czas / 3600;// 8000/3600 = 2
        Long minuty = (czas - godziny * 3600) / 60;//8000-2*3600 = 800/60godziny
        return "Dzisiaj za " + godziny + " h " + minuty + " min";
        */
}

