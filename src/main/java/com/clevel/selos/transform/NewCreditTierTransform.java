package com.clevel.selos.transform;

import com.clevel.selos.dao.master.BaseRateDAO;
import com.clevel.selos.dao.working.NewCreditTierDetailDAO;
import com.clevel.selos.integration.brms.model.response.PricingIntTier;
import com.clevel.selos.integration.brms.model.response.PricingInterest;
import com.clevel.selos.model.db.master.BaseRate;
import com.clevel.selos.model.db.master.User;
import com.clevel.selos.model.db.working.NewCreditDetail;
import com.clevel.selos.model.db.working.NewCreditTierDetail;
import com.clevel.selos.model.view.NewCreditTierDetailView;
import com.clevel.selos.util.Util;
import org.joda.time.DateTime;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class NewCreditTierTransform extends Transform {

    @Inject
    BaseRateDAO baseRateDAO;
    @Inject
    NewCreditTierDetailDAO newCreditTierDetailDAO;

    @Inject
    public NewCreditTierTransform() {
    }

    public List<NewCreditTierDetail> transformToModel(List<NewCreditTierDetailView> newCreditTierDetailViewList, NewCreditDetail newCreditDetail, User user) {

        List<NewCreditTierDetail> newCreditTierDetailList = new ArrayList<NewCreditTierDetail>();
        NewCreditTierDetail newCreditTierDetail;

        for (NewCreditTierDetailView newCreditTierDetailView : newCreditTierDetailViewList) {
            newCreditTierDetail = new NewCreditTierDetail();

            if (newCreditTierDetailView.getId() != 0) {
                //newCreditTierDetail.setId(newCreditTierDetailView.getId());
                newCreditTierDetail = newCreditTierDetailDAO.findById(newCreditTierDetailView.getId());
                newCreditTierDetail.setModifyDate(DateTime.now().toDate());
                newCreditTierDetail.setModifyBy(user);
            } else { // id = 0 create new
                newCreditTierDetail.setCreateDate(DateTime.now().toDate());
                newCreditTierDetail.setCreateBy(user);
            }

            newCreditTierDetail.setNo(newCreditTierDetailView.getNo());
            if(newCreditTierDetailView.getFinalBasePrice() != null && newCreditTierDetailView.getFinalBasePrice().getId() != 0){
                newCreditTierDetail.setFinalBasePrice(baseRateDAO.findById(newCreditTierDetailView.getFinalBasePrice().getId()));
            }else{
                newCreditTierDetail.setFinalBasePrice(null);
            }
            newCreditTierDetail.setFinalInterest(newCreditTierDetailView.getFinalInterest());

            if(newCreditTierDetailView.getSuggestBasePrice() != null && newCreditTierDetailView.getSuggestBasePrice().getId() != 0){
                newCreditTierDetail.setSuggestBasePrice(baseRateDAO.findById(newCreditTierDetailView.getSuggestBasePrice().getId()));
            }else{
                newCreditTierDetail.setSuggestBasePrice(null);
            }
            newCreditTierDetail.setSuggestInterest(newCreditTierDetailView.getSuggestInterest());

            if(newCreditTierDetailView.getStandardBasePrice() != null && newCreditTierDetailView.getStandardBasePrice().getId() != 0){
                newCreditTierDetail.setStandardBasePrice(baseRateDAO.findById(newCreditTierDetailView.getStandardBasePrice().getId()));
            }else{
                newCreditTierDetail.setStandardBasePrice(null);
            }
            newCreditTierDetail.setStandardInterest(newCreditTierDetailView.getStandardInterest());

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

            newFeeDetailView.setId(newCreditTierDetail.getId());
            newFeeDetailView.setCreateDate(newCreditTierDetail.getCreateDate());
            newFeeDetailView.setCreateBy(newCreditTierDetail.getCreateBy());
            newFeeDetailView.setModifyDate(newCreditTierDetail.getModifyDate());
            newFeeDetailView.setModifyBy(newCreditTierDetail.getModifyBy());
            newFeeDetailView.setNo(newCreditTierDetail.getNo());
            if(newCreditTierDetail.getFinalBasePrice() != null){
                newFeeDetailView.setFinalBasePrice(newCreditTierDetail.getFinalBasePrice());
            }else{
                newFeeDetailView.setFinalBasePrice(new BaseRate());
            }
            newFeeDetailView.setFinalInterest(newCreditTierDetail.getFinalInterest());
            newFeeDetailView.setFinalPriceLabel(toGetPricing(newCreditTierDetail.getFinalBasePrice(),newCreditTierDetail.getFinalInterest()));
            if(newCreditTierDetail.getSuggestBasePrice() != null){
                newFeeDetailView.setSuggestBasePrice(newCreditTierDetail.getSuggestBasePrice());
            }else{
                newFeeDetailView.setSuggestBasePrice(new BaseRate());
            }
            newFeeDetailView.setSuggestInterest(newCreditTierDetail.getSuggestInterest());
            newFeeDetailView.setSuggestPriceLabel(toGetPricing(newCreditTierDetail.getSuggestBasePrice(),newCreditTierDetail.getSuggestInterest()));
            if(newCreditTierDetail.getStandardBasePrice() != null){
                newFeeDetailView.setStandardBasePrice(newCreditTierDetail.getStandardBasePrice());
            }else{
                newFeeDetailView.setStandardBasePrice(new BaseRate());
            }
            newFeeDetailView.setStandardInterest(newCreditTierDetail.getStandardInterest());
            newFeeDetailView.setStandardPriceLabel(toGetPricing(newCreditTierDetail.getStandardBasePrice(),newCreditTierDetail.getStandardInterest()));
            newFeeDetailView.setInstallment(newCreditTierDetail.getInstallment());
            newFeeDetailView.setTenor(newCreditTierDetail.getTenor());
            newCreditTierDetailViewList.add(newFeeDetailView);
        }

        return newCreditTierDetailViewList;
    }

    public List<NewCreditTierDetailView> transformPricingIntTierToView(List<PricingInterest> pricingInterestList) {

        List<NewCreditTierDetailView> newCreditTierDetailViewList = new ArrayList<NewCreditTierDetailView>();
        NewCreditTierDetailView newFeeDetailView;
        for(PricingInterest pricingInterest : pricingInterestList){
            if(!Util.isNull(pricingInterest.getCreditDetailId())){
                for (PricingIntTier newPricingIntTier : pricingInterest.getPricingIntTierList()) {
                    newFeeDetailView = new NewCreditTierDetailView();
                    newFeeDetailView.setStandardInterest(newPricingIntTier.getMaxRateVariance());
        //            newFeeDetailView.setStandardBasePrice();
                    newFeeDetailView.setStandardPriceLabel(newPricingIntTier.getRateType());
                    log.info("newPricingIntTier.getRateType() ::: {}",newPricingIntTier.getRateType());
                    newCreditTierDetailViewList.add(newFeeDetailView);
                }
            }
        }

        return newCreditTierDetailViewList;
    }


    public String toGetPricing(BaseRate baseRate ,BigDecimal price){
        String priceToShow = "";

        if(price != null && baseRate != null){
            if (price.doubleValue() < 0) {
                priceToShow = baseRate.getName() + " " + price;
            } else {
                priceToShow = baseRate.getName() + " + " + price;
            }
        }

        return priceToShow;
    }
}
