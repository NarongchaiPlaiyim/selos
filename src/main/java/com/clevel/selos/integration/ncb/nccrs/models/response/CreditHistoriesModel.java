package com.clevel.selos.integration.ncb.nccrs.models.response;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import java.io.Serializable;
import java.util.ArrayList;

@XStreamAlias("credithistories")
public class CreditHistoriesModel implements Serializable {

    @XStreamImplicit(itemFieldName = "credithist")
    ArrayList<CreditHistModel> credithist = new ArrayList<CreditHistModel>();

    public ArrayList<CreditHistModel> getCredithist() {
        return credithist;
    }
}
