package android.webcrawler.osori.hungryosori.common;

/**
 * Created by kunju on 2016-08-25.
 */

import android.content.Context;

/**
 * Created by 고건주 on 2016-08-25.
 * Shared Preference 설정
 */
public class Pref {

    /** 공통 상수값 */
    private static final String SHARED_PREF_NAME = "hungryOsori.sharedPref";
    public static final boolean DEFAULT_BOOLEAN_VALUE   = false;
    public static final String  DEFAULT_STRING_VALUE    = null;

    /** 로그인 관련 상수 */
    private static final String PREF_KEY_KEEP_LOGIN     = "keepLogin";
    private static final String PREF_KEY_USER_KEY       = "userKey";
    private static final String PREF_KEY_USER_ID        = "userID";
    private static final String PREF_KEY_USER_PASSWORD  = "userPassword";
    private static final String PREF_KEY_COOKIE         = "cookie";

    /** 로그인 관련 Shared Preference 함수들 */
    public static void setKeepLogin(Context context, boolean isChecked){
        if(context == null){
            return;
        }
        context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
                .edit().putBoolean(PREF_KEY_KEEP_LOGIN, isChecked).apply();
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
    }

    public static String getUserKey(Context context) {
        if(context == null){
            return null;
        }
        String userKey = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE).
                getString(PREF_KEY_USER_KEY, DEFAULT_STRING_VALUE);
        return userKey;
    }

    public static void setUserID(Context context, String userEmail){
        if(context == null){
            return;
        }
        context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
                .edit().putString(PREF_KEY_USER_ID, userEmail).apply();
    }

    public static String getUserID(Context context) {
        if(context == null){
            return null;
        }
        String userKey = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE).
                getString(PREF_KEY_USER_ID, DEFAULT_STRING_VALUE);
        return userKey;
    }

    public static void setUserPassword(Context context, String userEmail){
        if(context == null){
            return;
        }
        context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
                .edit().putString(PREF_KEY_USER_PASSWORD, userEmail).apply();
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
    }
}
