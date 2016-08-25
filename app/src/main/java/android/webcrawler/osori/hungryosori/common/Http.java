package android.webcrawler.osori.hungryosori.common;

import android.webcrawler.osori.hungryosori.Model.NameValuePair;

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


/**
 * Created by 고건주 on 2016-08-25.
 */
public class Http {

    public static class ParamModel {
        private String url;
        ArrayList<NameValuePair> params = new ArrayList<>();

        private String getUrl() {
            return url;
        }
        public void setUrl(String url) {
            this.url = url;
        }

        private String getParamStr() {
            return getQuery();
        }

        public void setParamStr(String key, String value) {
            params.add(new NameValuePair(key, value));
        }

        private String getQuery(){
            StringBuilder result = new StringBuilder();
            boolean first = true;

            for (NameValuePair pair : params)
            {
                if (first)
                    first = false;
                else
                    result.append("&");

                try {
                    result.append(URLEncoder.encode(pair.getKey(), DEFAULT_ENCODING));
                    result.append("=");
                    result.append(URLEncoder.encode(pair.getValue(), DEFAULT_ENCODING));
                }catch (UnsupportedEncodingException e){

                }
            }

            return result.toString();
        }
    }

    private static final int TIME_OUT_MILLIS = 2000;
    private static final String DEFAULT_ENCODING = "UTF-8";

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
                urlConnection.setConnectTimeout(TIME_OUT_MILLIS);
                urlConnection.setReadTimeout(TIME_OUT_MILLIS);
                urlConnection.setRequestMethod("POST");

                out = new BufferedOutputStream(urlConnection.getOutputStream());

                out.write(paramStr.getBytes(DEFAULT_ENCODING));
                out.flush();

                urlConnection.connect();

                in = new BufferedInputStream(urlConnection.getInputStream());

                bReader = new BufferedReader(new InputStreamReader(in, DEFAULT_ENCODING));
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