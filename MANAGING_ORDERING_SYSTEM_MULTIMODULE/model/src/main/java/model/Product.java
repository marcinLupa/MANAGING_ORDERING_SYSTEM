package model;

import lombok.*;
import utils.EnumGuaranteeComponents;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private BigDecimal price;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "category_id")
    private Category category;

    @OneToMany(mappedBy = "product")
    @EqualsAndHashCode.Exclude
    private Set<Stock> stocks;


    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "guarantee_components", joinColumns = @JoinColumn(name = "product_id"))
    @Column(name="guarantee_components")
    @Enumerated(EnumType.STRING)
    private Set<EnumGuaranteeComponents> enumGuaranteeComponents;


    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "producer_id")

    private Producer producer;

    @Override
    public String toString() {
        return "Product{" +
                "ID :" + id +
                ", NAME: " + name +
                ", PRICE: " + price +" "+
                ", CATEGORY: " + category.getName() +" "+
                ", ENUM GUARANTE COMPONENTS: " + enumGuaranteeComponents +" "+
                ", PRODUCER: " +producer.getName();
    }
}
