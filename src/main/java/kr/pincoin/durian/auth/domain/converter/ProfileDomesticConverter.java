package kr.pincoin.durian.auth.domain.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.stream.Stream;

@Converter
public class ProfileDomesticConverter implements AttributeConverter<ProfileDomestic, Integer> {
    @Override
    public Integer convertToDatabaseColumn(ProfileDomestic domestic) {
        if (domestic == null) {
            return null;
        }

        return domestic.getCode();
    }

    @Override
    public ProfileDomestic convertToEntityAttribute(Integer code) {
        if (code == null) {
            return null;
        }

        return Stream.of(ProfileDomestic.values())
                .filter(c -> c.getCode().equals(code))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}