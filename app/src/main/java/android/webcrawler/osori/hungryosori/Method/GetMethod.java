package android.webcrawler.osori.hungryosori.Method;
import android.webcrawler.osori.hungryosori.Common.HttpResult;
import android.webcrawler.osori.hungryosori.Interface.Method;
import android.webcrawler.osori.hungryosori.Model.ParamModel;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by kunju on 2016-12-28.
 */
public class GetMethod extends Method{
    private static GetMethod instance = null;

    private GetMethod(){

    }

    public static GetMethod getInstance(){
        if(instance == null)
            instance = new GetMethod();
        return instance;
    }

    @Override
    public HttpResult send(ParamModel paramModel) {
        String response = null;
        String cookie   = null;
        try {
            String urlString    = paramModel.getUrl();
            String paramString  = paramModel.getParamStr();
            if(paramString != null && paramString.length() > 0){
                urlString += "?" + paramString;
            }
            final URL url = new URL(urlString);

            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            setCookie();

            response = readResponseMessage();
            cookie   = readCookie();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }finally {
            urlConnection.disconnect();
        }
        HttpResult result = new HttpResult(response, cookie);
        return result;
    }
}

