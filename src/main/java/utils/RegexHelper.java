package utils;

import java.util.regex.Pattern;

public class RegexHelper {

    public static final Pattern EMAIL_PATTERN = Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");

/*    public static final Pattern URL_PATTERN = Pattern.compile(
            "^(https?://)?(www\\.)?[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}([/\\?][\\w\\-._~:/?#\\[\\]@!$&'()*+,;=]*)?$");*/


    public static boolean isValidEmail(String email){
        return email != null && EMAIL_PATTERN.matcher(email).matches();
    }

    public static boolean isValidUrl(String url){
        //return url != null && URL_PATTERN.matcher(url).matches();
        if (url == null) return false;
        try {
            new java.net.URL(url).toURI();
            return true;
        } catch (Exception e){
            return false;
        }

    }
}