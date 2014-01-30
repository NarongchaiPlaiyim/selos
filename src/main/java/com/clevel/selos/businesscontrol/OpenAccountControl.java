package com.clevel.selos.businesscontrol;

import com.clevel.selos.dao.master.CustomerEntityDAO;
import com.clevel.selos.dao.master.ProductGroupDAO;
import com.clevel.selos.dao.master.RequestTypeDAO;
import com.clevel.selos.dao.working.*;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.RadioValue;
import com.clevel.selos.model.RelationValue;
import com.clevel.selos.model.db.master.BAPaymentMethod;
import com.clevel.selos.model.db.master.CustomerEntity;
import com.clevel.selos.model.db.master.SBFScore;
import com.clevel.selos.model.db.master.User;
import com.clevel.selos.model.db.working.*;
import com.clevel.selos.model.view.BasicInfoAccountPurposeView;
import com.clevel.selos.model.view.BasicInfoAccountView;
import com.clevel.selos.model.view.BasicInfoView;
import com.clevel.selos.model.view.CustomerInfoView;
import com.clevel.selos.transform.BasicInfoAccPurposeTransform;
import com.clevel.selos.transform.BasicInfoAccountTransform;
import com.clevel.selos.transform.BasicInfoTransform;
import com.clevel.selos.transform.CustomerTransform;
import com.clevel.selos.util.Util;
import org.slf4j.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Stateless
public class OpenAccountControl extends BusinessControl {
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
    CustomerTransform customerTransform;

    @Inject
    public OpenAccountControl(){

    }

    public List<CustomerInfoView> getCustomerList(long workCaseId){
        log.info("getCustomerList ::: workCaseId : {}", workCaseId);
        List<CustomerInfoView> customerInfoViewList = new ArrayList<CustomerInfoView>();
        customerInfoViewList = customerTransform.transformToViewList(customerDAO.findCustomerBorrowerAndGuarantor(workCaseId));
        return customerInfoViewList;
    }
}
