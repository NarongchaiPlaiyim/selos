package com.clevel.selos.model.view;

import com.clevel.selos.model.db.working.MandateDoc;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import java.io.Serializable;


public class MandateDocCustView implements Serializable {
    private long id;
    private String custName;
    private MandateDoc mandateDoc;
    public MandateDocCustView() {

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCustName() {
        return custName;
    }

    public void setCustName(String custName) {
        this.custName = custName;
    }

    public MandateDoc getMandateDoc() {
        return mandateDoc;
    }

    public void setMandateDoc(MandateDoc mandateDoc) {
        this.mandateDoc = mandateDoc;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("id", id)
                .append("custName", custName)
                .toString();
    }
}
