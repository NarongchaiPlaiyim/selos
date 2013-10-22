package com.clevel.selos.transform;

import com.clevel.selos.model.db.working.BizInfoDetail;
import com.clevel.selos.model.db.working.WorkCasePrescreen;
import com.clevel.selos.model.view.BizInfoDetailView;

import java.util.ArrayList;
import java.util.List;

public class BizInfoDetailTransform extends Transform {

    public BizInfoDetail transformToModel(BizInfoDetailView bizInfoDetailView){

        BizInfoDetail bizInfoDetail = new BizInfoDetail();

        if(bizInfoDetailView.getId()!= 0){
            bizInfoDetail.setId(bizInfoDetailView.getId());
        }

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
        bizInfoDetail.setPurchasePercentForeign( bizInfoDetailView.getPurchasePercentForeign());
        bizInfoDetail.setPurchaseTerm(bizInfoDetailView.getPurchaseTerm());
        bizInfoDetail.setStandardAccountPayable( bizInfoDetailView.getStandardAccountPayable());
        bizInfoDetail.setAveragePayableAmount(bizInfoDetailView.getAveragePayableAmount());
        bizInfoDetail.setPayablePercentCash(bizInfoDetailView.getPayablePercentCash());
        bizInfoDetail.setPayablePercentCredit(bizInfoDetailView.getPayablePercentCredit());
        bizInfoDetail.setPayablePercentLocal(bizInfoDetailView.getPayablePercentLocal());
        bizInfoDetail.setPayablePercentForeign( bizInfoDetailView.getPayablePercentForeign());
        bizInfoDetail.setPayableTerm(bizInfoDetailView.getPayableTerm());
        bizInfoDetail.setStandardStock(bizInfoDetailView.getStandardStock());
        bizInfoDetail.setStockDurationBDM(bizInfoDetailView.getStockDurationBDM());
        bizInfoDetail.setStockDurationUW(bizInfoDetailView.getStockDurationUW());
        bizInfoDetail.setStockValueBDM(bizInfoDetailView.getStockValueBDM());
        bizInfoDetail.setStockValueUW(bizInfoDetailView.getStockValueUW());
        return bizInfoDetail;
    }

