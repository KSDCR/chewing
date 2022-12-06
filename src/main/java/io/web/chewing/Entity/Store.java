package io.web.chewing.Entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Table
public class Store extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    private String detail;

    private LocalDateTime openTime;

    private LocalDateTime closeTime;

    private String address;

    private String phone;

    private String file;


    @ElementCollection(fetch = FetchType.LAZY)
    @Builder.Default
    private Set<Categories> categoriesSet = new HashSet<>();

    public void addCategories(Categories categories) {
        categoriesSet.add(categories);
    }

    public void clearCategories() {
        this.categoriesSet.clear();
    }

}
