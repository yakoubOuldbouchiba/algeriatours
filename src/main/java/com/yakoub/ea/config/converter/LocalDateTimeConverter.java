package com.yakoub.ea.config.converter;

import org.springframework.core.convert.converter.Converter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LocalDateTimeConverter implements Converter<LocalDateTime, String> {
    @Override
    public String convert(LocalDateTime source) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        String formattedDateTime = source.format(formatter);
        return formattedDateTime;
    }
}
