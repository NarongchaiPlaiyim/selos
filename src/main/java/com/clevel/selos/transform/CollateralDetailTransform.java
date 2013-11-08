package com.clevel.selos.transform;

import com.clevel.selos.model.db.master.AccountType;
import com.clevel.selos.model.db.working.Appraisal;
import com.clevel.selos.model.db.working.CollateralDetail;
import com.clevel.selos.model.view.AccountTypeView;
import com.clevel.selos.model.view.CollateralDetailView;
import org.joda.time.DateTime;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class CollateralDetailTransform extends Transform {

    @Inject
    public CollateralDetailTransform() {
    }

    public List<CollateralDetail> transformToModel(List<CollateralDetailView> collateralDetailViewList,Appraisal appraisal) {

        List<CollateralDetail> collateralDetailList = new ArrayList<CollateralDetail>();
        CollateralDetail collateralDetail;

        for (CollateralDetailView collateralDetailView: collateralDetailViewList) {
            collateralDetail = new CollateralDetail();
            if(collateralDetailView.getId()==0){
                collateralDetail.setCreateBy(collateralDetailView.getCreateBy());
                collateralDetail.setCreateDate(DateTime.now().toDate());
            }
            collateralDetail.setJobID(collateralDetailView.getJobID());
            collateralDetail.setNo(collateralDetailView.getNo());
            collateralDetail.setAppraisalDate(collateralDetailView.getAppraisalDate());
            collateralDetail.setAADDecision(collateralDetailView.getAADDecision());
            collateralDetail.setAADDecisionReason(collateralDetailView.getAADDecisionReason());
            collateralDetail.setAADDecisionReasonDetail(collateralDetailView.getAADDecisionReasonDetail());
            collateralDetail.setMortgageCondition(collateralDetailView.getMortgageCondition());
            collateralDetail.setMortgageConditionDetail(collateralDetailView.getMortgageConditionDetail());
            collateralDetail.setModifyBy(collateralDetailView.getModifyBy());
            collateralDetail.setModifyDate(collateralDetailView.getModifyDate());
            collateralDetail.setAppraisal(appraisal);

            collateralDetailList.add(collateralDetail);
        }

        return collateralDetailList;
    }

    public List<CollateralDetailView> transformToView(List<CollateralDetail> collateralDetailList) {

        List<CollateralDetailView> collateralDetailViewList = new ArrayList<CollateralDetailView>();
        CollateralDetailView collateralDetailView;

        for (CollateralDetail collateralDetail: collateralDetailList) {
            collateralDetailView = new CollateralDetailView();
            collateralDetailView.setId(collateralDetail.getId());
            collateralDetailView.setJobID(collateralDetail.getJobID());
            collateralDetailView.setNo(collateralDetail.getNo());
            collateralDetailView.setAppraisalDate(collateralDetail.getAppraisalDate());
            collateralDetailView.setAADDecision(collateralDetail.getAADDecision());
            collateralDetailView.setAADDecisionReason(collateralDetail.getAADDecisionReason());
            collateralDetailView.setAADDecisionReasonDetail(collateralDetail.getAADDecisionReasonDetail());
            collateralDetailView.setMortgageCondition(collateralDetail.getMortgageCondition());
            collateralDetailView.setMortgageConditionDetail(collateralDetail.getMortgageConditionDetail());
            collateralDetailView.setCreateBy(collateralDetail.getCreateBy());
            collateralDetailView.setCreateDate(collateralDetail.getCreateDate());
            collateralDetailView.setModifyBy(collateralDetail.getModifyBy());
            collateralDetailView.setModifyDate(collateralDetail.getModifyDate());

            collateralDetailViewList.add(collateralDetailView);
        }

        return collateralDetailViewList;
    }
}
