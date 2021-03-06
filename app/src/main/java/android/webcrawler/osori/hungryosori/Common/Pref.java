package android.webcrawler.osori.hungryosori.Common;

/**
 * Created by 고건주 on 2016-08-25.
 */

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Set;

/**
 * Created by 고건주&김규민 on 2016-08-25.
 * Shared Preference 관련 함수들
 */
public class Pref {

    private static SharedPreferences preference = null;

    /** 공통 상수값 */
    private static final String SHARED_PREF_NAME        = "hungryOsori.sharedPref";
    public static final boolean DEFAULT_BOOLEAN_VALUE   = false;
    public static final String  DEFAULT_STRING_VALUE    = null;

    /** Shared Preference 키 값들 */
    private static final String PREF_KEY_KEEP_LOGIN         = "keepLogin";
    private static final String PREF_KEY_USER_KEY           = "userKey";
    private static final String PREF_KEY_USER_ID            = "userID";
    private static final String PREF_KEY_USER_PASSWORD      = "userPassword";
    private static final String PREF_KEY_COOKIE             = "cookie";
    private static final String PREF_KEY_PUSHTOKEN          = "pushToken";

    public static void init(Context context){
        preference = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
    }

    /** Keep Login */
    public static void setKeepLogin(boolean keepLogin){
        if(preference != null) {
            preference.edit().putBoolean(PREF_KEY_KEEP_LOGIN, keepLogin).apply();
            Constant.keepLogin = keepLogin;
        }
    }

    public static boolean getKeepLogin(){
        boolean checked = Pref.DEFAULT_BOOLEAN_VALUE;
        if(preference != null)
            checked = preference.getBoolean(PREF_KEY_KEEP_LOGIN, DEFAULT_BOOLEAN_VALUE);
        return checked;
    }

    /** User Key */
    public static void setUserKey(String userKey){
        if(preference != null) {
            preference.edit().putString(PREF_KEY_USER_KEY, userKey).apply();
            Constant.userKey = userKey;
        }
    }

    public static String getUserKey() {
        String userKey = Pref.DEFAULT_STRING_VALUE;
        if(preference != null) {
            userKey = preference.getString(PREF_KEY_USER_KEY, DEFAULT_STRING_VALUE);
        }
        return userKey;
    }

    /** User ID */
    public static void setUserID(String userID){
        if(preference != null){
            preference.edit().putString(PREF_KEY_USER_ID, userID).apply();
            Constant.userID = userID;
        }
    }

    public static String getUserID() {
        String userID = DEFAULT_STRING_VALUE;
        if(preference != null){
            userID = preference.getString(PREF_KEY_USER_ID, DEFAULT_STRING_VALUE);
        }
        return userID;
    }

    /** Password */
    public static void setUserPassword(String userPassword){
        if(preference != null) {
            preference.edit().putString(PREF_KEY_USER_PASSWORD, userPassword).apply();
            Constant.userPassword = userPassword;
        }
    }

    public static String getUserPassword() {
        String userKey = DEFAULT_STRING_VALUE;
        if(preference != null){
            userKey = preference.getString(PREF_KEY_USER_PASSWORD, DEFAULT_STRING_VALUE);
        }
        return userKey;
    }

    /** Cookie */
    public static void setCookie(Set<String> cookie)
    {
        if(preference != null){
            preference.edit().putStringSet(PREF_KEY_COOKIE, cookie).apply();
            Constant.cookie = cookie;
        }
    }

    public static Set<String> getCookie()
    {
        Set<String> cookie = null;
        if(preference != null){
            cookie =  preference.getStringSet(PREF_KEY_COOKIE, null);
        }
        return cookie;
    }

    /** Push Token */
    public static void setPushToken(String pushToken){
        if(preference != null){
            preference.edit().putString(PREF_KEY_PUSHTOKEN, pushToken).apply();
            Constant.pushToken = pushToken;
        }
    }

    public static String getPushToken() {
        String pushToken = DEFAULT_STRING_VALUE;
        if (preference != null) {
            pushToken = preference.getString(PREF_KEY_PUSHTOKEN, DEFAULT_STRING_VALUE);
        }
        return pushToken;
    }

    public static boolean resetLogin(){
        if(preference == null){
            return false;
        }

        preference.edit().putStringSet(PREF_KEY_COOKIE, null).apply();
        preference.edit().putBoolean(PREF_KEY_KEEP_LOGIN, DEFAULT_BOOLEAN_VALUE).apply();
        preference.edit().putString(PREF_KEY_USER_ID, DEFAULT_STRING_VALUE).apply();
        preference.edit().putString(PREF_KEY_USER_KEY, DEFAULT_STRING_VALUE).apply();
        preference.edit().putString(PREF_KEY_USER_PASSWORD, DEFAULT_STRING_VALUE).apply();

        Constant.keepLogin      = DEFAULT_BOOLEAN_VALUE;
        Constant.userKey        = DEFAULT_STRING_VALUE;
        Constant.cookie         = null;
        Constant.userID         = DEFAULT_STRING_VALUE;
        Constant.userPassword   = DEFAULT_STRING_VALUE;

        return true;
    }
}