package impl;

import abstractConverter.JsonConverter;
import model.Shop;
import java.util.List;

public class ShopListJsonConverter extends JsonConverter<List<Shop>> {
    public ShopListJsonConverter(String jsonFilename) {
        super(jsonFilename);
    }
}
