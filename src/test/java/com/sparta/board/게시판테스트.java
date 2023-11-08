package com.sparta.board;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.sparta.board.dto.BoardRequestDto;
import com.sparta.board.dto.BoardResponseDto;
import com.sparta.board.entity.Board;
import com.sparta.board.repository.BoardRepository;
import com.sparta.board.service.BoardService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.server.ResponseStatusException;

@SpringBootTest
public class 게시판테스트 {

    @PersistenceContext
    EntityManager em;

    @Autowired
    BoardService boardService;

    @Autowired
    BoardRepository boardRepository;
    @Test
    void 게시글_생성() {
        BoardRequestDto requestDto = new BoardRequestDto();
        requestDto.setTitle("테스트2 게시글");
        requestDto.setWriter("테스트2 작성자");
        requestDto.setPassword("비밀번호");
        requestDto.setContent("테스트2 내용");

        BoardResponseDto responseDto = boardService.createBoard(requestDto);
        assertEquals("테스트2 게시글", responseDto.getTitle());
        assertEquals("테스트2 작성자", responseDto.getWriter());
        assertEquals("테스트2 내용", responseDto.getContent());
    }

    @Test
    void 게시글_전체_조회() {
        List<BoardResponseDto> boardList = boardService.getBoards();
        assertTrue(boardList.size() >= 1);
    }

    @Test
    void 게시글_조회() {
        Long id = 1L;
        BoardResponseDto responseDto = boardService.getBoard(id);
        assertEquals("테스트 게시글", responseDto.getTitle());
        assertEquals("테스트 작성자", responseDto.getWriter());
        assertEquals("테스트 내용", responseDto.getContent());
    }



    @Test
    void 게시글_수정_비밀번호_일치() {
        BoardRequestDto requestDto = new BoardRequestDto();
        requestDto.setTitle("생성된 제목");
        requestDto.setWriter("생성된 작성자");
        requestDto.setContent("생성된 내용");
        requestDto.setPassword("비밀번호");

        BoardResponseDto boardResponseDto = boardService.createBoard(requestDto);
        Long id = boardResponseDto.getId();

        assertEquals("생성된 제목", boardResponseDto.getTitle());
        assertEquals("생성된 작성자", boardResponseDto.getWriter());
        assertEquals("생성된 내용", boardResponseDto.getContent());

        requestDto.setTitle("수정된 제목");
        requestDto.setWriter("수정된 작성자");
        requestDto.setContent("수정된 내용");
        requestDto.setPassword("비밀번호");

        boardService.updateBoard(id, requestDto);

        em.clear();

        Board matchingPassword = em.find(Board.class, id);

        assertEquals("수정된 제목", matchingPassword.getTitle());
        assertEquals("수정된 작성자", matchingPassword.getWriter());
        assertEquals("수정된 내용", matchingPassword.getContent());
    }

    @Test
    void 게시글_수정_비밀번호_불일치() {
        BoardRequestDto requestDto = new BoardRequestDto();
        requestDto.setTitle("생성된 제목");
        requestDto.setWriter("생성된 작성자");
        requestDto.setContent("생성된 내용");
        requestDto.setPassword("비밀번호");

        BoardResponseDto boardResponseDto = boardService.createBoard(requestDto);
        Long id = boardResponseDto.getId();

        assertEquals("생성된 제목", boardResponseDto.getTitle());
        assertEquals("생성된 작성자", boardResponseDto.getWriter());
        assertEquals("생성된 내용", boardResponseDto.getContent());

        requestDto.setTitle("수정된 제목");
        requestDto.setWriter("수정된 작성자");
        requestDto.setContent("수정된 내용");
        requestDto.setPassword("틀린 비밀번호");

        assertThrows(IllegalArgumentException.class, () -> {
            boardService.updateBoard(id, requestDto);
        });

    }



    @Test
    void 게시글_삭제_비밀번호_일치() {
        BoardRequestDto requestDto = new BoardRequestDto();
        requestDto.setTitle("생성된 제목");
        requestDto.setWriter("생성된 작성자");
        requestDto.setContent("생성된 내용");
        requestDto.setPassword("비밀번호");

        BoardResponseDto boardResponseDto = boardService.createBoard(requestDto);
        Long id = boardResponseDto.getId();

        requestDto.setPassword("비밀번호");
        Long deletedId = boardService.deleteBoard(id, requestDto);
        assertEquals(id, deletedId);
    }

    @Test
    void 게시글_삭제_비밀번호_불일치() {
        BoardRequestDto requestDto = new BoardRequestDto();
        requestDto.setTitle("생성된 제목");
        requestDto.setWriter("생성된 작성자");
        requestDto.setContent("생성된 내용");
        requestDto.setPassword("비밀번호");

        BoardResponseDto boardResponseDto = boardService.createBoard(requestDto);
        Long id = boardResponseDto.getId();

        requestDto.setPassword("틀린 비밀번호");
        assertThrows(IllegalArgumentException.class, () -> {
            boardService.updateBoard(id, requestDto);
        });
    }
}