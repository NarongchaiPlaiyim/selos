package com.clevel.selos.transform;

import com.clevel.selos.model.db.master.User;
import com.clevel.selos.model.db.working.NewCollateral;
import com.clevel.selos.model.db.working.NewCollateralHead;
import com.clevel.selos.model.view.NewCollateralHeadDetailView;
import com.clevel.selos.model.view.NewSubCollateralDetailView;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class NewCollHeadDetailTransform extends Transform {

    @Inject
    public NewCollHeadDetailTransform() {
    }

    @Inject
    NewSubCollDetailTransform newSubCollDetailTransform;

    public NewCollateralHead transformNewCollateralHeadDetailViewToModel(NewCollateralHeadDetailView newCollateralHeadDetailView, NewCollateral collateralDetail, User user) {

        NewCollateralHead collateralHeaderDetail = new NewCollateralHead();

        if (newCollateralHeadDetailView.getId() != 0) {
            collateralHeaderDetail.setId(newCollateralHeadDetailView.getId());
            collateralHeaderDetail.setCreateDate(newCollateralHeadDetailView.getCreateDate());
            collateralHeaderDetail.setCreateBy(newCollateralHeadDetailView.getCreateBy());
        } else { // id = 0 create new
            collateralHeaderDetail.setCreateDate(new Date());
            collateralHeaderDetail.setCreateBy(user);
        }

        collateralHeaderDetail.setHeadCollType(newCollateralHeadDetailView.getHeadCollType());
        collateralHeaderDetail.setPotential(newCollateralHeadDetailView.getPotentialCollateral());
        collateralHeaderDetail.setCollateralLocation(newCollateralHeadDetailView.getCollateralLocation());
        collateralHeaderDetail.setTitleDeed(newCollateralHeadDetailView.getTitleDeed());
        collateralHeaderDetail.setCollTypePercentLTV(newCollateralHeadDetailView.getCollTypePercentLTV());
        collateralHeaderDetail.setExistingCredit(newCollateralHeadDetailView.getExistingCredit());
        collateralHeaderDetail.setInsuranceCompany(newCollateralHeadDetailView.getInsuranceCompany());
        collateralHeaderDetail.setAppraisalValue(newCollateralHeadDetailView.getAppraisalValue());
        collateralHeaderDetail.setModifyBy(newCollateralHeadDetailView.getModifyBy());
        collateralHeaderDetail.setModifyDate(newCollateralHeadDetailView.getModifyDate());
        collateralHeaderDetail.setNewCollateral(collateralDetail);

        return collateralHeaderDetail;
    }

    public List<NewCollateralHead> transformToModel(List<NewCollateralHeadDetailView> newCollateralHeadDetailViewList, NewCollateral collateralDetail, User user) {

        List<NewCollateralHead> collateralHeaderDetailList = new ArrayList<NewCollateralHead>();
        NewCollateralHead collateralHeaderDetail;

        for (NewCollateralHeadDetailView newCollateralHeadDetailView : newCollateralHeadDetailViewList) {
            collateralHeaderDetail = new NewCollateralHead();

            if (newCollateralHeadDetailView.getId() != 0) {
                collateralHeaderDetail.setId(newCollateralHeadDetailView.getId());
                collateralHeaderDetail.setCreateDate(newCollateralHeadDetailView.getCreateDate());
                collateralHeaderDetail.setCreateBy(newCollateralHeadDetailView.getCreateBy());
            } else { // id = 0 create new
                collateralHeaderDetail.setCreateDate(new Date());
                collateralHeaderDetail.setCreateBy(user);
            }

            collateralHeaderDetail.setHeadCollType(newCollateralHeadDetailView.getHeadCollType());
            collateralHeaderDetail.setPotential(newCollateralHeadDetailView.getPotentialCollateral());
            collateralHeaderDetail.setCollateralLocation(newCollateralHeadDetailView.getCollateralLocation());
            collateralHeaderDetail.setTitleDeed(newCollateralHeadDetailView.getTitleDeed());
            collateralHeaderDetail.setCollTypePercentLTV(newCollateralHeadDetailView.getCollTypePercentLTV());
            collateralHeaderDetail.setExistingCredit(newCollateralHeadDetailView.getExistingCredit());
            collateralHeaderDetail.setInsuranceCompany(newCollateralHeadDetailView.getInsuranceCompany());
            collateralHeaderDetail.setAppraisalValue(newCollateralHeadDetailView.getAppraisalValue());
            collateralHeaderDetail.setModifyBy(newCollateralHeadDetailView.getModifyBy());
            collateralHeaderDetail.setModifyDate(newCollateralHeadDetailView.getModifyDate());
            collateralHeaderDetail.setNewCollateral(collateralDetail);
            collateralHeaderDetailList.add(collateralHeaderDetail);
        }

        return collateralHeaderDetailList;
    }

    public List<NewCollateralHeadDetailView> transformToView(List<NewCollateralHead> collateralHeaderDetailList) {

        List<NewCollateralHeadDetailView> newCollateralHeadDetailViewList = new ArrayList<NewCollateralHeadDetailView>();
        NewCollateralHeadDetailView newCollateralHeadDetailView;

        for (NewCollateralHead collateralHeaderDetail : collateralHeaderDetailList) {
            newCollateralHeadDetailView = new NewCollateralHeadDetailView();
            newCollateralHeadDetailView.setCreateBy(collateralHeaderDetail.getCreateBy());
            newCollateralHeadDetailView.setCreateDate(collateralHeaderDetail.getCreateDate());
            newCollateralHeadDetailView.setModifyBy(collateralHeaderDetail.getModifyBy());
            newCollateralHeadDetailView.setModifyDate(collateralHeaderDetail.getModifyDate());
            newCollateralHeadDetailView.setHeadCollType(collateralHeaderDetail.getHeadCollType());
            newCollateralHeadDetailView.setPotentialCollateral(collateralHeaderDetail.getPotential());
            newCollateralHeadDetailView.setCollateralLocation(collateralHeaderDetail.getCollateralLocation());
            newCollateralHeadDetailView.setTitleDeed(collateralHeaderDetail.getTitleDeed());
            newCollateralHeadDetailView.setCollTypePercentLTV(collateralHeaderDetail.getCollTypePercentLTV());
            newCollateralHeadDetailView.setExistingCredit(collateralHeaderDetail.getExistingCredit());
            newCollateralHeadDetailView.setInsuranceCompany(collateralHeaderDetail.getInsuranceCompany());
            newCollateralHeadDetailView.setAppraisalValue(collateralHeaderDetail.getAppraisalValue());
            newCollateralHeadDetailView.setModifyBy(collateralHeaderDetail.getModifyBy());
            newCollateralHeadDetailView.setModifyDate(collateralHeaderDetail.getModifyDate());
            newCollateralHeadDetailView.setCreateBy(collateralHeaderDetail.getCreateBy());
            newCollateralHeadDetailView.setCreateDate(collateralHeaderDetail.getCreateDate());
            newCollateralHeadDetailView.setModifyBy(collateralHeaderDetail.getModifyBy());
            newCollateralHeadDetailView.setModifyDate(collateralHeaderDetail.getModifyDate());

            if (collateralHeaderDetail.getNewCollateralSubList() != null) {
                List<NewSubCollateralDetailView> newSubCollateralDetailViews = newSubCollDetailTransform.transformToView(collateralHeaderDetail.getNewCollateralSubList());
                newCollateralHeadDetailView.setNewSubCollateralDetailViewList(newSubCollateralDetailViews);
            }

            newCollateralHeadDetailViewList.add(newCollateralHeadDetailView);
        }

        return newCollateralHeadDetailViewList;
    }
}
