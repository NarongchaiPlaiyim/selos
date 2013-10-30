package com.clevel.selos.integration.ncb.ncrs.models.response;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import java.io.Serializable;
import java.util.ArrayList;

@XStreamAlias("tuefenquiry")
public class TUEFEnquiryModel implements Serializable {

    @XStreamAlias("header")
    private TUEFEnquiryHeaderModel header;

    @XStreamImplicit(itemFieldName = "name")
    private ArrayList<TUEFEnquiryNameModel> name = new ArrayList<TUEFEnquiryNameModel>();

    @XStreamImplicit(itemFieldName = "id")
    private ArrayList<TUEFEnquiryIdModel> id = new ArrayList<TUEFEnquiryIdModel>();

    public TUEFEnquiryModel(TUEFEnquiryHeaderModel header, ArrayList<TUEFEnquiryNameModel> name, ArrayList<TUEFEnquiryIdModel> id) {
        this.header = header;
        this.name = name;
        this.id = id;
    }

    public TUEFEnquiryHeaderModel getHeader() {
        return header;
    }

    public ArrayList<TUEFEnquiryNameModel> getName() {
        return name;
    }

    public ArrayList<TUEFEnquiryIdModel> getId() {
        return id;
    }
}
