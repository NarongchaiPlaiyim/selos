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
public class MortgageSummaryControl extends BusinessControl {
    @Inject
    @SELOS
    Logger log;
    @Inject
    MortgageSummaryDAO mortgageSummaryDAO;

    @Inject
    BasicInfoTransform basicInfoTransform;

    @Inject
    public MortgageSummaryControl(){

    }
}
