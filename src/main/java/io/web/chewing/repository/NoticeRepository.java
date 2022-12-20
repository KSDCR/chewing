package io.web.chewing.repository;

import io.web.chewing.Entity.Notice;
import io.web.chewing.domain.NoticeDTO;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NoticeRepository extends JpaRepository<Notice, Long> {

}
