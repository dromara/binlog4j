package com.gitee.Jmysy.binlog4j.springboot.starter;

import com.gitee.Jmysy.binlog4j.core.config.RedisConfig;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(Binlog4jAutoProperties.class)
public class Binlog4jAutoConfiguration {
    private Binlog4jAutoProperties binlog4jAutoProperties;

    public Binlog4jAutoConfiguration(Binlog4jAutoProperties properties) {
        this.binlog4jAutoProperties = properties;
        RedisConfig redisConfig = binlog4jAutoProperties.getRedisConfig();
        binlog4jAutoProperties.getClientConfigs().forEach((clientName, clientConfig) -> {
            if (redisConfig != null && clientConfig.getRedisConfig() == null) {
                clientConfig.setRedisConfig(redisConfig);
            }
        });
    }

    @Bean
    public Binlog4jInitializationBeanProcessor binlog4jAutoInitializing(Binlog4jAutoProperties properties) {
        return new Binlog4jInitializationBeanProcessor(properties.getClientConfigs());
    }
}
