package android.webcrawler.osori.hungryosori.Common;

import android.text.TextUtils;

/**
 * Created by 고건주 on 2016-08-25.
 * 공통으로 사용하는 라이브러리 함수를 모은 클래스로 public static 으로 구현
 */

public class Lib {

    /** 이메일 형식이 올바른지 체크하는 함수 */
    public final static boolean isValidEmail(CharSequence target) {
        if (TextUtils.isEmpty(target)) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target)
                    .matches();
        }
    }

}