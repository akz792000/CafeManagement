package org.cafe.management.config;

import org.cafe.management.domain.UserEntity;
import org.cafe.management.enums.RoleType;
import org.cafe.management.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * @author Ali Karimizandi
 * @since 2021
 */
@Configuration
public class InitializeConfiguration {

    @Bean
    CommandLineRunner init(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        return args -> {
            if (userRepository.findByUsername("User") == null) {

                // user entity
                UserEntity userEntity = new UserEntity();
                userEntity.setUsername("User");
                userEntity.setPassword(passwordEncoder.encode("Welcome555"));
                userEntity.setRoleType(RoleType.ROLE_MANAGER);

                // save
                userRepository.save(userEntity);
            }
        };
    }

}
