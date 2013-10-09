package com.clevel.selos.integration.rlos.csi.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.util.List;

public class CSIInputData implements Serializable {
    private List<NameModel> nameModelList;
    private List<IdModel> idModelList;

    public List<NameModel> getNameModelList() {
        return nameModelList;
    }

    public void setNameModelList(List<NameModel> nameModelList) {
        this.nameModelList = nameModelList;
    }

    public List<IdModel> getIdModelList() {
        return idModelList;
    }

    public void setIdModelList(List<IdModel> idModelList) {
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
