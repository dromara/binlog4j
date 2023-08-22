package com.gitee.Jmysy.binlog4j.core;

public interface IBinlogEventHandler<T> {

    /**
     * 插入
     * */
    void onInsert(BinlogEvent<T> event);

    /**
     * 修改
     * */
    void onUpdate(BinlogEvent<T> event);

    /**
     * 删除
     * */
    void onDelete(BinlogEvent<T> event);

}
