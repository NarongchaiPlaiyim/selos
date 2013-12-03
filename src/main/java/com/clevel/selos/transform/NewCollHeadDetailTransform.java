package com.clevel.selos.transform;

import com.clevel.selos.model.db.working.CollateralDetail;
import com.clevel.selos.model.db.working.CollateralHeaderDetail;
import com.clevel.selos.model.view.NewCollateralHeadDetailView;
import org.joda.time.DateTime;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class NewCollHeadDetailTransform extends Transform {

    @Inject
    public NewCollHeadDetailTransform() {
    }

    public List<CollateralHeaderDetail> transformToModel(List<NewCollateralHeadDetailView> newCollateralHeadDetailViewList,CollateralDetail collateralDetail) {

        List<CollateralHeaderDetail> collateralHeaderDetailList = new ArrayList<CollateralHeaderDetail>();
        CollateralHeaderDetail collateralHeaderDetail;

        for (NewCollateralHeadDetailView newCollateralHeadDetailView : newCollateralHeadDetailViewList) {
            collateralHeaderDetail = new CollateralHeaderDetail();
            if(newCollateralHeadDetailView.getId()==0){
                collateralHeaderDetail.setCreateBy(newCollateralHeadDetailView.getCreateBy());
                collateralHeaderDetail.setCreateDate(DateTime.now().toDate());
            }

            collateralHeaderDetail.setNo(newCollateralHeadDetailView.getNo());
            collateralHeaderDetail.setCollateralLocation(newCollateralHeadDetailView.getCollateralLocation());
            collateralHeaderDetail.setTitleDeed(newCollateralHeadDetailView.getTitleDeed());
            collateralHeaderDetail.setAppraisalValue(newCollateralHeadDetailView.getAppraisalValue());
            collateralHeaderDetail.setHeadCollType(newCollateralHeadDetailView.getHeadCollType());
            collateralHeaderDetail.setCollateralDetail(collateralDetail);

            collateralHeaderDetail.setModifyBy(newCollateralHeadDetailView.getModifyBy());
            collateralHeaderDetail.setModifyDate(newCollateralHeadDetailView.getModifyDate());

            collateralHeaderDetailList.add(collateralHeaderDetail);
        }

        return collateralHeaderDetailList;
    }

    public List<NewCollateralHeadDetailView> transformToView(List<CollateralHeaderDetail> collateralHeaderDetailList) {

        List<NewCollateralHeadDetailView> newCollateralHeadDetailViewList = new ArrayList<NewCollateralHeadDetailView>();
        NewCollateralHeadDetailView newCollateralHeadDetailView;

        for (CollateralHeaderDetail collateralHeaderDetail: collateralHeaderDetailList) {
            newCollateralHeadDetailView = new NewCollateralHeadDetailView();

            newCollateralHeadDetailView.setNo(collateralHeaderDetail.getNo());
            newCollateralHeadDetailView.setCollateralLocation(collateralHeaderDetail.getCollateralLocation());
            newCollateralHeadDetailView.setTitleDeed(collateralHeaderDetail.getTitleDeed());
            newCollateralHeadDetailView.setAppraisalValue(collateralHeaderDetail.getAppraisalValue());
            newCollateralHeadDetailView.setHeadCollType(collateralHeaderDetail.getHeadCollType());
            newCollateralHeadDetailView.setCreateBy(collateralHeaderDetail.getCreateBy());
            newCollateralHeadDetailView.setCreateDate(collateralHeaderDetail.getCreateDate());
            newCollateralHeadDetailView.setModifyBy(collateralHeaderDetail.getModifyBy());
            newCollateralHeadDetailView.setModifyDate(collateralHeaderDetail.getModifyDate());

            newCollateralHeadDetailViewList.add(newCollateralHeadDetailView);
        }

        return newCollateralHeadDetailViewList;
    }
}
