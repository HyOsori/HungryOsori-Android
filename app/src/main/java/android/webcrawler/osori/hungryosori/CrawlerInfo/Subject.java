package android.webcrawler.osori.hungryosori.CrawlerInfo;


import android.widget.ArrayAdapter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by kunju on 2016-12-17.
 */
public abstract class Subject {
    private List<ArrayAdapter> observers = new ArrayList<ArrayAdapter>();

    public void attach(ArrayAdapter adapter){
        observers.add(adapter);
    }

    public void detach(ArrayAdapter adapter){
        observers.remove(adapter);
    }

    public void notifyObservers(){
        for(ArrayAdapter o : observers){
            o.notifyDataSetChanged();
        }
    }
}
