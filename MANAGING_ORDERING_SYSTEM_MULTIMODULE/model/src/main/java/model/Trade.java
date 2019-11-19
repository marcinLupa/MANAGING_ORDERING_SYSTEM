package model;

import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

@Entity
@Table(name = "trades")
public class Trade {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;

    @OneToMany(mappedBy = "trade")
    @EqualsAndHashCode.Exclude
    private Set<Producer> producers;

    @Override
    public String toString() {
        return "TRADE: " +
                "ID: " + id +
                ", NAME: " + name;
    }
}
