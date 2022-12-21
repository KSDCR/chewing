package io.web.chewing.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Store_LikeID implements Serializable {

    @Id
    @OneToOne
    @JoinColumn(name = "store_name", referencedColumnName = "name")
    private Store store;

    @Id
    @ManyToOne
    @JoinColumn(name = "member_nickname", referencedColumnName = "nickname")
    private Member member;
}
