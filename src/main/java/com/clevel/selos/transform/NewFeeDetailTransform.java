package com.clevel.selos.transform;

import com.clevel.selos.dao.working.NewFeeCreditDAO;
import com.clevel.selos.model.ProposeType;
import com.clevel.selos.model.db.master.User;
import com.clevel.selos.model.db.working.NewCreditFacility;
import com.clevel.selos.model.db.working.NewFeeDetail;
import com.clevel.selos.model.view.NewFeeDetailView;
import org.joda.time.DateTime;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class NewFeeDetailTransform extends Transform {
    @Inject
    NewFeeCreditDAO newFeeCreditDAO;

    @Inject
    public NewFeeDetailTransform() {

    }

    public List<NewFeeDetail> transformToModel(List<NewFeeDetailView> newFeeDetailViewList, NewCreditFacility creditFacilityPropose, User user) {

        List<NewFeeDetail> newFeeDetailList = new ArrayList<NewFeeDetail>();
        NewFeeDetail newFeeDetail;

        for (NewFeeDetailView newFeeDetailView : newFeeDetailViewList) {
            newFeeDetail = new NewFeeDetail();
            if (newFeeDetail.getId() != 0) {
                newFeeDetail = newFeeCreditDAO.findById(newFeeDetailView.getId());
                newFeeDetail.setModifyDate(DateTime.now().toDate());
                newFeeDetail.setModifyBy(user);
            } else { // id = 0 create new
                newFeeDetail.setCreateDate(new Date());
                newFeeDetail.setCreateBy(user);
            }
            newFeeDetail.setProductProgram(newFeeDetailView.getProductProgram());
            newFeeDetail.setStandardFrontEndFee(newFeeDetailView.getStandardFrontEndFee());
            newFeeDetail.setCommitmentFee(newFeeDetailView.getCommitmentFee());
            newFeeDetail.setExtensionFee(newFeeDetailView.getExtensionFee());
            newFeeDetail.setPrepaymentFee(newFeeDetailView.getPrepaymentFee());
            newFeeDetail.setCancellationFee(newFeeDetailView.getCancellationFee());
            newFeeDetail.setNewCreditFacility(creditFacilityPropose);
            newFeeDetailList.add(newFeeDetail);
        }

        return newFeeDetailList;
    }

    public List<NewFeeDetailView> transformToView(List<NewFeeDetail> newFeeDetailList) {

        List<NewFeeDetailView> newFeeDetailViewList = new ArrayList<NewFeeDetailView>();
        NewFeeDetailView newFeeDetailView;

        for (NewFeeDetail newFeeDetail : newFeeDetailList) {
            newFeeDetailView = new NewFeeDetailView();
            newFeeDetailView.setCreateBy(newFeeDetail.getCreateBy());
            newFeeDetailView.setCreateDate(newFeeDetail.getCreateDate());
            newFeeDetailView.setModifyBy(newFeeDetail.getModifyBy());
            newFeeDetailView.setModifyDate(newFeeDetail.getModifyDate());
            newFeeDetailView.setProductProgram(newFeeDetail.getProductProgram());
            newFeeDetailView.setStandardFrontEndFee(newFeeDetail.getStandardFrontEndFee());
            newFeeDetailView.setCommitmentFee(newFeeDetail.getCommitmentFee());
            newFeeDetailView.setExtensionFee(newFeeDetail.getExtensionFee());
            newFeeDetailView.setPrepaymentFee(newFeeDetail.getPrepaymentFee());
            newFeeDetailView.setCancellationFee(newFeeDetail.getCancellationFee());
            newFeeDetailViewList.add(newFeeDetailView);
        }

        return newFeeDetailViewList;
    }
}
