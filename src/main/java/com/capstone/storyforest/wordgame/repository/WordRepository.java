package com.capstone.storyforest.wordgame.repository;

import com.capstone.storyforest.wordgame.entity.Word;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface WordRepository extends JpaRepository<Word, Long> {
    @Query(value = "SELECT * FROM word WHERE difficulty = :level ORDER BY RAND() LIMIT :count", nativeQuery = true)
    List<Word> findRandomByLevel(@Param("level") int level, @Param("count") int count);

    List<Word> findByTermInIgnoreCase(List<String> terms);

}
