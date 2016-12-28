package android.webcrawler.osori.hungryosori.Common;

/**
 * Created by kunju on 2016-12-28.
 */
public class HttpResult {
    private String response;
    private String cookie;

    public HttpResult(String response, String cookie){
        this.response = response;
        this.cookie   = cookie;
    }

    public String getResponse() {
        return response;
    }

    public String getCookie() {
        return cookie;
    }

}
