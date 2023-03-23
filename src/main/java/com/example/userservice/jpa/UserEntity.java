package com.example.userservice.jpa;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "users2")
public class UserEntity {
    //도메인이 아닌 DB 테이블로 생각하기/ 직접사용X

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    //userId NotNull,Unique 설정
    private String userId;
    @Column(nullable = false, unique = true, length = 50)
    //실제 id로 사용 = username
    private String email;
    @Column(nullable = false, unique = true)
    //password
    private String encrypedPwd;
    @Column(nullable = false, length = 50)
    private String name;
}
