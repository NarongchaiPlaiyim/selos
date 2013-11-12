package com.clevel.selos.integration.ncb.nccrs.models.response;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import java.io.Serializable;
import java.util.ArrayList;

@XStreamAlias("activeaccounts")
public class ActiveAccountsModel implements Serializable {

    @XStreamImplicit(itemFieldName = "account")
    ArrayList<AccountModel> account = new ArrayList<AccountModel>();

    public ArrayList<AccountModel> getAccount() {
        return account;
    }
}
