package com.pearadmin.binlog4jcore;

import com.gitee.Jmysy.binlog4j.core.BinlogClient;
import com.gitee.Jmysy.binlog4j.core.BinlogClientConfig;
import com.gitee.Jmysy.binlog4j.core.config.RedisConfig;
import com.gitee.Jmysy.binlog4j.core.enums.BinlogClientMode;
import com.pearadmin.binlog4jcore.handler.CustomEventHandler;

public class BootStrap {

    public static void main(String[] args) {

        RedisConfig redisConfig = new RedisConfig();
        redisConfig.setHost("127.0.0.1");
        redisConfig.setPort(6379);
        redisConfig.setPassword("taoren@123");

        // Binlog 客户端【配置】 非 Spring
        BinlogClientConfig clientConfig1 = new BinlogClientConfig();
        clientConfig1.setHost("127.0.0.1");
        clientConfig1.setPort(3306);
        clientConfig1.setUsername("root");
        clientConfig1.setPassword("taoren@123");
        clientConfig1.setServerId(1990);
        clientConfig1.setRedisConfig(redisConfig); // 依赖中间件（支撑 持久化模式 与 高可用 集群）
        clientConfig1.setPersistence(true); // 持久化模式
        clientConfig1.setMode(BinlogClientMode.cluster); // 高可用集群
        // clientConfig1.setMode(BinlogClientMode.standalone); // 单机模式

        BinlogClientConfig clientConfig2 = new BinlogClientConfig();
        clientConfig2.setHost("127.0.0.1");
        clientConfig2.setPort(3306);
        clientConfig2.setUsername("root");
        clientConfig2.setPassword("taoren@123");
        clientConfig2.setServerId(1990);
        clientConfig2.setRedisConfig(redisConfig); // 依赖中间件（支撑 持久化模式 与 高可用 集群）
        clientConfig2.setPersistence(true); // 持久化模式
        clientConfig2.setMode(BinlogClientMode.cluster); // 高可用集群
        // clientConfig2.setMode(BinlogClientMode.standalone); // 单机模式

        BinlogClient binlogClient1 = new BinlogClient(clientConfig1);
        BinlogClient binlogClient2 = new BinlogClient(clientConfig2);

        binlogClient1.registerEventHandler("taoren", "user", new CustomEventHandler());
        binlogClient2.registerEventHandler("taoren", "user", new CustomEventHandler());

        binlogClient1.connect();
        binlogClient2.connect();
    }
}
