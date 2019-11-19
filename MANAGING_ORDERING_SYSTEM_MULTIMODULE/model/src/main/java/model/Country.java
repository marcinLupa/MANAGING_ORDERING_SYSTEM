package model;

import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

@Entity
@Table(name = "countries")
public class Country {
    @Id
    @GeneratedValue
    private Long id;
    private String name;

    @OneToMany(mappedBy = "country")
    @EqualsAndHashCode.Exclude
    private Set<Shop> shops;

    @Override
    public String toString() {
        return "COUNTRY: " +
                "ID: " + id +
                ", NAME: " + name;
    }
}
