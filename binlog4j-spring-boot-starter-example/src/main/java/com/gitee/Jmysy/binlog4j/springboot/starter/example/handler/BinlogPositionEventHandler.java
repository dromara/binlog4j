package com.gitee.Jmysy.binlog4j.springboot.starter.example.handler;

import com.gitee.Jmysy.binlog4j.core.BinlogEvent;
import com.gitee.Jmysy.binlog4j.core.IBinlogEventHandler;
import com.gitee.Jmysy.binlog4j.springboot.starter.annotation.BinlogSubscriber;
import com.gitee.Jmysy.binlog4j.springboot.starter.example.domain.BinlogPosition;

@BinlogSubscriber(clientName = "master", database = "binlog4j", table ="binlog_position")
public class BinlogPositionEventHandler implements IBinlogEventHandler<BinlogPosition> {

    @Override
    public void onInsert(BinlogEvent<BinlogPosition> event) {
        System.out.println("插入数据:" + event.getData());
    }

    @Override
    public void onUpdate(BinlogEvent<BinlogPosition> event) {
        System.out.println("修改数据:" + event.getData());
    }

    @Override
    public void onDelete(BinlogEvent<BinlogPosition> event) {
        System.out.println("删除数据:" + event.getData());
    }
}
