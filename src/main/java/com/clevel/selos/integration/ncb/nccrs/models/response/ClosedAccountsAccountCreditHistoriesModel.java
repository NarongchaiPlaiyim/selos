package com.clevel.selos.integration.ncb.nccrs.models.response;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import java.io.Serializable;
import java.util.ArrayList;

@XStreamAlias("credithistories")
public class ClosedAccountsAccountCreditHistoriesModel implements Serializable {

    @XStreamImplicit(itemFieldName = "credithist")
    ArrayList<ClosedAccountsAccountCreditHistModel> credithist = new ArrayList<ClosedAccountsAccountCreditHistModel>();

    public ArrayList<ClosedAccountsAccountCreditHistModel> getCredithist() {
        return credithist;
    }
}
