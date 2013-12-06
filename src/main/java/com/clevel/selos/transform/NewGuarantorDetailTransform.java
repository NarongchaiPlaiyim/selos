package com.clevel.selos.transform;

import com.clevel.selos.model.db.master.User;
import com.clevel.selos.model.db.working.NewCreditFacility;
import com.clevel.selos.model.db.working.NewGuarantorDetail;
import com.clevel.selos.model.view.NewGuarantorDetailView;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class NewGuarantorDetailTransform extends Transform {

    @Inject
    public NewGuarantorDetailTransform() {
    }

    public List<NewGuarantorDetail> transformToModel(List<NewGuarantorDetailView> newGuarantorDetailViewList, NewCreditFacility newCreditFacility, User user) {

        List<NewGuarantorDetail> newGuarantorDetailList = new ArrayList<NewGuarantorDetail>();
        NewGuarantorDetail newGuarantorDetail;

        for (NewGuarantorDetailView newGuarantorDetailView : newGuarantorDetailViewList) {
            newGuarantorDetail = new NewGuarantorDetail();
            if (newGuarantorDetailView.getId() != 0) {
                newGuarantorDetail.setId(newGuarantorDetailView.getId());
                newGuarantorDetail.setCreateDate(newGuarantorDetailView.getCreateDate());
                newGuarantorDetail.setCreateBy(newGuarantorDetailView.getCreateBy());
            } else { // id = 0 create new
                newGuarantorDetail.setCreateDate(new Date());
                newGuarantorDetail.setCreateBy(user);
            }

            newGuarantorDetail.setGuarantorName(newGuarantorDetailView.getGuarantorName());
            newGuarantorDetail.setTcgLgNo(newGuarantorDetailView.getTcgLgNo());
            newGuarantorDetail.setNewCreditFacility(newCreditFacility);
            newGuarantorDetail.setTotalLimitGuaranteeAmount(newGuarantorDetailView.getTotalLimitGuaranteeAmount());
            newGuarantorDetailList.add(newGuarantorDetail);
        }

        return newGuarantorDetailList;
    }

    public List<NewGuarantorDetailView> transformToView(List<NewGuarantorDetail> newGuarantorDetailList) {
        List<NewGuarantorDetailView> newGuarantorDetailViews = new ArrayList<NewGuarantorDetailView>();
        NewGuarantorDetailView newGuarantorDetailView;

        for (NewGuarantorDetail newGuarantorDetail : newGuarantorDetailList) {
            newGuarantorDetailView = new NewGuarantorDetailView();
            newGuarantorDetailView.setCreateDate(newGuarantorDetail.getCreateDate());
            newGuarantorDetailView.setCreateBy(newGuarantorDetail.getCreateBy());
            newGuarantorDetailView.setModifyDate(newGuarantorDetail.getModifyDate());
            newGuarantorDetailView.setModifyBy(newGuarantorDetail.getModifyBy());
            newGuarantorDetailView.setGuarantorName(newGuarantorDetail.getGuarantorName());
            newGuarantorDetailView.setTcgLgNo(newGuarantorDetail.getTcgLgNo());
            newGuarantorDetailView.setTotalLimitGuaranteeAmount(newGuarantorDetail.getTotalLimitGuaranteeAmount());
            newGuarantorDetailViews.add(newGuarantorDetailView);
        }

        return newGuarantorDetailViews;
    }
}
