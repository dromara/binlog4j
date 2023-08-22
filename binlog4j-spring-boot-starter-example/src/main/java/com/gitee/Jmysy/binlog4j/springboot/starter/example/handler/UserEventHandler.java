package com.gitee.Jmysy.binlog4j.springboot.starter.example.handler;

import com.gitee.Jmysy.binlog4j.core.BinlogEvent;
import com.gitee.Jmysy.binlog4j.core.IBinlogEventHandler;
import com.gitee.Jmysy.binlog4j.springboot.starter.annotation.BinlogSubscriber;

@BinlogSubscriber(clientName = "master", database = ".*", table ="user.*")
public class UserEventHandler implements IBinlogEventHandler {

    @Override
    public void onUpdate(BinlogEvent event) {
        System.out.println("----------------------------");
        System.out.println("数据库：" + event.getDatabase());
        System.out.println("数据表：" + event.getTable());
        System.out.println("新数据：" + event.getData());
        System.out.println("原数据：" + event.getOriginalData());
    }

    @Override
    public void onInsert(BinlogEvent event) {
    }

    @Override
    public void onDelete(BinlogEvent event) {
    }

}
