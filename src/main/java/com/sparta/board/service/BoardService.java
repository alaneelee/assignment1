package com.sparta.board.service;

import com.sparta.board.dto.BoardRequestDto;
import com.sparta.board.dto.BoardResponseDto;
import com.sparta.board.entity.Board;
import com.sparta.board.repository.BoardRepository;
import java.util.List;
import java.util.Objects;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class BoardService {

    // DI
    private final BoardRepository boardRepository;
    public BoardService(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
    }

    // 게시글 작성
    public BoardResponseDto createBoard(BoardRequestDto requestDto) {
        Board board = new Board(requestDto);
        boardRepository.save(board);
        return new BoardResponseDto(board);
    }

    // 게시글 전체 조회
    public List<BoardResponseDto> getBoards() {
        return boardRepository.findAllByOrderByCreatedAtDesc().stream().map(BoardResponseDto::new).toList();
    }

    // 게시글 조회
    public BoardResponseDto getBoard(Long id) {
        Board board = findBoard(id);
        return new BoardResponseDto(board);
    }


    // 게시글 수정

    @Transactional
    public BoardResponseDto updateBoard(Long id, BoardRequestDto requestDto) {
        Board board = findBoard(id);

        String oldPassword = board.getPassword();
        String newPassword = requestDto.getPassword();

        if (Objects.equals(oldPassword, newPassword)) {
            board.update(requestDto);
            return new BoardResponseDto(board);
        } else {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
    }


    // 게시글 삭제
    public Long deleteBoard(Long id, BoardRequestDto requestDto) {
        Board board = findBoard(id);

        String oldPassword = board.getPassword();
        String newPassword = requestDto.getPassword();

        if (Objects.equals(oldPassword, newPassword)) {
            boardRepository.delete(board);
            return id;
        } else {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
    }

    private Board findBoard(Long id) {
        return boardRepository.findById(id).orElseThrow(() ->
            new IllegalArgumentException("선택한 게시글은 존재하지 않습니다.")
        );
    }
}
