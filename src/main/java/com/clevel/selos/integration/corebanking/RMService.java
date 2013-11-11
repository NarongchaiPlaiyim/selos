package com.clevel.selos.integration.corebanking;

import com.clevel.selos.exception.RMInterfaceException;
import com.clevel.selos.exception.ValidationException;
import com.clevel.selos.integration.RM;
import com.clevel.selos.integration.corebanking.model.SearchIndividual;
import com.clevel.selos.integration.corebanking.model.corporateInfo.CorporateModel;
import com.clevel.selos.integration.corebanking.model.corporateInfo.RegistrationAddress;
import com.clevel.selos.integration.corebanking.model.customeraccount.CustomerAccountListModel;
import com.clevel.selos.integration.corebanking.model.customeraccount.CustomerAccountResult;
import com.clevel.selos.integration.corebanking.model.customeraccount.SearchCustomerAccountModel;
import com.clevel.selos.integration.corebanking.model.individualInfo.ContactDetails;
import com.clevel.selos.integration.corebanking.model.individualInfo.IndividualModel;
import com.clevel.selos.integration.corebanking.model.individualInfo.Spouse;
import com.clevel.selos.integration.corebanking.model.individualInfo.Telephone;
import com.clevel.selos.model.ActionResult;
import com.clevel.selos.system.Config;
import com.clevel.selos.system.audit.SystemAuditor;
import com.clevel.selos.system.message.*;
import com.clevel.selos.util.Util;
import com.clevel.selos.util.ValidationUtil;
import com.sun.xml.internal.ws.client.BindingProviderProperties;
import com.tmb.common.data.eaisearchcustomeraccount.EAISearchCustomerAccount;
import com.tmb.common.data.eaisearchcustomeraccount.EAISearchCustomerAccount_Service;
import com.tmb.common.data.eaisearchindividualcustomercm.EAISearchIndividualCustomerCM;
import com.tmb.common.data.eaisearchindividualcustomercm.EAISearchIndividualCustomerCM_Service;
import com.tmb.common.data.requestsearchcustomeraccount.ReqSearchCustomerAccount;
import com.tmb.common.data.requestsearchindividualcustomer.Body;
import com.tmb.common.data.requestsearchindividualcustomer.Header;
import com.tmb.common.data.requestsearchindividualcustomer.ReqSearchIndividualCustomer;
import com.tmb.common.data.responsesearchcustomeraccount.ResSearchCustomerAccount;
import com.tmb.common.data.responsesearchindividualcustomer.ResSearchIndividualCustomer;
import com.tmb.sme.data.eaisearchcorporatecustomer.EAISearchCorporateCustomer;
import com.tmb.sme.data.eaisearchcorporatecustomer.EAISearchCorporateCustomer_Service;
import com.tmb.sme.data.requestsearchcorporatecustomer.ReqSearchCorporateCustomer;
import com.tmb.sme.data.responsesearchcorporatecustomer.ResSearchCorporateCustomer;
import org.slf4j.Logger;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;
import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RMService implements Serializable {
    @Inject
    @RM
    Logger log;

    @Inject
    @ExceptionMessage
    Message exceptionMsg;

    @Inject
    @ValidationMessage
    Message validationMsg;


    @Inject
    @Config(name = "interface.rm.individual.address")
    String individualAddress;
    @Inject
    @Config(name = "interface.rm.juristic.address")
    String corporateAddress;
    @Inject
    @Config(name = "interface.rm.customerAccount.address")
    String customerAccountAddress;

    @Inject
    @Config(name = "interface.rm.customerAccount.serverURL")
    String customerServerUrl;

    @Inject
    @Config(name = "interface.rm.customerAccount.sessionId")
    String customerSessionId;

    @Inject
    @Config(name = "interface.rm.individual.connectTimeout")
    String individualConnectTimeout;
    @Inject
    @Config(name = "interface.rm.juristic.connectTimeout")
    String juristicConnectTimeout;
    @Inject
    @Config(name = "interface.rm.customerAccount.connectTimeout")
    String cusAccountConnectTimeout;

    @Inject
    @Config(name = "interface.rm.individual.requestTimeout")
    String individualRequestTimeout;
    @Inject
    @Config(name = "interface.rm.juristic.requestTimeout")
    String juristicRequestTimeout;
    @Inject
    @Config(name = "interface.rm.customerAccount.requestTimeout")
    String cusAccountRequestTimeout;

    @Inject
    @Config(name = "interface.rm.replace.blank")
    String blank;

    @Inject
    @RM
    SystemAuditor rmAuditor;

    @Inject
    public RMService() {
    }


    @PostConstruct
    public void onCreate() {

    }
     //todo change to CM
    public IndividualModel individualCMService(SearchIndividual searchIndividual, String userId) throws Exception {
        log.debug("IndividualService() START");
        IndividualModel individualModel = null;
        //System.out.println("============================== : " + blank);

        //Validate ReqId
        if (!ValidationUtil.isValueInRange(1, 50, searchIndividual.getReqId().length())) {
            if (searchIndividual.getReqId() == null || searchIndividual.getReqId().equals("")) {
                throw new ValidationException(ValidationMapping.DATA_REQUIRED, validationMsg.get(ValidationMapping.DATA_REQUIRED, "(reqId)"));
            } else {
                throw new ValidationException(ValidationMapping.FIELD_LENGTH_INVALID, validationMsg.get(ValidationMapping.FIELD_LENGTH_INVALID, "(reqId)"));
            }
        }
        //Validate CustType
        if (!ValidationUtil.isValueEqual(1, searchIndividual.getCustType().length())) {
            if (searchIndividual.getCustType() == null || searchIndividual.getCustType().equals("")) {
                throw new ValidationException(ValidationMapping.DATA_REQUIRED, validationMsg.get(ValidationMapping.DATA_REQUIRED, "(custType)"));
            } else {
                throw new ValidationException(ValidationMapping.FIELD_LENGTH_INVALID, validationMsg.get(ValidationMapping.FIELD_LENGTH_INVALID, "(custType)"));
            }
        }
        //Validate Type
        if (!ValidationUtil.isValueEqual(2, searchIndividual.getType().length())) {
            if (searchIndividual.getType() == null || searchIndividual.getType().equals("")) {
                throw new ValidationException(ValidationMapping.DATA_REQUIRED, validationMsg.get(ValidationMapping.DATA_REQUIRED, "(type)"));
            } else {
                throw new ValidationException(ValidationMapping.FIELD_LENGTH_INVALID, validationMsg.get(ValidationMapping.FIELD_LENGTH_INVALID, "(type)"));
            }
        }
        //Validate CustId
        if (ValidationUtil.isGreaterThan(25, searchIndividual.getCustId().length())) {
            throw new ValidationException(ValidationMapping.FIELD_LENGTH_INVALID, validationMsg.get(ValidationMapping.FIELD_LENGTH_INVALID, "(custId)"));
        }
        //Validate CustNbr
        if (ValidationUtil.isGreaterThan(14, searchIndividual.getCustNbr().length())) {
            throw new ValidationException(ValidationMapping.FIELD_LENGTH_INVALID, validationMsg.get(ValidationMapping.FIELD_LENGTH_INVALID, "(custNbr)"));
        }
        //Validate CustName
        if (ValidationUtil.isGreaterThan(40, searchIndividual.getCustName().length())) {
            throw new ValidationException(ValidationMapping.FIELD_LENGTH_INVALID, validationMsg.get(ValidationMapping.FIELD_LENGTH_INVALID, "(custName)"));
        }
        //Validate CustSurname
        if (ValidationUtil.isGreaterThan(40, searchIndividual.getCustSurname().length())) {
            throw new ValidationException(ValidationMapping.FIELD_LENGTH_INVALID, validationMsg.get(ValidationMapping.FIELD_LENGTH_INVALID, "(surname)"));
        }
        //Validate RadSelectSearch
        if (!ValidationUtil.isValueInRange(1, 10, searchIndividual.getRadSelectSearch().length())) {
            if (searchIndividual.getRadSelectSearch() == null || searchIndividual.getRadSelectSearch().equals("")) {
                throw new ValidationException(ValidationMapping.DATA_REQUIRED, validationMsg.get(ValidationMapping.DATA_REQUIRED, "(radSelectSearch)"));
            } else {
                throw new ValidationException(ValidationMapping.FIELD_LENGTH_INVALID, validationMsg.get(ValidationMapping.FIELD_LENGTH_INVALID, "(radSelectSearch)"));
            }
        }
        //Validate Acronym
        if (!ValidationUtil.isValueInRange(1, 20, searchIndividual.getAcronym().length())) {
            if (searchIndividual.getAcronym() == null || searchIndividual.getAcronym().equals("")) {
                throw new ValidationException(ValidationMapping.DATA_REQUIRED, validationMsg.get(ValidationMapping.DATA_REQUIRED, "(acronym)"));
            } else {
                throw new ValidationException(ValidationMapping.FIELD_LENGTH_INVALID, validationMsg.get(ValidationMapping.FIELD_LENGTH_INVALID, "(acronym)"));
            }
        }
        //Validate ProductCode
        if (!ValidationUtil.isValueInRange(1, 8, searchIndividual.getProductCode().length())) {
            if (searchIndividual.getProductCode() == null || searchIndividual.getProductCode().equals("")) {
                throw new ValidationException(ValidationMapping.DATA_REQUIRED, validationMsg.get(ValidationMapping.DATA_REQUIRED, "(productCode)"));
            } else {
                throw new ValidationException(ValidationMapping.FIELD_LENGTH_INVALID, validationMsg.get(ValidationMapping.FIELD_LENGTH_INVALID, "(productCode)"));
            }
        }
        log.debug("Validate Pass!");

        Header header = new Header();
        header.setReqId(searchIndividual.getReqId());
        header.setAcronym(searchIndividual.getAcronym());
        header.setProductCode(searchIndividual.getProductCode());
//        header.setServerURL();
//        header.setSessionId();

        Body body = new Body();
        body.setCustType(searchIndividual.getCustType());
        body.setType(searchIndividual.getType());
        body.setCustId(searchIndividual.getCustId());
        body.setCustNbr(searchIndividual.getCustNbr());
        body.setCustName(searchIndividual.getCustName());
        body.setCustSurname(searchIndividual.getCustSurname());
        body.setRadSelectSearch(searchIndividual.getRadSelectSearch());


        ReqSearchIndividualCustomer reqSearch = new ReqSearchIndividualCustomer();
        reqSearch.setHeader(header);
        reqSearch.setBody(body);

        //actionDesc
        String actionDesc = "ReqID=" + reqSearch.getHeader().getReqId() + ",CustId=" + reqSearch.getBody().getCustId() + ",RedSelectSearch=" + reqSearch.getBody().getRadSelectSearch();

        //requestTime
        Date requestTime = new Date();
        String linkKey = Util.getLinkKey(userId);
        log.debug("LinkKey : {}", linkKey);
        log.debug("============================ Request ==============================");
        log.debug("requestServiceTime : {}", new Date());
        log.debug("requestHeaderData : {}", reqSearch.getHeader().toString());
        log.debug("requestBodyData : {}", reqSearch.getBody().toString());
        try {
            ResSearchIndividualCustomer resSearchIndividualCustomer = callServiceIndividualCM(reqSearch);
            if (resSearchIndividualCustomer != null) {
                //responseTime
                Date responseTime = new Date();
                log.debug("============================ Response ==============================");
                log.debug("responseServiceTime : {}", new Date());
                log.debug("responseHeaderData : {}", resSearchIndividualCustomer.getHeader().toString());

                if (resSearchIndividualCustomer.getBody() != null
                        && resSearchIndividualCustomer.getBody().getPersonalDetailSection() != null
                        && resSearchIndividualCustomer.getBody().getPersonalDetailSection().getPersonalDetail() != null) {
                    log.debug("responseBodyData : {}", resSearchIndividualCustomer.getBody().getPersonalDetailSection().getPersonalDetail().toString());
                }
                //Audit Data
                rmAuditor.add(userId, "individualService", actionDesc, requestTime, ActionResult.SUCCESS, "ResCode : "+resSearchIndividualCustomer.getHeader().getResCode()+
                        " , ProductCode : "+resSearchIndividualCustomer.getHeader().getProductCode(), responseTime, linkKey);

                //Check Success
                log.debug("requestServiceDescription : {}", resSearchIndividualCustomer.getHeader().getResDesc());
                if (resSearchIndividualCustomer.getHeader().getResCode().equals("0000")) {
                    individualModel = new IndividualModel();
                    //checkSearchResult
                    if (resSearchIndividualCustomer.getBody().getSearchResult().equals("CL")) {
                        throw new RMInterfaceException(ExceptionMapping.RM_CUSTOMER_RESULT_MULTIPLE, exceptionMsg.get(ExceptionMapping.RM_CUSTOMER_RESULT_MULTIPLE));
                    }
                    //personal detail session
                    individualModel.setTmbCusID(resSearchIndividualCustomer.getBody().getPersonalDetailSection().getPersonalDetail().getCustNbr());
                    individualModel.setTitleTH(resSearchIndividualCustomer.getBody().getPersonalDetailSection().getPersonalDetail().getTitle());
                    //spilt Name
                    if (resSearchIndividualCustomer.getBody().getPersonalDetailSection().getPersonalDetail().getName() != null) {
                        String name[] = resSearchIndividualCustomer.getBody().getPersonalDetailSection().getPersonalDetail().getName().split(" ");
                        int nameSize = name.length - 1;
                        if (nameSize >= 0) {
                            individualModel.setFirstname(name[0]);
                        }
                        if (nameSize >= 1) {
                            individualModel.setLastname(name[1]);
                        }
                    }
                    individualModel.setDocumentType(resSearchIndividualCustomer.getBody().getPersonalDetailSection().getPersonalDetail().getCustId());
                    individualModel.setCitizenID(resSearchIndividualCustomer.getBody().getPersonalDetailSection().getPersonalDetail().getCitizenId());
                    log.debug("=================================== {}", resSearchIndividualCustomer.getBody().getPersonalDetailSection().getPersonalDetail().getExpDt());
                    individualModel.setDocumentExpiredDate(resSearchIndividualCustomer.getBody().getPersonalDetailSection().getPersonalDetail().getExpDt());
                    individualModel.setCusType(resSearchIndividualCustomer.getBody().getPersonalDetailSection().getPersonalDetail().getCustType());
//
                    individualModel.setTelephoneNumber1(new Telephone(resSearchIndividualCustomer.getBody().getPersonalDetailSection().getPersonalDetail().getTelephoneNumber1()
                            , resSearchIndividualCustomer.getBody().getPersonalDetailSection().getPersonalDetail().getExtension1()
                            , resSearchIndividualCustomer.getBody().getPersonalDetailSection().getPersonalDetail().getTelephoneType1()));
                    individualModel.setTelephoneNumber2(new Telephone(resSearchIndividualCustomer.getBody().getPersonalDetailSection().getPersonalDetail().getTelephoneNumber2()
                            , resSearchIndividualCustomer.getBody().getPersonalDetailSection().getPersonalDetail().getExtension2()
                            , resSearchIndividualCustomer.getBody().getPersonalDetailSection().getPersonalDetail().getTelephoneType2()));
                    individualModel.setTelephoneNumber3(new Telephone(resSearchIndividualCustomer.getBody().getPersonalDetailSection().getPersonalDetail().getTelephoneNumber3()
                            , resSearchIndividualCustomer.getBody().getPersonalDetailSection().getPersonalDetail().getExtension3()
                            , resSearchIndividualCustomer.getBody().getPersonalDetailSection().getPersonalDetail().getTelephoneType3()));
                    individualModel.setTelephoneNumber4(new Telephone(resSearchIndividualCustomer.getBody().getPersonalDetailSection().getPersonalDetail().getTelephoneNumber4()
                            , resSearchIndividualCustomer.getBody().getPersonalDetailSection().getPersonalDetail().getExtension4()
                            , resSearchIndividualCustomer.getBody().getPersonalDetailSection().getPersonalDetail().getTelephoneType4()));

                    //
                    individualModel.setDateOfBirth(resSearchIndividualCustomer.getBody().getPersonalDetailSection().getPersonalDetail().getDateOfBirth());
                    individualModel.setGender(resSearchIndividualCustomer.getBody().getPersonalDetailSection().getPersonalDetail().getGender());
                    individualModel.setEducationBackground(resSearchIndividualCustomer.getBody().getPersonalDetailSection().getPersonalDetail().getEducationLevel());
                    individualModel.setRace(resSearchIndividualCustomer.getBody().getPersonalDetailSection().getPersonalDetail().getRace());
                    individualModel.setMarriageStatus(resSearchIndividualCustomer.getBody().getPersonalDetailSection().getPersonalDetail().getMaritalStatus());
                    individualModel.setNationality(resSearchIndividualCustomer.getBody().getPersonalDetailSection().getPersonalDetail().getIsoCitizenCtry());
                    individualModel.setNumberOfChild(resSearchIndividualCustomer.getBody().getPersonalDetailSection().getPersonalDetail().getNoOfChildren());
                    //spouse
                    String spouse1 = "", spouse2 = "";
                    if (resSearchIndividualCustomer.getBody().getPersonalDetailSection().getPersonalDetail().getSpouseName() != null) {
                        String spouseName[] = resSearchIndividualCustomer.getBody().getPersonalDetailSection().getPersonalDetail().getSpouseName().split(" ");
                        int spouseSize = spouseName.length - 1;
                        if (spouseSize >= 0) {
                            spouse1 = spouseName[0];
                        }
                        if (spouseSize >= 1) {
                            spouse2 = spouseName[1];
                        }
                    }

                    individualModel.setSpouse(new Spouse(spouse1, spouse2, resSearchIndividualCustomer.getBody().getPersonalDetailSection().getPersonalDetail().getSpouseTin()
                            , resSearchIndividualCustomer.getBody().getPersonalDetailSection().getPersonalDetail().getSpouseDateOfBirth()));

                    individualModel.setOccupationCode(resSearchIndividualCustomer.getBody().getPersonalDetailSection().getPersonalDetail().getOccupationCode1());
                    individualModel.setBizCode(resSearchIndividualCustomer.getBody().getPersonalDetailSection().getPersonalDetail().getBusType1());

                    individualModel.setTitleEN(resSearchIndividualCustomer.getBody().getPersonalDetailSection().getPersonalDetail().getTitleEng());
                    individualModel.setFirstnameEN(resSearchIndividualCustomer.getBody().getPersonalDetailSection().getPersonalDetail().getNameEng());
                    individualModel.setLastnameEN(resSearchIndividualCustomer.getBody().getPersonalDetailSection().getPersonalDetail().getNameEng());
                    //set HomeAddress
                    ContactDetails homeContactDetails = new ContactDetails();
                    if (resSearchIndividualCustomer.getBody().getPersonalDetailSection().getPersonalDetail().getResAddrLine1() != null) {
                        String homeAddressLine1[] = resSearchIndividualCustomer.getBody().getPersonalDetailSection().getPersonalDetail().getResAddrLine1().split(" ");
                        //validateSpiltSize
                        int homeAddressLineSize1 = homeAddressLine1.length - 1;
                        if (homeAddressLineSize1 >= 0) {
                            homeContactDetails.setAddressNo(homeAddressLine1[0]);
                        }
                        if (homeAddressLineSize1 >= 1) {
                            homeContactDetails.setAddressMoo(homeAddressLine1[1]);
                        }
                        if (homeAddressLineSize1 >= 2) {
                            homeContactDetails.setAddressBuilding(homeAddressLine1[2]);
                        }
                    }
                    homeContactDetails.setAddressStreet(resSearchIndividualCustomer.getBody().getPersonalDetailSection().getPersonalDetail().getResAddrLine2());
                    if (resSearchIndividualCustomer.getBody().getPersonalDetailSection().getPersonalDetail().getResAddrLine3() != null) {
                        String homeAddressLine3[] = resSearchIndividualCustomer.getBody().getPersonalDetailSection().getPersonalDetail().getResAddrLine3().split(" ");
                        //validateSpiltSize
                        int homeAddressLineSize3 = homeAddressLine3.length - 1;
                        if (homeAddressLineSize3 >= 0) {
                            homeContactDetails.setSubdistrict(Util.replaceToBlank(homeAddressLine3[0], blank));
                        }
                        if (homeAddressLineSize3 >= 1) {
                            homeContactDetails.setDistrict(Util.replaceToBlank(homeAddressLine3[1], blank));
                        }
                    }

                    homeContactDetails.setProvince(resSearchIndividualCustomer.getBody().getPersonalDetailSection().getPersonalDetail().getResCity());
                    homeContactDetails.setPostcode(resSearchIndividualCustomer.getBody().getPersonalDetailSection().getPersonalDetail().getResPostalCd());
                    homeContactDetails.setCountry(resSearchIndividualCustomer.getBody().getPersonalDetailSection().getPersonalDetail().getResCtry());
                    homeContactDetails.setCountryCode(resSearchIndividualCustomer.getBody().getPersonalDetailSection().getPersonalDetail().getResIsoCtryCode());
                    individualModel.setHomeAddress(homeContactDetails);

                    //set CurrentAddress
                    ContactDetails currentContactDetails = new ContactDetails();
                    if (resSearchIndividualCustomer.getBody().getPersonalDetailSection().getPersonalDetail().getRegAddrLine1() != null) {
                        String currentAddressLine1[] = resSearchIndividualCustomer.getBody().getPersonalDetailSection().getPersonalDetail().getRegAddrLine1().split(" ");
                        //validateSpiltSize
                        int currentAddressLineSize1 = currentAddressLine1.length - 1;
                        if (currentAddressLineSize1 >= 0) {
                            currentContactDetails.setAddressNo(currentAddressLine1[0]);
                        }
                        if (currentAddressLineSize1 >= 1) {
                            currentContactDetails.setAddressMoo(currentAddressLine1[1]);
                        }
                        if (currentAddressLineSize1 >= 2) {
                            currentContactDetails.setAddressBuilding(currentAddressLine1[2]);
                        }
                    }
                    currentContactDetails.setAddressStreet(resSearchIndividualCustomer.getBody().getPersonalDetailSection().getPersonalDetail().getRegAddrLine2());
                    if (resSearchIndividualCustomer.getBody().getPersonalDetailSection().getPersonalDetail().getRegAddrLine3() != null) {
                        String currentAddressLine3[] = resSearchIndividualCustomer.getBody().getPersonalDetailSection().getPersonalDetail().getRegAddrLine3().split(" ");
                        //validateSpiltSize
                        int currentAddressLineSize3 = currentAddressLine3.length - 1;
                        if (currentAddressLineSize3 >= 0) {
                            currentContactDetails.setSubdistrict(Util.replaceToBlank(currentAddressLine3[0], blank));
                        }
                        if (currentAddressLineSize3 >= 1) {
                            currentContactDetails.setDistrict(Util.replaceToBlank(currentAddressLine3[1], blank));
                        }
                    }
                    currentContactDetails.setProvince(resSearchIndividualCustomer.getBody().getPersonalDetailSection().getPersonalDetail().getRegCity());
                    currentContactDetails.setPostcode(resSearchIndividualCustomer.getBody().getPersonalDetailSection().getPersonalDetail().getRegPostalCd());
                    currentContactDetails.setCountry(resSearchIndividualCustomer.getBody().getPersonalDetailSection().getPersonalDetail().getRegCtry());
                    currentContactDetails.setCountryCode(resSearchIndividualCustomer.getBody().getPersonalDetailSection().getPersonalDetail().getRegIsoCtryCode());
                    individualModel.setCurrentAddress(currentContactDetails);


                    //set WorkAddress
                    ContactDetails workContactDetails = new ContactDetails();
                    if (resSearchIndividualCustomer.getBody().getPersonalDetailSection().getPersonalDetail().getBusAddrLine1() != null) {
                        String workAddressLine1[] = resSearchIndividualCustomer.getBody().getPersonalDetailSection().getPersonalDetail().getBusAddrLine1().split(" ");
                        //validateSpiltSize
                        int workAddressLineSize1 = workAddressLine1.length - 1;
                        if (workAddressLineSize1 >= 0) {
                            workContactDetails.setAddressNo(workAddressLine1[0]);
                        }
                        if (workAddressLineSize1 >= 1) {
                            workContactDetails.setAddressMoo(workAddressLine1[1]);
                        }
                        if (workAddressLineSize1 >= 2) {
                            workContactDetails.setAddressBuilding(workAddressLine1[2]);
                        }
                    }
                    workContactDetails.setAddressStreet(resSearchIndividualCustomer.getBody().getPersonalDetailSection().getPersonalDetail().getBusAddrLine2());
                    if (resSearchIndividualCustomer.getBody().getPersonalDetailSection().getPersonalDetail().getBusAddrLine3() != null) {
                        String workAddressLine3[] = resSearchIndividualCustomer.getBody().getPersonalDetailSection().getPersonalDetail().getBusAddrLine3().split(" ");
                        //ValidateSpiltSize
                        int workAddressLineSize3 = workAddressLine3.length - 1;
                        if (workAddressLineSize3 >= 0) {
                            workContactDetails.setSubdistrict(Util.replaceToBlank(workAddressLine3[0], blank));
                        }
                        if (workAddressLineSize3 >= 1) {
                            workContactDetails.setDistrict(Util.replaceToBlank(workAddressLine3[1], blank));
                        }
                    }
                    workContactDetails.setProvince(resSearchIndividualCustomer.getBody().getPersonalDetailSection().getPersonalDetail().getBusCity());
                    workContactDetails.setPostcode(resSearchIndividualCustomer.getBody().getPersonalDetailSection().getPersonalDetail().getBusPostalCd());
                    workContactDetails.setCountry(resSearchIndividualCustomer.getBody().getPersonalDetailSection().getPersonalDetail().getBusCtry());
                    workContactDetails.setCountryCode(resSearchIndividualCustomer.getBody().getPersonalDetailSection().getPersonalDetail().getBusIsoCtryCode());
                    individualModel.setWorkAddress(workContactDetails);


                } else if (resSearchIndividualCustomer.getHeader().getResCode().equals("1500")) { //Host Parameter is null
                    throw new RMInterfaceException(ExceptionMapping.RM_HOST_PARAMETER_IS_NULL, exceptionMsg.get(ExceptionMapping.RM_HOST_PARAMETER_IS_NULL));

                } else if (resSearchIndividualCustomer.getHeader().getResCode().equals("1511")) { //Data Not Found

                    log.debug("Data Not Found!");
                    throw new RMInterfaceException(ExceptionMapping.RM_DATA_NOT_FOUND, exceptionMsg.get(ExceptionMapping.RM_DATA_NOT_FOUND));

                } else if (resSearchIndividualCustomer.getHeader().getResCode().equals("3500")) { //fail
                    throw new RMInterfaceException(ExceptionMapping.RM_FAIL, exceptionMsg.get(ExceptionMapping.RM_FAIL));
                }
                //check null
            } else {
                log.warn("resSearchIndividualCustomer : Null");
                //Audit Data
                rmAuditor.add(userId, "IndividualService", actionDesc, requestTime, ActionResult.EXCEPTION, "responseIndividualCustomer : Null", new Date(), linkKey);
                throw new RMInterfaceException(ExceptionMapping.RM_DATA_NOT_FOUND, exceptionMsg.get(ExceptionMapping.RM_DATA_NOT_FOUND));
            }
        } catch (RMInterfaceException e) {
            throw e;
        } catch (Exception e) {
            log.error("Exception while call RM Individual", e);
            //Audit Data
            rmAuditor.add(userId, "IndividualService", actionDesc, requestTime, ActionResult.FAILED, e.getMessage(), new Date(), linkKey);
            throw new RMInterfaceException(e, ExceptionMapping.RM_SERVICE_FAILED, exceptionMsg.get(ExceptionMapping.RM_SERVICE_FAILED));
        }
        log.debug("IndividualService() END");
        return individualModel;
    }


    public CorporateModel corporateService(SearchIndividual searchIndividual, String userId) throws Exception {

        log.debug("CorporateService() START");
        CorporateModel corporateModel = null;

        //Validate ReqId
        if (!ValidationUtil.isValueInRange(1, 50, searchIndividual.getReqId().length())) {
            if (searchIndividual.getReqId() == null || searchIndividual.getReqId().equals("")) {
                throw new ValidationException(ValidationMapping.DATA_REQUIRED, validationMsg.get(ValidationMapping.DATA_REQUIRED, "(reqId)"));
            } else {
                throw new ValidationException(ValidationMapping.FIELD_LENGTH_INVALID, validationMsg.get(ValidationMapping.FIELD_LENGTH_INVALID, "(reqId)"));
            }
        }
        //Validate CustType
        if (!ValidationUtil.isValueEqual(1, searchIndividual.getCustType().length())) {
            if (searchIndividual.getCustType() == null || searchIndividual.getCustType().equals("")) {
                throw new ValidationException(ValidationMapping.DATA_REQUIRED, validationMsg.get(ValidationMapping.DATA_REQUIRED, "(custType)"));
            } else {
                throw new ValidationException(ValidationMapping.FIELD_LENGTH_INVALID, validationMsg.get(ValidationMapping.FIELD_LENGTH_INVALID, "(custType)"));
            }
        }
        //Validate Type
        if (!ValidationUtil.isValueEqual(2, searchIndividual.getType().length())) {
            if (searchIndividual.getType() == null || searchIndividual.getType().equals("")) {
                throw new ValidationException(ValidationMapping.DATA_REQUIRED, validationMsg.get(ValidationMapping.DATA_REQUIRED, "(type)"));
            } else {
                throw new ValidationException(ValidationMapping.FIELD_LENGTH_INVALID, validationMsg.get(ValidationMapping.FIELD_LENGTH_INVALID, "(type)"));
            }
        }
        //Validate CustId
        if (ValidationUtil.isGreaterThan(25, searchIndividual.getCustId().length())) {
            throw new ValidationException(ValidationMapping.FIELD_LENGTH_INVALID, validationMsg.get(ValidationMapping.FIELD_LENGTH_INVALID, "(custId)"));
        }
        //Validate CustNbr
        if (ValidationUtil.isGreaterThan(14, searchIndividual.getCustNbr().length())) {
            throw new ValidationException(ValidationMapping.FIELD_LENGTH_INVALID, validationMsg.get(ValidationMapping.FIELD_LENGTH_INVALID, "(custNbr)"));
        }
        //Validate CustName
        if (ValidationUtil.isGreaterThan(40, searchIndividual.getCustName().length())) {
            throw new ValidationException(ValidationMapping.FIELD_LENGTH_INVALID, validationMsg.get(ValidationMapping.FIELD_LENGTH_INVALID, "(custName)"));
        }
        //Validate RadSelectSearch
        if (!ValidationUtil.isValueInRange(1, 10, searchIndividual.getRadSelectSearch().length())) {
            if (searchIndividual.getRadSelectSearch() == null || searchIndividual.getRadSelectSearch().equals("")) {
                throw new ValidationException(ValidationMapping.DATA_REQUIRED, validationMsg.get(ValidationMapping.DATA_REQUIRED, "(radSelectSearch)"));
            } else {
                throw new ValidationException(ValidationMapping.FIELD_LENGTH_INVALID, validationMsg.get(ValidationMapping.FIELD_LENGTH_INVALID, "(radSelectSearch)"));
            }
        }
        log.debug("Validate Pass!");
        com.tmb.sme.data.requestsearchcorporatecustomer.Header header = new com.tmb.sme.data.requestsearchcorporatecustomer.Header();
        header.setReqID(searchIndividual.getReqId());

        com.tmb.sme.data.requestsearchcorporatecustomer.Body body = new com.tmb.sme.data.requestsearchcorporatecustomer.Body();
        body.setCustType(searchIndividual.getCustType());
        body.setType(searchIndividual.getType());
        body.setCustId(searchIndividual.getCustId());
        body.setCustNbr(searchIndividual.getCustNbr());
        body.setCustName(searchIndividual.getCustName());
        body.setRadSelectSearch(searchIndividual.getRadSelectSearch());

        ReqSearchCorporateCustomer reqSearch = new ReqSearchCorporateCustomer();
        reqSearch.setHeader(header);
        reqSearch.setBody(body);
        //actionDesc
        String actionDesc = "ReqID=" + reqSearch.getHeader().getReqID() + ",CustId=" + reqSearch.getBody().getCustId() + ",RedSelectSearch=" + reqSearch.getBody().getRadSelectSearch();
//
        //requestTime
        Date requestTime = new Date();
        String linkKey = Util.getLinkKey(userId);
        log.debug("LinkKey : {}", linkKey);
        log.debug("============================ Request ==============================");
        log.debug("requestServiceTime : {}", new Date());
        log.debug("requestHeaderData : {}", reqSearch.getHeader().toString());
        log.debug("requestBodyData : {}", reqSearch.getBody().toString());
        try {
            ResSearchCorporateCustomer resSearchCorporateCustomer = callServiceCorporate(reqSearch);
            if (resSearchCorporateCustomer != null) {
                //responseTime
                Date responseTime = new Date();
                log.debug("============================ Response ==============================");
                log.debug("responseServiceTime : {}", new Date());
                log.debug("responseHeaderData : {}", resSearchCorporateCustomer.getHeader().toString());
                if (resSearchCorporateCustomer.getBody() != null
                        && resSearchCorporateCustomer.getBody().getCorporateCustomerDetailSection() != null
                        && resSearchCorporateCustomer.getBody().getCorporateCustomerDetailSection().getCorporateDetail() != null) {
                    log.debug("responseBodyData : {}", resSearchCorporateCustomer.getBody().getCorporateCustomerDetailSection().getCorporateDetail().toString());
                }

                //Audit Data
                rmAuditor.add(userId, "corporateService", actionDesc, requestTime, ActionResult.SUCCESS,"ResDesc : "+ resSearchCorporateCustomer.getHeader().getResDesc(), responseTime, linkKey);

                //Check Success
                log.debug("requestServiceDescription : {}", resSearchCorporateCustomer.getHeader().getResDesc());
                if (resSearchCorporateCustomer.getHeader().getResCode().equals("0000")) {
                    corporateModel = new CorporateModel();
                    //checkSearchResult
                    if (resSearchCorporateCustomer.getBody().getSearchResult().equals("CL")) {
                        throw new RMInterfaceException(ExceptionMapping.RM_CUSTOMER_RESULT_MULTIPLE, exceptionMsg.get(ExceptionMapping.RM_CUSTOMER_RESULT_MULTIPLE));
                    }
                    //personal detail session
                    corporateModel.setTitleTH(resSearchCorporateCustomer.getBody().getCorporateCustomerDetailSection().getCorporateDetail().getTitle());
                    corporateModel.setTmbCusID(resSearchCorporateCustomer.getBody().getCorporateCustomerDetailSection().getCorporateDetail().getCustNbr());
                    corporateModel.setCompanyNameTH(resSearchCorporateCustomer.getBody().getCorporateCustomerDetailSection().getCorporateDetail().getThaiName1()
                            + " " + resSearchCorporateCustomer.getBody().getCorporateCustomerDetailSection().getCorporateDetail().getThaiName2()
                            + " " + resSearchCorporateCustomer.getBody().getCorporateCustomerDetailSection().getCorporateDetail().getThaiName3());
                    corporateModel.setCompanyNameEN(resSearchCorporateCustomer.getBody().getCorporateCustomerDetailSection().getCorporateDetail().getEngName1()
                            + " " + resSearchCorporateCustomer.getBody().getCorporateCustomerDetailSection().getCorporateDetail().getEngName2()
                            + " " + resSearchCorporateCustomer.getBody().getCorporateCustomerDetailSection().getCorporateDetail().getEngName3());
                    corporateModel.setRegistrationID(resSearchCorporateCustomer.getBody().getCorporateCustomerDetailSection().getCorporateDetail().getCitizenCId());
                    corporateModel.setRegistrationDate(resSearchCorporateCustomer.getBody().getCorporateCustomerDetailSection().getCorporateDetail().getEstDate());
                    corporateModel.setRegistrationCountry(resSearchCorporateCustomer.getBody().getCorporateCustomerDetailSection().getCorporateDetail().getIsoCountry());
                    corporateModel.setSubdistrict(Util.replaceToBlank(resSearchCorporateCustomer.getBody().getCorporateCustomerDetailSection().getCorporateDetail().getAddrTumbon(), blank));
                    corporateModel.setDistrict(Util.replaceToBlank(resSearchCorporateCustomer.getBody().getCorporateCustomerDetailSection().getCorporateDetail().getAddrAumper(), blank));
                    corporateModel.setProvince(resSearchCorporateCustomer.getBody().getCorporateCustomerDetailSection().getCorporateDetail().getCity());
                    corporateModel.setPostcode(resSearchCorporateCustomer.getBody().getCorporateCustomerDetailSection().getCorporateDetail().getPostalCd());
                    corporateModel.setCountry(resSearchCorporateCustomer.getBody().getCorporateCustomerDetailSection().getCorporateDetail().getCtry());
                    corporateModel.setCountryCode(resSearchCorporateCustomer.getBody().getCorporateCustomerDetailSection().getCorporateDetail().getIsoCtryCode());
                    corporateModel.setDocumentType(resSearchCorporateCustomer.getBody().getCorporateCustomerDetailSection().getCorporateDetail().getCId());
                    if (resSearchCorporateCustomer.getBody().getCorporateCustomerDetailSection().getCorporateDetail().getAddressLine1PRI() != null) {
                        String addressPri[] = resSearchCorporateCustomer.getBody().getCorporateCustomerDetailSection().getCorporateDetail().getAddressLine1PRI().split(" ");
                        int addressPriSize1 = addressPri.length;
                        if (addressPriSize1 > 0) {
                            corporateModel.setAddressNo(addressPri[0]);
                        }
                        if (addressPriSize1 > 1) {
                            corporateModel.setAddressMoo(addressPri[1]);
                        }
                        if (addressPriSize1 > 2) {
                            corporateModel.setAddressBuilding(addressPri[2]);
                        }
                    }
                    corporateModel.setAddressStreet(resSearchCorporateCustomer.getBody().getCorporateCustomerDetailSection().getCorporateDetail().getAddressLine2PRI());

                    RegistrationAddress registrationAddress = new RegistrationAddress();
                    registrationAddress.setSubdistrict(Util.replaceToBlank(resSearchCorporateCustomer.getBody().getCorporateCustomerDetailSection().getCorporateDetail().getComRegTumbon(), blank));
                    registrationAddress.setDistrict(Util.replaceToBlank(resSearchCorporateCustomer.getBody().getCorporateCustomerDetailSection().getCorporateDetail().getComRegAumper(), blank));
                    registrationAddress.setProvince(resSearchCorporateCustomer.getBody().getCorporateCustomerDetailSection().getCorporateDetail().getCity2());
//                    registrationAddress.setPostcode(resSearchCorporateCustomer.getBody().getCorporateCustomerDetailSection().getCorporateDetail().get);
                    registrationAddress.setCountry(resSearchCorporateCustomer.getBody().getCorporateCustomerDetailSection().getCorporateDetail().getCtry2());
                    registrationAddress.setCountryCode(resSearchCorporateCustomer.getBody().getCorporateCustomerDetailSection().getCorporateDetail().getIsoCtryCode2());
                    registrationAddress.setPhoneNo(resSearchCorporateCustomer.getBody().getCorporateCustomerDetailSection().getCorporateDetail().getTelephoneNumber1());
                    registrationAddress.setExtension(resSearchCorporateCustomer.getBody().getCorporateCustomerDetailSection().getCorporateDetail().getExtension1());
                    registrationAddress.setContactName(resSearchCorporateCustomer.getBody().getCorporateCustomerDetailSection().getCorporateDetail().getContactPerson());
                    registrationAddress.setContactPhoneNo(resSearchCorporateCustomer.getBody().getCorporateCustomerDetailSection().getCorporateDetail().getTelephoneNbr());
                    if (resSearchCorporateCustomer.getBody().getCorporateCustomerDetailSection().getCorporateDetail().getAddressLine1REG() != null) {
                        String addressReg[] = resSearchCorporateCustomer.getBody().getCorporateCustomerDetailSection().getCorporateDetail().getAddressLine1REG().split(" ");
                        int addressRegSize1 = addressReg.length;
                        if (addressRegSize1 > 0) {
                            registrationAddress.setAddressNo(addressReg[0]);
                        }
                        if (addressRegSize1 > 1) {
                            registrationAddress.setAddressMoo(addressReg[1]);
                        }
                        if (addressRegSize1 > 2) {
                            registrationAddress.setAddressBuilding(addressReg[2]);
                        }
                    }
                    registrationAddress.setAddressStreet(resSearchCorporateCustomer.getBody().getCorporateCustomerDetailSection().getCorporateDetail().getAddressLine2REG());
                    corporateModel.setRegistrationAddress(registrationAddress);

                    log.debug("responseCode: {}", resSearchCorporateCustomer.getHeader().getResCode());
                } else if (resSearchCorporateCustomer.getHeader().getResCode().equals("1500")) { //Host parameter is null
                    throw new RMInterfaceException(ExceptionMapping.RM_HOST_PARAMETER_IS_NULL, exceptionMsg.get(ExceptionMapping.RM_HOST_PARAMETER_IS_NULL));
                } else if (resSearchCorporateCustomer.getHeader().getResCode().equals("1511")) { //Data Not Found
                    log.debug("Data Not Found!");
                    throw new RMInterfaceException(ExceptionMapping.RM_DATA_NOT_FOUND, exceptionMsg.get(ExceptionMapping.RM_DATA_NOT_FOUND));

                } else if (resSearchCorporateCustomer.getHeader().getResCode().equals("3500")) {  //fail
                    throw new RMInterfaceException(ExceptionMapping.RM_FAIL, exceptionMsg.get(ExceptionMapping.RM_FAIL));
                }  //check null
            } else {
                log.warn(" resSearchCorporateCustomer : Null");
                //Audit Data
                rmAuditor.add(userId, "corporateService", actionDesc, requestTime, ActionResult.EXCEPTION, "responseCorporateCustomer : Null", new Date(), linkKey);
                throw new RMInterfaceException(ExceptionMapping.RM_DATA_NOT_FOUND, exceptionMsg.get(ExceptionMapping.RM_DATA_NOT_FOUND));
            }
        } catch (RMInterfaceException e) {
            throw e;
        } catch (Exception e) {
            log.error("Exception while call service RM Corporate!", e);
            //Audit Data
            rmAuditor.add(userId, "corporateService", actionDesc, requestTime, ActionResult.FAILED, e.getMessage(), new Date(), linkKey);
            throw new RMInterfaceException(ExceptionMapping.RM_SERVICE_FAILED, exceptionMsg.get(ExceptionMapping.RM_SERVICE_FAILED));
        }
        log.debug("CorporateService() END");
        return corporateModel;
    }

    /**
     * ***************************  CustomerAccount  *********************************
     */
    public CustomerAccountResult customerAccountService(SearchCustomerAccountModel searchCustomerAccountModel, String userId) throws Exception {
        log.debug("CustomerAccountService() START");
        CustomerAccountResult customerAccountResult = null;

        //Validate ReqId
        if (!ValidationUtil.isValueInRange(1, 50, searchCustomerAccountModel.getReqId().length())) {
            if (searchCustomerAccountModel.getReqId() == null || searchCustomerAccountModel.getReqId().equals("")) {
                throw new ValidationException(ValidationMapping.DATA_REQUIRED, validationMsg.get(ValidationMapping.DATA_REQUIRED, "(reqId)"));
            } else {
                throw new ValidationException(ValidationMapping.FIELD_LENGTH_INVALID, validationMsg.get(ValidationMapping.FIELD_LENGTH_INVALID, "(reqId)"));
            }
        }
        //Validate Acronym
        if (!ValidationUtil.isValueInRange(1, 20, searchCustomerAccountModel.getAcronym().length())) {
            if (searchCustomerAccountModel.getAcronym() == null || searchCustomerAccountModel.getAcronym().equals("")) {
                throw new ValidationException(ValidationMapping.DATA_REQUIRED, validationMsg.get(ValidationMapping.DATA_REQUIRED, "(acronym)"));
            } else {
                throw new ValidationException(ValidationMapping.FIELD_LENGTH_INVALID, validationMsg.get(ValidationMapping.FIELD_LENGTH_INVALID, "(acronym)"));
            }
        }
        //Validate ProductCode
        if (!ValidationUtil.isValueInRange(1, 8, searchCustomerAccountModel.getProductCode().length())) {
            if (searchCustomerAccountModel.getProductCode() == null || searchCustomerAccountModel.getProductCode().equals("")) {
                throw new ValidationException(ValidationMapping.DATA_REQUIRED, validationMsg.get(ValidationMapping.DATA_REQUIRED, "(productCode)"));
            } else {
                throw new ValidationException(ValidationMapping.FIELD_LENGTH_INVALID, validationMsg.get(ValidationMapping.FIELD_LENGTH_INVALID, "(productCode)"));
            }
        }
//        //Validate ServerURL
//        if (!ValidationUtil.isGreaterThan(100,searchIndividual.getCustNbr().length())) {
//            throw new ValidationException(validationMsg.get("validation.014"));
//        }
//        //Validate SessionId
//        if (!ValidationUtil.isGreaterThan(100,searchIndividual.getCustNbr().length())) {
//            throw new ValidationException(validationMsg.get("validation.014"));
//        }
        //Validate CustNbr
        if (ValidationUtil.isGreaterThan(14, searchCustomerAccountModel.getCustNbr().length())) {
            throw new ValidationException(ValidationMapping.FIELD_LENGTH_INVALID, validationMsg.get(ValidationMapping.FIELD_LENGTH_INVALID, "(custNbr)"));
        }
        //Validate RadSelectSearch
        if (!ValidationUtil.isValueInRange(1, 10, searchCustomerAccountModel.getRadSelectSearch().length())) {
            if (searchCustomerAccountModel.getRadSelectSearch() == null || searchCustomerAccountModel.getRadSelectSearch().equals("")) {
                throw new ValidationException(ValidationMapping.DATA_REQUIRED, validationMsg.get(ValidationMapping.DATA_REQUIRED, "(radSelectSearch)"));
            } else {
                throw new ValidationException(ValidationMapping.FIELD_LENGTH_INVALID, validationMsg.get(ValidationMapping.FIELD_LENGTH_INVALID, "(radSelectSearch)"));
            }
        }
        log.debug("Validate Pass!");
        //setHeader
        com.tmb.common.data.requestsearchcustomeraccount.Header header = new com.tmb.common.data.requestsearchcustomeraccount.Header();
        header.setReqId(searchCustomerAccountModel.getReqId());
        header.setAcronym(searchCustomerAccountModel.getAcronym());
        header.setProductCode(searchCustomerAccountModel.getProductCode());
        //todo
//        header.setServerURL(new JAXBElement<String>(new QName(customerServerUrl), String.class, customerServerUrl));
//        header.setSessionId(new JAXBElement<String>(new QName(customerSessionId), String.class, customerSessionId));

        //setBody
        com.tmb.common.data.requestsearchcustomeraccount.Body body = new com.tmb.common.data.requestsearchcustomeraccount.Body();
        body.setCustNbr(searchCustomerAccountModel.getCustNbr());
        body.setRadSelectSearch(searchCustomerAccountModel.getRadSelectSearch());


        ReqSearchCustomerAccount reqSearch = new ReqSearchCustomerAccount();
        reqSearch.setHeader(header);
        reqSearch.setBody(body);

        //actionDesc
        String actionDesc = "ReqID=" + reqSearch.getHeader().getReqId() + ",ProductCode=" + reqSearch.getHeader().getProductCode() + ",Acronym=" + reqSearch.getHeader().getAcronym()
                + ",CustNbr=" + reqSearch.getBody().getCustNbr() + ",RedSelectSearch=" + reqSearch.getBody().getRadSelectSearch();

        //requestTime
        Date requestTime = new Date();
        String linkKey = Util.getLinkKey(userId);
        log.debug("LinkKey : {}", linkKey);
        log.debug("============================ Request ==============================");
        log.debug("requestServiceTime : {}", requestTime);
        log.debug("requestHeaderData : {}", reqSearch.getHeader().toString());
        log.debug("requestBodyData : {}", reqSearch.getBody().toString());
        try {
            ResSearchCustomerAccount resSearchCustomerAccount = callServiceCustomerAccount(reqSearch);
            if (resSearchCustomerAccount != null) {
                //responseTime
                Date responseTime = new Date();
                log.debug("============================ Response ==============================");
                log.debug("responseServiceTime : {}", responseTime);
                log.debug("responseHeaderData : {}", resSearchCustomerAccount.getHeader().toString());
                if (resSearchCustomerAccount.getBody() != null && resSearchCustomerAccount.getBody().getAccountList() != null) {
                    log.debug("accountListSize: {}", resSearchCustomerAccount.getBody().getAccountList().size());
                }
                for (int i = 0; i < resSearchCustomerAccount.getBody().getAccountList().size(); i++) {
                    log.debug("accountListData " + i + 1 + " : {}", resSearchCustomerAccount.getBody().getAccountList().get(i).toString());
                }
                customerAccountResult = new CustomerAccountResult();
                customerAccountResult.setActionResult(ActionResult.SUCCESS);
                customerAccountResult.setCustomerId(searchCustomerAccountModel.getCustNbr());

                //Audit Data
                rmAuditor.add(userId, "customerAccountService", actionDesc, requestTime, ActionResult.SUCCESS, resSearchCustomerAccount.getHeader().getResDesc(), new Date(), linkKey);

                //Check Success
                log.debug("requestServiceDescription : {}", resSearchCustomerAccount.getHeader().getResDesc());
                if (resSearchCustomerAccount.getHeader().getResCode().equals("0000")) {
                    List<CustomerAccountListModel> listModelList = new ArrayList<CustomerAccountListModel>();

                    //checkSearchResult
                    if (resSearchCustomerAccount.getBody().getAccountList() != null && resSearchCustomerAccount.getBody().getAccountList().size() > 0) {
                        CustomerAccountListModel customerAccountListModel = null;

                        for (int i = 0; i < resSearchCustomerAccount.getBody().getAccountList().size(); i++) {
                            customerAccountListModel = new CustomerAccountListModel();
                            customerAccountListModel.setRel(resSearchCustomerAccount.getBody().getAccountList().get(i).getRel());
                            customerAccountListModel.setCd(resSearchCustomerAccount.getBody().getAccountList().get(i).getCd());
                            customerAccountListModel.setpSO(resSearchCustomerAccount.getBody().getAccountList().get(i).getPSO());
                            customerAccountListModel.setAppl(resSearchCustomerAccount.getBody().getAccountList().get(i).getAppl());
                            customerAccountListModel.setAccountNo(resSearchCustomerAccount.getBody().getAccountList().get(i).getAccountNo());
                            customerAccountListModel.setTrlr(resSearchCustomerAccount.getBody().getAccountList().get(i).getTrlr());
                            customerAccountListModel.setBalance(resSearchCustomerAccount.getBody().getAccountList().get(i).getBalance());
                            customerAccountListModel.setDir(resSearchCustomerAccount.getBody().getAccountList().get(i).getDir());
                            customerAccountListModel.setProd(resSearchCustomerAccount.getBody().getAccountList().get(i).getProd());
                            customerAccountListModel.setCtl1(resSearchCustomerAccount.getBody().getAccountList().get(i).getCtl1());
                            customerAccountListModel.setCtl2(resSearchCustomerAccount.getBody().getAccountList().get(i).getCtl2());
                            customerAccountListModel.setCtl3(resSearchCustomerAccount.getBody().getAccountList().get(i).getCtl3());
                            customerAccountListModel.setCtl4(resSearchCustomerAccount.getBody().getAccountList().get(i).getCtl4());
                            customerAccountListModel.setStatus(resSearchCustomerAccount.getBody().getAccountList().get(i).getStatus());
                            customerAccountListModel.setDate(resSearchCustomerAccount.getBody().getAccountList().get(i).getDate());
                            customerAccountListModel.setName(resSearchCustomerAccount.getBody().getAccountList().get(i).getName());
                            customerAccountListModel.setCitizenId(resSearchCustomerAccount.getBody().getAccountList().get(i).getCitizenId());
                            customerAccountListModel.setCurr(resSearchCustomerAccount.getBody().getAccountList().get(i).getCurr());
                            listModelList.add(customerAccountListModel);
                        }

                    }
                    customerAccountResult.setAccountListModels(listModelList);

                } else if (resSearchCustomerAccount.getHeader().getResCode().equals("1500")) { //Host Parameter is Null
                    throw new RMInterfaceException(ExceptionMapping.RM_HOST_PARAMETER_IS_NULL, exceptionMsg.get(ExceptionMapping.RM_HOST_PARAMETER_IS_NULL));
                } else if (resSearchCustomerAccount.getHeader().getResCode().equals("1511")) { //Data Not Found
                    log.debug("Data Not Found!");
                    throw new RMInterfaceException(ExceptionMapping.RM_DATA_NOT_FOUND, exceptionMsg.get(ExceptionMapping.RM_DATA_NOT_FOUND));
                } else if (resSearchCustomerAccount.getHeader().getResCode().equals("3500")) { //fail
                    throw new RMInterfaceException(ExceptionMapping.RM_FAIL, exceptionMsg.get(ExceptionMapping.RM_FAIL));
                }
                //check null
            } else {
                log.warn(" resSearchCustomerAccount : Null");
                //Audit Data
                rmAuditor.add(userId, "customerAccountService", actionDesc, requestTime, ActionResult.EXCEPTION, "responseCustomerAccount : Null", new Date(), linkKey);
                throw new RMInterfaceException(ExceptionMapping.RM_DATA_NOT_FOUND, exceptionMsg.get(ExceptionMapping.RM_DATA_NOT_FOUND));
            }

        } catch (RMInterfaceException e) {
            throw e;
        } catch (Exception e) {

            log.error("Exception while call service RM Search account!", e);
            //Audit Data
            rmAuditor.add(userId, "customerAccountService", actionDesc, requestTime, ActionResult.FAILED, e.getMessage(), new Date(), linkKey);
            throw new RMInterfaceException(e, ExceptionMapping.RM_SERVICE_FAILED, exceptionMsg.get(ExceptionMapping.RM_SERVICE_FAILED));
        }

        log.debug("CorporateService() END");
        return customerAccountResult;
    }


    // Services
    private ResSearchIndividualCustomer callServiceIndividualCM(ReqSearchIndividualCustomer reqSearch) throws Exception {
        log.debug("callServiceIndividual() START");
        com.tmb.common.data.responsesearchindividualcustomer.ResSearchIndividualCustomer resSearchIndividualCustomer = null;
        URL url = this.getClass().getResource("/com/tmb/EAISearchIndividualCustomerCM.wsdl");
        QName qname = new QName("http://data.common.tmb.com/EAISearchIndividualCustomerCM/", "EAISearchIndividualCustomerCM");
        EAISearchIndividualCustomerCM_Service service = new EAISearchIndividualCustomerCM_Service(url, qname);
        EAISearchIndividualCustomerCM eaiSearchInd = service.getEAISearchIndividualCustomerCM();
        try{
            ((BindingProvider) eaiSearchInd).getRequestContext().put(BindingProviderProperties.REQUEST_TIMEOUT,Integer.parseInt(individualRequestTimeout)*1000);
        }catch (Exception e){
            log.error("individual Service request_timeout must be a number!");
            ((BindingProvider) eaiSearchInd).getRequestContext().put(BindingProviderProperties.REQUEST_TIMEOUT,60000);
        }

        try{
            ((BindingProvider) eaiSearchInd).getRequestContext().put(BindingProviderProperties.CONNECT_TIMEOUT,Integer.parseInt(individualConnectTimeout)*1000);
        }catch (Exception e){
            log.error("individual Service connect_timeout must be a number!");
            ((BindingProvider) eaiSearchInd).getRequestContext().put(BindingProviderProperties.CONNECT_TIMEOUT,60000);
        }

        ((BindingProvider) eaiSearchInd).getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY,
                individualAddress);
        resSearchIndividualCustomer = eaiSearchInd.searchIndividualCustomer(reqSearch);
        log.debug("callServiceIndividual() END");
        return resSearchIndividualCustomer;
    }

    private ResSearchCorporateCustomer callServiceCorporate(ReqSearchCorporateCustomer reqSearch) throws Exception {
        log.debug("callServiceCorporate() START");
        ResSearchCorporateCustomer resSearchCorporateCustomer = null;
        URL url = this.getClass().getResource("/com/tmb/EAISearchCorporateCustomer.wsdl");
        QName qname = new QName("http://data.sme.tmb.com/EAISearchCorporateCustomer/", "EAISearchCorporateCustomer");
        EAISearchCorporateCustomer_Service service = new EAISearchCorporateCustomer_Service(url, qname);
        EAISearchCorporateCustomer eaiSearchCor = service.getEAISearchCorporateCustomer();
        try{
            ((BindingProvider) eaiSearchCor).getRequestContext().put(BindingProviderProperties.REQUEST_TIMEOUT, Integer.parseInt(juristicRequestTimeout)*1000);
        }catch (Exception e){
            log.error("juristic Service request_timeout must be a number!");
            ((BindingProvider) eaiSearchCor).getRequestContext().put(BindingProviderProperties.REQUEST_TIMEOUT, 60000);
        }

        try{
            ((BindingProvider) eaiSearchCor).getRequestContext().put(BindingProviderProperties.CONNECT_TIMEOUT, Integer.parseInt(juristicConnectTimeout)*1000);
        }catch (Exception e){
            log.error("juristic Service connect_timeout must be a number!");
            ((BindingProvider) eaiSearchCor).getRequestContext().put(BindingProviderProperties.CONNECT_TIMEOUT, 60000);
        }

        ((BindingProvider) eaiSearchCor).getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY,
                corporateAddress);
        resSearchCorporateCustomer = eaiSearchCor.searchCorporateCustomer(reqSearch);
        log.debug("callServiceCorporate() END");
        return resSearchCorporateCustomer;
    }

    private ResSearchCustomerAccount callServiceCustomerAccount(ReqSearchCustomerAccount reqSearch) throws Exception {
        log.debug("callServiceCustomerAccount() START");
        ResSearchCustomerAccount resSearchCustomerAccount = null;
        URL url = this.getClass().getResource("/com/tmb/EAISearchCustomerAccount.wsdl");
        QName qname = new QName("http://data.common.tmb.com/EAISearchCustomerAccount/", "EAISearchCustomerAccount");
        EAISearchCustomerAccount_Service service = new EAISearchCustomerAccount_Service(url, qname);
        EAISearchCustomerAccount eaiSearchCa = service.getEAISearchCustomerAccount();
        try{
            ((BindingProvider) eaiSearchCa).getRequestContext().put(BindingProviderProperties.REQUEST_TIMEOUT, Integer.parseInt(cusAccountRequestTimeout)*1000);
        }catch (Exception e){
            log.error("customerAccount Service request_timeout must be a number!");
            ((BindingProvider) eaiSearchCa).getRequestContext().put(BindingProviderProperties.REQUEST_TIMEOUT, 60000);
        }

        try{
            ((BindingProvider) eaiSearchCa).getRequestContext().put(BindingProviderProperties.CONNECT_TIMEOUT, Integer.parseInt(cusAccountConnectTimeout)*1000);
        }catch (Exception e){
            log.error("customerAccount Service connect_timeout must be a number!");
            ((BindingProvider) eaiSearchCa).getRequestContext().put(BindingProviderProperties.CONNECT_TIMEOUT, 60000);
        }


        ((BindingProvider) eaiSearchCa).getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY,
                customerAccountAddress);
        resSearchCustomerAccount = eaiSearchCa.searchCustomerAccount(reqSearch);
        log.debug("callServiceCustomerAccount() END");
        return resSearchCustomerAccount;
    }


}
