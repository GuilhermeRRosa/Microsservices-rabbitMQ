package gg.code.productapi.services;

import gg.code.productapi.config.exceptions.AuthenticationException;
import gg.code.productapi.dto.jwt.JwtResponse;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.nio.charset.StandardCharsets;

@Service
public class JwtService {

    @Value("${app-config.secrets.api-secret}")
    private String apiSecret;

    private static final String BEARER = "bearer";

    public void validateAuthorization(String token){
        var accesstoken = extractToken(token);
        try {
            var claims = Jwts.parserBuilder()
                    .setSigningKey(Keys.hmacShaKeyFor(apiSecret.getBytes()))
                    .build()
                    .parseClaimsJws(accesstoken)
                    .getBody();
            var user = JwtResponse.getUser(claims);
            if (ObjectUtils.isEmpty(user) || ObjectUtils.isEmpty(user.getId())) {
                throw new AuthenticationException("The user is not valid");
            }
        } catch (Exception e){
            e.printStackTrace();
            throw new AuthenticationException("Error while processing the access token");
        }
    }

    private String extractToken(String token){
        if (ObjectUtils.isEmpty(token)){
            throw new AuthenticationException("The access token was not informed");
        }

        if (token.contains(" ")) {
            return token.split(" ")[1];
        }

        return token;
    }

}
