package com.api.controller;

import com.api.entity.User;
import com.api.payload.UserDto;
import com.api.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService service;

    // url pattern: localhost:8081/api/user
    @PostMapping
    public ResponseEntity<?> CreateUser(@Valid @RequestBody User user) {
        try {
            UserDto dto = service.createUser(user);
            return new ResponseEntity<>(dto, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());

        }
    }

    // url pattern: localhost:8081/api/user
    // url pattern: localhost:8081/api/user?page=0
    // url pattern: http://localhost:8081/api/user?page=0&size=2
    @GetMapping
    public ResponseEntity<Page<UserDto>> getAllStates(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "2") int size) {
        Page<UserDto> states = service.getAllUser(PageRequest.of(page, size));
        return ResponseEntity.ok(states);
    }

    // url pattern: localhost:8081/api/user/8
    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody UserDto dto) {

        UserDto updatedto = service.updateUser(id, dto);
        return new ResponseEntity<>(updatedto, HttpStatus.CREATED);
    }

    // url pattern: localhost:8081/api/user/8
    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id){
        UserDto userById = service.getUserById(id);
        return new ResponseEntity<>(userById, HttpStatus.FOUND);
    }

    // url pattern: localhost:8081/api/user/8
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUserById(@PathVariable Long id){
        String msg = service.deleteUserById(id);
        return new ResponseEntity<>(msg, HttpStatus.OK);
    }

    // url pattern: localhost:8081/api/user/search?name=s
    // url pattern: localhost:8081/api/user/search?name=abc
    @GetMapping("/search")
    public ResponseEntity<List<UserDto>> searchByName (@RequestParam(required = false) String name){
        List<UserDto> userDtos = service.searchByName(name);
        if (userDtos.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return new ResponseEntity<>(userDtos, HttpStatus.FOUND);
    }
}