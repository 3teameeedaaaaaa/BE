package com.example.water.domain.board.repository;

import com.example.water.domain.board.entity.AnonymousBoard;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AnonymousBoardRepository extends JpaRepository<AnonymousBoard, Long> {
    List<AnonymousBoard> findAllByOrderByCreatedAtDesc();
}