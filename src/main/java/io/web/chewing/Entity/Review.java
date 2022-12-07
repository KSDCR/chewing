package io.web.chewing.Entity;

import io.web.chewing.config.security.dto.AuthMemberDTO;
import lombok.*;
import org.apache.ibatis.javassist.NotFoundException;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
@Table
public class Review extends BaseEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "store_id")
    private Store store;

    @NonNull
    private double rate;

    @OneToOne
    @JoinColumn(name = "member_nickname", referencedColumnName = "nickname")
    private Member member_id;

    @Column(length = 500)
    private String content;

    public void change(String content) {
        this.content = content;
    }

    public void assignUser(Member member) throws NotFoundException {


        this.member_id = member;
    }
}
