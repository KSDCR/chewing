package io.web.chewing.Entity;

import io.web.chewing.domain.NoticeDTO;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
@Table
@EntityListeners(value = {AuditingEntityListener.class})
public class Notice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String content;


    @CreatedDate
    @Column(name = "creat_time", updatable = false)
    private LocalDateTime created_at;

    @LastModifiedDate
    @Column(name = "modify_time")
    private LocalDateTime modified_at;

    public void change(NoticeDTO notice){
        this.title = notice.getTitle();
        this.content = notice.getContent();
    }
}
