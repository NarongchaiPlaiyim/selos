package com.clevel.selos.integration.corebanking;

import com.clevel.selos.exception.ValidationException;
import com.clevel.selos.integration.RM;
import com.clevel.selos.model.ActionResult;
import com.clevel.selos.model.RMmodel.CustomerAccountListModel;
import com.clevel.selos.model.RMmodel.CustomerAccountModel;
import com.clevel.selos.model.RMmodel.*;
import com.clevel.selos.model.RMmodel.corporateInfo.CorporateModel;
import com.clevel.selos.model.RMmodel.corporateInfo.RegistrationAddress;
import com.clevel.selos.model.RMmodel.individualInfo.ContactDetails;
import com.clevel.selos.model.RMmodel.individualInfo.IndividualModel;
import com.clevel.selos.model.RMmodel.individualInfo.Spouse;
import com.clevel.selos.model.RMmodel.individualInfo.Telephone;
import com.clevel.selos.system.Config;
import com.clevel.selos.system.audit.SystemAuditor;
import com.clevel.selos.system.message.*;
import com.clevel.selos.util.Util;
import com.clevel.selos.util.ValidationUtil;
import com.sun.xml.internal.ws.client.BindingProviderProperties;
import com.tmb.common.data.eaisearchcustomeraccount.EAISearchCustomerAccount;
import com.tmb.common.data.eaisearchcustomeraccount.EAISearchCustomerAccount_Service;
import com.tmb.common.data.requestsearchcustomeraccount.ReqSearchCustomerAccount;
import com.tmb.common.data.responsesearchcustomeraccount.ResSearchCustomerAccount;
import com.tmb.sme.data.eaisearchcorporatecustomer.EAISearchCorporateCustomer;
import com.tmb.sme.data.eaisearchcorporatecustomer.EAISearchCorporateCustomer_Service;
import com.tmb.sme.data.eaisearchindividualcustomer.EAISearchIndividualCustomer;
import com.tmb.sme.data.eaisearchindividualcustomer.EAISearchIndividualCustomer_Service;
import com.tmb.sme.data.requestsearchcorporatecustomer.ReqSearchCorporateCustomer;
import com.tmb.sme.data.requestsearchindividualcustomer.Body;
import com.tmb.sme.data.requestsearchindividualcustomer.Header;
import com.tmb.sme.data.requestsearchindividualcustomer.ReqSearchIndividualCustomer;
import com.tmb.sme.data.responsesearchcorporatecustomer.ResSearchCorporateCustomer;
import com.tmb.sme.data.responsesearchindividualcustomer.ResSearchIndividualCustomer;
import org.slf4j.Logger;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
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
    @Config(name = "interface.rm.service.connectTimeout")
    String connectTimeout;

    @Inject
    @Config(name = "interface.rm.service.requestTimeout")
    String requestTimeout;

    @Inject
    @RM
    SystemAuditor rmAuditor;

    @Inject
    public RMService() {
    }

    @PostConstruct
    public void onCreate() {

    }

    public IndividualModel individualService(SearchIndividual searchIndividual,String userId) throws Exception {
        log.debug("IndividualService() START");
        IndividualModel individualModel = null;

        //Validate ReqId
        if (!ValidationUtil.isValueInRange(1, 50, searchIndividual.getReqId().length())) {
            if(searchIndividual.getReqId()==null||searchIndividual.getReqId().equals("")){
                throw new ValidationException(validationMsg.get(ValidationMapping.DATA_REQUIRED,"(reqId)"));
            }else{
                throw new ValidationException(validationMsg.get(ValidationMapping.FIELD_LENGTH_INVALID,"(reqId)"));
            }
        }
        //Validate CustType
        if (!ValidationUtil.isValueEqual(1, searchIndividual.getCustType().length())) {
            if(searchIndividual.getCustType()==null||searchIndividual.getCustType().equals("")){
                throw new ValidationException(validationMsg.get(ValidationMapping.DATA_REQUIRED,"(custType)"));
            }else{
                throw new ValidationException(validationMsg.get(ValidationMapping.FIELD_LENGTH_INVALID,"(custType)"));
            }
        }
        //Validate Type
        if (!ValidationUtil.isValueEqual(2, searchIndividual.getType().length())) {
            if(searchIndividual.getType()==null||searchIndividual.getType().equals("")){
                throw new ValidationException(validationMsg.get(ValidationMapping.DATA_REQUIRED,"(type)"));
            }else{
                throw new ValidationException(validationMsg.get(ValidationMapping.FIELD_LENGTH_INVALID,"(type)"));
            }
        }
        //Validate CustId
        if (ValidationUtil.isGreaterThan(25, searchIndividual.getCustId().length())) {
            throw new ValidationException(validationMsg.get(ValidationMapping.FIELD_LENGTH_INVALID,"(custId)"));
        }
        //Validate CustNbr
        if (ValidationUtil.isGreaterThan(14, searchIndividual.getCustNbr().length())) {
            throw new ValidationException(validationMsg.get(ValidationMapping.FIELD_LENGTH_INVALID,"(custNbr)"));
        }
        //Validate CustName
        if (ValidationUtil.isGreaterThan(40, searchIndividual.getCustName().length())) {
            throw new ValidationException(validationMsg.get(ValidationMapping.FIELD_LENGTH_INVALID,"(custName)"));
        }
        //Validate CustSurname
        if (ValidationUtil.isGreaterThan(40, searchIndividual.getCustSurname().length())) {
            throw new ValidationException(validationMsg.get(ValidationMapping.FIELD_LENGTH_INVALID,"(surname)"));
        }
        //Validate RadSelectSearch
        if (!ValidationUtil.isValueInRange(1, 10, searchIndividual.getRadSelectSearch().length())) {
            if(searchIndividual.getRadSelectSearch()==null||searchIndividual.getRadSelectSearch().equals("")){
                throw new ValidationException(validationMsg.get(ValidationMapping.DATA_REQUIRED,"(radSelectSearch)"));
            }else{
                throw new ValidationException(validationMsg.get(ValidationMapping.FIELD_LENGTH_INVALID,"(radSelectSearch)"));
            }
        }
        log.debug("Validate Pass!");

        Header header = new Header();
        header.setReqID(searchIndividual.getReqId());

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
        String actionDesc = "ReqID=" + reqSearch.getHeader().getReqID() + ",CustId=" + reqSearch.getBody().getCustId() + ",RedSelectSearch=" + reqSearch.getBody().getRadSelectSearch();

        //requestTime
        Date requestTime = new Date();
        String linkKey = Util.getLinkKey(userId);
        log.debug("LinkKey : {}", linkKey);
        log.debug("============================ Request ==============================");
        log.debug("requestServiceTime : {}", new Date());
        log.debug("requestHeaderData : {}", reqSearch.getHeader().toString());
        log.debug("requestBodyData : {}", reqSearch.getBody().toString());
        try {
            ResSearchIndividualCustomer resSearchIndividualCustomer = callServiceIndividual(reqSearch);
            if (resSearchIndividualCustomer != null) {
                //responseTime
                Date responseTime = new Date();
                log.debug("============================ Response ==============================");
                log.debug("responseServiceTime : {}", new Date());
                log.debug("responseHeaderData : {}", resSearchIndividualCustomer.getHeader().toString());
                log.debug("responseBodyData : {}", resSearchIndividualCustomer.getBody().getPersonalDetailSection().getPersonalDetail().toString());


                //Audit Data
                rmAuditor.add(userId, "individualService", actionDesc, requestTime, ActionResult.SUCCEED, resSearchIndividualCustomer.getHeader().getResCode(), responseTime, linkKey);

                //Check Success
                log.debug("requestServiceDescription : {}", resSearchIndividualCustomer.getHeader().getResDesc());
                if (resSearchIndividualCustomer.getHeader().getResCode().equals("0000")) {
                    individualModel = new IndividualModel();
                    //checkSearchResult
                    if (resSearchIndividualCustomer.getBody().getSearchResult().equals("CL")) {
                        throw new ValidationException(validationMsg.get(ValidationMapping.CUSTOMER_RESULT_MULTIPLE));   
                    }
                    //personal detail session
                    individualModel.setTmbCusID(resSearchIndividualCustomer.getBody().getPersonalDetailSection().getPersonalDetail().getCustNbr());
                    individualModel.setTitleTH(resSearchIndividualCustomer.getBody().getPersonalDetailSection().getPersonalDetail().getTitle());
                    //spilt Name
                    String name[] = resSearchIndividualCustomer.getBody().getPersonalDetailSection().getPersonalDetail().getName().split(" ");
                    int nameSize = name.length - 1;
                    if (nameSize >= 0) {
                        individualModel.setFirstname(name[0]);
                    }
                    if (nameSize >= 1) {
                        individualModel.setLastname(name[1]);
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
                    individualModel.setNationality(resSearchIndividualCustomer.getBody().getPersonalDetailSection().getPersonalDetail().getIsoCitizenCtry());  //****
                    individualModel.setNumberOfChild(resSearchIndividualCustomer.getBody().getPersonalDetailSection().getPersonalDetail().getNoOfChildren());
                    //spouse
                    String spouseName[] = resSearchIndividualCustomer.getBody().getPersonalDetailSection().getPersonalDetail().getSpouseName().split(" ");
                    String spouse1 = "", spouse2 = "";
                    int spouseSize = spouseName.length - 1;
                    if (spouseSize >= 0) {
                        spouse1 = spouseName[0];
                    }
                    if (spouseSize >= 1) {
                        spouse2 = spouseName[1];
                    }

                    individualModel.setSpouse(new Spouse(spouse1, spouse2, resSearchIndividualCustomer.getBody().getPersonalDetailSection().getPersonalDetail().getSpouseTin()
                            , resSearchIndividualCustomer.getBody().getPersonalDetailSection().getPersonalDetail().getSpouseDateOfBirth()));

                    individualModel.setOccupationCode(resSearchIndividualCustomer.getBody().getPersonalDetailSection().getPersonalDetail().getOccupationCode1());
                    individualModel.setBizCode(resSearchIndividualCustomer.getBody().getPersonalDetailSection().getPersonalDetail().getBusType1());

                    individualModel.setTitleEN(resSearchIndividualCustomer.getBody().getPersonalDetailSection().getPersonalDetail().getTitleEng());
                    individualModel.setFirstnameEN(resSearchIndividualCustomer.getBody().getPersonalDetailSection().getPersonalDetail().getNameEng());
                    individualModel.setLastnameEN(resSearchIndividualCustomer.getBody().getPersonalDetailSection().getPersonalDetail().getNameEng());
                    //set HomeAddress
                    String homeAddressLine1[] = resSearchIndividualCustomer.getBody().getPersonalDetailSection().getPersonalDetail().getResAddrLine1().split(" ");
                    String homeAddressLine3[] = resSearchIndividualCustomer.getBody().getPersonalDetailSection().getPersonalDetail().getResAddrLine3().split(" ");
                    ContactDetails homeContactDetails = new ContactDetails();
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
                    homeContactDetails.setAddressStreet(resSearchIndividualCustomer.getBody().getPersonalDetailSection().getPersonalDetail().getResAddrLine2());
                    //validateSpiltSize
                    int homeAddressLineSize3 = homeAddressLine3.length - 1;
                    if (homeAddressLineSize3 >= 0) {
                        homeContactDetails.setSubdistrict(Util.replaceStringToBlank(homeAddressLine3[0], "แขวง"));
                    }
                    if (homeAddressLineSize3 >= 1) {
                        homeContactDetails.setDistrict(Util.replaceStringToBlank(homeAddressLine3[1], "เขต"));
                    }
                    homeContactDetails.setProvince(resSearchIndividualCustomer.getBody().getPersonalDetailSection().getPersonalDetail().getResCity());
                    homeContactDetails.setPostcode(resSearchIndividualCustomer.getBody().getPersonalDetailSection().getPersonalDetail().getResPostalCd());
                    homeContactDetails.setCountry(resSearchIndividualCustomer.getBody().getPersonalDetailSection().getPersonalDetail().getResCtry());
                    homeContactDetails.setCountryCode(resSearchIndividualCustomer.getBody().getPersonalDetailSection().getPersonalDetail().getResIsoCtryCode());
                    individualModel.setHomeAddress(homeContactDetails);


                    //set CurrentAddress
                    String currentAddressLine1[] = resSearchIndividualCustomer.getBody().getPersonalDetailSection().getPersonalDetail().getRegAddrLine1().split(" ");
                    String currentAddressLine3[] = resSearchIndividualCustomer.getBody().getPersonalDetailSection().getPersonalDetail().getRegAddrLine3().split(" ");
                    ContactDetails currentContactDetails = new ContactDetails();
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
                    currentContactDetails.setAddressStreet(resSearchIndividualCustomer.getBody().getPersonalDetailSection().getPersonalDetail().getRegAddrLine2());
                    //validateSpiltSize
                    int currentAddressLineSize3 = currentAddressLine3.length - 1;
                    if (currentAddressLineSize3 >= 0) {
                        currentContactDetails.setSubdistrict(Util.replaceStringToBlank(currentAddressLine3[0], "แขวง"));
                    }
                    if (currentAddressLineSize3 >= 1) {
                        currentContactDetails.setDistrict(Util.replaceStringToBlank(currentAddressLine3[1], "เขต"));
                    }
                    currentContactDetails.setProvince(resSearchIndividualCustomer.getBody().getPersonalDetailSection().getPersonalDetail().getRegCity());
                    currentContactDetails.setPostcode(resSearchIndividualCustomer.getBody().getPersonalDetailSection().getPersonalDetail().getRegPostalCd());
                    currentContactDetails.setCountry(resSearchIndividualCustomer.getBody().getPersonalDetailSection().getPersonalDetail().getRegCtry());
                    currentContactDetails.setCountryCode(resSearchIndividualCustomer.getBody().getPersonalDetailSection().getPersonalDetail().getRegIsoCtryCode());
                    individualModel.setCurrentAddress(currentContactDetails);


                    //set WorkAddress
                    String workAddressLine1[] = resSearchIndividualCustomer.getBody().getPersonalDetailSection().getPersonalDetail().getBusAddrLine1().split(" ");
                    String workAddressLine3[] = resSearchIndividualCustomer.getBody().getPersonalDetailSection().getPersonalDetail().getBusAddrLine3().split(" ");
                    ContactDetails workContactDetails = new ContactDetails();
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
                    workContactDetails.setAddressStreet(resSearchIndividualCustomer.getBody().getPersonalDetailSection().getPersonalDetail().getBusAddrLine2());
                    //ValidateSpiltSize
                    int workAddressLineSize3 = workAddressLine3.length - 1;
                    if (workAddressLineSize3 >= 0) {
                        workContactDetails.setSubdistrict(Util.replaceStringToBlank(workAddressLine3[0], "แขวง"));
                    }
                    if (workAddressLineSize3 >= 1) {
                        workContactDetails.setDistrict(Util.replaceStringToBlank(workAddressLine3[1], "เขต"));
                    }
                    workContactDetails.setProvince(resSearchIndividualCustomer.getBody().getPersonalDetailSection().getPersonalDetail().getBusCity());
                    workContactDetails.setPostcode(resSearchIndividualCustomer.getBody().getPersonalDetailSection().getPersonalDetail().getBusPostalCd());
                    workContactDetails.setCountry(resSearchIndividualCustomer.getBody().getPersonalDetailSection().getPersonalDetail().getBusCtry());
                    workContactDetails.setCountryCode(resSearchIndividualCustomer.getBody().getPersonalDetailSection().getPersonalDetail().getBusIsoCtryCode());
                    individualModel.setWorkAddress(workContactDetails);


                } else if (resSearchIndividualCustomer.getHeader().getResCode().equals("1500")) { //Host Parameter is null
                    throw new ValidationException(exceptionMsg.get(ExceptionMapping.HOST_PARAMETER_IS_NULL));
                    
                } else if (resSearchIndividualCustomer.getHeader().getResCode().equals("1511")) { //Data Not Found

                    log.debug("Data Not Found!");
                    individualModel = new IndividualModel();

                } else if (resSearchIndividualCustomer.getHeader().getResCode().equals("3500")) { //fail
                    throw new ValidationException(exceptionMsg.get(ExceptionMapping.FAIL));
                }
                //check null
            } else {
                log.warn("resSearchIndividualCustomer : Null");
                //Audit Data
                rmAuditor.add(userId, "IndividualService", actionDesc, requestTime, ActionResult.EXCEPTION, "responseIndividualCustomer : Null", new Date(), linkKey);
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Exception :{}", e.getMessage());
            //Audit Data
            rmAuditor.add(userId, "IndividualService", actionDesc, requestTime, ActionResult.FAILED, e.getMessage(), new Date(), linkKey);
        }
        log.debug("IndividualService() END");
        return individualModel;
    }


    public CorporateModel corporateService(SearchIndividual searchIndividual,String userId) throws Exception {

        log.debug("CorporateService() START");
        CorporateModel corporateModel = null;

        //Validate ReqId
        if (!ValidationUtil.isValueInRange(1, 50, searchIndividual.getReqId().length())) {
            if(searchIndividual.getReqId()==null||searchIndividual.getReqId().equals("")){
                throw new ValidationException(validationMsg.get(ValidationMapping.DATA_REQUIRED,"(reqId)"));
            }else{
                throw new ValidationException(validationMsg.get(ValidationMapping.FIELD_LENGTH_INVALID,"(reqId)"));
            }
        }
        //Validate CustType
        if (!ValidationUtil.isValueEqual(1, searchIndividual.getCustType().length())) {
            if(searchIndividual.getCustType()==null||searchIndividual.getCustType().equals("")){
                throw new ValidationException(validationMsg.get(ValidationMapping.DATA_REQUIRED,"(custType)"));
            }else{
                throw new ValidationException(validationMsg.get(ValidationMapping.FIELD_LENGTH_INVALID,"(custType)"));
            }
        }
        //Validate Type
        if (!ValidationUtil.isValueEqual(2, searchIndividual.getType().length())) {
            if(searchIndividual.getType()==null||searchIndividual.getType().equals("")){
                throw new ValidationException(validationMsg.get(ValidationMapping.DATA_REQUIRED,"(type)"));
            }else{
                throw new ValidationException(validationMsg.get(ValidationMapping.FIELD_LENGTH_INVALID,"(type)"));
            }
        }
        //Validate CustId
        if (ValidationUtil.isGreaterThan(25, searchIndividual.getCustId().length())) {
            throw new ValidationException(validationMsg.get(ValidationMapping.FIELD_LENGTH_INVALID,"(custId)"));
        }
        //Validate CustNbr
        if (ValidationUtil.isGreaterThan(14, searchIndividual.getCustNbr().length())) {
            throw new ValidationException(validationMsg.get(ValidationMapping.FIELD_LENGTH_INVALID,"(custNbr)"));
        }
        //Validate CustName
        if (ValidationUtil.isGreaterThan(40, searchIndividual.getCustName().length())) {
            throw new ValidationException(validationMsg.get(ValidationMapping.FIELD_LENGTH_INVALID,"(custName)"));
        }
        //Validate RadSelectSearch
        if (!ValidationUtil.isValueInRange(1, 10, searchIndividual.getRadSelectSearch().length())) {
            if(searchIndividual.getRadSelectSearch()==null||searchIndividual.getRadSelectSearch().equals("")){
                throw new ValidationException(validationMsg.get(ValidationMapping.DATA_REQUIRED,"(radSelectSearch)"));
            }else{
                throw new ValidationException(validationMsg.get(ValidationMapping.FIELD_LENGTH_INVALID,"(radSelectSearch)"));
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
                log.debug("responseBodyData : {}", resSearchCorporateCustomer.getBody().getCorporateCustomerDetailSection().getCorporateDetail().toString());


                //Audit Data
                rmAuditor.add(userId, "corporateService", actionDesc, requestTime, ActionResult.SUCCEED, resSearchCorporateCustomer.getHeader().getResDesc(), responseTime, linkKey);

                //Check Success
                log.debug("requestServiceDescription : {}", resSearchCorporateCustomer.getHeader().getResDesc());
                if (resSearchCorporateCustomer.getHeader().getResCode().equals("0000")) {
                    corporateModel = new CorporateModel();
                    //checkSearchResult
                    if (resSearchCorporateCustomer.getBody().getSearchResult().equals("CL")) {
                        throw new ValidationException(validationMsg.get(ValidationMapping.CUSTOMER_RESULT_MULTIPLE));
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
                    corporateModel.setSubdistrict(Util.replaceStringToBlank(resSearchCorporateCustomer.getBody().getCorporateCustomerDetailSection().getCorporateDetail().getAddrTumbon(),"แขวง"));
                    corporateModel.setDistrict(Util.replaceStringToBlank(resSearchCorporateCustomer.getBody().getCorporateCustomerDetailSection().getCorporateDetail().getAddrAumper(),"เขต"));
                    corporateModel.setProvince(resSearchCorporateCustomer.getBody().getCorporateCustomerDetailSection().getCorporateDetail().getCity());
                    corporateModel.setPostcode(resSearchCorporateCustomer.getBody().getCorporateCustomerDetailSection().getCorporateDetail().getPostalCd());
                    corporateModel.setCountry(resSearchCorporateCustomer.getBody().getCorporateCustomerDetailSection().getCorporateDetail().getCtry());
                    corporateModel.setCountryCode(resSearchCorporateCustomer.getBody().getCorporateCustomerDetailSection().getCorporateDetail().getIsoCtryCode());

                    RegistrationAddress registrationAddress = new RegistrationAddress();
                    registrationAddress.setSubdistrict(Util.replaceStringToBlank(resSearchCorporateCustomer.getBody().getCorporateCustomerDetailSection().getCorporateDetail().getComRegTumbon(),"แขวง"));
                    registrationAddress.setDistrict(Util.replaceStringToBlank(resSearchCorporateCustomer.getBody().getCorporateCustomerDetailSection().getCorporateDetail().getComRegAumper(),"เขต"));
                    registrationAddress.setProvince(resSearchCorporateCustomer.getBody().getCorporateCustomerDetailSection().getCorporateDetail().getCity2());
//                    registrationAddress.setPostcode(resSearchCorporateCustomer.getBody().getCorporateCustomerDetailSection().getCorporateDetail().get);
                    registrationAddress.setCountry(resSearchCorporateCustomer.getBody().getCorporateCustomerDetailSection().getCorporateDetail().getCtry2());
                    registrationAddress.setCountryCode(resSearchCorporateCustomer.getBody().getCorporateCustomerDetailSection().getCorporateDetail().getIsoCtryCode2());
                    registrationAddress.setPhoneNo(resSearchCorporateCustomer.getBody().getCorporateCustomerDetailSection().getCorporateDetail().getTelephoneNumber1());
                    registrationAddress.setExtension(resSearchCorporateCustomer.getBody().getCorporateCustomerDetailSection().getCorporateDetail().getExtension1());
                    registrationAddress.setContactName(resSearchCorporateCustomer.getBody().getCorporateCustomerDetailSection().getCorporateDetail().getContactPerson());
                    registrationAddress.setContactPhoneNo(resSearchCorporateCustomer.getBody().getCorporateCustomerDetailSection().getCorporateDetail().getTelephoneNbr());


                    corporateModel.setRegistrationAddress(registrationAddress);

                    log.debug("responseCode: {}", resSearchCorporateCustomer.getHeader().getResCode());
                } else if (resSearchCorporateCustomer.getHeader().getResCode().equals("1500")) { //Host parameter is null
                    throw new ValidationException(exceptionMsg.get(ExceptionMapping.HOST_PARAMETER_IS_NULL));
                } else if (resSearchCorporateCustomer.getHeader().getResCode().equals("1511")) { //Data Not Found
                    log.debug("Data Not Found!");
                    corporateModel = new CorporateModel();

                } else if (resSearchCorporateCustomer.getHeader().getResCode().equals("3500")) {  //fail
                    throw new ValidationException(exceptionMsg.get(ExceptionMapping.FAIL));
                }  //check null
            } else {
                log.warn(" resSearchCorporateCustomer : Null");
                //Audit Data
                rmAuditor.add(userId, "corporateService", actionDesc, requestTime, ActionResult.EXCEPTION, "responseCorporateCustomer : Null", new Date(), linkKey);
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Exception :{}", e.getMessage());
            //Audit Data
            rmAuditor.add(userId, "corporateService", actionDesc, requestTime, ActionResult.FAILED, e.getMessage(), new Date(), linkKey);
        }
        log.debug("CorporateService() END");
        return corporateModel;
    }

    /**
     * ***************************  CustomerAccount  *********************************
     */
    public CustomerAccountModel customerAccountService(SearchCustomerAccountModel searchCustomerAccountModel,String userId) throws Exception {
        log.debug("CustomerAccountService() START");
        CustomerAccountModel customerAccountModel = null;

        //Validate ReqId
        if (!ValidationUtil.isValueInRange(1, 50, searchCustomerAccountModel.getReqId().length())) {
            if(searchCustomerAccountModel.getReqId()==null||searchCustomerAccountModel.getReqId().equals("")){
                throw new ValidationException(validationMsg.get(ValidationMapping.DATA_REQUIRED,"(reqId)"));
            }else{
                throw new ValidationException(validationMsg.get(ValidationMapping.FIELD_LENGTH_INVALID,"(reqId)"));
            }
        }
        //Validate Acronym
        if (!ValidationUtil.isValueInRange(1, 20, searchCustomerAccountModel.getAcronym().length())) {
            if(searchCustomerAccountModel.getAcronym()==null||searchCustomerAccountModel.getAcronym().equals("")){
                throw new ValidationException(validationMsg.get(ValidationMapping.DATA_REQUIRED,"(acronym)"));
            }else{
                throw new ValidationException(validationMsg.get(ValidationMapping.FIELD_LENGTH_INVALID,"(acronym)"));
            }
        }
        //Validate ProductCode
        if (!ValidationUtil.isValueInRange(1, 8, searchCustomerAccountModel.getProductCode().length())) {
            if(searchCustomerAccountModel.getProductCode()==null||searchCustomerAccountModel.getProductCode().equals("")){
                throw new ValidationException(validationMsg.get(ValidationMapping.DATA_REQUIRED,"(productCode)"));
            }else{
                throw new ValidationException(validationMsg.get(ValidationMapping.FIELD_LENGTH_INVALID,"(productCode)"));
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
            throw new ValidationException(validationMsg.get(ValidationMapping.FIELD_LENGTH_INVALID,"(custNbr)"));
        }
        //Validate RadSelectSearch
        if (!ValidationUtil.isValueInRange(1, 10, searchCustomerAccountModel.getRadSelectSearch().length())) {
            if(searchCustomerAccountModel.getRadSelectSearch()==null||searchCustomerAccountModel.getRadSelectSearch().equals("")){
                throw new ValidationException(validationMsg.get(ValidationMapping.DATA_REQUIRED,"(radSelectSearch)"));
            }else{
                throw new ValidationException(validationMsg.get(ValidationMapping.FIELD_LENGTH_INVALID,"(radSelectSearch)"));
            }
        }
        log.debug("Validate Pass!");
        //setHeader
        com.tmb.common.data.requestsearchcustomeraccount.Header header = new com.tmb.common.data.requestsearchcustomeraccount.Header();
        header.setReqId(searchCustomerAccountModel.getReqId());
        header.setAcronym(searchCustomerAccountModel.getAcronym());
        header.setProductCode(searchCustomerAccountModel.getProductCode());
//        header.setServerURL("");
//        header.setSessionId("");

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
                log.debug("accountListSize: {}", resSearchCustomerAccount.getBody().getAccountList().size());
                for (int i = 0; i < resSearchCustomerAccount.getBody().getAccountList().size(); i++) {
                    log.debug("accountListData " + i + 1 + " : {}", resSearchCustomerAccount.getBody().getAccountList().get(i).toString());
                }
                customerAccountModel = new CustomerAccountModel();
                customerAccountModel.setResCode(resSearchCustomerAccount.getHeader().getResCode());
                customerAccountModel.setResDesc(resSearchCustomerAccount.getHeader().getResDesc());

                //Audit Data
                rmAuditor.add(userId, "customerAccountService", actionDesc, requestTime, ActionResult.SUCCEED, resSearchCustomerAccount.getHeader().getResDesc(), new Date(), linkKey);

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
                    customerAccountModel.setAccountBody(listModelList);

                } else if (resSearchCustomerAccount.getHeader().getResCode().equals("1500")) { //Host Parameter is Null
                    throw new ValidationException(exceptionMsg.get(ExceptionMapping.HOST_PARAMETER_IS_NULL));
                } else if (resSearchCustomerAccount.getHeader().getResCode().equals("1511")) { //Data Not Found
                    log.debug("Data Not Found!");
                    List<CustomerAccountListModel> listModelList = new ArrayList<CustomerAccountListModel>();
                    log.info(listModelList.size() + "");
                    customerAccountModel.setAccountBody(listModelList);
                } else if (resSearchCustomerAccount.getHeader().getResCode().equals("3500")) { //fail
                    throw new ValidationException(exceptionMsg.get(ExceptionMapping.FAIL));
                }
                //check null
            } else {
                log.warn(" resSearchCustomerAccount : Null");
                //Audit Data
                rmAuditor.add(userId, "customerAccountService", actionDesc, requestTime, ActionResult.EXCEPTION, "responseCustomerAccount : Null", new Date(), linkKey);
            }

        } catch (Exception e) {
            e.printStackTrace();
            log.error("Exception :{}", e.getMessage());
            //Audit Data
            rmAuditor.add(userId, "customerAccountService", actionDesc, requestTime, ActionResult.FAILED, e.getMessage(), new Date(), linkKey);
        }

        log.debug("CorporateService() END");
        return customerAccountModel;
    }


    // Services
    private ResSearchIndividualCustomer callServiceIndividual(ReqSearchIndividualCustomer reqSearch) throws Exception {
        log.debug("callServiceIndividual() START");
        ResSearchIndividualCustomer resSearchIndividualCustomer = null;
        URL url = this.getClass().getResource("/com/tmb/EAISearchIndividualCustomer.wsdl");
        QName qname = new QName("http://data.sme.tmb.com/EAISearchIndividualCustomer/", "EAISearchIndividualCustomer");
        EAISearchIndividualCustomer_Service service = new EAISearchIndividualCustomer_Service(url, qname);
        EAISearchIndividualCustomer eaiSearchInd = service.getEAISearchIndividualCustomer();
        ((BindingProvider) eaiSearchInd).getRequestContext().put(BindingProviderProperties.REQUEST_TIMEOUT, requestTimeout);
        ((BindingProvider) eaiSearchInd).getRequestContext().put(BindingProviderProperties.CONNECT_TIMEOUT, connectTimeout);
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
        ((BindingProvider) eaiSearchCor).getRequestContext().put(BindingProviderProperties.REQUEST_TIMEOUT, requestTimeout);
        ((BindingProvider) eaiSearchCor).getRequestContext().put(BindingProviderProperties.CONNECT_TIMEOUT, connectTimeout);
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
        ((BindingProvider) eaiSearchCa).getRequestContext().put(BindingProviderProperties.REQUEST_TIMEOUT, requestTimeout);
        ((BindingProvider) eaiSearchCa).getRequestContext().put(BindingProviderProperties.CONNECT_TIMEOUT, connectTimeout);
        ((BindingProvider) eaiSearchCa).getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY,
                customerAccountAddress);
        resSearchCustomerAccount = eaiSearchCa.searchCustomerAccount(reqSearch);
        log.debug("callServiceCustomerAccount() END");
        return resSearchCustomerAccount;
    }


}
