package br.com.ifba.infrastructure.config;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Base64;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class JwtService {
    private final String secret;
    private final long expirationMs;
    private final Base64.Encoder encoder = Base64.getUrlEncoder().withoutPadding();

    public JwtService(@Value("${app.jwt.secret}") String secret, @Value("${app.jwt.expiration-ms}") long expirationMs) {
        if (secret.getBytes(StandardCharsets.UTF_8).length < 32) throw new IllegalArgumentException("JWT secret deve ter pelo menos 32 bytes");
        this.secret = secret; this.expirationMs = expirationMs;
    }
    public String issue(String email) {
        long expiry = Instant.now().plusMillis(expirationMs).getEpochSecond();
        String head = encoder.encodeToString("{\"alg\":\"HS256\",\"typ\":\"JWT\"}".getBytes(StandardCharsets.UTF_8));
        String payload = encoder.encodeToString(("{\"sub\":\"" + email + "\",\"exp\":" + expiry + "}").getBytes(StandardCharsets.UTF_8));
        String unsigned = head + "." + payload;
        return unsigned + "." + encoder.encodeToString(sign(unsigned));
    }
    public String subject(String token) {
        try {
            String[] parts = token.split("\\.");
            if (parts.length != 3) return null;
            String header = new String(Base64.getUrlDecoder().decode(parts[0]), StandardCharsets.UTF_8);
            if (!header.contains("\"alg\":\"HS256\"") || !java.security.MessageDigest.isEqual(sign(parts[0] + "." + parts[1]), Base64.getUrlDecoder().decode(parts[2]))) return null;
            String json = new String(Base64.getUrlDecoder().decode(parts[1]), StandardCharsets.UTF_8);
            java.util.regex.Matcher sub = java.util.regex.Pattern.compile("\\\"sub\\\":\\\"([^\\\"]+)\\\"").matcher(json);
            java.util.regex.Matcher exp = java.util.regex.Pattern.compile("\\\"exp\\\":(\\d+)").matcher(json);
            if (!sub.find() || !exp.find() || Instant.now().getEpochSecond() >= Long.parseLong(exp.group(1))) return null;
            return sub.group(1);
        } catch (Exception ignored) { return null; }
    }
    private byte[] sign(String value) {
        try { Mac mac = Mac.getInstance("HmacSHA256"); mac.init(new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256")); return mac.doFinal(value.getBytes(StandardCharsets.UTF_8)); }
        catch (Exception e) { throw new IllegalStateException(e); }
    }
}
