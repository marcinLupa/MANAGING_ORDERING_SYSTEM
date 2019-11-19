package impl;

import abstractConverter.JsonConverter;
import model.Stock;

import java.util.List;

public class StockListJsonConverter extends JsonConverter<List<Stock>> {
    public StockListJsonConverter(String jsonFilename) {
        super(jsonFilename);
    }
}
