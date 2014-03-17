package com.clevel.selos.model.view;

import com.clevel.selos.model.DocLevel;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.util.List;

public class MandateDocView implements Serializable{

    private String ecmDocTypeId;
    private String ecmDocTypeDesc;
    private List<String> brmsDescList;
    private List<CustomerInfoSimpleView> customerInfoSimpleViewList;
    private DocLevel docLevel;

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

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("ecmDocTypeId", ecmDocTypeId)
                .append("ecmDocTypeDesc", ecmDocTypeDesc)
                .append("brmsDescList", brmsDescList)
                .append("customerInfoSimpleViewList", customerInfoSimpleViewList)
                .append("docLevel", docLevel)
                .toString();
    }
}
