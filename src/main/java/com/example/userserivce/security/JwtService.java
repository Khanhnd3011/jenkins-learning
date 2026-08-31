package com.example.userserivce.security;

import com.example.userserivce.entity.Role;
import com.example.userserivce.entity.User;
import com.example.userserivce.exception.AppException;
import com.example.userserivce.exception.ErrorCode;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.StringJoiner;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class JwtService {

    public static final String ACCESS = "access";
    public static final String REFRESH = "refresh";

    String signerKey;
    long accessDuration;
    long refreshDuration;

    public JwtService(
            @Value("${jwt.signer-key}") String signerKey,
            @Value("${jwt.access-duration}") long accessDuration,
            @Value("${jwt.refresh-duration}") long refreshDuration) {
        this.signerKey = signerKey;
        this.accessDuration = accessDuration;
        this.refreshDuration = refreshDuration;
    }

    public String generateAccessToken(User user) {
        return sign(new JWTClaimsSet.Builder()
                .subject(user.getUserId())
                .issuer("user-serivce")
                .issueTime(new Date())
                .expirationTime(accessExpiryTime())
                .claim("token_type", ACCESS)
                .claim("email", user.getEmail())
                .claim("roles", buildScope(user))
                .build());
    }

    public String generateRefreshToken(User user) {
        return sign(new JWTClaimsSet.Builder()
                .subject(user.getUserId())
                .issuer("user-serivce")
                .issueTime(new Date())
                .expirationTime(refreshExpiryTime())
                .claim("token_type", REFRESH)
                .build());
    }

    public Date accessExpiryTime() {
        return Date.from(Instant.now().plus(accessDuration, ChronoUnit.SECONDS));
    }

    public Date refreshExpiryTime() {
        return Date.from(Instant.now().plus(refreshDuration, ChronoUnit.SECONDS));
    }

    public SignedJWT verify(String token, String expectedType) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);
            JWSVerifier verifier = new MACVerifier(signerKey.getBytes());
            Date expiry = signedJWT.getJWTClaimsSet().getExpirationTime();
            boolean valid = signedJWT.verify(verifier) && expiry.after(new Date());
            if (!valid) {
                throw new AppException(ErrorCode.INVALID_TOKEN);
            }
            String tokenType = signedJWT.getJWTClaimsSet().getStringClaim("token_type");
            if (!expectedType.equals(tokenType)) {
                throw new AppException(ErrorCode.INVALID_TOKEN);
            }
            return signedJWT;
        } catch (ParseException | JOSEException e) {
            throw new AppException(ErrorCode.INVALID_TOKEN);
        }
    }

    private String sign(JWTClaimsSet claimsSet) {
        SignedJWT signedJWT = new SignedJWT(new JWSHeader(JWSAlgorithm.HS256), claimsSet);
        try {
            signedJWT.sign(new MACSigner(signerKey.getBytes()));
            return signedJWT.serialize();
        } catch (JOSEException e) {
            throw new AppException(ErrorCode.UNCATEGORIZED);
        }
    }

    private String buildScope(User user) {
        StringJoiner joiner = new StringJoiner(" ");
        if (user.getRoles() != null) {
            user.getRoles().stream()
                    .map(Role::getName)
                    .map(name -> "ROLE_" + name)
                    .forEach(joiner::add);
        }
        return joiner.toString();
    }
}
