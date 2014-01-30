package com.clevel.selos.transform;

import com.clevel.selos.dao.master.AuthorizationDOADAO;
import com.clevel.selos.dao.master.ReasonDAO;
import com.clevel.selos.integration.corebanking.model.CustomerInfo;
import com.clevel.selos.model.db.master.*;
import com.clevel.selos.model.db.working.*;
import com.clevel.selos.model.view.*;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ExSummaryTransform extends Transform {
    @Inject
    AuthorizationDOADAO authorizationDOADAO;
    @Inject
    ReasonDAO reasonDAO;

    @Inject
    public ExSummaryTransform() {
    }

    public ExSummary transformToModel(ExSummaryView exSummaryView, WorkCase workCase, User user) {
        ExSummary exSummary = new ExSummary();

        exSummary.setWorkCase(workCase);

        if (exSummaryView.getId() != 0) {
            exSummary.setId(exSummaryView.getId());
            exSummary.setCreateDate(exSummaryView.getCreateDate());
            exSummary.setCreateBy(exSummaryView.getCreateBy());
        } else { // id = 0 create new
            exSummary.setCreateDate(new Date());
            exSummary.setCreateBy(user);
        }
        exSummary.setModifyDate(new Date());
        exSummary.setModifyBy(user);

        /*//ExSumCharacteristicView
        exSummary.setCustomer(exSummaryView.getExSumCharacteristicView().getCustomer());
        exSummary.setCurrentDBR(exSummaryView.getExSumCharacteristicView().getCurrentDBR());
        exSummary.setFinalDBR(exSummaryView.getExSumCharacteristicView().getFinalDBR());
        exSummary.setIncome(exSummaryView.getExSumCharacteristicView().getIncome());
        exSummary.setRecommendedWCNeed(exSummaryView.getExSumCharacteristicView().getRecommendedWCNeed());
        exSummary.setActualWC(exSummaryView.getExSumCharacteristicView().getActualWC());
        exSummary.setStartBusinessDate(exSummaryView.getExSumCharacteristicView().getStartBusinessDate());
        exSummary.setYearInBusiness(exSummaryView.getExSumCharacteristicView().getYearInBusiness());
        exSummary.setSalePerYearBDM(exSummaryView.getExSumCharacteristicView().getSalePerYearBDM());
        exSummary.setSalePerYearUW(exSummaryView.getExSumCharacteristicView().getSalePerYearUW());
        exSummary.setGroupSaleBDM(exSummaryView.getExSumCharacteristicView().getGroupSaleBDM());
        exSummary.setGroupSaleUW(exSummaryView.getExSumCharacteristicView().getGroupSaleUW());
        exSummary.setGroupExposureBDM(exSummaryView.getExSumCharacteristicView().getGroupExposureBDM());
        exSummary.setGroupExposureUW(exSummaryView.getExSumCharacteristicView().getGroupExposureUW());
        
        //ExSumBusinessInfoView
        exSummary.setNetFixAsset(exSummaryView.getExSumBusinessInfoView().getNetFixAsset());
        exSummary.setNoOfEmployee(exSummaryView.getExSumBusinessInfoView().getNoOfEmployee());
        exSummary.setBizProvince(exSummaryView.getExSumBusinessInfoView().getBizProvince());
        exSummary.setBizType(exSummaryView.getExSumBusinessInfoView().getBizType());
        exSummary.setBizGroup(exSummaryView.getExSumBusinessInfoView().getBizGroup());
        exSummary.setBizCode(exSummaryView.getExSumBusinessInfoView().getBizCode());
        exSummary.setBizDesc(exSummaryView.getExSumBusinessInfoView().getBizDesc());
        exSummary.setQualitativeClass(exSummaryView.getExSumBusinessInfoView().getQualitativeClass());
        exSummary.setBizSize(exSummaryView.getExSumBusinessInfoView().getBizSize());
        exSummary.setBDM(exSummaryView.getExSumBusinessInfoView().getBDM());
        exSummary.setUW(exSummaryView.getExSumBusinessInfoView().getUW());
        exSummary.setAR(exSummaryView.getExSumBusinessInfoView().getAR());
        exSummary.setAP(exSummaryView.getExSumBusinessInfoView().getAP());
        exSummary.setINV(exSummaryView.getExSumBusinessInfoView().getINV());
        
        exSummary.setBusinessOperationActivity(exSummaryView.getBusinessOperationActivity());
        exSummary.setBusinessPermission(exSummaryView.getBusinessPermission());
        exSummary.setExpiryDate(exSummaryView.getExpiryDate());

        //ExSumAccountMovementView
        exSummary.setOdLimit(exSummaryView.getExSumAccMovementView().getOdLimit());
        exSummary.setUtilization(exSummaryView.getExSumAccMovementView().getUtilization());
        exSummary.setSwing(exSummaryView.getExSumAccMovementView().getSwing());
        exSummary.setOverLimitTimes(exSummaryView.getExSumAccMovementView().getOverLimitTimes());
        exSummary.setOverLimitDays(exSummaryView.getExSumAccMovementView().getOverLimitDays());
        exSummary.setChequeReturn(exSummaryView.getExSumAccMovementView().getChequeReturn());
        exSummary.setCashFlow(exSummaryView.getExSumAccMovementView().getCashFlow());
        exSummary.setCashFlowLimit(exSummaryView.getExSumAccMovementView().getCashFlowLimit());
        exSummary.setTradeChequeReturnAmount(exSummaryView.getExSumAccMovementView().getTradeChequeReturnAmount());
        exSummary.setTradeChequeReturnPercent(exSummaryView.getExSumAccMovementView().getTradeChequeReturnPercent());

        //ExSumCollateralView
        exSummary.setCashCollateralValue(exSummaryView.getExSumCollateralView().getCashCollateralValue());
        exSummary.setCoreAssetValue(exSummaryView.getExSumCollateralView().getCoreAssetValue());
        exSummary.setNoneCoreAssetValue(exSummaryView.getExSumCollateralView().getNoneCoreAssetValue());
        exSummary.setLimitApprove(exSummaryView.getExSumCollateralView().getLimitApprove());
        exSummary.setPercentLTV(exSummaryView.getExSumCollateralView().getPercentLTV());*/

        //Business Overview and Support Decision
        exSummary.setNatureOfBusiness(exSummaryView.getNatureOfBusiness());
        exSummary.setHistoricalAndReasonOfChange(exSummaryView.getHistoricalAndReasonOfChange());
        exSummary.setTmbCreditHistory(exSummaryView.getTmbCreditHistory());
        exSummary.setSupportReason(exSummaryView.getSupportReason());
        exSummary.setRm008Code(exSummaryView.getRm008Code());
        exSummary.setRm008Remark(exSummaryView.getRm008Remark());
        exSummary.setRm204Code(exSummaryView.getRm204Code());
        exSummary.setRm204Remark(exSummaryView.getRm204Remark());
        exSummary.setRm020Code(exSummaryView.getRm020Code());
        exSummary.setRm020Remark(exSummaryView.getRm020Remark());

        //UW Decision and Explanation
        exSummary.setApproveAuthority(exSummaryView.getApproveAuthority());
        if (exSummary.getApproveAuthority().getId() == 0) {
            exSummary.setApproveAuthority(null);
        }
        exSummary.setDecision(exSummaryView.getDecision());
        exSummary.setUwCode(exSummaryView.getUwCode());

        //UW Comment
        exSummary.setUwComment(exSummaryView.getUwComment());

        //
        exSummary.setYearInBusinessMonth(exSummaryView.getYearInBusinessMonth());

        return exSummary;
    }

    public ExSummaryView transformToView(ExSummary exSummary) {
        ExSummaryView exSummaryView = new ExSummaryView();

        exSummaryView.setId(exSummary.getId());

        exSummaryView.setCreateDate(exSummary.getCreateDate());
        exSummaryView.setCreateBy(exSummary.getCreateBy());
        exSummaryView.setModifyDate(exSummary.getModifyDate());
        exSummaryView.setModifyBy(exSummary.getModifyBy());

        /*//ExSumCharacteristicView
        ExSumCharacteristicView exSumCharacteristicView = new ExSumCharacteristicView();
        if(exSummary.getCustomer() != null
                || exSummary.getCurrentDBR() != null
                || exSummary.getFinalDBR() != null
                || exSummary.getIncome() != null
                || exSummary.getRecommendedWCNeed() != null
                || exSummary.getActualWC() != null
                || exSummary.getStartBusinessDate() != null
                || exSummary.getYearInBusiness() != null
                || exSummary.getSalePerYearBDM() != null
                || exSummary.getSalePerYearUW() != null
                || exSummary.getGroupSaleBDM() != null
                || exSummary.getGroupSaleUW() != null
                || exSummary.getGroupExposureBDM() != null
                || exSummary.getGroupExposureUW() != null){
            exSumCharacteristicView.setCustomer(exSummary.getCustomer());
            exSumCharacteristicView.setCurrentDBR(exSummary.getCurrentDBR());
            exSumCharacteristicView.setFinalDBR(exSummary.getFinalDBR());
            exSumCharacteristicView.setIncome(exSummary.getIncome());
            exSumCharacteristicView.setRecommendedWCNeed(exSummary.getRecommendedWCNeed());
            exSumCharacteristicView.setActualWC(exSummary.getActualWC());
            exSumCharacteristicView.setStartBusinessDate(exSummary.getStartBusinessDate());
            exSumCharacteristicView.setYearInBusiness(exSummary.getYearInBusiness());
            exSumCharacteristicView.setSalePerYearBDM(exSummary.getSalePerYearBDM());
            exSumCharacteristicView.setSalePerYearUW(exSummary.getSalePerYearUW());
            exSumCharacteristicView.setGroupSaleBDM(exSummary.getGroupSaleBDM());
            exSumCharacteristicView.setGroupSaleUW(exSummary.getGroupSaleUW());
            exSumCharacteristicView.setGroupExposureBDM(exSummary.getGroupExposureBDM());
            exSumCharacteristicView.setGroupExposureUW(exSummary.getGroupExposureUW());
        } else {
            exSumCharacteristicView = null;
        }
        exSummaryView.setExSumCharacteristicView(exSumCharacteristicView);

        //ExSumBusinessInfoView
        ExSumBusinessInfoView exSumBusinessInfoView = new ExSumBusinessInfoView();
        if(exSummary.getNetFixAsset() != null
                || exSummary.getBizProvince() != null
                || exSummary.getBizType() != null
                || exSummary.getBizGroup() != null
                || exSummary.getBizCode() != null
                || exSummary.getBizDesc() != null
                || exSummary.getQualitativeClass() != null
                || exSummary.getBizSize() != null
                || exSummary.getBDM() != null
                || exSummary.getUW() != null
                || exSummary.getAR() != null
                || exSummary.getAP() != null
                || exSummary.getINV() != null){
            exSumBusinessInfoView.setNetFixAsset(exSummary.getNetFixAsset());
            exSumBusinessInfoView.setNoOfEmployee(exSummary.getNoOfEmployee());
            exSumBusinessInfoView.setBizProvince(exSummary.getBizProvince());
            exSumBusinessInfoView.setBizType(exSummary.getBizType());
            exSumBusinessInfoView.setBizGroup(exSummary.getBizGroup());
            exSumBusinessInfoView.setBizCode(exSummary.getBizCode());
            exSumBusinessInfoView.setBizDesc(exSummary.getBizDesc());
            exSumBusinessInfoView.setQualitativeClass(exSummary.getQualitativeClass());
            exSumBusinessInfoView.setBizSize(exSummary.getBizSize());
            exSumBusinessInfoView.setBDM(exSummary.getBDM());
            exSumBusinessInfoView.setUW(exSummary.getUW());
            exSumBusinessInfoView.setAR(exSummary.getAR());
            exSumBusinessInfoView.setAP(exSummary.getAP());
            exSumBusinessInfoView.setINV(exSummary.getINV());
        } else {
            exSumBusinessInfoView = null;
        }
        exSummaryView.setExSumBusinessInfoView(exSumBusinessInfoView);

        exSummaryView.setBusinessOperationActivity(exSummary.getBusinessOperationActivity());
        exSummaryView.setBusinessPermission(exSummary.getBusinessPermission());
        exSummaryView.setExpiryDate(exSummary.getExpiryDate());

        //ExSumAccountMovementView
        ExSumAccountMovementView exSumAccountMovementView = new ExSumAccountMovementView();
        exSumAccountMovementView.setOdLimit(exSummary.getOdLimit());
        exSumAccountMovementView.setUtilization(exSummary.getUtilization());
        exSumAccountMovementView.setSwing(exSummary.getSwing());
        exSumAccountMovementView.setOverLimitTimes(exSummary.getOverLimitTimes());
        exSumAccountMovementView.setOverLimitDays(exSummary.getOverLimitDays());
        exSumAccountMovementView.setChequeReturn(exSummary.getChequeReturn());
        exSumAccountMovementView.setCashFlow(exSummary.getCashFlow());
        exSumAccountMovementView.setCashFlowLimit(exSummary.getCashFlowLimit());
        exSumAccountMovementView.setTradeChequeReturnAmount(exSummary.getTradeChequeReturnAmount());
        exSumAccountMovementView.setTradeChequeReturnPercent(exSummary.getTradeChequeReturnPercent());
        exSummaryView.setExSumAccMovementView(exSumAccountMovementView);

        //ExSumCollateralView
        ExSumCollateralView exSumCollateralView = new ExSumCollateralView();
        exSumCollateralView.setCashCollateralValue(exSummary.getCashCollateralValue());
        exSumCollateralView.setCoreAssetValue(exSummary.getCoreAssetValue());
        exSumCollateralView.setNoneCoreAssetValue(exSummary.getNoneCoreAssetValue());
        exSumCollateralView.setLimitApprove(exSummary.getLimitApprove());
        exSumCollateralView.setPercentLTV(exSummary.getPercentLTV());
        exSummaryView.setExSumCollateralView(exSumCollateralView);*/

        //Business Overview and Support Decision
        exSummaryView.setNatureOfBusiness(exSummary.getNatureOfBusiness());
        exSummaryView.setHistoricalAndReasonOfChange(exSummary.getHistoricalAndReasonOfChange());
        exSummaryView.setTmbCreditHistory(exSummary.getTmbCreditHistory());
        exSummaryView.setSupportReason(exSummary.getSupportReason());
        exSummaryView.setRm008Code(exSummary.getRm008Code());
        exSummaryView.setRm008Remark(exSummary.getRm008Remark());
        exSummaryView.setRm204Code(exSummary.getRm204Code());
        exSummaryView.setRm204Remark(exSummary.getRm204Remark());
        exSummaryView.setRm020Code(exSummary.getRm020Code());
        exSummaryView.setRm020Remark(exSummary.getRm020Remark());

        //UW Decision and Explanation
        exSummaryView.setApproveAuthority(exSummary.getApproveAuthority());
        if (exSummaryView.getApproveAuthority() == null) {
            exSummaryView.setApproveAuthority(new AuthorizationDOA());
        }

        exSummaryView.setDecision(exSummary.getDecision());
        exSummaryView.setUwCode(exSummary.getUwCode());

        List<ExSumReasonView> exSumReasonViewList = new ArrayList<ExSumReasonView>();
        if(exSummary.getDeviateCode() != null && exSummary.getDeviateCode().size() > 0){
            for(ExSumDeviate deviate : exSummary.getDeviateCode()){
                ExSumReasonView exSumReasonView = new ExSumReasonView();
                exSumReasonView.setId((int)deviate.getId());
                exSumReasonView.setCode(deviate.getDeviateCode().getCode());
                exSumReasonView.setDescription(deviate.getDeviateCode().getDescription());
                exSumReasonViewList.add(exSumReasonView);
            }
        }
        exSummaryView.setDeviateCode(exSumReasonViewList);

        //UW Comment
        exSummaryView.setUwComment(exSummary.getUwComment());

        return exSummaryView;
    }

    public List<ExSumDeviate> transformDeviateToModel(List<ExSumReasonView> exSumReasonViewList,long exSummaryId){
        List<ExSumDeviate> exSumDeviateList = new ArrayList<ExSumDeviate>();
        for(ExSumReasonView ed : exSumReasonViewList){
            ExSumDeviate deviate = new ExSumDeviate();

//            Add New Only
//            deviate.setId(ed.getId());

            Reason reason = reasonDAO.getByCode(ed.getCode());
            deviate.setDeviateCode(reason);

            ExSummary exSummary = new ExSummary();
            exSummary.setId(exSummaryId);
            deviate.setExSummary(exSummary);

            exSumDeviateList.add(deviate);
        }
        return exSumDeviateList;
    }

    public ExSumBusinessInfoView transformBizInfoSumToExSumBizView(BizInfoSummaryView bizInfoSummaryView, QualitativeView qualitativeView, BigDecimal bizSize){
        ExSumBusinessInfoView exSumBusinessInfoView = new ExSumBusinessInfoView();

        exSumBusinessInfoView.setNetFixAsset(bizInfoSummaryView.getNetFixAsset());
        exSumBusinessInfoView.setNoOfEmployee(bizInfoSummaryView.getNoOfEmployee());
        exSumBusinessInfoView.setBizProvince(bizInfoSummaryView.getProvince().getName());

        if(bizInfoSummaryView.getBizInfoDetailViewList() != null && bizInfoSummaryView.getBizInfoDetailViewList().size() > 0) {
            for(BizInfoDetailView bd : bizInfoSummaryView.getBizInfoDetailViewList()){
                if(bd.getIsMainDetail() == 1){
                    exSumBusinessInfoView.setBizType(bd.getBizType().getDescription());
                    exSumBusinessInfoView.setBizGroup(bd.getBizGroup().getDescription());
                    exSumBusinessInfoView.setBizCode(bd.getBizCode());
                    exSumBusinessInfoView.setBizDesc(bd.getBizDesc().getName());
                }
            }
        }

        if(qualitativeView != null){
            exSumBusinessInfoView.setQualitativeClass(qualitativeView.getQualityResult());
        } else {
            exSumBusinessInfoView.setQualitativeClass("");
        }

//        If Borrower is Juristic use Customer Info Detail else if Borrower is Individual use Bank Statement Summary
        exSumBusinessInfoView.setBizSize(bizSize);

        //todo:income factor percent
//        exSumBusinessInfoView.setBDM();
//        exSumBusinessInfoView.setUW();
        exSumBusinessInfoView.setAR(bizInfoSummaryView.getSumWeightAR());
        exSumBusinessInfoView.setAP(bizInfoSummaryView.getSumWeightAP());
        exSumBusinessInfoView.setINV(bizInfoSummaryView.getSumWeightINV());

        return exSumBusinessInfoView;
    }

    public ExSumAccountMovementView transformBankStmtToExSumBizView(BankStatement bankStatement){
        ExSumAccountMovementView exSumAccountMovementView = new ExSumAccountMovementView();

        exSumAccountMovementView.setOdLimit(bankStatement.getAvgLimit());
        exSumAccountMovementView.setUtilization(bankStatement.getAvgUtilizationPercent());
        exSumAccountMovementView.setSwing(bankStatement.getAvgSwingPercent());
        exSumAccountMovementView.setOverLimitTimes(bankStatement.getOverLimitTimes());
        exSumAccountMovementView.setOverLimitDays(bankStatement.getOverLimitDays());
        exSumAccountMovementView.setChequeReturn(bankStatement.getChequeReturn());
        exSumAccountMovementView.setCashFlow(bankStatement.getAvgIncomeGross());
        exSumAccountMovementView.setCashFlowLimit(bankStatement.getAvgGrossInflowPerLimit());
        exSumAccountMovementView.setTradeChequeReturnAmount(bankStatement.getTdChequeReturnAmount());
        exSumAccountMovementView.setTradeChequeReturnPercent(bankStatement.getTdChequeReturnPercent());

        return exSumAccountMovementView;
    }
}
