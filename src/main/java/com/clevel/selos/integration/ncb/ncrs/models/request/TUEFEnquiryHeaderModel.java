package com.clevel.selos.integration.ncb.ncrs.models.request;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import java.io.Serializable;

@XStreamAlias("header")
public class TUEFEnquiryHeaderModel implements Serializable {

    @XStreamAlias("memberref")
    private String memberref;

    @XStreamAlias("enqpurpose")
    private String enqpurpose;

    @XStreamAlias("enqamount")
    private String enqamount;

    @XStreamAlias("consent")
    private String consent;

    @XStreamAlias("disputeenquiry")
    private String disputeenquiry = "Y";

    public TUEFEnquiryHeaderModel(String memberref, String enqpurpose, String enqamount, String consent, String disputeenquiry) {
        this.memberref = memberref;
        this.enqpurpose = enqpurpose;
        this.enqamount = enqamount;
        this.consent = consent;
        this.disputeenquiry = disputeenquiry;
    }

    public TUEFEnquiryHeaderModel(String memberref, String enqpurpose, String enqamount, String consent) {
        this.memberref = memberref;
        this.enqpurpose = enqpurpose;
        this.enqamount = enqamount;
        this.consent = consent;
    }

    public TUEFEnquiryHeaderModel(String memberref, String enqpurpose, String consent) {
        this.memberref = memberref;
        this.enqpurpose = enqpurpose;
        this.consent = consent;
    }


}
