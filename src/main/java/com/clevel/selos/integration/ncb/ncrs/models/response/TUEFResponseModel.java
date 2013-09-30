package com.clevel.selos.integration.ncb.ncrs.models.response;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import java.io.Serializable;
import java.util.ArrayList;

@XStreamAlias("tuefresponse")
public class TUEFResponseModel implements Serializable {
    
    @XStreamAlias("responsedata")
    private Responsedata responsedata;
    
    @XStreamAlias("header")
    private TUEFResponseHeaderModel header;
    
    @XStreamImplicit(itemFieldName = "subject")
    private ArrayList<SubjectModel> subject = new ArrayList<SubjectModel>();

    public TUEFResponseModel(Responsedata responsedata, TUEFResponseHeaderModel header ,ArrayList<SubjectModel> subject) {
        this.responsedata = responsedata;
        this.header = header;
        this.subject = subject;
    }

    public Responsedata getResponsedata() {
        return responsedata;
    }

    public TUEFResponseHeaderModel getHeader() {
        return header;
    }

    public ArrayList<SubjectModel> getSubject() {
        return subject;
    }

    
}
