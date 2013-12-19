package com.clevel.selos.businesscontrol;

import com.clevel.selos.dao.master.CustomerEntityDAO;
import com.clevel.selos.dao.master.UserDAO;
import com.clevel.selos.dao.working.*;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.master.BAPaymentMethod;
import com.clevel.selos.model.db.master.CustomerEntity;
import com.clevel.selos.model.db.master.User;
import com.clevel.selos.model.db.working.*;
import com.clevel.selos.model.view.BasicInfoAccountPurposeView;
import com.clevel.selos.model.view.BasicInfoAccountView;
import com.clevel.selos.model.view.BasicInfoView;
import com.clevel.selos.transform.BasicInfoAccPurposeTransform;
import com.clevel.selos.transform.BasicInfoAccountTransform;
import com.clevel.selos.transform.BasicInfoTransform;
import org.hibernate.Hibernate;
import org.slf4j.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.List;

@Stateless
public class BasicInfoControl extends BusinessControl {
    @Inject
    @SELOS
    Logger log;
    @Inject
    BasicInfoDAO basicInfoDAO;
    @Inject
    CustomerDAO customerDAO;
    @Inject
    WorkCaseDAO workCaseDAO;
    @Inject
    OpenAccountDAO openAccountDAO;
    @Inject
    OpenAccPurposeDAO openAccPurposeDAO;
    @Inject
    CustomerEntityDAO customerEntityDAO;

    @Inject
    BasicInfoTransform basicInfoTransform;
    @Inject
    BasicInfoAccountTransform basicInfoAccountTransform;
    @Inject
    BasicInfoAccPurposeTransform basicInfoAccPurposeTransform;

    @Inject
    public BasicInfoControl(){

    }

    public BasicInfoView getBasicInfo(long workCaseId) {
        log.info("getBasicInfo ::: workCaseId : {}", workCaseId);
        BasicInfo basicInfo = basicInfoDAO.findByWorkCaseId(workCaseId);
        WorkCase workCase = workCaseDAO.findById(workCaseId);

        if (basicInfo == null) {
            basicInfo = new BasicInfo();
        }

        BasicInfoView basicInfoView = basicInfoTransform.transformToView(basicInfo, workCase);

        log.info("getBasicInfo ::: basicInfoView : {}", basicInfoView);
        return basicInfoView;
    }

    public CustomerEntity getCustomerEntityByWorkCaseId(long workCaseId) {
        CustomerEntity customerEntity = new CustomerEntity();
        List<Customer> customerList = customerDAO.findByWorkCaseId(workCaseId);
        if(customerList != null && customerList.size() > 0 && customerList.get(0).getId() != 0){
            for (Customer customer : customerList) {
                if (customer.getCustomerEntity() != null && customer.getCustomerEntity().getId() == 1) { // Customer Entity ; 1 = Individual ; 2 = Juristic
                    customerEntity = customer.getCustomerEntity();
                    break;
                } else if (customer.getCustomerEntity() != null && customer.getCustomerEntity().getId() == 2) {
                    customerEntity = customer.getCustomerEntity();
                }
            }
        }
        //if null or not have customer , return Juristic
        //customerEntity = customerEntityDAO.findById(2);

        log.debug("customerEntity : {}",customerEntity);

        return customerEntity;
    }

    public void saveBasicInfo(BasicInfoView basicInfoView, long workCaseId) {
        User user = getCurrentUser();

        WorkCase workCase = workCaseDAO.findById(workCaseId);

        if (basicInfoView.getQualitative() == 0) {
            basicInfoView.setQualitative(2);
        }

        if (basicInfoView.getApplyBA() == 0) {
            basicInfoView.setBaPaymentMethod(new BAPaymentMethod());
        }

        BasicInfo basicInfo = basicInfoTransform.transformToModel(basicInfoView, workCase, user);
        basicInfoDAO.persist(basicInfo);

        List<OpenAccount> openAccountList = openAccountDAO.findByBasicInfoId(basicInfo.getId());
        for (OpenAccount oa : openAccountList) {
            List<OpenAccPurpose> openAccPurposeList = openAccPurposeDAO.findByOpenAccountId(oa.getId());
            openAccPurposeDAO.delete(openAccPurposeList);
        }
        openAccountDAO.delete(openAccountList);

        if (basicInfoView.getBasicInfoAccountViews() != null && basicInfoView.getBasicInfoAccountViews().size() > 0) {
            for (BasicInfoAccountView biav : basicInfoView.getBasicInfoAccountViews()) {
                System.out.println("BasicInfoAccountView [ ID ] : "+ biav.getId() +" [ Account Name ] : " +biav.getAccountName());
                OpenAccount openAccount = basicInfoAccountTransform.transformToModel(biav, basicInfo);
                openAccountDAO.save(openAccount);

                for (BasicInfoAccountPurposeView biapv : biav.getBasicInfoAccountPurposeView()) {
                    if (biapv.isSelected()) {
                        OpenAccPurpose openAccPurpose = basicInfoAccPurposeTransform.transformToModel(biapv, openAccount);
                        openAccPurposeDAO.save(openAccPurpose);
                    }
                }
            }
        }

    }
}
