package com.clevel.selos.transform;

import com.clevel.selos.model.db.working.CreditFacilityPropose;
import com.clevel.selos.model.db.working.ProposeFeeDetail;
import com.clevel.selos.model.view.NewFeeDetailView;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class NewFeeDetailTransform extends Transform {

    @Inject
    public NewFeeDetailTransform() {
    }

    public List<ProposeFeeDetail> transformToModel(List<NewFeeDetailView> newFeeDetailViewList, CreditFacilityPropose creditFacilityPropose) {

        List<ProposeFeeDetail> proposeFeeDetailList = new ArrayList<ProposeFeeDetail>();
        ProposeFeeDetail proposeFeeDetail;

        for (NewFeeDetailView newFeeDetailView : newFeeDetailViewList){
            proposeFeeDetail = new ProposeFeeDetail();

            if (newFeeDetailView.getId() != 0) {
                proposeFeeDetail.setId(newFeeDetailView.getId());
            }
            proposeFeeDetail.setProductProgram(newFeeDetailView.getProductProgram());
            proposeFeeDetail.setStandardFrontEndFee(newFeeDetailView.getStandardFrontEndFee());
            proposeFeeDetail.setCommitmentFee(newFeeDetailView.getCommitmentFee());
            proposeFeeDetail.setExtensionFee(newFeeDetailView.getExtensionFee());
            proposeFeeDetail.setPrepaymentFee(newFeeDetailView.getPrepaymentFee());
            proposeFeeDetail.setCancellationFee(newFeeDetailView.getCancellationFee());
            proposeFeeDetail.setCreditFacilityPropose(creditFacilityPropose);
            proposeFeeDetailList.add(proposeFeeDetail);
        }

        return proposeFeeDetailList;
    }

    public List<NewFeeDetailView> transformToView(List<ProposeFeeDetail> proposeFeeDetailList) {

        List<NewFeeDetailView> newFeeDetailViewList = new ArrayList<NewFeeDetailView>();
        NewFeeDetailView newFeeDetailView;

        for (ProposeFeeDetail proposeFeeDetail : proposeFeeDetailList) {
            newFeeDetailView = new NewFeeDetailView();

            newFeeDetailView.setProductProgram(proposeFeeDetail.getProductProgram());
            newFeeDetailView.setStandardFrontEndFee(proposeFeeDetail.getStandardFrontEndFee());
            newFeeDetailView.setCommitmentFee(proposeFeeDetail.getCommitmentFee());
            newFeeDetailView.setExtensionFee(proposeFeeDetail.getExtensionFee());
            newFeeDetailView.setPrepaymentFee(proposeFeeDetail.getPrepaymentFee());
            newFeeDetailView.setCancellationFee(proposeFeeDetail.getCancellationFee());
            newFeeDetailViewList.add(newFeeDetailView);
        }

        return newFeeDetailViewList;
    }
}
