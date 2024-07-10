package org.dreamorbit.employee.security;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.dreamorbit.employee.entity.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtHelper {
	

    //requirement :
    public static final long JWT_TOKEN_VALIDITY = 5 * 60 * 60;

    //    public static final long JWT_TOKEN_VALIDITY =  60;
    private String secret = "afafasfafafasfasfasfafacasdasfasxASFACASDFACASDFASFASFDAFASFASDAADSCSDFADCVSGCFVADXCcadwavfsfarvf";
    
    //Generate Token
    public String generateToken(UserDetails userDetails) {
    	
    	return Jwts.builder().setSubject(userDetails.getUsername())
    			.setIssuedAt(new Date(System.currentTimeMillis()))
    			.setExpiration(new Date(System.currentTimeMillis()+1000*60*24))
    			.signWith(getSignKey(), SignatureAlgorithm.HS256)
    			.compact();
    }
    
    
    public String generateRefreshToken(Map<String, Object> extraClaims, UserDetails userDetails) {
    	
    	return Jwts.builder().setClaims(extraClaims).setSubject(userDetails.getUsername())
    			.setIssuedAt(new Date(System.currentTimeMillis()))
    			.setExpiration(new Date(System.currentTimeMillis()+ 604800000))
    			.signWith(getSignKey(), SignatureAlgorithm.HS256)
    			.compact();
    }
    
    
    public String extractUserName(String token) {
    	return extractClaims(token, Claims::getSubject);
    }

	private Key getSignKey() {
		byte[] key = Decoders.BASE64.decode(secret);
		return Keys.hmacShaKeyFor(key);
	}
	
	private <T> T extractClaims(String token, Function<Claims, T> claimsResolvers) {
		final Claims claims = extractAllClaims(token);
		return claimsResolvers.apply(claims);
	}

	private Claims extractAllClaims(String token) {
		return Jwts.parserBuilder().setSigningKey(getSignKey()).build().parseClaimsJws(token).getBody();
	}
	
	public boolean isTokenValid(String token, UserDetails userDetails) {
		final String username = extractUserName(token);
		return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
	}

	private boolean isTokenExpired(String token) {
		
		return extractClaims(token, Claims::getExpiration).before(new Date());
	}

	

}
