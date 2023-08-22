package com.gitee.Jmysy.binlog4j.core;

public class BinlogEvent<T> {

    /**
     * Event 来源 Table
     * */
    private String table;

    /**
     * Event 来源 Database
     * */
    private String database;

    /**
     * 新数据
     * */
    private T data;

    /**
     * 原数据
     * */
    private T originalData;

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public T getOriginalData() {
        return originalData;
    }

    public void setOriginalData(T originalData) {
        this.originalData = originalData;
    }

}
