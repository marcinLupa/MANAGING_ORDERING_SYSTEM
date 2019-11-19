package impl;

import abstractConverter.JsonConverter;

import model.Country;

import java.util.List;

public class CountryListJsonConverter extends JsonConverter<List<Country>> {
    public CountryListJsonConverter(String jsonFilename) {
        super(jsonFilename);
    }
}
