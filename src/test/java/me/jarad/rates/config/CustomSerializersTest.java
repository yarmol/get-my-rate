package me.jarad.rates.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.*;
import org.junit.BeforeClass;
import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.*;


public class CustomSerializersTest {


    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    static class Container {

        @JsonSerialize(using = CustomSerializers.BigDecimalToStringSerializer.class)
        @JsonDeserialize(using = CustomSerializers.BigDecimalFromStringDeserializer.class)
        BigDecimal value;
    }

    private static ObjectMapper mapper;

    private static String stringValue = "5003.23";
    private static double doubleValue = 5003.23;

    @BeforeClass
    public static void setUpSerializerTest() {
        mapper = new ObjectMapper();
        mapper.findAndRegisterModules();
        mapper.registerModule(new JavaTimeModule());
        mapper.configure(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS, true);
    }


    @Test
    public void bigDecimalSerializationTest() throws Exception {

        Container item = Container.builder().value(BigDecimal.valueOf(doubleValue)).build();
        JsonNode jsonNode = mapper.valueToTree(item);
        assertEquals(jsonNode.get("value").asText(),stringValue);

    }


    @Test
    public void bigDecimalDeserializationTest() throws Exception {
        ObjectNode objectNode = mapper.createObjectNode();
        objectNode.put("value",stringValue);

        Container item = mapper.treeToValue(objectNode, Container.class);
        assertEquals(item.getValue(), BigDecimal.valueOf(doubleValue));
    }

}