package com.clevel.selos.transform;

import com.clevel.selos.model.db.master.User;
import com.clevel.selos.model.db.working.ExistingCreditDetail;
import com.clevel.selos.model.db.working.ExistingCreditTierDetail;
import com.clevel.selos.model.view.ExistingCreditTierDetailView;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ExistingCreditTierTransform extends Transform {

    @Inject
    public ExistingCreditTierTransform() {
    }

    public List<ExistingCreditTierDetail> transformToModel(List<ExistingCreditTierDetailView> existingCreditTierDetailViewList,ExistingCreditDetail existingCreditDetail,User user){

        List<ExistingCreditTierDetail> existingCreditTierDetailList = new ArrayList<ExistingCreditTierDetail>();
        ExistingCreditTierDetail existingCreditTierDetail;

        for (ExistingCreditTierDetailView existingCreditTierDetailView : existingCreditTierDetailViewList){
            existingCreditTierDetail = new ExistingCreditTierDetail();

            if (existingCreditTierDetailView.getId() != 0) {
                existingCreditTierDetail.setId(existingCreditTierDetailView.getId());
                existingCreditTierDetail.setCreateDate(existingCreditTierDetailView.getCreateDate());
                existingCreditTierDetail.setCreateBy(existingCreditTierDetailView.getCreateBy());
            } else { // id = 0 create existing
                existingCreditTierDetail.setCreateDate(new Date());
                existingCreditTierDetail.setCreateBy(user);
            }

            existingCreditTierDetail.setNo(existingCreditTierDetailView.getNo());
            if(existingCreditTierDetailView.getFinalBasePrice().getId()!=0){
                existingCreditTierDetail.setFinalBasePrice(existingCreditTierDetailView.getFinalBasePrice());
            }else{
                existingCreditTierDetail.setFinalBasePrice(null);
            }
            existingCreditTierDetail.setFinalInterest(existingCreditTierDetailView.getStandardInterest());
            existingCreditTierDetail.setFinalPriceRate(existingCreditTierDetailView.getFinalPriceRate());

            existingCreditTierDetail.setInstallment(existingCreditTierDetailView.getInstallment());

            if(existingCreditTierDetailView.getStandardBasePrice().getId()!=0){
                existingCreditTierDetail.setStandardBasePrice(existingCreditTierDetailView.getStandardBasePrice());
            }else{
                existingCreditTierDetail.setStandardBasePrice(null);
            }
            existingCreditTierDetail.setStandardInterest(existingCreditTierDetailView.getStandardInterest());
            existingCreditTierDetail.setStandardPrice(existingCreditTierDetailView.getStandardPrice());

            if(existingCreditTierDetailView.getSuggestBasePrice().getId()!=0){
                existingCreditTierDetail.setSuggestBasePrice(existingCreditTierDetailView.getSuggestBasePrice());
            }else{
                existingCreditTierDetail.setSuggestBasePrice(null);
            }

            existingCreditTierDetail.setSuggestInterest(existingCreditTierDetailView.getSuggestInterest());
            existingCreditTierDetail.setSuggestPrice(existingCreditTierDetailView.getSuggestPrice());

            existingCreditTierDetail.setTenor(existingCreditTierDetailView.getTenor());
            existingCreditTierDetail.setExistingCreditDetail(existingCreditDetail);
            existingCreditTierDetailList.add(existingCreditTierDetail);
        }

        return existingCreditTierDetailList;
    }

    public List<ExistingCreditTierDetailView> transformToView(List<ExistingCreditTierDetail> existingCreditTierDetailList) {

        List<ExistingCreditTierDetailView> existingCreditTierDetailViewList = new ArrayList<ExistingCreditTierDetailView>();
        ExistingCreditTierDetailView existingFeeDetailView;

        for(ExistingCreditTierDetail existingCreditTierDetail : existingCreditTierDetailList)
        {
            existingFeeDetailView = new ExistingCreditTierDetailView();
            existingFeeDetailView.setCreateDate(existingCreditTierDetail.getCreateDate());
            existingFeeDetailView.setCreateBy(existingCreditTierDetail.getCreateBy());
            existingFeeDetailView.setModifyDate(existingCreditTierDetail.getModifyDate());
            existingFeeDetailView.setModifyBy(existingCreditTierDetail.getModifyBy());
            existingFeeDetailView.setNo(existingCreditTierDetail.getNo());
            existingFeeDetailView.setFinalPriceRate(existingCreditTierDetail.getFinalPriceRate());
            existingFeeDetailView.setSuggestBasePrice(existingCreditTierDetail.getSuggestBasePrice());
//            existingFeeDetailView.setEditFlag(existingCreditTierDetail.isCanEdit());
            existingFeeDetailView.setFinalBasePrice(existingCreditTierDetail.getFinalBasePrice());
            existingFeeDetailView.setFinalInterest(existingCreditTierDetail.getStandardInterest());
            existingFeeDetailView.setFinalPriceRate(existingCreditTierDetail.getFinalPriceRate());
            existingFeeDetailView.setInstallment(existingCreditTierDetail.getInstallment());
            existingFeeDetailView.setStandardBasePrice(existingCreditTierDetail.getStandardBasePrice());
            existingFeeDetailView.setStandardInterest(existingCreditTierDetail.getStandardInterest());
            existingFeeDetailView.setStandardPrice(existingCreditTierDetail.getStandardPrice());
            existingFeeDetailView.setSuggestBasePrice(existingCreditTierDetail.getSuggestBasePrice());
            existingFeeDetailView.setSuggestInterest(existingCreditTierDetail.getSuggestInterest());
            existingFeeDetailView.setSuggestPrice(existingCreditTierDetail.getSuggestPrice());
            existingFeeDetailView.setTenor(existingCreditTierDetail.getTenor());
            existingCreditTierDetailViewList.add(existingFeeDetailView);
    }

        return existingCreditTierDetailViewList;
    }
}
