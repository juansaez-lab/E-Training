package com.codingtrainers.etraining.repositories;

import com.codingtrainers.etraining.entities.Question;
import com.codingtrainers.etraining.entities.Response;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResponseRepository extends JpaRepository<Response, Long> {
    List<Response> findByQuestionId(Long questionId);

    @Query("delete from Response r where r.question.id = :questionId")
    void deleteAllByQuestionId(@Param("questionId") Long questionId);

    @Query("SELECT r FROM Response r WHERE r.question.id = :questionId AND r.active = true")
    List<Response> findActiveByQuestionId(@Param("questionId") Long questionId);

    void deleteAllByQuestion(Question question);

    @Query("SELECT r FROM Response r WHERE r.question.id IN (:questionIds) AND r.active = true")
    List<Response> findAllActiveByQuestionIdIn(@Param("questionIds") List<Long> questionIds);

    @Query("SELECT r FROM Response r WHERE r.question.id IN (:questionIds)")
    List<Response> findAllByQuestionIdIn(@Param("questionIds") List<Long> questionIds);

}
