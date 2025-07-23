package com.beyond.basic.b2_board.common;

import com.beyond.basic.b2_board.author.domain.Author;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.crypto.Data;
import java.security.Key;
import java.security.Signature;
import java.util.Date;

@Component
public class JwtTokenProvider {

    @Value("${jwt.expirationAt}")
    private int expirationAt;

    @Value("${jwt.secretKeyAt}")
    private String secretKeyAt;

    private Key secret_at_key;

    //스프링빈이 만들어지는 시점에 빈이 만들어진 직후에 아래 메서드가 바로 실행
    @PostConstruct
    public void makeKey(){
        // 암호화된 시크릿키를 다시 디코딩해주어야 한다. 두번째 인자는 어떤 알고리즘이 작성할 것인지.
        secret_at_key = new SecretKeySpec(java.util.Base64.getDecoder().decode(secretKeyAt), SignatureAlgorithm.HS512.getJcaName());
    }
    public String createAtToken(Author author){
        String email = author.getEmail();
        String role = author.getRole().toString();
        // claims는 페이로드(사용자 정보)
        Claims claims = Jwts.claims().setSubject(email);
        //주된키값을 제외한 나머지 사용자 정보는 put 사용하여 key:value세팅
        claims.put("role",role);

        Date now = new Date();
        //헤더 : 알고리즘 방식 - jwt
        String token = Jwts.builder()
                .setClaims(claims)
                //발행시간
                .setIssuedAt(now)
                //만료시간
                .setExpiration(new Date(now.getTime()+expirationAt*60*1000L)) // 30분을 세팅
                //시크릿키
                .signWith(secret_at_key)
                .compact();
        return token;
    }
}