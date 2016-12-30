package android.webcrawler.osori.hungryosori.Model;

import android.webcrawler.osori.hungryosori.Common.Constant;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

/**
 * Created by 고건주 on 2016-08-26.
 */
public  class ParamModel {
    private String url;
    ArrayList<NameValuePair> params = new ArrayList<>();

    public ArrayList<NameValuePair> getParams() {
        return params;
    }

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
        if(params.size() == 0)  return null;
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

            }catch (Exception e){

            }
        }

        return result.toString();
    }
}