package android.webcrawler.osori.hungryosori.Method;
import android.webcrawler.osori.hungryosori.Intercepter.AddCookiesInterceptor;
import android.webcrawler.osori.hungryosori.Intercepter.ReceivedCookiesInterceptor;
import android.webcrawler.osori.hungryosori.Interface.Method;
import android.webcrawler.osori.hungryosori.Model.ParamModel;
import java.io.IOException;
import java.net.URL;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by kunju on 2016-12-28.
 */
public class GetMethod extends Method{
    private static GetMethod instance = null;
    private OkHttpClient httpClient;

    private GetMethod(){}

    public static GetMethod getInstance(){
        if(instance == null)
            instance = new GetMethod();
        return instance;
    }

    @Override
    public String send(ParamModel paramModel) {
        httpClient          = new OkHttpClient().newBuilder().
                addInterceptor(new ReceivedCookiesInterceptor()).
                addInterceptor(new AddCookiesInterceptor()).
                build();

        try {
            String urlString    = paramModel.getUrl();
            String paramString  = paramModel.getParamStr();
            if(paramString != null && paramString.length() > 0){
                 urlString += "?" + paramString;
            }
            final URL url = new URL(urlString);

            Request  request = new Request.Builder().url(url).build();
            Response response = httpClient.newCall(request).execute();
            if(response.isSuccessful())
                return response.body().string();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return null;
    }
}

