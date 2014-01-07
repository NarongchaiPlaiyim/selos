package com.clevel.selos.model.view;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class ServiceSegmentView {

    private int id;
    private int code;
    private int active;
    private boolean existingSME;
    private boolean nonExistingSME;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }

    public boolean isExistingSME() {
        return existingSME;
    }

    public void setExistingSME(boolean existingSME) {
        this.existingSME = existingSME;
    }

    public boolean isNonExistingSME() {
        return nonExistingSME;
    }

    public void setNonExistingSME(boolean nonExistingSME) {
        this.nonExistingSME = nonExistingSME;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("nonExistingSME", nonExistingSME)
                .append("existingSME", existingSME)
                .append("active", active)
                .append("code", code)
                .append("id", id)
                .toString();
    }
}
