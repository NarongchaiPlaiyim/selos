package com.clevel.selos.controller;

import com.clevel.selos.dao.testdao.CardTypeDao;
import com.clevel.selos.model.db.testrm.CardType;
import com.clevel.selos.model.viewmodel.CardTypeView;
import com.clevel.selos.model.viewmodel.SearchIndividual;
import com.tmb.sme.data.eaisearchindividualcustomer.EAISearchIndividualCustomer;
import com.tmb.sme.data.eaisearchindividualcustomer.EAISearchIndividualCustomer_Service;
import com.tmb.sme.data.requestsearchindividualcustomer.Body;
import com.tmb.sme.data.requestsearchindividualcustomer.Header;
import com.tmb.sme.data.requestsearchindividualcustomer.ReqSearchIndividualCustomer;
import com.tmb.sme.data.responsesearchindividualcustomer.ResSearchIndividualCustomer;
import org.slf4j.Logger;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import javax.xml.crypto.dsig.Transform;
import javax.xml.namespace.QName;
import javax.xml.soap.*;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;

import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
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


    List<CardTypeView> listhardcode;



    String printrequest;

    String printresponse;

    public TestRM(){
    }

    @PostConstruct
    public void onCreate(){
//        log.debug("============================================================23");
//        list = dao.findAll();
          listhardcode=new ArrayList<CardTypeView>();
          listhardcode.add(new CardTypeView("NAME","VALUE"));
        searchIndividual = new SearchIndividual();
        searchIndividual.setCustType("P");



    }
    public void callservice(){
        System.out.println("99999999999999999999999999999999999999999999999999999999999");
//        log.debug("callservice() requestValue : ReqId = " + searchIndividual.getReqId() + " CustType = " + searchIndividual.getCustType());

        //Head
        Header header=new Header();
        header.setReqID(searchIndividual.getReqId());
        //Body
        Body  body=new Body();
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

        try {


            URL url=new URL("/com/tmb/sme/data/EAISearchIndividualCustomer.wsdl");
            QName qName=new QName("http://data.sme.tmb.com/EAISearchIndividualCustomer/", "EAISearchIndividualCustomer");
////            Send Request
            System.out.println("11111111111111111111111111111111111");
        EAISearchIndividualCustomer_Service service =new EAISearchIndividualCustomer_Service(url,qName);
        EAISearchIndividualCustomer port=service.getEAISearchIndividualCustomer();
                             port.searchIndividualCustomer(reqSearch);


//             setPrintresponse("Response1 :"+resSearchIndividualCustomer.toString() +"Response2 :"+resSearchIndividualCustomer.getHeader().getResCode());

        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
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

    public String getPrintrequest() {
        return printrequest;
    }

    public void setPrintrequest(String printrequest) {
        this.printrequest = printrequest;
    }

    public String getPrintresponse() {
        return printresponse;
    }

    public void setPrintresponse(String printresponse) {
        this.printresponse = printresponse;
    }
}
