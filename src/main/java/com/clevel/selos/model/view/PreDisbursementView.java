package com.clevel.selos.model.view;

import java.io.Serializable;

public class PreDisbursementView implements Serializable {
    private int approvedType;
    private String test;
    private boolean selectedTest;

    public PreDisbursementView() {
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

    public String getTest() {
        return test;
    }

    public void setTest(String test) {
        this.test = test;
    }

    public boolean isSelectedTest() {
        return selectedTest;
    }

    public void setSelectedTest(boolean selectedTest) {
        this.selectedTest = selectedTest;
    }
}
