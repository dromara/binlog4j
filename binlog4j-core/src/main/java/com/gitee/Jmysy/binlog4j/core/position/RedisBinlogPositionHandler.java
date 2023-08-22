package com.gitee.Jmysy.binlog4j.core.position;

import com.gitee.Jmysy.binlog4j.core.config.RedisConfig;
import com.google.gson.Gson;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class RedisBinlogPositionHandler implements BinlogPositionHandler {

    private Gson gson = new Gson();

    private JedisPool jedisPool;


    public RedisBinlogPositionHandler(RedisConfig redisConfig) {
        this.jedisPool = new JedisPool(new GenericObjectPoolConfig(), redisConfig.getHost(), redisConfig.getPort(), 1000, redisConfig.getPassword());
    }

    @Override
    public BinlogPosition loadPosition(Long serverId) {
        try (Jedis jedis = jedisPool.getResource()) {
            String value = jedis.get(serverId.toString());
            if (value != null) {
                return gson.fromJson(value, BinlogPosition.class);
            }
        }
        return null;
    }

    @Override
    public void savePosition(BinlogPosition position) {
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.set(position.getServerId().toString(), gson.toJson(position));
        }
    }
}
