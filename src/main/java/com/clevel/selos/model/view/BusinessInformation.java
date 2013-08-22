package com.clevel.selos.model.view;

import com.clevel.selos.model.db.master.BusinessDescription;
import com.clevel.selos.model.db.master.BusinessGroup;
import com.clevel.selos.system.MessageProvider;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.slf4j.Logger;

import javax.inject.Inject;

/**
 * Created with IntelliJ IDEA.
 * User: Rangsun
 * Date: 8/20/13
 * Time: 5:19 PM
 * To change this template use File | Settings | File Templates.
 */
public class BusinessInformation {
    @Inject
    Logger log;
    @Inject
    MessageProvider msg;

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
