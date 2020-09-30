package com.course.eugen.service;

import com.course.eugen.domain.User;
import com.course.eugen.domain.enums.RolesEnum;
import com.course.eugen.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
@Slf4j
@RequiredArgsConstructor
@DependsOn(value = {"UserService"})
public class OnStartUpService {

    private final UserRepository userRepository;

    @PostConstruct
    public void setUp() {
        var admin = userRepository.save(User.builder()
                .firstName("admin")
                .lastName("admin")
                .password("admin")
                .mail("admin@admin.ru")
                .globalRole(RolesEnum.ADMIN)
                .build());
        log.info("Admin created with id {}", admin.getId());
    }
}
