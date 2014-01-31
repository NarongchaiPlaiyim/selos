package com.clevel.selos.model.view;

import com.clevel.selos.model.db.master.BankAccountPurpose;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;

public class BankAccountPurposeView implements Serializable {
    private boolean isSelected;
    private BankAccountPurpose purpose;

    public BankAccountPurposeView() {
        reset();
    }

    public void reset() {
        this.purpose = new BankAccountPurpose();
    }

    public BankAccountPurpose getPurpose() {
        return purpose;
    }

    public void setPurpose(BankAccountPurpose purpose) {
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
