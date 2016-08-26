package android.webcrawler.osori.hungryosori.common;

import android.content.Context;
import android.webcrawler.osori.hungryosori.Model.NameValuePair;
import android.webcrawler.osori.hungryosori.Model.ParamModel;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;


/**
 * Created by 고건주 on 2016-08-25.
 */
public class Http {

    public Http(Context context){
        this.mContext = context;
        cookie = Pref.getCookie(context);
    }

    private Context mContext;
    private String cookie;                  // Cookie 정보

    public String send(ParamModel paramModel) {

        String result = null;

        try {
            final URL url = new URL(paramModel.getUrl());

            String paramStr = paramModel.getParamStr();

            HttpURLConnection urlConnection = null;
            OutputStream out = null;
            InputStream in = null;
            BufferedReader bReader = null;

            try {
                urlConnection = (HttpURLConnection) url.openConnection();

                urlConnection.setDoOutput(true);
                urlConnection.setDoInput(true);
                urlConnection.setConnectTimeout(Constant.TIME_OUT_MILLIS);
                urlConnection.setReadTimeout(Constant.TIME_OUT_MILLIS);
                urlConnection.setRequestMethod("POST");

                if(cookie.equals(Pref.DEFAULT_STRING_VALUE)) {
                    urlConnection.setInstanceFollowRedirects(false);
                }else{
                    urlConnection.setRequestProperty("cookie", cookie);
                }

                out = new BufferedOutputStream(urlConnection.getOutputStream());

                out.write(paramStr.getBytes(Constant.DEFAULT_ENCODING));
                out.flush();

                urlConnection.connect();

                if(cookie.equals(Pref.DEFAULT_STRING_VALUE)) {

                    Map map = urlConnection.getHeaderFields();
                    if (map.containsKey("Set-Cookie")) {
                        Collection c = (Collection) map.get("Set-Cookie");
                        for (Iterator i = c.iterator(); i.hasNext(); ) {
                            cookie += (String) i.next() + ", ";
                        }
                    }
                    Pref.setCookie(mContext, cookie);
                }

                in = new BufferedInputStream(urlConnection.getInputStream());

                bReader = new BufferedReader(new InputStreamReader(in, Constant.DEFAULT_ENCODING));
                StringBuilder strBuilder = new StringBuilder();

                String line;
                while ((line = bReader.readLine()) != null) {
                    strBuilder.append(line);
                }

                result = strBuilder.toString();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } finally {
                try {
                    bReader.close();
                    in.close();
                    out.close();
                    urlConnection.disconnect();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (NullPointerException e2) {
                    // TODO: handle exception
                    e2.printStackTrace();
                }
            }
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return result;
    }

}