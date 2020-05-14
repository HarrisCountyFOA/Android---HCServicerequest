package servicesequest.hctx.net.Utility;

import android.text.TextUtils;
import android.util.Patterns;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Valid {
    // validating email id
    public static boolean isValidEmail(String email) {
        if (!TextUtils.isEmpty(email)) {
            Pattern pattern = Patterns.EMAIL_ADDRESS;
            Matcher matcher = pattern.matcher(email);
            return matcher.matches();
        }
        return true;
    }

    public static boolean isValidMobile(String mobile) {
        if (!TextUtils.isEmpty(mobile)) {
            String PATTERN = "^[+][0-9]{10,13}$";

            Pattern pattern = Pattern.compile(PATTERN);
            Matcher matcher = pattern.matcher(mobile);
            return matcher.matches();
        }
        return false;
    }

}
