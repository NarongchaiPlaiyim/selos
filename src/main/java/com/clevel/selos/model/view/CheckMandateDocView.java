package com.clevel.selos.model.view;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CheckMandateDocView implements Serializable{
    private long id;
    private List<CheckMandatoryDocView> mandatoryDocumentsList;
    private List<CheckOptionalDocView> optionalDocumentsList;
    private List<CheckOtherDocView> otherDocumentsList;

    private boolean isCompleteFlag;
    private boolean isReasonFlag;
    private boolean isRemarkFlag;

    public CheckMandateDocView() {
        init();
    }

    private void init(){
        mandatoryDocumentsList = new ArrayList<CheckMandatoryDocView>();
        optionalDocumentsList = new ArrayList<CheckOptionalDocView>();
        otherDocumentsList = new ArrayList<CheckOtherDocView>();
        isCompleteFlag = false;
        isReasonFlag = false;
        isRemarkFlag = false;
    }

    public void readOnly(){
        isCompleteFlag = true;
        isReasonFlag = true;
        isRemarkFlag = true;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public List<CheckMandatoryDocView> getMandatoryDocumentsList() {
        return mandatoryDocumentsList;
    }

    public void setMandatoryDocumentsList(List<CheckMandatoryDocView> mandatoryDocumentsList) {
        this.mandatoryDocumentsList = mandatoryDocumentsList;
    }

    public List<CheckOptionalDocView> getOptionalDocumentsList() {
        return optionalDocumentsList;
    }

    public void setOptionalDocumentsList(List<CheckOptionalDocView> optionalDocumentsList) {
        this.optionalDocumentsList = optionalDocumentsList;
    }

    public List<CheckOtherDocView> getOtherDocumentsList() {
        return otherDocumentsList;
    }

    public void setOtherDocumentsList(List<CheckOtherDocView> otherDocumentsList) {
        this.otherDocumentsList = otherDocumentsList;
    }

    public boolean isCompleteFlag() {
        return isCompleteFlag;
    }

    public void setCompleteFlag(boolean completeFlag) {
        isCompleteFlag = completeFlag;
    }

    public boolean isReasonFlag() {
        return isReasonFlag;
    }

    public void setReasonFlag(boolean reasonFlag) {
        isReasonFlag = reasonFlag;
    }

    public boolean isRemarkFlag() {
        return isRemarkFlag;
    }

    public void setRemarkFlag(boolean remarkFlag) {
        isRemarkFlag = remarkFlag;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("id", id)
                .append("mandatoryDocumentsList", mandatoryDocumentsList)
                .append("optionalDocumentsList", optionalDocumentsList)
                .append("otherDocumentsList", otherDocumentsList)
                .toString();
    }
}
