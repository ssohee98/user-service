package com.example.userservice.service;

import com.example.userservice.dto.UserDTO;
import com.example.userservice.jpa.UserEntity;
import com.example.userservice.jpa.UserRepository;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.modelmapper.spi.MatchingStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    //실제 인증을 처리하는 메서드 **
    //로그인한 id(email) = username
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByEmail(username);

        if(userEntity == null) {
            throw new UsernameNotFoundException(username + ":not found");
        }

        //DB에 저장되어있는 id(email), pw를 비교해라 => 자동으로 인증 처리
        return new User(userEntity.getEmail(), userEntity.getEncrypedPwd(),
                        true, true, true, true, new ArrayList<>());
    }

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
