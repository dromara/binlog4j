package com.gitee.Jmysy.binlog4j.springboot.starter.example.handler;

import com.alibaba.fastjson.JSON;
import com.gitee.Jmysy.binlog4j.core.IBinlogEventHandler;
import com.gitee.Jmysy.binlog4j.springboot.starter.annotation.BinlogSubscriber;

@BinlogSubscriber(clientName = "master", database = "taoren", table ="user")
public class RoleEventHandler implements IBinlogEventHandler {

    @Override
    public void onInsert(Object data) {
        System.out.println("没有类型:" + JSON.toJSONString(data));
    }

    @Override
    public void onUpdate(Object originalData, Object data) {
        System.out.println("没有类型:" + JSON.toJSONString(data));
    }

    @Override
    public void onDelete(Object data) {
        System.out.println("没有类型:" + JSON.toJSONString(data));
    }
}
