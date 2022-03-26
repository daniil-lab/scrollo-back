package com.inst.base.config;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.*;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

@Component
public class JwtProvider {
    private final String jwtSecret = Base64.getEncoder().encodeToString("xyz".getBytes());

    public String generateToken(String login, UUID id) {
        Date date = Date.from(LocalDate.now().plusDays(15).atStartOfDay(ZoneId.systemDefault()).toInstant());

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
        } catch (ExpiredJwtException expEx) {
            System.out.println("Token expired");
        } catch (UnsupportedJwtException unsEx) {
            System.out.println("Unsupported jwt");
        } catch (MalformedJwtException mjEx) {
            System.out.println("Malformed jwt");
        } catch (SignatureException sEx) {
            System.out.println("Invalid signature");
        } catch (Exception e) {
            System.out.println("invalid token");
        }
        return false;
    }

    public Claims getDataFromToken(String token) {
        ObjectMapper mapper = new ObjectMapper();

        return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody();
    }
}