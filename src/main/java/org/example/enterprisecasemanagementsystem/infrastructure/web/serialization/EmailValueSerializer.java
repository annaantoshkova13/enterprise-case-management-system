package org.example.enterprisecasemanagementsystem.infrastructure.web.serialization;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.example.enterprisecasemanagementsystem.domain.EmailValue;

import java.io.IOException;

public class EmailValueSerializer extends JsonSerializer<EmailValue> {
    @Override
    public void serialize(EmailValue value, JsonGenerator gen, SerializerProvider serializers)
            throws IOException {
        gen.writeString(value.getValue());
    }

}
