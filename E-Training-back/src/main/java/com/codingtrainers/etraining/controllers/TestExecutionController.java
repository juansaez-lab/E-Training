package com.codingtrainers.etraining.controllers;


import com.codingtrainers.etraining.dto.input.NotesFromTeacherRequestDTO;
import com.codingtrainers.etraining.dto.input.TestExecutionRequestDTO;
import com.codingtrainers.etraining.dto.output.TestExecutionDTO;
import com.codingtrainers.etraining.dto.output.TestExecutionReview;
import com.codingtrainers.etraining.services.TestExecutionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/testexecution")
@CrossOrigin("*")
public class TestExecutionController {

    @Autowired
    private TestExecutionService testExecutionService;

    @GetMapping("/")
    public ResponseEntity<List<TestExecutionDTO>> getTestExecutions() {
        List<TestExecutionDTO> executions = testExecutionService.getTestExecutionsDTO();
        if (executions.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(executions);
    }
    @GetMapping("/false")
    public ResponseEntity<List<TestExecutionDTO>> getDeletedTestExecutions() {
        List<TestExecutionDTO> executions = testExecutionService.getDeletedTestExecutionsDTO();
        if (executions.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(executions);
    }

    @GetMapping("/users/{userId}/executions")
    public ResponseEntity<List<TestExecutionDTO>> getTestExecutionsByUserId(@PathVariable("userId") Long userId) {
        List<TestExecutionDTO> testDtos = testExecutionService.getTestExecutionsByUserId(userId);
        return ResponseEntity.ok(testDtos);
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<TestExecutionDTO> getTestExecutionById(@PathVariable("id") Long id) {
        return testExecutionService.getTestExecutionDTOById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}/delete")
    public ResponseEntity<Void> deleteTestExecution(@PathVariable("id") Long id) {
        testExecutionService.deleteTestExecution(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/activate")
    public ResponseEntity<Void> activateExecution(@PathVariable("id") Long id) {
        testExecutionService.activateTestExecution(id);
        return ResponseEntity.noContent().build();
    }


    @PostMapping("/")
    public ResponseEntity<Void> saveTestExecution(@RequestBody TestExecutionRequestDTO testExecutionRequestDTO) {
        testExecutionService.saveTestExecution(testExecutionRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/notes")
    public ResponseEntity<Void> saveTestExecutionNotes(@RequestBody NotesFromTeacherRequestDTO notesFromTeacherRequestDTO) {
        testExecutionService.saveNotesFromTeacher(notesFromTeacherRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/users/{userId}/subjects/{subjectId}/executions")
    public ResponseEntity<List<TestExecutionDTO>> getActiveTestExecutionsByUserAndSubject(
            @PathVariable Long userId,
            @PathVariable Long subjectId) {
        List<TestExecutionDTO> executions = testExecutionService.findActiveByUserIdAndSubjectId(userId, subjectId);

        if (executions.isEmpty()) {
            return ResponseEntity.noContent().build(); // HTTP 204
        }
        return ResponseEntity.ok(executions);
    }

    @GetMapping("/{id}/review")
    public ResponseEntity<TestExecutionReview> getTestExecutionReviewById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(testExecutionService.getTestExecutionForReview(id));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }


    //esto no funciona
   /* @GetMapping("/{executionId}/structure")
    public ResponseEntity<TestExecutionFullDTO> getTextExecution(@PathVariable("executionId") Long executionId) {
        try {
            TestExecutionFullDTO dto = testExecutionService.getTestExecution(executionId);
            return ResponseEntity.ok(dto);
        } catch (EntityNotFoundException enfe) {
            return ResponseEntity.notFound().build();
        }
    }*/
}
