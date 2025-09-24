package com.codingtrainers.etraining.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table (name = "response_cat")
public class ResponseCat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column (name = "description")
    private String description;

    @Column(name = "response_order")
    private Integer responseOrder;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id")
    private QuestionCat question;

    @Column (name = "active")
    private Boolean active;

    public ResponseCat() {}

    public ResponseCat(Long id, String description, int order, QuestionCat question) {
        this.id = id;
        this.description = description;
        this.responseOrder = order;
        this.question = question;
    }
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getOrder() {
        return responseOrder;
    }

    public void setOrder(Integer order) {
        this.responseOrder = order;
    }

    public QuestionCat getQuestion() {
        return question;
    }

    public void setQuestion(QuestionCat question) {
        this.question = question;
    }

}
