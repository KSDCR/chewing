package io.web.chewing.config.security.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.log4j.Log4j2;

import java.security.Key;
import java.time.ZonedDateTime;
import java.util.Date;


@Log4j2
public class JWTUtil {

    private Key randomKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    public String generateToken(String content) {

        //유효기간
        long expire = 60 * 10;

        return Jwts.builder()
                .setIssuedAt(new Date())
                .setExpiration(Date.from(ZonedDateTime.now().plusMinutes(expire).toInstant()))
                .claim("sub", content)
                .signWith(randomKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public String validateAndExtract(String tokenStr) {
        String contentValue;

        try {

            Jws jws = Jwts.parserBuilder().setSigningKey(randomKey).build().parseClaimsJws(tokenStr);
            log.info("--------------------------------");

            log.info(jws);
            log.info(jws.getBody().getClass());

            Claims claims = (Claims) jws.getBody();
            log.info("로그" + claims);

            log.info("--------------------------------");

            contentValue = claims.getSubject();


        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            contentValue = null;
        }
        return contentValue;
    }


}
