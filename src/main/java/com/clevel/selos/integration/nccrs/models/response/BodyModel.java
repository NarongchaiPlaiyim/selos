package com.clevel.selos.integration.nccrs.models.response;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import java.util.ArrayList;
import java.util.List;

@XStreamAlias("body")
public class BodyModel {
    
    @XStreamAlias("transaction")
    private TransactionModel transaction;
    
    @XStreamAlias("errormsg")
    private String errormsg;

    public TransactionModel getTransaction() {
        return transaction;
    }

    public String getErrormsg() {
        return errormsg;
    }
}
