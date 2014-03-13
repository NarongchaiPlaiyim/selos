package com.clevel.selos.model.view;

import java.io.Serializable;

public class CheckMandatoryDocView implements Serializable {
    private String documentType;
    private String BRMSDocument;
    private String owners;
    private String fileName;
    private int complete;
    private boolean incomplete ;
    private boolean indistinct;
    private boolean Incorrect ;
    private boolean expire;
    private String remark;
    private boolean disabledCompleteFlag;
    private String link;

    public CheckMandatoryDocView() {
        init();
    }

    public void reset(){
        init();
    }

    private void init(){

    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public boolean isDisabledCompleteFlag() {
        return disabledCompleteFlag;
    }

    public void setDisabledCompleteFlag(boolean disabledCompleteFlag) {
        this.disabledCompleteFlag = disabledCompleteFlag;
    }

    public String getOwners() {
        return owners;
    }

    public void setOwners(String owners) {
        this.owners = owners;
    }

    public String getDocumentType() {
        return documentType;
    }

    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }

    public String getBRMSDocument() {
        return BRMSDocument;
    }

    public void setBRMSDocument(String BRMSDocument) {
        this.BRMSDocument = BRMSDocument;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public int getComplete() {
        return complete;
    }

    public void setComplete(int complete) {
        this.complete = complete;
    }

    public boolean isIncomplete() {
        return incomplete;
    }

    public void setIncomplete(boolean incomplete) {
        this.incomplete = incomplete;
    }

    public boolean isIndistinct() {
        return indistinct;
    }

    public void setIndistinct(boolean indistinct) {
        this.indistinct = indistinct;
    }

    public boolean isIncorrect() {
        return Incorrect;
    }

    public void setIncorrect(boolean incorrect) {
        Incorrect = incorrect;
    }

    public boolean isExpire() {
        return expire;
    }

    public void setExpire(boolean expire) {
        this.expire = expire;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
