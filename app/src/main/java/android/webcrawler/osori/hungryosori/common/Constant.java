package android.webcrawler.osori.hungryosori.Common;

import java.util.Set;

/**
 * Created by 고건주 on 2016-08-25.
 * 상수 값을 저장하는 클래스
 */
public class Constant {
    public static final int PASSWORD_LENGTH_MIN = 6;    // 최소 패스워드 길이
    public static final int DELAY_TIME = 1000;
    public static final int PAGE_MY  = 0;    // My page
    public static final int PAGE_ALL = 1;    // All page

    /** 로그인 관련 값 */
    public static String        userKey;          // 사용자 Key
    public static boolean       keepLogin;
    public static String        userID;
    public static String        userPassword;
    public static Set<String>   cookie;
    public static String        userNewPassword;
    public static String        pushToken;

    /** Http 관련 상수 */
    public static final int     TIME_OUT_MILLIS = 2000;
    public static final String  DEFAULT_ENCODING = "UTF-8";

    public static final String MESSAGE           = "message";
    public static final String MESSAGE_SUCCESS   = "Success";
    public static final String SERVER_URL        = "http://52.78.113.6:8000";
    public static final int    SERVER_PORT       = 8000;
}