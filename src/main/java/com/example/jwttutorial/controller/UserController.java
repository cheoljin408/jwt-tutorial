package com.example.jwttutorial.controller;

import com.example.jwttutorial.dto.UserDto;
import com.example.jwttutorial.entity.Users;
import com.example.jwttutorial.service.UsersService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("api")
public class UserController {

    private final UsersService usersService;

    public UserController(UsersService usersService) {
        this.usersService = usersService;
    }

    @PostMapping("/signup")
    public ResponseEntity<Users> signup(@Valid @RequestBody UserDto userDto) {

        return ResponseEntity.ok(usersService.signUp(userDto));
    }

    @GetMapping("/user")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<Users> getMyUserInfo() {
        return ResponseEntity.ok(usersService.getMyUserWithAuthorities().get());
    }

    @GetMapping("/user/{username}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<Users> getUserInfo(@PathVariable String username) {
        return ResponseEntity.ok(usersService.getUserWithAuthorities(username).get());
    }
}
