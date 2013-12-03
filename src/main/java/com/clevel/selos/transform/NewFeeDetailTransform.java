package com.clevel.selos.transform;

import com.clevel.selos.model.db.working.NewCreditFacility;
import com.clevel.selos.model.db.working.NewFeeDetail;
import com.clevel.selos.model.view.NewFeeDetailView;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class NewFeeDetailTransform extends Transform {

    @Inject
    public NewFeeDetailTransform() {
    }

    public List<NewFeeDetail> transformToModel(List<NewFeeDetailView> newFeeDetailViewList, NewCreditFacility creditFacilityPropose) {

        List<NewFeeDetail> newFeeDetailList = new ArrayList<NewFeeDetail>();
        NewFeeDetail newFeeDetail;

        for (NewFeeDetailView newFeeDetailView : newFeeDetailViewList){
            newFeeDetail = new NewFeeDetail();

            if (newFeeDetailView.getId() != 0) {
                newFeeDetail.setId(newFeeDetailView.getId());
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
