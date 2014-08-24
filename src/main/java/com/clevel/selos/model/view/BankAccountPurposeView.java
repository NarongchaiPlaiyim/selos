package com.clevel.selos.model.view;

import com.clevel.selos.model.db.master.BankAccountPurpose;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;

public class BankAccountPurposeView implements Serializable {

    private long id;
    private String name;
    private int active;
    private boolean pledgeDefault;

    private boolean forOD;
    private boolean forTCG;

    private boolean isSelected;

    public BankAccountPurposeView() {
        reset();
    }

    public void reset() {
        id = 0L;
        name = null;
        active = 0;
        pledgeDefault = false;
        forOD = false;
        forTCG = false;
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

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }

    public boolean isPledgeDefault() {
        return pledgeDefault;
    }

    public void setPledgeDefault(boolean pledgeDefault) {
        this.pledgeDefault = pledgeDefault;
    }

    public boolean isForOD() {
        return forOD;
    }

    public void setForOD(boolean forOD) {
        this.forOD = forOD;
    }

    public boolean isForTCG() {
        return forTCG;
    }

    public void setForTCG(boolean forTCG) {
        this.forTCG = forTCG;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("id", id)
                .append("name", name)
                .append("active", active)
                .append("pledgeDefault", pledgeDefault)
                .append("forOD", forOD)
                .append("forTCG", forTCG)
                .append("isSelected", isSelected)
                .toString();
    }
}
