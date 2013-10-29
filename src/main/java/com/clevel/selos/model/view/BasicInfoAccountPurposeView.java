package com.clevel.selos.model.view;

import com.clevel.selos.model.db.master.OpenAccountPurpose;

import java.io.Serializable;

public class BasicInfoAccountPurposeView implements Serializable {
    private boolean isSelected;
    private OpenAccountPurpose purpose;

    public BasicInfoAccountPurposeView() {
        reset();
    }

    public void reset() {
        this.purpose = new OpenAccountPurpose();
    }

    public OpenAccountPurpose getPurpose() {
        return purpose;
    }

    public void setPurpose(OpenAccountPurpose purpose) {
        this.purpose = purpose;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
