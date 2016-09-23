package com.ojass.sathelper.entity;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * Created by Chandana on 9/16/2016.
 */
@Entity
public class TestResult {
    @Id
    @GeneratedValue
    private Long id;

    private String name;
    private Date testDate;

    @OneToOne
    private User teacher;

    @OneToMany(mappedBy = "testResult",cascade = CascadeType.ALL)
    private List<Result> results;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getTestDate() {
        return testDate;
    }

    public void setTestDate(Date testDate) {
        this.testDate = testDate;
    }

    public User getTeacher() {
        return teacher;
    }

    public void setTeacher(User teacher) {
        this.teacher = teacher;
    }

    public List<Result> getResults() {
        return results;
    }

    public void setResults(List<Result> results) {
        this.results = results;
    }
}
