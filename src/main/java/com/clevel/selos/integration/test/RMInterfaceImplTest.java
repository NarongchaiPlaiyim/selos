package com.clevel.selos.integration.test;

import com.clevel.selos.integration.RM;
import com.clevel.selos.integration.RMInterface;
import com.clevel.selos.integration.corebanking.model.corporateInfo.CorporateModel;
import com.clevel.selos.integration.corebanking.model.corporateInfo.CorporateResult;
import com.clevel.selos.integration.corebanking.model.customeraccount.CustomerAccountListModel;
import com.clevel.selos.integration.corebanking.model.customeraccount.CustomerAccountResult;
import com.clevel.selos.integration.corebanking.model.individualInfo.IndividualResult;
import com.clevel.selos.model.ActionResult;
import com.clevel.selos.model.view.CustomerInfoView;
import org.slf4j.Logger;

import javax.enterprise.inject.Alternative;
import javax.inject.Inject;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Alternative
public class RMInterfaceImplTest implements RMInterface ,Serializable {
    @Inject
    @RM
    Logger log;

    @Inject
    public RMInterfaceImplTest() {
    }


    @Override
    public IndividualResult getIndividualInfo(String userId,String customerId, DocumentType documentType,SearchBy searchBy) throws Exception {

        log.debug("======= IndividualServiceTest =======");
        IndividualResult individualResult=new IndividualResult();
        individualResult.setCustomerId(customerId);
        individualResult.setActionResult(ActionResult.SUCCEED);
//        IndividualModel individualModel = new IndividualModel();
//        individualModel.setResCode("0000");
//        individualModel.setResDesc("SUCCESS");
//        individualModel.setSearchResult("CD");
//        individualModel.setLastPageFlag("");
//            //personal detail session
//            individualModel.setTitle("นาย");
//            individualModel.setName("พสุ กุญ");
//            individualModel.setCustId("CI");
//            individualModel.setTelephone1("000023780");

////          //personal list session
//            if (resSearchIndividualCustomer.getBody().getPersonalListSection() != null && (resSearchIndividualCustomer.getBody().getPersonalListSection().getPersonalList() != null && resSearchIndividualCustomer.getBody().getPersonalListSection().getPersonalList().size() > 0)) {
//
//                int personalListSize = resSearchIndividualCustomer.getBody().getPersonalListSection().getPersonalList().size();
////
//                IndividualPersonalList resultPersonalList = null;
//                ArrayList<IndividualPersonalList> list = new ArrayList<IndividualPersonalList>();
//                if (personalListSize != 0) {
//                    for (int i = 0; i < personalListSize; i++) {
//                        resultPersonalList = new IndividualPersonalList();
//                        resultPersonalList.setAddress(resSearchIndividualCustomer.getBody().getPersonalListSection().getPersonalList().get(i).getAddress1());
//
//                        list.add(resultPersonalList);
//                    }
//                    individualModel.setPersonalLists(list);
//                }
//            }
//            log.debug("responseCode: {}", individualModel.getResCode());
//      }
        return individualResult;
    }

