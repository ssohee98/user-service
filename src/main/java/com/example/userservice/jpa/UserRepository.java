package com.example.userservice.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    //각 칼럼으로 해당 엔티티 찾기
    UserEntity findByUserId(String userId);
    UserEntity findByEmail(String username);




}
