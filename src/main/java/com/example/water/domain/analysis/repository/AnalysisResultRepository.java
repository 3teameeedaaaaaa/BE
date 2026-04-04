package com.example.water.domain.analysis.repository;

import com.example.water.domain.analysis.entity.AnalysisResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface AnalysisResultRepository extends JpaRepository<AnalysisResult, Long> {

    Optional<AnalysisResult> findBySessionId(Long sessionId);

    @Query("""
        select ar
        from AnalysisResult ar
        join fetch ar.session s
        left join fetch ar.distortion d
        where s.member.id = :memberId
        order by ar.createdAt desc
    """)
    List<AnalysisResult> findAllByMemberId(Long memberId);

    @Query("""
        select ar
        from AnalysisResult ar
        join fetch ar.session s
        left join fetch ar.distortion d
        where s.member.id = :memberId
        order by ar.createdAt desc
    """)
    List<AnalysisResult> findRecentByMemberId(Long memberId);
}