package model;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

@Entity
@Table(name = "customer_orders")
public class CustomerOrder {
    @Id
    @GeneratedValue
    private Long id;
    private LocalDate date;
    private BigDecimal discount;
    private Integer quantity;


    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "customer_id")
    private Customer customer;


    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "payment_id")
    private Payment payment;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "product_id")
    private Product product;

    @Override
    public String toString() {
        return "CUSTOMER ORDER: " +
                "ID: " + id +
                ", DATE: " + date +
                ", DISCOINT: " + discount +
                ", QUANTITY: " + quantity +
                ", CUSTOMER: " +customer.getName() +" "+
                 customer.getSurname() +" "+
                ", PAYMENT: " +payment.getPayment() +" "+
                ", PRODUCT: " +product.getName();
    }
}
