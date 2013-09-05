package com.clevel.selos.integration.corebanking;

import com.clevel.selos.exception.ValidationException;
import com.clevel.selos.integration.RM;
import com.clevel.selos.model.CAmodel.CustomerAccountListModel;
import com.clevel.selos.model.CAmodel.CustomerAccountModel;
import com.clevel.selos.model.RMmodel.SearchIndividual;
import com.clevel.selos.system.message.ExceptionMessage;
import com.clevel.selos.system.message.Message;
import com.clevel.selos.system.message.ValidationMessage;
import com.tmb.common.data.eaisearchcustomeraccount.EAISearchCustomerAccount;
import com.tmb.common.data.eaisearchcustomeraccount.EAISearchCustomerAccount_Service;
import com.tmb.common.data.requestsearchcustomeraccount.Body;
import com.tmb.common.data.requestsearchcustomeraccount.Header;
import com.tmb.common.data.requestsearchcustomeraccount.ReqSearchCustomerAccount;
import com.tmb.common.data.responsesearchcustomeraccount.ResSearchCustomerAccount;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;
import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class CAService implements Serializable {
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
    public CAService() {
    }

    public CustomerAccountModel intiCustomerAction(SearchIndividual searchIndividual) throws Exception {
//        log.info("============ CustomerAccount");
//        log.info("dsd");

        if (searchIndividual.getReqId().length() < 1 || searchIndividual.getReqId().length() > 50) {
            throw new ValidationException(validationMsg.get("validation.006"));
        }

        if (searchIndividual.getCustNbr().length() > 14) {
            throw new ValidationException(validationMsg.get("validation.006"));
        }

        if (searchIndividual.getRadSelectSearch().length() < 1 || searchIndividual.getRadSelectSearch().length() > 10) {
            throw new ValidationException(validationMsg.get("validation.006"));
        }

        Header header = new Header();
        header.setReqId(searchIndividual.getReqId());
        header.setAcronym("EAC");
        header.setProductCode("0");
        //header.setServerURL("");
        //header.setSessionId("");


        Body body = new Body();
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
            log.debug("accountListSize: {}", resSearchCustomerAccount.getBody().getAccountList().size());
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
        } else if (resSearchCustomerAccount.getHeader().getResCode().equals("1511")) {
            throw new ValidationException(exceptionMsg.get("exception.1500"));
        } else if (resSearchCustomerAccount.getHeader().getResCode().equals("3500")) {
            throw new ValidationException(exceptionMsg.get("exception.1500"));
        }
        return customerAccountModel;
    }

    // Services
    private ResSearchCustomerAccount callServiceCustomerAction(ReqSearchCustomerAccount reqSearch) throws Exception {
        ResSearchCustomerAccount resSearchCustomerAccount = null;

        URL url = this.getClass().getResource("/com/tmb/EAISearchCustomerAccount.wsdl");
        QName qname = new QName("http://data.common.tmb.com/EAISearchCustomerAccount/", "EAISearchCustomerAccount");
        EAISearchCustomerAccount_Service service = new EAISearchCustomerAccount_Service(url, qname);
        EAISearchCustomerAccount eaiSearchInd = service.getEAISearchCustomerAccount();
        ((BindingProvider) eaiSearchInd).getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY,
                "http://10.175.140.18:7809/services/EAISearchCustomerAccount");

        resSearchCustomerAccount = eaiSearchInd.searchCustomerAccount(reqSearch);

        return resSearchCustomerAccount;
    }
}
