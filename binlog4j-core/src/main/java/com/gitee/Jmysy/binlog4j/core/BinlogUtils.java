package com.gitee.Jmysy.binlog4j.core;

import com.github.shyiko.mysql.binlog.event.EventType;

public class BinlogUtils {

    public static boolean isUpdate(EventType eventType) {
        return eventType == EventType.PRE_GA_UPDATE_ROWS || eventType == EventType.UPDATE_ROWS || eventType == EventType.EXT_UPDATE_ROWS;
    }

    public static boolean isInsert(EventType eventType) {
        return eventType == EventType.PRE_GA_WRITE_ROWS || eventType == EventType.WRITE_ROWS || eventType == EventType.EXT_WRITE_ROWS;
    }

    public static boolean isDelete(EventType eventType) {
        return eventType == EventType.PRE_GA_DELETE_ROWS || eventType == EventType.DELETE_ROWS || eventType == EventType.EXT_DELETE_ROWS;
    }
}
