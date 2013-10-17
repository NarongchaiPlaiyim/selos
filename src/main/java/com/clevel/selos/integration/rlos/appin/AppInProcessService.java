package com.clevel.selos.integration.rlos.appin;

import com.clevel.selos.dao.ext.rlos.CustomerDetail1DAO;
import com.clevel.selos.dao.ext.rlos.CustomerDetail2DAO;
import com.clevel.selos.dao.system.SystemParameterDAO;
import com.clevel.selos.integration.RLOS;
import com.clevel.selos.integration.rlos.appin.model.AppInProcess;
import com.clevel.selos.integration.rlos.appin.model.CustomerDetail;
import com.clevel.selos.model.db.ext.rlos.AppInProcess1;
import com.clevel.selos.model.db.ext.rlos.AppInProcess2;
import com.clevel.selos.model.db.ext.rlos.CustomerDetail1;
import com.clevel.selos.model.db.ext.rlos.CustomerDetail2;
import com.clevel.selos.model.db.system.SystemParameter;
import com.clevel.selos.system.Config;
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
    public AppInProcessService(){

    }

    public List<AppInProcess> getAppInProcessData(List<String> citizenIdList){
        log.debug("getAppInProcessData (citizenIdList : {})",citizenIdList);
        List<AppInProcess> obligationList = new ArrayList<AppInProcess>();

        //check which table is current
        SystemParameter systemParameter = systemParameterDAO.findByParameterName(sysParam);

        if(systemParameter!=null){
            String value = systemParameter.getValue();

            if(value.equalsIgnoreCase("1")){
                //get from table 1
                List<CustomerDetail1> customerDetail1List = customerDetail1DAO.getListCustomerByCitizenId(citizenIdList);
                return getAppInProcessList(customerDetail1List, null);
            } else if(value.equalsIgnoreCase("2")){
                //get from table 2
                List<CustomerDetail2> customerDetail2List = customerDetail2DAO.getListCustomerByCitizenId(citizenIdList);
                return getAppInProcessList(null, customerDetail2List);
            }
        }

        return obligationList;
    }

    private List<AppInProcess> getAppInProcessList(List<CustomerDetail1> customerDetail1List, List<CustomerDetail2> customerDetail2List){
        List<AppInProcess> appInProcessList = new ArrayList<AppInProcess>();

        if(customerDetail1List!=null && customerDetail1List.size()>0){
            Map<String,AppInProcess> appInProcessHashMap = new HashMap<String, AppInProcess>(); //(appRefNumber, appInProcess)
            for(CustomerDetail1 customerDetail1 : customerDetail1List){
                if(customerDetail1.getAppInProcess1()!=null && !Util.isEmpty(customerDetail1.getAppInProcess1().getAppRefNumber())){
                    if(appInProcessHashMap.containsKey(customerDetail1.getAppInProcess1().getAppRefNumber())){
                        //get app in process for add new cus detail
                        AppInProcess appInProcess = appInProcessHashMap.get(customerDetail1.getAppInProcess1().getAppRefNumber());
                        //get customer detail
                        List<CustomerDetail> customerDetailList = appInProcess.getCustomerDetailList();
                        customerDetailList.add(transformCustomerDetail(customerDetail1,null));
                        //add new cus detail to list
                        appInProcess.setCustomerDetailList(customerDetailList);
                        //put app in process back to map
                        appInProcessHashMap.put(customerDetail1.getAppInProcess1().getAppRefNumber(),appInProcess);
                    } else {
                        //add new app in process into map
                        AppInProcess appInProcess = transformAppInProcess(customerDetail1,null);
                        //put app in process into map
                        appInProcessHashMap.put(customerDetail1.getAppInProcess1().getAppRefNumber(),appInProcess);
                    }
                }
            }

            if(appInProcessHashMap!=null && appInProcessHashMap.size()>0){
                //set app in process into return list
                for (String key: appInProcessHashMap.keySet()) {
                    AppInProcess appInProcess = appInProcessHashMap.get(key);
                    appInProcessList.add(appInProcess);
                }
            }
        } else if(customerDetail2List!=null && customerDetail2List.size()>0){
            Map<String,AppInProcess> appInProcessHashMap = new HashMap<String, AppInProcess>(); //(appRefNumber, appInProcess)
            for(CustomerDetail2 customerDetail2 : customerDetail2List){
                if(customerDetail2.getAppInProcess2()!=null && !Util.isEmpty(customerDetail2.getAppInProcess2().getAppRefNumber())){
                    if(appInProcessHashMap.containsKey(customerDetail2.getAppInProcess2().getAppRefNumber())){
                        //get app in process for add new cus detail
                        AppInProcess appInProcess = appInProcessHashMap.get(customerDetail2.getAppInProcess2().getAppRefNumber());
                        //get customer detail
                        List<CustomerDetail> customerDetailList = appInProcess.getCustomerDetailList();
                        customerDetailList.add(transformCustomerDetail(null,customerDetail2));
                        //add new cus detail to list
                        appInProcess.setCustomerDetailList(customerDetailList);
                        //put app in process back to map
                        appInProcessHashMap.put(customerDetail2.getAppInProcess2().getAppRefNumber(),appInProcess);
                    } else {
                        //add new app in process into map
                        AppInProcess appInProcess = transformAppInProcess(null,customerDetail2);
                        //put app in process into map
                        appInProcessHashMap.put(customerDetail2.getAppInProcess2().getAppRefNumber(),appInProcess);
                    }
                }
            }

            if(appInProcessHashMap!=null && appInProcessHashMap.size()>0){
                //set app in process into return list
                for (String key: appInProcessHashMap.keySet()) {
                    AppInProcess appInProcess = appInProcessHashMap.get(key);
                    appInProcessList.add(appInProcess);
                }
            }
        }

        return appInProcessList;
    }

    private CustomerDetail transformCustomerDetail(CustomerDetail1 customerDetail1, CustomerDetail2 customerDetail2){
        CustomerDetail customerDetail = new CustomerDetail();
        if(customerDetail1!=null){
            customerDetail.setCitizenId(customerDetail1.getCitizenId());
            customerDetail.setTitle(customerDetail1.getTitle());
            customerDetail.setFirstNameTh(customerDetail1.getFirstNameTh());
            customerDetail.setLastNameTh(customerDetail1.getLastNameTh());
            customerDetail.setBorrowerOrder(customerDetail1.getBorrowerOrder());
            customerDetail.setSpouseCitizenId(customerDetail1.getSpouseCitizenId());
            customerDetail.setSpouseFirstNameTh(customerDetail1.getSpouseFirstNameTh());
            customerDetail.setSpouseLastNameTh(customerDetail1.getSpouseLastNameTh());
            customerDetail.setSpouseCoApplicant(customerDetail1.getSpouseCoApplicant());
        } else if(customerDetail2!=null){
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

    private AppInProcess transformAppInProcess(CustomerDetail1 customerDetail1, CustomerDetail2 customerDetail2){
        AppInProcess appInProcess = new AppInProcess();
        List<CustomerDetail> customerDetailList = new ArrayList<CustomerDetail>();

        if(customerDetail1!=null){
            CustomerDetail customerDetail = transformCustomerDetail(customerDetail1,null);
            customerDetailList.add(customerDetail);
            AppInProcess1 appInProcessEntity = customerDetail1.getAppInProcess1();
            appInProcess.setAppNumber(appInProcessEntity.getAppRefNumber());
            appInProcess.setProductCode(appInProcessEntity.getProductCode());
            appInProcess.setProjectCode(appInProcessEntity.getProjectCode());
            appInProcess.setInterestRate(appInProcessEntity.getInterestRate());
            appInProcess.setRequestTenor(appInProcessEntity.getRequestTenor());
            appInProcess.setRequestLimit(appInProcessEntity.getRequestLimit());
            appInProcess.setFinalTenors(appInProcessEntity.getFinalTenors());
            appInProcess.setFinalLimit(appInProcessEntity.getFinalLimit());
            appInProcess.setFinalInstallment(appInProcessEntity.getFinalInstallment());
            appInProcess.setStatus(appInProcessEntity.getStatus());
            appInProcess.setCustomerDetailList(customerDetailList);
        } else if(customerDetail2!=null){
            CustomerDetail customerDetail = transformCustomerDetail(null,customerDetail2);
            customerDetailList.add(customerDetail);
            AppInProcess2 appInProcessEntity = customerDetail2.getAppInProcess2();
            appInProcess.setAppNumber(appInProcessEntity.getAppRefNumber());
            appInProcess.setProductCode(appInProcessEntity.getProductCode());
            appInProcess.setProjectCode(appInProcessEntity.getProjectCode());
            appInProcess.setInterestRate(appInProcessEntity.getInterestRate());
            appInProcess.setRequestTenor(appInProcessEntity.getRequestTenor());
            appInProcess.setRequestLimit(appInProcessEntity.getRequestLimit());
            appInProcess.setFinalTenors(appInProcessEntity.getFinalTenors());
            appInProcess.setFinalLimit(appInProcessEntity.getFinalLimit());
            appInProcess.setFinalInstallment(appInProcessEntity.getFinalInstallment());
            appInProcess.setStatus(appInProcessEntity.getStatus());
            appInProcess.setCustomerDetailList(customerDetailList);
        }

        return appInProcess;
    }
}
