package com.clevel.selos.controller;

import com.clevel.selos.integration.corebanking.CaService;
import com.clevel.selos.integration.corebanking.RmService;
import com.clevel.selos.model.CAmodel.CustomerAccountModel;
import com.clevel.selos.model.RMmodel.IndividualModel;
import com.clevel.selos.model.RMmodel.SearchIndividual;

import javax.annotation.PostConstruct;
import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: sahawat
 * Date: 30/8/2556
 * Time: 11:50 น.
 * To change this template use File | Settings | File Templates.
 */
public class TestCA  implements Serializable{

    CaService caService;

    SearchIndividual searchIndividual;

    public static String printResponse;
    public static String printRequest;
    private String printDetail;


    public TestCA(){

    }
    @PostConstruct
    public void onCreate(){
        searchIndividual=new SearchIndividual();
    }

    public void customerAccount() throws Exception {
        caService =new CaService();
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
            }
        }else{
            result.append("\n accountListSize : null");
        }

        printDetail=result.toString();
    }



    public static String getPrintResponse() {
        return printResponse;
    }

    public static void setPrintResponse(String printResponse) {
        TestCA.printResponse = printResponse;
    }

    public static String getPrintRequest() {
        return printRequest;
    }

    public static void setPrintRequest(String printRequest) {
        TestCA.printRequest = printRequest;
    }

    public String getPrintDetail() {
        return printDetail;
    }

    public void setPrintDetail(String printDetail) {
        this.printDetail = printDetail;
    }


}
