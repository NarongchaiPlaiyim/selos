package com.clevel.selos.integration.ncb.nccrs.models.response;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import java.io.Serializable;
import java.util.ArrayList;

@XStreamAlias("overdrafts")
public class OverDraftsModel implements Serializable {

    @XStreamImplicit(itemFieldName = "histbalance")
    private ArrayList<OverDraftsHistBalanceModel> histbalance = new ArrayList<OverDraftsHistBalanceModel>();

    public ArrayList<OverDraftsHistBalanceModel> getHistbalance() {
        return histbalance;
    }
}
