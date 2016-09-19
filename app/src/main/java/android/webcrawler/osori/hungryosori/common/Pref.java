package android.webcrawler.osori.hungryosori.Common;

/**
 * Created by 고건주 on 2016-08-25.
 */

import android.content.Context;

/**
 * Created by 고건주 on 2016-08-25.
 * Shared Preference 관련 함수들
 */
public class Pref {

    /** 공통 상수값 */
    private static final String SHARED_PREF_NAME = "hungryOsori.sharedPref";
    public static final boolean DEFAULT_BOOLEAN_VALUE   = false;
    public static final String  DEFAULT_STRING_VALUE    = null;

    /** Shared Preference 키 값들 */
    private static final String PREF_KEY_KEEP_LOGIN     = "keepLogin";
    private static final String PREF_KEY_USER_KEY       = "userKey";
    private static final String PREF_KEY_USER_ID        = "userID";
    private static final String PREF_KEY_USER_PASSWORD  = "userPassword";
    private static final String PREF_KEY_COOKIE         = "cookie";
    private static final String PREF_KEY_PUSHTOKEN         = "pushToken";
    /** 로그인 관련 Shared Preference 함수들 */
    public static void setKeepLogin(Context context, boolean keepLogin){
        if(context == null){
            return;
        }
        context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
                .edit().putBoolean(PREF_KEY_KEEP_LOGIN, keepLogin).apply();
        Constant.keepLogin = keepLogin;
    }

    public static boolean getKeepLogin(Context context){
        if(context == null){
            return DEFAULT_BOOLEAN_VALUE;
        }
        boolean checked = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE).
                getBoolean(PREF_KEY_KEEP_LOGIN, DEFAULT_BOOLEAN_VALUE);
        return checked;
    }

    public static void setUserKey(Context context, String userKey){
        if(context == null){
            return;
        }
        context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
                .edit().putString(PREF_KEY_USER_KEY, userKey).apply();
        Constant.userKey = userKey;
    }

    public static String getUserKey(Context context) {
        if(context == null){
            return null;
        }
        String userKey = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE).
                getString(PREF_KEY_USER_KEY, DEFAULT_STRING_VALUE);
        return userKey;
    }

    public static void setUserID(Context context, String userID){
        if(context == null){
            return;
        }
        context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
                .edit().putString(PREF_KEY_USER_ID, userID).apply();
        Constant.userID = userID;
    }

    public static String getUserID(Context context) {
        if(context == null){
            return null;
        }
        String userKey = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE).
                getString(PREF_KEY_USER_ID, DEFAULT_STRING_VALUE);
        return userKey;
    }

    public static void setUserPassword(Context context, String userPassword){
        if(context == null){
            return;
        }
        context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
                .edit().putString(PREF_KEY_USER_PASSWORD, userPassword).apply();
        Constant.userPassword = userPassword;
    }

    public static String getUserPassword(Context context) {
        if(context == null){
            return null;
        }
        String userKey = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE).
                getString(PREF_KEY_USER_PASSWORD, DEFAULT_STRING_VALUE);
        return userKey;
    }

    public static String getCookie(Context context)
    {
        if(context == null){
            return null;
        }
        String cookie =  context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE).
                getString(PREF_KEY_COOKIE, DEFAULT_STRING_VALUE);
        return cookie;
    }

    public static void setCookie(Context context, String cookie)
    {
        if(context == null){
            return;
        }
        context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
                .edit().putString(PREF_KEY_COOKIE, cookie).apply();
        Constant.cookie = cookie;
    }

    public static void setPushToken(Context context, String pushToken){
        if(context == null){
            return;
        }
        context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
                .edit().putString(PREF_KEY_PUSHTOKEN, pushToken).apply();
        Constant.pushToken = pushToken;
    }

    public static String getPushtoken(Context context) {
        if (context == null) {
            return null;
        }
        String userKey = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE).
                getString(PREF_KEY_PUSHTOKEN, DEFAULT_STRING_VALUE);
        return userKey;
    }

    public static boolean resetLogin(Context context){
        if(context == null){
            return false;
        }
        context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
                .edit().putString(PREF_KEY_COOKIE, DEFAULT_STRING_VALUE).apply();
        context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
                .edit().putBoolean(PREF_KEY_KEEP_LOGIN, DEFAULT_BOOLEAN_VALUE).apply();
        context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
                .edit().putString(PREF_KEY_USER_ID, DEFAULT_STRING_VALUE).apply();
        context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
                .edit().putString(PREF_KEY_USER_KEY, DEFAULT_STRING_VALUE).apply();
        context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
                .edit().putString(PREF_KEY_USER_PASSWORD, DEFAULT_STRING_VALUE).apply();

        Constant.keepLogin      = Pref.getKeepLogin(context);
        Constant.userKey        = Pref.getUserKey(context);
        Constant.cookie         = Pref.getCookie(context);
        Constant.userID         = Pref.getUserID(context);
        Constant.userPassword   = Pref.getUserPassword(context);

        return true;
    }
}