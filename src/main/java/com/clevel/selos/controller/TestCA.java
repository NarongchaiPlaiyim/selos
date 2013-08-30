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
        CustomerAccountModel customerAccountModel ;
        //callservice
        customerAccountModel = caService.intiCustomerAction(searchIndividual);
        //showData
        StringBuffer result=new StringBuffer();
//        result.append("==================== Individual Data Demo ===================");
//        result.append("\n responseCode : "+ customerAccountModel.getResCode());
//        result.append("\n responseDesc : "+ individualModel.getResDesc());
//        result.append("\n searchResult : "+ individualModel.getSearchResult());
//        result.append("\n lastPageFlag : "+ individualModel.getLastPageFlag());
//        result.append("\n\n ===== personal Detail Section =====");
//        result.append("\n title : "+ individualModel.getTitle());
//        result.append("\n custId : "+ individualModel.getCustId());
//        result.append("\n telephone1 : "+ individualModel.getTelephone1());
//        result.append("\n\n ===== personal List Section =====");

//        if(customerAccountModel.getPersonalLists()!=null ){
//            result.append("/n personalListSize : "+ individualModel.getPersonalLists().size());
//            for(int i=0;i< individualModel.getPersonalLists().size();i++){
//                result.append("/n address : "+ individualModel.getPersonalLists().get(i).getAddress());
//            }
//        }else{
//            result.append("/n personalListSize : null");
//        }

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
