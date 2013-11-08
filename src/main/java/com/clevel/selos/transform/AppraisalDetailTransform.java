package com.clevel.selos.transform;

import com.clevel.selos.model.db.working.Appraisal;
import com.clevel.selos.model.db.working.AppraisalDetail;
import com.clevel.selos.model.view.AppraisalDetailView;
import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

public class AppraisalDetailTransform extends Transform {
    public List<AppraisalDetail> transformToModel(List<AppraisalDetailView> appraisalDetailViewList,Appraisal appraisal){

    List<AppraisalDetail> appraisalDetailList = new ArrayList<AppraisalDetail>();
    for(AppraisalDetailView appraisalDetailView : appraisalDetailViewList){


        AppraisalDetail appraisalDetail = new AppraisalDetail();

        appraisalDetail.setAppraisal(appraisal);
        appraisalDetail.setNo(appraisalDetailView.getNo());
        appraisalDetail.setTitleDeed(appraisalDetailView.getTitleDeed());
        appraisalDetail.setPurposeReviewAppraisal(appraisalDetailView.getPurposeReviewAppraisal());
        appraisalDetail.setPurposeNewAppraisal(appraisalDetailView.getPurposeNewAppraisal());
        appraisalDetail.setPurposeReviewBuilding(appraisalDetailView.getPurposeReviewBuilding());
        appraisalDetail.setCharacteristic(appraisalDetailView.getCharacteristic());
        appraisalDetail.setNumberOfDocuments(appraisalDetailView.getNumberOfDocuments());
        appraisalDetail.setCreateBy(appraisalDetailView.getCreateBy());
        appraisalDetail.setCreateDate(DateTime.now().toDate());
        appraisalDetail.setModifyBy(appraisalDetailView.getModifyBy());
        appraisalDetail.setModifyDate(DateTime.now().toDate());

        appraisalDetailList.add(appraisalDetail);
    }
    return appraisalDetailList;
}

    public List<AppraisalDetailView> transformToView(List<AppraisalDetail> appraisalDetailList){
        List<AppraisalDetailView> appraisalDetailViewList = new ArrayList<AppraisalDetailView>();
        
        for(AppraisalDetail appraisalDetail : appraisalDetailList){

            AppraisalDetailView appraisalDetailView = new AppraisalDetailView();

            appraisalDetailView.setNo(appraisalDetail.getNo());
            appraisalDetailView.setTitleDeed(appraisalDetail.getTitleDeed());
            appraisalDetailView.setPurposeReviewAppraisal(appraisalDetail.getPurposeReviewAppraisal());
            appraisalDetailView.setPurposeNewAppraisal(appraisalDetail.getPurposeNewAppraisal());
            appraisalDetailView.setPurposeReviewBuilding(appraisalDetail.getPurposeReviewBuilding());
            appraisalDetailView.setCharacteristic(appraisalDetail.getCharacteristic());
            appraisalDetailView.setNumberOfDocuments(appraisalDetail.getNumberOfDocuments());
            appraisalDetailView.setCreateBy(appraisalDetail.getCreateBy());
            appraisalDetailView.setCreateDate(appraisalDetail.getCreateDate());
            appraisalDetailView.setModifyBy(appraisalDetail.getModifyBy());
            appraisalDetailView.setModifyDate(appraisalDetail.getModifyDate());

            appraisalDetailViewList.add(appraisalDetailView);
        }
        return appraisalDetailViewList;

    }
}
