package org.example.enterprisecasemanagementsystem.infrastructure.web.serialization;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.example.enterprisecasemanagementsystem.domain.EmailValue;

import java.io.IOException;

public class EmailValueDeserializer extends JsonDeserializer<EmailValue> {
    @Override
    public EmailValue deserialize(JsonParser p, DeserializationContext ctxt)
            throws IOException {
        String email = p.getText();
        return new EmailValue(email);
    }
}
