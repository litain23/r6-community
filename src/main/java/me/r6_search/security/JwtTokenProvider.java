package me.r6_search.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Component
public class JwtTokenProvider {
    @Value("${jwt.expiration-time}")
    private long JWT_TOKEN_EXPIRATION_SECOND;

    @Value("${jwt.secret}")
    private String JWT_SECRET;

    @Value("${jwt.refresh-expiration-time}")
    private long JWT_REFRESH_TOKEN_EXPIRATION_SECOND;

    @Value("${jwt.refresh-secret}")
    private String JWT_REFRESH_SECRET;

    private final UserProfileDetailsService userProfileDetailsService;

    private SecretKey getSignKey(String secret) {
       byte[] keyBytes = Decoders.BASE64.decode(secret);
       return new SecretKeySpec(keyBytes, SignatureAlgorithm.HS256.getJcaName());
    }

    public String generateToken(String username) {
        Map<String, Object> claims = new HashMap<>();
        return doGenerateToken(claims, username, JWT_SECRET, JWT_TOKEN_EXPIRATION_SECOND);
    }

    public String generateRefreshToken(String username) {
        Map<String, Object> claims = new HashMap<>();
        return doGenerateToken(claims, username, JWT_REFRESH_SECRET, JWT_REFRESH_TOKEN_EXPIRATION_SECOND);
    }

    private String doGenerateToken(Map<String, Object> claims, String subject, String secret, long expiredTime) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiredTime * 1000))
                .signWith(getSignKey(secret), SignatureAlgorithm.HS256)
                .compact();
    }

    public Date getExpireDate(Claims claims) {
        return claims.getExpiration();
    }

    public String getUsername(Claims claims) {
        return claims.getSubject();
    }

    public String getUsernameUsingRefreshToken(String token) {
        Claims claims = getClaimsFromToken(token, JWT_REFRESH_SECRET);
        return getUsername(claims);
    }

    public boolean isTokenExpiration(Claims claims) {
        return getExpireDate(claims).before(new Date(System.currentTimeMillis()));
    }

    public String resolveToken(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if(token != null && token.startsWith("Bearer ")) {
            return token.substring(7);
        }
        return null;
    }

    public Authentication getAuthentication(String token) {
        Claims claims = getClaimsFromToken(token, JWT_SECRET);
        UserProfileDetails userProfileDetails = userProfileDetailsService.loadUserByUsername(getUsername(claims));
        return new UsernamePasswordAuthenticationToken(userProfileDetails, null, userProfileDetails.getAuthorities());
    }

    private Claims getClaimsFromToken(String token, String secret) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignKey(secret))
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public boolean validateToken(String token) {
        try {
            Claims claims = getClaimsFromToken(token, JWT_SECRET);

            if (isTokenExpiration(claims)) {
                return false;
            }
            return true;
        } catch (ExpiredJwtException e) {
            throw e;
        } catch (JwtException e) {
            throw e;
        }
    }

    public boolean validateRefreshToken(String token) {
        try {
            Claims claims = getClaimsFromToken(token, JWT_REFRESH_SECRET);

            if (isTokenExpiration(claims)) {
                return false;
            }
            return true;
        } catch (ExpiredJwtException e) {
            throw e;
        } catch (JwtException e) {
            throw e;
        }
    }
}
