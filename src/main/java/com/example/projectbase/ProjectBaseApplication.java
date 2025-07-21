package com.example.projectbase;

import com.example.projectbase.config.StorageProperties;
import com.example.projectbase.config.properties.AdminInfoProperties;
import com.example.projectbase.constant.RoleConstant;
import com.example.projectbase.domain.entity.ClassRegistration;
import com.example.projectbase.domain.model.Role;
import com.example.projectbase.domain.entity.User;
import com.example.projectbase.repository.RoleRepository;
import com.example.projectbase.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@EnableConfigurationProperties({AdminInfoProperties.class, StorageProperties.class})
@SpringBootApplication
@EnableCaching
public class ProjectBaseApplication {

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;

    public static void main(String[] args) {
        Environment env = SpringApplication.run(ProjectBaseApplication.class, args).getEnvironment();
        String appName = env.getProperty("spring.application.name");
        if (appName != null) {
            appName = appName.toUpperCase();
        }
        String port = env.getProperty("server.port");
        log.info("-------------------------START " + appName
                + " Application------------------------------");
        log.info("   Application         : " + appName);
        log.info("   Url swagger-ui      : http://localhost:" + port + "/swagger-ui.html");
        log.info("-------------------------START SUCCESS " + appName
                + " Application------------------------------");
    }

    @Bean
    CommandLineRunner init(AdminInfoProperties userInfo) {
        return args -> {
            //init role
            if (roleRepository.count() == 0) {
                roleRepository.save(new Role(null, RoleConstant.ADMIN, null));
                roleRepository.save(new Role(null, RoleConstant.USER, null));
                roleRepository.save(new Role(null, RoleConstant.LEADER, null));
            }
            //init admin
            if (userRepository.count() == 0) {
                User admin = User.builder().username(userInfo.getUsername())
                        .password(passwordEncoder.encode(userInfo.getPassword()))
                        .fullName(userInfo.getFullName())
                        .role(roleRepository.findByRoleName(RoleConstant.ADMIN))
                        .lastLogin(LocalDateTime.now())
                        .email(userInfo.getEmail())
                        .build();
                User user = User.builder().username("DinhvuongUser")
                        .password(passwordEncoder.encode("User"))
                        .fullName("Dinh Van Vuong")
                        .role(roleRepository.findByRoleName(RoleConstant.USER))
                        .lastLogin(LocalDateTime.now())
                        .email("Vuongdz20k5@gmail.com")
                        .build();

                userRepository.save(user);
                userRepository.save(admin);
            }
        };
    }

}
