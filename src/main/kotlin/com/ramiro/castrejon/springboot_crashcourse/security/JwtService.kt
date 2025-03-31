package com.ramiro.castrejon.springboot_crashcourse.security

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.util.Base64
import java.util.Date

@Service
class JwtService(
    @Value("JWT_SECREt_BASE64") private val jwtSecret: String
) {

    private val secret = Keys.hmacShaKeyFor(Base64.getDecoder().decode(jwtSecret))
    private val accessTokenValidMs = 15L * 60L * 1000L
    val refreshTokenValidMs = 30L * 24L * 60L * 60L * 1000L

    private fun generateToken(
        userId: String,
        type: String,
        expiry: Long
    ): String {
        val now = Date()
        val expiryDate = Date(now.time + expiry)
        return Jwts.builder()
            .subject(userId)
            .claim("type", type)
            .issuedAt(now)
            .expiration(expiryDate)
            .signWith(secret, Jwts.SIG.HS256).compact()
    }

    fun generateAccessToken(
        userId: String
    ): String {
        return generateToken(userId, "access", accessTokenValidMs)
    }

    fun generateRefreshToken(
        userId: String
    ): String {
        return generateToken(userId, "refresh", refreshTokenValidMs)
    }

    fun validateAccessToken(token: String): Boolean {
        val claims = parseAllClaims(token) ?: return false
        val tokenType = claims["type"] as? String ?: return false
        return tokenType == "access"
    }

    fun validateRefreshToken(token: String): Boolean {
        val claims = parseAllClaims(token) ?: return false
        val tokenType = claims["type"] as? String ?: return false
        return tokenType == "refresh"
    }

    fun getUserId(token: String): String {
        val rawToken = if(token.startsWith("Bearer ")){
            token.removePrefix("Bearer ")
        } else token

        val claims = parseAllClaims(rawToken) ?: throw IllegalArgumentException("Invalid JWT token")

        return claims.subject
    }

    private fun parseAllClaims(token: String): Claims? {
        return try {
            Jwts.parser().verifyWith(secret)
                .build().parseSignedClaims(token).payload
        } catch (e: Exception) {
            null
        }
    }
}