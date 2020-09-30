package com.course.eugen.controller;


import com.course.eugen.dto.LoginDTO;
import com.course.eugen.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigInteger;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class LoginController {

    private final UserRepository userRepository;

    @PostMapping("/login")
    public BigInteger login(@RequestBody LoginDTO loginDTO) {
        var user = userRepository.findByMailAndPassword(loginDTO.getMail(), loginDTO.getPassword());
        if (user.isPresent()) {
            return user.get().getId();
        }
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User is not registered!");
    }
}
