package com.clevel.selos.integration.ncb.nccrs.models.response;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import java.io.Serializable;
import java.util.ArrayList;

@XStreamAlias("additional")
public class AdditionalModel implements Serializable {

    @XStreamImplicit(itemFieldName = "id")
    private ArrayList<ProfileIdModel> id = new ArrayList<ProfileIdModel>();

    @XStreamImplicit(itemFieldName = "name")
    private ArrayList<ProfileNameModel> name = new ArrayList<ProfileNameModel>();

    @XStreamImplicit(itemFieldName = "address")
    private ArrayList<ProfileAddressModel> address = new ArrayList<ProfileAddressModel>();

    public ArrayList<ProfileIdModel> getId() {
        return id;
    }

    public ArrayList<ProfileNameModel> getName() {
        return name;
    }

    public ArrayList<ProfileAddressModel> getAddress() {
        return address;
    }
}
