package com.gitee.Jmysy.binlog4j.core.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PatternUtils {

    // 判断字符串是否匹配给定的正则表达式
    public static boolean matches(String regex, String input) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);
        return matcher.matches();
    }
}
