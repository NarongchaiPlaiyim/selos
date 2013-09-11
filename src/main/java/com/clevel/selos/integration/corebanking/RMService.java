package com.clevel.selos.integration.corebanking;

import com.clevel.selos.dao.audit.RMActivityDAO;
import com.clevel.selos.exception.ValidationException;
import com.clevel.selos.integration.RM;
import com.clevel.selos.model.ActionResult;
import com.clevel.selos.model.RMmodel.CustomerAccountListModel;
import com.clevel.selos.model.RMmodel.CustomerAccountModel;
import com.clevel.selos.model.RMmodel.*;
import com.clevel.selos.model.db.audit.RMActivity;
import com.clevel.selos.system.Config;
import com.clevel.selos.system.audit.SystemAuditor;
import com.clevel.selos.system.message.ExceptionMessage;
import com.clevel.selos.system.message.Message;
import com.clevel.selos.system.message.ValidationMessage;
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
    public void onCreate(){

    }

    public IndividualModel individualService(SearchIndividual searchIndividual) throws Exception {
        log.debug("::::::::::::::::::::::::::::::::::::  IndividualService() START");
        //Validate ReqId
        if (!ValidationUtil.isValueInRange(1,50,searchIndividual.getReqId().length())) {
            throw new ValidationException(validationMsg.get("010"));
        }
        //Validate CustType
        if (!ValidationUtil.isEqualRange(1,searchIndividual.getCustType().length())) {
            throw new ValidationException(validationMsg.get("011"));
        }
        //Validate Type
        if (!ValidationUtil.isEqualRange(2,searchIndividual.getType().length())) {
            throw new ValidationException(validationMsg.get("012"));
        }
        //Validate CustId
        if (ValidationUtil.isGreaterThan(25,searchIndividual.getCustId().length())) {
            throw new ValidationException(validationMsg.get("013"));
        }
        //Validate CustNbr
        if (ValidationUtil.isGreaterThan(14,searchIndividual.getCustNbr().length())) {
            throw new ValidationException(validationMsg.get("014"));
        }
        //Validate CustName
        if (ValidationUtil.isGreaterThan(40,searchIndividual.getCustName().length())) {
            throw new ValidationException(validationMsg.get("015"));
        }
        //Validate CustSurname
        if (ValidationUtil.isGreaterThan(40,searchIndividual.getCustSurname().length())) {
            throw new ValidationException(validationMsg.get("016"));
        }
        //Validate RadSelectSearch
        if (!ValidationUtil.isValueInRange(1,10,searchIndividual.getRadSelectSearch().length())) {
            throw new ValidationException(validationMsg.get("017"));
        }
        log.debug(":::::::::::::::::::::::::::::::::::: Validate Pass!");
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
        log.debug("============================ Request ==============================");
        log.debug("::::::::::::::::::::::::::::::::::::  requestServiceTime : {}",new Date());
        log.debug("::::::::::::::::::::::::::::::::::::  requestHeaderData : {}",reqSearch.getHeader().toString());
        log.debug("::::::::::::::::::::::::::::::::::::  requestBodyData : {}",reqSearch.getBody().toString());
        ResSearchIndividualCustomer resSearchIndividualCustomer = callServiceIndividual(reqSearch);
        log.debug("============================ Response ==============================");
        log.debug("::::::::::::::::::::::::::::::::::::  responseServiceTime : {}",new Date());
        log.debug("::::::::::::::::::::::::::::::::::::  responseHeaderData : {}",resSearchIndividualCustomer.getHeader().toString());
        log.debug("::::::::::::::::::::::::::::::::::::  responseBodyData : {}",resSearchIndividualCustomer.getBody().getPersonalDetailSection().getPersonalDetail().toString());
        IndividualModel individualModel = new IndividualModel();
        individualModel.setResCode(resSearchIndividualCustomer.getHeader().getResCode());
        individualModel.setResDesc(resSearchIndividualCustomer.getHeader().getResDesc());

        //Check Success
        log.debug("::::::::::::::::::::::::::::::::::::  requestServiceDescription : {}",resSearchIndividualCustomer.getHeader().getResDesc());
        if (resSearchIndividualCustomer.getHeader().getResCode().equals("0000")) {

            individualModel.setSearchResult(resSearchIndividualCustomer.getBody().getSearchResult());
            //checkSearchResult
            if (individualModel.getSearchResult().equals("CL")) {
                throw new ValidationException(validationMsg.get("007"));
            }
            individualModel.setLastPageFlag(resSearchIndividualCustomer.getBody().getLastPageFlag());
            //personal detail session
            individualModel.setTitle(resSearchIndividualCustomer.getBody().getPersonalDetailSection().getPersonalDetail().getTitle());
            individualModel.setCustId(resSearchIndividualCustomer.getBody().getPersonalDetailSection().getPersonalDetail().getCustId());
            individualModel.setName(resSearchIndividualCustomer.getBody().getPersonalDetailSection().getPersonalDetail().getName());
            individualModel.setTelephone1(resSearchIndividualCustomer.getBody().getPersonalDetailSection().getPersonalDetail().getTelephoneNumber1());

//          //personal list session
            if (resSearchIndividualCustomer.getBody().getPersonalListSection() != null && (resSearchIndividualCustomer.getBody().getPersonalListSection().getPersonalList() != null && resSearchIndividualCustomer.getBody().getPersonalListSection().getPersonalList().size() > 0)) {

                int personalListSize = resSearchIndividualCustomer.getBody().getPersonalListSection().getPersonalList().size();
//
                IndividualPersonalList resultPersonalList = null;
                List<IndividualPersonalList> list = new ArrayList<IndividualPersonalList>();
                if (personalListSize != 0) {
                    for (int i = 0; i < personalListSize; i++) {
                        resultPersonalList = new IndividualPersonalList();
                        resultPersonalList.setAddress(resSearchIndividualCustomer.getBody().getPersonalListSection().getPersonalList().get(i).getAddress1());

                        list.add(resultPersonalList);
                    }
                    individualModel.setPersonalLists(list);
                }
            }


        }else if (resSearchIndividualCustomer.getHeader().getResCode().equals("1500")){ //Host Parameter is null
            throw new ValidationException(exceptionMsg.get("501"));
        }else if (resSearchIndividualCustomer.getHeader().getResCode().equals("1511")) { //Data Not Found

        log.debug(":::::::::::::::::::::::::::::::::::: Data Not Found!");
            List<IndividualPersonalList> listModelList = new ArrayList<IndividualPersonalList>();
            IndividualPersonalList resultPersonalList = new IndividualPersonalList();
            listModelList.add(resultPersonalList);
            individualModel.setPersonalLists(listModelList);


        } else if (resSearchIndividualCustomer.getHeader().getResCode().equals("3500")) { //fail
            throw new ValidationException(exceptionMsg.get("502"));
        }
        log.debug(":::::::::::::::::::::::::::::::::::: IndividualService() END");
        return individualModel;
    }



    public CorporateModel corporateService(SearchIndividual searchIndividual) throws Exception {

        log.debug("::::::::::::::::::::::::::::::::::::  CorporateService() START");
        //Validate ReqId
        if (!ValidationUtil.isValueInRange(1,50,searchIndividual.getReqId().length())) {
            throw new ValidationException(validationMsg.get("010"));
        }
        //Validate CustType
        if (!ValidationUtil.isEqualRange(1,searchIndividual.getCustType().length())) {
            throw new ValidationException(validationMsg.get("011"));
        }
        //Validate Type
        if (!ValidationUtil.isEqualRange(2,searchIndividual.getType().length())) {
            throw new ValidationException(validationMsg.get("012"));
        }
        //Validate CustId
        if (ValidationUtil.isGreaterThan(25,searchIndividual.getCustId().length())) {
            throw new ValidationException(validationMsg.get("013"));
        }
        //Validate CustNbr
        if (ValidationUtil.isGreaterThan(14,searchIndividual.getCustNbr().length())) {
            throw new ValidationException(validationMsg.get("014"));
        }
        //Validate CustName
        if (ValidationUtil.isGreaterThan(40,searchIndividual.getCustName().length())) {
            throw new ValidationException(validationMsg.get("015"));
        }
        //Validate RadSelectSearch
        if (!ValidationUtil.isValueInRange(1,10,searchIndividual.getRadSelectSearch().length())) {
            throw new ValidationException(validationMsg.get("017"));
        }
        log.debug("::::::::::::::::::::::::::::::::::::  Validate Pass!");
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

        log.debug("============================ Request ==============================");
        log.debug("::::::::::::::::::::::::::::::::::::  requestServiceTime : {}",new Date());
        log.debug("::::::::::::::::::::::::::::::::::::  requestHeaderData : {}",reqSearch.getHeader().toString());
        log.debug("::::::::::::::::::::::::::::::::::::  requestBodyData : {}",reqSearch.getBody().toString());
        ResSearchCorporateCustomer resSearchCorporateCustomer = callServiceCorporate(reqSearch);
        log.debug("============================ Response ==============================");
        log.debug("::::::::::::::::::::::::::::::::::::  responseServiceTime : {}",new Date());
        log.debug("::::::::::::::::::::::::::::::::::::  responseHeaderData : {}",resSearchCorporateCustomer.getHeader().toString());
        log.debug("::::::::::::::::::::::::::::::::::::  responseBodyData : {}",resSearchCorporateCustomer.getBody().getCorporateCustomerDetailSection().getCorporateDetail().toString());
        CorporateModel corporateModel = new CorporateModel();
        corporateModel.setResCode(resSearchCorporateCustomer.getHeader().getResCode());
        corporateModel.setResDesc(resSearchCorporateCustomer.getHeader().getResDesc());

        //Check Success
        log.debug("::::::::::::::::::::::::::::::::::::  requestServiceDescription : {}",resSearchCorporateCustomer.getHeader().getResDesc());
        if (resSearchCorporateCustomer.getHeader().getResCode().equals("0000")) {
            corporateModel.setSearchResult(resSearchCorporateCustomer.getBody().getSearchResult());
            //checkSearchResult
            if (corporateModel.getSearchResult().equals("CL")) {
                throw new ValidationException(validationMsg.get("007"));
            }
            //personal detail session
            corporateModel.setTitle(resSearchCorporateCustomer.getBody().getCorporateCustomerDetailSection().getCorporateDetail().getTitle());
            corporateModel.setCustNbr(resSearchCorporateCustomer.getBody().getCorporateCustomerDetailSection().getCorporateDetail().getCustNbr());
            corporateModel.setThaiName1(resSearchCorporateCustomer.getBody().getCorporateCustomerDetailSection().getCorporateDetail().getThaiName1());
            corporateModel.setcId(resSearchCorporateCustomer.getBody().getCorporateCustomerDetailSection().getCorporateDetail().getCId());
            corporateModel.setCitizenId(resSearchCorporateCustomer.getBody().getCorporateCustomerDetailSection().getCorporateDetail().getCitizenCId());
            corporateModel.setEstDate(resSearchCorporateCustomer.getBody().getCorporateCustomerDetailSection().getCorporateDetail().getEstDate());

//          //personal list session
            if (resSearchCorporateCustomer.getBody().getCorporateCustomerListSection() != null && (resSearchCorporateCustomer.getBody().getCorporateCustomerListSection().getCorporateList() != null &&
                    resSearchCorporateCustomer.getBody().getCorporateCustomerListSection().getCorporateList().size() > 0)) {
                int corporateListSize = resSearchCorporateCustomer.getBody().getCorporateCustomerListSection().getCorporateList().size();
//
                CorporatePersonalList corporatePersonalList = null;
                List<CorporatePersonalList> list = new ArrayList<CorporatePersonalList>();
                if (corporateListSize != 0) {
                    for (int i = 0; i < corporateListSize; i++) {
                        corporatePersonalList = new CorporatePersonalList();
                        corporatePersonalList.setCustNbr1(resSearchCorporateCustomer.getBody().getCorporateCustomerListSection().getCorporateList().get(i).getCustNbr1());
                        corporatePersonalList.setcId1(resSearchCorporateCustomer.getBody().getCorporateCustomerListSection().getCorporateList().get(i).getCId1());
                        corporatePersonalList.setCitizenId1(resSearchCorporateCustomer.getBody().getCorporateCustomerListSection().getCorporateList().get(i).getCitizenCId1());
                        corporatePersonalList.setTitle1(resSearchCorporateCustomer.getBody().getCorporateCustomerListSection().getCorporateList().get(i).getTitle1());

                        list.add(corporatePersonalList);
                    }
                    corporateModel.setPersonalList(list);
                }
            }
            log.debug("responseCode: {}", corporateModel.getResCode());
        }else if (resSearchCorporateCustomer.getHeader().getResCode().equals("1500")) { //Host parameter is null
            throw new ValidationException(exceptionMsg.get("501"));
        } else if (resSearchCorporateCustomer.getHeader().getResCode().equals("1511")) { //Data Not Found
            log.debug(":::::::::::::::::::::::::::::::::::: Data Not Found!");
            List<CorporatePersonalList> listModelList = new ArrayList<CorporatePersonalList>();
            CorporatePersonalList corporatePersonalList = new CorporatePersonalList();
            listModelList.add(corporatePersonalList);
            corporateModel.setPersonalList(listModelList);


        } else if (resSearchCorporateCustomer.getHeader().getResCode().equals("3500")) {  //fail
            throw new ValidationException(exceptionMsg.get("502"));
        }
        log.debug("::::::::::::::::::::::::::::::::::::  CorporateService() END");
        return corporateModel;
    }

    /******************************  CustomerAccount  **********************************/
    public CustomerAccountModel customerAccountService(SearchCustomerAccountModel searchCustomerAccountModel) throws Exception {
        log.debug("::::::::::::::::::::::::::::::::::::  CustomerAccountService() START");

        //Validate ReqId
        if (!ValidationUtil.isValueInRange(1,50,searchCustomerAccountModel.getReqId().length())) {
            throw new ValidationException(validationMsg.get("010"));
        }
        //Validate Acronym
        if (!ValidationUtil.isValueInRange(1,20,searchCustomerAccountModel.getAcronym().length())) {
            throw new ValidationException(validationMsg.get("018"));
        }
        //Validate ProductCode
        if (!ValidationUtil.isValueInRange(1,8,searchCustomerAccountModel.getProductCode().length())) {
            throw new ValidationException(validationMsg.get("019"));
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
        if (ValidationUtil.isGreaterThan(14,searchCustomerAccountModel.getCustNbr().length())) {
            throw new ValidationException(validationMsg.get("014"));
        }
        //Validate RadSelectSearch
        if (!ValidationUtil.isValueInRange(1,10,searchCustomerAccountModel.getRadSelectSearch().length())) {
            throw new ValidationException(validationMsg.get("017"));
        }
        log.debug("::::::::::::::::::::::::::::::::::::  Validate Pass!");
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

//        log.debug("::::::::::::::::::::::::::::::::::::  ");
        ReqSearchCustomerAccount reqSearch = new ReqSearchCustomerAccount();
        reqSearch.setHeader(header);
        reqSearch.setBody(body);
        CustomerAccountModel customerAccountModel=null;
        Date requestTime=new Date();
        try{
        log.debug("============================ Request ==============================");
        log.debug("::::::::::::::::::::::::::::::::::::  requestServiceTime : {}",requestTime);
        log.debug("::::::::::::::::::::::::::::::::::::  requestHeaderData : {}",reqSearch.getHeader().toString());
        log.debug("::::::::::::::::::::::::::::::::::::  requestBodyData : {}",reqSearch.getBody().toString());
        ResSearchCustomerAccount resSearchCustomerAccount = callServiceCustomerAccount(reqSearch);
        Date responseTime=new Date();
        log.debug("============================ Response ==============================");
        log.debug("::::::::::::::::::::::::::::::::::::  responseServiceTime : {}",responseTime);
        log.debug("::::::::::::::::::::::::::::::::::::  responseHeaderData : {}",resSearchCustomerAccount.getHeader().toString());
        log.debug("::::::::::::::::::::::::::::::::::::  accountListSize: {}", resSearchCustomerAccount.getBody().getAccountList().size());
            for(int i=0 ;i<resSearchCustomerAccount.getBody().getAccountList().size();i++){
        log.debug("::::::::::::::::::::::::::::::::::::  accountListData "+i+1+" : {}",resSearchCustomerAccount.getBody().getAccountList().get(i).toString());
            }
         customerAccountModel = new CustomerAccountModel();
        customerAccountModel.setResCode(resSearchCustomerAccount.getHeader().getResCode());
        customerAccountModel.setResDesc(resSearchCustomerAccount.getHeader().getResDesc());

        //Audit Data
       rmAuditor.add("userid","customerAccountService","ff",new Date(), ActionResult.SUCCEED,"ff",new Date(),"df");

        //Check Success
        log.debug("::::::::::::::::::::::::::::::::::::  requestServiceDescription : {}", resSearchCustomerAccount.getHeader().getResDesc());
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
            throw new ValidationException(exceptionMsg.get("501"));
        } else if (resSearchCustomerAccount.getHeader().getResCode().equals("1511")) { //Data Not Found
            log.debug(":::::::::::::::::::::::::::::::::::: Data Not Found!");
            List<CustomerAccountListModel> listModelList = new ArrayList<CustomerAccountListModel>();
            log.info(listModelList.size()+"");
            customerAccountModel.setAccountBody(listModelList);


        } else if (resSearchCustomerAccount.getHeader().getResCode().equals("3500")) { //fail
            throw new ValidationException(exceptionMsg.get("502"));
        }

        }catch (Exception e){
            e.printStackTrace();
            Date responseTime=new Date();
            //Audit Data
            rmAuditor.add("userid","customerAccountService","ff",requestTime, ActionResult.FAILED,"ff",responseTime,"df");
        }

        log.debug("::::::::::::::::::::::::::::::::::::  CorporateService() END");
        return customerAccountModel;
    }




    // Services
    private ResSearchIndividualCustomer callServiceIndividual(ReqSearchIndividualCustomer reqSearch) throws Exception {
        log.debug("::::::::::::::::::::::::::::::::::::  callServiceIndividual() START");
        ResSearchIndividualCustomer resSearchIndividualCustomer = null;
        URL url = this.getClass().getResource("/com/tmb/EAISearchIndividualCustomer.wsdl");
        QName qname = new QName("http://data.sme.tmb.com/EAISearchIndividualCustomer/", "EAISearchIndividualCustomer");
        EAISearchIndividualCustomer_Service service = new EAISearchIndividualCustomer_Service(url, qname);
        EAISearchIndividualCustomer eaiSearchInd = service.getEAISearchIndividualCustomer();
        ((BindingProvider) eaiSearchInd).getRequestContext().put(BindingProviderProperties.REQUEST_TIMEOUT,requestTimeout);
        ((BindingProvider) eaiSearchInd).getRequestContext().put(BindingProviderProperties.CONNECT_TIMEOUT,connectTimeout);
        ((BindingProvider) eaiSearchInd).getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY,
                individualAddress);
