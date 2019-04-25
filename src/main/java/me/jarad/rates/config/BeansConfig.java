package me.jarad.rates.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;

/**
 * Application common beans configuration
 */
@Configuration
public class BeansConfig {

    @Autowired
    void configureObjectMapper(final ObjectMapper mapper) {
        mapper.findAndRegisterModules();
        mapper.registerModule(new JavaTimeModule());
        mapper.configure(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS, true);

    }
}
