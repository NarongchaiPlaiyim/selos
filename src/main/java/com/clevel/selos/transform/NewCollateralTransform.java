package com.clevel.selos.transform;

import com.clevel.selos.dao.master.CollateralTypeDAO;
import com.clevel.selos.dao.master.MortgageTypeDAO;
import com.clevel.selos.dao.master.SubCollateralTypeDAO;
import com.clevel.selos.dao.working.*;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.master.MortgageType;
import com.clevel.selos.model.db.master.User;
import com.clevel.selos.model.db.working.*;
import com.clevel.selos.model.view.*;
import com.clevel.selos.util.DateTimeUtil;
import com.clevel.selos.util.Util;
import org.joda.time.DateTime;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class NewCollateralTransform extends Transform {
    @Inject
    @SELOS
    Logger log;
    @Inject
    private NewCollateralHeadTransform newCollateralHeadTransform;
    @Inject
    private NewCollateralDAO newCollateralDAO;
    private List<NewCollateral> newCollateralList;
    private List<NewCollateralView> newCollateralViewList;
    @Inject
    public NewCollateralTransform() {

    }


    public List<NewCollateral> transformsCollateralToModel(List<NewCollateralView> newCollateralViewList, NewCreditFacility newCreditFacility, User user) {
        List<NewCollateral> newCollateralList = new ArrayList<NewCollateral>();
        NewCollateral newCollateral;

        for (NewCollateralView newCollateralView : newCollateralViewList) {
            newCollateral = new NewCollateral();
            newCollateral.setProposeType("P");
            if (newCollateralView.getId() != 0) {
                newCollateral.setCreateDate(newCollateralView.getCreateDate());
                newCollateral.setCreateBy(newCollateralView.getCreateBy());
            } else { // id = 0 create new
                newCollateral.setCreateDate(new Date());
                newCollateral.setCreateBy(user);
            }
            newCollateral.setJobID(newCollateralView.getJobID());
            newCollateral.setAadDecision(newCollateralView.getAadDecision());
            newCollateral.setAadDecisionReason(newCollateralView.getAadDecisionReason());
            newCollateral.setAadDecisionReasonDetail(newCollateralView.getAadDecisionReasonDetail());
            newCollateral.setAppraisalDate(newCollateralView.getAppraisalDate());
            newCollateral.setBdmComments(newCollateralView.getBdmComments());
            newCollateral.setMortgageCondition(newCollateralView.getMortgageCondition());
            newCollateral.setMortgageConditionDetail(newCollateralView.getMortgageConditionDetail());
            newCollateral.setTypeOfUsage(newCollateralView.getTypeOfUsage());
            newCollateral.setUsage(newCollateralView.getUsage());
            newCollateral.setUwDecision(newCollateralView.getUwDecision());
            newCollateral.setUwRemark(newCollateralView.getUwRemark());
            newCollateral.setNewCreditFacility(newCreditFacility);
            newCollateralList.add(newCollateral);
        }
        return newCollateralList;
    }

    public List<NewCollateral> transformToModel(final List<NewCollateralView> newCollateralViewList, final User user, final NewCreditFacility newCreditFacility){
        log.debug("-- transform List<NewCollateralView> to List<NewCollateral>(Size of list is {})", ""+newCollateralViewList.size());
        newCollateralList = new ArrayList<NewCollateral>();
        NewCollateral model = null;
        long id = 0;
        for(NewCollateralView view : newCollateralViewList){
            id = view.getId();
            if(id != 0){
                model = newCollateralDAO.findById(id);
            } else {
                model = new NewCollateral();
                log.debug("-- NewCollateral created");
                model.setCreateBy(user);
                model.setCreateDate(DateTime.now().toDate());
                model.setNewCreditFacility(newCreditFacility);
            }
            model.setModifyBy(user);
            model.setModifyDate(DateTime.now().toDate());
            model.setJobID(view.getJobID());
            model.setAppraisalDate(view.getAppraisalDate());
            model.setAadDecision(view.getAadDecision());
            model.setAadDecisionReason(view.getAadDecisionReason());
            model.setAadDecisionReasonDetail(view.getAadDecisionReasonDetail());
            model.setUsage(view.getUsage());
            model.setTypeOfUsage(view.getTypeOfUsage());
            model.setMortgageCondition(view.getMortgageCondition());
            model.setMortgageConditionDetail(view.getMortgageConditionDetail());
            model.setNewCollateralHeadList(newCollateralHeadTransform.transformToModel(Util.safetyList(view.getNewCollateralHeadViewList()), user));
            newCollateralList.add(model);
        }
        return newCollateralList;
    }

    public List<NewCollateral> transformToNewModel(final List<NewCollateralView> newCollateralViewList, final User user, final NewCreditFacility newCreditFacility){
        log.debug("-- transform List<NewCollateralView> to new List<NewCollateral>(Size of list is {})", ""+newCollateralViewList.size());
        newCollateralList = new ArrayList<NewCollateral>();
        NewCollateral model = null;
        for(NewCollateralView view : newCollateralViewList){
            model = new NewCollateral();
            log.debug("-- NewCollateral created");
            model.setCreateBy(user);
            model.setCreateDate(DateTime.now().toDate());
            model.setNewCreditFacility(newCreditFacility);
            model.setModifyBy(user);
            model.setModifyDate(DateTime.now().toDate());
            model.setJobID(view.getJobID());
            model.setAppraisalDate(view.getAppraisalDate());
            model.setAadDecision(view.getAadDecision());
            model.setAadDecisionReason(view.getAadDecisionReason());
            model.setAadDecisionReasonDetail(view.getAadDecisionReasonDetail());
            model.setUsage(view.getUsage());
            model.setTypeOfUsage(view.getTypeOfUsage());
            model.setMortgageCondition(view.getMortgageCondition());
            model.setMortgageConditionDetail(view.getMortgageConditionDetail());
            model.setNewCollateralHeadList(newCollateralHeadTransform.transformToNewModel(Util.safetyList(view.getNewCollateralHeadViewList()), user));
            newCollateralList.add(model);
        }
        return newCollateralList;
    }

    public List<NewCollateralView> transformToView(final List<NewCollateral> newCollateralList){
        log.debug("-- transform List<NewCollateral> to List<NewCollateralView>(Size of list is {})", ""+newCollateralList.size());
        newCollateralViewList = new ArrayList<NewCollateralView>();
        NewCollateralView view = null;
        for(NewCollateral model : newCollateralList){
            view = new NewCollateralView();
            view.setId(model.getId());
            view.setJobID(model.getJobID());
            view.setJobIDSearch(model.getJobID());
            view.setAppraisalDate(model.getAppraisalDate());
            view.setAadDecision(model.getAadDecision());
            view.setAadDecisionReason(model.getAadDecisionReason());
            view.setAadDecisionReasonDetail(model.getAadDecisionReasonDetail());
            view.setUsage(model.getUsage());
            view.setTypeOfUsage(model.getTypeOfUsage());
            view.setMortgageCondition(model.getMortgageCondition());
            view.setMortgageConditionDetail(model.getMortgageConditionDetail());
            view.setCreateBy(model.getCreateBy());
            view.setCreateDate(model.getCreateDate());
            view.setNewCollateralHeadViewList(newCollateralHeadTransform.transformToView(Util.safetyList(model.getNewCollateralHeadList())));
            newCollateralViewList.add(view);
        }
        return newCollateralViewList;
    }
}
