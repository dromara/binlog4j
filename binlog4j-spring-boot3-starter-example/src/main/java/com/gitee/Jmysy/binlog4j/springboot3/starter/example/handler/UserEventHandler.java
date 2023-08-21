package com.gitee.Jmysy.binlog4j.springboot3.starter.example.handler;

import com.alibaba.fastjson.JSON;
import com.gitee.Jmysy.binlog4j.core.IBinlogEventHandler;
import com.gitee.Jmysy.binlog4j.springboot.starter.annotation.BinlogSubscriber;
import com.gitee.Jmysy.binlog4j.springboot3.starter.example.domain.User;

@BinlogSubscriber(clientName = "master", database = "taoren", table ="user")
public class UserEventHandler implements IBinlogEventHandler<User> {

    @Override
    public void onInsert(User target) {
        System.out.println("插入数据：" + JSON.toJSONString(target));
    }

    @Override
    public void onUpdate(User source, User target) {
        System.out.println("修改数据:" + JSON.toJSONString(target));
    }

    @Override
    public void onDelete(User target) {
        System.out.println("删除数据");
    }

}
