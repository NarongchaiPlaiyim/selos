package com.clevel.selos.integration.corebanking;


import com.clevel.selos.model.Gender;
import com.clevel.selos.model.RMmodel.individualInfo.IndividualModel;
import com.clevel.selos.model.view.AddressView;
import com.clevel.selos.model.view.CustomerInfoView;
import com.clevel.selos.util.Util;

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


    public CustomerInfoView tranform(IndividualModel individualModel){

        CustomerInfoView customerInfoView =new CustomerInfoView();

        customerInfoView.setCitizenId(individualModel.getCitizenID());
        customerInfoView.setFirstNameTh(individualModel.getFirstname());
        customerInfoView.setLastNameTh(individualModel.getLastname());
        customerInfoView.setFirstNameEn(individualModel.getFirstnameEN());
        customerInfoView.setLastNameEn(individualModel.getLastnameEN());
        customerInfoView.setCustomerId(individualModel.getDocumentType());
//        customerInfoView.setDocumentType("");
        customerInfoView.setDocumentExpiredDate(Util.convertStringToDateBuddhist(individualModel.getDocumentExpiredDate()));
        customerInfoView.setServiceSegment("");
//        customerInfoView.setCustomerEntity("");
        customerInfoView.setSearchBy(0);
        customerInfoView.setSearchId("");
//        customerInfoView.setBorrowerType("");
        customerInfoView.setCardAuthorizeBy("");
//        customerInfoView.setTitleTh("");
//        customerInfoView.setTitleEn("");
        customerInfoView.setGender(Gender.MALE);
        customerInfoView.setAge(0);
//        customerInfoView.setOrigin("");
//        customerInfoView.setNationality("");
//        customerInfoView.setEducation("");
//        customerInfoView.setOccupation("");
//        customerInfoView.setCitizenCountry("");
//        customerInfoView.setRegistrationCountry("");
        customerInfoView.setMobileNumber("");
        customerInfoView.setFaxNumber("");
        customerInfoView.setEmail("");
//        customerInfoView.setMailingAddressType("");
//        customerInfoView.setKycLevel("");
        customerInfoView.setConvenantFlag(false);
        customerInfoView.setEwsFlag(false);
        customerInfoView.setReviewFlag(false);
        customerInfoView.setReason("");
        customerInfoView.setNumberOfChild(0);
        customerInfoView.setChildrenList(null);
//        customerInfoView.setApproxIncome("");
//        customerInfoView.setMaritalStatus("");
        customerInfoView.setRegistrationId("");
        customerInfoView.setDateOfBirth(new Date());
        customerInfoView.setDateOfRegister(new Date());
//        customerInfoView.setRelation("");
//        customerInfoView.setReference("");
        customerInfoView.setCollateralOwner(false);
        customerInfoView.setPercentShare(new BigDecimal(0));


        //Workaddress
        AddressView workAddress=new AddressView();
        workAddress.setAddressNo(individualModel.getWorkAddress().getAddressNo());
        workAddress.setMoo(individualModel.getWorkAddress().getAddressMoo());
        workAddress.setBuilding(individualModel.getWorkAddress().getAddressBuilding());
        workAddress.setRoad(individualModel.getWorkAddress().getAddressStreet());
//        workAddress.setSubDistrict("");
//        workAddress.setDistrict("");
//        workAddress.setProvince("");
//        workAddress.setAddressType("");
        workAddress.setPostalCode(individualModel.getWorkAddress().getPostcode());
        customerInfoView.setWorkAddress(workAddress);

        //CurrentAddress
        AddressView currentAddress=new AddressView();
        currentAddress.setAddressNo(individualModel.getCurrentAddress().getAddressNo());
        currentAddress.setMoo(individualModel.getCurrentAddress().getAddressMoo());
        currentAddress.setBuilding(individualModel.getCurrentAddress().getAddressBuilding());
        currentAddress.setRoad(individualModel.getCurrentAddress().getAddressStreet());
//        currentAddress.setSubDistrict("");
//        currentAddress.setDistrict("");
//        currentAddress.setProvince("");
//        currentAddress.setAddressType("");
        currentAddress.setPostalCode(individualModel.getCurrentAddress().getPostcode());

        customerInfoView.setCurrentAddress(currentAddress);

        //HomeAddress
        AddressView homeAddress=new AddressView();
        homeAddress.setAddressNo(individualModel.getHomeAddress().getAddressNo());
        homeAddress.setMoo(individualModel.getHomeAddress().getAddressMoo());
        homeAddress.setBuilding(individualModel.getHomeAddress().getAddressBuilding());
        homeAddress.setRoad(individualModel.getHomeAddress().getAddressStreet());
//        homeAddress.setSubDistrict("");
//        homeAddress.setDistrict("");
//        homeAddress.setProvince("");
//        homeAddress.setAddressType("");
        homeAddress.setPostalCode(individualModel.getHomeAddress().getPostcode());
        customerInfoView.setCurrentAddress(homeAddress);

        return customerInfoView;
    }



}
