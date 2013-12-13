package com.clevel.selos.transform;

import com.clevel.selos.model.db.master.User;
import com.clevel.selos.model.db.working.ExistingCreditFacility;
import com.clevel.selos.model.db.working.NewCreditFacility;
import com.clevel.selos.model.db.working.ExistingGuarantorDetail;
import com.clevel.selos.model.view.ExistingGuarantorDetailView;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ExistingGuarantorDetailTransform extends Transform {

    @Inject
    public ExistingGuarantorDetailTransform() {
    }

    public List<ExistingGuarantorDetail> transformToModel(List<ExistingGuarantorDetailView> existingGuarantorDetailViewList, ExistingCreditFacility existingCreditFacility, User user) {

        List<ExistingGuarantorDetail> existingGuarantorDetailList = new ArrayList<ExistingGuarantorDetail>();
        ExistingGuarantorDetail existingGuarantorDetail;

        for (ExistingGuarantorDetailView existingGuarantorDetailView : existingGuarantorDetailViewList) {
            existingGuarantorDetail = new ExistingGuarantorDetail();
            if (existingGuarantorDetailView.getId() != 0) {
                existingGuarantorDetail.setId(existingGuarantorDetailView.getId());
                existingGuarantorDetail.setCreateDate(existingGuarantorDetailView.getCreateDate());
                existingGuarantorDetail.setCreateBy(existingGuarantorDetailView.getCreateBy());
            } else { // id = 0 create new
                existingGuarantorDetail.setCreateDate(new Date());
                existingGuarantorDetail.setCreateBy(user);
            }

            existingGuarantorDetail.setGuarantorName(existingGuarantorDetailView.getGuarantorName());
            existingGuarantorDetail.setTcglgNo(existingGuarantorDetailView.getTcgLgNo());
            existingGuarantorDetail.setExistingCreditFacility(existingCreditFacility);
            existingGuarantorDetail.setTotalLimitGuaranteeAmount(existingGuarantorDetailView.getTotalLimitGuaranteeAmount());
            existingGuarantorDetailList.add(existingGuarantorDetail);
        }

        return existingGuarantorDetailList;
    }

    public List<ExistingGuarantorDetailView> transformToView(List<ExistingGuarantorDetail> existingGuarantorDetailList) {
        List<ExistingGuarantorDetailView> existingGuarantorDetailViews = new ArrayList<ExistingGuarantorDetailView>();
        ExistingGuarantorDetailView existingGuarantorDetailView;

        for (ExistingGuarantorDetail existingGuarantorDetail : existingGuarantorDetailList) {
            existingGuarantorDetailView = new ExistingGuarantorDetailView();
            existingGuarantorDetailView.setCreateDate(existingGuarantorDetail.getCreateDate());
            existingGuarantorDetailView.setCreateBy(existingGuarantorDetail.getCreateBy());
            existingGuarantorDetailView.setModifyDate(existingGuarantorDetail.getModifyDate());
            existingGuarantorDetailView.setModifyBy(existingGuarantorDetail.getModifyBy());
            existingGuarantorDetailView.setGuarantorName(existingGuarantorDetail.getGuarantorName());
            existingGuarantorDetailView.setTcgLgNo(existingGuarantorDetail.getTcglgNo());
            existingGuarantorDetailView.setTotalLimitGuaranteeAmount(existingGuarantorDetail.getTotalLimitGuaranteeAmount());
            existingGuarantorDetailViews.add(existingGuarantorDetailView);
        }

        return existingGuarantorDetailViews;
    }
}
