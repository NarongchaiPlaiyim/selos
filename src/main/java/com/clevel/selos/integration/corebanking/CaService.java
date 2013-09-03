package com.clevel.selos.integration.corebanking;

import com.clevel.selos.exception.ValidationException;
import com.clevel.selos.model.CAmodel.CustomerAccountListModel;
import com.clevel.selos.model.CAmodel.CustomerAccountModel;
import com.clevel.selos.model.RMmodel.CorporateModel;
import com.clevel.selos.model.RMmodel.CorporatePersonalList;
import com.clevel.selos.model.RMmodel.SearchIndividual;
import com.tmb.common.data.eaisearchcustomeraccount.EAISearchCustomerAccount;
import com.tmb.common.data.eaisearchcustomeraccount.EAISearchCustomerAccount_Service;
import com.tmb.common.data.requestsearchcustomeraccount.ReqSearchCustomerAccount;
import com.tmb.common.data.responsesearchcustomeraccount.ResSearchCustomerAccount;
import com.tmb.sme.data.eaisearchindividualcustomer.EAISearchIndividualCustomer;
import com.tmb.sme.data.eaisearchindividualcustomer.EAISearchIndividualCustomer_Service;
import com.tmb.sme.data.requestsearchcorporatecustomer.ReqSearchCorporateCustomer;
import com.tmb.sme.data.requestsearchindividualcustomer.ReqSearchIndividualCustomer;
import com.tmb.sme.data.responsesearchcorporatecustomer.ResSearchCorporateCustomer;
import com.tmb.sme.data.responsesearchindividualcustomer.ResSearchIndividualCustomer;

import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;
import java.io.Serializable;
import java.math.BigDecimal;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: sahawat
 * Date: 30/8/2556
 * Time: 13:33 น.
 * To change this template use File | Settings | File Templates.
 */
public class CaService implements Serializable{

    public CustomerAccountModel intiCustomerAction(SearchIndividual searchIndividual) throws Exception {

        System.out.println("======================  " +searchIndividual.getReqId());
        if(searchIndividual.getReqId().length()<1 || searchIndividual.getReqId().length()>50){
            throw new ValidationException("ReID Invalid");
        }

        if(searchIndividual.getCustNbr().length()>14){
            throw new ValidationException("CustNbr Invalid");
        }

        if(searchIndividual.getRadSelectSearch().length()<1 || searchIndividual.getRadSelectSearch().length()>10){
            throw new ValidationException("RadSelectSearch Invalid");
        }

        com.tmb.common.data.requestsearchcustomeraccount.Header header=new com.tmb.common.data.requestsearchcustomeraccount.Header();
        header.setReqId(searchIndividual.getReqId());
        header.setAcronym("EAC");
        header.setProductCode("0");
        //header.setServerURL("");
        //header.setSessionId("");


        com.tmb.common.data.requestsearchcustomeraccount.Body  body=new com.tmb.common.data.requestsearchcustomeraccount.Body();
        body.setCustNbr(searchIndividual.getCustNbr());
        body.setRadSelectSearch(searchIndividual.getRadSelectSearch());

        ReqSearchCustomerAccount reqSearch=new ReqSearchCustomerAccount();
        reqSearch.setHeader(header);
        reqSearch.setBody(body);

        ResSearchCustomerAccount resSearchCustomerAccount = callserviceCustomerAction(reqSearch);

        CustomerAccountModel customerAccountModel =new CustomerAccountModel();
        customerAccountModel.setResCode(resSearchCustomerAccount.getHeader().getResCode());
        customerAccountModel.setResDesc(resSearchCustomerAccount.getHeader().getResDesc());

        //Check Success
        if(resSearchCustomerAccount.getHeader().getResCode().equals("0000")){
            List<CustomerAccountListModel> listModelList=null;
            //checkSearchResult
            System.out.println("hellooooooooooooooooooooooooooooo "+resSearchCustomerAccount.getBody().getAccountList().size());
        if(resSearchCustomerAccount.getBody().getAccountList()!=null&&resSearchCustomerAccount.getBody().getAccountList().size()>0){
            CustomerAccountListModel customerAccountListModel=null;
                 listModelList=new ArrayList<CustomerAccountListModel>();
                 for(int i=0;i<resSearchCustomerAccount.getBody().getAccountList().size();i++){
                     System.out.println("============================================ 111");
                     customerAccountListModel=new CustomerAccountListModel();

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
            System.out.println("cccccccccccccccccc "+listModelList.size());
        }
        return customerAccountModel;

    }




    // Services

    private ResSearchCustomerAccount callserviceCustomerAction(ReqSearchCustomerAccount reqSearch)throws Exception{
        ResSearchCustomerAccount resSearchCustomerAccount=null;


        URL url = this.getClass().getResource("/EAISearchCustomerAccount.wsdl");
        QName qname = new QName("http://data.common.tmb.com/EAISearchCustomerAccount/", "EAISearchCustomerAccount");
        EAISearchCustomerAccount_Service service = new EAISearchCustomerAccount_Service(url,qname);
        EAISearchCustomerAccount eaiSearchInd = service.getEAISearchCustomerAccount();
        ((BindingProvider)eaiSearchInd).getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY ,
                "http://10.175.140.18:7809/services/EAISearchCustomerAccount");

        resSearchCustomerAccount=eaiSearchInd.searchCustomerAccount(reqSearch) ;


        return resSearchCustomerAccount;
    }
}
