package io.web.chewing.Entity;

import io.web.chewing.config.security.dto.AuthMemberDTO;
import lombok.*;
import org.apache.ibatis.javassist.NotFoundException;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Table
@EntityListeners(value = {AuditingEntityListener.class})
public class Review implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "store_id", referencedColumnName = "id")
    private Store store;

    @NonNull
    private double rate;

    @OneToOne
    @JoinColumn(name = "member_nickname", referencedColumnName = "nickname")
    private Member member_id;

    @Column(length = 500)
    private String content;

    @CreatedDate
    @Column(name = "creat_time", updatable = false)
    private LocalDateTime created_at;

    @LastModifiedDate
    @Column(name = "modify_time")
    private LocalDateTime modified_at;

    public void change(String content) {
        this.content = content;
    }

    public void assignUser(Member member) {

        this.member_id = member;
    }
}
