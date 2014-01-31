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
import org.joda.time.DateTime;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.ArrayList;
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

    public List<NewCollateral> transformToModel(final List<NewCollateralView> newCollateralViewList, final User user, final NewCreditFacility newCreditFacility){
        log.debug("-- transformToModel(Size of list is {})", ""+newCollateralViewList.size());
        newCollateralList = new ArrayList<NewCollateral>();
        NewCollateral model = null;
        long id = 0;
        for(NewCollateralView view : newCollateralViewList){
            id = view.getId();
            if(id != 0){
                model = newCollateralDAO.findById(id);
                model.setModifyBy(user);
                model.setModifyDate(DateTime.now().toDate());
            } else {
                model = new NewCollateral();
                model.setCreateBy(user);
                model.setCreateDate(DateTime.now().toDate());
                model.setModifyBy(user);
                model.setModifyDate(DateTime.now().toDate());
                model.setNewCreditFacility(newCreditFacility);
            }
            model.setJobID(view.getJobID());
            model.setAppraisalDate(view.getAppraisalDate());
            model.setAadDecision(view.getAadDecision());
            model.setAadDecisionReason(view.getAadDecisionReason());
            model.setAadDecisionReasonDetail(view.getAadDecisionReasonDetail());
            model.setUsage(view.getUsage());
            model.setTypeOfUsage(view.getTypeOfUsage());
            model.setMortgageCondition(view.getMortgageCondition());
            model.setMortgageConditionDetail(view.getMortgageConditionDetail());
            model.setNewCollateralHeadList(newCollateralHeadTransform.transformToModel(view.getNewCollateralHeadViewList(), user));
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
            view.setNewCollateralHeadViewList(newCollateralHeadTransform.transformToView(model.getNewCollateralHeadList()));
            newCollateralViewList.add(view);
        }
        return newCollateralViewList;
    }
}
