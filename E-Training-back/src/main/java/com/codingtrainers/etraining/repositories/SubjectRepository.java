package com.codingtrainers.etraining.repositories;
import com.codingtrainers.etraining.entities.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubjectRepository extends JpaRepository<Subject, Long> {

    Optional<Subject> findByIdAndActiveTrue(Long id);

    Optional<Subject> findByNameAndActiveTrue(String name);

    List<Subject> findAllByActiveTrue();

    List<Subject> findAllByActiveFalse();

    Subject save(Subject subject);

}
