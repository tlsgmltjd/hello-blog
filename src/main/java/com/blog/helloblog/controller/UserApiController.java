package com.blog.helloblog.controller;

import com.blog.helloblog.dto.request.AddUserRequest;
import com.blog.helloblog.service.UserSerivce;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@RequiredArgsConstructor
@Controller
public class UserApiController {

    private final UserSerivce userSerivce;

    @PostMapping("/user")
    public String signup(AddUserRequest request) {
        userSerivce.save(request); // 회원가입 메서드 호출
        return "redirect:/login"; // 회원가입이 완료되면 로그인 페이지로 이동
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        new SecurityContextLogoutHandler().logout(request, response,
                SecurityContextHolder.getContext().getAuthentication());
        // 로그아웃을 담당하는 핸들러인 SecurityContextLogoutHandler의 logout() 메서드를 호출하여 로그아웃

        return "redirect:/login";
    }
}
