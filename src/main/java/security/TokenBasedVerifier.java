package security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.data.ChallengeResponse;
import org.restlet.security.Role;
import org.restlet.security.User;
import org.restlet.security.Verifier;
import util.JWT;

public class TokenBasedVerifier implements Verifier {
    public int verify(Request request, Response response) {
        ChallengeResponse cr = request.getChallengeResponse();
        String token = cr.getRawValue();
        Claims claims;
        try {
            claims = getClaims(token);
        } catch (JwtException e) {
            return Verifier.RESULT_INVALID;
        }
        request.getClientInfo().getRoles().add(new Role(claims.get("role").toString()));
        request.getClientInfo().setUser(new User(claims.get("id").toString()));
        System.out.println("ROLE IS " + claims.get("role").toString());
        return Verifier.RESULT_VALID;
    }

    Claims getClaims(String token) {
        Claims claims = JWT.decodeJWT(token);
        return claims;
    }
}
