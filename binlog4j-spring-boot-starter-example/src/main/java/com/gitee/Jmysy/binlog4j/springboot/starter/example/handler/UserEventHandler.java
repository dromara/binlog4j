package com.gitee.Jmysy.binlog4j.springboot.starter.example.handler;

import com.gitee.Jmysy.binlog4j.core.BinlogEvent;
import com.gitee.Jmysy.binlog4j.core.IBinlogEventHandler;
import com.gitee.Jmysy.binlog4j.springboot.starter.annotation.BinlogSubscriber;

@BinlogSubscriber(clientName = "master", database = ".*", table ="user.*")
public class UserEventHandler implements IBinlogEventHandler {

    @Override
    public void onUpdate(BinlogEvent event) {

    }

    @Override
    public void onInsert(BinlogEvent event) {
    }

    @Override
    public void onDelete(BinlogEvent event) {
    }

}
