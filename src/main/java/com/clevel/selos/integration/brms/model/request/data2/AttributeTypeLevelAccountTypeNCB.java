package com.clevel.selos.integration.brms.model.request.data2;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;

public class AttributeTypeLevelAccountTypeNCB implements Serializable {
    private boolean tmbBankFlag;
    private boolean ncbNPLFlag;

    public AttributeTypeLevelAccountTypeNCB() {
    }

    public AttributeTypeLevelAccountTypeNCB(boolean tmbBankFlag, boolean ncbNPLFlag) {
        this.tmbBankFlag = tmbBankFlag;
        this.ncbNPLFlag = ncbNPLFlag;
    }

    public boolean isTmbBankFlag() {
        return tmbBankFlag;
    }

    public void setTmbBankFlag(boolean tmbBankFlag) {
        this.tmbBankFlag = tmbBankFlag;
    }

    public boolean isNcbNPLFlag() {
        return ncbNPLFlag;
    }

    public void setNcbNPLFlag(boolean ncbNPLFlag) {
        this.ncbNPLFlag = ncbNPLFlag;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("tmbBankFlag", tmbBankFlag)
                .append("ncbNPLFlag", ncbNPLFlag)
                .toString();
    }
}
