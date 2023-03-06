package at.collew.account.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.security.SecureRandom;

/**
 * Configurationclass for the passwordencoder
 * @author Patrick Stadt
 * @version 1
 */
@Configuration
public class PasswordConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        //return PasswordEncoderFactories.createDelegatingPasswordEncoder(); <- does not work because PasswordEncoder mapped id is null
        //The DelegatingPasswordEncoder allows to change encodings without problems to past encodings | default bcrypt
        return new BCryptPasswordEncoder(11);
    }
}
