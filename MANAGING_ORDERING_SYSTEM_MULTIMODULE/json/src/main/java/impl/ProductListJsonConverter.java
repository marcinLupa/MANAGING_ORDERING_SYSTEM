package impl;


import abstractConverter.JsonConverter;
import model.Product;
import java.util.List;

public class ProductListJsonConverter extends JsonConverter<List<Product>> {
    public ProductListJsonConverter(String jsonFilename) {
        super(jsonFilename);
    }
}
