package impl;

import abstractConverter.JsonConverter;

import model.Customer;

import java.util.List;

public class CustomerListJsonConverter extends JsonConverter<List<Customer>> {
    public CustomerListJsonConverter(String jsonFilename) {
        super(jsonFilename);
    }
}
