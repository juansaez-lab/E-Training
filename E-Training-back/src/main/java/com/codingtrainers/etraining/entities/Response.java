package com.codingtrainers.etraining.entities;

import jakarta.persistence.*;


@Entity
@Table (name = "response")
public class Response {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column (name = "description")
    private String description;

    @Column(name = "response_order")
    private Integer responseOrder;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id")
    private Question question;

    @Column (name = "active")
    private Boolean active;

    public Response() {}

    public Response(Long id, String description, int order, Question question) {
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

    public Question getQuestion() {
        return question;
    }

    public Integer getResponseOrder() {
        return responseOrder;
    }

    public void setResponseOrder(Integer responseOrder) {
        this.responseOrder = responseOrder;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}
