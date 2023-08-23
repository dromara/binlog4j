package com.gitee.Jmysy.binlog4j.springboot.starter;

import com.gitee.Jmysy.binlog4j.core.BinlogClient;
import com.gitee.Jmysy.binlog4j.core.BinlogClientConfig;
import com.gitee.Jmysy.binlog4j.core.IBinlogEventHandler;
import com.gitee.Jmysy.binlog4j.springboot.starter.annotation.BinlogSubscriber;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.annotation.AnnotationUtils;

import java.util.Map;

public class Binlog4jInitializationBeanProcessor implements SmartInitializingSingleton, ApplicationContextAware {

    private ApplicationContext applicationContext;

    private Map<String, BinlogClientConfig> clientConfigs;

    public Binlog4jInitializationBeanProcessor(Map<String, BinlogClientConfig> clientConfigs) {
        this.clientConfigs = clientConfigs;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void afterSingletonsInstantiated() {
        Map<String, IBinlogEventHandler> handlers = applicationContext.getBeansOfType(IBinlogEventHandler.class);
        clientConfigs.forEach((clientName, clientConfig) -> {
            BinlogClient client = new BinlogClient(clientConfig);
            handlers.forEach((beanName, handler) -> {
                BinlogSubscriber annotation = AnnotationUtils.findAnnotation(AopUtils.getTargetClass(handler), BinlogSubscriber.class);
                if(clientName.equals(annotation.clientName())) {
                    client.registerEventHandler(annotation.database(), annotation.table(), handler);
                }
            });
            client.connect();
        });
    }
}
