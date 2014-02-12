package com.clevel.selos.transform;

import com.clevel.selos.model.db.master.BaseRate;
import com.clevel.selos.model.db.master.User;
import com.clevel.selos.model.db.working.NewCreditDetail;
import com.clevel.selos.model.db.working.NewCreditTierDetail;
import com.clevel.selos.model.view.NewCreditTierDetailView;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class NewCreditTierTransform extends Transform {

    @Inject
    public NewCreditTierTransform() {
    }

    public List<NewCreditTierDetail> transformToModel(List<NewCreditTierDetailView> newCreditTierDetailViewList, NewCreditDetail newCreditDetail, User user) {

        List<NewCreditTierDetail> newCreditTierDetailList = new ArrayList<NewCreditTierDetail>();
        NewCreditTierDetail newCreditTierDetail;

        for (NewCreditTierDetailView newCreditTierDetailView : newCreditTierDetailViewList) {
            newCreditTierDetail = new NewCreditTierDetail();

            if (newCreditTierDetail.getId() != 0) {
                newCreditTierDetail.setCreateDate(newCreditTierDetailView.getCreateDate());
                newCreditTierDetail.setCreateBy(newCreditTierDetailView.getCreateBy());
            } else { // id = 0 create new
                newCreditTierDetail.setCreateDate(new Date());
                newCreditTierDetail.setCreateBy(user);
            }

            newCreditTierDetail.setNo(newCreditTierDetailView.getNo());
            newCreditTierDetail.setFinalBasePrice(newCreditTierDetailView.getFinalBasePrice());
            newCreditTierDetail.setFinalInterest(newCreditTierDetailView.getFinalInterest());
            newCreditTierDetail.setSuggestBasePrice(newCreditTierDetailView.getSuggestBasePrice());
            newCreditTierDetail.setSuggestInterest(newCreditTierDetailView.getSuggestInterest());
            newCreditTierDetail.setInstallment(newCreditTierDetailView.getInstallment());
            newCreditTierDetail.setTenor(newCreditTierDetailView.getTenor());
            newCreditTierDetail.setNewCreditDetail(newCreditDetail);
            newCreditTierDetailList.add(newCreditTierDetail);
        }

        return newCreditTierDetailList;
    }

    public List<NewCreditTierDetailView> transformToView(List<NewCreditTierDetail> newCreditTierDetailList) {

        List<NewCreditTierDetailView> newCreditTierDetailViewList = new ArrayList<NewCreditTierDetailView>();
        NewCreditTierDetailView newFeeDetailView;

        for (NewCreditTierDetail newCreditTierDetail : newCreditTierDetailList) {
            newFeeDetailView = new NewCreditTierDetailView();
            newFeeDetailView.setCreateDate(newCreditTierDetail.getCreateDate());
            newFeeDetailView.setCreateBy(newCreditTierDetail.getCreateBy());
            newFeeDetailView.setModifyDate(newCreditTierDetail.getModifyDate());
            newFeeDetailView.setModifyBy(newCreditTierDetail.getModifyBy());
            newFeeDetailView.setNo(newCreditTierDetail.getNo());
            newFeeDetailView.setFinalBasePrice(newCreditTierDetail.getFinalBasePrice());
            newFeeDetailView.setFinalInterest(newCreditTierDetail.getFinalInterest());
            newFeeDetailView.setFinalPriceRate(toGetPricing(newCreditTierDetail.getFinalBasePrice(),newCreditTierDetail.getFinalInterest()));
            newFeeDetailView.setSuggestBasePrice(newCreditTierDetail.getSuggestBasePrice());
            newFeeDetailView.setSuggestInterest(newCreditTierDetail.getSuggestInterest());
            newFeeDetailView.setSuggestPrice(toGetPricing(newCreditTierDetail.getSuggestBasePrice(),newCreditTierDetail.getSuggestInterest()));
            newFeeDetailView.setInstallment(newCreditTierDetail.getInstallment());
            newFeeDetailView.setTenor(newCreditTierDetail.getTenor());
            newCreditTierDetailViewList.add(newFeeDetailView);
        }

        return newCreditTierDetailViewList;
    }

    public String toGetPricing(BaseRate baseRate ,BigDecimal price){
        String priceToShow = "";

        if (price.doubleValue() < 0)
        {
            priceToShow = baseRate.getName() + " " + price;
        }else{
            priceToShow = baseRate.getName() + " + " + price;
        }

        return priceToShow;
    }
}
