package com.clevel.selos.model.view.openaccount;

import com.clevel.selos.model.db.master.OpenAccountPurpose;

import java.io.Serializable;

public class OpenAccountPurposeView implements Serializable {
    private boolean isSelected;
    private OpenAccountPurpose purpose;

    public OpenAccountPurposeView() {
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
