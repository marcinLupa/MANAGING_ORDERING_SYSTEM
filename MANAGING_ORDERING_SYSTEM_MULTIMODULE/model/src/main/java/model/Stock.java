package model;

import lombok.*;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

@Entity
@Table(name = "stocks")
public class Stock {
    @Id
    @GeneratedValue
    private Long id;
    private Integer quantity;


    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "product_id")
    private Product product;


    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "shop_id")
    private Shop shop;

    @Override
    public String toString() {
        return "STOCK: " +
                "ID: " + id +
                ", QUANTITY: " + quantity +" "+
                ", PRODUCT: " +product.getName() +" "+
                ", SHOP: " +shop.getName();
    }
}
