package com.clevel.selos.model.db.master;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "mst_action")
public class Action implements Serializable {
    @Id
    @Column(name = "id")
    private long id;
    @Column(name = "name", length = 50)
    private String name;
    @Column(name = "description", length = 100)
    private String description;
    @Column(name = "submit_bpm")
    private int submitBPM;
    @Column(name = "active")
    private int active;

    @OneToOne
    @JoinColumn(name = "action_code")
    private Relretunactions relretunactions;

    public Relretunactions getRelretunactions() {
        return relretunactions;
    }

    public void setRelretunactions(Relretunactions relretunactions) {
        this.relretunactions = relretunactions;
    }

    public Action() {
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

    public int getSubmitBPM() {
        return submitBPM;
    }

    public void setSubmitBPM(int submitBPM) {
        this.submitBPM = submitBPM;
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
                append("submitBPM", submitBPM).
                append("active", active).
                toString();
    }
}
