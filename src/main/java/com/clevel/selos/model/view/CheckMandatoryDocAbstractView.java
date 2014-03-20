package com.clevel.selos.model.view;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public abstract class CheckMandatoryDocAbstractView implements Serializable {
    private long id;
    private String documentType;
    private List<String> BRMSDocumentType;
    private List<String> Owners;
    private List<CheckMandatoryDocFileNameView> fileNameViewList;
    private int complete;
    private boolean incomplete ;
    private boolean indistinct;
    private boolean Incorrect ;
    private boolean expire;
    private String remark;

    public CheckMandatoryDocAbstractView() {
        init();
    }

    private void init(){
        fileNameViewList = new ArrayList<CheckMandatoryDocFileNameView>();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDocumentType() {
        return documentType;
    }

    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }

    public List<String> getBRMSDocumentType() {
        return BRMSDocumentType;
    }

    public void setBRMSDocumentType(List<String> BRMSDocumentType) {
        this.BRMSDocumentType = BRMSDocumentType;
    }

    public List<String> getOwners() {
        return Owners;
    }

    public void setOwners(List<String> owners) {
        Owners = owners;
    }

    public List<CheckMandatoryDocFileNameView> getFileNameViewList() {
        return fileNameViewList;
    }

    public void setFileNameViewList(List<CheckMandatoryDocFileNameView> fileNameViewList) {
        this.fileNameViewList = fileNameViewList;
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

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("id", id)
                .append("documentType", documentType)
                .append("BRMSDocumentType", BRMSDocumentType)
                .append("Owners", Owners)
                .append("fileNameViewList", fileNameViewList)
                .append("complete", complete)
                .append("incomplete", incomplete)
                .append("indistinct", indistinct)
                .append("Incorrect", Incorrect)
                .append("expire", expire)
                .append("remark", remark)
                .toString();
    }
}
