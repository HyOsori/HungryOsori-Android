package android.webcrawler.osori.hungryosori.Model;

/**
 * Created by kunju on 2016-08-25.
 */
public class LoginResult {

    private String message;
    private String user_key;

    public String getMessage(){
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUser_key(){
        return user_key;
    }

    public void setUser_key(String user_key){
        this.user_key = user_key;
    }

}
