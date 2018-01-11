package android.webcrawler.osori.hungryosori.Method;
import android.util.Log;
import android.webcrawler.osori.hungryosori.Intercepter.AddCookiesInterceptor;
import android.webcrawler.osori.hungryosori.Intercepter.ReceivedCookiesInterceptor;
import android.webcrawler.osori.hungryosori.Interface.Method;
import android.webcrawler.osori.hungryosori.Model.NameValuePair;
import android.webcrawler.osori.hungryosori.Model.ParamModel;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by kunju on 2016-12-28.
 */
public class PostMethod extends Method {
    private static PostMethod instance = null;
    private OkHttpClient httpClient;

    private PostMethod(){

    }

    public static PostMethod getInstance(){
        if(instance == null)
            instance = new PostMethod();
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
        String result = "";
        Request request = builder.build();

        try {
            Response response = httpClient.newCall(request).execute();
            if(response.isSuccessful())
                return response.body().string();
        }catch (IOException e){
        }
        return null;
    }


    //    Overloading send method
    //    This is used to add authorization at header for getting whole crawler list
    public String send(ParamModel paramModel, String token) {
        httpClient          = new OkHttpClient().newBuilder().
                addInterceptor(new ReceivedCookiesInterceptor()).
                addInterceptor(new AddCookiesInterceptor()).
                build();

        Request.Builder builder = new Request.Builder().url(paramModel.getUrl());
        setParameter(paramModel.getParameters(), builder);
        String result = "";
        Request request = builder
                .header("Authorization" , "Token " + token)
                .build();

        try {
            Response response = httpClient.newCall(request).execute();
            if(response.isSuccessful())
                return response.body().string();
            else{

                Log.e("response error code", response.code()+"");
                Log.e("response error message", response.message());
            }
        }catch (IOException e){
        }
        return null;
//        httpClient          = new OkHttpClient().newBuilder().
//                addInterceptor(new ReceivedCookiesInterceptor()).
//                addInterceptor(new AddCookiesInterceptor()).
//                build();
//
//
//        try {
//            String urlString    = paramModel.getUrl();
//            String paramString  = paramModel.getParamStr();
//
//            final URL url = new URL(urlString);
//
//            Request.Builder builder = new Request.Builder().url(paramModel.getUrl());
//            setParameter(paramModel.getParameters(), builder);
//            Request request = builder.header("Authorization" , "Token " + token).build();
//            //TEST
//            paramString  = paramModel.getParamStr();
//            if(paramString != null && paramString.length() > 0){
//                Log.e("params_PostMeth2", paramString);
//            }
//            Log.e("request_postMeth2", request.toString());
//
//            Response response = httpClient.newCall(request).execute();
//            if(response.isSuccessful())
////                result = response.body().string();
//                return response.body().string();
//        }catch (IOException e){
//            Log.e("postMethod", "send_error");
//        }
//        return null;
    }

    private void setParameter(ArrayList<NameValuePair> parameters, Request.Builder builder){
        if(parameters != null){
            FormBody.Builder postData = new FormBody.Builder();
            Log.e("postMeth", "***************setParam****************");
            for(NameValuePair pair : parameters){
                if(pair.getKey() != null && pair.getValue() != null) {
                    Log.e(pair.getKey(), pair.getValue());
                    postData.add(pair.getKey(), pair.getValue());
                }
            }
            Log.e("postMeth", "****************************************");
            builder.post(postData.build());
        }
    }

}
