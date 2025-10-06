package com.example.expense.config;

import java.util.Date;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
public class JWTUtil {

	@Value("${app.jwt.secret:U2VjcmV0S2V5VG9HZW5KV1RzMTIzNDU2}") // Base64-encoded secret
	private String secret;

	@Value("${app.jwt.expiration-ms:86400000}")
	private long jwtExpirationMs;

	private SecretKey getSigningKey() {
		byte[] keyBytes = Decoders.BASE64.decode(secret);
		return Keys.hmacShaKeyFor(keyBytes);
	}

	public String generateToken(String username) {
		return Jwts.builder().subject(username).issuedAt(new Date())
				.expiration(new Date(System.currentTimeMillis() + jwtExpirationMs)).signWith(getSigningKey())

				.compact();
	}

	public String extractUsername(String token) {
		return extractClaim(token, Claims::getSubject);
	}

	public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
		Claims claims = Jwts.parser().verifyWith(getSigningKey()).build().parseSignedClaims(token).getPayload();
		return claimsResolver.apply(claims);
	}

	public boolean isTokenValid(String token, String username) {
		try {
			final String extracted = extractUsername(token);
			return extracted.equals(username) && !isTokenExpired(token);
		} catch (Exception e) {
			return false;
		}
	}

	private boolean isTokenExpired(String token) {
		final Date expiration = extractClaim(token, Claims::getExpiration);
		return expiration.before(new Date());
	}
}
