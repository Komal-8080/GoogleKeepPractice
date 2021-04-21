package com.bridgelabz.googlekeep.utility;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.auth0.jwt.interfaces.Verification;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.UUID;

@Component
public class TokenUtil {
    public final String TOKEN_SECRET = "sd5745FAHFW";

    public String createToken(UUID id) {
        try {
            // to set algorithm
            Algorithm algorithm = Algorithm.HMAC256(TOKEN_SECRET);
            // payload
            long EXPIRATION_TIME = 1800_000 ; // 30Mins
            String token = JWT.create().withClaim("userId", String.valueOf(id)).withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME)).sign(algorithm);
            return token;
        } catch (JWTCreationException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String refreshToken(UUID id) {
        try {
            // to set algorithm
            Algorithm algorithm = Algorithm.HMAC256(TOKEN_SECRET);
            // payload
            long EXPIRATION_TIME = 900_000 ;
            String token = JWT.create().withClaim("userId", String.valueOf(id)).withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME)).sign(algorithm);
            return token;
        } catch (JWTCreationException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String decodeToken(String token) {
        UUID id;
        // for verification algorithm
        Verification verification = null;
        try {
            verification = JWT.require(Algorithm.HMAC256(TOKEN_SECRET));
        } catch (IllegalArgumentException e) {

            e.printStackTrace();
        }
        JWTVerifier jwtverifier = verification.build();
        // to decode token
        DecodedJWT decodedjwt = jwtverifier.verify(token);

        Claim claim = decodedjwt.getClaim("userId");
        id = UUID.fromString(claim.asString());
        return String.valueOf(id);
    }

}
