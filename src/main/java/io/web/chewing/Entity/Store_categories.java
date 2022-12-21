package io.web.chewing.Entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Builder
@DynamicInsert
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Table
public class Store_categories implements Serializable {

    @Id
    @OneToOne
    @JoinColumn(name = "store_id")
    Store store;

    String category;
}
