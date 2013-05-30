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

/**
 * Aktywność wyświetlająca listę seansów, czyli nasz główny ekran
 * Klasa SeansListActivity dziedziczy po klasie Activity, tzn. klasa Activity jest klasą bazową dla tej klasy,
 * Rozszerzając ją dodajemy nowe funkcje do tych które są już zdefinowane a Activity
 */
public class SeansListActivity extends Activity {

    @Override
    /**
     * Metoda z klasy przeciążona, czyli taka metoda już istnieje, ale my definiujemy jej kod na nowo
     * Wywoływana podczas uruchamiania aktywności
     */
    protected void onCreate(Bundle savedInstanceState) {
        //wywołujemy bazową implementację tej metody na wszelki wypadek
        super.onCreate(savedInstanceState);
        //ustawiamy layout widoku na zdefiniowany w pliku res/layout/activity_seans_list.xml
        setContentView(R.layout.activity_seans_list);
        //pobieramy kontrolkę o id 'listView' w której będziemy wyświetlać listę seansów
        ListView listView = (ListView) findViewById(R.id.listView);

        //ustawiamy co ma się dziać gdy ktoś kliknie na element list (setOnItemClickListener) poprzez stworzenie inline anonimowej klasy
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                //pobieramy mapę z danymi o seansie, które stworzyliśmy po pobraniu informacji o telefonie. Zobacz ReadSeanseTask
                //HashMap to implementacja Mapy, czyli zbioru obiektów, do których możemy odwoływać się poprzez klucz - unikalną nazwę
                HashMap<String, ?> fields = (HashMap<String, ?>) parent.getItemAtPosition(position);
                //Pobieramy z pod klucza "seans" obiekt typu Seans
                Seans seans = (Seans) fields.get("seans");

                //Kawałek kodu do wyświetlania komunikatu na ekranie
                Context context = getApplicationContext();
                CharSequence text = "Trwa ładowanie...";
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(context, text, duration);
                toast.show();

                //odpalamy w oddzielnej pętli pobranie i wyświetlenie szczegółów seansu
                //musimy to zrobić w oddzielnym wątku, ponieważ będzie ściągać dane z internetu
                //Standardowo Android nie pozwala robić tego w głównym wątku, gdyż mogło by to powodować
                //brak responsywności aplikacji podczas oczekiwania na dane

                //Najpierw tworzymy obiekt ReadExtraDataSeansTask i przekazujemy mu jako parametr startowy
                //(czyli do tzw. konstruktora) obiekt SeansListActivity, z którego wywołaliśmy to zadanie
                //oraz wywołujemy metodę execute z parametrem będącym obiektem seansu, do którego chcemy
                //doczytać opis i zdjęcie
                new ReadExtraDataSeansTask(SeansListActivity.this).execute(seans);
            }
        });

        inicjujObslugeBanera();

        initReadList();

    }

    /**
     * Ustawia aby po kliknięciu na baner coder dojo otwierała się strona http://coder-dojo.pl
     */
    private void inicjujObslugeBanera() {
        //tworzymy obiekt obsługujący kliknięcie na kontrolce. Ponieważ w tym przypadku zachowanie będzie identyczne
        //dla trzech kontrolek(obrazek logo, tekst oraz link) to tworzymym oddzielny obiekt i podpinamy go pod tego obiekty
        //wczesniej robiliśmy to w jednej linii tworząc anonimowy obiekt (tzw. inline)
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
    }

    /**
     * Pomocnicza metoda wywoływana gdy chcemy zainicjować pobieranie danych o wszystkich seansach
     * Metoda odpala pobieranie danych (ReadSeanseTask) i natychmiast wychodzi nie czekając na skończenie tej operacji.
     * Oprócz tego wyświetla komunikat "Pobieranie"
     */
    private void initReadList() {
        new ReadSeanseTask().execute(this);
        Context context = getApplicationContext();
        String text = "Pobieranie...";
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

    /**
     * Metoda wywoływana przez system, jeśli użytkownik kliknie w jakąś pozycję w menu. Ponieważ mamy tylko jedną pozycję,
     * która odświeża listę, to po prostu wywołujemy metodę initReadList
     * @param item pozycja menu, która została kliknięta
     * @return zawsze true. Zgodnie z opisem bazowej funkcji powinna zwrócić true, jeśli metoda obsłużyła kliknięcie w menu
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        initReadList();
        return true;
    }

    /**
     * Metoda wywoływana przez system, gdy trzeba pokazać menu opcji
     * @param menu menu które będzie wyświetlone
     * @return true, aby menu została wyświetlona, gdyby było false, menu nie pokazało by się
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Elementy menu zdefiniowaliśmy w res/menu/seans_list.xml
        getMenuInflater().inflate(R.menu.seans_list, menu);
        return true;
    }
    
}
