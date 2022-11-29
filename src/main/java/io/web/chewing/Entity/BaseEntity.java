package io.web.chewing.Entity;

import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@MappedSuperclass
@EntityListeners(value = {AuditingEntityListener.class})
@Getter
public abstract class BaseEntity {
    @CreatedDate
    @Column(name = "creat_time", updatable = false)
    private LocalDateTime created_at;

    @LastModifiedDate
    @Column(name = "modify_time")
    private LocalDateTime modified_at;

}
