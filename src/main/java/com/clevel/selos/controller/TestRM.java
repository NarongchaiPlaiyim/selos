package com.clevel.selos.controller;

import com.clevel.selos.dao.testdao.CardTypeDao;
import com.clevel.selos.model.db.testrm.CardType;
import com.clevel.selos.model.viewmodel.CardTypeView;
import com.clevel.selos.model.viewmodel.SearchIndividual;
import com.tmb.sme.data.eaisearchcorporatecustomer.EAISearchCorporateCustomer;
import com.tmb.sme.data.eaisearchcorporatecustomer.EAISearchCorporateCustomer_Service;
import com.tmb.sme.data.eaisearchindividualcustomer.EAISearchIndividualCustomer;
import com.tmb.sme.data.eaisearchindividualcustomer.EAISearchIndividualCustomer_Service;
import com.tmb.sme.data.requestsearchcorporatecustomer.ReqSearchCorporateCustomer;
import com.tmb.sme.data.requestsearchindividualcustomer.ReqSearchIndividualCustomer;
import com.tmb.sme.data.responsesearchcorporatecustomer.ResSearchCorporateCustomer;
import com.tmb.sme.data.responsesearchindividualcustomer.ResSearchIndividualCustomer;
import org.slf4j.Logger;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;
import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


@ViewScoped
@ManagedBean(name="testrm")
public class TestRM implements Serializable{
    @Inject
//    @Integration(Integration.System.RM)
    Logger log;
    @Inject
    CardTypeDao dao;

    SearchIndividual searchIndividual;
    List<CardType> list;
    List<CardTypeView>listhardcode;
    public static String printResponse;
    public static String printRequest;
    public TestRM(){
    }

    @PostConstruct
    public void onCreate(){
//        log.debug("LOG DEBUGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGG");
//        list = dao.findAll();
        listhardcode=new ArrayList<CardTypeView>();
        listhardcode.add(new CardTypeView("NAME","CI"));

        searchIndividual = new SearchIndividual();
//        searchIndividual.setCustType("P");



    }

    public void IntiIndividual(){
        com.tmb.sme.data.requestsearchindividualcustomer.Header header=new com.tmb.sme.data.requestsearchindividualcustomer.Header();
        header.setReqID(searchIndividual.getReqId());

        com.tmb.sme.data.requestsearchindividualcustomer.Body  body=new com.tmb.sme.data.requestsearchindividualcustomer.Body();
        body.setCustType(searchIndividual.getCustType());
//        body.setType(searchIndividual.getType());
        body.setType("CI");
        body.setCustId(searchIndividual.getCustId());
        body.setCustNbr(searchIndividual.getCustNbr());
        body.setCustName(searchIndividual.getCustName());
        body.setCustSurname(searchIndividual.getCustSurname());
        body.setRadSelectSearch(searchIndividual.getRadSelectSearch());

        ReqSearchIndividualCustomer reqSearch=new ReqSearchIndividualCustomer();
        reqSearch.setHeader(header);
        reqSearch.setBody(body);

     ResSearchIndividualCustomer resSearchIndividualCustomer = callserviceIndividual(reqSearch);
    }

    public void IntiCorporate(){
        com.tmb.sme.data.requestsearchcorporatecustomer.Header header=new com.tmb.sme.data.requestsearchcorporatecustomer.Header();
        header.setReqID(searchIndividual.getReqId());

        com.tmb.sme.data.requestsearchcorporatecustomer.Body  body=new com.tmb.sme.data.requestsearchcorporatecustomer.Body();
        body.setCustType(searchIndividual.getCustType());
//        body.setType(searchIndividual.getType());
        body.setType("CI");
        body.setCustId(searchIndividual.getCustId());
        body.setCustNbr(searchIndividual.getCustNbr());
        body.setCustName(searchIndividual.getCustName());
        body.setRadSelectSearch(searchIndividual.getRadSelectSearch());


        ReqSearchCorporateCustomer reqSearch=new ReqSearchCorporateCustomer();
        reqSearch.setHeader(header);
        reqSearch.setBody(body);

        ResSearchCorporateCustomer resSearchCorporateCustomer = callserviceCorporate(reqSearch);


    }


    // Services

    public ResSearchIndividualCustomer callserviceIndividual(ReqSearchIndividualCustomer reqSearch){
        ResSearchIndividualCustomer resSearchIndividualCustomer=null;
        try{
            System.out.println("callserviceIndividual() requestValue : ReqId = "+searchIndividual.getReqId()+" CustType = "+searchIndividual.getCustType());
            URL url = this.getClass().getResource("/EAISearchIndividualCustomer.wsdl");
            QName qname = new QName("http://data.sme.tmb.com/EAISearchIndividualCustomer/", "EAISearchIndividualCustomer");
            System.out.println("00000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000");
            EAISearchIndividualCustomer_Service service = new EAISearchIndividualCustomer_Service(url,qname);
            EAISearchIndividualCustomer eaiSearchInd = service.getEAISearchIndividualCustomer();
            ((BindingProvider)eaiSearchInd).getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY ,
                    "http://10.175.140.18:7809/EAISearchIndividualCustomer");

            resSearchIndividualCustomer=eaiSearchInd.searchIndividualCustomer(reqSearch) ;

        } catch (Exception ex) {
                ex.printStackTrace();
        }
          return resSearchIndividualCustomer;
    }


    public ResSearchCorporateCustomer callserviceCorporate(ReqSearchCorporateCustomer reqSearch){
        ResSearchCorporateCustomer resSearchCorporateCustomer=null;
        try{
            System.out.println("callserviceCorporate() requestValue : ReqId = "+searchIndividual.getReqId()+" CustType = "+searchIndividual.getCustType());


            URL url = this.getClass().getResource("/EAISearchCorporateCustomer.wsdl");
            QName qname = new QName("http://data.sme.tmb.com/EAISearchCorporateCustomer/", "EAISearchCorporateCustomer");
            System.out.println("00000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000");
            EAISearchCorporateCustomer_Service service = new EAISearchCorporateCustomer_Service(url,qname);
            EAISearchCorporateCustomer eaiSearchCor = service.getEAISearchCorporateCustomer();
            ((BindingProvider)eaiSearchCor).getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY ,
                    "http://10.175.140.18:7807/EAISearchCorporateCustomer");

            resSearchCorporateCustomer = eaiSearchCor.searchCorporateCustomer(reqSearch);



        } catch (Exception ex) {
            ex.printStackTrace();
            setPrintResponse(ex.getMessage());

        }
          return resSearchCorporateCustomer;
    }


    public void changeListener(){
        String value=searchIndividual.getType();

        searchIndividual.setCustSurname(value);
    }



    public SearchIndividual getSearchIndividual() {
        return searchIndividual;
    }

    public void setSearchIndividual(SearchIndividual searchIndividual) {
        this.searchIndividual = searchIndividual;
    }

    public List<CardType> getList() {
        return list;
    }

    public void setList(List<CardType> list) {
        this.list = list;
    }
    public List<CardTypeView> getListhardcode() {
        return listhardcode;
    }

    public void setListhardcode(List<CardTypeView> listhardcode) {
        this.listhardcode = listhardcode;
    }

    public String getPrintResponse() {
        return printResponse;
    }

    public void setPrintResponse(String printResponse) {
        this.printResponse = printResponse;
    }
    public String getPrintRequest() {
        return printRequest;
    }

    public void setPrintRequest(String printRequest) {
        this.printRequest = printRequest;
    }
}
