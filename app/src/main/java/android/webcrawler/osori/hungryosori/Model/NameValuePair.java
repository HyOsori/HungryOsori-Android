package android.webcrawler.osori.hungryosori.Model;

/**
 * Created by kunju on 2016-08-25.
 */
public class NameValuePair {
    private String key;
    private String value;

    public NameValuePair(String key, String value){
        this.key = key;
        this.value = value;
    }

    public String getKey(){
        return this.key;
    }

    public String getValue()
    {
        return this.value;
    }
}
