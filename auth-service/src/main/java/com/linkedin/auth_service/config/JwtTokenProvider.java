package com.linkedin.auth_service.config;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;

@Component
public class JwtTokenProvider {

    @Value("${app.jwt-secret}")
    private String jwtSecret;

    @Value("${app.jwt-expiration-milliseconds}")
    private long jwtExpirationDate;

    private final String ADMIN_KEY = "x-user-admin";
    private final String ID_KEY = "x-user-id";
    private final String EMAIL_KEY = "x-user-email";

    // generate JWT token
    public String generateToken(String email, long id, boolean isAdmin){
        Date currentDate = new Date();

        Date expireDate = new Date(currentDate.getTime() + jwtExpirationDate);

        return Jwts.builder()
                .subject(email)
                .issuedAt(currentDate)
                .expiration(expireDate)
                .claim(ADMIN_KEY, isAdmin)
                .claim(ID_KEY, id)
                .claim(EMAIL_KEY, email)
                .signWith(key())
                .compact();
    }

    // get email from jwt token
    public String getEmail(String token){
        return Jwts.parser()
                .verifyWith((SecretKey) key())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    // validate jwt token
//    public boolean validateToken(String token){
//        try {
//            Jwts.parser()
//                    .verifyWith((SecretKey) key())
//                    .build()
//                    .parse(token);
//            return true;
//        }
//        catch (MalformedJwtException malformedJwtException){
//            throw new ApiException(HttpStatus.BAD_REQUEST, "Invalid Jwt token");
//        }
//        catch (ExpiredJwtException expiredJwtException){
//            throw new ApiException(HttpStatus.BAD_REQUEST, "Expired Jwt token");
//        }
//        catch (UnsupportedJwtException unsupportedJwtException){
//            throw new ApiException(HttpStatus.BAD_REQUEST, "Unsupported Jwt token");
//        }
//        catch (IllegalArgumentException illegalArgumentException){
//            throw new ApiException(HttpStatus.BAD_REQUEST, "Jwt claims string is null or empty");
//        }
//    }

    // Decode base64 key
    private Key key(){
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }
}
