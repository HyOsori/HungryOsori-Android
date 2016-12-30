package android.webcrawler.osori.hungryosori.Method;

import android.webcrawler.osori.hungryosori.Common.Constant;
import android.webcrawler.osori.hungryosori.Common.HttpResult;
import android.webcrawler.osori.hungryosori.Interface.Method;
import android.webcrawler.osori.hungryosori.Model.ParamModel;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by kunju on 2016-12-28.
 */
public class PostMethod extends Method {
    private static PostMethod instance = null;

    private PostMethod(){

    }

    public static PostMethod getInstance(){
        if(instance == null)
            instance = new PostMethod();
        return instance;
    }

    @Override
    public HttpResult send(ParamModel paramModel) {
        String response = null;
        String cookie   = null;
        try {
            String urlString    = paramModel.getUrl();
            final URL url = new URL(urlString);

            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setDoOutput(true);
            urlConnection.setDoInput(true);
            urlConnection.setConnectTimeout(Constant.TIME_OUT_MILLIS);
            urlConnection.setReadTimeout(Constant.TIME_OUT_MILLIS);
            urlConnection.setRequestMethod("POST");

            setCookie();
            setParameter(paramModel.getParamStr());

            urlConnection.connect();

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

    private void setParameter(String paramStr){
        OutputStream out = null;
        if(paramStr.length() > 0) {
            try {
                out = new BufferedOutputStream(urlConnection.getOutputStream());
                out.write(paramStr.getBytes(Constant.DEFAULT_ENCODING));
                out.flush();
            }catch (IOException e){

            }finally {
                if(out != null){
                    try{
                        out.close();
                    }catch (IOException e){

                    }
                }
            }
        }
    }

}
