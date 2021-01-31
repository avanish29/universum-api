package com.universum.common.configuration.json;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.universum.common.configuration.json.serializer.LocalDateDeserializer;
import com.universum.common.configuration.json.serializer.LocalDateSerializer;
import com.universum.common.configuration.json.serializer.LocalDateTimeDeserializer;
import com.universum.common.configuration.json.serializer.LocalDateTimeSerializer;
import com.universum.common.configuration.json.serializer.NullKeySerializer;

@Configuration
public class ObjectMapperConfig {
	@Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();

        JavaTimeModule javaTimeModule = new JavaTimeModule();
        javaTimeModule.addSerializer(LocalDate.class, new LocalDateSerializer());
        javaTimeModule.addDeserializer(LocalDate.class, new LocalDateDeserializer());
        javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer());
        javaTimeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer());
        
        objectMapper.registerModule(javaTimeModule);
        objectMapper.getSerializerProvider().setNullKeySerializer(new NullKeySerializer());
        
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        
        return objectMapper;
    }
}
