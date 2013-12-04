package com.clevel.selos.transform;

import com.clevel.selos.model.db.working.CollateralDetail;
import com.clevel.selos.model.db.working.CollateralHeaderDetail;
import com.clevel.selos.model.view.CollateralHeaderDetailView;
import org.joda.time.DateTime;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class CollateralHeaderDetailTransform extends Transform {

    @Inject
    public CollateralHeaderDetailTransform() {
    }

    public List<CollateralHeaderDetail> transformToModel(List<CollateralHeaderDetailView> collateralHeaderDetailViewList,CollateralDetail collateralDetail) {

        List<CollateralHeaderDetail> collateralHeaderDetailList = new ArrayList<CollateralHeaderDetail>();
        CollateralHeaderDetail collateralHeaderDetail;

        for (CollateralHeaderDetailView collateralHeaderDetailView: collateralHeaderDetailViewList) {
            collateralHeaderDetail = new CollateralHeaderDetail();
            if(collateralHeaderDetailView.getId()==0){
                collateralHeaderDetail.setCreateBy(collateralHeaderDetailView.getCreateBy());
                collateralHeaderDetail.setCreateDate(DateTime.now().toDate());
            }

            collateralHeaderDetail.setNo(collateralHeaderDetailView.getNo());
            collateralHeaderDetail.setCollateralLocation(collateralHeaderDetailView.getCollateralLocation());
            collateralHeaderDetail.setTitleDeed(collateralHeaderDetailView.getTitleDeed());
            collateralHeaderDetail.setAppraisalValue(collateralHeaderDetailView.getAppraisalValue());
            collateralHeaderDetail.setHeadCollType(collateralHeaderDetailView.getHeadCollType());
            collateralHeaderDetail.setCollateralDetail(collateralDetail);

            collateralHeaderDetail.setModifyBy(collateralHeaderDetailView.getModifyBy());
            collateralHeaderDetail.setModifyDate(collateralHeaderDetailView.getModifyDate());

            collateralHeaderDetailList.add(collateralHeaderDetail);
        }

        return collateralHeaderDetailList;
    }

    public List<CollateralHeaderDetailView> transformToView(List<CollateralHeaderDetail> collateralHeaderDetailList) {

        List<CollateralHeaderDetailView> collateralHeaderDetailViewList = new ArrayList<CollateralHeaderDetailView>();
        CollateralHeaderDetailView collateralHeaderDetailView;

        for (CollateralHeaderDetail collateralHeaderDetail: collateralHeaderDetailList) {
            collateralHeaderDetailView = new CollateralHeaderDetailView();

            collateralHeaderDetailView.setNo(collateralHeaderDetail.getNo());
            collateralHeaderDetailView.setCollateralLocation(collateralHeaderDetail.getCollateralLocation());
            collateralHeaderDetailView.setTitleDeed(collateralHeaderDetail.getTitleDeed());
            collateralHeaderDetailView.setAppraisalValue(collateralHeaderDetail.getAppraisalValue());
            collateralHeaderDetailView.setHeadCollType(collateralHeaderDetail.getHeadCollType());
            collateralHeaderDetailView.setCreateBy(collateralHeaderDetail.getCreateBy());
            collateralHeaderDetailView.setCreateDate(collateralHeaderDetail.getCreateDate());
            collateralHeaderDetailView.setModifyBy(collateralHeaderDetail.getModifyBy());
            collateralHeaderDetailView.setModifyDate(collateralHeaderDetail.getModifyDate());

            collateralHeaderDetailViewList.add(collateralHeaderDetailView);
        }

        return collateralHeaderDetailViewList;
    }
}
