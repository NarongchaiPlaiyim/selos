package com.clevel.selos.transform;

import com.clevel.selos.model.db.working.CollateralHeaderDetail;
import com.clevel.selos.model.db.working.SubCollateralDetail;
import com.clevel.selos.model.view.SubCollateralDetailView;
import org.joda.time.DateTime;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class SubCollateralDetailTransform extends Transform {

    @Inject
    public SubCollateralDetailTransform() {
    }

    public List<SubCollateralDetail> transformToModel(List<SubCollateralDetailView> subCollateralDetailViewList , CollateralHeaderDetail collateralHeaderDetail) {

        List<SubCollateralDetail> subCollateralDetailList = new ArrayList<SubCollateralDetail>();
        SubCollateralDetail subCollateralDetail;

        for (SubCollateralDetailView subCollateralDetailView: subCollateralDetailViewList) {
            subCollateralDetail = new SubCollateralDetail();
            if(subCollateralDetailView.getId()==0){
                subCollateralDetail.setCreateBy(subCollateralDetailView.getCreateBy());
                subCollateralDetail.setCreateDate(DateTime.now().toDate());
            }

            subCollateralDetail.setNo(subCollateralDetailView.getNo());
            subCollateralDetail.setTitleDeed(subCollateralDetailView.getTitleDeed());
            subCollateralDetail.setAppraisalValue(subCollateralDetailView.getAppraisalValue());
            subCollateralDetail.setAddress(subCollateralDetailView.getAddress());
            subCollateralDetail.setCollateralOwner(subCollateralDetailView.getCollateralOwner());
            subCollateralDetail.setSubCollateralType(subCollateralDetailView.getSubCollateralType());
            subCollateralDetail.setModifyBy(subCollateralDetailView.getModifyBy());
            subCollateralDetail.setModifyDate(subCollateralDetailView.getModifyDate());
            subCollateralDetail.setCollateralHeaderDetail(collateralHeaderDetail);
            subCollateralDetailList.add(subCollateralDetail);
        }

        return subCollateralDetailList;
    }

    public List<SubCollateralDetailView> transformToView(List<SubCollateralDetail> subCollateralDetailList) {

        List<SubCollateralDetailView> subCollateralDetailViewList = new ArrayList<SubCollateralDetailView>();
        SubCollateralDetailView subCollateralDetailView;

        for (SubCollateralDetail subCollateralDetail: subCollateralDetailList) {
            subCollateralDetailView = new SubCollateralDetailView();
            subCollateralDetailView.setId(subCollateralDetail.getId());
            subCollateralDetailView.setNo(subCollateralDetail.getNo());
            subCollateralDetailView.setTitleDeed(subCollateralDetail.getTitleDeed());
            subCollateralDetailView.setAppraisalValue(subCollateralDetail.getAppraisalValue());
            subCollateralDetailView.setAddress(subCollateralDetail.getAddress());
            subCollateralDetailView.setCollateralOwner(subCollateralDetail.getCollateralOwner());
            subCollateralDetailView.setSubCollateralType(subCollateralDetail.getSubCollateralType());
            subCollateralDetailView.setCreateBy(subCollateralDetail.getCreateBy());
            subCollateralDetailView.setCreateDate(subCollateralDetail.getCreateDate());
            subCollateralDetailView.setModifyBy(subCollateralDetail.getModifyBy());
            subCollateralDetailView.setModifyDate(subCollateralDetail.getModifyDate());
            subCollateralDetailViewList.add(subCollateralDetailView);
        }

        return subCollateralDetailViewList;
    }
}
