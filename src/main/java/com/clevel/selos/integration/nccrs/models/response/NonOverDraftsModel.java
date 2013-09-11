package com.clevel.selos.integration.nccrs.models.response;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import java.util.ArrayList;

@XStreamAlias("nonoverdrafts")
public class NonOverDraftsModel {
    
    @XStreamImplicit(itemFieldName = "histbalance")
    private ArrayList<NonOverDraftsHistBalanceModel> histbalance = new ArrayList<NonOverDraftsHistBalanceModel>();

    public ArrayList<NonOverDraftsHistBalanceModel> getHistbalance() {
        return histbalance;
    }
}
