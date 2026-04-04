package com.example.water.domain.analysis.repository;

import com.example.water.domain.analysis.entity.CognitiveDistortion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CognitiveDistortionRepository extends JpaRepository<CognitiveDistortion, Long> {
    Optional<CognitiveDistortion> findByTag(String tag);
}