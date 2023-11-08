package com.sparta.board.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sparta.board.entity.Board;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class BoardResponseDto {
    private Long id;
    private String title;
    private String writer;
    private String content;
    private LocalDateTime createdAt;

    @JsonIgnore
    private String password;

    public BoardResponseDto(Board board) {
        this.id = board.getId();
        this.title = board.getTitle();
        this.content = board.getContent();
        this.writer = board.getWriter();
        this.createdAt = board.getCreatedAt();
    }
}