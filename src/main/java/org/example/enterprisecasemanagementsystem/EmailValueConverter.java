package org.example.enterprisecasemanagementsystem;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class EmailValueConverter implements Converter<String, EmailValue> {

    @Override
    public EmailValue convert(String source) {
        System.out.println("=== EMAIL VALUE CONVERTER CALLED ===");
        System.out.println("Converting string to EmailValue: " + source);
        System.out.println("Stack trace:");
        new Exception("Converter stack trace").printStackTrace();

        return new EmailValue(source);
    }
}