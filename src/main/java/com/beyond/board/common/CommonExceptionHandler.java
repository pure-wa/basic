//package com.beyond.board.common;
//
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.MethodArgumentNotValidException;
//import org.springframework.web.bind.annotation.ControllerAdvice;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//
//import java.util.NoSuchElementException;
//
//// Controller 어노테이션이 붙어있는 클래스의 모든 예외를 모니터링하여 예외를 인터셉팅
//@ControllerAdvice
//@Slf4j
//public class CommonExceptionHandler {
//
//    @ExceptionHandler(IllegalArgumentException.class)
//    public ResponseEntity<?> illegalArgumentException(IllegalArgumentException e) {
//        return new ResponseEntity<>(new CommonErrorDto(HttpStatus.BAD_REQUEST.value(), e.getMessage()), HttpStatus.BAD_REQUEST);
//    }
//
//    @ExceptionHandler(NoSuchElementException.class)
//    public ResponseEntity<?> noSuchElementException(NoSuchElementException e) {
//
//        log.error("[HANSOOM][ERROR] - NoSuchElementException - {}", e.getMessage());
//
//        return new ResponseEntity<>(new CommonErrorDto(HttpStatus.NOT_FOUND.value(), e.getMessage()), HttpStatus.NOT_FOUND);
//    }
//
//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    public ResponseEntity<String> handleValidationException(MethodArgumentNotValidException e) {
//        // 첫 번째 필드 에러에서 기본 메시지만 가져오기
//        String errorMessage = e.getBindingResult()
//                .getFieldError()
//                .getDefaultMessage();
//
//        return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
//    }
//
//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<?> error(Exception e) {
//        return new ResponseEntity<>(new CommonErrorDto(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
//    }
//
//
//}
