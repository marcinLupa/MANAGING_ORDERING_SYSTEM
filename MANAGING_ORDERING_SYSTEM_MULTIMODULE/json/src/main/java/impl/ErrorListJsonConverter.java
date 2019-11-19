package impl;


import abstractConverter.JsonConverter;
import model.Errors;

import java.util.List;

public class ErrorListJsonConverter extends JsonConverter<List<Errors>> {
    public ErrorListJsonConverter(String jsonFilename) {
        super(jsonFilename);
    }
}
