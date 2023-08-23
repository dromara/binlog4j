package com.gitee.Jmysy.binlog4j.core;

import com.gitee.Jmysy.binlog4j.core.utils.JDBCUtils;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class BinlogEventHandlerDetails<T> {

    private String database;

    private String table;

    private IBinlogEventHandler eventHandler;

    private BinlogClientConfig clientConfig;

    private Class<T> entityClass;

    private Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();

    public void invokeInsert(String databaseName, String tableName, List<Serializable[]> data) {
        data.forEach(row -> {
            BinlogEvent<T> binlogEvent = new BinlogEvent<>();
            binlogEvent.setDatabase(databaseName);
            binlogEvent.setTable(tableName);
            binlogEvent.setData(toEntity(row, databaseName, tableName));
            eventHandler.onInsert(binlogEvent);
        });
    }

    public void invokeUpdate(String databaseName, String tableName, List<Map.Entry<Serializable[], Serializable[]>> data) {
        data.forEach(row ->  {
            BinlogEvent<T> binlogEvent = new BinlogEvent<>();
            binlogEvent.setDatabase(databaseName);
            binlogEvent.setTable(tableName);
            binlogEvent.setData(toEntity(row.getValue(), databaseName, tableName));
            binlogEvent.setOriginalData(toEntity(row.getKey(), databaseName, tableName));
            eventHandler.onUpdate(binlogEvent);
        });
    }

    public void invokeDelete(String databaseName, String tableName, List<Serializable[]> data) {
        data.forEach(row -> {
            BinlogEvent<T> binlogEvent = new BinlogEvent<>();
            binlogEvent.setDatabase(databaseName);
            binlogEvent.setTable(tableName);
            binlogEvent.setData(toEntity(row, databaseName, tableName));
            eventHandler.onDelete(binlogEvent);
        });
    }

    public T toEntity(Serializable[] data, String databaseName, String tableName) {
        String[] columnNames = JDBCUtils.getColumnNames(clientConfig, databaseName, tableName);
        Map<String, Object> obj = new HashMap<>();
        for (int i = 0; i < data.length; i++) {
            Serializable field = data[i];
            if (field instanceof Date) {
                data[i] = new Date(((Date) field).getTime() + clientConfig.getTimeOffset());
            }
            obj.put(columnNames[i], data[i]);
        }
        if(entityClass == null) {
            return (T) obj;
        }
        String jsonStr = gson.toJson(obj);
        return gson.fromJson(jsonStr, entityClass);
    }
}
