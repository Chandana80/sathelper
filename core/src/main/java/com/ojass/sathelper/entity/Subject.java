package com.ojass.sathelper.entity;

import javax.persistence.*;

/**
 * Created by Chandana on 9/16/2016.
 */
@Entity
public class Subject {

    @Id
    @GeneratedValue
    private Long id;
    private String name;

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

}
