package taskManager.web.security.jwt;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import taskManager.web.dto.JwtAuthenticationDto;

@Service
public class JwtService {
    
    @Value("${jwt.secret}")
    private String jwtSecret;



    public JwtAuthenticationDto generateAuthToken(String email){
        JwtAuthenticationDto jwtDto = new JwtAuthenticationDto();
        jwtDto.setToken(generateJwtToken(email));
        jwtDto.setRefreshToken(generateRefreshToken(email));
        return jwtDto;
    }


    public JwtAuthenticationDto refreshBaseToken(String email, String refreshToken){
        JwtAuthenticationDto jwtDto = new JwtAuthenticationDto();
        jwtDto.setToken(generateJwtToken(email));
        jwtDto.setRefreshToken(refreshToken);
        return jwtDto;
    }


    public String getEmailFromToken(String token){
        Claims claims = Jwts.parser()
                            .verifyWith(getSingingKey())
                            .build()
                            .parseSignedClaims(token)
                            .getPayload();
        return claims.getSubject();
    }


    public boolean validateJwtToken(String token){
        try {
            Jwts.parser()
                    .verifyWith(getSingingKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
            return true;
        } catch(Exception ex){
            return false;
        }
    }


    private String generateJwtToken(String email){
        Date date = Date.from(LocalDateTime.now().plusMinutes(10).atZone(ZoneId.systemDefault()).toInstant());
        return Jwts.builder()
                .subject(email)
                .expiration(date)
                .signWith(getSingingKey())
                .compact();
    }

    private String generateRefreshToken(String email){
        Date date = Date.from(LocalDateTime.now().plusDays(1).atZone(ZoneId.systemDefault()).toInstant());
        return Jwts.builder()
                .subject(email)
                .expiration(date)
                .signWith(getSingingKey())
                .compact();
    }


    private SecretKey getSingingKey(){
        byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);

        return Keys.hmacShaKeyFor(keyBytes);
    }

}
