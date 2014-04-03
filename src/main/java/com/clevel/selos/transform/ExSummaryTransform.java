package com.clevel.selos.transform;

import com.clevel.selos.businesscontrol.BizInfoSummaryControl;
import com.clevel.selos.dao.master.AuthorizationDOADAO;
import com.clevel.selos.dao.master.ReasonDAO;
import com.clevel.selos.integration.corebanking.model.CustomerInfo;
import com.clevel.selos.model.db.master.*;
import com.clevel.selos.model.db.working.*;
import com.clevel.selos.model.view.*;
import com.clevel.selos.util.FacesUtil;
import com.clevel.selos.util.Util;

import javax.inject.Inject;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ExSummaryTransform extends Transform {
    @Inject
    private AuthorizationDOADAO authorizationDOADAO;
    @Inject
    private ReasonDAO reasonDAO;

    @Inject
    private BizInfoSummaryControl bizInfoSummaryControl;

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

    public ExSumBusinessInfoView transformBizInfoSumToExSumBizView(BizInfoSummaryView bizInfoSummaryView, QualitativeView qualitativeView, BigDecimal bizSize, ExSummary exSummary){
        ExSumBusinessInfoView exSumBusinessInfoView = new ExSumBusinessInfoView();

        exSumBusinessInfoView.setNetFixAsset(bizInfoSummaryView.getNetFixAsset());
        exSumBusinessInfoView.setNoOfEmployee(bizInfoSummaryView.getNoOfEmployee());
        exSumBusinessInfoView.setBizProvince(bizInfoSummaryView.getProvince().getName());

        if(qualitativeView != null){
            exSumBusinessInfoView.setQualitativeClass(qualitativeView.getQualityResult());
        } else {
            exSumBusinessInfoView.setQualitativeClass("");
        }

//        If Borrower is Juristic use Customer Info Detail else if Borrower is Individual use Bank Statement Summary
        exSumBusinessInfoView.setBizSize(bizSize);

        exSumBusinessInfoView.setAR(bizInfoSummaryView.getSumWeightAR());
        exSumBusinessInfoView.setAP(bizInfoSummaryView.getSumWeightAP());
        exSumBusinessInfoView.setINV(bizInfoSummaryView.getSumWeightINV());

        if(exSummary != null && exSummary.getId() != 0){
            exSumBusinessInfoView.setBDM(exSummary.getIncomeFactorBDM());
            exSumBusinessInfoView.setUW(exSummary.getIncomeFactorUW());
        }

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
