package com.clevel.selos.integration.ncb.ncrs.models.response;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import java.io.Serializable;
import java.util.ArrayList;

@XStreamAlias("subject")
public class SubjectModel implements Serializable {
    @XStreamImplicit(itemFieldName = "name")
    private ArrayList<SubjectNameModel> name = new ArrayList<SubjectNameModel>();

    @XStreamImplicit(itemFieldName = "id")
    private ArrayList<SubjectIdModel> id = new ArrayList<SubjectIdModel>();

    @XStreamImplicit(itemFieldName = "address")
    private ArrayList<SubjectAddressModel> address = new ArrayList<SubjectAddressModel>();

    @XStreamImplicit(itemFieldName = "account")
    private ArrayList<SubjectAccountModel> account = new ArrayList<SubjectAccountModel>();

    @XStreamImplicit(itemFieldName = "enquiry")
    private ArrayList<EnquiryModel> enquiry = new ArrayList<EnquiryModel>();

    @XStreamImplicit(itemFieldName = "dispute")
    private ArrayList<SubjectDisputeModel> dispute = new ArrayList<SubjectDisputeModel>();

    public SubjectModel(ArrayList<SubjectNameModel> name
            , ArrayList<SubjectIdModel> id
            , ArrayList<SubjectAddressModel> address
            , ArrayList<SubjectAccountModel> account
            , ArrayList<EnquiryModel> enquiry
            , ArrayList<SubjectDisputeModel> dispute
    ) {
        this.name = name;
        this.id = id;
        this.address = address;
        this.account = account;
        this.enquiry = enquiry;
        this.dispute = dispute;
    }

    public ArrayList<SubjectNameModel> getName() {
        return name;
    }

    public ArrayList<SubjectIdModel> getId() {
        return id;
    }

    public ArrayList<SubjectAddressModel> getAddress() {
        return address;
    }

    public ArrayList<SubjectAccountModel> getAccount() {
        return account;
    }

    public ArrayList<EnquiryModel> getEnquiry() {
        return enquiry;
    }

    public ArrayList<SubjectDisputeModel> getDispute() {
        return dispute;
    }


}
