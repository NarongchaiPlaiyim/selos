package com.clevel.selos.businesscontrol;

import com.clevel.selos.dao.master.TCGCollateralTypeDAO;
import com.clevel.selos.dao.working.TcgDAO;
import com.clevel.selos.dao.working.TcgDetailDAO;
import com.clevel.selos.dao.working.WorkCaseDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.master.TCGCollateralType;
import com.clevel.selos.model.db.master.User;
import com.clevel.selos.model.db.working.TCG;
import com.clevel.selos.model.db.working.TCGDetail;
import com.clevel.selos.model.db.working.WorkCase;
import com.clevel.selos.model.view.TCGDetailView;
import com.clevel.selos.model.view.TCGView;
import com.clevel.selos.transform.TCGDetailTransform;
import com.clevel.selos.transform.TCGTransform;
import com.clevel.selos.util.Util;
import org.slf4j.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.List;


@Stateless
public class TCGInfoControl extends BusinessControl {
    @Inject
    @SELOS
    private Logger log;

    @Inject
    TCGDetailTransform tcgDetailTransform;
    @Inject
    TCGTransform tcgTransform;

    @Inject
    TcgDAO tcgDAO;
    @Inject
    TcgDetailDAO tcgDetailDAO;
    @Inject
    WorkCaseDAO workCaseDAO;
    @Inject
    TCGCollateralTypeDAO tcgCollateralTypeDAO;

    @Inject
    public void TCGInfoControl() {

    }

    public void onSaveTCGToDB(TCGView tcgView, List<TCGDetailView> tcgDetailViewList, Long workCaseId) {

        log.info("onSaveTCGToDB begin");
        log.info("workCaseId {} ", workCaseId);
        log.info("tcgView  {} ", tcgView.toString());
        WorkCase workCase = workCaseDAO.findById(workCaseId);
        User user = getCurrentUser();
        TCG tcg = tcgTransform.transformTCGViewToModel(tcgView, workCase, user);
        log.info("transform comeback {} ", tcg.toString());
        tcgDAO.persist(tcg);
        log.info("persist tcg");
        List<TCGDetail> tcgDetailList = tcgDetailTransform.transformTCGDetailViewToModel(tcgDetailViewList, tcg);
        tcgDetailDAO.persist(tcgDetailList);
    }

    public void onEditTCGToDB(TCGView tcgView, List<TCGDetailView> tcgDetailViewList, Long workCaseId) {

        log.info("onEditTCGToDB begin");
        log.info("workCaseId {} ", workCaseId);
        WorkCase workCase = workCaseDAO.findById(workCaseId);
        User user = getCurrentUser();
        TCG tcg = tcgTransform.transformTCGViewToModel(tcgView, workCase, user);
        tcgDAO.persist(tcg);
        log.info("persist tcg");

        List<TCGDetail> tcgDetailListToDelete = tcgDetailDAO.findTCGDetailByTcgId(tcg.getId());
        log.info("tcgDetailListToDelete :: {}", tcgDetailListToDelete.size());
        tcgDetailDAO.delete(tcgDetailListToDelete);
        log.info("delete tcgDetailListToDelete");

        List<TCGDetail> tcgDetailList = tcgDetailTransform.transformTCGDetailViewToModel(tcgDetailViewList, tcg);
        tcgDetailDAO.persist(tcgDetailList);
        log.info("persist tcgDetailList");

    }

    public TCGView getTcgView(long workCaseId) {
        log.info("getTcgView :: workCaseId  :: {}", workCaseId);
        TCGView tcgView = null;

        WorkCase workCase = workCaseDAO.findById(workCaseId);
        log.info("getTcgView :: workCase AppNumber :: {}", workCase.getAppNumber());
        if (workCase != null) {
            TCG tcg = tcgDAO.findByWorkCase(workCase);

            if (tcg != null) {
                log.info("tcg :: {} ", tcg.getId());
                tcgView = tcgTransform.transformTCGToTcgView(tcg);
            }
        }

        return tcgView;
    }

    public List<TCGDetailView> getTcgDetailListView(TCGView tcgView) {
        log.info("getTcgDetailListView :: tcgId  :: {}", tcgView.getId());
        List<TCGDetailView> tcgDetailViewList = null;

        List<TCGDetail> TCGDetailList = tcgDetailDAO.findTCGDetailByTcgId(tcgView.getId());

        if (TCGDetailList.size() > 0) {
            tcgDetailViewList = tcgDetailTransform.transformTCGDetailModelToView(TCGDetailList);
        }

        return tcgDetailViewList;
    }


