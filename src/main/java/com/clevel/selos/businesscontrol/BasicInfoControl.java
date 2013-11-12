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
    UserDAO userDAO;
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
        CustomerEntity customerEntity;
        List<Customer> customerList = customerDAO.findByWorkCaseId(workCaseId);
        for (Customer customer : customerList) {
            if (customer.getCustomerEntity() != null && customer.getCustomerEntity().getId() == 1) { // Customer Entity ; 1 = Individual ; 2 = Juristic
                customerEntity = customer.getCustomerEntity();
                return customerEntity;
            }
        }
        customerEntity = customerEntityDAO.findById(2);
        return customerEntity;
    }

    public void saveBasicInfo(BasicInfoView basicInfoView, long workCaseId, String userId) {
        WorkCase workCase = workCaseDAO.findById(workCaseId);
        User user = userDAO.findById(userId);

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

        if (basicInfoView.getBasicInfoAccountViews().size() != 0) {
            for (BasicInfoAccountView biav : basicInfoView.getBasicInfoAccountViews()) {
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
