package dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import utils.EnumGuaranteeComponents;

import java.math.BigDecimal;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductDto {
    private Long id;
    private String name;
    private BigDecimal price;
    private CategoryDto categoryDto;
    private ProducerDto producerDto;
    private Set<EnumGuaranteeComponents> enumGuaranteeComponents;

}
