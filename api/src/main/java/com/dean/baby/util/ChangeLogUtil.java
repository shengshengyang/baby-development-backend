package com.dean.baby.common.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import net.logstash.logback.argument.StructuredArguments;

import java.util.Map;
import java.util.UUID;

/**
 * 異動日誌工具類
 * 用於記錄 Service 層的資料異動
 */
@Slf4j
public class ChangeLogUtil {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 記錄建立操作
     *
     * @param entityName 實體名稱
     * @param entityId   實體 ID
     * @param data       建立的資料
     */
    public static void logCreate(String entityName, Object entityId, Object data) {
        log.info("CREATE - {} created with id: {}",
                entityName,
                entityId,
                StructuredArguments.keyValue("action", "CREATE"),
                StructuredArguments.keyValue("entity", entityName),
                StructuredArguments.keyValue("entityId", String.valueOf(entityId)),
                StructuredArguments.keyValue("data", toJsonSafe(data))
        );
    }

    /**
     * 記錄更新操作
     *
     * @param entityName 實體名稱
     * @param entityId   實體 ID
     * @param oldData    更新前的資料
     * @param newData    更新後的資料
     */
    public static void logUpdate(String entityName, Object entityId, Object oldData, Object newData) {
        log.info("UPDATE - {} updated with id: {}",
                entityName,
                entityId,
                StructuredArguments.keyValue("action", "UPDATE"),
                StructuredArguments.keyValue("entity", entityName),
                StructuredArguments.keyValue("entityId", String.valueOf(entityId)),
                StructuredArguments.keyValue("oldData", toJsonSafe(oldData)),
                StructuredArguments.keyValue("newData", toJsonSafe(newData))
        );
    }

    /**
     * 記錄刪除操作
     *
     * @param entityName 實體名稱
     * @param entityId   實體 ID
     * @param data       被刪除的資料
     */
    public static void logDelete(String entityName, Object entityId, Object data) {
        log.warn("DELETE - {} deleted with id: {}",
                entityName,
                entityId,
                StructuredArguments.keyValue("action", "DELETE"),
                StructuredArguments.keyValue("entity", entityName),
                StructuredArguments.keyValue("entityId", String.valueOf(entityId)),
                StructuredArguments.keyValue("deletedData", toJsonSafe(data))
        );
    }

    /**
     * 記錄狀態變更
     *
     * @param entityName 實體名稱
     * @param entityId   實體 ID
     * @param field      變更欄位
     * @param oldValue   舊值
     * @param newValue   新值
     */
    public static void logStatusChange(String entityName, Object entityId, String field, Object oldValue, Object newValue) {
        log.info("STATUS_CHANGE - {} {} changed from {} to {}",
                entityName,
                field,
                oldValue,
                newValue,
                StructuredArguments.keyValue("action", "STATUS_CHANGE"),
                StructuredArguments.keyValue("entity", entityName),
                StructuredArguments.keyValue("entityId", String.valueOf(entityId)),
                StructuredArguments.keyValue("field", field),
                StructuredArguments.keyValue("oldValue", String.valueOf(oldValue)),
                StructuredArguments.keyValue("newValue", String.valueOf(newValue))
        );
    }

    /**
     * 記錄業務操作
     *
     * @param operation 操作名稱
     * @param entityName 實體名稱
     * @param entityId   實體 ID
     * @param details    詳細資訊
     */
    public static void logOperation(String operation, String entityName, Object entityId, Map<String, Object> details) {
        log.info("OPERATION - {} on {}",
                operation,
                entityName,
                StructuredArguments.keyValue("action", operation),
                StructuredArguments.keyValue("entity", entityName),
                StructuredArguments.keyValue("entityId", String.valueOf(entityId)),
                StructuredArguments.keyValue("details", toJsonSafe(details))
        );
    }

    /**
     * 記錄錯誤
     *
     * @param operation 操作名稱
     * @param entityName 實體名稱
     * @param error      錯誤訊息
     */
    public static void logError(String operation, String entityName, String error) {
        log.error("ERROR - {} on {} failed: {}",
                operation,
                entityName,
                error,
                StructuredArguments.keyValue("action", operation),
                StructuredArguments.keyValue("entity", entityName),
                StructuredArguments.keyValue("error", error)
        );
    }

    /**
     * 安全地將物件轉換為 JSON
     */
    private static String toJsonSafe(Object obj) {
        if (obj == null) {
            return "{}";
        }
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            log.warn("Failed to serialize object to JSON", e);
            return obj.toString();
        }
    }

    /**
     * 產生關聯 ID 用於追蹤
     */
    public static String generateCorrelationId() {
        return UUID.randomUUID().toString();
    }

    /**
     * 比較兩個物件並記錄差異
     *
     * @param entityName 實體名稱
     * @param entityId   實體 ID
     * @param oldObj     舊物件
     * @param newObj     新物件
     */
    public static void logChanges(String entityName, Object entityId, Object oldObj, Object newObj) {
        if (oldObj == null && newObj == null) {
            return;
        }

        if (oldObj == null) {
            logCreate(entityName, entityId, newObj);
            return;
        }

        if (newObj == null) {
            logDelete(entityName, entityId, oldObj);
            return;
        }

        logUpdate(entityName, entityId, oldObj, newObj);
    }
}
