package com.example.water.domain.member.controller;

import com.example.water.domain.member.dto.LoginRequestDto;
import com.example.water.domain.member.dto.MemberResponseDto;
import com.example.water.domain.member.service.MemberService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/login")
    public MemberResponseDto login(@RequestBody LoginRequestDto request, HttpSession session) {
        return memberService.login(request, session);
    }

    @PostMapping("/logout")
    public String logout(HttpSession session) {
        memberService.logout(session);
        return "success logout";
    }

    @GetMapping("/me")
    public MemberResponseDto me(HttpSession session) {
        return memberService.getLoginMember(session);
    }
}