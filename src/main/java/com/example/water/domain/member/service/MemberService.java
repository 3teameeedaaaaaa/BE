package com.example.water.domain.member.service;

import com.example.water.domain.member.dto.LoginRequestDto;
import com.example.water.domain.member.dto.MemberResponseDto;
import com.example.water.domain.member.dto.SessionMemberDto;
import com.example.water.domain.member.entity.Member;
import com.example.water.domain.member.repository.MemberRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public MemberResponseDto login(LoginRequestDto request, HttpSession session) {
        Member member = memberRepository.findByLoginId(request.getLoginId())
                .orElseThrow(() -> new IllegalArgumentException("아이디 또는 비밀번호가 올바르지 않습니다."));

        if (!passwordEncoder.matches(request.getPassword(), member.getHashedPassword())) {
            throw new IllegalArgumentException("아이디 또는 비밀번호가 올바르지 않습니다.");
        }

        SessionMemberDto sessionMember = new SessionMemberDto(
                member.getId(),
                member.getLoginId(),
                member.getNickname()
        );

        session.setAttribute("loginMember", sessionMember);

        return new MemberResponseDto(
                member.getId(),
                member.getLoginId(),
                member.getNickname()
        );
    }

    public void logout(HttpSession session) {
        session.invalidate();
    }

    public MemberResponseDto getLoginMember(HttpSession session) {
        SessionMemberDto sessionMember = (SessionMemberDto) session.getAttribute("loginMember");

        if (sessionMember == null) {
            throw new IllegalArgumentException("로그인된 사용자가 없습니다.");
        }

        return new MemberResponseDto(
                sessionMember.getId(),
                sessionMember.getLoginId(),
                sessionMember.getNickname()
        );
    }
}