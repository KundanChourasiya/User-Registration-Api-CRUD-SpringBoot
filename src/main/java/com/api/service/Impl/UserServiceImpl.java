package com.api.service.Impl;

import com.api.GlobalException.ResourceNotFoundError;
import com.api.entity.User;
import com.api.payload.UserDto;
import com.api.repository.UserRepository;
import com.api.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository repository;

    @Override
    public UserDto createUser(User user) {
        if (repository.existsByEmail(user.getEmail()))
            throw new  IllegalArgumentException("User email already exist.");
        if (repository.existsByMobile(user.getMobile()))
            throw new  IllegalArgumentException("User Mobile no. already exist.");
        User saveUser = repository.save(user);
        UserDto dto = mapToDto(saveUser);
        return dto;
    }

    @Override
    public Page<UserDto> getAllUser(Pageable pageable) {
        Page<User> userPage = repository.findAll(pageable);
        List<UserDto> userDtoList = userPage.getContent().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
        return new PageImpl<>(userDtoList, pageable, userPage.getTotalPages());
    }

    @Override
    public UserDto updateUser(Long id, UserDto dto) {
        User user = repository.findById(id).orElseThrow(() -> new ResourceNotFoundError("Record Not found"));
        user.setFullName(dto.getFullName());
        user.setEmail(dto.getEmail());
        user.setMobile(dto.getMobile());
        user.setGender(dto.getGender());
        user.setAge(dto.getAge());
        repository.save(user);

        UserDto userdto = mapToDto(user);
        return userdto;
    }

    @Override
    public UserDto getUserById(Long id) {
        User user = repository.findById(id).orElseThrow(() -> new ResourceNotFoundError("Record not found"));
        UserDto dto = mapToDto(user);
        return dto;
    }

    @Override
    public String deleteUserById(Long id) {
        User user = repository.findById(id).orElseThrow(() -> new ResourceNotFoundError("Record Not found"));
        repository.delete(user);
        return "User Delete Successfully";
    }

    @Override
    public List<UserDto> searchByName(String name) {
        List<User> byFullNameContainingIgnoreCase = repository.findByFullNameContainingIgnoreCase(name);
        List<UserDto> userDtoList = byFullNameContainingIgnoreCase.stream().map(u -> mapToDto(u)).collect(Collectors.toList());
        return userDtoList;
    }

    // dto to entity
    private UserDto mapToDto(User user){
        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setFullName(user.getFullName());
        dto.setEmail(user.getEmail());
        dto.setMobile(user.getMobile());
        dto.setAge(user.getAge());
        dto.setGender(user.getGender());
        return dto;
    }

    // entity to dto
    private User mapToEntity(UserDto dto){
        User user = new User();
        user.setId(dto.getId());
        user.setFullName(dto.getFullName());
        user.setEmail(dto.getEmail());
        user.setMobile(dto.getMobile());
        user.setAge(dto.getAge());
        user.setGender(dto.getGender());
        return user;
    }

}
