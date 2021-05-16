package util;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import javax.xml.bind.DatatypeConverter;
import java.sql.Date;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Random;

public class JWT {

    private static String base64 = "vaqh5GQ3vGHLPhe2igBk/mCl7B0K458qdocFr4X14GU=";

    private static byte[] secret = Base64.getDecoder().decode(base64);

    public static String jwtCreate(String subject,String audience,String role, Long id){
        Instant now = Instant.now();

        String jwt = Jwts.builder()
                .setSubject(subject)
                .setAudience(audience)
                .claim("role", role)
                .claim("id", id)
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(now.plus(15, ChronoUnit.MINUTES)))
                .signWith(Keys.hmacShaKeyFor(secret))
                .compact();
            return jwt;
    }

    public static Claims decodeJWT(String jwt) {
        //This line will throw an exception if it is not a signed JWS (as expected)
        Claims claims = Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(jwt).getBody();
        return claims;
    }

}