    @Override
    public CorporateResult getCorporateInfo(String userId,String customerId, DocumentType documentType,SearchBy searchBy) throws Exception {
        log.debug("======= CorporateServiceTest =======");

        CorporateResult corporateResult = new CorporateResult();
        corporateResult.setActionResult(ActionResult.SUCCEED);
        corporateResult.setCustomerId(customerId);
//        corporateModel.setResCode("0000");
//        corporateModel.setResDesc("SUCCESS");
//        corporateModel.setSearchResult("CD");
//
//            //personal detail session
//            corporateModel.setTitle("");
//            corporateModel.setCustNbr("00000001180946");
//            corporateModel.setThaiName1("นาย พสุ กุญ");
//            corporateModel.setcId("TN");
//            corporateModel.setCitizenId("1013305588");
//            corporateModel.setEstDate("00/00/0000");

//          //personal list session
//            if (resSearchCorporateCustomer.getBody().getCorporateCustomerListSection() != null && (resSearchCorporateCustomer.getBody().getCorporateCustomerListSection().getCorporateList() != null &&
//                    resSearchCorporateCustomer.getBody().getCorporateCustomerListSection().getCorporateList().size() > 0)) {
//                int corporateListSize = resSearchCorporateCustomer.getBody().getCorporateCustomerListSection().getCorporateList().size();
////
//                CorporatePersonalList corporatePersonalList = null;
//                ArrayList<CorporatePersonalList> list = new ArrayList<CorporatePersonalList>();
//                if (corporateListSize != 0) {
//                    for (int i = 0; i < corporateListSize; i++) {
//                        corporatePersonalList = new CorporatePersonalList();
//                        corporatePersonalList.setCustNbr1(resSearchCorporateCustomer.getBody().getCorporateCustomerListSection().getCorporateList().get(i).getCustNbr1());
//                        corporatePersonalList.setcId1(resSearchCorporateCustomer.getBody().getCorporateCustomerListSection().getCorporateList().get(i).getCId1());
//                        corporatePersonalList.setCitizenId1(resSearchCorporateCustomer.getBody().getCorporateCustomerListSection().getCorporateList().get(i).getCitizenCId1());
//                        corporatePersonalList.setTitle1(resSearchCorporateCustomer.getBody().getCorporateCustomerListSection().getCorporateList().get(i).getTitle1());
//
//                        list.add(corporatePersonalList);
//                    }
//                    corporateModel.setPersonalList(list);
//                }
//            }
//            log.debug("responseCode: {}", corporateModel.getResCode());

        return corporateResult;
    }

    @Override
    public CustomerAccountResult getCustomerAccountInfo(String userId,String customerId) throws Exception {

        log.debug("======= CustomerAccountServiceTest =======");

        CustomerAccountResult customerAccountResult = new CustomerAccountResult();
        customerAccountResult.setActionResult(ActionResult.SUCCEED);

        List<CustomerAccountListModel> listModelList=new ArrayList<CustomerAccountListModel>();

       CustomerAccountListModel customerAccountListModel;
                    customerAccountListModel = new CustomerAccountListModel();
                    customerAccountListModel.setRel("PRI");
                    customerAccountListModel.setCd("IND");
                    customerAccountListModel.setpSO("P");
                    customerAccountListModel.setAppl("AC");
                    customerAccountListModel.setAccountNo("00000000000022");
                    customerAccountListModel.setTrlr("");
                    customerAccountListModel.setBalance(new BigDecimal(0.00));
                    customerAccountListModel.setDir("D");
                    customerAccountListModel.setProd("");
                    customerAccountListModel.setCtl1("0011");
                    customerAccountListModel.setCtl2("0000");
                    customerAccountListModel.setCtl3("0000");
                    customerAccountListModel.setCtl4("0000");
                    customerAccountListModel.setStatus("");
                    customerAccountListModel.setDate("11/03/2546");
                    customerAccountListModel.setName("นาย ชรัหาญ");
                    customerAccountListModel.setCitizenId("3100101068917");
                    customerAccountListModel.setCurr("THB");
                    listModelList.add(customerAccountListModel);

                    customerAccountListModel = new CustomerAccountListModel();
                    customerAccountListModel.setRel("PRI");
                    customerAccountListModel.setCd("IND");
                    customerAccountListModel.setpSO("P");
                    customerAccountListModel.setAppl("AL");
                    customerAccountListModel.setAccountNo("00015914591004");
                    customerAccountListModel.setTrlr("");
                    customerAccountListModel.setBalance(new BigDecimal(0.00));
                    customerAccountListModel.setDir("D");
                    customerAccountListModel.setProd("");
                    customerAccountListModel.setCtl1("0011");
                    customerAccountListModel.setCtl2("0001");
                    customerAccountListModel.setCtl3("0001");
                    customerAccountListModel.setCtl4("0000");
                    customerAccountListModel.setStatus("");
                    customerAccountListModel.setDate("00/00/0000");
                    customerAccountListModel.setName("นาย ชรัหาญ");
                    customerAccountListModel.setCitizenId("3100101068917");
                    customerAccountListModel.setCurr("");
                    listModelList.add(customerAccountListModel);

            customerAccountResult.setAccountListModels(listModelList);

        return customerAccountResult;
    }
}
