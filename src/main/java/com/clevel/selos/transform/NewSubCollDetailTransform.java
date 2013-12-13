package com.clevel.selos.transform;

import com.clevel.selos.dao.working.CustomerDAO;
import com.clevel.selos.dao.working.NewCollateralSubDetailDAO;
import com.clevel.selos.model.db.master.User;
import com.clevel.selos.model.db.working.NewCollateralHeadDetail;
import com.clevel.selos.model.db.working.NewCollateralSubDetail;
import com.clevel.selos.model.view.NewSubCollateralDetailView;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class NewSubCollDetailTransform extends Transform {

    @Inject
    public NewSubCollDetailTransform() {}

    @Inject
    CustomerDAO customerDAO;
    @Inject
    NewCollateralSubDetailDAO newCollateralSubDetailDAO;
    @Inject
    CustomerTransform customerTransform;

    public List<NewCollateralSubDetail> transformToModel(List<NewSubCollateralDetailView> newSubCollateralDetailViewList, NewCollateralHeadDetail collateralHeaderDetail,User user){

        List<NewCollateralSubDetail> subCollateralDetailList = new ArrayList<NewCollateralSubDetail>();
        NewCollateralSubDetail subCollateralDetail;

        for (NewSubCollateralDetailView newSubCollateralDetailView : newSubCollateralDetailViewList) {
            subCollateralDetail = new NewCollateralSubDetail();

            if (newSubCollateralDetailView.getId() != 0) {
                subCollateralDetail.setId(newSubCollateralDetailView.getId());
                subCollateralDetail.setCreateDate(newSubCollateralDetailView.getCreateDate());
                subCollateralDetail.setCreateBy(newSubCollateralDetailView.getCreateBy());
            } else { // id = 0 create new
                subCollateralDetail.setCreateDate(new Date());
                subCollateralDetail.setCreateBy(user);
            }

            subCollateralDetail.setMortgageList(newSubCollateralDetailView.getMortgageList());
            subCollateralDetail.setTitleDeed(newSubCollateralDetailView.getTitleDeed());
            subCollateralDetail.setAppraisalValue(newSubCollateralDetailView.getAppraisalValue());
            subCollateralDetail.setAddress(newSubCollateralDetailView.getAddress());
            subCollateralDetail.setCollateralOwner(newSubCollateralDetailView.getCollateralOwner());
            subCollateralDetail.setSubCollTypeCaption(newSubCollateralDetailView.getSubCollateralType());
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
            newSubCollateralDetailView.setMortgageList(subCollateralDetail.getMortgageList());
            newSubCollateralDetailViewList.add(newSubCollateralDetailView);
        }

        return newSubCollateralDetailViewList;
    }
}
