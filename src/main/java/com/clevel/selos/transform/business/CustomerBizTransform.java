package com.clevel.selos.transform.business;

import com.clevel.selos.dao.master.*;
import com.clevel.selos.integration.corebanking.model.corporateInfo.CorporateModel;
import com.clevel.selos.integration.corebanking.model.corporateInfo.CorporateResult;
import com.clevel.selos.integration.corebanking.model.customeraccount.CustomerAccountListModel;
import com.clevel.selos.integration.corebanking.model.customeraccount.CustomerAccountResult;
import com.clevel.selos.integration.corebanking.model.individualInfo.IndividualModel;
import com.clevel.selos.model.ActionResult;
import com.clevel.selos.model.Gender;
import com.clevel.selos.model.view.AddressView;
import com.clevel.selos.model.view.CustomerAccountView;
import com.clevel.selos.model.view.CustomerInfoResultView;
import com.clevel.selos.model.view.CustomerInfoView;
import com.clevel.selos.util.Util;
import org.hibernate.criterion.Restrictions;
import com.clevel.selos.integration.corebanking.model.individualInfo.IndividualResult;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class CustomerBizTransform extends BusinessTransform {
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
    AddressTypeDAO addressTypeDAO;
    @Inject
    MaritalStatusDAO maritalStatusDAO;
    @Inject
    BusinessTypeDAO businessTypeDAO;
    @Inject
    RaceDAO raceDAO;

    public CustomerInfoResultView tranformIndividual(IndividualResult individualResult){
        CustomerInfoResultView customerInfoResultView = null;
        if(individualResult!=null){
            customerInfoResultView = new CustomerInfoResultView();
            if(individualResult.getActionResult() == ActionResult.SUCCEED){
                customerInfoResultView.setActionResult(individualResult.getActionResult());
                customerInfoResultView.setCustomerId(individualResult.getCustomerId());
                if(individualResult.getIndividualModel()!=null){
                    IndividualModel individualModel = individualResult.getIndividualModel();
                    CustomerInfoView customerInfoView =new CustomerInfoView();

                    customerInfoView.setCitizenId(individualModel.getCitizenID());
                    customerInfoView.setTitleTh(titleDAO.findOneByCriteria(Restrictions.eq("titleTh", individualModel.getTitleTH())));
                    customerInfoView.setFirstNameTh(individualModel.getFirstname());
                    customerInfoView.setLastNameTh(individualModel.getLastname());
                    customerInfoView.setCustomerId(individualModel.getDocumentType());
                    customerInfoView.setDocumentType(documentTypeDAO.findOneByCriteria(Restrictions.eq("documentTypeCode", individualModel.getDocumentType())));
                    customerInfoView.setDocumentExpiredDate(Util.convertStringToDateBuddhist(individualModel.getDocumentExpiredDate()));
                    customerInfoView.setDateOfBirth(Util.convertStringToDateBuddhist(individualModel.getDateOfBirth()));

                    if(individualModel.getGender().equals("M")){
                        customerInfoView.setGender(Gender.MALE);
                    }else if(individualModel.getGender().equals("F")){
                        customerInfoView.setGender(Gender.FEMALE);
                    }
                    customerInfoView.setEducation(educationDAO.findOneByCriteria(Restrictions.eq("code",individualModel.getEducationBackground())));
                    customerInfoView.setOrigin(raceDAO.findOneByCriteria(Restrictions.eq("code",individualModel.getRace())));
                    customerInfoView.setMaritalStatus(maritalStatusDAO.findOneByCriteria(Restrictions.eq("code",individualModel.getMarriageStatus())));
                    customerInfoView.setNationality(nationalityDAO.findOneByCriteria(Restrictions.eq("code",individualModel.getNationality())));
                    customerInfoView.setNumberOfChild(new Integer(individualModel.getNumberOfChild()));
                    if(individualModel.getOccupationCode().matches("[0-9]*")){
                        customerInfoView.setOccupation(occupationDAO.findOneByCriteria(Restrictions.eq("code",new Integer(individualModel.getOccupationCode()))));
                    }
            //        customerInfoView.setBusinessType(businessTypeDAO.findOneByCriteria(Restrictions.eq("",individualModel.getBizCode())));

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
                    System.out.println("=================== Work Address District : "+individualModel.getWorkAddress().getDistrict());
                    workAddress.setDistrict(districtDAO.findOneByCriteria(Restrictions.eq("name",individualModel.getWorkAddress().getDistrict())));
                    workAddress.setProvince(provinceDAO.findOneByCriteria(Restrictions.eq("name",individualModel.getWorkAddress().getProvince())));
                    workAddress.setAddressType(addressTypeDAO.findById(3));
                    workAddress.setPostalCode(individualModel.getWorkAddress().getPostcode());
                    workAddress.setCountry(countryDAO.findOneByCriteria(Restrictions.eq("code2",individualModel.getWorkAddress().getCountryCode())));
                    workAddress.setPhoneNumber(workPhoneNumber);
                    workAddress.setExtension(workPhoneExtension);
                    workAddress.setTestString(individualModel.getWorkAddress().getDistrict());
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
                    currentAddress.setCountry(countryDAO.findOneByCriteria(Restrictions.eq("code2", individualModel.getCurrentAddress().getCountryCode())));
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
                    homeAddress.setCountry(countryDAO.findOneByCriteria(Restrictions.eq("code2",individualModel.getHomeAddress().getCountryCode())));
                    homeAddress.setPhoneNumber(homePhoneNumber);
                    homeAddress.setExtension(homePhoneExtension);
                    customerInfoView.setRegisterAddress(homeAddress);

                    customerInfoResultView.setCustomerInfoView(customerInfoView);
                }
            } else {
                customerInfoResultView.setCustomerId(individualResult.getCustomerId());
                customerInfoResultView.setActionResult(individualResult.getActionResult());
                customerInfoResultView.setReason(individualResult.getReason());
            }
        }
        return customerInfoResultView;
    }

    public CustomerInfoResultView tranformJuristic(CorporateResult corporateResult){
        CustomerInfoResultView customerInfoResultView = null;
        if(corporateResult != null){
            customerInfoResultView = new CustomerInfoResultView();
            if(corporateResult.getActionResult() == ActionResult.SUCCEED){
                customerInfoResultView.setActionResult(corporateResult.getActionResult());
                customerInfoResultView.setCustomerId(corporateResult.getCustomerId());
                if(corporateResult.getCorporateModel()!=null){
                    CorporateModel corporateModel = corporateResult.getCorporateModel();
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
                }
            } else {
                customerInfoResultView.setCustomerId(corporateResult.getCustomerId());
                customerInfoResultView.setActionResult(corporateResult.getActionResult());
                customerInfoResultView.setReason(corporateResult.getReason());
            }
        }

        return customerInfoResultView;
    }

    public CustomerAccountView transformCustomerAccount(CustomerAccountResult customerAccountResult){
        CustomerAccountView customerAccountView = null;
        if(customerAccountResult != null){
            customerAccountView = new CustomerAccountView();
            if(customerAccountResult.getActionResult() == ActionResult.SUCCEED){
                customerAccountView.setActionResult(customerAccountResult.getActionResult());
                customerAccountView.setCustomerId(customerAccountResult.getCustomerId());
                if(customerAccountResult.getAccountListModels()!=null && customerAccountResult.getAccountListModels().size() > 0){
                    List<String> accountList = new ArrayList<String>();
                    for(CustomerAccountListModel customerAccountListModel : customerAccountResult.getAccountListModels()){
                        if(!Util.isEmpty(customerAccountListModel.getAccountNo())){
                            accountList.add(customerAccountListModel.getName());
//                            accountList.add(customerAccountListModel.getAccountNo());
                        }
                    }
                    customerAccountView.setAccountList(accountList);
                }
            } else {
                customerAccountView.setCustomerId(customerAccountResult.getCustomerId());
                customerAccountView.setActionResult(customerAccountResult.getActionResult());
                customerAccountView.setReason(customerAccountResult.getReason());
            }
        }
        return customerAccountView;
    }
}
