package com.example.water.domain.member.controller;

import com.example.water.domain.member.dto.LoginRequestDto;
import com.example.water.domain.member.dto.MemberResponseDto;
import com.example.water.domain.member.dto.SignupRequestDto;
import com.example.water.domain.member.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Member", description = "회원 관련 API")
@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @Operation(summary = "회원가입")
    @PostMapping("/signup")
    public Long signup(@Valid @RequestBody SignupRequestDto request) {
        return memberService.signup(request);
    }

    @Operation(summary = "로그인")
    @PostMapping("/login")
    public MemberResponseDto login(@RequestBody LoginRequestDto request, HttpSession session) {
        return memberService.login(request, session);
    }

    @Operation(summary = "로그아웃")
    @PostMapping("/logout")
    public String logout(HttpSession session) {
        memberService.logout(session);
        return "success logout";
    }

    @Operation(summary = "로그인 사용자 확인")
    @GetMapping("/me")
    public MemberResponseDto me(HttpSession session) {
        return memberService.getLoginMember(session);
    }
}