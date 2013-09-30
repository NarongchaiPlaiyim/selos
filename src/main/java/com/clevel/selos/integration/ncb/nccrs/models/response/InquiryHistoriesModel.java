package com.clevel.selos.integration.ncb.nccrs.models.response;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import java.util.ArrayList;

@XStreamAlias("inquiryhistories")
public class InquiryHistoriesModel {
    
    @XStreamImplicit(itemFieldName = "inqhist")
    private ArrayList<InqHistModel> inqhist = new ArrayList<InqHistModel>();

    public ArrayList<InqHistModel> getInqhist() {
        return inqhist;
    }
}
