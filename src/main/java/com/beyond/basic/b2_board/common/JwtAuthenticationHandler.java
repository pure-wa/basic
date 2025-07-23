package com.beyond.basic.b2_board.common;

import com.beyond.basic.b2_board.author.dto.CommonErrorDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.PrintWriter;

// 401 Error인 경우
@Component
@Slf4j  // logback
public class JwtAuthenticationHandler implements AuthenticationEntryPoint {

    @Override
    // request = 사용자의 요청, response = 사용자에게 응답, accessDeniedException = 로그
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        log.error(authException.getMessage());  // error 메시지 받기(info 이상이라 받을 수 있음)
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);    // 401. 헤더에 상태코드 세팅
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        CommonErrorDto dto = new CommonErrorDto(401, "token이 없거나 유효하지 않습니다.");
        ObjectMapper objectMapper = new ObjectMapper();
        String body = objectMapper.writeValueAsString(dto);

        // 리턴
        PrintWriter printWriter = response.getWriter();
        printWriter.write(body);
        printWriter.flush();
    }
}