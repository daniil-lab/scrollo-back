package com.inst.base.config;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.inst.base.util.ServiceException;
import io.jsonwebtoken.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Base64;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

@Component
public class RefreshJwtProvider {
    private final String jwtSecret = Base64.getEncoder().encodeToString("refresh".getBytes());

    public String generateToken(String login, UUID id) {
        Date date = Date.from(LocalDate.now().plusDays(90).atStartOfDay(ZoneId.systemDefault()).toInstant());

        ObjectMapper mapper = new ObjectMapper();

        AuthCredentials credentials = new AuthCredentials(login, id);

        Map<String, Object> tokenData = mapper.convertValue(credentials, new TypeReference<>() {});

        return Jwts.builder()
                .setClaims(tokenData)
                .setExpiration(date)
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token);
            return true;
        } catch (Exception expEx) {
            throw new ServiceException("Invalid JWT", HttpStatus.BAD_REQUEST);
        }
    }

    public Claims getDataFromToken(String token) {
        ObjectMapper mapper = new ObjectMapper();

        return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody();
    }
}