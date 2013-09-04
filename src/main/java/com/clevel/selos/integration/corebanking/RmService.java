package com.clevel.selos.integration.corebanking;

import com.clevel.selos.exception.ValidationException;
import com.clevel.selos.model.RMmodel.*;
import com.tmb.sme.data.eaisearchcorporatecustomer.EAISearchCorporateCustomer;
import com.tmb.sme.data.eaisearchcorporatecustomer.EAISearchCorporateCustomer_Service;
import com.tmb.sme.data.eaisearchindividualcustomer.EAISearchIndividualCustomer;
import com.tmb.sme.data.eaisearchindividualcustomer.EAISearchIndividualCustomer_Service;
import com.tmb.sme.data.requestsearchcorporatecustomer.ReqSearchCorporateCustomer;
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


public class RmService implements Serializable{

    @Inject
    Logger log;



    public IndividualModel intiIndividual(SearchIndividual searchIndividual) throws Exception {

        //Validate
        if(searchIndividual.getReqId().length()<1 || searchIndividual.getReqId().length()>50){
            throw new ValidationException("RegisterID Invalid");
        }
        if(searchIndividual.getCustType().length()!=1){
            throw new ValidationException("CustType Invalid");
        }
        if(searchIndividual.getType().length()!=2){
            throw new ValidationException("Type Invalid");
        }
        if(searchIndividual.getCustId().length()>25){
            throw new ValidationException("CustomerId Invalid");
        }
        if(searchIndividual.getCustNbr().length()>14){
            throw new ValidationException("CustNbr Invalid");
        }
        if(searchIndividual.getCustName().length()>40){
            throw new ValidationException("CustName Invalid");
        }
        if(searchIndividual.getCustSurname().length()>40){
            throw new ValidationException("CustSurname Invalid");
        }
        if(searchIndividual.getRadSelectSearch().length()<1 || searchIndividual.getRadSelectSearch().length()>10){
            throw new ValidationException("RadSelectSearch Invalid");
        }

        com.tmb.sme.data.requestsearchindividualcustomer.Header header=new com.tmb.sme.data.requestsearchindividualcustomer.Header();
        header.setReqID(searchIndividual.getReqId());

        com.tmb.sme.data.requestsearchindividualcustomer.Body  body=new com.tmb.sme.data.requestsearchindividualcustomer.Body();
        body.setCustType(searchIndividual.getCustType());
        body.setType(searchIndividual.getType());
        body.setCustId(searchIndividual.getCustId());
        body.setCustNbr(searchIndividual.getCustNbr());
        body.setCustName(searchIndividual.getCustName());
        body.setCustSurname(searchIndividual.getCustSurname());
        body.setRadSelectSearch(searchIndividual.getRadSelectSearch());

        ReqSearchIndividualCustomer reqSearch=new ReqSearchIndividualCustomer();
        reqSearch.setHeader(header);
        reqSearch.setBody(body);


        ResSearchIndividualCustomer resSearchIndividualCustomer = callserviceIndividual(reqSearch);

        IndividualModel individualModel =new IndividualModel();
        individualModel.setResCode(resSearchIndividualCustomer.getHeader().getResCode());
        individualModel.setResDesc(resSearchIndividualCustomer.getHeader().getResDesc());

        //Check Success
        if(resSearchIndividualCustomer.getHeader().getResCode().equals("0000")){

        individualModel.setSearchResult(resSearchIndividualCustomer.getBody().getSearchResult());
            //checkSearchResult
            if( individualModel.getSearchResult().equals("CL")){
                throw new ValidationException("Customer List for multiple customers result");
            }
        individualModel.setLastPageFlag(resSearchIndividualCustomer.getBody().getLastPageFlag());
          //personal detail session
        individualModel.setTitle(resSearchIndividualCustomer.getBody().getPersonalDetailSection().getPersonalDetail().getTitle());
        individualModel.setCustId(resSearchIndividualCustomer.getBody().getPersonalDetailSection().getPersonalDetail().getCustId());
        individualModel.setTelephone1(resSearchIndividualCustomer.getBody().getPersonalDetailSection().getPersonalDetail().getTelephoneNumber1());

//          //personal list session

        if(resSearchIndividualCustomer.getBody().getPersonalListSection()!=null && (resSearchIndividualCustomer.getBody().getPersonalListSection().getPersonalList()!=null && resSearchIndividualCustomer.getBody().getPersonalListSection().getPersonalList().size()>0)){

        int perlistsize=resSearchIndividualCustomer.getBody().getPersonalListSection().getPersonalList().size();

//
        IndividualPersonalList resultPersonalList=null;
        ArrayList<IndividualPersonalList>list=new ArrayList<IndividualPersonalList>();
               if(perlistsize!=0){
                         for(int i =0;i<perlistsize;i++){
                                 resultPersonalList=new IndividualPersonalList();
                                 resultPersonalList.setAddress(resSearchIndividualCustomer.getBody().getPersonalListSection().getPersonalList().get(i).getAddress1());

                             list.add(resultPersonalList);
                          }
                   individualModel.setPersonalLists(list);
               }
        }

        System.out.println("RMServiceeeeeeeeeeeeeeeeeeeeeeeeeeeeeee : "+ individualModel.getResCode());

     }
        return individualModel;
    }


