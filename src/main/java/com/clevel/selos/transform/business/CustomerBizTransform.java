package com.clevel.selos.transform.business;

import com.clevel.selos.dao.ext.map.RMTitleDAO;
import com.clevel.selos.dao.master.*;
import com.clevel.selos.integration.corebanking.model.corporateInfo.CorporateModel;
import com.clevel.selos.integration.corebanking.model.corporateInfo.CorporateResult;
import com.clevel.selos.integration.corebanking.model.customeraccount.CustomerAccountListModel;
import com.clevel.selos.integration.corebanking.model.customeraccount.CustomerAccountResult;
import com.clevel.selos.integration.corebanking.model.individualInfo.IndividualModel;
import com.clevel.selos.integration.corebanking.model.individualInfo.IndividualResult;
import com.clevel.selos.model.ActionResult;
import com.clevel.selos.model.Gender;
import com.clevel.selos.model.db.ext.map.RMTitle;
import com.clevel.selos.model.db.master.*;
import com.clevel.selos.model.view.AddressView;
import com.clevel.selos.model.view.CustomerAccountView;
import com.clevel.selos.model.view.CustomerInfoResultView;
import com.clevel.selos.model.view.CustomerInfoView;
import com.clevel.selos.util.DateTimeUtil;
import com.clevel.selos.util.Util;
import org.hibernate.criterion.Restrictions;

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
    @Inject
    RMTitleDAO rmTitleDAO;

    public CustomerInfoResultView tranformIndividual(IndividualResult individualResult) {
        CustomerInfoResultView customerInfoResultView = null;
        if (individualResult != null) {
            customerInfoResultView = new CustomerInfoResultView();
            if (individualResult.getActionResult() == ActionResult.SUCCESS) {
                customerInfoResultView.setActionResult(individualResult.getActionResult());
                customerInfoResultView.setCustomerId(individualResult.getCustomerId());
                if (individualResult.getIndividualModel() != null) {
                    IndividualModel individualModel = individualResult.getIndividualModel();
                    CustomerInfoView customerInfoView = new CustomerInfoView();

                    customerInfoView.reset();

                    customerInfoView.setCitizenId(individualModel.getCitizenID());
                    RMTitle rmTitle = rmTitleDAO.findOneByCriteria(Restrictions.eq("rmTitle", individualModel.getTitleTH()));
                    if (rmTitle != null && rmTitle.getTitle() != null) {
                        customerInfoView.setTitleTh(rmTitle.getTitle());
                    }

                    if (customerInfoView.getTitleTh() == null) {
                        customerInfoView.setTitleTh(new Title());
                    }

                    RMTitle rmTitleEn = rmTitleDAO.findOneByCriteria(Restrictions.eq("rmTitle", individualModel.getTitleEN()));
                    if (rmTitleEn != null && rmTitleEn.getTitle() != null) {
                        customerInfoView.setTitleEn(rmTitleEn.getTitle());
                    }

                    if (customerInfoView.getTitleEn() == null) {
                        customerInfoView.setTitleEn(new Title());
                    }

                    customerInfoView.setFirstNameTh(individualModel.getFirstname());
                    customerInfoView.setLastNameTh(individualModel.getLastname());
                    customerInfoView.setFirstNameEn(individualModel.getFirstnameEN()); //add en name
                    customerInfoView.setLastNameEn(individualModel.getLastnameEN());
                    customerInfoView.setTmbCustomerId(individualModel.getTmbCusID());
                    customerInfoView.setDocumentType(documentTypeDAO.findOneByCriteria(Restrictions.eq("documentTypeCode", individualModel.getDocumentType())));
                    customerInfoView.setCustomerEntity(customerEntityDAO.findById(1));
                    if (customerInfoView.getDocumentType() == null) {
                        customerInfoView.setDocumentType(new DocumentType());
                    }
                    log.debug("CustomerBizTransform ::: documentExpiredDate : {}", individualModel.getDocumentExpiredDate());
                    if (!"00/00/0000".equalsIgnoreCase(individualModel.getDocumentExpiredDate())) {
                        customerInfoView.setDocumentExpiredDate(DateTimeUtil.parseToDate(individualModel.getDocumentExpiredDate()));
                        log.debug("CustomerBizTransform ::: documentExpiredDate parseDate : {}", DateTimeUtil.parseToDate(individualModel.getDocumentExpiredDate()));
                    } else {
                        customerInfoView.setDocumentExpiredDate(null);
                    }

                    log.debug("CustomerBizTransform ::: getDateOfBirth : {}", individualModel.getDateOfBirth());
                    if (!"00/00/0000".equalsIgnoreCase(individualModel.getDateOfBirth())) {
                        customerInfoView.setDateOfBirth(DateTimeUtil.parseToDate(individualModel.getDateOfBirth()));
                        log.debug("CustomerBizTransform ::: getDateOfBirth parseDate : {}", DateTimeUtil.parseToDate(individualModel.getDateOfBirth()));
                    } else {
                        customerInfoView.setDateOfBirth(null);
                    }

                    if (individualModel.getGender().equals("M")) {
                        customerInfoView.setGender(Gender.MALE.value());
                    } else if (individualModel.getGender().equals("F")) {
                        customerInfoView.setGender(Gender.FEMALE.value());
                    }
                    customerInfoView.setEducation(educationDAO.findOneByCriteria(Restrictions.eq("code", individualModel.getEducationBackground())));
                    if (customerInfoView.getEducation() == null) {
                        customerInfoView.setEducation(new Education());
                    }
                    customerInfoView.setOrigin(raceDAO.findOneByCriteria(Restrictions.eq("code", individualModel.getRace())));
                    if (customerInfoView.getOrigin() == null) {
                        customerInfoView.setOrigin(new Race());
                    }
                    customerInfoView.setMaritalStatus(maritalStatusDAO.findOneByCriteria(Restrictions.eq("code", individualModel.getMarriageStatus())));
                    if (customerInfoView.getMaritalStatus() == null) {
                        customerInfoView.setMaritalStatus(new MaritalStatus());
                    }
                    customerInfoView.setNationality(nationalityDAO.findOneByCriteria(Restrictions.eq("code", individualModel.getNationality())));
                    if (customerInfoView.getNationality() == null) {
                        customerInfoView.setNationality(new Nationality());
                    }
                    //TODO Check Null before Casting
                    if (!Util.isEmpty(individualModel.getNumberOfChild())) {
                        customerInfoView.setNumberOfChild(new Integer(individualModel.getNumberOfChild()));
                    }
                    if (!Util.isEmpty(individualModel.getOccupationCode())) {
                        if (individualModel.getOccupationCode().matches("[0-9]*")) {
                            customerInfoView.setOccupation(occupationDAO.findOneByCriteria(Restrictions.eq("code", new Integer(individualModel.getOccupationCode()))));
                            if (customerInfoView.getOccupation() == null) {
                                customerInfoView.setOccupation(new Occupation());
                            }
                        } else {
                            customerInfoView.setOccupation(new Occupation());
                        }
                    } else {
                        customerInfoView.setOccupation(new Occupation());
                    }

                    CustomerInfoView spouse = new CustomerInfoView();
                    spouse.reset();
                    spouse.setFirstNameTh(individualModel.getSpouse().getFirstname());
                    spouse.setLastNameTh(individualModel.getSpouse().getLastname());
                    spouse.setCitizenId(individualModel.getSpouse().getCitizenID());
                    //spouse.setDateOfBirth(Util.convertStringToDateBuddhist(individualModel.getSpouse().getDateOfBirth()));
                    log.debug("CustomerBizTransform ::: spouse DateOfBirth : {}", individualModel.getSpouse().getDateOfBirth());
                    if (!"00/00/0000".equalsIgnoreCase(individualModel.getSpouse().getDateOfBirth())) {
                        spouse.setDateOfBirth(DateTimeUtil.parseToDate(individualModel.getSpouse().getDateOfBirth()));
                        log.debug("CustomerBizTransform ::: spouse DateOfBirth parse date : {}", DateTimeUtil.parseToDate(individualModel.getSpouse().getDateOfBirth()));
                    } else {
                        spouse.setDateOfBirth(null);
                    }
                    customerInfoView.setSpouse(spouse);


                    //=========================================== ValidatePhoneNumber
                    String mobileNumber1 = "";
                    String mobileNumber2 = "";
                    String faxNumber = "";
                    String workPhoneNumber = "";
                    String workPhoneExtension = "";
                    String currentPhoneNumber = "";
                    String currentPhoneExtension = "";
                    String homePhoneNumber = "";
                    String homePhoneExtension = "";

                    if (individualModel.getTelephoneNumber1() != null) {
                        //If Telephone type 1  = M move telephoneNumber1 to Mobile (1)
                        if (individualModel.getTelephoneNumber1().getTelephoneType() != null && individualModel.getTelephoneNumber1().getTelephoneType().equals("M")) {
                            mobileNumber1 = individualModel.getTelephoneNumber1().getTelephoneNumber();
                        }
                        // If Telephone type 1 = B move telephoneNumber1 to Working Address- Contact Number move extension1 to Working Address - Ext Number
                        if (individualModel.getTelephoneNumber1().getTelephoneType() != null && individualModel.getTelephoneNumber1().getTelephoneType().equals("B")) {
                            workPhoneNumber = individualModel.getTelephoneNumber1().getTelephoneNumber();
                            workPhoneExtension = individualModel.getTelephoneNumber1().getExtension();
                        }
                        //If Telephone type 1 = R move telephoneNumber1 to Personal Information-Home Number move extension1 to Personal Information-Ext Number
                        if (individualModel.getTelephoneNumber1().getTelephoneType() != null && individualModel.getTelephoneNumber1().getTelephoneType().equals("R")) {
//                            homePhoneNumber=
                        }
                    }

                    if (individualModel.getTelephoneNumber2() != null) {
                        //If Telephone type 2 = M : If no value in Mobile (1) , move telephoneNumber2 to Mobile (1),else move telephoneNumber2 to Mobile (2)
                        if (individualModel.getTelephoneNumber2().getTelephoneType() != null && individualModel.getTelephoneNumber2().getTelephoneType().equals("M")) {
                            if (mobileNumber1 == null || "".equalsIgnoreCase(mobileNumber1)) {
                                mobileNumber1 = individualModel.getTelephoneNumber2().getTelephoneNumber();
                            } else {
                                mobileNumber2 = individualModel.getTelephoneNumber2().getTelephoneNumber();            //****************
                            }
                        }
                        //If Telephone type 2 = B if no value in Working Address- Contact Number , move telephoneNumber2 to Working Address- Contact Number move extension2 to Working Address - Ext Number
                        if (individualModel.getTelephoneNumber2().getTelephoneType() != null && individualModel.getTelephoneNumber2().getTelephoneType().equals("B")) {
                            if (workPhoneNumber == null || "".equalsIgnoreCase(workPhoneNumber)) {
                                workPhoneNumber = individualModel.getTelephoneNumber2().getTelephoneNumber();
                                workPhoneExtension = individualModel.getTelephoneNumber2().getExtension();
                            }
                        }
                        // If Telephone type 2 = R , if no value in Personal Information-Home Number ,  move telephoneNumber2 to  Personal Information-Home Number move extension2 to Personal Information-Ext Number
                        if (individualModel.getTelephoneNumber2().getTelephoneType() != null && individualModel.getTelephoneNumber2().getTelephoneType().equals("R")) {

                        }
                    }

                    if (individualModel.getTelephoneNumber3() != null) {
                        //If Telephone type 3 = M : If no value in Mobile (1) , move telephoneNumber3 to Mobile (1),else move telephoneNumber3 to Mobile (2)
                        if (individualModel.getTelephoneNumber3().getTelephoneType() != null && individualModel.getTelephoneNumber3().getTelephoneType().equals("M")) {
                            if (mobileNumber1 == null || "".equalsIgnoreCase(mobileNumber1)) {
                                mobileNumber1 = individualModel.getTelephoneNumber3().getTelephoneNumber();
                            } else {
                                mobileNumber2 = individualModel.getTelephoneNumber3().getTelephoneNumber();            //****************
                            }
                        }

                        //If Telephone type 3 = B if no value in Working Address- Contact Number , move telephoneNumber3 to Working Address- Contact Number move extension3 to Working Address - Ext Number
                        if (individualModel.getTelephoneNumber3().getTelephoneType() != null && individualModel.getTelephoneNumber3().getTelephoneType().equals("B")) {
                            if (workPhoneNumber == null || "".equalsIgnoreCase(workPhoneNumber)) {
                                workPhoneNumber = individualModel.getTelephoneNumber3().getTelephoneNumber();
                                workPhoneExtension = individualModel.getTelephoneNumber3().getExtension();
                            }
                        }
                        // If Telephone type 3 = R , if no value in Personal Information-Home Number ,  move telephoneNumber3 to  Personal Information-Home Number move extension3 to Personal Information-Ext Number
                        if (individualModel.getTelephoneNumber3().getTelephoneType() != null && individualModel.getTelephoneNumber3().getTelephoneType().equals("R")) {

                        }
                    }

                    if (individualModel.getTelephoneNumber4() != null) {
                        //If Telephone type 4 = M : If no value in Mobile (1) , move telephoneNumber4 to Mobile (1),else move telephoneNumber4 to Mobile (2)
                        if (individualModel.getTelephoneNumber4().getTelephoneType() != null && individualModel.getTelephoneNumber4().getTelephoneType().equals("M")) {
                            if (mobileNumber1 == null || "".equalsIgnoreCase(mobileNumber1)) {
                                mobileNumber1 = individualModel.getTelephoneNumber4().getTelephoneNumber();
                            } else {
                                mobileNumber2 = individualModel.getTelephoneNumber4().getTelephoneNumber();            //****************
                            }
                        }
                        //If Telephone type 4 = B if no value in Working Address- Contact Number , move telephoneNumber4 to Working Address- Contact Number move extension4 to Working Address - Ext Number
                        if (individualModel.getTelephoneNumber4().getTelephoneType() != null && individualModel.getTelephoneNumber4().getTelephoneType().equals("B")) {
                            if (workPhoneNumber == null || "".equalsIgnoreCase(workPhoneNumber)) {
                                workPhoneNumber = individualModel.getTelephoneNumber4().getTelephoneNumber();
                                workPhoneExtension = individualModel.getTelephoneNumber4().getExtension();
                            }
                        }
                        // If Telephone type 4 = R , if no value in Personal Information-Home Number ,  move telephoneNumber4 to  Personal Information-Home Number move extension4 to Personal Information-Ext Number
                        if (individualModel.getTelephoneNumber4().getTelephoneType() != null && individualModel.getTelephoneNumber4().getTelephoneType().equals("R")) {

                        }
                    }

                    customerInfoView.setMobileNumber(mobileNumber1);
                    customerInfoView.setFaxNumber(faxNumber);

                    //Workaddress
                    AddressView workAddress = new AddressView();
                    workAddress.setAddressNo(individualModel.getWorkAddress().getAddressNo());
                    workAddress.setMoo(individualModel.getWorkAddress().getAddressMoo());
                    workAddress.setBuilding(individualModel.getWorkAddress().getAddressBuilding());
                    workAddress.setRoad(individualModel.getWorkAddress().getAddressStreet());
                    workAddress.setProvince(provinceDAO.getByName(individualModel.getWorkAddress().getProvince()));
                    if (workAddress.getProvince() == null || workAddress.getProvince() != null && workAddress.getProvince().getCode() == 0) {
                        workAddress.setProvince(new Province());
                        workAddress.setDistrict(new District());
                        workAddress.setSubDistrict(new SubDistrict());
                    } else {
                        workAddress.setDistrict(districtDAO.getByNameAndProvince(individualModel.getWorkAddress().getDistrict(), workAddress.getProvince()));
                        if (workAddress.getDistrict() == null || workAddress.getDistrict() != null && workAddress.getDistrict().getId() == 0) {
                            workAddress.setDistrict(new District());
                        } else {
                            workAddress.setSubDistrict(subDistrictDAO.getByNameAndDistrict(individualModel.getWorkAddress().getSubdistrict(), workAddress.getDistrict()));
                            if (workAddress.getSubDistrict() == null) {
                                workAddress.setSubDistrict(new SubDistrict());
                            }
                        }
                    }
                    workAddress.setAddressType(addressTypeDAO.findById(3));
                    workAddress.setPostalCode(individualModel.getWorkAddress().getPostcode());
                    workAddress.setCountry(countryDAO.findOneByCriteria(Restrictions.eq("code2", individualModel.getWorkAddress().getCountryCode())));
                    if (workAddress.getCountry() == null) {
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
                    currentAddress.setProvince(provinceDAO.getByName(individualModel.getCurrentAddress().getProvince()));
                    if (currentAddress.getProvince() == null || currentAddress.getProvince() != null && currentAddress.getProvince().getCode() == 0) {
                        currentAddress.setProvince(new Province());
                        currentAddress.setDistrict(new District());
                        currentAddress.setSubDistrict(new SubDistrict());
                    } else {
                        currentAddress.setDistrict(districtDAO.getByNameAndProvince(individualModel.getCurrentAddress().getDistrict(), currentAddress.getProvince()));
                        if (currentAddress.getDistrict() == null || currentAddress.getDistrict() != null && currentAddress.getDistrict().getId() == 0) {
                            currentAddress.setDistrict(new District());
                        } else {
                            currentAddress.setSubDistrict(subDistrictDAO.getByNameAndDistrict(individualModel.getCurrentAddress().getSubdistrict(), currentAddress.getDistrict()));
                            if (currentAddress.getSubDistrict() == null) {
                                currentAddress.setSubDistrict(new SubDistrict());
                            }
                        }
                    }
                    currentAddress.setAddressType(addressTypeDAO.findById(1));
                    currentAddress.setPostalCode(individualModel.getCurrentAddress().getPostcode());
                    currentAddress.setCountry(countryDAO.findOneByCriteria(Restrictions.eq("code2", individualModel.getCurrentAddress().getCountryCode())));
                    if (currentAddress.getCountry() == null) {
                        currentAddress.setCountry(new Country());
                    }
                    currentAddress.setPhoneNumber(currentPhoneNumber);
                    currentAddress.setExtension(currentPhoneExtension);
                    customerInfoView.setCurrentAddress(currentAddress);

                    //HomeAddress
                    AddressView homeAddress = new AddressView();
                    homeAddress.setAddressNo(individualModel.getHomeAddress().getAddressNo());
                    homeAddress.setMoo(individualModel.getHomeAddress().getAddressMoo());
                    homeAddress.setBuilding(individualModel.getHomeAddress().getAddressBuilding());
                    homeAddress.setRoad(individualModel.getHomeAddress().getAddressStreet());
                    homeAddress.setProvince(provinceDAO.getByName(individualModel.getHomeAddress().getProvince()));
                    if (homeAddress.getProvince() == null || homeAddress.getProvince() != null && homeAddress.getProvince().getCode() == 0) {
                        homeAddress.setProvince(new Province());
                        homeAddress.setDistrict(new District());
                        homeAddress.setSubDistrict(new SubDistrict());
                    } else {
                        homeAddress.setDistrict(districtDAO.getByNameAndProvince(individualModel.getHomeAddress().getDistrict(), homeAddress.getProvince()));
                        if (homeAddress.getDistrict() == null || homeAddress.getDistrict() != null && homeAddress.getDistrict().getId() == 0) {
                            homeAddress.setDistrict(new District());
                        } else {
                            homeAddress.setSubDistrict(subDistrictDAO.getByNameAndDistrict(individualModel.getHomeAddress().getSubdistrict(), homeAddress.getDistrict()));
                            if (homeAddress.getSubDistrict() == null) {
                                homeAddress.setSubDistrict(new SubDistrict());
                            }
                        }
                    }
                    homeAddress.setAddressType(addressTypeDAO.findById(2));
                    homeAddress.setPostalCode(individualModel.getHomeAddress().getPostcode());
                    homeAddress.setCountry(countryDAO.findOneByCriteria(Restrictions.eq("code2", individualModel.getHomeAddress().getCountryCode())));
                    if (homeAddress.getCountry() == null) {
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

    public CustomerInfoResultView tranformJuristic(CorporateResult corporateResult) {
        CustomerInfoResultView customerInfoResultView = null;
        if (corporateResult != null) {
            customerInfoResultView = new CustomerInfoResultView();
            if (corporateResult.getActionResult() == ActionResult.SUCCESS) {
                customerInfoResultView.setActionResult(corporateResult.getActionResult());
                customerInfoResultView.setCustomerId(corporateResult.getCustomerId());
                if (corporateResult.getCorporateModel() != null) {
                    CorporateModel corporateModel = corporateResult.getCorporateModel();
                    CustomerInfoView customerInfoView = new CustomerInfoView();

                    customerInfoView.reset();

                    customerInfoView.setTmbCustomerId(corporateModel.getTmbCusID());

                    RMTitle rmTitle = rmTitleDAO.findOneByCriteria(Restrictions.eq("rmTitle", corporateModel.getTitleTH()));
                    if (rmTitle != null && rmTitle.getTitle() != null) {
                        customerInfoView.setTitleTh(rmTitle.getTitle());
                    }

                    if (customerInfoView.getTitleTh() == null) {
                        customerInfoView.setTitleTh(new Title());
                    }
                    customerInfoView.setFirstNameTh(corporateModel.getCompanyNameTH());
                    customerInfoView.setFirstNameEn(corporateModel.getCompanyNameEN());
                    customerInfoView.setRegistrationId(corporateModel.getRegistrationID());
                    customerInfoView.setDocumentType(documentTypeDAO.findOneByCriteria(Restrictions.eq("documentTypeCode", corporateModel.getDocumentType())));
                    if (customerInfoView.getDocumentType() == null) {
                        customerInfoView.setDocumentType(new DocumentType());
                    }
                    customerInfoView.setCustomerEntity(customerEntityDAO.findById(2));

                    log.debug("CustomerBizTransform ::: registrationDate : {}", corporateModel.getRegistrationDate());
                    if (!"00/00/0000".equalsIgnoreCase(corporateModel.getRegistrationDate())) {
                        customerInfoView.setDateOfRegister(DateTimeUtil.parseToDate(corporateModel.getRegistrationDate()));
                        log.debug("CustomerBizTransform ::: registrationDate parse date : {}", DateTimeUtil.parseToDate(corporateModel.getRegistrationDate()));
                    } else {
                        customerInfoView.setDateOfRegister(null);
                    }

                    customerInfoView.setRegistrationCountry(countryDAO.findOneByCriteria(Restrictions.eq("code2", corporateModel.getRegistrationCountry())));
                    if (customerInfoView.getRegistrationCountry() == null) {
                        customerInfoView.setRegistrationCountry(new Country());
                    }

                    //CurrentAddress
                    AddressView currentAddress = new AddressView();
                    currentAddress.setAddressNo(corporateModel.getAddressNo());
                    currentAddress.setMoo(corporateModel.getAddressMoo());
                    currentAddress.setBuilding(corporateModel.getAddressBuilding());
                    currentAddress.setRoad(corporateModel.getAddressStreet());
                    currentAddress.setProvince(provinceDAO.getByName(corporateModel.getProvince()));
                    if (currentAddress.getProvince() == null || currentAddress.getProvince() != null && currentAddress.getProvince().getCode() == 0) {
                        currentAddress.setProvince(new Province());
                        currentAddress.setDistrict(new District());
                        currentAddress.setSubDistrict(new SubDistrict());
                    } else {
                        currentAddress.setDistrict(districtDAO.getByNameAndProvince(corporateModel.getDistrict(), currentAddress.getProvince()));
                        if (currentAddress.getDistrict() == null || currentAddress.getDistrict() != null && currentAddress.getDistrict().getId() == 0) {
                            currentAddress.setDistrict(new District());
                        } else {
                            currentAddress.setSubDistrict(subDistrictDAO.getByNameAndDistrict(corporateModel.getSubdistrict(), currentAddress.getDistrict()));
                            if (currentAddress.getSubDistrict() == null) {
                                currentAddress.setSubDistrict(new SubDistrict());
                            }
                        }
                    }
                    currentAddress.setPostalCode(corporateModel.getPostcode());
                    currentAddress.setCountry(countryDAO.findOneByCriteria(Restrictions.eq("code2", corporateModel.getCountryCode())));
                    if (currentAddress.getCountry() == null) {
                        currentAddress.setCountry(new Country());
                    }
                    currentAddress.setAddressType(addressTypeDAO.findById(1));
                    customerInfoView.setCurrentAddress(currentAddress);

                    AddressView registrationAddress = new AddressView();
                    registrationAddress.setAddressNo(corporateModel.getRegistrationAddress().getAddressNo());
                    registrationAddress.setMoo(corporateModel.getRegistrationAddress().getAddressMoo());
                    registrationAddress.setBuilding(corporateModel.getRegistrationAddress().getAddressBuilding());
                    registrationAddress.setRoad(corporateModel.getRegistrationAddress().getAddressStreet());
                    registrationAddress.setProvince(provinceDAO.getByName(corporateModel.getProvince()));
                    if (registrationAddress.getProvince() == null || registrationAddress.getProvince() != null && registrationAddress.getProvince().getCode() == 0) {
                        registrationAddress.setProvince(new Province());
                        registrationAddress.setDistrict(new District());
                        registrationAddress.setSubDistrict(new SubDistrict());
                    } else {
                        registrationAddress.setDistrict(districtDAO.getByNameAndProvince(corporateModel.getDistrict(), registrationAddress.getProvince()));
                        if (registrationAddress.getDistrict() == null || registrationAddress.getDistrict() != null && registrationAddress.getDistrict().getId() == 0) {
                            registrationAddress.setDistrict(new District());
                        } else {
                            registrationAddress.setSubDistrict(subDistrictDAO.getByNameAndDistrict(corporateModel.getSubdistrict(), registrationAddress.getDistrict()));
                            if (registrationAddress.getSubDistrict() == null) {
                                registrationAddress.setSubDistrict(new SubDistrict());
                            }
                        }
                    }
                    registrationAddress.setCountry(countryDAO.findOneByCriteria(Restrictions.eq("code2", corporateModel.getRegistrationAddress().getCountryCode())));
                    if (registrationAddress.getCountry() == null) {
                        registrationAddress.setCountry(new Country());
                    }
                    registrationAddress.setPhoneNumber(corporateModel.getRegistrationAddress().getPhoneNo());
                    registrationAddress.setExtension(corporateModel.getRegistrationAddress().getExtension());
                    registrationAddress.setContactName(corporateModel.getRegistrationAddress().getContactName());
                    registrationAddress.setContactPhone(corporateModel.getRegistrationAddress().getContactPhoneNo());
                    registrationAddress.setAddressType(addressTypeDAO.findById(2));
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

    public CustomerAccountView transformCustomerAccount(CustomerAccountResult customerAccountResult) {
        log.debug("transformCustomerAccount()");
        CustomerAccountView customerAccountView = null;
        if (customerAccountResult != null) {
            customerAccountView = new CustomerAccountView();
            if (customerAccountResult.getActionResult() == ActionResult.SUCCESS) {
                customerAccountView.setActionResult(customerAccountResult.getActionResult());
                customerAccountView.setCustomerId(customerAccountResult.getCustomerId());
                if (customerAccountResult.getAccountListModels() != null && customerAccountResult.getAccountListModels().size() > 0) {
                    List<String> accountList = new ArrayList<String>();
                    int resultRow=0;
                    for (CustomerAccountListModel customerAccountListModel : customerAccountResult.getAccountListModels()) {
                        if (!Util.isEmpty(customerAccountListModel.getAccountNo())) {
                            //check Appl = IM
                            if (customerAccountListModel.getAppl() != null) {
                                if (customerAccountListModel.getAppl().equals("IM")) {
                                    resultRow++;
                                    log.debug("TransformAccountListData: {}",customerAccountListModel.toString());
                                    accountList.add(customerAccountListModel.getAccountNo());
                                }
                                //check Appl = ST
                                if (customerAccountListModel.getAppl().equals("ST")) {
                                    if (customerAccountListModel.getCtl4() != null) {
                                        if (customerAccountListModel.getCtl4().equals("0200")) {
                                            if (customerAccountListModel.getAccountNo().length() >= 4) {
                                                resultRow++;
                                                log.debug("TransformAccountListData: {}",customerAccountListModel.toString());
                                                accountList.add(customerAccountListModel.getAccountNo().substring(4));
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                    log.debug("TransformAccountListSize: {}",resultRow);
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
