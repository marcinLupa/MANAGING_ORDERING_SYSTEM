package model;

import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

@Entity
@Table(name = "categories")
public class Category {
    @Id
    @GeneratedValue
    private Long id;
    private String name;

    @OneToMany(mappedBy = "category")
    @EqualsAndHashCode.Exclude
    private Set<Product> products;

    @Override
    public String toString() {
        return "CATEGORY: " +
                "ID: " + id +
                ", NAME: " + name;
    }
}
