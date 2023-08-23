package com.gitee.Jmysy.binlog4j.core.dispatcher;

import com.gitee.Jmysy.binlog4j.core.BinlogClientConfig;
import com.gitee.Jmysy.binlog4j.core.BinlogEventHandlerDetails;
import com.gitee.Jmysy.binlog4j.core.utils.BinlogUtils;
import com.gitee.Jmysy.binlog4j.core.position.BinlogPosition;
import com.gitee.Jmysy.binlog4j.core.position.BinlogPositionHandler;
import com.gitee.Jmysy.binlog4j.core.utils.PatternUtils;
import com.github.shyiko.mysql.binlog.BinaryLogClient;
import com.github.shyiko.mysql.binlog.event.*;
import lombok.Data;

import java.io.Serializable;
import java.util.*;

public class BinlogEventDispatcher implements BinaryLogClient.EventListener {
    private Map<Long, TableMapEventData> tableMap = new HashMap<>();

    private List<BinlogEventHandlerDetails> eventHandlerMap;

    private BinlogClientConfig clientConfig;

    private BinlogPositionHandler binlogPositionHandler;

    public BinlogEventDispatcher(BinlogClientConfig clientConfig, BinlogPositionHandler positionHandler,  List<BinlogEventHandlerDetails> eventHandlerMap) {
        this.clientConfig = clientConfig;
        this.eventHandlerMap = eventHandlerMap;
        this.binlogPositionHandler = positionHandler;
    }

    @Override
    public void onEvent(Event event) {
        EventHeaderV4 headerV4 = event.getHeader();
        EventType eventType = headerV4.getEventType();
        if(eventType == EventType.TABLE_MAP) {
            TableMapEventData eventData = event.getData();
            String database = eventData.getDatabase();
            String table = eventData.getTable();
            eventHandlerMap.forEach(eventHandler -> {
                if(PatternUtils.matches(eventHandler.getDatabase(), database) && PatternUtils.matches(eventHandler.getTable(), table)) {
                    tableMap.put(eventData.getTableId(), eventData);
                }
            });
        } else {
            if(EventType.isRowMutation(eventType)) {
                RowMutationEventData rowMutationEventData = new RowMutationEventData(event.getData());
                TableMapEventData tableMapEventData = tableMap.get(rowMutationEventData.getTableId());
                if(tableMapEventData != null) {
                    String database = tableMapEventData.getDatabase();
                    String table = tableMapEventData.getTable();
                    this.eventHandlerMap.forEach((eventHandler) -> {
                        if(PatternUtils.matches(eventHandler.getDatabase(), database) && PatternUtils.matches(eventHandler.getTable(), table)) {
                            if(BinlogUtils.isUpdate(eventType)) eventHandler.invokeUpdate(database, table, rowMutationEventData.getUpdateRows());
                            if(BinlogUtils.isDelete(eventType)) eventHandler.invokeDelete(database, table, rowMutationEventData.getDeleteRows());
                            if(BinlogUtils.isInsert(eventType)) eventHandler.invokeInsert(database, table, rowMutationEventData.getInsertRows());
                        }
                    });
                }
            }
        }
        // 固化逻辑
        if(clientConfig.getPersistence()) {
            if (binlogPositionHandler != null) {
                BinlogPosition binlogPosition = new BinlogPosition();
                if (EventType.ROTATE == eventType) {
                    binlogPosition.setServerId(clientConfig.getServerId());
                    binlogPosition.setFilename(((RotateEventData) event.getData()).getBinlogFilename());
                    binlogPosition.setPosition(((RotateEventData) event.getData()).getBinlogPosition());
                } else {
                    binlogPosition = binlogPositionHandler.loadPosition(clientConfig.getServerId());
                    if (binlogPosition != null) {
                        binlogPosition.setPosition(headerV4.getNextPosition());
                    }
                }
                binlogPositionHandler.savePosition(binlogPosition);
            }
        }
    }

    @Data
    public class RowMutationEventData {

        private long tableId;

        private List<Serializable[]> insertRows;

        private List<Serializable[]> deleteRows;

        private List<Map.Entry<Serializable[], Serializable[]>> updateRows;

        public RowMutationEventData(EventData eventData) {

            if(eventData instanceof UpdateRowsEventData) {
                UpdateRowsEventData updateRowsEventData = (UpdateRowsEventData) eventData;
                this.tableId = updateRowsEventData.getTableId();
                this.updateRows = updateRowsEventData.getRows();
            }

            if(eventData instanceof WriteRowsEventData) {
                WriteRowsEventData writeRowsEventData = (WriteRowsEventData) eventData;
                this.tableId = writeRowsEventData.getTableId();
                this.insertRows = writeRowsEventData.getRows();
            }

            if(eventData instanceof DeleteRowsEventData) {
                DeleteRowsEventData deleteRowsEventData = (DeleteRowsEventData) eventData;
                this.tableId = deleteRowsEventData.getTableId();
                this.deleteRows = deleteRowsEventData.getRows();
            }
        }
    }
}
