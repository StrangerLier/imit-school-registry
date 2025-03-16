package omsu.mim.imit.school.registry.config.security;

import lombok.Getter;
import org.springframework.context.annotation.Configuration;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;

@Getter
@Configuration
public class RsaKeys {

    private final KeyPair pair;

    public RsaKeys() throws NoSuchAlgorithmException {
        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
        generator.initialize(2048);
        pair = generator.generateKeyPair();
    }
}

