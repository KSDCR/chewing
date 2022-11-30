package io.web.chewing.Entity;

import lombok.*;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
@Table
public class Store_Like extends BaseEntity{


    @EmbeddedId
    private Store_LikeID store_likeID;


}
