package com.clevel.selos.integration.corebanking;


import com.clevel.selos.dao.master.CustomerEntityDAO;
import com.clevel.selos.dao.master.DocumentTypeDAO;
import com.clevel.selos.dao.master.TitleDAO;
import com.clevel.selos.model.Gender;
import com.clevel.selos.model.RMmodel.corporateInfo.CorporateModel;
import com.clevel.selos.model.view.CustomerInfoView;
import com.clevel.selos.util.Util;
import org.hibernate.criterion.Restrictions;

import javax.inject.Inject;
import java.io.Serializable;

public class TranformCorporate implements Serializable{
    @Inject
    TitleDAO titleDAO;
    @Inject
    DocumentTypeDAO documentTypeDAO;
    @Inject
    CustomerEntityDAO customerEntityDAO;


    public CustomerInfoView tranform(CorporateModel corporateModel){

        CustomerInfoView customerInfoView =new CustomerInfoView();

        customerInfoView.setCitizenId(corporateModel.getRegistrationID());
        String thaiName[]=corporateModel.getCompanyNameTH1().split(" ");
        customerInfoView.setTitleTh(titleDAO.findOneByCriteria(Restrictions.eq("titleTh", thaiName[0])));
        customerInfoView.setFirstNameTh(thaiName[1]);
        customerInfoView.setLastNameTh(thaiName[2]);

        String engName[]=corporateModel.getCompanyNameEN1().split(" ");
        customerInfoView.setTitleEn(titleDAO.findOneByCriteria(Restrictions.eq("titleEn", engName[0])));
        customerInfoView.setFirstNameEn(engName[1]);
        customerInfoView.setLastNameEn(engName[2]);
        customerInfoView.setCustomerId(corporateModel.getTmbCusID());
//        customerInfoView.setDocumentType(documentTypeDAO.findOneByCriteria(Restrictions.eq("type", corporateModel.get())));
//        customerInfoView.setDocumentExpiredDate(Util.convertStringToDateBuddhist(individualModel.getDocumentExpiredDate()));
//        customerInfoView.setServiceSegment(individualModel.get);
        customerInfoView.setCustomerEntity(customerEntityDAO.findOneByCriteria(Restrictions.eq("","")));
        customerInfoView.setSearchBy(0);  //**
        customerInfoView.setSearchId("");     //**
//        customerInfoView.setBorrowerType("");
        customerInfoView.setCardAuthorizeBy("");
        customerInfoView.setGender(Gender.MALE);      //*
        customerInfoView.setAge(0);       //*
//        customerInfoView.setOrigin(nationalityDAO.findOneByCriteria(Restrictions.eq("",individualModel.getRace()))); //**
//        customerInfoView.setNationality("");
//        customerInfoView.setEducation(educationDAO.findOneByCriteria(Restrictions.eq("","")));
//        customerInfoView.setOccupation(occupationDAO.findOneByCriteria(Restrictions.eq("","")));
//        customerInfoView.setCitizenCountry(countryDAO.findOneByCriteria(Restrictions.eq("code2",individualModel.getNationality())));
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
//        customerInfoView.setNumberOfChild(new Integer(individualModel.getNumberOfChild()));
        customerInfoView.setChildrenList(null);       //*
//        customerInfoView.setApproxIncome("");     //*
//        customerInfoView.setMaritalStatus("");        //**
//        customerInfoView.setRegistrationId("");    //**
//        customerInfoView.setDateOfBirth(Util.convertStringToDateBuddhist(individualModel.getDateOfBirth()));
//        customerInfoView.setDateOfRegister(new Date()); //*
//        customerInfoView.setRelation(relationDAO.findOneByCriteria(Restrictions.eq("",""))); //*
//        customerInfoView.setReference(referenceDAO.findOneByCriteria(Restrictions.eq("",""))); //*
        customerInfoView.setCollateralOwner(false);
//        customerInfoView.setPercentShare(new BigDecimal(0));

        return customerInfoView;
    }
}
