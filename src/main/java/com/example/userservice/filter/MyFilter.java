package com.example.userservice.filter;

import io.jsonwebtoken.Jwts;
import org.springframework.core.env.Environment;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class MyFilter implements Filter {
    private Environment env;

    public MyFilter(Environment env) {
        this.env = env;
    }

    private void onError(HttpServletResponse response, String httpStatus) throws IOException {
        response.addHeader("error", httpStatus);
        response.sendError(HttpServletResponse.SC_FORBIDDEN, httpStatus); //400 에러
    }

    @Override
    public void doFilter(ServletRequest request2, ServletResponse response2, FilterChain chain) throws IOException, ServletException {
        System.out.println("필터 호출");

        HttpServletRequest request = (HttpServletRequest) request2;
        HttpServletResponse response = (HttpServletResponse) response2;

        //header에 권한 정보가 없다면
        if(request.getHeader("AUTHORIZATION") == null) {
            onError(response, "UNAUTHORIZATION");
        } else {
            String authorizationHeader = request.getHeader("AUTHORIZATION");
            System.out.println(authorizationHeader);

            //Bearer token이 있다면 지워주기
            String jwt = authorizationHeader.replace("Bearer", "");

            if(!isJwtValid(jwt)) {
                onError(response, "UNAUTHORIZATION22");
            }
        }

        //필터를 체인에 연결해주기
        chain.doFilter(request2, response2);
    }

    private boolean isJwtValid(String jwt) {
        //처음에 리턴값은 true로 지정
        boolean returnValue = true;

        String subject = null;
        try {   //해당 id의 subject값 가져오기
            subject = Jwts.parser().setSigningKey(env.getProperty("token.secret"))
                    .parseClaimsJws(jwt).getBody().getSubject();
        } catch(Exception e) {
            returnValue = false;
        }

        //토큰값을 제대로 가져오지 못했거나, 토큰값이 없으면
        if (subject == null || subject.isEmpty()) {
            returnValue = false;
        }

        return returnValue;
    }
}
