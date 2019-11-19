package model;

import lombok.*;
import utils.EPayment;

import javax.persistence.*;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

@Entity
@Table(name = "payments")
public class Payment {
    @Id
    @GeneratedValue
    private Long id;
    @Enumerated(EnumType.STRING)
    private EPayment payment;

    @OneToMany(mappedBy = "payment")
    @EqualsAndHashCode.Exclude
    private Set<CustomerOrder> customerOrders;

    @Override
    public String toString() {
        return "PAYMENT: " +
                "ID: " + id +
                ", PAYMENT: " + payment;
    }
}
