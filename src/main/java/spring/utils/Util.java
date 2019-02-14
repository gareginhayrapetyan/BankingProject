package spring.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Util {

    public static boolean isValidUserName(String userName) {
        Pattern pattern = Pattern.compile("^[A-Za-z0-9]+(?:[ _-][A-Za-z0-9]+)*$");
        Matcher matcher = pattern.matcher(userName);

        return matcher.matches();
    }


    public static boolean isValidEmail(String email) {
        Pattern pattern = Pattern.compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
        Matcher matcher = pattern.matcher(email);

        return matcher.matches();
    }

}
