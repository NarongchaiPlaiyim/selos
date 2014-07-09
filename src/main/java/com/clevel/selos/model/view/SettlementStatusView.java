package com.clevel.selos.model.view;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;

public class SettlementStatusView implements Serializable {
    private int id;
    private String name;
    private int active;
    private String ncbCode;

    public SettlementStatusView() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }

    public String getNcbCode() {
        return ncbCode;
    }

    public void setNcbCode(String ncbCode) {
        this.ncbCode = ncbCode;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("id", id)
                .append("name", name)
                .append("active", active)
                .append("ncbCode", ncbCode)
                .toString();
    }
}
