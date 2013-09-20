package com.clevel.selos.controller;

import com.clevel.selos.dao.testdao.CardTypeDao;
import com.clevel.selos.integration.RM;
import com.clevel.selos.integration.RMInterface;
import com.clevel.selos.model.RMmodel.CustomerAccountModel;
import com.clevel.selos.model.db.testrm.CardType;
import com.clevel.selos.model.RMmodel.CardTypeView;
import com.clevel.selos.model.RMmodel.corporateInfo.CorporateModel;
import com.clevel.selos.model.RMmodel.SearchIndividual;
import com.clevel.selos.model.view.CustomerInfoView;
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
    RMInterface rmInterfaceImpl;

    SearchIndividual searchIndividual;
    CorporateModel corporateModel;

    List<CardType> list;
    List<CardTypeView>listhardcode;

    private String printDetail;

    public TestService(){

    }

    @PostConstruct
    public void onCreate(){
        log.info("TEST RM SERVICE START  ");

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

        CustomerInfoView customerInfoView = rmInterfaceImpl.getIndividualInfo(searchIndividual.getReqId(),searchIndividual.getType(),
                         searchIndividual.getCustId(), RMInterface.DocumentType.CITIZEN_ID);



        printDetail=customerInfoView.toString();
        printDetail+=customerInfoView.getWorkAddress().toString();
        printDetail+=customerInfoView.getCurrentAddress().toString();
        printDetail+=customerInfoView.getRegisterAddress().toString();

    }


    public void corporate() throws Exception {

        CustomerInfoView customerInfoView = rmInterfaceImpl.getCorporateInfo(searchIndividual.getReqId(), searchIndividual.getType(),
                searchIndividual.getCustId(), RMInterface.DocumentType.CITIZEN_ID);


        printDetail=customerInfoView.toString();
    }

    public void customerAccount() throws Exception {

        CustomerAccountModel customerAccountModel =new CustomerAccountModel();
        //callservice
        customerAccountModel = rmInterfaceImpl.getCustomerAccountInfo(searchIndividual.getReqId(), searchIndividual.getType(),
                searchIndividual.getCustNbr(), RMInterface.DocumentType.CITIZEN_ID);
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
                result.append("\n name : "+ customerAccountModel.getAccountBody().get(i).getName());
                result.append("\n citizenId : "+ customerAccountModel.getAccountBody().get(i).getCitizenId());
                result.append("\n curr : "+ customerAccountModel.getAccountBody().get(i).getCurr());
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


    public String getPrintDetail() {
        return printDetail;
    }

    public void setPrintDetail(String printDetail) {
        this.printDetail = printDetail;
    }
}
