package com.clevel.selos.integration.corebanking;


import com.clevel.selos.dao.master.*;
import com.clevel.selos.model.Gender;
import com.clevel.selos.model.RMmodel.individualInfo.IndividualModel;
import com.clevel.selos.model.view.AddressView;
import com.clevel.selos.model.view.CustomerInfoView;
import com.clevel.selos.util.Util;
import org.hibernate.criterion.Restrictions;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class TranformIndividual implements Serializable{

    @Inject
    public TranformIndividual(){

    }
     @PostConstruct
    public void onCreate(){

    }

    @Inject
    SubDistrictDAO subDistrictDAO;
    @Inject
    DistrictDAO districtDAO;
    @Inject
    ProvinceDAO provinceDAO;
    @Inject
    DocumentTypeDAO documentTypeDAO;
    @Inject
    TitleDAO titleDAO;
    @Inject
    NationalityDAO nationalityDAO;
    @Inject
    EducationDAO educationDAO;
    @Inject
    OccupationDAO occupationDAO;
    @Inject
    CountryDAO countryDAO;
    @Inject
    KYCLevelDAO kycLevelDAO;
    @Inject
    RelationDAO relationDAO;
    @Inject
    ReferenceDAO referenceDAO;
    @Inject
    AddressTypeDAO addressTypeDAO;
    @Inject
    MaritalStatusDAO maritalStatusDAO;
    @Inject
    BusinessTypeDAO businessTypeDAO;

    public CustomerInfoView tranform(IndividualModel individualModel){

        CustomerInfoView customerInfoView =new CustomerInfoView();

        customerInfoView.setCitizenId(individualModel.getCitizenID());
        customerInfoView.setTitleTh(titleDAO.findOneByCriteria(Restrictions.eq("titleTh", individualModel.getTitleTH())));
        customerInfoView.setFirstNameTh(individualModel.getFirstname());
        customerInfoView.setLastNameTh(individualModel.getLastname());
//        customerInfoView.setTitleEn(titleDAO.findOneByCriteria(Restrictions.eq("titleEn", individualModel.getTitleEN())));
//        customerInfoView.setFirstNameEn(individualModel.getFirstnameEN());
//        customerInfoView.setLastNameEn(individualModel.getLastnameEN());
        customerInfoView.setCustomerId(individualModel.getDocumentType());
        customerInfoView.setDocumentType(documentTypeDAO.findOneByCriteria(Restrictions.eq("type", individualModel.getDocumentType())));
        customerInfoView.setDocumentExpiredDate(Util.convertStringToDateBuddhist(individualModel.getDocumentExpiredDate()));
        customerInfoView.setDateOfBirth(Util.convertStringToDateBuddhist(individualModel.getDateOfBirth()));

        if(individualModel.getGender().equals("M")){
        customerInfoView.setGender(Gender.MALE);
        }else if(individualModel.getGender().equals("F")){
        customerInfoView.setGender(Gender.FEMALE);
        }
//        customerInfoView.setEducation(educationDAO.findOneByCriteria(Restrictions.eq("code",individualModel.getEducationBackground())));
//        customerInfoView.setOrigin(nationalityDAO.findOneByCriteria(Restrictions.eq("",individualModel.getRace())));  //***
//        customerInfoView.setMaritalStatus(maritalStatusDAO.findOneByCriteria(Restrictions.eq("",individualModel.getMarriageStatus())));
//        customerInfoView.setNationality(nationalityDAO.findOneByCriteria(Restrictions.eq("",individualModel.getNationality())));
        customerInfoView.setNumberOfChild(new Integer(individualModel.getNumberOfChild()));
//        customerInfoView.setOccupation(occupationDAO.findOneByCriteria(Restrictions.eq("",individualModel.getOccupationCode())));
//        customerInfoView.setBusinessType(businessTypeDAO.findOneByCriteria(Restrictions.eq("","")));

        CustomerInfoView spouse=new CustomerInfoView();
        spouse.setFirstNameTh(individualModel.getSpouse().getFirstname());
        spouse.setLastNameTh(individualModel.getSpouse().getLastname());
        spouse.setCitizenId(individualModel.getSpouse().getCitizenID());
        spouse.setDateOfBirth(Util.convertStringToDateBuddhist(individualModel.getSpouse().getDateOfBirth()));
        customerInfoView.setSpouse(spouse);


        //=========================================== ValidatePhoneNumber
        String mobileNumber1="";
        String mobileNumber2="";
        String faxNumber="";
        String workPhoneNumber="";
        String workPhoneExtension="";
        String currentPhoneNumber="";
        String currentPhoneExtension="";
        String homePhoneNumber="";
        String homePhoneExtension="";

        //If Telephone type 1  = M move telephoneNumber1 to Mobile (1)
        if(individualModel.getTelephoneNumber1().getTelephoneType().equals("M")){
            mobileNumber1=individualModel.getTelephoneNumber1().getTelephoneNumber();
        }
        // If Telephone type 1 = B move telephoneNumber1 to Working Address- Contact Number move extension1 to Working Address - Ext Number
        if(individualModel.getTelephoneNumber1().getTelephoneType().equals("B")){
            workPhoneNumber=individualModel.getTelephoneNumber1().getTelephoneNumber();
            workPhoneExtension=individualModel.getTelephoneNumber1().getExtension();
        }
        //If Telephone type 1 = R move telephoneNumber1 to Personal Information-Home Number move extension1 to Personal Information-Ext Number
        if(individualModel.getTelephoneNumber1().getTelephoneType().equals("R")){

        }

        //If Telephone type 2 = M : If no value in Mobile (1) , move telephoneNumber2 to Mobile (1),else move telephoneNumber2 to Mobile (2)
        if(individualModel.getTelephoneNumber2().getTelephoneType().equals("M")){
            if(mobileNumber1==""||mobileNumber1==null){
                mobileNumber1=individualModel.getTelephoneNumber2().getTelephoneNumber();
            }else{
                mobileNumber2=individualModel.getTelephoneNumber2().getTelephoneNumber();            //****************
            }
        }
        //If Telephone type 2 = B if no value in Working Address- Contact Number , move telephoneNumber2 to Working Address- Contact Number move extension2 to Working Address - Ext Number
        if(individualModel.getTelephoneNumber2().getTelephoneType().equals("B")){
           if(workPhoneNumber==""||workPhoneNumber==null){
               workPhoneNumber=individualModel.getTelephoneNumber2().getTelephoneNumber();
               workPhoneExtension=individualModel.getTelephoneNumber2().getExtension();
           }
        }
        // If Telephone type 2 = R , if no value in Personal Information-Home Number ,  move telephoneNumber2 to  Personal Information-Home Number move extension2 to Personal Information-Ext Number
        if(individualModel.getTelephoneNumber2().getTelephoneType().equals("R")){

        }

        //If Telephone type 3 = M : If no value in Mobile (1) , move telephoneNumber3 to Mobile (1),else move telephoneNumber3 to Mobile (2)
        if(individualModel.getTelephoneNumber3().getTelephoneType().equals("M")){
            if(mobileNumber1==""||mobileNumber1==null){
                mobileNumber1=individualModel.getTelephoneNumber3().getTelephoneNumber();
            }else{
                mobileNumber2=individualModel.getTelephoneNumber3().getTelephoneNumber();            //****************
            }
        }
        //If Telephone type 3 = B if no value in Working Address- Contact Number , move telephoneNumber3 to Working Address- Contact Number move extension3 to Working Address - Ext Number
        if(individualModel.getTelephoneNumber3().getTelephoneType().equals("B")){
            if(workPhoneNumber==""||workPhoneNumber==null){
                workPhoneNumber=individualModel.getTelephoneNumber3().getTelephoneNumber();
                workPhoneExtension=individualModel.getTelephoneNumber3().getExtension();
            }
        }
        // If Telephone type 3 = R , if no value in Personal Information-Home Number ,  move telephoneNumber3 to  Personal Information-Home Number move extension3 to Personal Information-Ext Number
        if(individualModel.getTelephoneNumber3().getTelephoneType().equals("R")){

        }

        //If Telephone type 4 = M : If no value in Mobile (1) , move telephoneNumber4 to Mobile (1),else move telephoneNumber4 to Mobile (2)
        if(individualModel.getTelephoneNumber4().getTelephoneType().equals("M")){
            if(mobileNumber1==""||mobileNumber1==null){
                mobileNumber1=individualModel.getTelephoneNumber4().getTelephoneNumber();
            }else{
                mobileNumber2=individualModel.getTelephoneNumber4().getTelephoneNumber();            //****************
            }
        }
        //If Telephone type 4 = B if no value in Working Address- Contact Number , move telephoneNumber4 to Working Address- Contact Number move extension4 to Working Address - Ext Number
        if(individualModel.getTelephoneNumber4().getTelephoneType().equals("B")){
            if(workPhoneNumber==""||workPhoneNumber==null){
                workPhoneNumber=individualModel.getTelephoneNumber4().getTelephoneNumber();
                workPhoneExtension=individualModel.getTelephoneNumber4().getExtension();
            }
        }
        // If Telephone type 4 = R , if no value in Personal Information-Home Number ,  move telephoneNumber4 to  Personal Information-Home Number move extension4 to Personal Information-Ext Number
        if(individualModel.getTelephoneNumber4().getTelephoneType().equals("R")){

        }



        customerInfoView.setMobileNumber(mobileNumber1);
        customerInfoView.setFaxNumber(faxNumber);

        //Workaddress
        AddressView workAddress=new AddressView();
        workAddress.setAddressNo(individualModel.getWorkAddress().getAddressNo());
        workAddress.setMoo(individualModel.getWorkAddress().getAddressMoo());
        workAddress.setBuilding(individualModel.getWorkAddress().getAddressBuilding());
        workAddress.setRoad(individualModel.getWorkAddress().getAddressStreet());
        workAddress.setSubDistrict(subDistrictDAO.findOneByCriteria(Restrictions.eq("name",individualModel.getWorkAddress().getSubdistrict())));
        workAddress.setDistrict(districtDAO.findOneByCriteria(Restrictions.eq("name",individualModel.getWorkAddress().getDistrict())));
        workAddress.setProvince(provinceDAO.findOneByCriteria(Restrictions.eq("name",individualModel.getWorkAddress().getProvince())));
        workAddress.setAddressType(addressTypeDAO.findById(3));
        workAddress.setPostalCode(individualModel.getWorkAddress().getPostcode());
        workAddress.setCountry(countryDAO.findOneByCriteria(Restrictions.eq("name",individualModel.getWorkAddress().getCountry())));
        workAddress.setCountryCode(countryDAO.findOneByCriteria(Restrictions.eq("code2",individualModel.getWorkAddress().getCountryCode())));
        workAddress.setPhoneNumber(workPhoneNumber);
        workAddress.setExtension(workPhoneExtension);
        customerInfoView.setWorkAddress(workAddress);

        //CurrentAddress
        AddressView currentAddress=new AddressView();
        currentAddress.setAddressNo(individualModel.getCurrentAddress().getAddressNo());
        currentAddress.setMoo(individualModel.getCurrentAddress().getAddressMoo());
        currentAddress.setBuilding(individualModel.getCurrentAddress().getAddressBuilding());
        currentAddress.setRoad(individualModel.getCurrentAddress().getAddressStreet());
        currentAddress.setSubDistrict(subDistrictDAO.findOneByCriteria(Restrictions.eq("name", individualModel.getCurrentAddress().getSubdistrict())));
        currentAddress.setDistrict(districtDAO.findOneByCriteria(Restrictions.eq("name", individualModel.getCurrentAddress().getDistrict())));
        currentAddress.setProvince(provinceDAO.findOneByCriteria(Restrictions.eq("name", individualModel.getCurrentAddress().getProvince())));
        currentAddress.setAddressType(addressTypeDAO.findById(1));
        currentAddress.setPostalCode(individualModel.getCurrentAddress().getPostcode());
        currentAddress.setCountry(countryDAO.findOneByCriteria(Restrictions.eq("name", individualModel.getCurrentAddress().getCountry())));
        currentAddress.setCountryCode(countryDAO.findOneByCriteria(Restrictions.eq("code2", individualModel.getCurrentAddress().getCountryCode())));
        currentAddress.setPhoneNumber(currentPhoneNumber);
        currentAddress.setExtension(currentPhoneExtension);
        customerInfoView.setCurrentAddress(currentAddress);

        //HomeAddress
        AddressView homeAddress=new AddressView();
        homeAddress.setAddressNo(individualModel.getHomeAddress().getAddressNo());
        homeAddress.setMoo(individualModel.getHomeAddress().getAddressMoo());
        homeAddress.setBuilding(individualModel.getHomeAddress().getAddressBuilding());
        homeAddress.setRoad(individualModel.getHomeAddress().getAddressStreet());
        homeAddress.setSubDistrict(subDistrictDAO.findOneByCriteria(Restrictions.eq("name",individualModel.getHomeAddress().getSubdistrict())));
        homeAddress.setDistrict(districtDAO.findOneByCriteria(Restrictions.eq("name",individualModel.getHomeAddress().getDistrict())));
        homeAddress.setProvince(provinceDAO.findOneByCriteria(Restrictions.eq("name",individualModel.getHomeAddress().getProvince())));
        homeAddress.setAddressType(addressTypeDAO.findById(2));
        homeAddress.setPostalCode(individualModel.getHomeAddress().getPostcode());
        homeAddress.setCountry(countryDAO.findOneByCriteria(Restrictions.eq("name",individualModel.getHomeAddress().getCountry())));
        homeAddress.setCountryCode(countryDAO.findOneByCriteria(Restrictions.eq("code2",individualModel.getHomeAddress().getCountryCode())));
        homeAddress.setPhoneNumber(homePhoneNumber);
        homeAddress.setExtension(homePhoneExtension);
        customerInfoView.setRegisterAddress(homeAddress);

        return customerInfoView;
    }



}
