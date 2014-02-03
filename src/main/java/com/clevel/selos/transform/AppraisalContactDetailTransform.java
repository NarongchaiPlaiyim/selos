package com.clevel.selos.transform;

import com.clevel.selos.model.db.working.Appraisal;
import com.clevel.selos.model.db.working.AppraisalContactDetail;
import com.clevel.selos.model.view.AppraisalContactDetailView;
import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

public class AppraisalContactDetailTransform extends Transform {
    public List<AppraisalContactDetail> transformToModel(List<AppraisalContactDetailView> appraisalContactDetailViewList,Appraisal appraisal){

    List<AppraisalContactDetail> appraisalContactDetailList = new ArrayList<AppraisalContactDetail>();
    for(AppraisalContactDetailView appraisalContactDetailView : appraisalContactDetailViewList){


        AppraisalContactDetail appraisalContactDetail = new AppraisalContactDetail();

        appraisalContactDetail.setAppraisal(appraisal);
//        appraisalContactDetail.setCustomerName(appraisalContactDetailView.getCustomerName());
//        appraisalContactDetail.setContactNo(appraisalContactDetailView.getContactNo());

        appraisalContactDetail.setCreateBy(appraisalContactDetailView.getCreateBy());
        appraisalContactDetail.setCreateDate(DateTime.now().toDate());
        appraisalContactDetail.setModifyBy(appraisalContactDetailView.getCreateBy());
        appraisalContactDetail.setModifyDate(DateTime.now().toDate());

        appraisalContactDetailList.add(appraisalContactDetail);
    }
    return appraisalContactDetailList;
}

    public List<AppraisalContactDetailView> transformToView(List<AppraisalContactDetail> appraisalContactDetailList){
        List<AppraisalContactDetailView> appraisalContactDetailViewList = new ArrayList<AppraisalContactDetailView>();
        
        for(AppraisalContactDetail appraisalContactDetail : appraisalContactDetailList){

            AppraisalContactDetailView appraisalContactDetailView = new AppraisalContactDetailView();

//            appraisalContactDetailView.setCustomerName(appraisalContactDetail.getCustomerName());
//            appraisalContactDetailView.setContactNo(appraisalContactDetail.getContactNo());

            appraisalContactDetailView.setCreateBy(appraisalContactDetail.getCreateBy());
            appraisalContactDetailView.setCreateDate(appraisalContactDetail.getCreateDate());
            appraisalContactDetailView.setModifyBy(appraisalContactDetail.getModifyBy());
            appraisalContactDetailView.setModifyDate(appraisalContactDetail.getModifyDate());

            appraisalContactDetailViewList.add(appraisalContactDetailView);
        }
        return appraisalContactDetailViewList;

    }
}
