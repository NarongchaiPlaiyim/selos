package com.clevel.selos.model.db.master;

import com.clevel.selos.model.StepType;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "mst_status")
public class Status implements Serializable {
    @Id
    @Column(name = "id")
    private long id;
    @Column(name = "type", length = 20)
    @Enumerated(EnumType.STRING)
    private StepType stepType;
    @Column(name = "name", length = 100)
    private String name;
    @Column(name = "code", length = 5)
    private String code;
    @Column(name = "description", length = 100)
    private String description;
    @Column(name = "active")
    private int active;

    public Status() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public StepType getStepType() {
        return stepType;
    }

    public void setStepType(StepType stepType) {
        this.stepType = stepType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
                append("stepType", stepType).
                append("name", name).
                append("description", description).
                append("active", active).
                toString();
    }
}
