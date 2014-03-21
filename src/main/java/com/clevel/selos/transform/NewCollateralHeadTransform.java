package com.clevel.selos.transform;
import com.clevel.selos.dao.working.NewCollateralHeadDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.master.CollateralType;
import com.clevel.selos.model.db.master.User;
import com.clevel.selos.model.db.working.NewCollateral;
import com.clevel.selos.model.db.working.NewCollateralHead;
import com.clevel.selos.model.view.NewCollateralHeadView;
import com.clevel.selos.util.Util;
import org.joda.time.DateTime;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class NewCollateralHeadTransform extends Transform {
    @Inject
    @SELOS
    Logger log;

    @Inject
    private NewCollateralSubTransform newCollateralSubTransform;
    private List<NewCollateralHead> newCollateralHeadList;
    private List<NewCollateralHeadView> newCollateralHeadViewList;

    @Inject
    public NewCollateralHeadTransform() {
    }

    public List<NewCollateralHead> transformToModel(final List<NewCollateralHeadView> newCollateralHeadViewList, final User user){
        log.debug("---- transformToNewModel [NewCollateralHeadViewList.size[{}]]", newCollateralHeadViewList.size());
        newCollateralHeadList = new ArrayList<NewCollateralHead>();
        NewCollateralHead model = null;
        for(NewCollateralHeadView view : newCollateralHeadViewList){
            model = new NewCollateralHead();
            log.debug("---- NewCollateralHead created");
            if(!Util.isZero(view.getId())){
                model.setId(view.getId());
                log.debug("---- NewCollateralHead.id[{}]", model.getId());
            } else {
                model.setCreateDate(DateTime.now().toDate());
                model.setCreateBy(user);
            }
            model.setCollID(view.getCollID());
            if(!Util.isNull(view.getHeadCollType()) && !Util.isZero(view.getHeadCollType().getId())){
                model.setHeadCollType(view.getHeadCollType());
                log.debug("------ NewCollateralHead.CollateralType.id[{}]", model.getHeadCollType().getId());
            } else {
                model.setHeadCollType(null);
            }
            if(!Util.isNull(view.getCollTypePercentLTV()) && !Util.isZero(view.getCollTypePercentLTV().getId())){
                model.setCollTypePercentLTV(view.getCollTypePercentLTV());
                log.debug("------ NewCollateralHead.CollateralType.id[{}]", model.getCollTypePercentLTV().getId());
            } else {
                model.setCollTypePercentLTV(null);
            }
            model.setCollateralLocation(view.getCollateralLocation());
            model.setExistingCredit(view.getExistingCredit());
            model.setInsuranceCompany(view.getInsuranceCompany());
            model.setAppraisalValue(view.getAppraisalValue());
            model.setTitleDeed(view.getTitleDeed());
            model.setNewCollateralSubList(newCollateralSubTransform.transformToModel(Util.safetyList(view.getNewCollateralSubViewList()), user));
            model.setModifyBy(user);
            model.setModifyDate(DateTime.now().toDate());
            newCollateralHeadList.add(model);
        }
        log.debug("----[RETURNED] NewCollateralHeadList.size[{}]", newCollateralHeadList.size());
        return newCollateralHeadList;
    }

    public List<NewCollateralHead> transformToNewModel(final List<NewCollateralHeadView> newCollateralHeadViewList, final User user){
        log.debug("---- transformToNewModel [NewCollateralHeadViewList.size[{}]]", newCollateralHeadViewList.size());
        newCollateralHeadList = new ArrayList<NewCollateralHead>();
        NewCollateralHead model = null;
        for(NewCollateralHeadView view : newCollateralHeadViewList){
            model = new NewCollateralHead();
            log.debug("-- NewCollateralHead created");
            model.setCreateDate(DateTime.now().toDate());
            model.setCreateBy(user);
            model.setCollID(view.getCollID());
            if(!Util.isNull(view.getHeadCollType()) && !Util.isZero(view.getHeadCollType().getId())){
                model.setHeadCollType(view.getHeadCollType());
                log.debug("------ NewCollateralHead.CollateralType.id[{}]", model.getHeadCollType().getId());
            } else {
                model.setHeadCollType(null);
            }
            if(!Util.isNull(view.getCollTypePercentLTV()) && !Util.isZero(view.getCollTypePercentLTV().getId())){
                model.setCollTypePercentLTV(view.getCollTypePercentLTV());
                log.debug("------ NewCollateralHead.CollateralType.id[{}]", model.getCollTypePercentLTV().getId());
            } else {
                model.setCollTypePercentLTV(null);
            }
            model.setCollateralLocation(view.getCollateralLocation());
            model.setExistingCredit(view.getExistingCredit());
            model.setInsuranceCompany(view.getInsuranceCompany());
            model.setAppraisalValue(view.getAppraisalValue());
            model.setTitleDeed(view.getTitleDeed());
            model.setNewCollateralSubList(newCollateralSubTransform.transformToNewModel(Util.safetyList(view.getNewCollateralSubViewList()), user));
            model.setModifyBy(user);
            model.setModifyDate(DateTime.now().toDate());
            newCollateralHeadList.add(model);
        }
        log.debug("----[RETURNED] NewCollateralHeadList.size[{}]", newCollateralHeadList.size());
        return newCollateralHeadList;
    }
    
    public List<NewCollateralHeadView> transformToView(final List<NewCollateralHead> newCollateralHeadList){
        log.debug("---- transformToView [NewCollateralHeadList.size[{}]]", newCollateralHeadList.size());
        newCollateralHeadViewList = new ArrayList<NewCollateralHeadView>();
        NewCollateralHeadView view = null;
        for(NewCollateralHead model : newCollateralHeadList){
            view = new NewCollateralHeadView();
            log.debug("---- NewCollateralHeadView created");
            if(!Util.isZero(model.getId())){
                view.setId(model.getId());
                log.debug("---- NewCollateralHeadView.id[{}]", view.getId());
            } else {
                view.setCreateDate(model.getCreateDate());
                view.setCreateBy(model.getCreateBy());
            }
            view.setCollID(model.getCollID());
            if(!Util.isNull(model.getHeadCollType()) && !Util.isZero(model.getHeadCollType().getId())){
                view.setHeadCollType(model.getHeadCollType());
                log.debug("------ NewCollateralHeadView.CollateralType.id[{}]", view.getHeadCollType().getId());
            } else {
                view.setHeadCollType(new CollateralType());
            }
            if(!Util.isNull(model.getCollTypePercentLTV()) && !Util.isZero(model.getCollTypePercentLTV().getId())){
                view.setCollTypePercentLTV(model.getCollTypePercentLTV());
                log.debug("------ NewCollateralHeadView.CollateralType.id[{}]", view.getCollTypePercentLTV().getId());
            } else {
                view.setCollTypePercentLTV(new CollateralType());
            }
            view.setCollateralLocation(model.getCollateralLocation());
            view.setExistingCredit(model.getExistingCredit());
            view.setInsuranceCompany(model.getInsuranceCompany());
            view.setAppraisalValue(model.getAppraisalValue());
            view.setTitleDeed(model.getTitleDeed());
            view.setNewCollateralSubViewList(newCollateralSubTransform.transformToView(Util.safetyList(model.getNewCollateralSubList())));
            view.setModifyBy(model.getModifyBy());
            view.setModifyDate(model.getModifyDate());
            newCollateralHeadViewList.add(view);
            /*view.getSubCollateralType();
            view.getPotential();
            view.getNewCollateral();
            view.getNewCollateralSubList();
            view.getCollateralChar();
            view.getNumberOfDocuments();
            view.getPurposeReviewAppraisal();
            view.getPurposeNewAppraisal();
            view.getPurposeReviewBuilding();
            view.getProposeType(); */
        }
        log.debug("----[RETURNED] NewCollateralHeadViewList.size[{}]", newCollateralHeadViewList.size());
        return newCollateralHeadViewList;
    }

    public NewCollateralHeadView copyToNewView(NewCollateralHeadView originalNewCollateralHeadView, boolean isNewId) {
        NewCollateralHeadView newCollateralHeadView = new NewCollateralHeadView();
        if (originalNewCollateralHeadView != null) {
            newCollateralHeadView.setId(isNewId ? 0 : originalNewCollateralHeadView.getId());
            newCollateralHeadView.setNo(originalNewCollateralHeadView.getNo());
            newCollateralHeadView.setTitleDeed(originalNewCollateralHeadView.getTitleDeed());
            newCollateralHeadView.setCollateralLocation(originalNewCollateralHeadView.getCollateralLocation());
            newCollateralHeadView.setAppraisalValue(originalNewCollateralHeadView.getAppraisalValue());
            newCollateralHeadView.setHeadCollType(originalNewCollateralHeadView.getHeadCollType());
            newCollateralHeadView.setCollTypePercentLTV(originalNewCollateralHeadView.getCollTypePercentLTV());
            newCollateralHeadView.setPotentialCollateral(originalNewCollateralHeadView.getPotentialCollateral());
            newCollateralHeadView.setCollID(originalNewCollateralHeadView.getCollID());
            newCollateralHeadView.setExistingCredit(originalNewCollateralHeadView.getExistingCredit());
            newCollateralHeadView.setInsuranceCompany(originalNewCollateralHeadView.getInsuranceCompany());
            newCollateralHeadView.setCreateDate(originalNewCollateralHeadView.getCreateDate());
            newCollateralHeadView.setCreateBy(originalNewCollateralHeadView.getCreateBy());
            newCollateralHeadView.setModifyDate(originalNewCollateralHeadView.getModifyDate());
            newCollateralHeadView.setModifyBy(originalNewCollateralHeadView.getModifyBy());
            newCollateralHeadView.setNewCollateralSubViewList(newCollateralSubTransform.copyToNewViews(originalNewCollateralHeadView.getNewCollateralSubViewList(), isNewId));
        }
        return newCollateralHeadView;
    }

    public List<NewCollateralHeadView> copyToNewViews(List<NewCollateralHeadView> originalNewCollHeadViews, boolean isNewId) {
        List<NewCollateralHeadView> newCollHeadViews = new ArrayList<NewCollateralHeadView>();
        if (originalNewCollHeadViews != null && originalNewCollHeadViews.size() > 0) {
            for (NewCollateralHeadView originalNewCollHeadView : originalNewCollHeadViews) {
                newCollHeadViews.add(copyToNewView(originalNewCollHeadView, isNewId));
            }
        }
        return newCollHeadViews;
    }
}
