package com.clevel.selos.businesscontrol;

import com.clevel.selos.dao.working.BankStatementSummaryDAO;
import com.clevel.selos.dao.working.BizInfoDetailDAO;
import com.clevel.selos.dao.working.BizInfoSummaryDAO;
import com.clevel.selos.dao.working.WorkCaseDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.StepValue;
import com.clevel.selos.model.db.master.User;
import com.clevel.selos.model.db.working.BankStatementSummary;
import com.clevel.selos.model.db.working.BizInfoDetail;
import com.clevel.selos.model.db.working.BizInfoSummary;
import com.clevel.selos.model.db.working.WorkCase;
import com.clevel.selos.model.view.BankStmtSummaryView;
import com.clevel.selos.model.view.BizInfoDetailView;
import com.clevel.selos.model.view.BizInfoSummaryView;
import com.clevel.selos.transform.BizInfoDetailTransform;
import com.clevel.selos.transform.BizInfoSummaryTransform;
import com.clevel.selos.util.FacesUtil;
import com.clevel.selos.util.Util;
import org.joda.time.DateTime;
import org.slf4j.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Stateless
public class BizInfoSummaryControl extends BusinessControl {
	@Inject
    @SELOS
    private Logger log;

    @Inject
    BizInfoSummaryDAO bizInfoSummaryDAO;
    @Inject
    BizInfoDetailDAO bizInfoDetailDAO;
    @Inject
    WorkCaseDAO workCaseDAO;
    @Inject
    BankStatementSummaryDAO bankStmtSummaryDAO;
    @Inject
    BankStmtControl bankStmtControl;

    @Inject
    BizInfoDetailTransform bizInfoDetailTransform;
    @Inject
    BizInfoSummaryTransform bizInfoSummaryTransform;

    @Inject
    public BizInfoSummaryControl(){
    }

    public void onSaveBizSummaryToDB(BizInfoSummaryView bizInfoSummaryView, long workCaseId) {
        log.info("onSaveBizSummaryToDB begin :: bizInfoSummaryView : {} workCaseId : {} ",bizInfoSummaryView ,workCaseId);

        log.info("find workCase begin");
        WorkCase workCase = workCaseDAO.findById(workCaseId);
        log.info("find workCase end workCase is " + workCase.toString());

        User user = getCurrentUser();
        if (bizInfoSummaryView.getId() == 0) {
            bizInfoSummaryView.setCreateBy(user);
            bizInfoSummaryView.setCreateDate(DateTime.now().toDate());
        }
        bizInfoSummaryView.setModifyBy(user);

        BizInfoSummary bizInfoSummary = bizInfoSummaryTransform.transformToModel(bizInfoSummaryView);
        bizInfoSummary.setWorkCase(workCase);

        log.info("bizInfoSummaryDAO.persist begin " + bizInfoSummary.toString());

        bizInfoSummaryDAO.persist(bizInfoSummary);
        log.info("onSaveBizSummaryToDB end");
    }


    public BizInfoSummaryView onGetBizInfoSummaryByWorkCase(long workCaseId) {
        log.info("onGetBizInfoSummaryByWorkCase workCaseId is " + workCaseId);

        BizInfoSummary bizInfoSummary;
        BizInfoSummaryView bizInfoSummaryView;

        bizInfoSummary = bizInfoSummaryDAO.findByWorkCaseId(workCaseId);

        if (bizInfoSummary != null) {
            bizInfoSummaryView = bizInfoSummaryTransform.transformToView(bizInfoSummary);
        } else {
            bizInfoSummaryView = null;
        }
        return bizInfoSummaryView;
    }

    public List<BizInfoDetailView> onGetBizInfoDetailViewByBizInfoSummary(long bizInfoSummaryID) {
        log.info("onGetBizInfoDetailViewByBizInfoSummary :: bizInfoSummaryID : {} ",bizInfoSummaryID);

        BizInfoSummary bizInfoSummary = bizInfoSummaryDAO.findById(bizInfoSummaryID);

        List<BizInfoDetail> bizInfoDetailList = bizInfoDetailDAO.findByBizInfoSummaryId(bizInfoSummary);

        List<BizInfoDetailView> bizInfoDetailViewList = bizInfoDetailTransform.transformToViewList(bizInfoDetailList);

        return bizInfoDetailViewList;
    }

