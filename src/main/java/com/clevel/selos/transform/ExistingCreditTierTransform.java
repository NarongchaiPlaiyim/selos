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
    BaseRateTransform baseRateTransform;

    @Inject
    public ExistingCreditTierTransform() {
    }

    public List<ExistingCreditTierDetail> transformsToModel(List<ExistingCreditTierDetailView> existingCreditTierDetailViewList,ExistingCreditDetail existingCreditDetail,User user){

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
            if(existingCreditTierDetailView.getFinalBasePrice()!=null && existingCreditTierDetailView.getFinalBasePrice().getId()!=0){
                existingCreditTierDetail.setFinalBasePrice(baseRateTransform.transformToModel(existingCreditTierDetailView.getFinalBasePrice()));
            }else{
                existingCreditTierDetail.setFinalBasePrice(null);
            }
            existingCreditTierDetail.setFinalInterest(existingCreditTierDetailView.getFinalInterest());
            existingCreditTierDetail.setFinalPriceRate(existingCreditTierDetailView.getFinalPriceRate());

            existingCreditTierDetail.setInstallment(existingCreditTierDetailView.getInstallment());

            if(existingCreditTierDetailView.getStandardBasePrice()!=null && existingCreditTierDetailView.getStandardBasePrice().getId()!=0){
                existingCreditTierDetail.setStandardBasePrice(baseRateTransform.transformToModel(existingCreditTierDetailView.getStandardBasePrice()));
            }else{
                existingCreditTierDetail.setStandardBasePrice(null);
            }
            existingCreditTierDetail.setStandardInterest(existingCreditTierDetailView.getStandardInterest());
            existingCreditTierDetail.setStandardPrice(existingCreditTierDetailView.getStandardPrice());

            if(existingCreditTierDetailView.getSuggestBasePrice()!=null && existingCreditTierDetailView.getSuggestBasePrice().getId()!=0){
                existingCreditTierDetail.setSuggestBasePrice(baseRateTransform.transformToModel(existingCreditTierDetailView.getSuggestBasePrice()));
            }else{
                existingCreditTierDetail.setSuggestBasePrice(null);
            }

            existingCreditTierDetail.setSuggestInterest(existingCreditTierDetailView.getSuggestInterest());
            existingCreditTierDetail.setSuggestPrice(existingCreditTierDetailView.getSuggestPrice());

            existingCreditTierDetail.setTenor(existingCreditTierDetailView.getTenor());
            existingCreditTierDetail.setExistingCreditDetail(existingCreditDetail);
            existingCreditTierDetail.setModifyDate(new Date());
            existingCreditTierDetail.setModifyBy(user);
            existingCreditTierDetailList.add(existingCreditTierDetail);
        }

        return existingCreditTierDetailList;
    }

    public List<ExistingCreditTierDetailView> transformsToView(List<ExistingCreditTierDetail> existingCreditTierDetailList) {

        List<ExistingCreditTierDetailView> existingCreditTierDetailViewList = new ArrayList<ExistingCreditTierDetailView>();
        ExistingCreditTierDetailView existingCreditTierDetailView;

        for(ExistingCreditTierDetail existingCreditTierDetail : existingCreditTierDetailList)
        {
            existingCreditTierDetailView = new ExistingCreditTierDetailView();
            existingCreditTierDetailView.setNo(existingCreditTierDetail.getNo());
            existingCreditTierDetailView.setCreateDate(existingCreditTierDetail.getCreateDate());
            existingCreditTierDetailView.setCreateBy(existingCreditTierDetail.getCreateBy());
            existingCreditTierDetailView.setModifyDate(existingCreditTierDetail.getModifyDate());
            existingCreditTierDetailView.setModifyBy(existingCreditTierDetail.getModifyBy());
            existingCreditTierDetailView.setNo(existingCreditTierDetail.getNo());
            existingCreditTierDetailView.setFinalPriceRate(existingCreditTierDetail.getFinalPriceRate());
            existingCreditTierDetailView.setSuggestBasePrice(baseRateTransform.transformToView(existingCreditTierDetail.getSuggestBasePrice()));
//            existingCreditTierDetailView.setEditFlag(existingCreditTierDetail.isCanEdit());
            existingCreditTierDetailView.setFinalBasePrice(baseRateTransform.transformToView(existingCreditTierDetail.getFinalBasePrice()));
            existingCreditTierDetailView.setFinalInterest(existingCreditTierDetail.getFinalInterest());
            existingCreditTierDetailView.setFinalPriceRate(existingCreditTierDetail.getFinalPriceRate());
            existingCreditTierDetailView.setInstallment(existingCreditTierDetail.getInstallment());
            existingCreditTierDetailView.setStandardBasePrice(baseRateTransform.transformToView(existingCreditTierDetail.getStandardBasePrice()));
            existingCreditTierDetailView.setStandardInterest(existingCreditTierDetail.getStandardInterest());
            existingCreditTierDetailView.setStandardPrice(existingCreditTierDetail.getStandardPrice());
            existingCreditTierDetailView.setSuggestBasePrice(baseRateTransform.transformToView(existingCreditTierDetail.getSuggestBasePrice()));
            existingCreditTierDetailView.setSuggestInterest(existingCreditTierDetail.getSuggestInterest());
            existingCreditTierDetailView.setSuggestPrice(existingCreditTierDetail.getSuggestPrice());
            existingCreditTierDetailView.setTenor(existingCreditTierDetail.getTenor());
            existingCreditTierDetailViewList.add(existingCreditTierDetailView);
    }

        return existingCreditTierDetailViewList;
    }
}
