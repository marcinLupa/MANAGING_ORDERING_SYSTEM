package impl;

import abstractConverter.JsonConverter;
import model.Payment;

import java.util.List;

public class PaymentListJsonConverter extends JsonConverter<List<Payment>> {
    public PaymentListJsonConverter(String jsonFilename) {
        super(jsonFilename);
    }
}
