package model;

import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

@Entity
@Table(name = "customers")
public class Customer {
    @Id
    @GeneratedValue
    private Long id;
    private Integer age;
    private String name;
    private String surname;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "country_id")
    private Country country;

    @OneToMany(mappedBy = "customer")
    @EqualsAndHashCode.Exclude
    private Set<CustomerOrder> customerOrders;

    @Override
    public String toString() {
        return "CUSTOMER: " +
                "ID: " + id +
                ", NAME: " + name +
                ", SURNAME: " + surname +
                ", AGE: " + age +" "+
                ", COUNTRY: " +country.getName();
    }
}
