package com.gitee.Jmysy.binlog4j.core;

import com.gitee.Jmysy.binlog4j.core.config.RedisConfig;
import com.gitee.Jmysy.binlog4j.core.enums.BinlogClientMode;
import com.gitee.Jmysy.binlog4j.core.utils.MD5Utils;

import java.util.concurrent.TimeUnit;

public class BinlogClientConfig {

    /**
     * 账户
     * */
    private String username;

    /**
     * 密码
     * */
    private String password;

    /**
     * 地址
     * */
    private String host;

    /**
     * 端口
     * */
    private int port = 3306;

    /**
     * 时间偏移量
     * */
    private long timeOffset = 0;

    /**
     * 客户端编号 (不同的集群)
     * */
    private long serverId;

    /**
     * 是否保持连接
     * */
    private boolean keepAlive = true;

    /**
     * 保持连接时间
     * */
    private long KeepAliveInterval = TimeUnit.MINUTES.toMillis(1L);

    /**
     * 链接超时
     * */
    private long connectTimeout = TimeUnit.SECONDS.toMillis(3L);

    /**
     * 发送心跳包时间间隔
     * */
    private long heartbeatInterval = 6000;

    /**
     * “分布式” “记忆读取”
     *
     * 依赖的 Redis 中间件配置
     * */
    private RedisConfig redisConfig;

    /**
     * 读取记忆
     * */
    private Boolean persistence;

    /**
     * 部署模式
     * */
    private BinlogClientMode mode;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public long getServerId() {
        return serverId;
    }

    public void setServerId(long serverId) {
        this.serverId = serverId;
    }

    public boolean getKeepAlive() {
        return keepAlive;
    }

    public void setKeepAlive(boolean keepAlive) {
        this.keepAlive = keepAlive;
    }

    public long getKeepAliveInterval() {
        return KeepAliveInterval;
    }

    public void setKeepAliveInterval(long keepAliveInterval) {
        KeepAliveInterval = keepAliveInterval;
    }

    public boolean isKeepAlive() {
        return keepAlive;
    }

    public long getHeartbeatInterval() {
        return heartbeatInterval;
    }

    public void setHeartbeatInterval(long heartbeatInterval) {
        this.heartbeatInterval = heartbeatInterval;
    }

    public RedisConfig getRedisConfig() {
        return redisConfig;
    }

    public void setRedisConfig(RedisConfig redisConfig) {
        this.redisConfig = redisConfig;
    }


    public long getConnectTimeout() {
        return connectTimeout;
    }

    public void setConnectTimeout(long connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    public Boolean getPersistence() {
        return persistence;
    }

    public void setPersistence(Boolean persistence) {
        this.persistence = persistence;
    }

    public BinlogClientMode getMode() {
        return mode;
    }

    public void setMode(BinlogClientMode mode) {
        this.mode = mode;
    }

    public String getKey() {
        return MD5Utils.encrypt(this.host + ":" + this.port + ":" + this.serverId);
    }

    public long getTimeOffset() {
        return timeOffset;
    }

    public void setTimeOffset(long timeOffset) {
        this.timeOffset = timeOffset;
    }
}
