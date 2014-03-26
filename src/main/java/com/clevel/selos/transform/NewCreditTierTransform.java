package com.clevel.selos.transform;

import com.clevel.selos.dao.master.BaseRateDAO;
import com.clevel.selos.dao.working.NewCreditTierDetailDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.integration.brms.model.response.PricingIntTier;
import com.clevel.selos.model.db.master.BaseRate;
import com.clevel.selos.model.db.master.User;
import com.clevel.selos.model.db.working.NewCreditDetail;
import com.clevel.selos.model.db.working.NewCreditTierDetail;
import com.clevel.selos.model.view.BaseRateView;
import com.clevel.selos.model.view.NewCreditTierDetailView;
import org.joda.time.DateTime;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class NewCreditTierTransform extends Transform {
    @SELOS
    @Inject
    private Logger log;

    @Inject
    BaseRateDAO baseRateDAO;
    @Inject
    NewCreditTierDetailDAO newCreditTierDetailDAO;
    @Inject
    BaseRateTransform baseRateTransform;

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
            if (newCreditTierDetailView.getFinalBasePrice() != null && newCreditTierDetailView.getFinalBasePrice().getId() != 0) {
                newCreditTierDetail.setFinalBasePrice(baseRateDAO.findById(newCreditTierDetailView.getFinalBasePrice().getId()));
            } else {
                newCreditTierDetail.setFinalBasePrice(null);
            }
            newCreditTierDetail.setFinalInterest(newCreditTierDetailView.getFinalInterest());

            if (newCreditTierDetailView.getSuggestBasePrice() != null && newCreditTierDetailView.getSuggestBasePrice().getId() != 0) {
                newCreditTierDetail.setSuggestBasePrice(baseRateDAO.findById(newCreditTierDetailView.getSuggestBasePrice().getId()));
            } else {
                newCreditTierDetail.setSuggestBasePrice(null);
            }
            newCreditTierDetail.setSuggestInterest(newCreditTierDetailView.getSuggestInterest());

            if (newCreditTierDetailView.getStandardBasePrice() != null && newCreditTierDetailView.getStandardBasePrice().getId() != 0) {
                newCreditTierDetail.setStandardBasePrice(baseRateDAO.findById(newCreditTierDetailView.getStandardBasePrice().getId()));
            } else {
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
            if (newCreditTierDetail.getFinalBasePrice() != null) {
                newFeeDetailView.setFinalBasePrice(baseRateTransform.transformToView(newCreditTierDetail.getFinalBasePrice()));
            } else {
                newFeeDetailView.setFinalBasePrice(new BaseRateView());
            }
            newFeeDetailView.setFinalInterest(newCreditTierDetail.getFinalInterest());
            newFeeDetailView.setFinalPriceLabel(toGetPricing(newCreditTierDetail.getFinalBasePrice(), newCreditTierDetail.getFinalInterest()));
            if (newCreditTierDetail.getSuggestBasePrice() != null) {
                newFeeDetailView.setSuggestBasePrice(baseRateTransform.transformToView(newCreditTierDetail.getSuggestBasePrice()));
            } else {
                newFeeDetailView.setSuggestBasePrice(new BaseRateView());
            }
            newFeeDetailView.setSuggestInterest(newCreditTierDetail.getSuggestInterest());
            newFeeDetailView.setSuggestPriceLabel(toGetPricing(newCreditTierDetail.getSuggestBasePrice(), newCreditTierDetail.getSuggestInterest()));
            if (newCreditTierDetail.getStandardBasePrice() != null) {
                newFeeDetailView.setStandardBasePrice(baseRateTransform.transformToView(newCreditTierDetail.getStandardBasePrice()));
            } else {
                newFeeDetailView.setStandardBasePrice(new BaseRateView());
            }
            newFeeDetailView.setStandardInterest(newCreditTierDetail.getStandardInterest());
            newFeeDetailView.setStandardPriceLabel(toGetPricing(newCreditTierDetail.getStandardBasePrice(), newCreditTierDetail.getStandardInterest()));
            newFeeDetailView.setInstallment(newCreditTierDetail.getInstallment());
            newFeeDetailView.setTenor(newCreditTierDetail.getTenor());
            newCreditTierDetailViewList.add(newFeeDetailView);
        }

        return newCreditTierDetailViewList;
    }

    public List<NewCreditTierDetailView> transformPricingIntTierToView(List<PricingIntTier> pricingInterestList) {
        List<NewCreditTierDetailView> newCreditTierDetailViewList = new ArrayList<NewCreditTierDetailView>();
        NewCreditTierDetailView newCreditTierDetailView;
        log.debug("transformPricingIntTierToView size ::: {}", pricingInterestList.size());

        for (PricingIntTier newPricingIntTier : pricingInterestList) {
            StringBuilder standRate = new StringBuilder();
            StringBuilder suggestRate = new StringBuilder();
            StringBuilder finalRate = new StringBuilder();
            newCreditTierDetailView = new NewCreditTierDetailView();

            //Standard
            standRate.append(newPricingIntTier.getRateType()).append(newPricingIntTier.getSpread()).append(newPricingIntTier.getRateVariance().toString());
            newCreditTierDetailView.setStandardBasePrice(baseRateTransform.transformToView(baseRateDAO.findByName(newPricingIntTier.getRateType())));
            newCreditTierDetailView.setStandardInterest(newPricingIntTier.getRateVariance());
            newCreditTierDetailView.setStandardPriceLabel(standRate.toString());

            //Suggestion
            suggestRate.append(newPricingIntTier.getRateType()).append(newPricingIntTier.getSpread()).append(newPricingIntTier.getRateVariance().toString());
            newCreditTierDetailView.setSuggestBasePrice(baseRateTransform.transformToView(baseRateDAO.findByName(newPricingIntTier.getRateType())));
            newCreditTierDetailView.setSuggestInterest(newPricingIntTier.getRateVariance());
            newCreditTierDetailView.setSuggestPriceLabel(suggestRate.toString());

            //Final
            finalRate.append(newPricingIntTier.getRateType()).append(newPricingIntTier.getSpread()).append(newPricingIntTier.getRateVariance().toString());
            newCreditTierDetailView.setFinalBasePrice(baseRateTransform.transformToView(baseRateDAO.findByName(newPricingIntTier.getRateType())));
            newCreditTierDetailView.setFinalInterest(newPricingIntTier.getRateVariance());
            newCreditTierDetailView.setFinalPriceLabel(finalRate.toString());

            log.debug("newPricingIntTier.getRateType() ::: {}", newCreditTierDetailView.getStandardPriceLabel());
            newCreditTierDetailViewList.add(newCreditTierDetailView);
        }


        return newCreditTierDetailViewList;
    }

    public String toGetPricing(BaseRate baseRate, BigDecimal price) {
        String priceToShow = "";
        if (price != null && baseRate != null) {
            if (price.doubleValue() < 0) {
                priceToShow = baseRate.getName() + " " + price;
            } else {
                priceToShow = baseRate.getName() + " + " + price;
            }
        }
        return priceToShow;
    }
}
