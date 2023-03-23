package com.example.userservice.controller;

import com.example.userservice.dto.UserDTO;
import com.example.userservice.jpa.UserEntity;
import com.example.userservice.service.UserService;
import com.example.userservice.vo.RequestUser;
import com.example.userservice.vo.ResponseUser;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    //회원가입
    @PostMapping("/users")
    public ResponseEntity<ResponseUser> createUser(@RequestBody RequestUser user) {

        //회원가입하고 넘어온 정보 RequestUser 객체

        //변환(매핑)하기 위한 설정 / UserDTO로 리턴하기 위해
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        //RequestUser 객체 -> UserDTO 객체로 변환해서 서비스.createUser
        UserDTO userDTO = mapper.map(user, UserDTO.class);
        userService.createUser(userDTO);

        //다시 UserDTO 객체 -> RequestUser 객체로 변환해서 리턴
        ResponseUser responseUser = mapper.map(userDTO, ResponseUser.class);

        return ResponseEntity.status(HttpStatus.CREATED).body(responseUser);
    }

    //회원 목록 출력
    @GetMapping("/user2/list")
    public ResponseEntity<List<ResponseUser>> getUsers() {
        Iterable<UserEntity> userList = userService.getUserByAll();

        List<ResponseUser> result = new ArrayList<>();
        //userList의 값을 하나씩 받아와서 v(UserEntity)
        //ResponseUser 객체로 변환해서 result에 추가
        userList.forEach(v -> {
            result.add(new ModelMapper().map(v, ResponseUser.class));
        });

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @GetMapping("/hello/a")
    public String hello() {
        return "hello";
    }
}
