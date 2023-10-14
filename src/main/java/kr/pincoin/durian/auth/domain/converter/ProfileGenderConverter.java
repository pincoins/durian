package kr.pincoin.durian.auth.domain.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.stream.Stream;

@Converter
public class ProfileGenderConverter implements AttributeConverter<ProfileGender, Integer> {
    @Override
    public Integer convertToDatabaseColumn(ProfileGender gender) {
        if (gender == null) {
            return null;
        }

        return gender.getCode();
    }

    @Override
    public ProfileGender convertToEntityAttribute(Integer code) {
        if (code == null) {
            return null;
        }

        return Stream.of(ProfileGender.values())
                .filter(c -> c.getCode().equals(code))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}

