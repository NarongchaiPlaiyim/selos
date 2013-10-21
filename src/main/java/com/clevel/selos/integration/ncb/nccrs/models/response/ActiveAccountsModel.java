package com.clevel.selos.integration.ncb.nccrs.models.response;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import java.util.ArrayList;

@XStreamAlias("activeaccounts")
public class ActiveAccountsModel {
    
    @XStreamImplicit(itemFieldName = "account")
    ArrayList<AccountModel> account = new ArrayList<AccountModel>();

    public ArrayList<AccountModel> getAccount() {
        return account;
    }
}
