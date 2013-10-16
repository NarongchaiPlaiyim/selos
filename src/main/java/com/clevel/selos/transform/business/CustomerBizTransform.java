package com.clevel.selos.transform.business;

import com.clevel.selos.dao.master.*;
import com.clevel.selos.integration.corebanking.model.corporateInfo.CorporateModel;
import com.clevel.selos.integration.corebanking.model.corporateInfo.CorporateResult;
import com.clevel.selos.integration.corebanking.model.customeraccount.CustomerAccountListModel;
import com.clevel.selos.integration.corebanking.model.customeraccount.CustomerAccountResult;
import com.clevel.selos.integration.corebanking.model.individualInfo.IndividualModel;
import com.clevel.selos.model.ActionResult;
import com.clevel.selos.model.Gender;
import com.clevel.selos.model.db.master.*;
import com.clevel.selos.model.view.*;
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
    @Inject
    CustomerEntityDAO customerEntityDAO;

    public CustomerInfoResultView tranformIndividual(IndividualResult individualResult){
        CustomerInfoResultView customerInfoResultView = null;
        if(individualResult!=null){
            customerInfoResultView = new CustomerInfoResultView();
            if(individualResult.getActionResult() == ActionResult.SUCCEED){
                customerInfoResultView.setActionResult(individualResult.getActionResult());
                customerInfoResultView.setCustomerId(individualResult.getCustomerId());
                if(individualResult.getIndividualModel()!=null){
                    IndividualModel individualModel = individualResult.getIndividualModel();
                    CustomerInfoView customerInfoView = new CustomerInfoView();

                    customerInfoView.reset();

                    customerInfoView.setCitizenId(individualModel.getCitizenID());
                    customerInfoView.setTitleTh(titleDAO.findOneByCriteria(Restrictions.eq("titleTh", individualModel.getTitleTH())));
                    if(customerInfoView.getTitleTh() == null){
                        customerInfoView.setTitleTh(new Title());
                    }
                    customerInfoView.setFirstNameTh(individualModel.getFirstname());
                    customerInfoView.setLastNameTh(individualModel.getLastname());
                    customerInfoView.setTmbCustomerId(individualModel.getDocumentType());
                    customerInfoView.setDocumentType(documentTypeDAO.findOneByCriteria(Restrictions.eq("documentTypeCode", individualModel.getDocumentType())));
                    customerInfoView.setCustomerEntity(customerEntityDAO.findById(1));
                    if(customerInfoView.getDocumentType() == null){
                        customerInfoView.setDocumentType(new DocumentType());
                    }
                    customerInfoView.setDocumentExpiredDate(Util.convertStringToDateBuddhist(individualModel.getDocumentExpiredDate()));
                    customerInfoView.setDateOfBirth(Util.convertStringToDateBuddhist(individualModel.getDateOfBirth()));

                    if(individualModel.getGender().equals("M")){
                        customerInfoView.setGender(Gender.MALE);
                    }else if(individualModel.getGender().equals("F")){
                        customerInfoView.setGender(Gender.FEMALE);
                    }
                    customerInfoView.setEducation(educationDAO.findOneByCriteria(Restrictions.eq("code",individualModel.getEducationBackground())));
                    if(customerInfoView.getEducation() == null){
                        customerInfoView.setEducation(new Education());
                    }
                    customerInfoView.setOrigin(raceDAO.findOneByCriteria(Restrictions.eq("code",individualModel.getRace())));
                    if(customerInfoView.getOrigin() == null){
                        customerInfoView.setOrigin(new Race());
                    }
                    customerInfoView.setMaritalStatus(maritalStatusDAO.findOneByCriteria(Restrictions.eq("code",individualModel.getMarriageStatus())));
                    if(customerInfoView.getMaritalStatus() == null){
                        customerInfoView.setMaritalStatus(new MaritalStatus());
                    }
                    customerInfoView.setNationality(nationalityDAO.findOneByCriteria(Restrictions.eq("code",individualModel.getNationality())));
                    if(customerInfoView.getNationality() == null){
                        customerInfoView.setNationality(new Nationality());
                    }
                    customerInfoView.setNumberOfChild(new Integer(individualModel.getNumberOfChild()));
                    if(individualModel.getOccupationCode().matches("[0-9]*")){
                        customerInfoView.setOccupation(occupationDAO.findOneByCriteria(Restrictions.eq("code",new Integer(individualModel.getOccupationCode()))));
                        if(customerInfoView.getOccupation() == null){
                            customerInfoView.setOccupation(new Occupation());
                        }
                    }else{
                        customerInfoView.setOccupation(new Occupation());
                    }
            //        customerInfoView.setBusinessType(businessTypeDAO.findOneByCriteria(Restrictions.eq("",individualModel.getBizCode())));

                    CustomerInfoView spouse = new CustomerInfoView();
                    spouse.reset();
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
                    AddressView workAddress = new AddressView();
                    workAddress.setAddressNo(individualModel.getWorkAddress().getAddressNo());
                    workAddress.setMoo(individualModel.getWorkAddress().getAddressMoo());
                    workAddress.setBuilding(individualModel.getWorkAddress().getAddressBuilding());
                    workAddress.setRoad(individualModel.getWorkAddress().getAddressStreet());
                    workAddress.setSubDistrict(subDistrictDAO.findOneByCriteria(Restrictions.eq("name",individualModel.getWorkAddress().getSubdistrict())));
                    if(workAddress.getSubDistrict() == null){
                        workAddress.setSubDistrict(new SubDistrict());
                    }
                    workAddress.setDistrict(districtDAO.findOneByCriteria(Restrictions.eq("name",individualModel.getWorkAddress().getDistrict())));
                    if(workAddress.getDistrict() == null){
                        workAddress.setDistrict(new District());
                    }
                    workAddress.setProvince(provinceDAO.findOneByCriteria(Restrictions.eq("name",individualModel.getWorkAddress().getProvince())));
                    if(workAddress.getProvince() == null){
                        workAddress.setProvince(new Province());
                    }
                    workAddress.setAddressType(addressTypeDAO.findById(3));
                    workAddress.setPostalCode(individualModel.getWorkAddress().getPostcode());
                    workAddress.setCountry(countryDAO.findOneByCriteria(Restrictions.eq("code2",individualModel.getWorkAddress().getCountryCode())));
                    if(workAddress.getCountry() == null){
                        workAddress.setCountry(new Country());
                    }
                    workAddress.setPhoneNumber(workPhoneNumber);
                    workAddress.setExtension(workPhoneExtension);
                    customerInfoView.setWorkAddress(workAddress);

                    //CurrentAddress
                    AddressView currentAddress = new AddressView();
                    currentAddress.setAddressNo(individualModel.getCurrentAddress().getAddressNo());
                    currentAddress.setMoo(individualModel.getCurrentAddress().getAddressMoo());
                    currentAddress.setBuilding(individualModel.getCurrentAddress().getAddressBuilding());
                    currentAddress.setRoad(individualModel.getCurrentAddress().getAddressStreet());
                    currentAddress.setSubDistrict(subDistrictDAO.findOneByCriteria(Restrictions.eq("name", individualModel.getCurrentAddress().getSubdistrict())));
                    if(currentAddress.getSubDistrict() == null){
                        currentAddress.setSubDistrict(new SubDistrict());
                    }
                    currentAddress.setDistrict(districtDAO.findOneByCriteria(Restrictions.eq("name", individualModel.getCurrentAddress().getDistrict())));
                    if(currentAddress.getDistrict() == null){
                        currentAddress.setDistrict(new District());
                    }
                    currentAddress.setProvince(provinceDAO.findOneByCriteria(Restrictions.eq("name", individualModel.getCurrentAddress().getProvince())));
                    if(currentAddress.getProvince() == null){
                        currentAddress.setProvince(new Province());
                    }
                    currentAddress.setAddressType(addressTypeDAO.findById(1));
                    currentAddress.setPostalCode(individualModel.getCurrentAddress().getPostcode());
                    currentAddress.setCountry(countryDAO.findOneByCriteria(Restrictions.eq("code2", individualModel.getCurrentAddress().getCountryCode())));
                    if(currentAddress.getCountry() == null){
                        currentAddress.setCountry(new Country());
                    }
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
                    if(homeAddress.getSubDistrict() == null){
                        homeAddress.setSubDistrict(new SubDistrict());
                    }
                    homeAddress.setDistrict(districtDAO.findOneByCriteria(Restrictions.eq("name",individualModel.getHomeAddress().getDistrict())));
                    if(homeAddress.getDistrict() == null){
                        homeAddress.setDistrict(new District());
                    }
                    homeAddress.setProvince(provinceDAO.findOneByCriteria(Restrictions.eq("name",individualModel.getHomeAddress().getProvince())));
                    if(homeAddress.getProvince() == null){
                        homeAddress.setProvince(new Province());
                    }
                    homeAddress.setAddressType(addressTypeDAO.findById(2));
                    homeAddress.setPostalCode(individualModel.getHomeAddress().getPostcode());
                    homeAddress.setCountry(countryDAO.findOneByCriteria(Restrictions.eq("code2",individualModel.getHomeAddress().getCountryCode())));
                    if(homeAddress.getCountry() == null){
                        homeAddress.setCountry(new Country());
                    }
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

                    customerInfoView.setTmbCustomerId(corporateModel.getTmbCusID());
                    customerInfoView.setTitleTh(titleDAO.findOneByCriteria(Restrictions.eq("titleTh", corporateModel.getTitleTH())));
                    if(customerInfoView.getTitleTh() == null){
                        customerInfoView.setTitleTh(new Title());
                    }
                    customerInfoView.setFirstNameTh(corporateModel.getCompanyNameTH());
                    customerInfoView.setFirstNameEn(corporateModel.getCompanyNameEN());
                    customerInfoView.setRegistrationId(corporateModel.getRegistrationID());
                    customerInfoView.setDocumentType(documentTypeDAO.findOneByCriteria(Restrictions.eq("documentTypeCode", corporateModel.getDocumentType())));
                    if(customerInfoView.getDocumentType() == null){
                        customerInfoView.setDocumentType(new DocumentType());
                    }
                    customerInfoView.setCustomerEntity(customerEntityDAO.findById(2));
                    customerInfoView.setDateOfRegister(Util.convertStringToDateBuddhist(corporateModel.getRegistrationDate()));
                    customerInfoView.setRegistrationCountry(countryDAO.findOneByCriteria(Restrictions.eq("code2",corporateModel.getRegistrationCountry())));
                    if(customerInfoView.getRegistrationCountry() == null){
                        customerInfoView.setRegistrationCountry(new Country());
                    }

                    //CurrentAddress
                    AddressView currentAddress = new AddressView();
                    currentAddress.setSubDistrict(subDistrictDAO.findOneByCriteria(Restrictions.eq("name",corporateModel.getSubdistrict())));
                    if(currentAddress.getSubDistrict() == null){
                        currentAddress.setSubDistrict(new SubDistrict());
                    }
                    currentAddress.setDistrict(districtDAO.findOneByCriteria(Restrictions.eq("name",corporateModel.getDistrict())));
                    if(currentAddress.getDistrict() == null){
                        currentAddress.setDistrict(new District());
                    }
                    currentAddress.setProvince(provinceDAO.findOneByCriteria(Restrictions.eq("name",corporateModel.getProvince())));
                    if(currentAddress.getProvince() == null){
                        currentAddress.setProvince(new Province());
                    }
                    currentAddress.setPostalCode(corporateModel.getPostcode());
                    currentAddress.setCountry(countryDAO.findOneByCriteria(Restrictions.eq("code2",corporateModel.getCountryCode())));
                    if(currentAddress.getCountry() == null){
                        currentAddress.setCountry(new Country());
                    }
                    customerInfoView.setCurrentAddress(currentAddress);

                    AddressView registrationAddress = new AddressView();
                    registrationAddress.setSubDistrict(subDistrictDAO.findOneByCriteria(Restrictions.eq("name",corporateModel.getRegistrationAddress().getSubdistrict())));
                    if(registrationAddress.getSubDistrict() == null){
                        registrationAddress.setSubDistrict(new SubDistrict());
                    }
                    registrationAddress.setDistrict(districtDAO.findOneByCriteria(Restrictions.eq("name",corporateModel.getRegistrationAddress().getDistrict())));
                    if(registrationAddress.getDistrict() == null){
                        registrationAddress.setDistrict(new District());
                    }
                    registrationAddress.setProvince(provinceDAO.findOneByCriteria(Restrictions.eq("name",corporateModel.getRegistrationAddress().getProvince())));
                    if(registrationAddress.getProvince() == null){
                        registrationAddress.setProvince(new Province());
                    }
                    registrationAddress.setCountry(countryDAO.findOneByCriteria(Restrictions.eq("code2",corporateModel.getRegistrationAddress().getCountryCode())));
                    if(registrationAddress.getCountry() == null){
                        registrationAddress.setCountry(new Country());
                    }
                    registrationAddress.setPhoneNumber(corporateModel.getRegistrationAddress().getPhoneNo());
                    registrationAddress.setExtension(corporateModel.getRegistrationAddress().getExtension());
                    registrationAddress.setContactName(corporateModel.getRegistrationAddress().getContactName());
                    registrationAddress.setContactPhone(corporateModel.getRegistrationAddress().getContactPhoneNo());
                    customerInfoView.setRegisterAddress(registrationAddress);

                    customerInfoResultView.setCustomerInfoView(customerInfoView);
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
                            accountList.add(customerAccountListModel.getAccountNo());
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
