package com.clevel.selos.model.db.master;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "mst_step")
public class Step implements Serializable {
    @Id
    @Column(name = "id")
    private long id;
    @Column(name = "name", length = 10)
    private String name;
    @Column(name = "description", length = 100)
    private String description;
    @OneToOne
    @JoinColumn(name = "stage_id")
    private Stage stage;
    @Column(name = "doc_check", columnDefinition = "int default 0")
    private int docCheck;
    @Column(name = "check_brms", columnDefinition = "int defualt 0")
    private int checkBRMS;
    @Column(name = "active")
    private int active;

    public Step() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public int getDocCheck() {
        return docCheck;
    }

    public void setDocCheck(int docCheck) {
        this.docCheck = docCheck;
    }

    public int getCheckBRMS() {
        return checkBRMS;
    }

    public void setCheckBRMS(int checkBRMS) {
        this.checkBRMS = checkBRMS;
    }

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).
                append("id", id).
                append("name", name).
                append("description", description).
                append("stage", stage).
                append("active", active).
                toString();
    }
}
