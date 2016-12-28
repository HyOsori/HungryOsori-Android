package android.webcrawler.osori.hungryosori.Interface;

import android.webcrawler.osori.hungryosori.Common.Constant;
import android.webcrawler.osori.hungryosori.Common.HttpResult;
import android.webcrawler.osori.hungryosori.Model.ParamModel;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by kunju on 2016-12-28.
 */
public abstract class Method {
    private     boolean cookieSet;
    protected   HttpURLConnection urlConnection;

    public Method(){
        cookieSet = false;
    }

    public boolean isCookieSet() {
        return cookieSet;
    }

    public void setCookieSet(boolean cookieSet) {
        this.cookieSet = cookieSet;
    }

    public abstract HttpResult send(ParamModel paramModel);

    protected void setCookie(){
        if (isCookieSet()) {
            urlConnection.setRequestProperty("cookie", Constant.cookie);
        }
    }

    protected String readResponseMessage(){
        StringBuilder strBuilder = new StringBuilder();
        InputStream in          = null;
        BufferedReader bReader  = null;
        try {
            in = new BufferedInputStream(urlConnection.getInputStream());
            bReader = new BufferedReader(new InputStreamReader(in, Constant.DEFAULT_ENCODING));

            String line;
            while ((line = bReader.readLine()) != null) {
                strBuilder.append(line);
            }
        }catch (IOException e){

        }finally {
            if(in != null){
                try {
                    in.close();
                    if(bReader != null)
                        bReader.close();
                }catch (IOException e){

                }
            }
        }

        return strBuilder.toString();
    }
    protected String readCookie(){
        /** 쿠키 값 읽어오기 */
        StringBuilder cookie = new StringBuilder();
        Map map = urlConnection.getHeaderFields();
        if (map != null && map.containsKey("Set-Cookie")) {
            Collection c = (Collection) map.get("Set-Cookie");
            for (Iterator i = c.iterator(); i.hasNext(); ) {
                cookie.append(i.next() + ", ");
            }
        }
        return cookie.toString();
    }
}