    public BizInfoDetailView transformToView(BizInfoDetail bizInfoDetail){

        BizInfoDetailView bizInfoDetailView = new BizInfoDetailView();

        bizInfoDetailView.setId(bizInfoDetail.getId());
        bizInfoDetailView.setBizInfoText(bizInfoDetail.getBizInfoText());
        bizInfoDetailView.setBizActivity(bizInfoDetail.getBusinessActivity());
        bizInfoDetailView.setBizType(bizInfoDetail.getBusinessType());
        bizInfoDetailView.setBizGroup(bizInfoDetail.getBusinessGroup());
        bizInfoDetailView.setBizDesc(bizInfoDetail.getBusinessDescription());
        bizInfoDetailView.setBizCode(bizInfoDetail.getBizCode());
        bizInfoDetailView.setBizComment(bizInfoDetail.getBizComment());
        bizInfoDetailView.setIncomeFactor(bizInfoDetail.getIncomeFactor());
        bizInfoDetailView.setAdjustedIncomeFactor(bizInfoDetail.getAdjustedIncomeFactor());
        bizInfoDetailView.setPercentBiz(bizInfoDetail.getPercentBiz());
        bizInfoDetailView.setBizPermission(bizInfoDetail.getBizPermission());
        bizInfoDetailView.setBizDocPermission(bizInfoDetail.getBizDocPermission());
        bizInfoDetailView.setBizDocExpiryDate(bizInfoDetail.getBizDocExpiryDate());
        bizInfoDetailView.setExpIndCountryName(bizInfoDetail.getExpIndCountryName());
        bizInfoDetailView.setPercentExpIndCountryName(bizInfoDetail.getPercentExpIndCountryName());
        bizInfoDetailView.setSupplierTotalPercentBuyVolume(bizInfoDetail.getSupplierTotalPercentBuyVolume());
        bizInfoDetailView.setSupplierTotalPercentCredit(bizInfoDetail.getSupplierTotalPercentCredit());
        bizInfoDetailView.setSupplierTotalCreditTerm(bizInfoDetail.getSupplierTotalCreditTerm());
        bizInfoDetailView.setSupplierUWAdjustPercentCredit(bizInfoDetail.getSupplierUWAdjustPercentCredit());
        bizInfoDetailView.setSupplierUWAdjustCreditTerm(bizInfoDetail.getSupplierUWAdjustCreditTerm());
        bizInfoDetailView.setBuyerTotalPercentBuyVolume(bizInfoDetail.getBuyerTotalPercentBuyVolume());
        bizInfoDetailView.setBuyerTotalPercentCredit(bizInfoDetail.getBuyerTotalPercentCredit());
        bizInfoDetailView.setBuyerTotalCreditTerm(bizInfoDetail.getBuyerTotalCreditTerm());
        bizInfoDetailView.setBuyerUWAdjustPercentCredit(bizInfoDetail.getBuyerUWAdjustPercentCredit());
        bizInfoDetailView.setBuyerUWAdjustCreditTerm(bizInfoDetail.getBuyerUWAdjustCreditTerm());
        bizInfoDetailView.setStandardAccountReceivable(bizInfoDetail.getStandardAccountReceivable());
        bizInfoDetailView.setAveragePurchaseAmount(bizInfoDetail.getAveragePurchaseAmount());
        bizInfoDetailView.setPurchasePercentCash(bizInfoDetail.getPurchasePercentCash());
        bizInfoDetailView.setPurchasePercentCredit(bizInfoDetail.getPurchasePercentCredit());
        bizInfoDetailView.setPurchasePercentLocal(bizInfoDetail.getPurchasePercentLocal());
        bizInfoDetailView.setPurchasePercentForeign( bizInfoDetail.getPurchasePercentForeign());
        bizInfoDetailView.setPurchaseTerm(bizInfoDetail.getPurchaseTerm());
        bizInfoDetailView.setStandardAccountPayable( bizInfoDetail.getStandardAccountPayable());
        bizInfoDetailView.setAveragePayableAmount(bizInfoDetail.getAveragePayableAmount());
        bizInfoDetailView.setPayablePercentCash(bizInfoDetail.getPayablePercentCash());
        bizInfoDetailView.setPayablePercentCredit(bizInfoDetail.getPayablePercentCredit());
        bizInfoDetailView.setPayablePercentLocal(bizInfoDetail.getPayablePercentLocal());
        bizInfoDetailView.setPayablePercentForeign( bizInfoDetail.getPayablePercentForeign());
        bizInfoDetailView.setPayableTerm(bizInfoDetail.getPayableTerm());
        bizInfoDetailView.setStandardStock(bizInfoDetail.getStandardStock());
        bizInfoDetailView.setStockDurationBDM(bizInfoDetail.getStockDurationBDM());
        bizInfoDetailView.setStockDurationUW(bizInfoDetail.getStockDurationUW());
        bizInfoDetailView.setStockValueBDM(bizInfoDetail.getStockValueBDM());
        bizInfoDetailView.setStockValueUW(bizInfoDetail.getStockValueUW());

        return bizInfoDetailView;
    }

    public List<BizInfoDetailView> transformToPreScreenView(List<BizInfoDetail> bizInfoDetails){
        List<BizInfoDetailView> bizInfoDetailViewList = new ArrayList<BizInfoDetailView>();
        for(BizInfoDetail item : bizInfoDetails){
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

    public List<BizInfoDetail> transformPrescreenToModel(List<BizInfoDetailView> bizInfoDetailViews, WorkCasePrescreen workCasePrescreen){
        List<BizInfoDetail> bizInfoDetailList = new ArrayList<BizInfoDetail>();
        for(BizInfoDetailView item : bizInfoDetailViews){
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

}
