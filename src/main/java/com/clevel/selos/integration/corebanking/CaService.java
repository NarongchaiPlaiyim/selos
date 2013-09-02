package com.clevel.selos.integration.corebanking;

import com.clevel.selos.exception.ValidationException;
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
import java.net.URL;
import java.util.ArrayList;

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

            //checkSearchResult
//            if( customerAccountModel.getSearchResult().equals("CL")){
//                throw new ValidationException("Customer List for multiple customers result");
//            }
            //personal detail session
//            customerAccountModel.setRel(resSearchCorporateCustomer.getBody().getCorporateCustomerDetailSection().getCorporateDetail().getTitle());
//            customerAccountModel.setCd(resSearchCorporateCustomer.getBody().getCorporateCustomerDetailSection().getCorporateDetail().getTitle());
//            customerAccountModel.setThaiName1(resSearchCorporateCustomer.getBody().getCorporateCustomerDetailSection().getCorporateDetail().getTitle());
//            customerAccountModel.setcId(resSearchCorporateCustomer.getBody().getCorporateCustomerDetailSection().getCorporateDetail().getTitle());
//            customerAccountModel.setCitizenId(resSearchCorporateCustomer.getBody().getCorporateCustomerDetailSection().getCorporateDetail().getTitle());
//            customerAccountModel.setEstDate(resSearchCorporateCustomer.getBody().getCorporateCustomerDetailSection().getCorporateDetail().getTitle());

//          //personal list session

//            if(resSearchCorporateCustomer.getBody().getCorporateCustomerListSection()!=null && (resSearchCorporateCustomer.getBody().getCorporateCustomerListSection().getCorporateList()!=null &&
//                    resSearchCorporateCustomer.getBody().getCorporateCustomerListSection().getCorporateList().size()>0)){
//
//                int perlistsize=resSearchCorporateCustomer.getBody().getCorporateCustomerListSection().getCorporateList().size();

//
//                CorporatePersonalList corporatePersonalList=null;
//                ArrayList<CorporatePersonalList> list=new ArrayList<CorporatePersonalList>();
//                if(perlistsize!=0){
//                    for(int i =0;i<perlistsize;i++){
//                        corporatePersonalList=new CorporatePersonalList();
//                        corporatePersonalList.setCustNbr1(resSearchCorporateCustomer.getBody().getCorporateCustomerListSection().getCorporateList().get(i).getCustNbr1());
//                        corporatePersonalList.setcId1(resSearchCorporateCustomer.getBody().getCorporateCustomerListSection().getCorporateList().get(i).getCId1());
//                        corporatePersonalList.setCitizenId1(resSearchCorporateCustomer.getBody().getCorporateCustomerListSection().getCorporateList().get(i).getCitizenCId1());
//                        corporatePersonalList.setTitle1(resSearchCorporateCustomer.getBody().getCorporateCustomerListSection().getCorporateList().get(i).getTitle1());
//
//                        list.add(corporatePersonalList);
//                    }
//                    customerAccountModel.setPersonalList(list);
//                }
//            }


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
