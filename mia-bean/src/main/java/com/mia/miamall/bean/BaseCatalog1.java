package com.mia.miamall.bean;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;

public class BaseCatalog1 implements Serializable {

    @Id
    @Column
    private String id;

    @Override
    public String toString() {
        return "BaseCatalog1{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                '}';
    }

    @Column
    private String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
