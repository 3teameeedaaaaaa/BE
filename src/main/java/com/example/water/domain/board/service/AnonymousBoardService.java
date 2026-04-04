package com.example.water.domain.board.service;

import com.example.water.domain.analysis.entity.AnalysisResult;
import com.example.water.domain.analysis.repository.AnalysisResultRepository;
import com.example.water.domain.board.dto.BoardCreateRequestDto;
import com.example.water.domain.board.dto.BoardResponseDto;
import com.example.water.domain.board.entity.AnonymousBoard;
import com.example.water.domain.board.repository.AnonymousBoardRepository;
import com.example.water.domain.member.dto.SessionMemberDto;
import com.example.water.domain.member.entity.Member;
import com.example.water.domain.member.repository.MemberRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AnonymousBoardService {

    private final AnonymousBoardRepository anonymousBoardRepository;
    private final AnalysisResultRepository analysisResultRepository;
    private final MemberRepository memberRepository;

    public Long createBoard(BoardCreateRequestDto request, HttpSession httpSession) {
        SessionMemberDto loginMember = (SessionMemberDto) httpSession.getAttribute("loginMember");
        if (loginMember == null) {
            throw new IllegalArgumentException("로그인된 사용자가 없습니다.");
        }

        Member member = memberRepository.findById(loginMember.getId())
                .orElseThrow(() -> new IllegalArgumentException("회원이 존재하지 않습니다."));

        AnalysisResult result = analysisResultRepository.findById(request.getResultId())
                .orElseThrow(() -> new IllegalArgumentException("분석 결과가 존재하지 않습니다."));

        if (!result.getSession().getMember().getId().equals(loginMember.getId())) {
            throw new IllegalArgumentException("본인 결과만 공유할 수 있습니다.");
        }

        AnonymousBoard board = new AnonymousBoard();
        board.setMember(member);
        board.setResult(result);
        board.setTitle(request.getTitle());
        board.setContent(request.getContent());

        anonymousBoardRepository.save(board);
        return board.getId();
    }

    @Transactional(readOnly = true)
    public List<BoardResponseDto> getBoards() {
        return anonymousBoardRepository.findAllByOrderByCreatedAtDesc().stream()
                .map(board -> BoardResponseDto.builder()
                        .boardId(board.getId())
                        .resultId(board.getResult().getId())
                        .title(board.getTitle())
                        .content(board.getContent())
                        .likeCount(board.getLikeCount())
                        .distortionTypeName(
                                board.getResult().getDistortion() != null
                                        ? board.getResult().getDistortion().getTypeName()
                                        : null
                        )
                        .reflectionSummary(board.getResult().getReflectionSummary())
                        .build())
                .toList();
    }
}