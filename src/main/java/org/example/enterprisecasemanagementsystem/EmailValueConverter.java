package org.example.enterprisecasemanagementsystem;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class EmailValueConverter implements Converter<String, EmailValue> {

    @Override
    public EmailValue convert(String source) {
        System.out.println("=== EMAIL VALUE CONVERTER CALLED ===");
        System.out.println("Source: " + source);

        try {
            EmailValue emailValue = new EmailValue(source);
            System.out.println("Converted to: " + emailValue);
            return emailValue;
        } catch (Exception e) {
            System.out.println("Conversion error: " + e.getMessage());
            throw e;
        }
    }
}