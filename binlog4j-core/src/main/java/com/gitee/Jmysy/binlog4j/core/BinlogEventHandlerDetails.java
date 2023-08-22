package com.gitee.Jmysy.binlog4j.core;

import com.alibaba.fastjson.PropertyNamingStrategy;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.util.TypeUtils;
import com.gitee.Jmysy.binlog4j.core.utils.JDBCUtils;
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

    private static final ParserConfig snakeCase;

    static {
        snakeCase = new ParserConfig();
        snakeCase.propertyNamingStrategy = PropertyNamingStrategy.SnakeCase;
    }

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
            binlogEvent.setData(toEntity(row.getKey(), databaseName, tableName));
            binlogEvent.setOriginalData(toEntity(row.getValue(), databaseName, tableName));
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
        return TypeUtils.cast(obj, entityClass, snakeCase);
    }
}
