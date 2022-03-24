package com.icia.member.interceptor;

import com.icia.member.common.SessionConst;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class LoginCheckInterceptor implements HandlerInterceptor {
    // 인터셉터 : 컨트롤러로 가기 전에 미리 확인해주는 역할.
    // 방역패스 느낌.. ㅎ
    // + 추가(220112)----
    // 인터셉터에서 사용자가 요청한 주소값을 일단 넣기로 하자.(로그인 후 사용자가 원하는 페이지로 이동하게 하기 위해)
  // ---- 여기까지 추가---
    // @Override : 부모가 가진 메서드를 재정의
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        // 사용자가 요청한 주소값
        String requestURL = request.getRequestURI();
        System.out.println("requestURL = " + requestURL);

        // 세션 가져옴
        HttpSession session = request.getSession();
        // HttpSession도 Request객체에 들어있음.
        // 서블릿이 있어야 웹 서버 자체를 구축할 수 있음.
        // 서블릿을 쉽게 쓰도록 하는게 스프링.
        // 서블릿을 쉽게 쓰도록 나온게 springFramework, 더 쉽게 나온게 SpringBoot

        //세션에 로그인 정보가 있는지 확인
        if(session.getAttribute(SessionConst.LOGIN_EMAIL) == null){
            // 미로그인 상태
            // 로그인을 하지 않은 경우 바로 로그인페이지로 보내고 로그인을 하면 요청한 화면을 보여줌.
            //세션에 요청한 주소값 담기
            session.setAttribute("redirectURL", requestURL);
            response.sendRedirect("/member/login");
//            response.sendRedirect("/member/login?redirectURL=" + requestURI);
            // redirect는 response 객체가 가지고 있음.
            return false;


        } else{
            // 로그인 상태
            return true;
        }



    }


}
