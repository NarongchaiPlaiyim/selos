package com.clevel.selos.transform;

import com.clevel.selos.model.db.working.CollateralHeaderDetail;
import com.clevel.selos.model.db.working.SubCollateralDetail;
import com.clevel.selos.model.view.NewSubCollateralDetailView;
import org.joda.time.DateTime;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class NewCreditDetailTransform extends Transform {

    @Inject
    public NewCreditDetailTransform() {
    }

    public List<SubCollateralDetail> transformToModel(List<NewSubCollateralDetailView> newSubCollateralDetailViewList, CollateralHeaderDetail collateralHeaderDetail) {

        List<SubCollateralDetail> subCollateralDetailList = new ArrayList<SubCollateralDetail>();
        SubCollateralDetail subCollateralDetail;

        for (NewSubCollateralDetailView newSubCollateralDetailView : newSubCollateralDetailViewList) {
            subCollateralDetail = new SubCollateralDetail();
            if(newSubCollateralDetailView.getId()==0){
                subCollateralDetail.setCreateBy(newSubCollateralDetailView.getCreateBy());
                subCollateralDetail.setCreateDate(DateTime.now().toDate());
            }

            subCollateralDetail.setNo(newSubCollateralDetailView.getNo());
            subCollateralDetail.setTitleDeed(newSubCollateralDetailView.getTitleDeed());
            subCollateralDetail.setAppraisalValue(newSubCollateralDetailView.getAppraisalValue());
            subCollateralDetail.setAddress(newSubCollateralDetailView.getAddress());
            subCollateralDetail.setCollateralOwner(newSubCollateralDetailView.getCollateralOwner());
            subCollateralDetail.setSubCollateralType(newSubCollateralDetailView.getSubCollateralType());
            subCollateralDetail.setModifyBy(newSubCollateralDetailView.getModifyBy());
            subCollateralDetail.setModifyDate(newSubCollateralDetailView.getModifyDate());
            subCollateralDetail.setCollateralHeaderDetail(collateralHeaderDetail);
            subCollateralDetailList.add(subCollateralDetail);
        }

        return subCollateralDetailList;
    }

    public List<NewSubCollateralDetailView> transformToView(List<SubCollateralDetail> subCollateralDetailList) {

        List<NewSubCollateralDetailView> newSubCollateralDetailViewList = new ArrayList<NewSubCollateralDetailView>();
        NewSubCollateralDetailView newSubCollateralDetailView;

        for (SubCollateralDetail subCollateralDetail: subCollateralDetailList) {
            newSubCollateralDetailView = new NewSubCollateralDetailView();
            newSubCollateralDetailView.setId(subCollateralDetail.getId());
            newSubCollateralDetailView.setNo(subCollateralDetail.getNo());
            newSubCollateralDetailView.setTitleDeed(subCollateralDetail.getTitleDeed());
            newSubCollateralDetailView.setAppraisalValue(subCollateralDetail.getAppraisalValue());
            newSubCollateralDetailView.setAddress(subCollateralDetail.getAddress());
            newSubCollateralDetailView.setCollateralOwner(subCollateralDetail.getCollateralOwner());
            newSubCollateralDetailView.setSubCollateralType(subCollateralDetail.getSubCollateralType());
            newSubCollateralDetailView.setCreateBy(subCollateralDetail.getCreateBy());
            newSubCollateralDetailView.setCreateDate(subCollateralDetail.getCreateDate());
            newSubCollateralDetailView.setModifyBy(subCollateralDetail.getModifyBy());
            newSubCollateralDetailView.setModifyDate(subCollateralDetail.getModifyDate());
            newSubCollateralDetailViewList.add(newSubCollateralDetailView);
        }

        return newSubCollateralDetailViewList;
    }
}
