package com.clevel.selos.integration.ncb.nccrs.models.response;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import java.util.ArrayList;

@XStreamAlias("overdrafts")
public class OverDraftsModel {
    
    @XStreamImplicit(itemFieldName = "histbalance")
    private ArrayList<OverDraftsHistBalanceModel> histbalance = new ArrayList<OverDraftsHistBalanceModel>(); 

    public ArrayList<OverDraftsHistBalanceModel> getHistbalance() {
        return histbalance;
    }
}
