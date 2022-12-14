package io.web.chewing.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Store_LikeID implements Serializable {

    @OneToOne
    @JoinColumn(name = "store_name", referencedColumnName = "name")
    private Store store;

    @ManyToOne
    @JoinColumn(name = "member_nickname", referencedColumnName = "nickname")
    private Member member;
}
