package com.clevel.selos.integration.ncb.nccrs.models.response;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import java.io.Serializable;
import java.util.ArrayList;

@XStreamAlias("closedaccounts")
public class ClosedAccountsModel implements Serializable {

    @XStreamImplicit(itemFieldName = "account")
    private ArrayList<ClosedAccountsAccountModel> account = new ArrayList<ClosedAccountsAccountModel>();

    public ArrayList<ClosedAccountsAccountModel> getAccount() {
        return account;
    }
}
