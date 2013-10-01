package com.clevel.selos.model.view;

import java.io.Serializable;
import java.util.List;
import com.clevel.selos.integration.brms.model.response.PreScreenResponse;

public class PreScreenResponseView implements Serializable {
    List<PreScreenResponse> custResponseList;
    List<PreScreenResponse> groupResponseList;

    public PreScreenResponseView(){

    }

    public List<PreScreenResponse> getCustResponseList() {
        return custResponseList;
    }

    public void setCustResponseList(List<PreScreenResponse> custResponseList) {
        this.custResponseList = custResponseList;
    }

    public List<PreScreenResponse> getGroupResponseList() {
        return groupResponseList;
    }

    public void setGroupResponseList(List<PreScreenResponse> groupResponseList) {
        this.groupResponseList = groupResponseList;
    }
}