    public List<BizInfoDetail> onGetBizInfoDetailByBizInfoSummary(long bizInfoSummaryID) {
        log.info("onGetBizInfoDetailByBizInfoSummary :: bizInfoSummaryID : {}",bizInfoSummaryID);

        List<BizInfoDetail> bizInfoDetailList;

        BizInfoSummary bizInfoSummary = bizInfoSummaryDAO.findById(bizInfoSummaryID);

        log.info("bizInfoSummaryID after findById " + bizInfoSummary);

        bizInfoDetailList = bizInfoDetailDAO.findByBizInfoSummaryId(bizInfoSummary);

        return bizInfoDetailList;
    }

    public BankStmtSummaryView getBankStmtSummary(long workCaseId){
        log.info("getBankStmtSummary :: workCaseId : {}",workCaseId);
        BankStmtSummaryView bankStmtSummaryView = new BankStmtSummaryView();
        BankStatementSummary bankStmtSummary = bankStmtSummaryDAO.findByWorkCaseId(workCaseId);

        if(bankStmtSummary != null){
            bankStmtSummaryView.setGrdTotalIncomeNetBDM(bankStmtSummary.getGrdTotalIncomeNetBDM());
            bankStmtSummaryView.setGrdTotalIncomeNetUW(bankStmtSummary.getGrdTotalIncomeNetUW());
        } else {
            bankStmtSummaryView.setGrdTotalIncomeNetBDM(BigDecimal.ZERO);
            bankStmtSummaryView.setGrdTotalIncomeNetUW(BigDecimal.ZERO);
        }

        return bankStmtSummaryView;
    }

    public void calByBankStatement(long workCaseId){
        log.info("calGrdTotalIncomeByBankStatement :: workCaseId : {}",workCaseId);
        long stepId = 0;

        HttpSession session = FacesUtil.getSession(true);

        if(session.getAttribute("stepId") != null){
            stepId = Long.parseLong(session.getAttribute("stepId").toString());
            log.debug("stepId : {}",stepId);
        }

        BizInfoSummary bizInfoSummary = bizInfoSummaryDAO.findByWorkCaseId(workCaseId);
        if(bizInfoSummary == null){
            bizInfoSummary = new BizInfoSummary();
        }

        //for set circulation , cal grd total income gross
        BankStatementSummary bankStatementSummary = bankStmtSummaryDAO.findByWorkCaseId(workCaseId);
        if (bankStatementSummary != null && bankStatementSummary.getGrdTotalIncomeGross() != null){
            bizInfoSummary.setCirculationAmount(bankStatementSummary.getGrdTotalIncomeGross());
        } else {
            bizInfoSummary.setCirculationAmount(BigDecimal.ZERO);
        }
        BizInfoSummaryView bizInfoSummaryView = bizInfoSummaryTransform.transformToView(bizInfoSummary);
        BizInfoSummaryView bizSumView = calSummaryTable(bizInfoSummaryView);
        bizInfoSummary = bizInfoSummaryTransform.transformToModel(bizSumView);

        //for set sum income amount
        BigDecimal income;
        BigDecimal twelve = BigDecimal.valueOf(12);
        BigDecimal oneHundred = BigDecimal.valueOf(100);
        BigDecimal calSumIncomeNet;
        BigDecimal sumIncomeNet;

        if(!Util.isNull(bankStatementSummary)){
            if(stepId >= StepValue.CREDIT_DECISION_UW1.value()){
                if (!Util.isNull(bankStatementSummary.getGrdTotalIncomeNetUW())){
                    income = bankStatementSummary.getGrdTotalIncomeNetUW();
                } else {
                    income = BigDecimal.ZERO;
                }
            } else {
                if (!Util.isNull(bankStatementSummary.getGrdTotalIncomeNetBDM())){
                    income = bankStatementSummary.getGrdTotalIncomeNetBDM();
                } else {
                    income = BigDecimal.ZERO;
                }
            }

            log.debug("income : {} ", income);

            if(!Util.isNull(income)){
                calSumIncomeNet = Util.multiply(income,twelve);
                sumIncomeNet = calSumIncomeNet.setScale(2,RoundingMode.HALF_UP);
            } else {
                sumIncomeNet = BigDecimal.ZERO;
            }
        } else {
            sumIncomeNet = BigDecimal.ZERO;
        }

        bizInfoSummary.setSumIncomeAmount(sumIncomeNet);

        WorkCase workCase = workCaseDAO.findById(workCaseId);
        bizInfoSummary.setWorkCase(workCase);

        bizInfoSummaryDAO.persist(bizInfoSummary);

        //for cal each detail
        if(bizInfoSummary.getId() != 0){
            List<BizInfoDetail> bizInfoDetailList = onGetBizInfoDetailByBizInfoSummary(bizInfoSummary.getId());
            for(BizInfoDetail bd : bizInfoDetailList){
                bd.setIncomeAmount(Util.divide(Util.multiply(sumIncomeNet,bd.getPercentBiz()),oneHundred));
            }
            bizInfoDetailDAO.persist(bizInfoDetailList);
        }
    }