    public BigDecimal toCalculateLtvValue(TCGDetailView tcgDetailView) {
        double ltvValue = 0;
        double ltvPercent = 0;
        BigDecimal ltvValueBig = BigDecimal.ZERO;

        TCGCollateralType tcgCollateralType = tcgCollateralTypeDAO.findById(tcgDetailView.getTcgCollateralType().getId());

        if (tcgCollateralType != null && tcgDetailView != null) {
            ltvPercent = tcgCollateralType.getPercentLTV();
            log.info("ltvPercent :: {} ", ltvPercent);
            ltvValue = tcgDetailView.getAppraisalAmount().doubleValue() * ltvPercent;
            ltvValueBig = new BigDecimal(ltvValue);
        }

        return ltvValueBig;
    }


    public BigDecimal toCalculateSumValue(List<TCGDetailView> TCGDetailViewList, String typeAmt) {
        BigDecimal sum = new BigDecimal(0);

        for (TCGDetailView tcgDetailView : TCGDetailViewList) {
            if (tcgDetailView != null && typeAmt != "") {
                if (typeAmt.equals("Appraisal")) {
                    sum = sum.add(tcgDetailView.getAppraisalAmount());
                } else if (typeAmt.equals("LTV")) {
                    sum = sum.add(tcgDetailView.getLtvValue());
                } else {
                    sum = new BigDecimal(0);
                }
            }
        }

        log.info("sum ::: {} ", sum);

        return sum;
    }

    public BigDecimal toCalculateSumValueInThis(List<TCGDetailView> TCGDetailViewList, String typeAmt) {
        BigDecimal sum = new BigDecimal(0);

        for (TCGDetailView tcgDetailView : TCGDetailViewList) {
            if(tcgDetailView != null && typeAmt != "") {
                if (typeAmt.equals("Appraisal")) {
                    if (tcgDetailView.getProposeInThisRequest() == 2) {
                        sum = sum.add(tcgDetailView.getAppraisalAmount());
                    }
                } else if (typeAmt.equals("LTV")) {
                    if (tcgDetailView.getProposeInThisRequest() == 2) {
                        sum = sum.add(tcgDetailView.getLtvValue());
                    }
                } else {
                    sum = new BigDecimal(0);
                }
            }

        }
        log.info("sum ::: {} ", sum);

        return sum;
    }

    public BigDecimal toCalCollateralRuleResult(TCGView tcgView) {
        BigDecimal sumAdd = BigDecimal.ZERO;
        BigDecimal sumAppraisalDivide = BigDecimal.ZERO;
        BigDecimal sumAppraisalAmount = BigDecimal.ZERO;

        if(tcgView!=null){
            BigDecimal num1 = tcgView.getRequestLimitRequiredTCG();
            BigDecimal num2 = tcgView.getRequestLimitNotRequiredTCG();
            BigDecimal num3 = tcgView.getExistingLoanRatioUnderSameCollateral();
            BigDecimal num4 = tcgView.getExistingLoanRatioNotUnderSameCollateral();
            log.info("tcgView.getRequestLimitRequiredTCG() ::: {} ", tcgView.getRequestLimitRequiredTCG());
            log.info("tcgView.getRequestLimitNotRequiredTCG() ::: {} ", tcgView.getRequestLimitNotRequiredTCG());
            log.info("tcgView.getExistingLoanRatioUnderSameCollateral() ::: {} ", tcgView.getExistingLoanRatioUnderSameCollateral());
            log.info("tcgView.getExistingLoanRatioNotUnderSameCollateral() ::: {} ", tcgView.getExistingLoanRatioNotUnderSameCollateral());
            sumAdd = num1.add(num2).add(num3).add(num4);
            log.info("SUM After add :: {}",sumAdd);
            log.info("tcgView.getSumAppraisalAmount() :: {}", tcgView.getSumAppraisalAmount());
            sumAppraisalDivide = Util.divide(tcgView.getSumAppraisalAmount(),sumAdd);
            sumAppraisalAmount = Util.divide(sumAppraisalDivide,BigDecimal.valueOf(100));

            log.info("sumAppraisalAmount ::: {} ", sumAppraisalAmount);

        }
        return sumAppraisalAmount;
    }

    public BigDecimal toCalRequestTCGAmount(TCGView tcgView) {
        Double sumRequestTCGAmount = 0.00;
        BigDecimal sumAdd = BigDecimal.ZERO;
        BigDecimal requestTCGAmount = BigDecimal.ZERO;

        if(tcgView != null){
            BigDecimal num1 = tcgView.getExistingLoanRatioUnderSameCollateral();
            BigDecimal num2 = tcgView.getRequestLimitRequiredTCG();
            BigDecimal num3 = tcgView.getSumInThisLtvValue();
            BigDecimal num4 = tcgView.getTcbFloodAmount();

            sumAdd = num1.add(num2);
            sumRequestTCGAmount = sumAdd.doubleValue() - num3.doubleValue() - num4.doubleValue();
            log.info("sumRequestTCGAmount :: {}", sumRequestTCGAmount);
            requestTCGAmount = new BigDecimal(sumRequestTCGAmount);
        }
        return requestTCGAmount;
    }

    // edit calculate

}
