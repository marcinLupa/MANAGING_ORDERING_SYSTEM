package impl;


import abstractConverter.JsonConverter;
import model.CustomerOrder;
import java.util.List;

public class CustomerOrderListJSonConverter extends JsonConverter<List<CustomerOrder>> {
    public CustomerOrderListJSonConverter(String jsonFilename) {
        super(jsonFilename);
    }
}
