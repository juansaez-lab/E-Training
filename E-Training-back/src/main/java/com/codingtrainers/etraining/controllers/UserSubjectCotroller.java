package com.codingtrainers.etraining.controllers;

import com.codingtrainers.etraining.dto.input.AssignUserSubjectRequestDTO;
import com.codingtrainers.etraining.dto.output.UserSubjectResponseDTO;
import com.codingtrainers.etraining.entities.UserSubject;
import com.codingtrainers.etraining.services.UserSubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usersubject")
@CrossOrigin("*")
public class UserSubjectCotroller {

    @Autowired
   private UserSubjectService userSubjectService;

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<UserSubjectResponseDTO>> getSubjectsByUser(@PathVariable("userId") Long userId) {
        try {
            List<UserSubjectResponseDTO> subjects = userSubjectService.getSubjectsByUser(userId);
            if (subjects.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(subjects);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    @GetMapping("/subject/{subjectId}")
    public ResponseEntity<List<UserSubjectResponseDTO>> getUsersBySubject(@PathVariable("subjectId") Long subjectId) {
        try {
            List<UserSubjectResponseDTO> users = userSubjectService.getUsersBySubject(subjectId);
            if (users.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(users);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/assign")
    public ResponseEntity<UserSubject> assignUser(@RequestBody AssignUserSubjectRequestDTO dto) {
        try {
            UserSubject us = userSubjectService.assignUserToSubject(dto.getUserId(), dto.getSubjectId());
            return ResponseEntity.status(HttpStatus.CREATED).body(us);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    @PutMapping("/activate")
    public ResponseEntity<UserSubject> activateRelation(@RequestBody AssignUserSubjectRequestDTO dto) {
            try {
                UserSubject us = userSubjectService.reactivateRelation(dto.getUserId(), dto.getSubjectId());
                return ResponseEntity.ok(us);
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        }
    @PutMapping("/delete")
    public ResponseEntity<UserSubject> deleteRelation(@RequestBody AssignUserSubjectRequestDTO dto) {
        try {
            UserSubject us = userSubjectService.deleteRelation(dto.getUserId(), dto.getSubjectId());
            return ResponseEntity.ok(us);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
