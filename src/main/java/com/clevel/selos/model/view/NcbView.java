package com.clevel.selos.model.view;

import com.clevel.selos.model.ActionResult;

import java.util.List;

public class NcbView {
    private String idNumber;
    private ActionResult result;
    private String reason;
    private NCBInfoView NCBInfoView;
    private List<NCBDetailView> NCBDetailViews;

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public ActionResult getResult() {
        return result;
    }

    public void setResult(ActionResult result) {
        this.result = result;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public NCBInfoView getNCBInfoView() {
        return NCBInfoView;
    }

    public void setNCBInfoView(NCBInfoView NCBInfoView) {
        this.NCBInfoView = NCBInfoView;
    }

    public List<NCBDetailView> getNCBDetailViews() {
        return NCBDetailViews;
    }

    public void setNCBDetailViews(List<NCBDetailView> NCBDetailViews) {
        this.NCBDetailViews = NCBDetailViews;
    }
}
