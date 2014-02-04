package com.clevel.selos.transform;

import com.clevel.selos.dao.working.NewCollateralDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.master.User;
import com.clevel.selos.model.db.working.*;
import com.clevel.selos.model.view.AppraisalDetailView;
import com.clevel.selos.model.view.NewCollateralHeadView;
import com.clevel.selos.util.Util;
import org.joda.time.DateTime;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class AppraisalDetailTransform extends Transform {
    @Inject
    @SELOS
    Logger log;
    @Inject
    private NewCollateralHeadTransform newCollateralHeadTransform;
    @Inject
    private NewCollateralDAO newCollateralDAO;
    private List<NewCollateral> newCollateralList;
    private List<AppraisalDetailView> appraisalDetailViewList;
    @Inject
    public AppraisalDetailTransform() {

    }

    public List<NewCollateral> transformToModel(final List<AppraisalDetailView> appraisalDetailViewList, final User user, final NewCreditFacility newCreditFacility){
        log.debug("-- transform List<NewCollateral> to List<AppraisalDetailView>(Size of list is {})", ""+appraisalDetailViewList.size());
        newCollateralList = new ArrayList<NewCollateral>();
//        NewCollateral newCollateral = null;
        NewCollateralHead model = null;
        List<NewCollateralHead> newCollateralHeadList = null;
        long id = 0;

        newCollateralList = Util.safetyList(newCollateralDAO.findNewCollateralByNewCreditFacility(newCreditFacility));

        for(NewCollateral newCollateral : newCollateralList){
            newCollateralHeadList = Util.safetyList(newCollateral.getNewCollateralHeadList());
        }


//        for(AppraisalDetailView view : appraisalDetailViewList){
//            id = view.getId();
//            newCollateral = newCollateralDAO.findById(id);
//            newCollateralHeadList = Util.safetyList(newCollateral.getNewCollateralHeadList());
//            for(NewCollateralHead model :  newCollateralHeadList){
//
//            }
//
//        }
        return newCollateralList;
    }

    public List<AppraisalDetailView> transformToView(final List<NewCollateral> newCollateralList){
        log.debug("-- transform List<NewCollateral> to List<AppraisalDetailView>(Size of list is {})", ""+newCollateralList.size());
        appraisalDetailViewList = new ArrayList<AppraisalDetailView>();
        AppraisalDetailView view = null;
        List<NewCollateralHead> newCollateralHeadList = null;
        for(NewCollateral newCollateral : newCollateralList) {
            newCollateralHeadList = Util.safetyList(newCollateral.getNewCollateralHeadList());
            for (NewCollateralHead model  : newCollateralHeadList) {
                view = new AppraisalDetailView();
                view.setNewCollateralId(newCollateral.getId());
                view.setNewCollateralHeadId(model.getId());
                view.setTitleDeed(model.getTitleDeed());
                  //TODO w8 P'LK confirm
//                view.set
                view.setCharacteristic(model.getCollateralChar());
                view.setNumberOfDocuments(model.getNumberOfDocuments());
                view.setModifyBy(model.getModifyBy());
                view.setModifyDate(model.getModifyDate());
                appraisalDetailViewList.add(view);
            }
        }
        return appraisalDetailViewList;
    }

}
