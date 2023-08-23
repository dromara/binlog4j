package com.gitee.Jmysy.binlog4j.core;

import com.gitee.Jmysy.binlog4j.core.config.RedisConfig;
import com.gitee.Jmysy.binlog4j.core.enums.BinlogClientMode;
import com.gitee.Jmysy.binlog4j.core.exception.Binlog4jException;
import com.gitee.Jmysy.binlog4j.core.position.BinlogPosition;
import com.gitee.Jmysy.binlog4j.core.position.BinlogPositionHandler;
import com.gitee.Jmysy.binlog4j.core.position.RedisBinlogPositionHandler;
import com.github.shyiko.mysql.binlog.BinaryLogClient;
import com.gitee.Jmysy.binlog4j.core.dispatcher.BinlogEventDispatcher;

import com.gitee.Jmysy.binlog4j.core.utils.ClassUtils;
import lombok.extern.slf4j.Slf4j;
import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.config.SingleServerConfig;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
public class BinlogClient implements IBinlogClient{

    private BinlogClientConfig clientConfig;

    private BinaryLogClient client;

    private BinlogPositionHandler positionHandler;

    private RedissonClient redissonClient;

    private List<BinlogEventHandlerDetails> eventHandlerMap = new ArrayList<>();

    private ExecutorService executor;

    public BinlogClient(BinlogClientConfig clientConfig) {
        if(clientConfig.getPersistence() || clientConfig.getMode() == BinlogClientMode.cluster) {
           if(clientConfig.getRedisConfig() == null) {
               throw new Binlog4jException("Cluster mode or persistence enabled, missing Redis configuration");
           }
            this.positionHandler = new RedisBinlogPositionHandler(clientConfig.getRedisConfig());
            this.redissonClient = createRedissonClient(clientConfig.getRedisConfig());
        }
        this.clientConfig = clientConfig;
        this.executor = Executors.newCachedThreadPool();
    }

    public void registerEventHandler(String database, String table, IBinlogEventHandler eventHandler) {
        BinlogEventHandlerDetails eventHandlerDetails = new BinlogEventHandlerDetails();
        eventHandlerDetails.setDatabase(database);
        eventHandlerDetails.setTable(table);
        eventHandlerDetails.setClientConfig(clientConfig);
        eventHandlerDetails.setEntityClass(ClassUtils.getGenericType(eventHandler.getClass()));
        eventHandlerDetails.setEventHandler(eventHandler);
        this.eventHandlerMap.add(eventHandlerDetails);
    }

    @Override
    public void connect() {
        BinlogClientMode clientMode = clientConfig.getMode();
        if(clientMode != null && clientMode == BinlogClientMode.cluster) {
            ScheduledExecutorService scheduledExecutor = Executors.newScheduledThreadPool(1);
            scheduledExecutor.scheduleWithFixedDelay(this::runWithCluster, 0, 1000, TimeUnit.MILLISECONDS);
        } else {
            executor.submit(this::runWithStandalone);
        }
    }

    @Override
    public void disconnect() {
        if(client != null) {
            try {
                client.disconnect();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    public void runWithStandalone() {
        try {
            client = new BinaryLogClient(clientConfig.getHost(), clientConfig.getPort(), clientConfig.getUsername(), clientConfig.getPassword());
            client.registerEventListener(new BinlogEventDispatcher(this.clientConfig, positionHandler, this.eventHandlerMap));
            client.setKeepAlive(clientConfig.getKeepAlive());
            client.setKeepAliveInterval(clientConfig.getKeepAliveInterval());
            client.setHeartbeatInterval(clientConfig.getHeartbeatInterval());
            client.setConnectTimeout(clientConfig.getConnectTimeout());
            client.setServerId(clientConfig.getServerId());
            if(clientConfig.getPersistence()) {
                if(clientConfig.isInaugural() != true) {
                    if(positionHandler != null) {
                        BinlogPosition binlogPosition = positionHandler.loadPosition(clientConfig.getServerId());
                        if(binlogPosition != null) {
                            client.setBinlogFilename(binlogPosition.getFilename());
                            client.setBinlogPosition(binlogPosition.getPosition());
                        }
                    }
                }
            }
            client.connect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void runWithCluster() {
        RLock lock = redissonClient.getLock(clientConfig.getKey());
        try {
            if(lock.tryLock()) {
                runWithStandalone();
            }
        } finally {
            if(lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }

    private RedissonClient createRedissonClient(RedisConfig redisConfig) {
        Config config = new Config();
        SingleServerConfig singleServerConfig = config.useSingleServer();
        singleServerConfig.setAddress("redis://" + redisConfig.getHost() + ":" + redisConfig.getPort());
        singleServerConfig.setPassword(redisConfig.getPassword());
        config.setLockWatchdogTimeout(10000L);
        return Redisson.create(config);
    }
}
