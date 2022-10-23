package com.example.jwttutorial.service;

import com.example.jwttutorial.dto.UserDto;
import com.example.jwttutorial.entity.Authority;
import com.example.jwttutorial.entity.Users;
import com.example.jwttutorial.repository.UsersRepository;
import com.example.jwttutorial.util.SecurityUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Optional;

@Service
public class UsersService {

    private final UsersRepository usersRepository;

    private final PasswordEncoder passwordEncoder;

    public UsersService(UsersRepository usersRepository, PasswordEncoder passwordEncoder) {
        this.usersRepository = usersRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public Users signUp(UserDto userDto) {

        if (usersRepository.findOneWithAuthoritiesByUsername(userDto.getUsername()).orElse(null) != null) {
            throw new RuntimeException("이미 가입되어 있는 유저입니다.");
        }

        Authority authority = Authority.builder()
                .authorityName("ROLE_USER")
                .build();

        Users user = Users.builder()
                .username(userDto.getUsername())
                .password(passwordEncoder.encode(userDto.getPassword()))
                .nickname(userDto.getNickname())
                .activated(true)
                .authorities(Collections.singleton(authority))
                .build();

        return usersRepository.save(user);
    }

    @Transactional(readOnly = true)
    public Optional<Users> getUserWithAuthorities(String username) {
        return usersRepository.findOneWithAuthoritiesByUsername(username);
    }

    @Transactional(readOnly = true)
    public Optional<Users> getMyUserWithAuthorities() {
        return SecurityUtil.getCurrentUsername().flatMap(usersRepository::findOneWithAuthoritiesByUsername);
    }
}
