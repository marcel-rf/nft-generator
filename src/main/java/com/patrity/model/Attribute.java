package com.patrity.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.File;
import java.util.Objects;

@Data
@AllArgsConstructor
public class Attribute {

    @JsonProperty("trait_type")
    public AttributeType type;
    @JsonProperty("value")
    public String name;
    @JsonIgnore
    public File file;
    public Rarity rarity;

    // Default no-arg constructor required for jackson
    public Attribute() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Attribute attribute = (Attribute) o;
        return type == attribute.type && (file == null && attribute.file == null || file.equals(attribute.file));
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, file);
    }
}
