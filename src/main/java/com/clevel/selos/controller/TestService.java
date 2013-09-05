package com.clevel.selos.controller;

import com.clevel.selos.dao.testdao.CardTypeDao;
import com.clevel.selos.integration.RM;
import com.clevel.selos.integration.corebanking.CAService;
import com.clevel.selos.integration.corebanking.RMService;
import com.clevel.selos.model.CAmodel.CustomerAccountModel;
import com.clevel.selos.model.db.testrm.CardType;
import com.clevel.selos.model.RMmodel.CardTypeView;
import com.clevel.selos.model.RMmodel.CorporateModel;
import com.clevel.selos.model.RMmodel.IndividualModel;
import com.clevel.selos.model.RMmodel.SearchIndividual;
import org.slf4j.Logger;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


@ViewScoped
@ManagedBean(name="testrm")
public class TestService implements Serializable{
    @Inject
    @RM
    Logger log;

    @Inject
    CardTypeDao dao;

    @Inject
    RMService rmService;
    @Inject
    CAService caService;

    SearchIndividual searchIndividual;
    CorporateModel corporateModel;

    List<CardType> list;
    List<CardTypeView>listhardcode;

    public static String printResponse;
    public static String printRequest;
    private String printDetail;

    public TestService(){

    }

    @PostConstruct
    public void onCreate(){
        log.info("LOG DEBUGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGG");
        log.debug("TESTLOGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGG");

//        list = dao.findAll();
        listhardcode=new ArrayList<CardTypeView>();
        listhardcode.add(new CardTypeView("Citizen ID","CI"));
        listhardcode.add(new CardTypeView("Tax Identification Number","TN"));
        listhardcode.add(new CardTypeView("Passport","PP"));
        listhardcode.add(new CardTypeView("Driver License","DL"));
        listhardcode.add(new CardTypeView("Swift Code","SW"));
        listhardcode.add(new CardTypeView("FI Code","FI"));
        listhardcode.add(new CardTypeView("Work Permit","WP"));
        listhardcode.add(new CardTypeView("Alien ID","AI"));
        listhardcode.add(new CardTypeView("Temporary Card","TC"));
        listhardcode.add(new CardTypeView("Other","OT"));

        searchIndividual = new SearchIndividual();
    }

    ////////////////////////////////////////////////////////   call Service

    public void individual() throws Exception {

        IndividualModel individualModel ;
        //callservice
       individualModel = rmService.intiIndividual(searchIndividual);
        //showData
       StringBuffer result=new StringBuffer();
        result.append("==================== Individual Data Demo ===================");
        result.append("\n responseCode : "+ individualModel.getResCode());
        result.append("\n responseDesc : "+ individualModel.getResDesc());
        result.append("\n searchResult : "+ individualModel.getSearchResult());
        result.append("\n lastPageFlag : "+ individualModel.getLastPageFlag());
        result.append("\n\n ===== personal Detail Section =====");
        result.append("\n title : "+ individualModel.getTitle());
        result.append("\n custId : "+ individualModel.getCustId());
        result.append("\n telephone1 : "+ individualModel.getTelephone1());
        result.append("\n\n ===== personal List Section =====");

        if(individualModel.getPersonalLists()!=null ){
        result.append("\n personalListSize : "+ individualModel.getPersonalLists().size());
                for(int i=0;i< individualModel.getPersonalLists().size();i++){
                result.append("\n address : "+ individualModel.getPersonalLists().get(i).getAddress());
                }
        }else{
            result.append("\n personalListSize : null");
        }

        printDetail=result.toString();
    }


