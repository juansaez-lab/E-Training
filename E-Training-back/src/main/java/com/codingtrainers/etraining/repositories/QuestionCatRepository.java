package com.codingtrainers.etraining.repositories;

import com.codingtrainers.etraining.entities.QuestionCat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionCatRepository extends JpaRepository<QuestionCat, Long> {

}

