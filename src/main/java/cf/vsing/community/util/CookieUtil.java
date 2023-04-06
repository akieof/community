package cf.vsing.community.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

public class CookieUtil {
    public static String getCookie(HttpServletRequest request, String name) {
        if (request == null || name == null) {
            throw new IllegalArgumentException();
        } else {
            if (request.getCookies() == null) {
                return null;
            }
            for (Cookie cookie : request.getCookies()) {
                if (cookie.getName().equals(name)) {
                    return cookie.getValue();
                }
            }
            return null;
        }


    }
}
