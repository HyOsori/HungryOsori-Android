package android.webcrawler.osori.hungryosori.Model;

import android.webcrawler.osori.hungryosori.common.Constant;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

/**
 * Created by kunju on 2016-08-26.
 */
public  class ParamModel {
    private String url;
    ArrayList<NameValuePair> params = new ArrayList<>();

    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }

    public String getParamStr() {
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
                result.append(URLEncoder.encode(pair.getKey(), Constant.DEFAULT_ENCODING));
                result.append("=");
                result.append(URLEncoder.encode(pair.getValue(), Constant.DEFAULT_ENCODING));
            }catch (UnsupportedEncodingException e){

            }
        }

        return result.toString();
    }
}