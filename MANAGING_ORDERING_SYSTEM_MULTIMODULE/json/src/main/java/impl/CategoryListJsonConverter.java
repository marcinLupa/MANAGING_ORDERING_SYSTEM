package impl;


import abstractConverter.JsonConverter;
import model.Category;

import java.util.List;

public class CategoryListJsonConverter extends JsonConverter<List<Category>> {
    public CategoryListJsonConverter(String jsonFilename) {
        super(jsonFilename);
    }
}
