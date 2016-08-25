package android.webcrawler.osori.hungryosori.common;

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

/**
 * Created by kunju on 2016-08-25.
 */
public class Http {

    public static class ParamModel {
        private String url;
        private String paramStr;

        private String getUrl() {
            return url;
        }
        public void setUrl(String url) {
            this.url = url;
        }
        private String getParamStr() {
            return paramStr;
        }
        public void setParamStr(String paramStr) {
            this.paramStr = paramStr;
        }
    }

    public static final  int METHOD_POST = 1;
    private static final int DEFAULT_METHOD = METHOD_POST;

    private static final int CONNECTION_TIME_OUT_MILLIS = 6000;
    private static final String DEFAULT_ENCODING = "UTF-8";

    private String encoding;
    private int method;

    public Http() {
        encoding = DEFAULT_ENCODING;
        method = DEFAULT_METHOD;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    public String send(ParamModel paramModel) {
        return send(paramModel, method);
    }

    public String send(ParamModel paramModel, int method) {
        if ((method != METHOD_POST)) {
            return null;
        }

        String result = null;
        try {
            final URL url = new URL(paramModel.getUrl());

            String paramStr = paramModel.getParamStr();
            if (paramStr == null) {
                paramStr = "";
            }

            HttpURLConnection urlConnection = null;
            OutputStream out = null;
            InputStream in = null;
            BufferedReader bReader = null;

            try {
                urlConnection = (HttpURLConnection) url.openConnection();

                urlConnection.setDoOutput(true);
                urlConnection.setDoInput(true);
                urlConnection.setConnectTimeout(CONNECTION_TIME_OUT_MILLIS);
                urlConnection.setRequestMethod("POST");
                urlConnection.setChunkedStreamingMode(0);

                out = new BufferedOutputStream(urlConnection.getOutputStream());

                out.write(paramStr.getBytes(encoding));

                in = new BufferedInputStream(urlConnection.getInputStream());

                bReader = new BufferedReader(new InputStreamReader(in, encoding));
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