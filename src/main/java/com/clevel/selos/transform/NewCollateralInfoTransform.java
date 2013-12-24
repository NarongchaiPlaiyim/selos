package com.clevel.selos.transform;

import com.clevel.selos.dao.master.CollateralTypeDAO;
import com.clevel.selos.dao.master.SubCollateralTypeDAO;
import com.clevel.selos.dao.working.BasicInfoDAO;
import com.clevel.selos.dao.working.CustomerDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.master.User;
import com.clevel.selos.model.db.working.NewCollateralDetail;
import com.clevel.selos.model.db.working.NewCreditFacility;
import com.clevel.selos.model.view.NewCollateralInfoView;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class NewCollateralInfoTransform extends Transform {
    @Inject
    @SELOS
    Logger log;
    @Inject
    CustomerTransform customerTransform;
    @Inject
    CustomerDAO customerDAO;
    @Inject
    SubCollateralTypeDAO subCollateralTypeDAO;
    @Inject
    CollateralTypeDAO collateralTypeDAO;
    @Inject
    BasicInfoDAO basicInfoDAO;
    @Inject
    CreditTypeDetailTransform creditTypeDetailTransform;

    public NewCollateralDetail transformsNewCollateralInfoViewToModel(NewCollateralInfoView  newCollateralInfoView, NewCreditFacility newCreditFacility, User user) {
            NewCollateralDetail newCollateralDetail = new NewCollateralDetail();

            if (newCollateralInfoView.getId() != 0) {
                newCollateralDetail.setId(newCollateralInfoView.getId());
                newCollateralDetail.setCreateDate(newCollateralInfoView.getCreateDate());
                newCollateralDetail.setCreateBy(newCollateralInfoView.getCreateBy());
            } else { // id = 0 create new
                newCollateralDetail.setCreateDate(new Date());
                newCollateralDetail.setCreateBy(user);
            }
            newCollateralDetail.setJobID(newCollateralInfoView.getJobID());
            newCollateralDetail.setAadDecision(newCollateralInfoView.getAadDecision());
            newCollateralDetail.setAadDecisionReason(newCollateralInfoView.getAadDecisionReason());
            newCollateralDetail.setAadDecisionReasonDetail(newCollateralInfoView.getAadDecisionReasonDetail());
            newCollateralDetail.setAppraisalDate(newCollateralInfoView.getAppraisalDate());
            newCollateralDetail.setBdmComments(newCollateralInfoView.getBdmComments());
            newCollateralDetail.setMortgageCondition(newCollateralInfoView.getMortgageCondition());
            newCollateralDetail.setMortgageConditionDetail(newCollateralInfoView.getMortgageConditionDetail());
            newCollateralDetail.setTypeOfUsage(newCollateralInfoView.getTypeOfUsage());
            newCollateralDetail.setUsage(newCollateralInfoView.getUsage());
            newCollateralDetail.setUwDecision(newCollateralInfoView.getUwDecision());
            newCollateralDetail.setUwRemark(newCollateralInfoView.getUwRemark());
            newCollateralDetail.setNewCreditFacility(newCreditFacility);

        return newCollateralDetail;
    }

    public List<NewCollateralDetail> transformsToModel(List<NewCollateralInfoView> newCollateralInfoViewList, NewCreditFacility newCreditFacility, User user) {
        List<NewCollateralDetail> newCollateralDetailList = new ArrayList<NewCollateralDetail>();
        NewCollateralDetail newCollateralDetail;

        for (NewCollateralInfoView newCollateralInfoView : newCollateralInfoViewList) {
            newCollateralDetail = new NewCollateralDetail();

            if (newCollateralInfoView.getId() != 0) {
                newCollateralDetail.setId(newCollateralInfoView.getId());
                newCollateralDetail.setCreateDate(newCollateralInfoView.getCreateDate());
                newCollateralDetail.setCreateBy(newCollateralInfoView.getCreateBy());
            } else { // id = 0 create new
                newCollateralDetail.setCreateDate(new Date());
                newCollateralDetail.setCreateBy(user);
            }
            newCollateralDetail.setJobID(newCollateralInfoView.getJobID());
            newCollateralDetail.setAadDecision(newCollateralInfoView.getAadDecision());
            newCollateralDetail.setAadDecisionReason(newCollateralInfoView.getAadDecisionReason());
            newCollateralDetail.setAadDecisionReasonDetail(newCollateralInfoView.getAadDecisionReasonDetail());
            newCollateralDetail.setAppraisalDate(newCollateralInfoView.getAppraisalDate());
            newCollateralDetail.setBdmComments(newCollateralInfoView.getBdmComments());
            newCollateralDetail.setMortgageCondition(newCollateralInfoView.getMortgageCondition());
            newCollateralDetail.setMortgageConditionDetail(newCollateralInfoView.getMortgageConditionDetail());
            newCollateralDetail.setTypeOfUsage(newCollateralInfoView.getTypeOfUsage());
            newCollateralDetail.setUsage(newCollateralInfoView.getUsage());
            newCollateralDetail.setUwDecision(newCollateralInfoView.getUwDecision());
            newCollateralDetail.setUwRemark(newCollateralInfoView.getUwRemark());
            newCollateralDetail.setNewCreditFacility(newCreditFacility);
            newCollateralDetailList.add(newCollateralDetail);
        }
        return newCollateralDetailList;
    }

    public List<NewCollateralInfoView> transformsToView(List<NewCollateralDetail> newCollateralInfoViewList) {
        List<NewCollateralInfoView> newCollateralDInfoViewList = new ArrayList<NewCollateralInfoView>();
        NewCollateralInfoView newCollateralInfoView;

        for (NewCollateralDetail newCollateralDetail1 : newCollateralInfoViewList) {
            newCollateralInfoView = new NewCollateralInfoView();
            newCollateralInfoView.setCreateDate(newCollateralDetail1.getCreateDate());
            newCollateralInfoView.setCreateBy(newCollateralDetail1.getCreateBy());
            newCollateralInfoView.setCreateDate(newCollateralDetail1.getCreateDate());
            newCollateralInfoView.setCreateBy(newCollateralDetail1.getCreateBy());
            newCollateralInfoView.setJobID(newCollateralDetail1.getJobID());
            newCollateralInfoView.setAadDecision(newCollateralDetail1.getAadDecision());
            newCollateralInfoView.setAadDecisionReason(newCollateralDetail1.getAadDecisionReason());
            newCollateralInfoView.setAadDecisionReasonDetail(newCollateralDetail1.getAadDecisionReasonDetail());
            newCollateralInfoView.setAppraisalDate(newCollateralDetail1.getAppraisalDate());
            newCollateralInfoView.setBdmComments(newCollateralDetail1.getBdmComments());
            newCollateralInfoView.setMortgageCondition(newCollateralDetail1.getMortgageCondition());
            newCollateralInfoView.setMortgageConditionDetail(newCollateralDetail1.getMortgageConditionDetail());
            newCollateralInfoView.setTypeOfUsage(newCollateralDetail1.getTypeOfUsage());
            newCollateralInfoView.setUsage(newCollateralDetail1.getUsage());
            newCollateralInfoView.setUwDecision(newCollateralDetail1.getUwDecision());
            newCollateralInfoView.setUwRemark(newCollateralDetail1.getUwRemark());
            newCollateralDInfoViewList.add(newCollateralInfoView);
        }
        return newCollateralDInfoViewList;
    }

}
