package com.pearadmin.binlog4jcore.handler;

import com.gitee.Jmysy.binlog4j.core.IBinlogEventHandler;

public class CustomEventHandler implements IBinlogEventHandler {

    @Override
    public void onUpdate(Object source, Object target) {
        System.out.println("修改数据");
    }

    @Override
    public void onDelete(Object target) {
        System.out.println("删除数据");
    }

    @Override
    public void onInsert(Object target) {
        System.out.println("插入数据");
    }

}