    public BizInfoSummaryView calSummaryTable(BizInfoSummaryView bizInfoSummaryView){
        log.info("calSummaryTable begin");
        BigDecimal sumIncomeAmount = BigDecimal.ZERO ;
        BigDecimal productCostPercent = BigDecimal.ZERO ;
        BigDecimal operatingExpenseAmount = BigDecimal.ZERO ;
        BigDecimal profitMarginAmount;
        BigDecimal profitMarginPercent;
        BigDecimal operatingExpensePercent;
        BigDecimal earningsBeforeTaxAmount;
        BigDecimal earningsBeforeTaxPercent;
        BigDecimal reduceInterestAmount;
        BigDecimal reduceTaxAmount;
        BigDecimal reduceInterestPercent;
        BigDecimal reduceTaxPercent;
        BigDecimal netMarginAmount;
        BigDecimal netMarginPercent;
        BigDecimal hundred = new BigDecimal(100);

        if(!Util.isNull(bizInfoSummaryView.getCirculationAmount())){
            sumIncomeAmount = bizInfoSummaryView.getCirculationAmount();
        }

        if(!Util.isNull(bizInfoSummaryView.getProductionCostsPercentage())){
            productCostPercent = bizInfoSummaryView.getProductionCostsPercentage();
        }

        if(!Util.isNull(bizInfoSummaryView.getOperatingExpenseAmount())){
            operatingExpenseAmount = bizInfoSummaryView.getOperatingExpenseAmount();
        }

        profitMarginPercent = Util.subtract(hundred,productCostPercent);
        profitMarginAmount = Util.divide(Util.multiply(sumIncomeAmount,profitMarginPercent),hundred);

        bizInfoSummaryView.setProductionCostsAmount(Util.divide(Util.multiply(sumIncomeAmount,productCostPercent),100));
        bizInfoSummaryView.setProfitMarginPercentage(profitMarginPercent);
        bizInfoSummaryView.setProfitMarginAmount(profitMarginAmount);

        operatingExpensePercent = Util.divide(Util.multiply(operatingExpenseAmount,hundred),sumIncomeAmount);
        bizInfoSummaryView.setOperatingExpensePercentage(operatingExpensePercent);

        earningsBeforeTaxAmount = Util.subtract(profitMarginAmount,operatingExpenseAmount);
        earningsBeforeTaxPercent = Util.subtract(profitMarginPercent,operatingExpensePercent);

        bizInfoSummaryView.setEarningsBeforeTaxAmount(earningsBeforeTaxAmount);
        bizInfoSummaryView.setEarningsBeforeTaxPercentage(earningsBeforeTaxPercent);

        reduceInterestAmount = bizInfoSummaryView.getReduceInterestAmount();
        reduceTaxAmount = bizInfoSummaryView.getReduceTaxAmount();

        reduceInterestPercent = Util.divide(Util.multiply(reduceInterestAmount,hundred),sumIncomeAmount);
        reduceTaxPercent = Util.divide(Util.multiply(reduceTaxAmount,hundred),sumIncomeAmount);

        bizInfoSummaryView.setReduceInterestPercentage(reduceInterestPercent);
        bizInfoSummaryView.setReduceTaxPercentage(reduceTaxPercent);

        netMarginAmount = Util.subtract(Util.subtract(earningsBeforeTaxAmount,reduceInterestAmount),reduceTaxAmount);
        netMarginPercent = Util.subtract(Util.subtract(earningsBeforeTaxPercent,reduceInterestPercent),reduceTaxPercent);

        bizInfoSummaryView.setNetMarginAmount(netMarginAmount);
        bizInfoSummaryView.setNetMarginPercentage(netMarginPercent);

        log.info("calSummaryTable end - bizInfoSummaryView : {}",bizInfoSummaryView);
        return bizInfoSummaryView;
    }
}