//                "http://10.175.140.18:7809/EAISearchIndividualCustomer");

        resSearchIndividualCustomer = eaiSearchInd.searchIndividualCustomer(reqSearch);
        log.debug("::::::::::::::::::::::::::::::::::::  callServiceIndividual() END");
        return resSearchIndividualCustomer;
    }

    private ResSearchCorporateCustomer callServiceCorporate(ReqSearchCorporateCustomer reqSearch) throws Exception {
        log.debug("::::::::::::::::::::::::::::::::::::  callServiceCorporate() START");
        ResSearchCorporateCustomer resSearchCorporateCustomer = null;
        URL url = this.getClass().getResource("/com/tmb/EAISearchCorporateCustomer.wsdl");
        QName qname = new QName("http://data.sme.tmb.com/EAISearchCorporateCustomer/", "EAISearchCorporateCustomer");
        EAISearchCorporateCustomer_Service service = new EAISearchCorporateCustomer_Service(url, qname);
        EAISearchCorporateCustomer eaiSearchCor = service.getEAISearchCorporateCustomer();
        ((BindingProvider) eaiSearchCor).getRequestContext().put(BindingProviderProperties.REQUEST_TIMEOUT,requestTimeout);
        ((BindingProvider) eaiSearchCor).getRequestContext().put(BindingProviderProperties.CONNECT_TIMEOUT,connectTimeout);
        ((BindingProvider) eaiSearchCor).getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY,
                corporateAddress);
