package com.clevel.selos.model.view;

import com.clevel.selos.integration.ncb.nccrs.nccrsmodel.NCCRSOutputModel;
import com.clevel.selos.integration.ncb.ncrs.ncrsmodel.NCRSOutputModel;

import java.io.Serializable;
import java.util.List;

public class NCBOutputView implements Serializable {
    private List<NCRSOutputModel> ncrsOutputModelList;
    private List<NCCRSOutputModel> nccrsOutputModelList;

    public NCBOutputView(){

    }

    public List<NCRSOutputModel> getNcrsOutputModelList() {
        return ncrsOutputModelList;
    }

    public void setNcrsOutputModelList(List<NCRSOutputModel> ncrsOutputModelList) {
        this.ncrsOutputModelList = ncrsOutputModelList;
    }

    public List<NCCRSOutputModel> getNccrsOutputModelList() {
        return nccrsOutputModelList;
    }

    public void setNccrsOutputModelList(List<NCCRSOutputModel> nccrsOutputModelList) {
        this.nccrsOutputModelList = nccrsOutputModelList;
    }
}
