package Utilities;

import java.io.IOException;

import org.joda.money.Money;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;


public class JodaMoneyJsonSerializer extends JsonSerializer<Money> {

    @Override
    public void serialize(Money value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonProcessingException {
        jgen.writeString(value.toString());
    }
}