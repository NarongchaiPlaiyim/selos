package com.clevel.selos.businesscontrol;

import com.clevel.selos.dao.master.StepLandingPageDAO;
import com.clevel.selos.dao.master.UserDAO;
import com.clevel.selos.dao.working.*;
import com.clevel.selos.filenet.bpm.services.dto.CaseDTO;
import com.clevel.selos.integration.BPMInterface;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.master.StepLandingPage;
import com.clevel.selos.model.db.master.User;
import com.clevel.selos.model.db.working.*;
import com.clevel.selos.model.view.AppBorrowerHeaderView;
import com.clevel.selos.model.view.AppHeaderView;
import com.clevel.selos.model.view.CustomerInfoView;
import com.clevel.selos.model.view.InboxView;
import com.clevel.selos.model.view.NewCollateralView;
import com.clevel.selos.model.view.insurance.InsuranceInfoView;
import com.clevel.selos.security.UserDetail;
import com.clevel.selos.transform.CustomerTransform;
import com.clevel.selos.transform.InsuranceInfoTransform;
import com.clevel.selos.transform.NewCollateralTransform;
import com.clevel.selos.transform.business.InboxBizTransform;
import com.clevel.selos.util.Util;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Stateless
public class InsuranceInfoControl extends BusinessControl {
    @Inject
    @SELOS
    private Logger log;

    @Inject
    BPMInterface bpmInterface;

    @Inject
    NewCollateralDAO newCollateralDAO;

    @Inject
    NewCollateralTransform newCollateralTransform;
    
    @Inject
    InsuranceInfoTransform insuranceInfoTransform;
    
    @Inject
    public InsuranceInfoControl(){

    }
    
    public List<InsuranceInfoView> getInsuranceInfo(long workCaseId){
    	 List<NewCollateral> newCollateralList = newCollateralDAO.findNewCollateralByTypeA(workCaseId);
    	 return insuranceInfoTransform.transformsNewCollateralToView(newCollateralList);
    }    
 
}
