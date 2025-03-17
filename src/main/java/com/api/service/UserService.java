package com.api.service;

import com.api.entity.User;
import com.api.payload.UserDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserService {

    public UserDto createUser(User user);

    public Page<UserDto> getAllUser(Pageable pageable);

    UserDto updateUser(Long id, UserDto dto);

    UserDto getUserById(Long id);

    String deleteUserById(Long id);

    List<UserDto> searchByName(String name);

}
