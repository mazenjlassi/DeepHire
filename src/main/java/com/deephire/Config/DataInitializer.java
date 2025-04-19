package com.deephire.Config;


import com.deephire.Enums.ERole;
import com.deephire.Models.Role;
import com.deephire.Repositories.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner initRoles(RoleRepository roleRepository) {
        return args -> {
            for (ERole roleEnum : ERole.values()) {
                if (!roleRepository.existsByName(roleEnum)) {
                    Role role = new Role();
                    role.setName(roleEnum);
                    roleRepository.save(role);
                }
            }
        };
    }
}

