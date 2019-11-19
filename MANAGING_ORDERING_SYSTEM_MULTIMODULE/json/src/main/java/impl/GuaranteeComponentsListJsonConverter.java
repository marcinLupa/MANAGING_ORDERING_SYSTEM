package impl;

import abstractConverter.JsonConverter;
import utils.EnumGuaranteeComponents;

import java.util.List;

public class GuaranteeComponentsListJsonConverter extends JsonConverter<List<EnumGuaranteeComponents>> {
    public GuaranteeComponentsListJsonConverter(String jsonFilename) {
        super(jsonFilename);
    }
}
