package com.clevel.selos.integration.ncrs.models.response;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import java.io.Serializable;

@XStreamAlias("header")
public class TUEFResponseHeaderModel implements Serializable {
    @XStreamAlias("memberref")
    private String memberref;
    
    @XStreamAlias("subjectreturncode")
    private String subjectreturncode;
    
    @XStreamAlias("enqcontrolnum")
    private String enqcontrolnum;

    public TUEFResponseHeaderModel(String memberref, String subjectreturncode, String enqcontrolnum) {
        this.memberref = memberref;
        this.subjectreturncode = subjectreturncode;
        this.enqcontrolnum = enqcontrolnum;
    }

    public String getMemberref() {
        return memberref;
    }

    public String getSubjectreturncode() {
        return subjectreturncode;
    }

    public String getEnqcontrolnum() {
        return enqcontrolnum;
    }
    
    
    
}
