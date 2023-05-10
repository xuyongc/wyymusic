package com.example.wyymusic.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author xyc
 * @CreteDate 2023/4/27 8:48
 **/
public class RegexUtil {
    public static boolean isPhone(String phone){
        String validPattern = "^((13[0-9])|(14[5,7,9])|(15([0-3]|[5-9]))|(166)|(17[0,1,3,5,6,7,8])|(18[0-9])|(19[8|9]))\\d{8}$";
        Matcher matcher = Pattern.compile(validPattern).matcher(phone);

        return !matcher.find();
    }

    public static boolean isHaveSpecialCharacter(String text){
        String validPattern = "[`~!@#$%^&*()+=|{}':;',\\\\[\\\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Matcher matcher = Pattern.compile(validPattern).matcher(text);

        return matcher.find();
    }
}
