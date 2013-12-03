package com.clevel.selos.transform;

import com.clevel.selos.model.db.working.NewCollateralHeadDetail;
import com.clevel.selos.model.db.working.NewCollateralSubDetail;
import com.clevel.selos.model.view.NewSubCollateralDetailView;
import org.joda.time.DateTime;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class NewSubCollDetailTransform extends Transform {

    @Inject
    public NewSubCollDetailTransform() {
    }

    public List<NewCollateralSubDetail> transformToModel(List<NewSubCollateralDetailView> newSubCollateralDetailViewList, NewCollateralHeadDetail collateralHeaderDetail) {

        List<NewCollateralSubDetail> subCollateralDetailList = new ArrayList<NewCollateralSubDetail>();
        NewCollateralSubDetail subCollateralDetail;

        for (NewSubCollateralDetailView newSubCollateralDetailView : newSubCollateralDetailViewList) {
            subCollateralDetail = new NewCollateralSubDetail();

            if(newSubCollateralDetailView.getId()==0){
                subCollateralDetail.setId(newSubCollateralDetailView.getId());
                subCollateralDetail.setCreateBy(newSubCollateralDetailView.getCreateBy());
                subCollateralDetail.setCreateDate(DateTime.now().toDate());
            }

            subCollateralDetail.setTitleDeed(newSubCollateralDetailView.getTitleDeed());
            subCollateralDetail.setAppraisalValue(newSubCollateralDetailView.getAppraisalValue());
            subCollateralDetail.setAddress(newSubCollateralDetailView.getAddress());
            subCollateralDetail.setCollateralOwner(newSubCollateralDetailView.getCollateralOwner());
            subCollateralDetail.setSubCollTypeCaption(newSubCollateralDetailView.getSubCollateralType());
            subCollateralDetail.setModifyBy(newSubCollateralDetailView.getModifyBy());
            subCollateralDetail.setModifyDate(newSubCollateralDetailView.getModifyDate());
            subCollateralDetail.setNewCollateralHeadDetail(collateralHeaderDetail);
            subCollateralDetailList.add(subCollateralDetail);
        }

        return subCollateralDetailList;
    }

    public List<NewSubCollateralDetailView> transformToView(List<NewCollateralSubDetail> subCollateralDetailList) {

        List<NewSubCollateralDetailView> newSubCollateralDetailViewList = new ArrayList<NewSubCollateralDetailView>();
        NewSubCollateralDetailView newSubCollateralDetailView;

        for (NewCollateralSubDetail subCollateralDetail: subCollateralDetailList) {
            newSubCollateralDetailView = new NewSubCollateralDetailView();
            newSubCollateralDetailView.setId(subCollateralDetail.getId());
            newSubCollateralDetailView.setTitleDeed(subCollateralDetail.getTitleDeed());
            newSubCollateralDetailView.setAppraisalValue(subCollateralDetail.getAppraisalValue());
            newSubCollateralDetailView.setAddress(subCollateralDetail.getAddress());
            newSubCollateralDetailView.setCollateralOwner(subCollateralDetail.getCollateralOwner());
            newSubCollateralDetailView.setSubCollateralType(subCollateralDetail.getSubCollTypeCaption());
            newSubCollateralDetailView.setCreateBy(subCollateralDetail.getCreateBy());
            newSubCollateralDetailView.setCreateDate(subCollateralDetail.getCreateDate());
            newSubCollateralDetailView.setModifyBy(subCollateralDetail.getModifyBy());
            newSubCollateralDetailView.setModifyDate(subCollateralDetail.getModifyDate());
            newSubCollateralDetailViewList.add(newSubCollateralDetailView);
        }

        return newSubCollateralDetailViewList;
    }
}
