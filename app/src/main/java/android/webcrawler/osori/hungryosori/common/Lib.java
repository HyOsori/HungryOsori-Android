package android.webcrawler.osori.hungryosori.common;

import android.text.TextUtils;

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