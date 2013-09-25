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
        //bizCode not mapping
        customerInfoView.setMobileNumber("");
        customerInfoView.setFaxNumber("");



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
        customerInfoView.setWorkAddress(workAddress);

        //CurrentAddress
        AddressView currentAddress=new AddressView();
        currentAddress.setAddressNo(individualModel.getCurrentAddress().getAddressNo());
        currentAddress.setMoo(individualModel.getCurrentAddress().getAddressMoo());
        currentAddress.setBuilding(individualModel.getCurrentAddress().getAddressBuilding());
        currentAddress.setRoad(individualModel.getCurrentAddress().getAddressStreet());
        currentAddress.setSubDistrict(subDistrictDAO.findOneByCriteria(Restrictions.eq("name",individualModel.getCurrentAddress().getSubdistrict())));
        currentAddress.setDistrict(districtDAO.findOneByCriteria(Restrictions.eq("name",individualModel.getCurrentAddress().getDistrict())));
        currentAddress.setProvince(provinceDAO.findOneByCriteria(Restrictions.eq("name",individualModel.getCurrentAddress().getProvince())));
        currentAddress.setAddressType(addressTypeDAO.findById(1));
        currentAddress.setPostalCode(individualModel.getCurrentAddress().getPostcode());
                                     //     Not Find country countryCode

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
        customerInfoView.setRegisterAddress(homeAddress);

        return customerInfoView;
    }



}