//        "http://10.175.140.18:7807/EAISearchCorporateCustomer");
        resSearchCorporateCustomer = eaiSearchCor.searchCorporateCustomer(reqSearch);
        log.debug("::::::::::::::::::::::::::::::::::::  callServiceCorporate() END");
        return resSearchCorporateCustomer;
    }
    private ResSearchCustomerAccount callServiceCustomerAccount(ReqSearchCustomerAccount reqSearch) throws Exception {
        log.debug("::::::::::::::::::::::::::::::::::::  callServiceCustomerAccount() START");
        ResSearchCustomerAccount resSearchCustomerAccount = null;
        URL url = this.getClass().getResource("/com/tmb/EAISearchCustomerAccount.wsdl");
        QName qname = new QName("http://data.common.tmb.com/EAISearchCustomerAccount/", "EAISearchCustomerAccount");
        EAISearchCustomerAccount_Service service = new EAISearchCustomerAccount_Service(url, qname);
        EAISearchCustomerAccount eaiSearchCa = service.getEAISearchCustomerAccount();
        ((BindingProvider) eaiSearchCa).getRequestContext().put(BindingProviderProperties.REQUEST_TIMEOUT,requestTimeout);
        ((BindingProvider) eaiSearchCa).getRequestContext().put(BindingProviderProperties.CONNECT_TIMEOUT,connectTimeout);
        ((BindingProvider) eaiSearchCa).getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY,
                customerAccountAddress);

//        "http://10.175.140.18:7809/services/EAISearchCustomerAccount");
        resSearchCustomerAccount = eaiSearchCa.searchCustomerAccount(reqSearch);
        log.debug("::::::::::::::::::::::::::::::::::::  callServiceCustomerAccount() END");
        return resSearchCustomerAccount;
    }



}
