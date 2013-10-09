package com.clevel.selos.controller;

import com.clevel.selos.dao.testdao.CardTypeDao;
import com.clevel.selos.exception.ApplicationRuntimeException;
import com.clevel.selos.integration.RM;
import com.clevel.selos.integration.RMInterface;
import com.clevel.selos.integration.corebanking.model.customeraccount.*;
import com.clevel.selos.integration.corebanking.model.individualInfo.IndividualResult;
import com.clevel.selos.model.ActionResult;
import com.clevel.selos.model.db.testrm.CardType;
import com.clevel.selos.integration.corebanking.model.CardTypeView;
import com.clevel.selos.integration.corebanking.model.corporateInfo.*;
import com.clevel.selos.integration.corebanking.model.SearchIndividual;
import com.clevel.selos.model.view.CustomerAccountView;
import com.clevel.selos.model.view.CustomerInfoResultView;
import com.clevel.selos.model.view.CustomerInfoView;
import com.clevel.selos.system.Config;
import com.clevel.selos.transform.business.CustomerBizTransform;
import com.clevel.selos.util.Util;
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

    @Inject
    @Config(name = "interface.rm.replace.blank")
    String blank;

    @Inject
    CustomerBizTransform customerBizTransform;

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

        IndividualResult individualResult = rmInterfaceImpl.getIndividualInfo("win",searchIndividual.getCustId(), RMInterface.DocumentType.CITIZEN_ID,RMInterface.SearchBy.CUSTOMER_ID);



//        printDetail=customerInfoView.toString();
//        printDetail+=customerInfoView.getWorkAddress().toString();
//        printDetail+=customerInfoView.getCurrentAddress().toString();

        CustomerInfoResultView customerInfoResultView =  customerBizTransform.tranformIndividual(individualResult);

        if(customerInfoResultView.getCustomerInfoView()!=null){
            CustomerInfoView customerInfoView = customerInfoResultView.getCustomerInfoView();
            printDetail=customerInfoView.getWorkAddress().getProvince().getName();
            printDetail+=" "+customerInfoView.getWorkAddress().getProvince().getCode();
            printDetail+=" "+customerInfoView.getWorkAddress().getProvince().getActive();
            printDetail+=" "+customerInfoView.getWorkAddress().getProvince().getRegion();

            printDetail+="\n"+customerInfoView.getRegisterAddress().getSubDistrict().toString();
            printDetail+=" "+customerInfoView.getRegisterAddress().getDistrict().toString();
            printDetail+="\n\n\n\n\n"+customerInfoView.getWorkAddress().getAddressType().toString();
            printDetail+="\n"+customerInfoView.getCurrentAddress().getAddressType().toString();
            printDetail+="\n"+customerInfoView.getRegisterAddress().getAddressType().toString();
//
//            printDetail+=customerInfoView.toString();
        }

    }


    public void corporate() {
           try{
        CorporateResult corporateResult = rmInterfaceImpl.getCorporateInfo("win",searchIndividual.getCustId(), RMInterface.DocumentType.CORPORATE_ID,RMInterface.SearchBy.CUSTOMER_ID);

        CustomerInfoResultView customerInfoResultView = customerBizTransform.tranformJuristic(corporateResult);
        if(customerInfoResultView.getCustomerInfoView()!=null){
            CustomerInfoView customerInfoView = customerInfoResultView.getCustomerInfoView();
            printDetail=customerInfoView.toString();
            printDetail+="\n\n\n"+customerInfoView.toString();
            printDetail+="\n\n\n"+customerInfoView.getCurrentAddress().toString();
            printDetail+="\n\n\n"+customerInfoView.getRegisterAddress().toString();
        }
           }catch (ApplicationRuntimeException e){

           }
    }

    public void customerAccount() throws Exception {

        CustomerAccountResult customerAccountResult =new CustomerAccountResult();
        //callservice
        System.out.println("sssssssssssssss "+searchIndividual.getCustNbr());
        customerAccountResult = rmInterfaceImpl.getCustomerAccountInfo("win",searchIndividual.getCustNbr());


       CustomerAccountView customerAccountView= customerBizTransform.transformCustomerAccount(customerAccountResult);
        //showData
        StringBuffer result=new StringBuffer();
        result.append("==================== CustomerAccountList Data Demo ===================");
        result.append("\n result : "+ customerAccountView.getActionResult().toString());
        if(customerAccountView.getActionResult() == ActionResult.FAILED){
            result.append("\n reason : "+ customerAccountView.getReason());
        }


        if(customerAccountView.getAccountList()!=null&& customerAccountView.getAccountList().size()>0 ){
            result.append("\n cusAccountListSize : "+ customerAccountView.getAccountList().size());
            for(int i=0;i< customerAccountView.getAccountList().size();i++){
                result.append("\n AccountNumber : "+ customerAccountView.getAccountList().get(i));

                result.append("\n =========================================================== T");

            }
        }else{
            result.append("\n accountListSize : 0");
        }

        printDetail=result.toString();
        System.out.println(printDetail);
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
