package com.example.userservice.service;

import com.example.userservice.dto.UserDTO;
import com.example.userservice.jpa.UserEntity;
import com.example.userservice.jpa.UserRepository;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.modelmapper.spi.MatchingStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public UserDTO createUser(UserDTO userDTO) {
        userDTO.setUserId(UUID.randomUUID().toString());

        //변환(매핑)하기 위한 설정
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        //UserEntity에 userDTO를 복사해 넣기
        UserEntity userEntity = mapper.map(userDTO, UserEntity.class);
        //userDTO의 pw를 암호화해서 userEntity에 넣기
        userEntity.setEncrypedPwd(passwordEncoder.encode(userDTO.getPwd()));

        //userEntity DB에 저장
        userRepository.save(userEntity);

        //userDTO에 UserEntity를 복사해 넣고 리턴
        UserDTO returnUserDTO = mapper.map(userEntity, UserDTO.class);
        return returnUserDTO;
    }

    @Override
    public UserDTO getUserByUserId(String userId) {
        return null;
    }

    @Override
    public Iterable<UserEntity> getUserByAll() {
        return null;
    }
}
