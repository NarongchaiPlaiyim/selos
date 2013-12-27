package com.clevel.selos.transform;

import com.clevel.selos.model.db.master.User;
import com.clevel.selos.model.db.working.NewCreditDetail;
import com.clevel.selos.model.db.working.NewCreditTierDetail;
import com.clevel.selos.model.view.NewCreditTierDetailView;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class NewCreditTierTransform extends Transform {

    @Inject
    public NewCreditTierTransform() {
    }

    public List<NewCreditTierDetail> transformToModel(List<NewCreditTierDetailView> newCreditTierDetailViewList,NewCreditDetail newCreditDetail,User user){

        List<NewCreditTierDetail> newCreditTierDetailList = new ArrayList<NewCreditTierDetail>();
        NewCreditTierDetail newCreditTierDetail;

        for (NewCreditTierDetailView newCreditTierDetailView : newCreditTierDetailViewList){
            newCreditTierDetail = new NewCreditTierDetail();

            if (newCreditTierDetailView.getId() != 0) {
                newCreditTierDetail.setId(newCreditTierDetailView.getId());
                newCreditTierDetail.setCreateDate(newCreditTierDetailView.getCreateDate());
                newCreditTierDetail.setCreateBy(newCreditTierDetailView.getCreateBy());
            } else { // id = 0 create new
                newCreditTierDetail.setCreateDate(new Date());
                newCreditTierDetail.setCreateBy(user);
            }

            newCreditTierDetail.setNo(newCreditTierDetailView.getNo());
            newCreditTierDetail.setFinalPriceRate(newCreditTierDetailView.getFinalPriceRate());
            newCreditTierDetail.setSuggestBasePrice(newCreditTierDetailView.getSuggestBasePrice());
            newCreditTierDetail.setFinalBasePrice(newCreditTierDetailView.getFinalBasePrice());
            newCreditTierDetail.setFinalInterest(newCreditTierDetailView.getStandardInterest());
            newCreditTierDetail.setFinalPriceRate(newCreditTierDetailView.getFinalPriceRate());
            newCreditTierDetail.setInstallment(newCreditTierDetailView.getInstallment());
            newCreditTierDetail.setStandardBasePrice(newCreditTierDetailView.getStandardBasePrice());
            newCreditTierDetail.setStandardInterest(newCreditTierDetailView.getStandardInterest());
            newCreditTierDetail.setStandardPrice(newCreditTierDetailView.getStandardPrice());
            newCreditTierDetail.setSuggestBasePrice(newCreditTierDetailView.getSuggestBasePrice());
            newCreditTierDetail.setSuggestInterest(newCreditTierDetailView.getSuggestInterest());
            newCreditTierDetail.setSuggestPrice(newCreditTierDetailView.getSuggestPrice());
            newCreditTierDetail.setTenor(newCreditTierDetailView.getTenor());
            newCreditTierDetail.setNewCreditDetail(newCreditDetail);
            newCreditTierDetailList.add(newCreditTierDetail);
        }

        return newCreditTierDetailList;
    }

    public List<NewCreditTierDetailView> transformToView(List<NewCreditTierDetail> newCreditTierDetailList) {

        List<NewCreditTierDetailView> newCreditTierDetailViewList = new ArrayList<NewCreditTierDetailView>();
        NewCreditTierDetailView newFeeDetailView;

        for(NewCreditTierDetail newCreditTierDetail : newCreditTierDetailList)
        {
            newFeeDetailView = new NewCreditTierDetailView();
            newFeeDetailView.setCreateDate(newCreditTierDetail.getCreateDate());
            newFeeDetailView.setCreateBy(newCreditTierDetail.getCreateBy());
            newFeeDetailView.setModifyDate(newCreditTierDetail.getModifyDate());
            newFeeDetailView.setModifyBy(newCreditTierDetail.getModifyBy());
            newFeeDetailView.setNo(newCreditTierDetail.getNo());
            newFeeDetailView.setFinalPriceRate(newCreditTierDetail.getFinalPriceRate());
            newFeeDetailView.setSuggestBasePrice(newCreditTierDetail.getSuggestBasePrice());
            newFeeDetailView.setFinalBasePrice(newCreditTierDetail.getFinalBasePrice());
            newFeeDetailView.setFinalInterest(newCreditTierDetail.getStandardInterest());
            newFeeDetailView.setFinalPriceRate(newCreditTierDetail.getFinalPriceRate());
            newFeeDetailView.setInstallment(newCreditTierDetail.getInstallment());
            newFeeDetailView.setStandardBasePrice(newCreditTierDetail.getStandardBasePrice());
            newFeeDetailView.setStandardInterest(newCreditTierDetail.getStandardInterest());
            newFeeDetailView.setStandardPrice(newCreditTierDetail.getStandardPrice());
            newFeeDetailView.setSuggestBasePrice(newCreditTierDetail.getSuggestBasePrice());
            newFeeDetailView.setSuggestInterest(newCreditTierDetail.getSuggestInterest());
            newFeeDetailView.setSuggestPrice(newCreditTierDetail.getSuggestPrice());
            newFeeDetailView.setTenor(newCreditTierDetail.getTenor());
            newCreditTierDetailViewList.add(newFeeDetailView);
    }

        return newCreditTierDetailViewList;
    }
}
