package com.clevel.selos.transform;

import com.clevel.selos.dao.working.BizInfoDetailDAO;
import com.clevel.selos.model.db.master.BusinessActivity;
import com.clevel.selos.model.db.master.BusinessType;
import com.clevel.selos.model.db.master.User;
import com.clevel.selos.model.db.working.BizInfoDetail;
import com.clevel.selos.model.db.working.WorkCasePrescreen;
import com.clevel.selos.model.view.BizInfoDetailView;
import org.joda.time.DateTime;

import javax.inject.Inject;
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
        bizInfoDetailView.setPurchasePercentForeign(bizInfoDetail.getPurchasePercentForeign());
        bizInfoDetailView.setPurchaseTerm(bizInfoDetail.getPurchaseTerm());
        bizInfoDetailView.setStandardAccountPayable(bizInfoDetail.getStandardAccountPayable());
        bizInfoDetailView.setAveragePayableAmount(bizInfoDetail.getAveragePayableAmount());
        bizInfoDetailView.setPayablePercentCash(bizInfoDetail.getPayablePercentCash());
        bizInfoDetailView.setPayablePercentCredit(bizInfoDetail.getPayablePercentCredit());
        bizInfoDetailView.setPayablePercentLocal(bizInfoDetail.getPayablePercentLocal());
        bizInfoDetailView.setPayablePercentForeign(bizInfoDetail.getPayablePercentForeign());
        bizInfoDetailView.setPayableTerm(bizInfoDetail.getPayableTerm());
        bizInfoDetailView.setStandardStock(bizInfoDetail.getStandardStock());
        bizInfoDetailView.setStockDurationBDM(bizInfoDetail.getStockDurationBDM());
        bizInfoDetailView.setStockDurationUW(bizInfoDetail.getStockDurationUW());
        bizInfoDetailView.setStockValueBDM(bizInfoDetail.getStockValueBDM());
        bizInfoDetailView.setStockValueUW(bizInfoDetail.getStockValueUW());

        bizInfoDetailView.setModifyBy(bizInfoDetail.getModifyBy());
        bizInfoDetailView.setModifyDate(bizInfoDetail.getModifyDate());
        bizInfoDetailView.setCreateBy(bizInfoDetail.getCreateBy());
        bizInfoDetailView.setCreateDate(bizInfoDetail.getCreateDate());

        bizInfoDetailView.setIsMainDetail(bizInfoDetail.getIsMainDetail());
        bizInfoDetailView.setIncomeAmount(bizInfoDetail.getIncomeAmount());

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
