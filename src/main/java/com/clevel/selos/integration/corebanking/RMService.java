package com.clevel.selos.integration.corebanking;

import com.clevel.selos.exception.ValidationException;
import com.clevel.selos.integration.RM;
import com.clevel.selos.model.CAmodel.CustomerAccountListModel;
import com.clevel.selos.model.CAmodel.CustomerAccountModel;
import com.clevel.selos.model.RMmodel.*;
import com.clevel.selos.system.Config;
import com.clevel.selos.system.message.ExceptionMessage;
import com.clevel.selos.system.message.Message;
import com.clevel.selos.system.message.ValidationMessage;
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

    public IndividualModel intiIndividual(SearchIndividual searchIndividual) throws Exception {
        //Validate
        if (searchIndividual.getReqId().length() < 1 || searchIndividual.getReqId().length() > 50) {
            throw new ValidationException("RegisterID Invalid");

        }
        if (searchIndividual.getCustType().length() != 1) {
            throw new ValidationException("CustType Invalid");
        }
        if (searchIndividual.getType().length() != 2) {
            throw new ValidationException("Type Invalid");
        }
        if (searchIndividual.getCustId().length() > 25) {
            throw new ValidationException("CustomerId Invalid");
        }
        if (searchIndividual.getCustNbr().length() > 14) {
            throw new ValidationException("CustNbr Invalid");
        }
        if (searchIndividual.getCustName().length() > 40) {
            throw new ValidationException("CustName Invalid");
        }
        if (searchIndividual.getCustSurname().length() > 40) {
            throw new ValidationException("CustSurname Invalid");
        }
        if (searchIndividual.getRadSelectSearch().length() < 1 || searchIndividual.getRadSelectSearch().length() > 10) {
            throw new ValidationException("RadSelectSearch Invalid");
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

    public CorporateModel intiCorporate(SearchIndividual searchIndividual) throws Exception {
        log.debug("requestId: {}", searchIndividual.getReqId());

        if (searchIndividual.getReqId().length() < 1 || searchIndividual.getReqId().length() > 50) {
            throw new ValidationException("ReID Invalid");
        }
        if (searchIndividual.getCustType().length() != 1) {
            throw new ValidationException("CustType Invalid");
        }
        if (searchIndividual.getType().length() != 2) {
            throw new ValidationException("Type Invalid");
        }
        if (searchIndividual.getCustId().length() > 25) {
            throw new ValidationException("CustId Invalid");
        }
        if (searchIndividual.getCustNbr().length() > 14) {
            throw new ValidationException("CustNbr Invalid");
        }
        if (searchIndividual.getCustName().length() > 40) {
            throw new ValidationException("CustName Invalid");
        }
        if (searchIndividual.getRadSelectSearch().length() < 1 || searchIndividual.getRadSelectSearch().length() > 10) {
            throw new ValidationException("RadSelectSearch Invalid");
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

    public CustomerAccountModel intiCustomerAccount(SearchIndividual searchIndividual) throws Exception {
        log.info("============ CustomerAccount");


        if (searchIndividual.getReqId().length() < 1 || searchIndividual.getReqId().length() > 50) {
            throw new ValidationException(validationMsg.get("validation.006"));
        }

        if (searchIndividual.getCustNbr().length() > 14) {
            throw new ValidationException(validationMsg.get("validation.006"));
        }

        if (searchIndividual.getRadSelectSearch().length() < 1 || searchIndividual.getRadSelectSearch().length() > 10) {
            throw new ValidationException(validationMsg.get("validation.006"));
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
