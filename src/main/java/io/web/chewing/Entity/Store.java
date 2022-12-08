package io.web.chewing.Entity;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Time;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table
public class Store extends BaseEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    private String detail;

    private String openTime;

    private String closeTime;

    private String address;

    private String phone;

    private String file;

    @Enumerated(EnumType.STRING)
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
