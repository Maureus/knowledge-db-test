package com.example.knowledgedbcore.security.jwt;


import com.example.knowledgedbcore.dao.authdao.AuthTokenDao;
import com.example.knowledgedbcore.models.AuthToken;
import com.example.knowledgedbcore.security.services.UserDetailsImpl;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.function.Function;

@Component
public class JwtUtils {
    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    @Value("${knowledgedb.app.jwtSecret}")
    private String jwtSecret;

    @Value("${knowledgedb.app.jwtExpirationMs}")
    private int jwtExpirationMs;

    private AuthTokenDao authTokenDao;

    @Autowired
    public JwtUtils(AuthTokenDao authTokenDao) {
        this.authTokenDao = authTokenDao;
    }

    public String generateJwtToken(Authentication authentication) {

        UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();

        return Jwts.builder()
                .setSubject((userPrincipal.getUsername()))
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    public String getUserNameFromJwtToken(String token) {
        return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody();
    }

    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    public boolean validateJwtToken(String authToken) {
        try {
//            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
            AuthToken authTokenDB = authTokenDao.findByJwtToken(authToken)
                    .orElseThrow(() -> new IllegalArgumentException("Token is not found or expired"));
            String userName = getUsernameFromToken(authTokenDB.getJwtToken());

            if (isTokenExpired(authTokenDB.getJwtToken())) {
                authTokenDao.delete(authTokenDB);
                return false;
            }

            if (userName == null) {
                return false;
            }

            return true;
        } catch (SignatureException e) {
            logger.error("Invalid JWT signature: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty: {}", e.getMessage());
        }

        return false;
    }
}
