package com.clevel.selos.integration.ncb.ncrs.models.response;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import java.io.Serializable;
import java.util.ArrayList;

@XStreamAlias("name")
public class SubjectNameModel implements Serializable {

    @XStreamAlias("familyname")
    private String familyname;

    @XStreamAlias("firstname")
    private String firstname;

    @XStreamAlias("middle")
    private String middle;

    @XStreamAlias("maritalstatus")
    private String maritalstatus;

    @XStreamAlias("dateofbirth")
    private String dateofbirth;

    @XStreamAlias("gender")
    private String gender;

    @XStreamAlias("title")
    private String title;

    @XStreamAlias("nationality")
    private String nationality;

    @XStreamAlias("numberofchildren")
    private String numberofchildren;

    @XStreamAlias("spousename")
    private String spousename;

    @XStreamAlias("occupation")
    private String occupation;

    @XStreamAlias("enqconsent")
    private String enqconsent;

    @XStreamImplicit(itemFieldName = "dispute")
    private ArrayList<SubjectNameDisputeModel> dispute = new ArrayList<SubjectNameDisputeModel>();

    public SubjectNameModel(String familyname, String firstname, String middle, String maritalstatus, String dateofbirth, String gender, String title, String nationality, String numberofchildren, String spousename, String occupation, String enqconsent, ArrayList<SubjectNameDisputeModel> dispute) {
        this.familyname = familyname;
        this.firstname = firstname;
        this.middle = middle;
        this.maritalstatus = maritalstatus;
        this.dateofbirth = dateofbirth;
        this.gender = gender;
        this.title = title;
        this.nationality = nationality;
        this.numberofchildren = numberofchildren;
        this.spousename = spousename;
        this.occupation = occupation;
        this.enqconsent = enqconsent;
        this.dispute = dispute;
    }

    public String getFamilyname() {
        return familyname;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getMiddle() {
        return middle;
    }

    public String getMaritalstatus() {
        return maritalstatus;
    }

    public String getDateofbirth() {
        return dateofbirth;
    }

    public String getGender() {
        return gender;
    }

    public String getTitle() {
        return title;
    }

    public String getNationality() {
        return nationality;
    }

    public String getNumberofchildren() {
        return numberofchildren;
    }

    public String getSpousename() {
        return spousename;
    }

    public String getOccupation() {
        return occupation;
    }

    public String getEnqconsent() {
        return enqconsent;
    }

    public ArrayList<SubjectNameDisputeModel> getDispute() {
        return dispute;
    }


}
