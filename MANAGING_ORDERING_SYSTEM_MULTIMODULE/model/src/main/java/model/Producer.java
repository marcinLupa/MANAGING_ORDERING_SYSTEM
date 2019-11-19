package model;

import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

@Entity
@Table(name = "producers")
public class Producer {
    @Id
    @GeneratedValue
    private Long id;
    private String name;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "country_id")
    private Country country;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "trade_id")
    private Trade trade;


    @OneToMany(mappedBy = "producer",
    fetch = FetchType.EAGER)
    @EqualsAndHashCode.Exclude
    private Set<Product> products;

    @Override
    public String toString() {
        return "PRODUCER: " +
                "ID: " + id +
                ", NAME: " + name +
                ", COUNTRY: " +country.getName() +" "+
                ", TRADE: " +trade.getName();
    }
}
