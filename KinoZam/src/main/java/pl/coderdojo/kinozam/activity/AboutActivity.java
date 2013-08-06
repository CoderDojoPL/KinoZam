package pl.coderdojo.kinozam.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import pl.coderdojo.kinozam.R;

/**
 * Created by luk on 15.06.13.
 */
public class AboutActivity extends Activity {


    @Override
    /**
     * Metoda z klasy przeciążona, czyli taka metoda już istnieje, ale my definiujemy jej kod na nowo
     * Wywoływana podczas uruchamiania aktywności
     */
    protected void onCreate(Bundle savedInstanceState) {
        //wywołujemy bazową implementację tej metody na wszelki wypadek
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //ustawiamy layout widoku na zdefiniowany w pliku res/layout/activity_seans_list.xml
        setContentView(R.layout.activity_about);


    }


}
