package com.clevel.selos.model.view;

import com.clevel.selos.model.db.master.BusinessDescription;
import com.clevel.selos.system.message.Message;
import com.clevel.selos.system.message.NormalMessage;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.slf4j.Logger;

import javax.inject.Inject;

public class BusinessInfoView {
    @Inject
    Logger log;
    @Inject
    @NormalMessage
    Message msg;

    private BusinessDescription businessDescription;

    public BusinessDescription getBusinessDescription() {
        return businessDescription;
    }

    public void setBusinessDescription(BusinessDescription businessDescription) {
        this.businessDescription = businessDescription;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("businessDescription", businessDescription)
                .toString();
    }
}
