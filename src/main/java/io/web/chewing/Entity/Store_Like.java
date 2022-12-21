package io.web.chewing.Entity;

import lombok.*;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Table
public class Store_Like extends BaseEntity implements Serializable {


    @EmbeddedId
    private Store_LikeID store_likeID;


}
