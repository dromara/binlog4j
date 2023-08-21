## binlog4j

轻量级 Java Binlog 客户端，提供 高可用集群，宕机续读 等。

---------------
 
集群模式, 通过集群部署的方式，保证服务高可用。
 
宕机续读, 避免宕机期间造成数据丢失。 

数据转换，基于泛型封装 binlog Event 的序列化数据。
 
兼容 传统项目 与 Spring Boot / Cloud 项目。
 
兼容 Spring Boot 2.x 与 Spring Boot 3.x 版本。

### 下载安装

```xml
<dependency>
   <groupId>com.gitee.Jmysy</groupId>
   <artifactId>binlog4j-core</artifactId>
   <version>${latest.version}</version>
</dependency>
```

### 简单使用

```java

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

        binlogClient.registerEventHandler("pear-admin", "sys_user", new IBinlogEventHandler() {
            @Override
            public void onInsert(Object data) {

            }

            @Override
            public void onUpdate(Object originalData, Object data) {

            }

            @Override
            public void onDelete(Object data) {

            }
        });

        binlogClient.connect();
    }
}

```

### Spring Boot 集成

```yaml

spring:
  binlog4j:
    redis-config:
      host: 127.0.0.1
      port: 6379
      password: taoren@123
    client-configs:
      master:
        username: root
        password: taoren@123
        host: 127.0.0.1
        port: 3306
        serverId: 1990
        mode: cluster
        persistence: false

```

```java

@BinlogSubscriber(clientName = "master", database = "pear-admin", table ="sys_user")
public class UserEventHandler implements IBinlogEventHandler<User> {

    @Override
    public void onInsert(User target) {
        System.out.println("插入数据：" + JSON.toJSONString(target));
    }

    @Override
    public void onUpdate(User source, User target) {
        System.out.println("修改数据:" + JSON.toJSONString(target));
    }

    @Override
    public void onDelete(User target) {
        System.out.println("删除数据");
    }

}
```

