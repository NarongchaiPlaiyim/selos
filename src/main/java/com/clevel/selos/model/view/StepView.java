package com.clevel.selos.model.view;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;

public class StepView implements Serializable {
    private long id;
    private String name;
    private String description;
    private StageView stageView;
    private int docCheck;
    private int checkBRMS;
    private int active;

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

    public StageView getStageView() {
        return stageView;
    }

    public void setStageView(StageView stageView) {
        this.stageView = stageView;
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
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("id", id)
                .append("name", name)
                .append("description", description)
                .append("stageView", stageView)
                .append("docCheck", docCheck)
                .append("checkBRMS", checkBRMS)
                .append("active", active)
                .toString();
    }
}
