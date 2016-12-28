package android.webcrawler.osori.hungryosori.Common;

import android.webcrawler.osori.hungryosori.Interface.Method;
import android.webcrawler.osori.hungryosori.Method.PostMethod;
import android.webcrawler.osori.hungryosori.Model.ParamModel;

/**
 * Created by kunju on 2016-12-28.
 */
public class Http2{

    private Method method = PostMethod.getInstance();
    private ParamModel paramModel;

    public Http2(ParamModel paramModel){
        this.paramModel = paramModel;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public void setCookie(boolean cookie){
        method.setCookieSet(cookie);
    }

    public HttpResult send(){
        return method.send(this.paramModel);
    }


}
