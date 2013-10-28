package com.clevel.selos.integration.ncb.ncrs.models.request;

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

    /*public void setHeader(TUEFEnquiryHeaderModel header) {
        this.header = header;
    }

    public void setName(ArrayList<TUEFEnquiryNameModel> name) {
        this.name = name;
    }

    public void setId(ArrayList<TUEFEnquiryIdModel> id) {
        this.id = id;
    }*/


}