    public CorporateModel intiCorporate(SearchIndividual searchIndividual) throws Exception {

        System.out.println("======================  " +searchIndividual.getReqId());
        if(searchIndividual.getReqId().length()<1 || searchIndividual.getReqId().length()>50){
            throw new ValidationException("ReID Invalid");
        }
        if(searchIndividual.getCustType().length()!=1){
            throw new ValidationException("CustType Invalid");
        }
        if(searchIndividual.getType().length()!=2){
            throw new ValidationException("Type Invalid");
        }
        if(searchIndividual.getCustId().length()>25){
            throw new ValidationException("CustId Invalid");
        }
        if(searchIndividual.getCustNbr().length()>14){
            throw new ValidationException("CustNbr Invalid");
        }
        if(searchIndividual.getCustName().length()>40){
            throw new ValidationException("CustName Invalid");
        }
        if(searchIndividual.getRadSelectSearch().length()<1 || searchIndividual.getRadSelectSearch().length()>10){
            throw new ValidationException("RadSelectSearch Invalid");
        }

        com.tmb.sme.data.requestsearchcorporatecustomer.Header header=new com.tmb.sme.data.requestsearchcorporatecustomer.Header();
        header.setReqID(searchIndividual.getReqId());

        com.tmb.sme.data.requestsearchcorporatecustomer.Body  body=new com.tmb.sme.data.requestsearchcorporatecustomer.Body();
        body.setCustType(searchIndividual.getCustType());
        body.setType(searchIndividual.getType());
        body.setCustId(searchIndividual.getCustId());
        body.setCustNbr(searchIndividual.getCustNbr());
        body.setCustName(searchIndividual.getCustName());
        body.setRadSelectSearch(searchIndividual.getRadSelectSearch());


        ReqSearchCorporateCustomer reqSearch=new ReqSearchCorporateCustomer();
        reqSearch.setHeader(header);
        reqSearch.setBody(body);

        ResSearchCorporateCustomer resSearchCorporateCustomer = callserviceCorporate(reqSearch);

        CorporateModel corporateModel =new CorporateModel();
        corporateModel.setResCode(resSearchCorporateCustomer.getHeader().getResCode());
        corporateModel.setResDesc(resSearchCorporateCustomer.getHeader().getResDesc());

        //Check Success
        if(resSearchCorporateCustomer.getHeader().getResCode().equals("0000")){
            corporateModel.setSearchResult(resSearchCorporateCustomer.getBody().getSearchResult());
            //checkSearchResult
            if( corporateModel.getSearchResult().equals("CL")){
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

            if(resSearchCorporateCustomer.getBody().getCorporateCustomerListSection()!=null && (resSearchCorporateCustomer.getBody().getCorporateCustomerListSection().getCorporateList()!=null &&
                    resSearchCorporateCustomer.getBody().getCorporateCustomerListSection().getCorporateList().size()>0)){

                int perlistsize=resSearchCorporateCustomer.getBody().getCorporateCustomerListSection().getCorporateList().size();

//
                CorporatePersonalList corporatePersonalList=null;
                ArrayList<CorporatePersonalList>list=new ArrayList<CorporatePersonalList>();
                if(perlistsize!=0){
                    for(int i =0;i<perlistsize;i++){
                        corporatePersonalList=new CorporatePersonalList();
                        corporatePersonalList.setCustNbr1(resSearchCorporateCustomer.getBody().getCorporateCustomerListSection().getCorporateList().get(i).getCustNbr1());
                        corporatePersonalList.setcId1(resSearchCorporateCustomer.getBody().getCorporateCustomerListSection().getCorporateList().get(i).getCId1());
                        corporatePersonalList.setCitizenId1(resSearchCorporateCustomer.getBody().getCorporateCustomerListSection().getCorporateList().get(i).getCitizenCId1());
                        corporatePersonalList.setTitle1(resSearchCorporateCustomer.getBody().getCorporateCustomerListSection().getCorporateList().get(i).getTitle1());

                        list.add(corporatePersonalList);
                    }
                    corporateModel.setPersonalList(list);
                }
            }

            System.out.println("RMServiceeeeeeeeeeeeeeeeeeeeeeeeeeeeeee : "+ corporateModel.getResCode());

        }
        return corporateModel;

    }




    // Services

    private ResSearchIndividualCustomer callserviceIndividual(ReqSearchIndividualCustomer reqSearch)throws Exception{
        ResSearchIndividualCustomer resSearchIndividualCustomer=null;


            URL url = this.getClass().getResource("/EAISearchIndividualCustomer.wsdl");
            QName qname = new QName("http://data.sme.tmb.com/EAISearchIndividualCustomer/", "EAISearchIndividualCustomer");
            System.out.println("00000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000");
            EAISearchIndividualCustomer_Service service = new EAISearchIndividualCustomer_Service(url,qname);
            EAISearchIndividualCustomer eaiSearchInd = service.getEAISearchIndividualCustomer();
            ((BindingProvider)eaiSearchInd).getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY ,
                    "http://10.175.140.18:7809/EAISearchIndividualCustomer");

            resSearchIndividualCustomer=eaiSearchInd.searchIndividualCustomer(reqSearch) ;


        return resSearchIndividualCustomer;
    }


    private ResSearchCorporateCustomer callserviceCorporate(ReqSearchCorporateCustomer reqSearch)throws Exception{
        ResSearchCorporateCustomer resSearchCorporateCustomer=null;


            URL url = this.getClass().getResource("/EAISearchCorporateCustomer.wsdl");
            QName qname = new QName("http://data.sme.tmb.com/EAISearchCorporateCustomer/", "EAISearchCorporateCustomer");
            System.out.println("00000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000");
            EAISearchCorporateCustomer_Service service = new EAISearchCorporateCustomer_Service(url,qname);
            EAISearchCorporateCustomer eaiSearchCor = service.getEAISearchCorporateCustomer();
            ((BindingProvider)eaiSearchCor).getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY ,
                    "http://10.175.140.18:7807/EAISearchCorporateCustomer");

            resSearchCorporateCustomer = eaiSearchCor.searchCorporateCustomer(reqSearch);


        return resSearchCorporateCustomer;
    }
}
