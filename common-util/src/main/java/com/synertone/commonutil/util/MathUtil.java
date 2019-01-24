package com.synertone.commonutil.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MathUtil {
    public static boolean isNumber(String number){
        String regex="^[+-]?\\d+(\\.\\d+)?$";
        Pattern pattern=Pattern.compile(regex);
        Matcher matcher = pattern.matcher(number);
        return matcher.matches();
    }
}
