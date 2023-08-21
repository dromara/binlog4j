package com.gitee.Jmysy.binlog4j.core.utils;

import com.alibaba.fastjson.PropertyNamingStrategy;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.util.TypeUtils;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ClassUtils {

    public static <T> Class<T> getGenericType(Class<?> cls) {
        Type type = cls.getGenericInterfaces()[0];
        if (type instanceof ParameterizedType) {
            ParameterizedType paramType = (ParameterizedType) type;
            Type[] argTypes = paramType.getActualTypeArguments();
            if (argTypes.length > 0) {
                return (Class<T>) argTypes[0];
            }
        }
        return null;
    }
}
