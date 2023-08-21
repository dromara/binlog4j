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

    public void invokeInsert(List<Serializable[]> data) {
        data.forEach(row -> {
            eventHandler.onInsert(toEntity(row));
        });
    }

    public void invokeUpdate(List<Map.Entry<Serializable[], Serializable[]>> data) {
        data.forEach(row ->  {
            eventHandler.onUpdate(toEntity(row.getKey()), toEntity(row.getValue()));
        });
    }

    public void invokeDelete(List<Serializable[]> data) {
        data.forEach(row -> {
            eventHandler.onDelete(toEntity(row));
        });
    }

    public T toEntity(Serializable[] data) {

        String[] columnNames = JDBCUtils.getColumnNames(clientConfig, database, table);

        Map<String, Object> b = new HashMap<>();

        for (int i = 0; i < data.length; i++) {
            Serializable da = data[i];
            if (da instanceof Date) {
                data[i] = new Date(((Date) da).getTime() + clientConfig.getTimeOffset());
            }
            b.put(columnNames[i], data[i]);
        }

        return TypeUtils.cast(b, entityClass, snakeCase);
    }
}
