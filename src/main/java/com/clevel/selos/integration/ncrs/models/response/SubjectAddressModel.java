package com.clevel.selos.integration.ncrs.models.response;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import java.io.Serializable;
import java.util.ArrayList;

@XStreamAlias("address")
public class SubjectAddressModel implements Serializable {
    
    @XStreamAlias("line1")
    private String line1;
    
    @XStreamAlias("line2")
    private String line2;
    
    @XStreamAlias("line3")
    private String line3;
    
    @XStreamAlias("subdistrict")
    private String subdistrict;
    
    @XStreamAlias("district")
    private String district;
    
    @XStreamAlias("province")
    private String province;
    
    @XStreamAlias("country")
    private String country;
    
    @XStreamAlias("postalcode")
    private String postalcode;
    
    @XStreamAlias("telephone")
    private String telephone;
    
    @XStreamAlias("telephonetype")
    private String telephonetype;
    
    @XStreamAlias("addresstype")
    private String addresstype;
    
    @XStreamAlias("residential")
    private String residential;
    
    @XStreamAlias("reporteddate")
    private String reporteddate;
    
    @XStreamImplicit(itemFieldName = "dispute")
    private ArrayList<SubjectAddressDisputeModel> dispute = new ArrayList<SubjectAddressDisputeModel>();

    public SubjectAddressModel(String line1, String line2, String line3, String subdistrict, String district, String province, String country, String postalcode, String telephone, String telephonetype, String addresstype, String residential, String reporteddate, ArrayList<SubjectAddressDisputeModel> dispute) {
        this.line1 = line1;
        this.line2 = line2;
        this.line3 = line3;
        this.subdistrict = subdistrict;
        this.district = district;
        this.province = province;
        this.country = country;
        this.postalcode = postalcode;
        this.telephone = telephone;
        this.telephonetype = telephonetype;
        this.addresstype = addresstype;
        this.residential = residential;
        this.reporteddate = reporteddate;
        this.dispute = dispute;
    }

    public String getLine1() {
        return line1;
    }

    public String getLine2() {
        return line2;
    }

    public String getLine3() {
        return line3;
    }

    public String getSubdistrict() {
        return subdistrict;
    }

    public String getDistrict() {
        return district;
    }

    public String getProvince() {
        return province;
    }

    public String getCountry() {
        return country;
    }

    public String getPostalcode() {
        return postalcode;
    }

    public String getTelephone() {
        return telephone;
    }

    public String getTelephonetype() {
        return telephonetype;
    }

    public String getAddresstype() {
        return addresstype;
    }

    public String getResidential() {
        return residential;
    }

    public String getReporteddate() {
        return reporteddate;
    }

    public ArrayList<SubjectAddressDisputeModel> getDispute() {
        return dispute;
    }
    
}
