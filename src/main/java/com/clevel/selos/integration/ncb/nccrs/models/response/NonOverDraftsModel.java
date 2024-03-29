package com.clevel.selos.integration.ncb.nccrs.models.response;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import java.io.Serializable;
import java.util.ArrayList;

@XStreamAlias("nonoverdrafts")
public class NonOverDraftsModel implements Serializable {

    @XStreamImplicit(itemFieldName = "histbalance")
    private ArrayList<NonOverDraftsHistBalanceModel> histbalance = new ArrayList<NonOverDraftsHistBalanceModel>();

    public ArrayList<NonOverDraftsHistBalanceModel> getHistbalance() {
        return histbalance;
    }
}
