package com.codingtrainers.etraining.repositories;

import com.codingtrainers.etraining.entities.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {

    @Query("select q from Question q where q.test.id = :testId")
    List<Question> findAllByTestId(@Param("testId") Long testId);

    List<Question> findAllByActiveTrue();

    Optional<Question> findByIdAndActiveTrue(Long id);

    List<Question>findAllByActiveFalse();

    @Query("SELECT q FROM Question q WHERE q.id IN :ids AND q.active = true")
    List<Question> findAllActiveByIdIn(@Param("ids") List<Long> ids);
}

