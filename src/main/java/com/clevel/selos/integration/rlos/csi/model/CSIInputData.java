package com.clevel.selos.integration.rlos.csi.model;

import com.clevel.selos.model.AccountInfoId;
import com.clevel.selos.model.AccountInfoName;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.util.List;

public class CSIInputData implements Serializable {
    private List<AccountInfoName> nameModelList;
    private List<AccountInfoId> idModelList;

    public List<AccountInfoName> getNameModelList() {
        return nameModelList;
    }

    public void setNameModelList(List<AccountInfoName> nameModelList) {
        this.nameModelList = nameModelList;
    }

    public List<AccountInfoId> getIdModelList() {
        return idModelList;
    }

    public void setIdModelList(List<AccountInfoId> idModelList) {
        this.idModelList = idModelList;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("nameModelList", nameModelList)
                .append("idModelList", idModelList)
                .toString();
    }
}
