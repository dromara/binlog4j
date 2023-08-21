package com.gitee.Jmysy.binlog4j.core;

import com.gitee.Jmysy.binlog4j.core.config.RedisConfig;
import com.gitee.Jmysy.binlog4j.core.enums.BinlogClientMode;
import com.gitee.Jmysy.binlog4j.core.handler.CustomEventHandler;

public class BootStrap {

    public static void main(String[] args) {

        RedisConfig redisConfig = new RedisConfig();
        redisConfig.setHost("127.0.0.1");
        redisConfig.setPort(6379);
        redisConfig.setPassword("taoren@123");

        // Binlog 客户端【配置】 非 Spring
        BinlogClientConfig clientConfig = new BinlogClientConfig();
        clientConfig.setHost("127.0.0.1");
        clientConfig.setPort(3306);
        clientConfig.setUsername("root");
        clientConfig.setPassword("taoren@123");
        clientConfig.setServerId(1990);
        clientConfig.setRedisConfig(redisConfig); // 依赖中间件（支撑 持久化模式 与 高可用 集群）
        clientConfig.setPersistence(true); // 持久化模式
        clientConfig.setMode(BinlogClientMode.cluster); // 高可用集群
        // clientConfig1.setMode(BinlogClientMode.standalone); // 单机模式

        BinlogClient binlogClient = new BinlogClient(clientConfig);

        binlogClient.registerEventHandler("taoren", "user", new CustomEventHandler());

        binlogClient.connect();
    }
}
