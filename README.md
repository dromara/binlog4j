
<h2 align="center">Binlog4j</h2>

<p align="center">
	<strong>轻量级 Mysql Binlog 客户端, 提供宕机续读, 高可用集群等特性</strong>
</p>

<p align="center">
    <a href="http://www.apache.org/licenses/LICENSE-2.0.html" target="_blank">
        <img src="http://img.shields.io/:license-apache-brightgreen.svg" >
    </a>
    <a href="https://central.sonatype.com/search?q=binlog4j&smo=true" target="_blank">
        <img src="https://img.shields.io/maven-central/v/com.gitee.Jmysy/binlog4j" />
    </a>
    <a>
        <img src="https://img.shields.io/badge/JDK-1.8+-green.svg" >
    </a>
    <a>
        <img src="https://img.shields.io/badge/springBoot-2.0+-green.svg" >
    </a>
    <a>
        <img src="https://img.shields.io/badge/springBoot-3.0+-green.svg" >
    </a>
</p>

## 简介

- 集群模式, 通过集群部署的方式，保证服务高可用。

- 宕机续读, 避免宕机期间造成数据丢失。

- 数据转换, 基于泛型封装 binlog Event 的序列化数据。

- 兼容 传统项目 与 Spring Boot / Cloud 项目。

- 兼容 Spring Boot 2.x 与 Spring Boot 3.x 版本。


### 下载安装

```xml
<dependency>
   <groupId>com.gitee.Jmysy</groupId>
   <artifactId>binlog4j-core</artifactId>
   <version>latest.version</version>
</dependency>
```

或

```text
implementation group: 'com.gitee.Jmysy', name: 'binlog4j-core', version: 'latest.version'
```

### 简单使用

通过 BinlogClient 创建 binlog 客户端, IBinlogEventHandler 用于接受 binlog 事件通知, 该接口允许使用泛型, 数据将遵循驼峰规则进行封装。

```java
public class BootStrap {

    public static void main(String[] args) {
        
        BinlogClientConfig clientConfig = new BinlogClientConfig();
        clientConfig.setHost("127.0.0.1");
        clientConfig.setPort(3306);
        clientConfig.setUsername("root");
        clientConfig.setPassword("taoren@123");
        clientConfig.setServerId(1990);
  
        IBinlogClient binlogClient = new BinlogClient(clientConfig);

        binlogClient.registerEventHandler("database", "table", new IBinlogEventHandler() {
            
            @Override
            public void onInsert(Object data) {
                System.out.println("插入数据:{}", data);
            }

            @Override
            public void onUpdate(Object originalData, Object data) {
                System.out.println("修改数据:{}", data);
            }

            @Override
            public void onDelete(Object data) {
                System.out.println("删除数据:{}", data);
            }
        });

        binlogClient.connect();
    }
}

```

### 高级特性

通过 Persistence 配置为 true 启用宕机续读功能, Binlog4j 会将 binlog 的 filename 与 position 记录到 redis, 所以同时你需要设置 Redis 配置。

```java
public class BootStrap {

    public static void main(String[] args) {

        RedisConfig redisConfig = new RedisConfig();
        redisConfig.setHost("127.0.0.1");
        redisConfig.setPort(6379);
        redisConfig.setPassword("taoren@123");

        BinlogClientConfig clientConfig = new BinlogClientConfig();
        clientConfig.setHost("127.0.0.1");
        clientConfig.setPort(3306);
        clientConfig.setUsername("root");
        clientConfig.setPassword("taoren@123");
        clientConfig.setServerId(1990); // Client 编号
        clientConfig.setRedisConfig(redisConfig); // Redis 配置
        clientConfig.setPersistence(true); // 启用持久化 (宕机重启后, 从上次读取的位置开始)
        clientConfig.setMode(BinlogClientMode.cluster); // 高可用集群

        BinlogClient binlogClient = new BinlogClient(clientConfig);

        binlogClient.registerEventHandler("database", "table", new IBinlogEventHandler<User>() {

            @Override
            public void onInsert(Object data) {
                System.out.println("插入数据:{}", data);
            }

            @Override
            public void onUpdate(Object originalData, Object data) {
                System.out.println("修改数据:{}", data);
            }

            @Override
            public void onDelete(Object data) {
                System.out.println("删除数据:{}", data);
            }
        });

        binlogClient.connect();
    }
}

```

### Spring Boot Starter

```agsl
<dependency>
    <groupId>com.gitee.Jmysy</groupId>
    <artifactId>binlog4j-spring-boot-starter</artifactId>
    <version>latest.version</version>
</dependency>
```

或

```text
implementation group: 'com.gitee.Jmysy', name: 'binlog4j-spring-boot-starter', version: 'latest.version'
```


首先, 在 application.yml 中填写 BinlogClient 配置

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

```

使用 @BinlogSubscriber 注解, 指定 IBinlogEventHandler 需要注册到哪个客户端, 并且指定监听的 database 与 table。

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
        System.out.println("删除数据:" + JSON.toJSONString(target));
    }

}
```

