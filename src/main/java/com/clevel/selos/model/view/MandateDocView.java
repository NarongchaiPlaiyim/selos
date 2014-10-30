package com.clevel.selos.model.view;

import com.clevel.selos.model.DocLevel;
import com.clevel.selos.model.DocMandateType;
import com.clevel.selos.model.RadioValue;
import com.clevel.selos.model.view.master.ReasonView;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.faces.model.SelectItem;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MandateDocView implements Serializable {
    private long id;
    private String ecmDocTypeId;
    private String ecmDocTypeDesc;
    private List<String> brmsDescList;
    private List<CustomerInfoSimpleView> customerInfoSimpleViewList;
    private DocLevel docLevel;
    private int numberOfDoc;
    private DocMandateType docMandateType;
    private boolean display;
    private List<MandateDocFileNameView> mandateDocFileNameViewList;
    private RadioValue completedFlag;
    private List<String> selectedReasonList;
    private String remark;

    public MandateDocView(){
        selectedReasonList = new ArrayList<String>();
        customerInfoSimpleViewList = new ArrayList<CustomerInfoSimpleView>();
        mandateDocFileNameViewList = new ArrayList<MandateDocFileNameView>();
        brmsDescList = new ArrayList<String>();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getEcmDocTypeId() {
        return ecmDocTypeId;
    }

    public void setEcmDocTypeId(String ecmDocTypeId) {
        this.ecmDocTypeId = ecmDocTypeId;
    }

    public String getEcmDocTypeDesc() {
        return ecmDocTypeDesc;
    }

    public void setEcmDocTypeDesc(String ecmDocTypeDesc) {
        this.ecmDocTypeDesc = ecmDocTypeDesc;
    }

    public List<String> getBrmsDescList() {
        return brmsDescList;
    }

    public void setBrmsDescList(List<String> brmsDescList) {
        this.brmsDescList = brmsDescList;
    }

    public List<CustomerInfoSimpleView> getCustomerInfoSimpleViewList() {
        return customerInfoSimpleViewList;
    }

    public void setCustomerInfoSimpleViewList(List<CustomerInfoSimpleView> customerInfoSimpleViewList) {
        this.customerInfoSimpleViewList = customerInfoSimpleViewList;
    }

    public DocLevel getDocLevel() {
        return docLevel;
    }

    public void setDocLevel(DocLevel docLevel) {
        this.docLevel = docLevel;
    }

    public int getNumberOfDoc() {
        return numberOfDoc;
    }

    public void setNumberOfDoc(int numberOfDoc) {
        this.numberOfDoc = numberOfDoc;
    }

    public DocMandateType getDocMandateType() {
        return docMandateType;
    }

    public void setDocMandateType(DocMandateType docMandateType) {
        this.docMandateType = docMandateType;
    }

    public boolean isDisplay() {
        return display;
    }

    public void setDisplay(boolean display) {
        this.display = display;
    }

    public List<MandateDocFileNameView> getMandateDocFileNameViewList() {
        return mandateDocFileNameViewList;
    }

    public void setMandateDocFileNameViewList(List<MandateDocFileNameView> mandateDocFileNameViewList) {
        this.mandateDocFileNameViewList = mandateDocFileNameViewList;
    }

    public RadioValue getCompletedFlag() {
        return completedFlag;
    }

    public void setCompletedFlag(RadioValue completedFlag) {
        this.completedFlag = completedFlag;
    }

    public List<String> getSelectedReasonList() {
        return selectedReasonList;
    }

    public void setSelectedReasonList(List<String> selectedReasonList) {
        this.selectedReasonList = selectedReasonList;
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
                .append("ecmDocTypeId", ecmDocTypeId)
                .append("ecmDocTypeDesc", ecmDocTypeDesc)
                .append("brmsDescList", brmsDescList)
                .append("customerInfoSimpleViewList", customerInfoSimpleViewList)
                .append("docLevel", docLevel)
                .append("numberOfDoc", numberOfDoc)
                .append("docMandateType", docMandateType)
                .append("display", display)
                .toString();
    }
}
