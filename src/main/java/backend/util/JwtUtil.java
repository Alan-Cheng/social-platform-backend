package backend.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import backend.entity.Role;
import backend.entity.User;
import backend.repository.UserRepository;

@Component
public class JwtUtil {

    private static final SecretKey SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS512);
    private static final long EXPIRATION_TIME = 1000 * 60 * 60; // 設置過期時間 1 小時

    @Autowired
    private UserRepository userRepository;


    // 取得使用者的角色
    private List<String> getUserRoles(String username) {
        User user = userRepository.findByUserPhone(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        return user.getRoles().stream()
                   .map(Role::getName)
                   .collect(Collectors.toList());
    }

    public String generateToken(String username) {
        List<String> roles = getUserRoles(username);  // 從資料庫中獲取角色

        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .claim("roles", roles) // 將角色列表放入 Token 的 claim 部分
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                .compact();
    }

    public static Claims extractClaims(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
    

    public static String extractUsername(String token) {
        return extractClaims(token).getSubject();
    }

    public static List<String> extractRoles(String token) {
        return (List<String>) extractClaims(token).get("roles");
    }

    public static boolean isTokenExpired(String token) {
        return extractClaims(token).getExpiration().before(new Date());
    }

    public boolean validateToken(String token, String username) {
    return (username.equals(username) && !isTokenExpired(token));
    }
}
