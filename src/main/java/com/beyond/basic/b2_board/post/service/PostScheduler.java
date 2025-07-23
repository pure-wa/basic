package com.beyond.basic.b2_board.post.service;

import com.beyond.basic.b2_board.post.domain.Post;
import com.beyond.basic.b2_board.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Component
@Transactional
@RequiredArgsConstructor
//스케쥴러 서버를 2대 이상 운영하게 되면, 중복 스케쥴이 돌아갈 여지 있으므로,
//redis등의 솔루션을 사용하여 스케쥴 제어.
public class PostScheduler {
    private final PostRepository postRepository;

//    cron의 각 자리는 "초 분 시간 일 월 요일" 을 의미
//    * * * * * * : 매월 , 매일 , 매시간 , 매분 , 매초 마다 돌아감.
//    0 0 * * * * : 매월 , 매일 , 매시간 , 0분 , 0초에
//    0 0 11 * * *: 매월 , 매일 , 11시, 0분 ,0초에
//    0 0/1 * * * *: 매월, 매일 , 매시 1분마다 돌아간다.
    @Scheduled(cron = "0 0/1 * * * *")
    public void postSchedule(){
        log.info("===예약스케쥴러 시작===");
//        3개인수로 설정
        List<Post> posts = postRepository.findByAppointmentAndDelYn("Y", "N", Pageable.unpaged()).getContent();
        LocalDateTime now = LocalDateTime.now();
        for (Post p : posts){
            if(p.getAppointmentTime().isBefore(now)){
                p.updateAppointment("N");
            }
        }

        log.info("===예약스케쥴러 끝===");
    }
}
