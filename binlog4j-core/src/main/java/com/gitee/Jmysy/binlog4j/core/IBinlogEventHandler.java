package com.gitee.Jmysy.binlog4j.core;

public interface IBinlogEventHandler<T> {

    /**
     * 插入
     * */
    void onInsert(T data);

    /**
     * 修改
     * */
    void onUpdate(T originalData, T data);

    /**
     * 删除
     * */
    void onDelete(T data);

}
