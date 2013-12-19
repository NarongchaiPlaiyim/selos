package com.clevel.selos.model.view;

import java.io.Serializable;

public class AgreementSingView implements Serializable {
    private int approvedType;
    private String confirm;
    private int complete;
    private String remark;

    public AgreementSingView() {
        init();
    }

    public void reset(){
        init();
    }

    private void init(){

    }

    public int getApprovedType() {
        return approvedType;
    }

    public void setApprovedType(int approvedType) {
        this.approvedType = approvedType;
    }

    public String getConfirm() {
        return confirm;
    }

    public void setConfirm(String confirm) {
        this.confirm = confirm;
    }

    public int getComplete() {
        return complete;
    }

    public void setComplete(int complete) {
        this.complete = complete;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
