package impl;

import abstractConverter.JsonConverter;
import model.Producer;
import java.util.List;

public class ProducerListJsonConverter extends JsonConverter<List<Producer>> {
    public ProducerListJsonConverter(String jsonFilename) {
        super(jsonFilename);
    }
}
