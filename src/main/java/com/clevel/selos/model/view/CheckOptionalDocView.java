package com.clevel.selos.model.view;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CheckOptionalDocView implements Serializable /*extends CheckMandatoryDocAbstractView*/{
    private long id;
    private String documentType;
    private List<MandateDocBRMSView> BRMSDocumentTypeList;
    private List<MandateDocCustView> ownewList;
    private List<MandateDocFileNameView> fileNameViewList;
    private int complete;
    private boolean incomplete ;
    private boolean indistinct;
    private boolean Incorrect ;
    private boolean expire;
    private String remark;
    private boolean isCompleteFlag;
    public CheckOptionalDocView() {
//        super();
        init();
    }

    private void init(){
        BRMSDocumentTypeList = new ArrayList<MandateDocBRMSView>();
        ownewList = new ArrayList<MandateDocCustView>();
        fileNameViewList = new ArrayList<MandateDocFileNameView>();
        incomplete = false;
        indistinct = false;
        Incorrect = false;
        expire = false;
        isCompleteFlag = false;
    }

    public void readOnly(){
        isCompleteFlag = true;
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

    public List<MandateDocBRMSView> getBRMSDocumentTypeList() {
        return BRMSDocumentTypeList;
    }

    public void setBRMSDocumentTypeList(List<MandateDocBRMSView> BRMSDocumentTypeList) {
        this.BRMSDocumentTypeList = BRMSDocumentTypeList;
    }

    public List<MandateDocCustView> getOwnewList() {
        return ownewList;
    }

    public void setOwnewList(List<MandateDocCustView> ownewList) {
        this.ownewList = ownewList;
    }

    public List<MandateDocFileNameView> getFileNameViewList() {
        return fileNameViewList;
    }

    public void setFileNameViewList(List<MandateDocFileNameView> fileNameViewList) {
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

    public boolean isCompleteFlag() {
        return isCompleteFlag;
    }

    public void setCompleteFlag(boolean completeFlag) {
        isCompleteFlag = completeFlag;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("id", id)
                .append("documentType", documentType)
                .append("BRMSDocumentTypeList", BRMSDocumentTypeList)
                .append("ownewList", ownewList)
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
