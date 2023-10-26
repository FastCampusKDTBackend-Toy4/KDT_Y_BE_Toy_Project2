package com.kdt_y_be_toy_project2.domain.trip.domain;

import jakarta.persistence.AttributeConverter;

public class TripTypeConverter implements AttributeConverter<TripType, String> {
    @Override
    public String convertToDatabaseColumn(TripType attribute) {
        if (attribute == null) {
            throw new NullPointerException("TripType이 비어 있습니다.");
        }
        return attribute.getValue();
    }

    @Override
    public TripType convertToEntityAttribute(String dbData) {
        return TripType.getByValue(dbData);
    }
}
