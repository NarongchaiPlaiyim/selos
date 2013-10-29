package com.clevel.selos.integration.ncb.nccrs.models.response;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import java.util.ArrayList;

@XStreamAlias("creditsummary")
public class CreditSummaryModel {

    @XStreamImplicit(itemFieldName = "bytype")
    private ArrayList<ByTypeModel> bytype = new ArrayList<ByTypeModel>();

    public ArrayList<ByTypeModel> getBytype() {
        return bytype;
    }
}
