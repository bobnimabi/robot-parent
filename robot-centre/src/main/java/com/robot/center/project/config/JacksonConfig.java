package com.robot.center.project.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.TimeZone;

/**
 * jackson2 日期序列化和反序列化处理
 */
@Configuration
public class JacksonConfig {
    /**
     * jackson2 json序列化
     */
    @Bean
    @Primary
    public ObjectMapper serializingObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        // 属性为Null或""的不进行序列化
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        // 允许出现特殊字符和转义符
        objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
        // 允许出现单引号
        objectMapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
        // 在反序列化时忽略在 json 中存在但 Java 对象不存在的属性
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        // 时区设置
        objectMapper.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));

        JavaTimeModule javaTimeModule = new JavaTimeModule();
        // LocalDateTime序列化和反序列化
        javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer());
        javaTimeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer());

        objectMapper.registerModule(javaTimeModule);
        return objectMapper;
    }


    /**
     * 日期序列化
     */
    public class LocalDateTimeSerializer extends JsonSerializer<LocalDateTime> {
        @Override
        public void serialize(LocalDateTime value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            gen.writeNumber(value.toEpochSecond(ZoneOffset.ofHours(8)));
        }
    }

    /**
     * 日期反序列化
     */
    public class LocalDateTimeDeserializer extends JsonDeserializer<LocalDateTime> {
        @Override
        public LocalDateTime deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            return LocalDateTime.ofEpochSecond(Long.parseLong(p.getValueAsString()), 0, ZoneOffset.ofHours(8));
        }
    }
}
