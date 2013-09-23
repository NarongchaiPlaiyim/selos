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
    CustomerEntityDAO customerEntityDAO;
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

    public CustomerInfoView tranform(IndividualModel individualModel){

        CustomerInfoView customerInfoView =new CustomerInfoView();

        customerInfoView.setCitizenId(individualModel.getCitizenID());
        customerInfoView.setFirstNameTh(individualModel.getFirstname());
        customerInfoView.setLastNameTh(individualModel.getLastname());
        customerInfoView.setFirstNameEn(individualModel.getFirstnameEN());
        customerInfoView.setLastNameEn(individualModel.getLastnameEN());
        customerInfoView.setCustomerId(individualModel.getDocumentType());
        customerInfoView.setDocumentType(documentTypeDAO.findOneByCriteria(Restrictions.eq("type", individualModel.getDocumentType())));
        customerInfoView.setDocumentExpiredDate(Util.convertStringToDateBuddhist(individualModel.getDocumentExpiredDate()));
//        customerInfoView.setServiceSegment(individualModel.get);
//        customerInfoView.setCustomerEntity(customerEntityDAO.findOneByCriteria(Restrictions.eq("","")));
        customerInfoView.setSearchBy(0);  //**
        customerInfoView.setSearchId("");     //**
//        customerInfoView.setBorrowerType("");
        customerInfoView.setCardAuthorizeBy("");
        customerInfoView.setTitleTh(titleDAO.findOneByCriteria(Restrictions.eq("titleTh", individualModel.getTitleTH())));
        customerInfoView.setTitleEn(titleDAO.findOneByCriteria(Restrictions.eq("titleEn", individualModel.getTitleEN())));
        customerInfoView.setGender(Gender.MALE);      //*
        customerInfoView.setAge(0);       //*
//        customerInfoView.setOrigin(nationalityDAO.findOneByCriteria(Restrictions.eq("",individualModel.getRace()))); //**
//        customerInfoView.setNationality("");
//        customerInfoView.setEducation(educationDAO.findOneByCriteria(Restrictions.eq("","")));
//        customerInfoView.setOccupation(occupationDAO.findOneByCriteria(Restrictions.eq("","")));
        customerInfoView.setCitizenCountry(countryDAO.findOneByCriteria(Restrictions.eq("code2",individualModel.getNationality())));
//        customerInfoView.setRegistrationCountry("");      //*
        customerInfoView.setMobileNumber("");             //*
        customerInfoView.setFaxNumber("");                //*
//        customerInfoView.setEmail(individualModel);    //*
//        customerInfoView.setMailingAddressType("");   //*
//        customerInfoView.setKycLevel(kycLevelDAO.findOneByCriteria(Restrictions.eq("","")));  //*
//        customerInfoView.setConvenantFlag(false);
//        customerInfoView.setEwsFlag(false);
//        customerInfoView.setReviewFlag(false);
//        customerInfoView.setReason("");
        customerInfoView.setNumberOfChild(new Integer(individualModel.getNumberOfChild()));
        customerInfoView.setChildrenList(null);       //*
//        customerInfoView.setApproxIncome("");     //*
//        customerInfoView.setMaritalStatus("");        //**
//        customerInfoView.setRegistrationId("");    //**
        customerInfoView.setDateOfBirth(Util.convertStringToDateBuddhist(individualModel.getDateOfBirth()));
//        customerInfoView.setDateOfRegister(new Date()); //*
//        customerInfoView.setRelation(relationDAO.findOneByCriteria(Restrictions.eq("",""))); //*
//        customerInfoView.setReference(referenceDAO.findOneByCriteria(Restrictions.eq("",""))); //*
        customerInfoView.setCollateralOwner(false);
//        customerInfoView.setPercentShare(new BigDecimal(0));


        //Workaddress
        AddressView workAddress=new AddressView();
        workAddress.setAddressNo(individualModel.getWorkAddress().getAddressNo());
        workAddress.setMoo(individualModel.getWorkAddress().getAddressMoo());
        workAddress.setBuilding(individualModel.getWorkAddress().getAddressBuilding());
        workAddress.setRoad(individualModel.getWorkAddress().getAddressStreet());
        workAddress.setSubDistrict(subDistrictDAO.findOneByCriteria(Restrictions.eq("name",individualModel.getWorkAddress().getSubdistrict().replace("แขวง",""))));
        workAddress.setDistrict(districtDAO.findOneByCriteria(Restrictions.eq("name",individualModel.getWorkAddress().getDistrict().replace("เขต",""))));
        workAddress.setProvince(provinceDAO.findOneByCriteria(Restrictions.eq("name",individualModel.getWorkAddress().getProvince())));
        workAddress.setAddressType(addressTypeDAO.findById(3));
        workAddress.setPostalCode(individualModel.getWorkAddress().getPostcode());
        customerInfoView.setWorkAddress(workAddress);

        //CurrentAddress
        AddressView currentAddress=new AddressView();
        currentAddress.setAddressNo(individualModel.getCurrentAddress().getAddressNo());
        currentAddress.setMoo(individualModel.getCurrentAddress().getAddressMoo());
        currentAddress.setBuilding(individualModel.getCurrentAddress().getAddressBuilding());
        currentAddress.setRoad(individualModel.getCurrentAddress().getAddressStreet());
        currentAddress.setSubDistrict(subDistrictDAO.findOneByCriteria(Restrictions.eq("name",individualModel.getCurrentAddress().getSubdistrict().replace("แขวง",""))));
        currentAddress.setDistrict(districtDAO.findOneByCriteria(Restrictions.eq("name",individualModel.getCurrentAddress().getDistrict().replace("เขต",""))));
        currentAddress.setProvince(provinceDAO.findOneByCriteria(Restrictions.eq("name",individualModel.getCurrentAddress().getProvince())));
        currentAddress.setAddressType(addressTypeDAO.findById(1));
        currentAddress.setPostalCode(individualModel.getCurrentAddress().getPostcode());

        customerInfoView.setCurrentAddress(currentAddress);

        //HomeAddress
        AddressView homeAddress=new AddressView();
        homeAddress.setAddressNo(individualModel.getHomeAddress().getAddressNo());
        homeAddress.setMoo(individualModel.getHomeAddress().getAddressMoo());
        homeAddress.setBuilding(individualModel.getHomeAddress().getAddressBuilding());
        homeAddress.setRoad(individualModel.getHomeAddress().getAddressStreet());
        homeAddress.setSubDistrict(subDistrictDAO.findOneByCriteria(Restrictions.eq("name",individualModel.getHomeAddress().getSubdistrict().replace("แขวง",""))));
        homeAddress.setDistrict(districtDAO.findOneByCriteria(Restrictions.eq("name",individualModel.getHomeAddress().getDistrict().replace("เขต",""))));
        homeAddress.setProvince(provinceDAO.findOneByCriteria(Restrictions.eq("name",individualModel.getHomeAddress().getProvince())));
        homeAddress.setAddressType(addressTypeDAO.findById(2));
        homeAddress.setPostalCode(individualModel.getHomeAddress().getPostcode());
        customerInfoView.setRegisterAddress(homeAddress);

        return customerInfoView;
    }



}
