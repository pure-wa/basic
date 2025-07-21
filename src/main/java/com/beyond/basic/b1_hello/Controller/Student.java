package com.beyond.basic.b1_hello.Controller;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

//@Getter // 클래스내의 모든 속성을 대상으로 getter가 생성
@Data // getter, setter, toString 메서드까지 모두 만들어주는 어노테이션
@AllArgsConstructor // 모든 매개변수가 있는 생성
@NoArgsConstructor // 기본 생성자
public class Student {
    private String name;
    private String email;
    private List<Score> scores;
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    static
    class Score {
        private String subject;
        private int point;
    }
}
