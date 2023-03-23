package com.example.userservice.service;

import com.example.userservice.dto.UserDTO;
import com.example.userservice.jpa.UserEntity;
import org.springframework.stereotype.Service;


public interface UserService {
    UserDTO createUser(UserDTO userDTO);
    UserDTO getUserByUserId(String userId);
    Iterable<UserEntity> getUserByAll();

}
