package uk.gov.digital.ho.egar.people.model;

import org.apache.commons.lang.StringUtils;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;


public enum Gender {
    MALE,
    FEMALE,
    UNSPECIFIED;

    @JsonCreator
    public static Gender forValue(String value) {
        if (value == null) {
            return null;
        }

        return Gender.valueOf(StringUtils.upperCase(value));
    }

    @JsonValue
    public String toValue() {
        return this.name();
    }


}