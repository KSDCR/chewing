package io.web.chewing.service;

import io.web.chewing.Entity.Notice;
import io.web.chewing.domain.NoticeDTO;
import io.web.chewing.repository.NoticeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class NoticeService {
   private final ModelMapper modelMapper;
    private final NoticeRepository noticeRepository;

    public Long register(NoticeDTO noticeDTO) {

        /*Notice notice = modelMapper.map(noticeDTO, Notice.class);*/
        Notice notice1 = io.web.chewing.Entity.Notice.builder()
                .content(noticeDTO.getContent())
                .title(noticeDTO.getTitle())
                .build();
            log.info("NoticeService register()");

        Long bno = noticeRepository.save(notice1).getId();

        return bno;
    }
    public List<Notice> noticeDTOList (){
        List<Notice> notices = noticeRepository.findAll();
        log.info("NoticeService:" + notices);


         return notices;
    }

    public Notice get(Long id) {
        Optional<Notice> notice = noticeRepository.findById(id);
        Notice findNotice = notice.orElseThrow();
        log.info("NoticeService 값 확인"+findNotice.getTitle()+ findNotice.getContent());

        return findNotice;
    }

    public void delete(Long id) {
        Optional<Notice> beoNotice = noticeRepository.findById(id);
        Notice beNotice = beoNotice.orElseThrow();

        noticeRepository.delete(beNotice);
    }

    public void modify(NoticeDTO notice) {
        Optional<Notice> beoNotice = noticeRepository.findById(notice.getId());
        Notice beNotice = beoNotice.orElseThrow();
        beNotice.change(notice);
        log.info("beNotice = "+String.valueOf(beNotice));
        noticeRepository.save(beNotice);
    }
}