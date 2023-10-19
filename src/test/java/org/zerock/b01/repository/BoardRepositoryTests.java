package org.zerock.b01.repository;

import lombok.extern.log4j.Log4j2;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.zerock.b01.domain.Board;

import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Log4j2
public class BoardRepositoryTests {

    @Autowired
    private BoardRepository boardRepository;

    @Test
    public void testInsert() {

        IntStream.rangeClosed(1, 100).forEach(i -> {
            Board board = Board.builder()
                    .title("title..." + i)
                    .content("content..." + i)
                    .writer("user" + (i %10))
                    .build();

            Board result = boardRepository.save(board);
            log.info("BNO: " + result.getBno());
        });
    }

    @DisplayName("select 기능 테스트")
    @Test
    public void testSelect() {
        Long bno = 100L;

//        Optional<Board> result = boardRepository.findById(bno);

//        Board board = result.orElseThrow();
//        log.info("board >>>>>>>>>>>>> {}", board);

        Board selectResult = boardRepository.findById(bno).orElseThrow();

        Assertions.assertThat(selectResult).isNotNull();
        log.info("board >>>>>>>>>>>>> {}", selectResult);

    }

    @DisplayName("update 기능 테스트")
    @Test
    public void testUpdate() {

        Long bno = 100L;

        Board updateTest = boardRepository.findById(bno).orElseThrow();
        updateTest.change("update..title 333", "update content 333");

        boardRepository.save(updateTest);
        log.info("updateTest >>>>>>>> {}", updateTest);
    }

    @DisplayName("delete 기능 테스트")
    @Test
    public void testDelete() {
        Long bno = 1L;

        boardRepository.deleteById(bno);
    }

    @Test
    public void testPaging() {

        // 1page order by bno desc
        Pageable pageable = PageRequest.of(0, 10, Sort.by("bno").descending());

        Page<Board> result = boardRepository.findAll(pageable);

        log.info("total count: " + result.getTotalElements());
        log.info("total pages: " + result.getTotalPages());
        log.info("current page number: " + result.getNumber());
        log.info("item count per page: " + result.getSize());

        List<Board> todoList = result.getContent();

//        todoList.forEach(board -> log.info(board));
        todoList.forEach(log::info);
    }

    @Test
    public void testSearch1() {

        // 2 page order by bno desc
        Pageable pageable = PageRequest.of(1, 10, Sort.by("bno").descending());

        boardRepository.search1(pageable);
    }

    @Test
    public void testSearchAll() {
        String[] types = {"t", "c", "w"};

        String keyword = "1";

        Pageable pageable = PageRequest.of(0, 10, Sort.by("bno").descending());

        Page<Board> result = boardRepository.searchAll(types, keyword, pageable);

        //total pages
        log.info(result.getTotalPages());

        // page size
        log.info(result.getSize());

        // pageNumber
        log.info(result.getNumber());

        // prev next
        log.info(result.hasPrevious() + ": " + result.hasNext());

        result.getContent().forEach(board -> log.info(board));
    }




}





















