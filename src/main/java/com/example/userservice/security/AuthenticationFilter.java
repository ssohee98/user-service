package com.example.userservice.security;

import com.example.userservice.dto.UserDTO;
import com.example.userservice.service.UserService;
import com.example.userservice.vo.RequestLogin;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private UserService userService;
    private Environment env;


    public AuthenticationFilter(AuthenticationManager authenticationManager,
                                UserService userService, Environment env){

        super.setAuthenticationManager(authenticationManager);
        this.userService = userService;
        this.env = env;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {

        //인증되기전 사전작업 / 로그인으로 들어오는 정보를 RequestLogin 객체로
        try {
            RequestLogin creds = new ObjectMapper().readValue(request.getInputStream(), RequestLogin.class);

            //받아온 로그인정보를(email,password) 토큰으로 처리해서 Authentication 형태로 리턴
            return getAuthenticationManager().authenticate(
                    new UsernamePasswordAuthenticationToken(
                            creds.getEmail(),
                            creds.getPassword(),
                            new ArrayList<>()
                    )
            );

        }catch (IOException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {

        //인증성공 후 호출됨/ 사후작업

        //로그인한 정보를 getPrincipal()로 가져와서 User객체에 넣기
        String userName = ((User)authResult.getPrincipal()).getUsername();
        //System.out.println("userName: " +userName); //id(email)

        //userName으로 id를 가져와서 토큰 만들고 싶어서
        UserDTO userDetail = userService.getUserDetailByEmail(userName);

        //JWT 토큰 만들어서 클라이언트에게 전달
        String token = Jwts.builder()
                .setSubject(userDetail.getUserId())
                .setExpiration(new Date(System.currentTimeMillis() +
                                            Long.parseLong(env.getProperty("token.expiration_time"))))  //만료시간:token 날짜에서 +하루
                .signWith(SignatureAlgorithm.HS512, env.getProperty("token.secret"))
                .compact();

        //response에 addHeader로 "token"이란 이름으로 token / "userId"란 이름으로 id값 전달
        response.addHeader("token", token);
        response.addHeader("userId", userDetail.getUserId());
    }
}
