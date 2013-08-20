package com.clevel.selos.controller;

import com.clevel.selos.dao.testdao.CardTypeDao;
import com.clevel.selos.model.db.testrm.CardType;
import com.clevel.selos.model.viewmodel.SearchIndividual;
import com.tmb.sme.data.eaisearchindividualcustomer.EAISearchIndividualCustomer;
import com.tmb.sme.data.eaisearchindividualcustomer.EAISearchIndividualCustomer_Service;
import com.tmb.sme.data.requestsearchindividualcustomer.Body;
import com.tmb.sme.data.requestsearchindividualcustomer.Header;
import com.tmb.sme.data.requestsearchindividualcustomer.ReqSearchIndividualCustomer;
import org.slf4j.Logger;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import java.io.Serializable;
import java.util.List;



@ViewScoped
@ManagedBean(name="testrm")
public class TestRM implements Serializable{
    @Inject
    Logger log;
    @Inject
    CardTypeDao dao;

    SearchIndividual searchIndividual;
    List<CardType> list;

    public TestRM(){
    }

    @PostConstruct
    public void onCreate(){
        list = dao.findAll();

        searchIndividual = new SearchIndividual();
        searchIndividual.setCustType("P");



    }
    public void callservice(){

        System.out.println("callservice() requestValue : ReqId = "+searchIndividual.getReqId()+" CustType = "+searchIndividual.getCustType());
        com.tmb.sme.data.requestsearchindividualcustomer.Header header=new Header();
        header.setReqID(searchIndividual.getReqId());

        com.tmb.sme.data.requestsearchindividualcustomer.Body  body=new Body();
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

        EAISearchIndividualCustomer_Service service =new EAISearchIndividualCustomer_Service();
        EAISearchIndividualCustomer port=service.getEAISearchIndividualCustomer();
        port.searchIndividualCustomer(reqSearch);
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
}
