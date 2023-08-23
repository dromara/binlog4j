package com.gitee.Jmysy.binlog4j.core;

public interface IBinlogEventHandler<T> {

    /**
     * 插入
     *
     * @param event 事件详情
     * */
    void onInsert(BinlogEvent<T> event);

    /**
     * 修改
     *
     * @param event 事件详情
     * */
    void onUpdate(BinlogEvent<T> event);

    /**
     * 删除
     *
     * @param event 事件详情
     * */
    void onDelete(BinlogEvent<T> event);

}
