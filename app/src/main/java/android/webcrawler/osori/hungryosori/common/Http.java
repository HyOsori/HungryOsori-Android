package android.webcrawler.osori.hungryosori.Common;

import android.content.Context;
import android.webcrawler.osori.hungryosori.Model.ParamModel;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;


/**
 * Created by 고건주 on 2016-08-25.
 * 서버와 통신할 때 쓰는 클래스
 */
public class Http {

    public Http(Context context){
        this.mContext = context;
    }

    private Context mContext;

    public String send(ParamModel paramModel, boolean isLogin){
        if(isLogin == true){
            return sendLogin(paramModel);
        }else{
            return send(paramModel);
        }
    }

    private String send(ParamModel paramModel) {

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
                if(Constant.cookie != null) {
                    //urlConnection.setRequestProperty("cookie", Constant.cookie);
                }

                if(paramStr.length() > 0) {
                    out = new BufferedOutputStream(urlConnection.getOutputStream());
                    out.write(paramStr.getBytes(Constant.DEFAULT_ENCODING));
                    out.flush();
                }

                urlConnection.connect();

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

    public String sendGetMethod(ParamModel paramModel) {

        String result = null;

        try {
            final URL url = new URL(paramModel.getUrl());

            HttpURLConnection urlConnection = null;
            InputStream in = null;
            BufferedReader bReader = null;

            try {
                urlConnection = (HttpURLConnection) url.openConnection();

                urlConnection.setRequestMethod("GET");

                if(Constant.cookie != null) {
                   // urlConnection.setRequestProperty("cookie", Constant.cookie);
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


    private String sendLogin(ParamModel paramModel) {

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
                urlConnection.setInstanceFollowRedirects(false);

                out = new BufferedOutputStream(urlConnection.getOutputStream());

                out.write(paramStr.getBytes(Constant.DEFAULT_ENCODING));
                out.flush();

                urlConnection.connect();

                /** 쿠키 값 읽어오기 */
                String cookie = "";
                Map map = urlConnection.getHeaderFields();
                if (map.containsKey("Set-Cookie")) {
                    Collection c = (Collection) map.get("Set-Cookie");
                    for (Iterator i = c.iterator(); i.hasNext(); ) {
                        cookie += i.next() + ", ";
                    }
                }

                /** 쿠키 정보 저장 */
               // Pref.setCookie(mContext, cookie);
               // Pref.setCookie(mContext, cookie);

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