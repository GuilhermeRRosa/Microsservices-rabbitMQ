package gg.code.productapi.dto.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class JwtResponse {

    private Integer id;
    private String name;
    private String email;

    /*
        Recupera as claims contidas no JWT(Objeto authuser) para o JwtResponse
     */
    public static JwtResponse getUser(Claims jwtClaims){
        try {
            return new ObjectMapper().convertValue(jwtClaims.get("authUser"), JwtResponse.class);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
