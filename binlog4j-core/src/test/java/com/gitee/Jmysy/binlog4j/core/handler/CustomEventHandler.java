package com.gitee.Jmysy.binlog4j.core.handler;

import com.gitee.Jmysy.binlog4j.core.BinlogEvent;
import com.gitee.Jmysy.binlog4j.core.IBinlogEventHandler;

public class CustomEventHandler implements IBinlogEventHandler {
    @Override
    public void onUpdate(BinlogEvent event) {
        System.out.println("修改数据");
    }

    @Override
    public void onDelete(BinlogEvent event) {
        System.out.println("删除数据");
    }

    @Override
    public void onInsert(BinlogEvent event) {
        System.out.println("插入数据");
    }

}
