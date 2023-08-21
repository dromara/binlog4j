package com.gitee.Jmysy.binlog4j.springboot.starter.annotation;

import org.springframework.stereotype.Component;
import java.lang.annotation.*;

@Documented
@Component
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface BinlogSubscriber {

    String clientName();

    String table();

    String database();

}
