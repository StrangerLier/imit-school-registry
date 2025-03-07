package omsu.mim.imit.school.registry.config.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtProvider {

    private final SecurityProperties settings;
    private final RsaKeys keys;

    public String generateToken(String login) {
        Date date = Date.from(
                LocalDate.now()
                        .plusDays(settings.getJwtExpireInDays())
                        .atStartOfDay(ZoneId.systemDefault())
                        .toInstant()
        );

        return Jwts.builder()
                .setSubject(login)
                .setExpiration(date)
                .signWith(keys.getPair().getPrivate())
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(keys.getPair().getPrivate())
                    .build()
                    .parse(token);
            return true;
        } catch (Exception e) {
            log.warn("invalid token");
            return false;
        }
    }

    public String getUsernameFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(keys.getPair().getPrivate())
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }

}

