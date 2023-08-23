package com.gitee.Jmysy.binlog4j.springboot.starter.example.handler;

import com.gitee.Jmysy.binlog4j.core.BinlogEvent;
import com.gitee.Jmysy.binlog4j.core.IBinlogEventHandler;
import com.gitee.Jmysy.binlog4j.springboot.starter.annotation.BinlogSubscriber;
import com.gitee.Jmysy.binlog4j.springboot.starter.example.domain.User;

@BinlogSubscriber(clientName = "master", database = "taoren", table ="user")
public class RoleEventHandler implements IBinlogEventHandler<User> {

    @Override
    public void onInsert(BinlogEvent<User> event) {
        System.out.println("插入数据:" + event.getData());
    }

    @Override
    public void onUpdate(BinlogEvent event) {
        System.out.println("修改数据:" + event.getData());
    }

    @Override
    public void onDelete(BinlogEvent event) {
        System.out.println("删除数据:" + event.getData());
    }
}
