package android.webcrawler.osori.hungryosori.Method;

import android.webcrawler.osori.hungryosori.Intercepter.AddCookiesInterceptor;
import android.webcrawler.osori.hungryosori.Intercepter.ReceivedCookiesInterceptor;
import android.webcrawler.osori.hungryosori.Interface.Method;
import android.webcrawler.osori.hungryosori.Model.NameValuePair;
import android.webcrawler.osori.hungryosori.Model.ParamModel;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by kunju on 2016-12-30.
 */
public class PutMethod extends Method{
    private static PutMethod instance;
    private OkHttpClient httpClient;

    private PutMethod(){}

    public static PutMethod getInstance(){
        if(instance == null){
            instance = new PutMethod();
        }
        return instance;
    }

    @Override
    public String send(ParamModel paramModel) {

        httpClient          = new OkHttpClient().newBuilder().
                addInterceptor(new ReceivedCookiesInterceptor()).
                addInterceptor(new AddCookiesInterceptor()).
                build();

        Request.Builder builder = new Request.Builder().url(paramModel.getUrl());
        setParameter(paramModel.getParameters(), builder);

        Request request = builder.build();
        try {
            Response response = httpClient.newCall(request).execute();
            if(response.isSuccessful())
                return response.body().string();
        }catch (IOException e){
        }
        return null;
    }

    private void setParameter(ArrayList<NameValuePair> parameters, Request.Builder builder){
        if(parameters != null){
            FormBody.Builder postData = new FormBody.Builder();
            for(NameValuePair pair : parameters){
                if(pair.getKey() != null && pair.getValue() != null) {
                    postData.add(pair.getKey(), pair.getValue());
                }
            }
            builder.put(postData.build());
        }
    }
}
