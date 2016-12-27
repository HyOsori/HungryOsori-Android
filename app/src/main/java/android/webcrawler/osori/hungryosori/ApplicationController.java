package android.webcrawler.osori.hungryosori;

import android.app.Application;
import android.webcrawler.osori.hungryosori.Interface.NetworkService;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.GsonConverterFactory;
import retrofit2.Retrofit;

/**
 * Created by kunju on 2016-12-27.
 */
public class ApplicationController extends Application{
    private static ApplicationController instance;
    private NetworkService networkService;
    private String baseUrl;

    public  static ApplicationController getInstance(){
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        ApplicationController.instance = this;
    }

    public  NetworkService getNetworkService(){
        return networkService;
    }

    public void buildNetworkService(String ip, int port){
        synchronized (ApplicationController.class){
            if(networkService == null){
                baseUrl = String.format("http://%s:%d", ip, port);
                Gson gson = new GsonBuilder().create();

                GsonConverterFactory factory = GsonConverterFactory.create(gson);
                // 서버에서 json 형식으로 데이터를 보내고 이를 파싱해서 받아오기 위해서 사용

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(baseUrl)
                        .addConverterFactory(factory)
                        .build();

                networkService = retrofit.create(NetworkService.class);
            }
        }
    }

}
