package model;

import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

@Entity
@Table(name = "shops")
public class Shop {
    @Id
    @GeneratedValue
    private Long id;
    private String name;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "country_id")
    private Country country;

    @OneToMany(mappedBy = "shop",
            fetch = FetchType.EAGER)
    @EqualsAndHashCode.Exclude
    private Set<Stock> stocks;

    @Override
    public String toString() {
        return "SHOP: " +
                "ID: " + id +
                ", NAME: " + name +" "+
                ", COUNTRY: " +country.getName();
    }
}
