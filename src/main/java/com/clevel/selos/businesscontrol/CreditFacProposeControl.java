package com.clevel.selos.businesscontrol;

import com.clevel.selos.dao.master.CollateralTypeDAO;
import com.clevel.selos.dao.master.SubCollateralTypeDAO;
import com.clevel.selos.dao.working.BasicInfoDAO;
import com.clevel.selos.dao.working.CustomerDAO;
import com.clevel.selos.dao.working.WorkCaseDAO;
import com.clevel.selos.model.db.master.User;
import com.clevel.selos.model.db.working.*;
import com.clevel.selos.model.view.CreditFacProposeView;
import com.clevel.selos.transform.*;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.List;

@Stateless
public class CreditFacProposeControl extends BusinessControl {
    @Inject
    CustomerTransform customerTransform;
    @Inject
    NewCreditFacilityTransform newCreditFacilityTransform;
    @Inject
    NewFeeDetailTransform newFeeDetailTransform;
    @Inject
    NewCreditDetailTransform newCreditDetailTransform;
    @Inject
    CreditTypeDetailTransform creditTypeDetailTransform;
    @Inject
    NewCollateralInfoTransform newCollateralInfoTransform;
    @Inject
    NewCollHeadDetailTransform newCollHeadDetailTransform;
    @Inject
    NewSubCollDetailTransform newSubCollDetailTransform;
    @Inject
    NewGuarantorDetailTransform newGuarantorDetailTransform;
    @Inject
    NewConditionDetailTransform newConditionDetailTransform;
    @Inject
    CustomerDAO customerDAO;
    @Inject
    SubCollateralTypeDAO subCollateralTypeDAO;
    @Inject
    CollateralTypeDAO collateralTypeDAO;
    @Inject
    BasicInfoDAO basicInfoDAO;
    @Inject
    WorkCaseDAO workCaseDAO;

    public CreditFacProposeControl(){}

    public List<Customer> getListOfGuarantor(long workCaseId){
        log.info("workCaseId :: {}",workCaseId);
        return customerDAO.findGuarantorByWorkCaseId(workCaseId);
    }

    public BasicInfo getBasicByWorkCaseId(long workCaseId){
        log.info("workCaseId :: {}",workCaseId);
        return basicInfoDAO.findByWorkCaseId(workCaseId);

    }

    public void onSaveNewCreditFacility(CreditFacProposeView creditFacProposeView, Long workCaseId ,User user) {
        log.info("onSaveNewCreditFacility begin");
        log.info("workCaseId {} ", workCaseId);
        WorkCase workCase = workCaseDAO.findById(workCaseId);
        CreditFacilityPropose creditFacilityPropose = newCreditFacilityTransform.transformToModelDB(creditFacProposeView,workCase,user);
       // creditFacilityProposeDAO.persist(creditFacilityPropose);      because N'KO created dao of newCreditFacility


        List<ProposeFeeDetail> proposeFeeDetailList = newFeeDetailTransform.transformToModel(creditFacProposeView.getNewFeeDetailViewList(),creditFacilityPropose);
   /*     List<NewCreditDetail> newCreditDetailList = newCreditDetailTransform.transformToModel();
        List<ProposeCollateralDetail> proposeCollateralDetailList = newCollateralInfoTransform.transformsToModel();
        List<ProposeGuarantorDetail>  proposeGuarantorDetailList = newGuarantorDetailTransform.transformToModel();
        List<ProposeConditionDetail>  proposeConditionDetailList = newConditionDetailTransform.transformToModel();
        List<CreditTypeDetail> creditTypeDetailList = creditTypeDetailTransform.transformToModel();
        List<CreditTypeDetail> creditTypeDetailList = creditTypeDetailTransform.transformToModel();
*/
//        List<SubCollateralDetail> subCollateralDetailList = newSubCollDetailTransform.transformToModel();
    }


}