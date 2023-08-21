package com.gitee.Jmysy.binlog4j.core.position;

public interface BinlogPositionHandler {

    public BinlogPosition loadPosition(Long serverId);

    public void savePosition(BinlogPosition position);
}
