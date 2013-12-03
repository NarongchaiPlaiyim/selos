package com.clevel.selos.transform;

import com.clevel.selos.model.db.working.NewCollateralDetail;
import com.clevel.selos.model.db.working.NewCollateralHeadDetail;
import com.clevel.selos.model.view.NewCollateralHeadDetailView;
import org.joda.time.DateTime;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class NewCollHeadDetailTransform extends Transform {

    @Inject
    public NewCollHeadDetailTransform() {
    }

    public List<NewCollateralHeadDetail> transformToModel(List<NewCollateralHeadDetailView> newCollateralHeadDetailViewList,NewCollateralDetail collateralDetail) {

        List<NewCollateralHeadDetail> collateralHeaderDetailList = new ArrayList<NewCollateralHeadDetail>();
        NewCollateralHeadDetail collateralHeaderDetail;

        for (NewCollateralHeadDetailView newCollateralHeadDetailView : newCollateralHeadDetailViewList) {
            collateralHeaderDetail = new NewCollateralHeadDetail();
            if(newCollateralHeadDetailView.getId()==0){
                collateralHeaderDetail.setId(newCollateralHeadDetailView.getId());
                collateralHeaderDetail.setCreateBy(newCollateralHeadDetailView.getCreateBy());
                collateralHeaderDetail.setCreateDate(DateTime.now().toDate());
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
            collateralHeaderDetail.setNewCollateralDetail(collateralDetail);
            collateralHeaderDetailList.add(collateralHeaderDetail);
        }

        return collateralHeaderDetailList;
    }

    public List<NewCollateralHeadDetailView> transformToView(List<NewCollateralHeadDetail> collateralHeaderDetailList) {

        List<NewCollateralHeadDetailView> newCollateralHeadDetailViewList = new ArrayList<NewCollateralHeadDetailView>();
        NewCollateralHeadDetailView newCollateralHeadDetailView;

        for (NewCollateralHeadDetail collateralHeaderDetail: collateralHeaderDetailList) {
            newCollateralHeadDetailView = new NewCollateralHeadDetailView();
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

            newCollateralHeadDetailViewList.add(newCollateralHeadDetailView);
        }

        return newCollateralHeadDetailViewList;
    }
}
