package com.example.water.domain.board.controller;

import com.example.water.domain.board.dto.BoardCreateRequestDto;
import com.example.water.domain.board.dto.BoardResponseDto;
import com.example.water.domain.board.service.AnonymousBoardService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/boards")
public class AnonymousBoardController {

    private final AnonymousBoardService anonymousBoardService;

    @PostMapping
    public ResponseEntity<Long> createBoard(
            @Valid @RequestBody BoardCreateRequestDto request,
            HttpSession session
    ) {
        return ResponseEntity.ok(anonymousBoardService.createBoard(request, session));
    }

    @GetMapping
    public ResponseEntity<List<BoardResponseDto>> getBoards() {
        return ResponseEntity.ok(anonymousBoardService.getBoards());
    }
}