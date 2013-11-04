package com.clevel.selos.integration.rlos.appin;

import com.clevel.selos.dao.ext.rlos.CustomerDetail1DAO;
import com.clevel.selos.dao.ext.rlos.CustomerDetail2DAO;
import com.clevel.selos.dao.system.SystemParameterDAO;
import com.clevel.selos.integration.RLOS;
import com.clevel.selos.integration.rlos.appin.model.AppInProcess;
import com.clevel.selos.integration.rlos.appin.model.AppInProcessResult;
import com.clevel.selos.integration.rlos.appin.model.CreditDetail;
import com.clevel.selos.integration.rlos.appin.model.CustomerDetail;
import com.clevel.selos.model.ActionResult;
import com.clevel.selos.model.db.ext.rlos.AppInProcess1;
import com.clevel.selos.model.db.ext.rlos.AppInProcess2;
import com.clevel.selos.model.db.ext.rlos.CustomerDetail1;
import com.clevel.selos.model.db.ext.rlos.CustomerDetail2;
import com.clevel.selos.model.db.system.SystemParameter;
import com.clevel.selos.system.Config;
import com.clevel.selos.system.message.ExceptionMapping;
import com.clevel.selos.system.message.ExceptionMessage;
import com.clevel.selos.system.message.Message;
import com.clevel.selos.util.Util;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AppInProcessService implements Serializable {
    @Inject
    @RLOS
    Logger log;

    @Inject
    @Config(name = "interface.rlos.appin.table.sysparam")
    String sysParam;

    @Inject
    SystemParameterDAO systemParameterDAO;
    @Inject
    CustomerDetail1DAO customerDetail1DAO;
    @Inject
    CustomerDetail2DAO customerDetail2DAO;

    @Inject
    @ExceptionMessage
    Message exceptionMsg;

    @Inject
    public AppInProcessService() {

    }

    public AppInProcessResult getAppInProcessData(List<String> citizenIdList) {
        log.debug("getAppInProcessData (citizenIdList : {})", citizenIdList);
        AppInProcessResult appInProcessResult = new AppInProcessResult();

        //check which table is current
        SystemParameter systemParameter = systemParameterDAO.findByParameterName(sysParam);

        if (systemParameter != null) {
            String value = systemParameter.getValue();

            if (value.equalsIgnoreCase("1")) {
                //get from table 1
                List<CustomerDetail1> customerDetail1List = customerDetail1DAO.getListCustomerByCitizenId(citizenIdList);
                List<AppInProcess> appInProcessList = getAppInProcessList(customerDetail1List, null);
                if (appInProcessList != null && appInProcessList.size() > 0) {
                    appInProcessResult.setActionResult(ActionResult.SUCCESS);
                    appInProcessResult.setAppInProcessList(appInProcessList);
                } else {
                    appInProcessResult.setActionResult(ActionResult.FAILED);
                    appInProcessResult.setReason(exceptionMsg.get(ExceptionMapping.RLOS_DATA_NOT_FOUND));
                    appInProcessResult.setAppInProcessList(new ArrayList<AppInProcess>());
                }
            } else if (value.equalsIgnoreCase("2")) {
                //get from table 2
                List<CustomerDetail2> customerDetail2List = customerDetail2DAO.getListCustomerByCitizenId(citizenIdList);
                List<AppInProcess> appInProcessList = getAppInProcessList(null, customerDetail2List);
                if (appInProcessList != null && appInProcessList.size() > 0) {
                    appInProcessResult.setActionResult(ActionResult.SUCCESS);
                    appInProcessResult.setAppInProcessList(appInProcessList);
                } else {
                    appInProcessResult.setActionResult(ActionResult.FAILED);
                    appInProcessResult.setReason(exceptionMsg.get(ExceptionMapping.RLOS_DATA_NOT_FOUND));
                    appInProcessResult.setAppInProcessList(new ArrayList<AppInProcess>());
                }
            } else {
                appInProcessResult.setActionResult(ActionResult.FAILED);
                appInProcessResult.setReason(exceptionMsg.get(ExceptionMapping.INVALID_SYSTEM_PARAM));
                appInProcessResult.setAppInProcessList(new ArrayList<AppInProcess>());
            }
        } else {
            appInProcessResult.setActionResult(ActionResult.FAILED);
            appInProcessResult.setReason(exceptionMsg.get(ExceptionMapping.NOT_FOUND_SYSTEM_PARAM));
            appInProcessResult.setAppInProcessList(new ArrayList<AppInProcess>());
        }

        return appInProcessResult;
    }

    private List<AppInProcess> getAppInProcessList(List<CustomerDetail1> customerDetail1List, List<CustomerDetail2> customerDetail2List) {
        List<AppInProcess> appInProcessList = new ArrayList<AppInProcess>();

        if (customerDetail1List != null && customerDetail1List.size() > 0) {
            Map<String, AppInProcess> appInProcessHashMap = new HashMap<String, AppInProcess>(); //(appRefNumber, appInProcess)
            for (CustomerDetail1 customerDetail1 : customerDetail1List) {
                if (customerDetail1.getAppInProcess1() != null && !Util.isEmpty(customerDetail1.getAppInProcess1().getAppRefNumber())) {
                    if (appInProcessHashMap.containsKey(customerDetail1.getAppInProcess1().getAppRefNumber())) {
                        //get app in process for add new cus detail
                        AppInProcess appInProcess = appInProcessHashMap.get(customerDetail1.getAppInProcess1().getAppRefNumber());
                        //get customer detail
                        List<CustomerDetail> customerDetailList = appInProcess.getCustomerDetailList();
                        customerDetailList.add(transformCustomerDetail(customerDetail1, null));
                        //add new cus detail to list
                        appInProcess.setCustomerDetailList(customerDetailList);
                        //put app in process back to map
                        appInProcessHashMap.put(customerDetail1.getAppInProcess1().getAppRefNumber(), appInProcess);
                    } else {
                        //add new app in process into map
                        AppInProcess appInProcess = transformAppInProcess(customerDetail1, null);
                        //put app in process into map
                        appInProcessHashMap.put(customerDetail1.getAppInProcess1().getAppRefNumber(), appInProcess);
                    }
                }
            }

            if (appInProcessHashMap != null && appInProcessHashMap.size() > 0) {
                //set app in process into return list
                for (String key : appInProcessHashMap.keySet()) {
                    AppInProcess appInProcess = appInProcessHashMap.get(key);
                    appInProcessList.add(appInProcess);
                }
            }
        } else if (customerDetail2List != null && customerDetail2List.size() > 0) {
            Map<String, AppInProcess> appInProcessHashMap = new HashMap<String, AppInProcess>(); //(appRefNumber, appInProcess)
            for (CustomerDetail2 customerDetail2 : customerDetail2List) {
                if (customerDetail2.getAppInProcess2() != null && !Util.isEmpty(customerDetail2.getAppInProcess2().getAppRefNumber())) {
                    if (appInProcessHashMap.containsKey(customerDetail2.getAppInProcess2().getAppRefNumber())) {
                        //get app in process for add new cus detail
                        AppInProcess appInProcess = appInProcessHashMap.get(customerDetail2.getAppInProcess2().getAppRefNumber());
                        //get customer detail
                        List<CustomerDetail> customerDetailList = appInProcess.getCustomerDetailList();
                        customerDetailList.add(transformCustomerDetail(null, customerDetail2));
                        //add new cus detail to list
                        appInProcess.setCustomerDetailList(customerDetailList);
                        //put app in process back to map
                        appInProcessHashMap.put(customerDetail2.getAppInProcess2().getAppRefNumber(), appInProcess);
                    } else {
                        //add new app in process into map
                        AppInProcess appInProcess = transformAppInProcess(null, customerDetail2);
                        //put app in process into map
                        appInProcessHashMap.put(customerDetail2.getAppInProcess2().getAppRefNumber(), appInProcess);
                    }
                }
            }

            if (appInProcessHashMap != null && appInProcessHashMap.size() > 0) {
                //set app in process into return list
                for (String key : appInProcessHashMap.keySet()) {
                    AppInProcess appInProcess = appInProcessHashMap.get(key);
                    appInProcessList.add(appInProcess);
                }
            }
        }

        return appInProcessList;
    }

    private CustomerDetail transformCustomerDetail(CustomerDetail1 customerDetail1, CustomerDetail2 customerDetail2) {
        CustomerDetail customerDetail = new CustomerDetail();
        if (customerDetail1 != null) {
            customerDetail.setCitizenId(customerDetail1.getCitizenId());
            customerDetail.setTitle(customerDetail1.getTitle());
            customerDetail.setFirstNameTh(customerDetail1.getFirstNameTh());
            customerDetail.setLastNameTh(customerDetail1.getLastNameTh());
            customerDetail.setBorrowerOrder(customerDetail1.getBorrowerOrder());
            customerDetail.setSpouseCitizenId(customerDetail1.getSpouseCitizenId());
            customerDetail.setSpouseFirstNameTh(customerDetail1.getSpouseFirstNameTh());
            customerDetail.setSpouseLastNameTh(customerDetail1.getSpouseLastNameTh());
            customerDetail.setSpouseCoApplicant(customerDetail1.getSpouseCoApplicant());
        } else if (customerDetail2 != null) {
            customerDetail.setCitizenId(customerDetail2.getCitizenId());
            customerDetail.setTitle(customerDetail2.getTitle());
            customerDetail.setFirstNameTh(customerDetail2.getFirstNameTh());
            customerDetail.setLastNameTh(customerDetail2.getLastNameTh());
            customerDetail.setBorrowerOrder(customerDetail2.getBorrowerOrder());
            customerDetail.setSpouseCitizenId(customerDetail2.getSpouseCitizenId());
            customerDetail.setSpouseFirstNameTh(customerDetail2.getSpouseFirstNameTh());
            customerDetail.setSpouseLastNameTh(customerDetail2.getSpouseLastNameTh());
            customerDetail.setSpouseCoApplicant(customerDetail2.getSpouseCoApplicant());
        }
        return customerDetail;
    }

    private AppInProcess transformAppInProcess(CustomerDetail1 customerDetail1, CustomerDetail2 customerDetail2) {
        AppInProcess appInProcess = new AppInProcess();
        List<CustomerDetail> customerDetailList = new ArrayList<CustomerDetail>();

        if (customerDetail1 != null) {
            List<CreditDetail> creditDetails = new ArrayList<CreditDetail>();
            CustomerDetail customerDetail = transformCustomerDetail(customerDetail1, null);
            customerDetailList.add(customerDetail);
            AppInProcess1 appInProcessEntity = customerDetail1.getAppInProcess1();
            appInProcess.setAppNumber(appInProcessEntity.getAppRefNumber());
            if (!Util.isEmpty(appInProcessEntity.getProductCode()) || !Util.isEmpty(appInProcessEntity.getProjectCode())) {
                CreditDetail creditDetail = new CreditDetail();
                creditDetail.setProductCode(appInProcessEntity.getProductCode());
                creditDetail.setProjectCode(appInProcessEntity.getProjectCode());
                creditDetail.setInterestRate(appInProcessEntity.getInterestRate());
                creditDetail.setRequestTenor(appInProcessEntity.getRequestTenor());
                creditDetail.setRequestLimit(appInProcessEntity.getRequestLimit());
                creditDetail.setFinalTenors(appInProcessEntity.getFinalTenors());
                creditDetail.setFinalLimit(appInProcessEntity.getFinalLimit());
                creditDetail.setFinalInstallment(appInProcessEntity.getFinalInstallment());
                creditDetails.add(creditDetail);
            }
            if (!Util.isEmpty(appInProcessEntity.getProductCode2()) || !Util.isEmpty(appInProcessEntity.getProjectCode2())) {
                CreditDetail creditDetail = new CreditDetail();
                creditDetail.setProductCode(appInProcessEntity.getProductCode2());
                creditDetail.setProjectCode(appInProcessEntity.getProjectCode2());
                creditDetail.setInterestRate(appInProcessEntity.getInterestRate2());
                creditDetail.setRequestTenor(appInProcessEntity.getRequestTenor2());
                creditDetail.setRequestLimit(appInProcessEntity.getRequestLimit2());
                creditDetail.setFinalTenors(appInProcessEntity.getFinalTenors2());
                creditDetail.setFinalLimit(appInProcessEntity.getFinalLimit2());
                creditDetail.setFinalInstallment(appInProcessEntity.getFinalInstallment2());
                creditDetails.add(creditDetail);
            }
            appInProcess.setCreditDetailList(creditDetails);
            appInProcess.setStatus(appInProcessEntity.getStatus());
            appInProcess.setCustomerDetailList(customerDetailList);
        } else if (customerDetail2 != null) {
            List<CreditDetail> creditDetails = new ArrayList<CreditDetail>();
            CustomerDetail customerDetail = transformCustomerDetail(null, customerDetail2);
            customerDetailList.add(customerDetail);
            AppInProcess2 appInProcessEntity = customerDetail2.getAppInProcess2();
            appInProcess.setAppNumber(appInProcessEntity.getAppRefNumber());
            if (!Util.isEmpty(appInProcessEntity.getProductCode()) || !Util.isEmpty(appInProcessEntity.getProjectCode())) {
                CreditDetail creditDetail = new CreditDetail();
                creditDetail.setProductCode(appInProcessEntity.getProductCode());
                creditDetail.setProjectCode(appInProcessEntity.getProjectCode());
                creditDetail.setInterestRate(appInProcessEntity.getInterestRate());
                creditDetail.setRequestTenor(appInProcessEntity.getRequestTenor());
                creditDetail.setRequestLimit(appInProcessEntity.getRequestLimit());
                creditDetail.setFinalTenors(appInProcessEntity.getFinalTenors());
                creditDetail.setFinalLimit(appInProcessEntity.getFinalLimit());
                creditDetail.setFinalInstallment(appInProcessEntity.getFinalInstallment());
                creditDetails.add(creditDetail);
            }
            if (!Util.isEmpty(appInProcessEntity.getProductCode2()) || !Util.isEmpty(appInProcessEntity.getProjectCode2())) {
                CreditDetail creditDetail = new CreditDetail();
                creditDetail.setProductCode(appInProcessEntity.getProductCode2());
                creditDetail.setProjectCode(appInProcessEntity.getProjectCode2());
                creditDetail.setInterestRate(appInProcessEntity.getInterestRate2());
                creditDetail.setRequestTenor(appInProcessEntity.getRequestTenor2());
                creditDetail.setRequestLimit(appInProcessEntity.getRequestLimit2());
                creditDetail.setFinalTenors(appInProcessEntity.getFinalTenors2());
                creditDetail.setFinalLimit(appInProcessEntity.getFinalLimit2());
                creditDetail.setFinalInstallment(appInProcessEntity.getFinalInstallment2());
                creditDetails.add(creditDetail);
            }
            appInProcess.setCreditDetailList(creditDetails);
            appInProcess.setStatus(appInProcessEntity.getStatus());
            appInProcess.setCustomerDetailList(customerDetailList);
        }

        return appInProcess;
    }
}
