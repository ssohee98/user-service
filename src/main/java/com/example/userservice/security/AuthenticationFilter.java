package com.example.userservice.security;

import com.example.userservice.vo.RequestLogin;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    public AuthenticationFilter(AuthenticationManager authenticationManager){
        super.setAuthenticationManager(authenticationManager);
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

    }
}
