package com.kdt_y_be_toy_project2.domain.itinerary.domain;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class TransportationTypeConverter implements AttributeConverter<TransportationType, String> {
	@Override
	public String convertToDatabaseColumn(TransportationType attribute) {
		if (attribute == null) {
			throw new NullPointerException("이동수단이 비어 있습니다.");
		}
		return attribute.getValue();
	}

	@Override
	public TransportationType convertToEntityAttribute(String dbData) {
		return TransportationType.getByValue(dbData);
	}
}
