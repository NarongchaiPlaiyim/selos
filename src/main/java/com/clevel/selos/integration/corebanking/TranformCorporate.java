package com.clevel.selos.integration.corebanking;


import com.clevel.selos.dao.master.*;
import com.clevel.selos.model.Gender;
import com.clevel.selos.model.RMmodel.corporateInfo.CorporateModel;
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
    CustomerEntityDAO customerEntityDAO;
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

        customerInfoView.setCurrentAddress(currentAddress);

        return customerInfoView;
    }
}
