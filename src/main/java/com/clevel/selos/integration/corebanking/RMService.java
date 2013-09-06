package com.clevel.selos.integration.corebanking;

import com.clevel.selos.exception.ValidationException;
import com.clevel.selos.integration.RM;
import com.clevel.selos.model.RMmodel.CustomerAccountListModel;
import com.clevel.selos.model.RMmodel.CustomerAccountModel;
import com.clevel.selos.model.RMmodel.*;
import com.clevel.selos.system.Config;
import com.clevel.selos.system.message.ExceptionMessage;
import com.clevel.selos.system.message.Message;
import com.clevel.selos.system.message.ValidationMessage;
import com.clevel.selos.util.ValidationUtil;
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

import javax.inject.Inject;
import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;
import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;
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
    @Config(name = "selos.interface.rm.individual.address")
    String individualAddress;
    @Inject
    @Config(name = "selos.interface.rm.juristic.address")
    String corporateAddress;
    @Inject
    @Config(name = "selos.interface.rm.customerAccount.address")
    String customerAccountAddress;


    @Inject
    public RMService() {
    }

    public IndividualModel IndividualService(SearchIndividual searchIndividual) throws Exception {
        log.debug("IndividualService() =====================");
        //Validate ReqId
        if (!ValidationUtil.isValueInRange(1,5,searchIndividual.getReqId().length())) {
            throw new ValidationException(validationMsg.get("validation.010"));
        }
        //Validate CustType
        if (!ValidationUtil.isEqualRange(1,searchIndividual.getCustType().length())) {
            throw new ValidationException(validationMsg.get("validation.011"));
        }
        //Validate Type
        if (!ValidationUtil.isEqualRange(2,searchIndividual.getType().length())) {
            throw new ValidationException(validationMsg.get("validation.012"));
        }
        //Validate CustId
        if (!ValidationUtil.isGreaterThan(25,searchIndividual.getCustId().length())) {
            throw new ValidationException(validationMsg.get("validation.013"));
        }
        //Validate CustNbr
        if (!ValidationUtil.isGreaterThan(14,searchIndividual.getCustNbr().length())) {
            throw new ValidationException(validationMsg.get("validation.014"));
        }
        //Validate CustName
        if (!ValidationUtil.isGreaterThan(40,searchIndividual.getCustName().length())) {
            throw new ValidationException(validationMsg.get("validation.015"));
        }
        //Validate CustSurname
        if (!ValidationUtil.isGreaterThan(40,searchIndividual.getCustSurname().length())) {
            throw new ValidationException(validationMsg.get("validation.016"));
        }
        //Validate RadSelectSearch
        if (!ValidationUtil.isValueInRange(1,10,searchIndividual.getRadSelectSearch().length())) {
            throw new ValidationException(validationMsg.get("validation.017"));
        }

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

        ResSearchIndividualCustomer resSearchIndividualCustomer = callServiceIndividual(reqSearch);

        IndividualModel individualModel = new IndividualModel();
        individualModel.setResCode(resSearchIndividualCustomer.getHeader().getResCode());
        individualModel.setResDesc(resSearchIndividualCustomer.getHeader().getResDesc());

        //Check Success
        if (resSearchIndividualCustomer.getHeader().getResCode().equals("0000")) {

            individualModel.setSearchResult(resSearchIndividualCustomer.getBody().getSearchResult());
            //checkSearchResult
            if (individualModel.getSearchResult().equals("CL")) {
                throw new ValidationException("Customer List for multiple customers result");
            }
            individualModel.setLastPageFlag(resSearchIndividualCustomer.getBody().getLastPageFlag());
            //personal detail session
            individualModel.setTitle(resSearchIndividualCustomer.getBody().getPersonalDetailSection().getPersonalDetail().getTitle());
            individualModel.setCustId(resSearchIndividualCustomer.getBody().getPersonalDetailSection().getPersonalDetail().getCustId());
            individualModel.setTelephone1(resSearchIndividualCustomer.getBody().getPersonalDetailSection().getPersonalDetail().getTelephoneNumber1());

//          //personal list session
            if (resSearchIndividualCustomer.getBody().getPersonalListSection() != null && (resSearchIndividualCustomer.getBody().getPersonalListSection().getPersonalList() != null && resSearchIndividualCustomer.getBody().getPersonalListSection().getPersonalList().size() > 0)) {

                int personalListSize = resSearchIndividualCustomer.getBody().getPersonalListSection().getPersonalList().size();
//
                IndividualPersonalList resultPersonalList = null;
                ArrayList<IndividualPersonalList> list = new ArrayList<IndividualPersonalList>();
                if (personalListSize != 0) {
                    for (int i = 0; i < personalListSize; i++) {
                        resultPersonalList = new IndividualPersonalList();
                        resultPersonalList.setAddress(resSearchIndividualCustomer.getBody().getPersonalListSection().getPersonalList().get(i).getAddress1());

                        list.add(resultPersonalList);
                    }
                    individualModel.setPersonalLists(list);
                }
            }
            log.debug("responseCode: {}", individualModel.getResCode());
        }
        return individualModel;
    }

    public CorporateModel CorporateService(SearchIndividual searchIndividual) throws Exception {

        log.debug("CorporateService() =====================");
        //Validate ReqId
        if (!ValidationUtil.isValueInRange(1,5,searchIndividual.getReqId().length())) {
            throw new ValidationException(validationMsg.get("validation.010"));
        }
        //Validate CustType
        if (!ValidationUtil.isEqualRange(1,searchIndividual.getCustType().length())) {
            throw new ValidationException(validationMsg.get("validation.011"));
        }
        //Validate Type
        if (!ValidationUtil.isEqualRange(2,searchIndividual.getType().length())) {
            throw new ValidationException(validationMsg.get("validation.012"));
        }
        //Validate CustId
        if (!ValidationUtil.isGreaterThan(25,searchIndividual.getCustId().length())) {
            throw new ValidationException(validationMsg.get("validation.013"));
        }
        //Validate CustNbr
        if (!ValidationUtil.isGreaterThan(14,searchIndividual.getCustNbr().length())) {
            throw new ValidationException(validationMsg.get("validation.014"));
        }
        //Validate CustName
        if (!ValidationUtil.isGreaterThan(40,searchIndividual.getCustName().length())) {
            throw new ValidationException(validationMsg.get("validation.015"));
        }
        //Validate RadSelectSearch
        if (!ValidationUtil.isValueInRange(1,10,searchIndividual.getRadSelectSearch().length())) {
            throw new ValidationException(validationMsg.get("validation.017"));
        }

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

        ResSearchCorporateCustomer resSearchCorporateCustomer = callServiceCorporate(reqSearch);

        CorporateModel corporateModel = new CorporateModel();
        corporateModel.setResCode(resSearchCorporateCustomer.getHeader().getResCode());
        corporateModel.setResDesc(resSearchCorporateCustomer.getHeader().getResDesc());

        //Check Success
        if (resSearchCorporateCustomer.getHeader().getResCode().equals("0000")) {
            corporateModel.setSearchResult(resSearchCorporateCustomer.getBody().getSearchResult());
            //checkSearchResult
            if (corporateModel.getSearchResult().equals("CL")) {
                throw new ValidationException("Customer List for multiple customers result");
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
                ArrayList<CorporatePersonalList> list = new ArrayList<CorporatePersonalList>();
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
        }
        return corporateModel;
    }

    public CustomerAccountModel CustomerAccountService(SearchIndividual searchIndividual) throws Exception {
        log.debug("CustomerAccountService() =====================");

        //Validate ReqId
        if (!ValidationUtil.isValueInRange(1,50,searchIndividual.getReqId().length())) {
            throw new ValidationException(validationMsg.get("validation.010"));
        }
//        //Validate Acronym
//        if (!ValidationUtil.isValueInRange(1,20,searchIndividual.getReqId().length())) {
//            throw new ValidationException(validationMsg.get("validation.014"));
//        }
//        //Validate ProductCode
//        if (!ValidationUtil.isValueInRange(1,8,searchIndividual.getReqId().length())) {
//            throw new ValidationException(validationMsg.get("validation.014"));
//        }
//        //Validate ServerURL
//        if (!ValidationUtil.isGreaterThan(100,searchIndividual.getCustNbr().length())) {
//            throw new ValidationException(validationMsg.get("validation.014"));
//        }
//        //Validate SessionId
//        if (!ValidationUtil.isGreaterThan(100,searchIndividual.getCustNbr().length())) {
//            throw new ValidationException(validationMsg.get("validation.014"));
//        }
        //Validate CustNbr
        if (!ValidationUtil.isGreaterThan(14,searchIndividual.getCustNbr().length())) {
            throw new ValidationException(validationMsg.get("validation.014"));
        }
        //Validate RadSelectSearch
        if (!ValidationUtil.isValueInRange(1,10,searchIndividual.getRadSelectSearch().length())) {
            throw new ValidationException(validationMsg.get("validation.017"));
        }

        com.tmb.common.data.requestsearchcustomeraccount.Header header = new com.tmb.common.data.requestsearchcustomeraccount.Header();
        header.setReqId(searchIndividual.getReqId());
        header.setAcronym("EAC");
        header.setProductCode("0");
        //header.setServerURL("");
        //header.setSessionId("");


        com.tmb.common.data.requestsearchcustomeraccount.Body body = new com.tmb.common.data.requestsearchcustomeraccount.Body();
        body.setCustNbr(searchIndividual.getCustNbr());
        body.setRadSelectSearch(searchIndividual.getRadSelectSearch());

        ReqSearchCustomerAccount reqSearch = new ReqSearchCustomerAccount();
        reqSearch.setHeader(header);
        reqSearch.setBody(body);

        ResSearchCustomerAccount resSearchCustomerAccount = callServiceCustomerAction(reqSearch);

        CustomerAccountModel customerAccountModel = new CustomerAccountModel();
        customerAccountModel.setResCode(resSearchCustomerAccount.getHeader().getResCode());
        customerAccountModel.setResDesc(resSearchCustomerAccount.getHeader().getResDesc());

        //Check Success
        if (resSearchCustomerAccount.getHeader().getResCode().equals("0000")) {
            List<CustomerAccountListModel> listModelList = new ArrayList<CustomerAccountListModel>();
            //checkSearchResult
//            log.debug("accountListSize: ", resSearchCustomerAccount.getBody().getAccountList().size());
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
                    listModelList.add(customerAccountListModel);
                }
            }
            customerAccountModel.setAccountBody(listModelList);

        } else if (resSearchCustomerAccount.getHeader().getResCode().equals("1500")) {
            throw new ValidationException(exceptionMsg.get("exception.1500"));
        } else if (resSearchCustomerAccount.getHeader().getResCode().equals("1511")) { //Data Not Found

            List<CustomerAccountListModel> listModelList = new ArrayList<CustomerAccountListModel>();
            log.info(listModelList.size()+"");
            customerAccountModel.setAccountBody(listModelList);


        } else if (resSearchCustomerAccount.getHeader().getResCode().equals("3500")) {
            throw new ValidationException(exceptionMsg.get("exception.1500"));
        }
        return customerAccountModel;
    }

    // Services
    private ResSearchIndividualCustomer callServiceIndividual(ReqSearchIndividualCustomer reqSearch) throws Exception {
        log.debug("callServiceIndividual() ====================");
        ResSearchIndividualCustomer resSearchIndividualCustomer = null;
        URL url = this.getClass().getResource("/com/tmb/EAISearchIndividualCustomer.wsdl");
        QName qname = new QName("http://data.sme.tmb.com/EAISearchIndividualCustomer/", "EAISearchIndividualCustomer");
        EAISearchIndividualCustomer_Service service = new EAISearchIndividualCustomer_Service(url, qname);
        EAISearchIndividualCustomer eaiSearchInd = service.getEAISearchIndividualCustomer();
        ((BindingProvider) eaiSearchInd).getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY,
                individualAddress);

        resSearchIndividualCustomer = eaiSearchInd.searchIndividualCustomer(reqSearch);

        return resSearchIndividualCustomer;
    }

    private ResSearchCorporateCustomer callServiceCorporate(ReqSearchCorporateCustomer reqSearch) throws Exception {
        log.debug("callServiceCorporate() ====================");
        ResSearchCorporateCustomer resSearchCorporateCustomer = null;
        URL url = this.getClass().getResource("/com/tmb/EAISearchCorporateCustomer.wsdl");
        QName qname = new QName("http://data.sme.tmb.com/EAISearchCorporateCustomer/", "EAISearchCorporateCustomer");
        EAISearchCorporateCustomer_Service service = new EAISearchCorporateCustomer_Service(url, qname);
        EAISearchCorporateCustomer eaiSearchCor = service.getEAISearchCorporateCustomer();
        ((BindingProvider) eaiSearchCor).getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY,
                corporateAddress);

        resSearchCorporateCustomer = eaiSearchCor.searchCorporateCustomer(reqSearch);

        return resSearchCorporateCustomer;
    }
    private ResSearchCustomerAccount callServiceCustomerAction(ReqSearchCustomerAccount reqSearch) throws Exception {
        log.debug("callServiceCustomerAction() ====================");
        ResSearchCustomerAccount resSearchCustomerAccount = null;
        URL url = this.getClass().getResource("/com/tmb/EAISearchCustomerAccount.wsdl");
        QName qname = new QName("http://data.common.tmb.com/EAISearchCustomerAccount/", "EAISearchCustomerAccount");
        EAISearchCustomerAccount_Service service = new EAISearchCustomerAccount_Service(url, qname);
        EAISearchCustomerAccount eaiSearchInd = service.getEAISearchCustomerAccount();
        ((BindingProvider) eaiSearchInd).getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY,
                customerAccountAddress);

        resSearchCustomerAccount = eaiSearchInd.searchCustomerAccount(reqSearch);

        return resSearchCustomerAccount;
    }
}
