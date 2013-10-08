package com.clevel.selos.integration.rlos.csi.model;

import java.util.List;

public class CSIInputData {
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
}
