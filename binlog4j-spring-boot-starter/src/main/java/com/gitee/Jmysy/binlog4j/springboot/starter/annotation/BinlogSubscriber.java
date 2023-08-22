package com.gitee.Jmysy.binlog4j.springboot.starter.annotation;

import org.springframework.stereotype.Component;
import java.lang.annotation.*;

@Documented
@Component
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface BinlogSubscriber {

    /**
     * 客户端
     * */
    String clientName();

    /**
     * 表名
     *
     * 默认应用到所有 client 的所有 database 的所有 table
     * */
    String table() default ".*";

    /**
     * 数据库
     *
     * 默认应用到所有 client 的所有 database 的所有 table
     * */
    String database() default ".*";

}
