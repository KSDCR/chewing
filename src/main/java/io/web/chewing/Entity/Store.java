package io.web.chewing.Entity;

import lombok.*;

import javax.persistence.*;
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
@ToString
@Table
public class Store extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    private String detail;

    private Time openTime;

    private Time closeTime;

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

    public void change(String name, String detail, String address, String phone, String file, Time openTime, Time closeTime) {
        this.name = name;
        this.detail = detail;
        this.address = address;
        this.phone = phone;
        this.file = file;
        this.openTime = openTime;
        this.closeTime = closeTime;
    }
}
