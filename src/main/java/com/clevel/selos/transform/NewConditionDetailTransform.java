package com.clevel.selos.transform;

import com.clevel.selos.model.db.master.User;
import com.clevel.selos.model.db.working.NewConditionDetail;
import com.clevel.selos.model.db.working.NewCreditFacility;
import com.clevel.selos.model.view.NewConditionDetailView;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class NewConditionDetailTransform extends Transform {

    @Inject
    public NewConditionDetailTransform() {
    }

    public List<NewConditionDetail> transformToModel(List<NewConditionDetailView> newConditionDetailViewList, NewCreditFacility newCreditFacility, User user) {

        List<NewConditionDetail> newConditionDetails = new ArrayList<NewConditionDetail>();
        NewConditionDetail newConditionDetail;

        for (NewConditionDetailView newConditionDetailView : newConditionDetailViewList) {
            newConditionDetail = new NewConditionDetail();
            newConditionDetail.setId(newConditionDetailView.getId());
            newConditionDetail.setModifyDate(newConditionDetailView.getModifyDate());
            newConditionDetail.setModifyBy(newConditionDetailView.getModifyBy());
            newConditionDetail.setCreateDate(new Date());
            newConditionDetail.setCreateBy(user);
            newConditionDetail.setNo(newConditionDetailView.getNo());
            newConditionDetail.setConditionDesc(newConditionDetailView.getConditionDesc());
            newConditionDetail.setLoanType(newConditionDetailView.getLoanType());
            newConditionDetail.setNewCreditFacility(newCreditFacility);
            newConditionDetails.add(newConditionDetail);
        }

        return newConditionDetails;
    }

    public List<NewConditionDetailView> transformToView(List<NewConditionDetail> newConditionDetailList) {

        List<NewConditionDetailView> newConditionDetailViewList = new ArrayList<NewConditionDetailView>();
        NewConditionDetailView newConditionDetailView;

        for (NewConditionDetail newConditionDetail : newConditionDetailList) {
            newConditionDetailView = new NewConditionDetailView();
//            newConditionDetailView.setId(newConditionDetail.getId());
            newConditionDetailView.setCreateBy(newConditionDetail.getCreateBy());
            newConditionDetailView.setCreateDate(newConditionDetail.getCreateDate());
            newConditionDetailView.setNo(newConditionDetail.getNo());
            newConditionDetailView.setLoanType(newConditionDetail.getLoanType());
            newConditionDetailView.setConditionDesc(newConditionDetail.getConditionDesc());
            newConditionDetailView.setModifyBy(newConditionDetail.getModifyBy());
            newConditionDetailView.setModifyDate(newConditionDetail.getModifyDate());
            newConditionDetailViewList.add(newConditionDetailView);
        }

        return newConditionDetailViewList;
    }
}
