package org.example.springboot.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.util.Date;

public class TokenUtils {

    // Token的过期时间，单位为毫秒（这里设置为1天）
    private static final long EXPIRATION_TIME = 24 * 60 * 60 * 1000;
    // 使用更安全的随机密钥，确保长度和复杂性
    private static final SecretKey SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS512); // 必须是64字符的字符串

    /**
     * 生成Token
     * @param userId 用户ID
     * @return 生成的Token字符串
     */
    public static String createToken(String userId) {
        return Jwts.builder()
                .setSubject(userId)  // 设置主题（用户ID）
                .setIssuedAt(new Date())  // 设置生成时间
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))  // 设置过期时间
                .signWith(SECRET_KEY)  // 使用密钥和HS512算法签名
                .compact();
    }

    /**
     * 验证Token并获取其中的声明信息
     * @param token 待验证的Token
     * @return Token中的声明信息
     */
    public static Claims parseToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * 检查Token是否过期
     * @param token 待验证的Token
     * @return true表示过期，false表示未过期
     */
    public static boolean isTokenExpired(String token) {
        Claims claims = parseToken(token);
        return claims.getExpiration().before(new Date());
    }
}
