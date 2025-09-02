package com.tradeflow.inventory_backend.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Component
public class StringToLocalDateConverter implements Converter<String, LocalDate> {

	private static final DateTimeFormatter[] FORMATTERS = {
			DateTimeFormatter.ISO_LOCAL_DATE,
			DateTimeFormatter.ofPattern("yyyy-MM-dd"),
			DateTimeFormatter.ofPattern("dd-MM-yyyy"),
			DateTimeFormatter.ofPattern("MM/dd/yyyy")
	};

	@Override
	public LocalDate convert(String source) {
		if (source == null || source.trim().isEmpty()) {
			return null;
		}

		for (DateTimeFormatter formatter : FORMATTERS) {
			try {
				return LocalDate.parse(source.trim(), formatter);
			} catch (DateTimeParseException e) {
				// Try next formatter
			}
		}

		throw new IllegalArgumentException("Unable to parse date: " + source +
				". Please use format: YYYY-MM-DD");
	}
}