    public void corporate() throws Exception {

        corporateModel=new CorporateModel();
        corporateModel = rmService.intiCorporate(searchIndividual);
        //showData
        StringBuffer result=new StringBuffer();
        result.append("==================== Corporate Data Demo ===================");
        result.append("\n responseCode : "+ corporateModel.getResCode());
        result.append("\n responseDesc : "+ corporateModel.getResDesc());
        result.append("\n searchResult : "+ corporateModel.getSearchResult());

        result.append("\n\n ===== personal Detail Section =====");
        result.append("\n title : "+ corporateModel.getTitle());
        result.append("\n custNbr : "+ corporateModel.getCustNbr());
        result.append("\n citizenId : "+ corporateModel.getCitizenId());
        result.append("\n thaiName1 : "+ corporateModel.getThaiName1());
        result.append("\n cId : "+ corporateModel.getcId());
        result.append("\n estDate : "+ corporateModel.getEstDate());
        result.append("\n\n ===== personal List Section =====");

        if(corporateModel.getPersonalList()!=null ){
            result.append("\n personalListSize : "+ corporateModel.getPersonalList().size());
            for(int i=0;i< corporateModel.getPersonalList().size();i++){
                result.append("\n custNbr1 : "+ corporateModel.getPersonalList().get(i).getCustNbr1());
                result.append("\n cId1 : "+ corporateModel.getPersonalList().get(i).getcId1());
                result.append("\n citizenId1 : "+ corporateModel.getPersonalList().get(i).getCitizenId1());
                result.append("\n title1 : "+ corporateModel.getPersonalList().get(i).getTitle1());
            }
        }else{
            result.append("\n personalListSize : null");
        }

        printDetail=result.toString();
    }

    public void customerAccount() throws Exception {

        CustomerAccountModel customerAccountModel =new CustomerAccountModel();
        //callservice
        customerAccountModel = caService.intiCustomerAction(searchIndividual);
        //showData
        StringBuffer result=new StringBuffer();
        result.append("==================== CustomerAccountList Data Demo ===================");
        result.append("\n reqId : "+ customerAccountModel.getReqId());
        result.append("\n resCode : "+ customerAccountModel.getResCode());
        result.append("\n resDesc : "+ customerAccountModel.getResDesc());

        if(customerAccountModel.getAccountBody()!=null&&customerAccountModel.getAccountBody().size()>0 ){
            result.append("\n cusAccountListSize : "+ customerAccountModel.getAccountBody().size());
            for(int i=0;i< customerAccountModel.getAccountBody().size();i++){
                result.append("\n rel : "+ customerAccountModel.getAccountBody().get(i).getRel());
                result.append("\n cd : "+ customerAccountModel.getAccountBody().get(i).getCd());
                result.append("\n pSO : "+ customerAccountModel.getAccountBody().get(i).getpSO());
                result.append("\n appl : "+ customerAccountModel.getAccountBody().get(i).getAppl());
                result.append("\n accountNo : "+ customerAccountModel.getAccountBody().get(i).getAccountNo());
                result.append("\n trlr : "+ customerAccountModel.getAccountBody().get(i).getTrlr());
                result.append("\n balance : "+ customerAccountModel.getAccountBody().get(i).getBalance());
                result.append("\n dir : "+ customerAccountModel.getAccountBody().get(i).getDir());
                result.append("\n prod : "+ customerAccountModel.getAccountBody().get(i).getProd());
                result.append("\n ctl1 : "+ customerAccountModel.getAccountBody().get(i).getCtl1());
                result.append("\n ctl2 : "+ customerAccountModel.getAccountBody().get(i).getCtl2());
                result.append("\n ctl3 : "+ customerAccountModel.getAccountBody().get(i).getCtl3());
                result.append("\n ctl4 : "+ customerAccountModel.getAccountBody().get(i).getCtl4());
                result.append("\n status : "+ customerAccountModel.getAccountBody().get(i).getStatus());
                result.append("\n date : "+ customerAccountModel.getAccountBody().get(i).getDate());
                result.append("\n =========================================================== ");
            }
        }else{
            result.append("\n accountListSize : "+customerAccountModel.getAccountBody().size());
        }

        printDetail=result.toString();
    }


    //////////////////////////////////////////////////////////////////////


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


    // print Request Response

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
    public String getPrintDetail() {
        return printDetail;
    }

    public void setPrintDetail(String printDetail) {
        this.printDetail = printDetail;
    }
}
