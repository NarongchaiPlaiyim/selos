package com.clevel.selos.businesscontrol;

import com.clevel.selos.dao.working.BasicInfoDAO;
import com.clevel.selos.dao.working.CustomerDAO;
import com.clevel.selos.dao.working.WorkCaseDAO;
import com.clevel.selos.model.db.master.CustomerEntity;
import com.clevel.selos.model.db.working.BasicInfo;
import com.clevel.selos.model.db.working.Customer;
import com.clevel.selos.model.db.working.WorkCase;
import com.clevel.selos.model.view.BasicInfoView;
import com.clevel.selos.transform.BasicInfoTransform;
import org.slf4j.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.List;

@Stateless
public class BasicInfoControl extends BusinessControl {
    @Inject
    Logger log;

    @Inject
    BasicInfoDAO basicInfoDAO;
    @Inject
    CustomerDAO customerDAO;
    @Inject
    WorkCaseDAO workCaseDAO;

    @Inject
    BasicInfoTransform basicInfoTransform;

    public BasicInfoView getBasicInfo(long workCaseId){
        log.info("getBasicInfo ::: workCaseId : {}", workCaseId);
        BasicInfoView basicInfoView = null;
        BasicInfo basicInfo = basicInfoDAO.findByWorkCaseId(workCaseId);
        WorkCase workCase = workCaseDAO.findById(workCaseId);

        if(basicInfo == null){
            basicInfo = new BasicInfo();
        }

        basicInfoView = basicInfoTransform.transformToView(basicInfo,workCase);

        log.info("getBasicInfo ::: basicInfoView : {}", basicInfoView);
        return basicInfoView;
    }

    public CustomerEntity getCustomerEntityByWorkCaseId(long workCaseId){
        CustomerEntity customerEntity = new CustomerEntity();
        List<Customer> customerList = customerDAO.findByWorkCaseId(workCaseId);
        for(Customer c : customerList){
            if(c.getCustomerEntity().getId() == 1){ // Customer Entity ; 1 = Individual ; 2 = Juristic
                customerEntity = c.getCustomerEntity();
                return customerEntity;
            }
        }
        return customerEntity;
    }
}
