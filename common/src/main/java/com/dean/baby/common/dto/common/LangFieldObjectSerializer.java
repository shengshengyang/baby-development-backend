package com.dean.baby.common.dto.common;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public class LangFieldObjectSerializer extends JsonSerializer<LangFieldObject> {

    @Override
    public void serialize(LangFieldObject value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeStartObject();

        if (value == null) {
            // 如果 LangFieldObject 為 null，返回所有語言字段為空字串的結構
            gen.writeStringField("en", "");
            gen.writeStringField("ja", "");
            gen.writeStringField("zh-CN", "");
            gen.writeStringField("zh-TW", "");
            gen.writeStringField("ko", "");
            gen.writeStringField("vi", "");
        } else {
            // 如果 LangFieldObject 不為 null，檢查每個字段是否為 null 並設置為空字串
            gen.writeStringField("en", value.en == null ? "" : value.en);
            gen.writeStringField("ja", value.ja == null ? "" : value.ja);
            gen.writeStringField("zh-CN", value.zh_cn == null ? "" : value.zh_cn);
            gen.writeStringField("zh-TW", value.zh_tw == null ? "" : value.zh_tw);
            gen.writeStringField("ko", value.ko == null ? "" : value.ko);
            gen.writeStringField("vi", value.vi == null ? "" : value.vi);
        }

        gen.writeEndObject();
    }

    @Override
    public void serializeWithType(LangFieldObject value, JsonGenerator gen, SerializerProvider serializers,
                                  com.fasterxml.jackson.databind.jsontype.TypeSerializer typeSer) throws IOException {
        serialize(value, gen, serializers);
    }
}
