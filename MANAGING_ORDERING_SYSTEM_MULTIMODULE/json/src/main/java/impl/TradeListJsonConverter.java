package impl;

import abstractConverter.JsonConverter;
import model.Trade;

import java.util.List;

public class TradeListJsonConverter extends JsonConverter<List<Trade>> {
    public TradeListJsonConverter(String jsonFilename) {
        super(jsonFilename);
    }
}
