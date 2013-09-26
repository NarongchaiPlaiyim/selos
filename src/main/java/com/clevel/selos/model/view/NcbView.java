package com.clevel.selos.model.view;

import com.clevel.selos.model.ActionResult;

import java.util.List;

public class NcbView {
    private String idNumber;
    private ActionResult result;
    private String reason;
    private NcbResultView ncbResultView;
    private List<NcbRecordView> ncbRecordViews;

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

    public NcbResultView getNcbResultView() {
        return ncbResultView;
    }

    public void setNcbResultView(NcbResultView ncbResultView) {
        this.ncbResultView = ncbResultView;
    }

    public List<NcbRecordView> getNcbRecordViews() {
        return ncbRecordViews;
    }

    public void setNcbRecordViews(List<NcbRecordView> ncbRecordViews) {
        this.ncbRecordViews = ncbRecordViews;
    }
}
