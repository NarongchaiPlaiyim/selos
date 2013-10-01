package com.clevel.selos.transform.business;


import com.clevel.selos.dao.master.*;
import com.clevel.selos.integration.corebanking.model.corporateInfo.CorporateModel;
import com.clevel.selos.model.view.AddressView;
import com.clevel.selos.model.view.CustomerInfoView;
import com.clevel.selos.util.Util;
import org.hibernate.criterion.Restrictions;

import javax.inject.Inject;
import java.io.Serializable;

public class TranformCorporate implements Serializable{

    @Inject
    DocumentTypeDAO documentTypeDAO;
    @Inject
    TitleDAO titleDAO;
    @Inject
    CountryDAO countryDAO;
    @Inject
    SubDistrictDAO subDistrictDAO;
    @Inject
    DistrictDAO districtDAO;
    @Inject
    ProvinceDAO provinceDAO;

    public CustomerInfoView tranform(CorporateModel corporateModel){

        CustomerInfoView customerInfoView =new CustomerInfoView();

        customerInfoView.setCustomerId(corporateModel.getTmbCusID());
        customerInfoView.setTitleTh(titleDAO.findOneByCriteria(Restrictions.eq("titleTh", corporateModel.getTitleTH())));
        customerInfoView.setFirstNameTh(corporateModel.getCompanyNameTH());
        customerInfoView.setFirstNameEn(corporateModel.getCompanyNameEN());
        customerInfoView.setCitizenId(corporateModel.getRegistrationID());
        customerInfoView.setDateOfRegister(Util.convertStringToDateBuddhist(corporateModel.getRegistrationDate()));
        customerInfoView.setRegistrationCountry(countryDAO.findOneByCriteria(Restrictions.eq("code2",corporateModel.getRegistrationCountry())));

        //CurrentAddress
        AddressView currentAddress=new AddressView();
        currentAddress.setSubDistrict(subDistrictDAO.findOneByCriteria(Restrictions.eq("name",corporateModel.getSubdistrict())));
        currentAddress.setDistrict(districtDAO.findOneByCriteria(Restrictions.eq("name",corporateModel.getDistrict())));
        currentAddress.setProvince(provinceDAO.findOneByCriteria(Restrictions.eq("name",corporateModel.getProvince())));
        currentAddress.setPostalCode(corporateModel.getPostcode());
        currentAddress.setCountry(countryDAO.findOneByCriteria(Restrictions.eq("code2",corporateModel.getCountryCode())));
        customerInfoView.setCurrentAddress(currentAddress);

        AddressView registrationAddress=new AddressView();
        registrationAddress.setSubDistrict(subDistrictDAO.findOneByCriteria(Restrictions.eq("name",corporateModel.getRegistrationAddress().getSubdistrict())));
        registrationAddress.setDistrict(districtDAO.findOneByCriteria(Restrictions.eq("name",corporateModel.getRegistrationAddress().getDistrict())));
        registrationAddress.setProvince(provinceDAO.findOneByCriteria(Restrictions.eq("name",corporateModel.getRegistrationAddress().getProvince())));
        registrationAddress.setCountry(countryDAO.findOneByCriteria(Restrictions.eq("code2",corporateModel.getRegistrationAddress().getCountryCode())));
        registrationAddress.setPhoneNumber(corporateModel.getRegistrationAddress().getPhoneNo());
        registrationAddress.setExtension(corporateModel.getRegistrationAddress().getExtension());
        registrationAddress.setContactName(corporateModel.getRegistrationAddress().getContactName());
        registrationAddress.setContactPhone(corporateModel.getRegistrationAddress().getContactPhoneNo());
        customerInfoView.setRegisterAddress(registrationAddress);

        return customerInfoView;
    }
}
