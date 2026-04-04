package com.example.water.init;

import com.example.water.domain.member.entity.Member;
import com.example.water.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MemberTest implements CommandLineRunner {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (memberRepository.findByLoginId("test").isEmpty()) {
            Member member = new Member();
            member.setLoginId("test");
            member.setHashedPassword(passwordEncoder.encode("1234"));
            member.setNickname("testNickName");

            memberRepository.save(member);
        }
    }
}