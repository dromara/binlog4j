package com.gitee.Jmysy.binlog4j.core;

public interface IBinlogClient {

    /**
     * 开始连接
     * */
    void connect();

    /**
     * 断开连接
     * */
    void disconnect();

     /**
      * 注册 binlog event 处理器
      *
      * @param database 数据库
      * @param table 表
      * @param eventHandler 事件处理器
      * */
    void registerEventHandler(String database, String table, IBinlogEventHandler eventHandler);
}
