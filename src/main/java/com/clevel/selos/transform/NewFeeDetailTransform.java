package com.clevel.selos.transform;

import com.clevel.selos.model.db.working.CollateralHeaderDetail;
import com.clevel.selos.model.db.working.CreditFacilityPropose;
import com.clevel.selos.model.db.working.ProposeFeeDetail;
import com.clevel.selos.model.db.working.SubCollateralDetail;
import com.clevel.selos.model.view.NewFeeDetailView;
import com.clevel.selos.model.view.NewSubCollateralDetailView;
import org.joda.time.DateTime;

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

        for (NewFeeDetailView newFeeDetailView : newFeeDetailViewList) {
            proposeFeeDetail = new ProposeFeeDetail();

            if(newFeeDetailView.getId()==0){
                proposeFeeDetail.setCreateBy(newFeeDetailView.getCreateBy());
                proposeFeeDetail.setCreateDate(DateTime.now().toDate());
            }

            proposeFeeDetail.setProductProgram(newFeeDetailView.getProductProgram());
            proposeFeeDetail.setStandardFrontEndFee(newFeeDetailView.getStandardFrontEndFee());
            proposeFeeDetail.setCommitmentFee(newFeeDetailView.getCommitmentFee());
            proposeFeeDetail.setExtensionFee(newFeeDetailView.getExtensionFee());
            proposeFeeDetail.setPrepaymentFee(newFeeDetailView.getPrepaymentFee());
            proposeFeeDetail.setCancellationFee(newFeeDetailView.getCancellationFee());
            proposeFeeDetailList.add(proposeFeeDetail);
        }

        return proposeFeeDetailList;
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
