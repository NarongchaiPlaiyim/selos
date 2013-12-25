package com.clevel.selos.model.view;

import com.clevel.selos.model.db.master.OpenAccountPurpose;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

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

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).
                append("isSelected", isSelected).
                append("purpose", purpose).
                toString();
    }
}
