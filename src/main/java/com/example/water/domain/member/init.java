package com.example.water.domain.member;

import com.example.water.domain.member.entity.Member;
import com.example.water.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class init implements CommandLineRunner {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (memberRepository.findByLoginId("test").isEmpty()) {
            Member member = new Member();
            member.setLoginId("test");
            member.setHashedPassword(passwordEncoder.encode("1234"));
            member.setNickname("상ㅗ");

            memberRepository.save(member);
        }
    }
}