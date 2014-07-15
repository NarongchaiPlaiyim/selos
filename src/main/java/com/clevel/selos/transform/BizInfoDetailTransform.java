package com.clevel.selos.transform;

import com.clevel.selos.dao.working.BizInfoDetailDAO;
import com.clevel.selos.model.db.master.BusinessActivity;
import com.clevel.selos.model.db.master.BusinessType;
import com.clevel.selos.model.db.master.User;
import com.clevel.selos.model.db.working.BizInfoDetail;
import com.clevel.selos.model.db.working.WorkCasePrescreen;
import com.clevel.selos.model.view.BizInfoDetailView;
import com.clevel.selos.util.Util;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BizInfoDetailTransform extends Transform {
    @Inject
    private BizInfoDetailDAO bizInfoDetailDAO;

    public BizInfoDetail transformToModel(BizInfoDetailView bizInfoDetailView, User user) {

        BizInfoDetail bizInfoDetail = new BizInfoDetail();

        if (bizInfoDetailView.getId() != 0) {
            bizInfoDetail = bizInfoDetailDAO.findById(bizInfoDetailView.getId());
        } else {
            bizInfoDetail.setCreateBy(user);
            bizInfoDetail.setCreateDate(new Date());
        }

        bizInfoDetail.setModifyBy(user);
        bizInfoDetail.setModifyDate(new Date());

        bizInfoDetail.setBizInfoText(bizInfoDetailView.getBizInfoText());
        bizInfoDetail.setBusinessActivity(bizInfoDetailView.getBizActivity());
        bizInfoDetail.setBusinessType(bizInfoDetailView.getBizType());
        bizInfoDetail.setBusinessGroup(bizInfoDetailView.getBizGroup());
        bizInfoDetail.setBusinessDescription(bizInfoDetailView.getBizDesc());
        bizInfoDetail.setBizCode(bizInfoDetailView.getBizCode());
        bizInfoDetail.setBizComment(bizInfoDetailView.getBizComment());
        bizInfoDetail.setIncomeFactor(bizInfoDetailView.getIncomeFactor());
        bizInfoDetail.setAdjustedIncomeFactor(bizInfoDetailView.getAdjustedIncomeFactor());
        bizInfoDetail.setPercentBiz(bizInfoDetailView.getPercentBiz());
        bizInfoDetail.setBizPermission(bizInfoDetailView.getBizPermission());
        bizInfoDetail.setBizDocPermission(bizInfoDetailView.getBizDocPermission());
        bizInfoDetail.setBizDocExpiryDate(bizInfoDetailView.getBizDocExpiryDate());
        bizInfoDetail.setExpIndCountryName(bizInfoDetailView.getExpIndCountryName());
        bizInfoDetail.setPercentExpIndCountryName(bizInfoDetailView.getPercentExpIndCountryName());
        bizInfoDetail.setSupplierTotalPercentBuyVolume(bizInfoDetailView.getSupplierTotalPercentBuyVolume());
        bizInfoDetail.setSupplierTotalPercentCredit(bizInfoDetailView.getSupplierTotalPercentCredit());
        bizInfoDetail.setSupplierTotalCreditTerm(bizInfoDetailView.getSupplierTotalCreditTerm());
        bizInfoDetail.setSupplierUWAdjustPercentCredit(bizInfoDetailView.getSupplierUWAdjustPercentCredit());
        bizInfoDetail.setSupplierUWAdjustCreditTerm(bizInfoDetailView.getSupplierUWAdjustCreditTerm());
        bizInfoDetail.setBuyerTotalPercentBuyVolume(bizInfoDetailView.getBuyerTotalPercentBuyVolume());
        bizInfoDetail.setBuyerTotalPercentCredit(bizInfoDetailView.getBuyerTotalPercentCredit());
        bizInfoDetail.setBuyerTotalCreditTerm(bizInfoDetailView.getBuyerTotalCreditTerm());
        bizInfoDetail.setBuyerUWAdjustPercentCredit(bizInfoDetailView.getBuyerUWAdjustPercentCredit());
        bizInfoDetail.setBuyerUWAdjustCreditTerm(bizInfoDetailView.getBuyerUWAdjustCreditTerm());
        bizInfoDetail.setStandardAccountReceivable(bizInfoDetailView.getStandardAccountReceivable());
        bizInfoDetail.setAveragePurchaseAmount(bizInfoDetailView.getAveragePurchaseAmount());
        bizInfoDetail.setPurchasePercentCash(bizInfoDetailView.getPurchasePercentCash());
        bizInfoDetail.setPurchasePercentCredit(bizInfoDetailView.getPurchasePercentCredit());
        bizInfoDetail.setPurchasePercentLocal(bizInfoDetailView.getPurchasePercentLocal());
        bizInfoDetail.setPurchasePercentForeign(bizInfoDetailView.getPurchasePercentForeign());
        bizInfoDetail.setPurchaseTerm(bizInfoDetailView.getPurchaseTerm());
        bizInfoDetail.setStandardAccountPayable(bizInfoDetailView.getStandardAccountPayable());
        bizInfoDetail.setAveragePayableAmount(bizInfoDetailView.getAveragePayableAmount());
        bizInfoDetail.setPayablePercentCash(bizInfoDetailView.getPayablePercentCash());
        bizInfoDetail.setPayablePercentCredit(bizInfoDetailView.getPayablePercentCredit());
        bizInfoDetail.setPayablePercentLocal(bizInfoDetailView.getPayablePercentLocal());
        bizInfoDetail.setPayablePercentForeign(bizInfoDetailView.getPayablePercentForeign());
        bizInfoDetail.setPayableTerm(bizInfoDetailView.getPayableTerm());
        bizInfoDetail.setStandardStock(bizInfoDetailView.getStandardStock());
        bizInfoDetail.setStockDurationBDM(bizInfoDetailView.getStockDurationBDM());
        bizInfoDetail.setStockDurationUW(bizInfoDetailView.getStockDurationUW());
        bizInfoDetail.setStockValueBDM(bizInfoDetailView.getStockValueBDM());
        bizInfoDetail.setStockValueUW(bizInfoDetailView.getStockValueUW());
        bizInfoDetail.setIncomeAmount(bizInfoDetailView.getIncomeAmount());
        bizInfoDetail.setIsMainDetail(bizInfoDetailView.getIsMainDetail());

        return bizInfoDetail;
    }

    public BizInfoDetailView transformToView(BizInfoDetail bizInfoDetail) {

        BizInfoDetailView bizInfoDetailView = new BizInfoDetailView();

        bizInfoDetailView.setId(bizInfoDetail.getId());
        bizInfoDetailView.setBizInfoText(bizInfoDetail.getBizInfoText());
        bizInfoDetailView.setBizActivity(bizInfoDetail.getBusinessActivity());
        if(bizInfoDetail.getBusinessActivity() == null){                         //Check null if PreScreen Send by NULL
            bizInfoDetailView.setBizActivity(new BusinessActivity());
        }
        bizInfoDetailView.setBizType(bizInfoDetail.getBusinessType());
        if(bizInfoDetail.getBusinessType() == null){
            bizInfoDetailView.setBizType(new BusinessType());
        }
        bizInfoDetailView.setBizGroup(bizInfoDetail.getBusinessGroup());
        bizInfoDetailView.setBizDesc(bizInfoDetail.getBusinessDescription());
        bizInfoDetailView.setBizCode(bizInfoDetail.getBizCode());
        bizInfoDetailView.setBizComment(bizInfoDetail.getBizComment());
        bizInfoDetailView.setIncomeFactor(Util.isNull(bizInfoDetail.getIncomeFactor()) ? BigDecimal.ZERO : bizInfoDetail.getIncomeFactor());
        bizInfoDetailView.setAdjustedIncomeFactor(Util.isNull(bizInfoDetail.getAdjustedIncomeFactor()) ? BigDecimal.ZERO : bizInfoDetail.getAdjustedIncomeFactor());
        bizInfoDetailView.setPercentBiz(Util.isNull(bizInfoDetail.getPercentBiz()) ? BigDecimal.ZERO : bizInfoDetail.getPercentBiz());
        bizInfoDetailView.setBizPermission(bizInfoDetail.getBizPermission());
        bizInfoDetailView.setBizDocPermission(bizInfoDetail.getBizDocPermission());

        bizInfoDetailView.setBizDocExpiryDate(bizInfoDetail.getBizDocExpiryDate());

        bizInfoDetailView.setExpIndCountryName(bizInfoDetail.getExpIndCountryName());
        bizInfoDetailView.setPercentExpIndCountryName(Util.isNull(bizInfoDetail.getPercentExpIndCountryName()) ? BigDecimal.ZERO : bizInfoDetail.getPercentExpIndCountryName());
        bizInfoDetailView.setSupplierTotalPercentBuyVolume(Util.isNull(bizInfoDetail.getSupplierTotalPercentBuyVolume()) ? BigDecimal.ZERO : bizInfoDetail.getSupplierTotalPercentBuyVolume());
        bizInfoDetailView.setSupplierTotalPercentCredit(Util.isNull(bizInfoDetail.getSupplierTotalPercentCredit()) ? BigDecimal.ZERO : bizInfoDetail.getSupplierTotalPercentCredit());
        bizInfoDetailView.setSupplierTotalCreditTerm(Util.isNull(bizInfoDetail.getSupplierTotalCreditTerm()) ? BigDecimal.ZERO : bizInfoDetail.getSupplierTotalCreditTerm());
        bizInfoDetailView.setSupplierUWAdjustPercentCredit(Util.isNull(bizInfoDetail.getSupplierUWAdjustPercentCredit()) ? BigDecimal.ZERO : bizInfoDetail.getSupplierUWAdjustPercentCredit());
        bizInfoDetailView.setSupplierUWAdjustCreditTerm(Util.isNull(bizInfoDetail.getSupplierUWAdjustCreditTerm()) ? BigDecimal.ZERO : bizInfoDetail.getSupplierUWAdjustCreditTerm());
        bizInfoDetailView.setBuyerTotalPercentBuyVolume(Util.isNull(bizInfoDetail.getBuyerTotalPercentBuyVolume()) ? BigDecimal.ZERO : bizInfoDetail.getBuyerTotalPercentBuyVolume());
        bizInfoDetailView.setBuyerTotalPercentCredit(Util.isNull(bizInfoDetail.getBuyerTotalPercentCredit()) ? BigDecimal.ZERO : bizInfoDetail.getBuyerTotalPercentCredit());
        bizInfoDetailView.setBuyerTotalCreditTerm(Util.isNull(bizInfoDetail.getBuyerTotalCreditTerm()) ? BigDecimal.ZERO : bizInfoDetail.getBuyerTotalCreditTerm());
        bizInfoDetailView.setBuyerUWAdjustPercentCredit(Util.isNull(bizInfoDetail.getBuyerUWAdjustPercentCredit()) ? BigDecimal.ZERO : bizInfoDetail.getBuyerUWAdjustPercentCredit());
        bizInfoDetailView.setBuyerUWAdjustCreditTerm(Util.isNull(bizInfoDetail.getBuyerUWAdjustCreditTerm()) ? BigDecimal.ZERO : bizInfoDetail.getBuyerUWAdjustCreditTerm());
        bizInfoDetailView.setStandardAccountReceivable(Util.isNull(bizInfoDetail.getStandardAccountReceivable()) ? BigDecimal.ZERO : bizInfoDetail.getStandardAccountReceivable());
        bizInfoDetailView.setAveragePurchaseAmount(Util.isNull(bizInfoDetail.getAveragePurchaseAmount()) ? BigDecimal.ZERO : bizInfoDetail.getAveragePurchaseAmount());
        bizInfoDetailView.setPurchasePercentCash(Util.isNull(bizInfoDetail.getPurchasePercentCash()) ? BigDecimal.ZERO : bizInfoDetail.getPurchasePercentCash());
        bizInfoDetailView.setPurchasePercentCredit(Util.isNull(bizInfoDetail.getPurchasePercentCredit()) ? BigDecimal.ZERO : bizInfoDetail.getPurchasePercentCredit());
        bizInfoDetailView.setPurchasePercentLocal(Util.isNull(bizInfoDetail.getPurchasePercentLocal()) ? BigDecimal.ZERO : bizInfoDetail.getPurchasePercentLocal());
        bizInfoDetailView.setPurchasePercentForeign(Util.isNull(bizInfoDetail.getPurchasePercentForeign()) ? BigDecimal.ZERO : bizInfoDetail.getPurchasePercentForeign());
        bizInfoDetailView.setPurchaseTerm(Util.isNull(bizInfoDetail.getPurchaseTerm()) ? BigDecimal.ZERO : bizInfoDetail.getPurchaseTerm());
        bizInfoDetailView.setStandardAccountPayable(Util.isNull(bizInfoDetail.getStandardAccountPayable()) ? BigDecimal.ZERO : bizInfoDetail.getStandardAccountPayable());
        bizInfoDetailView.setAveragePayableAmount(Util.isNull(bizInfoDetail.getAveragePayableAmount()) ? BigDecimal.ZERO : bizInfoDetail.getAveragePayableAmount());
        bizInfoDetailView.setPayablePercentCash(Util.isNull(bizInfoDetail.getPayablePercentCash()) ? BigDecimal.ZERO : bizInfoDetail.getPayablePercentCash());
        bizInfoDetailView.setPayablePercentCredit(Util.isNull(bizInfoDetail.getPayablePercentCredit()) ? BigDecimal.ZERO : bizInfoDetail.getPayablePercentCredit());
        bizInfoDetailView.setPayablePercentLocal(Util.isNull(bizInfoDetail.getPayablePercentLocal()) ? BigDecimal.ZERO : bizInfoDetail.getPayablePercentLocal());
        bizInfoDetailView.setPayablePercentForeign(Util.isNull(bizInfoDetail.getPayablePercentForeign()) ? BigDecimal.ZERO : bizInfoDetail.getPayablePercentForeign());
        bizInfoDetailView.setPayableTerm(Util.isNull(bizInfoDetail.getPayableTerm()) ? BigDecimal.ZERO : bizInfoDetail.getPayableTerm());
        bizInfoDetailView.setStandardStock(Util.isNull(bizInfoDetail.getStandardStock()) ? BigDecimal.ZERO : bizInfoDetail.getStandardStock());
        bizInfoDetailView.setStockDurationBDM(Util.isNull(bizInfoDetail.getStockDurationBDM()) ? BigDecimal.ZERO : bizInfoDetail.getStockDurationBDM());
        bizInfoDetailView.setStockDurationUW(Util.isNull(bizInfoDetail.getStockDurationUW()) ? BigDecimal.ZERO : bizInfoDetail.getStockDurationUW());
        bizInfoDetailView.setStockValueBDM(Util.isNull(bizInfoDetail.getStockValueBDM()) ? BigDecimal.ZERO : bizInfoDetail.getStockValueBDM());
        bizInfoDetailView.setStockValueUW(Util.isNull(bizInfoDetail.getStockValueUW()) ? BigDecimal.ZERO : bizInfoDetail.getStockValueUW());

        bizInfoDetailView.setModifyBy(bizInfoDetail.getModifyBy());
        bizInfoDetailView.setModifyDate(bizInfoDetail.getModifyDate());
        bizInfoDetailView.setCreateBy(bizInfoDetail.getCreateBy());
        bizInfoDetailView.setCreateDate(bizInfoDetail.getCreateDate());

        bizInfoDetailView.setIsMainDetail(bizInfoDetail.getIsMainDetail());
        bizInfoDetailView.setIncomeAmount(Util.isNull(bizInfoDetail.getIncomeAmount()) ? BigDecimal.ZERO : bizInfoDetail.getIncomeAmount());

        return bizInfoDetailView;
    }

    public List<BizInfoDetailView> transformToPreScreenView(List<BizInfoDetail> bizInfoDetails) {
        List<BizInfoDetailView> bizInfoDetailViewList = new ArrayList<BizInfoDetailView>();
        for (BizInfoDetail item : bizInfoDetails) {
            BizInfoDetailView bizInfoDetailView = new BizInfoDetailView();
            bizInfoDetailView.setId(item.getId());
            bizInfoDetailView.setBizInfoText(item.getBizInfoText());
            bizInfoDetailView.setBizType(item.getBusinessType());
            bizInfoDetailView.setBizGroup(item.getBusinessGroup());
            bizInfoDetailView.setBizDesc(item.getBusinessDescription());
            bizInfoDetailView.setBizCode(item.getBizCode());
            bizInfoDetailView.setIncomeFactor(item.getIncomeFactor());
            bizInfoDetailView.setAdjustedIncomeFactor(item.getAdjustedIncomeFactor());
            bizInfoDetailView.setPercentBiz(item.getPercentBiz());
            bizInfoDetailView.setBizPermission(item.getBizPermission());
            bizInfoDetailView.setBizComment(item.getBizComment());

            bizInfoDetailViewList.add(bizInfoDetailView);
        }

        return bizInfoDetailViewList;
    }

    public List<BizInfoDetail> transformPrescreenToModel(List<BizInfoDetailView> bizInfoDetailViews, WorkCasePrescreen workCasePrescreen) {
        List<BizInfoDetail> bizInfoDetailList = new ArrayList<BizInfoDetail>();
        for (BizInfoDetailView item : bizInfoDetailViews) {
            BizInfoDetail bizInfoDetail = new BizInfoDetail();
            bizInfoDetail.setId(item.getId());
            bizInfoDetail.setWorkCasePrescreen(workCasePrescreen);
            bizInfoDetail.setBizInfoText(item.getBizInfoText());
            bizInfoDetail.setBusinessType(item.getBizType());
            bizInfoDetail.setBusinessGroup(item.getBizGroup());
            bizInfoDetail.setBusinessDescription(item.getBizDesc());
            bizInfoDetail.setBizCode(item.getBizCode());
            bizInfoDetail.setIncomeFactor(item.getIncomeFactor());
            bizInfoDetail.setAdjustedIncomeFactor(item.getAdjustedIncomeFactor());
            bizInfoDetail.setPercentBiz(item.getPercentBiz());
            bizInfoDetail.setBizPermission(item.getBizPermission());
            bizInfoDetail.setBizComment(item.getBizComment());

            bizInfoDetailList.add(bizInfoDetail);
        }

        return bizInfoDetailList;
    }

    public List<BizInfoDetailView> transformToViewList(List<BizInfoDetail> bizInfoDetails){
        List<BizInfoDetailView> bizInfoDetailViewList = new ArrayList<BizInfoDetailView>();

        for(BizInfoDetail item : bizInfoDetails){
            BizInfoDetailView bizInfoDetailView = transformToView(item);
            bizInfoDetailViewList.add(bizInfoDetailView);
        }
        return bizInfoDetailViewList;
    }
}
