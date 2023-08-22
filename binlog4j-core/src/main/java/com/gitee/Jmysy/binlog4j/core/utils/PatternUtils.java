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

    // 查找字符串中与给定的正则表达式匹配的子字符串
    public static String find(String regex, String input) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);

        if (matcher.find()) {
            return matcher.group();
        } else {
            return null;
        }
    }

    // 替换字符串中与给定正则表达式匹配的部分
    public static String replaceAll(String regex, String replacement, String input) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);
        return matcher.replaceAll(replacement);
    }

    // 获取字符串中与给定正则表达式匹配的部分的数量
    public static int countMatches(String regex, String input) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);

        int count = 0;
        while (matcher.find()) {
            count++;
        }

        return count;
    }
}
