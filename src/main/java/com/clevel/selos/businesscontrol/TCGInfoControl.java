package com.clevel.selos.businesscontrol;

import com.clevel.selos.dao.master.PotentialCollateralDAO;
import com.clevel.selos.dao.master.TCGCollateralTypeDAO;
import com.clevel.selos.dao.relation.PotentialColToTCGColDAO;
import com.clevel.selos.dao.working.*;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.RadioValue;
import com.clevel.selos.model.db.master.PotentialCollateral;
import com.clevel.selos.model.db.master.TCGCollateralType;
import com.clevel.selos.model.db.master.User;
import com.clevel.selos.model.db.relation.PotentialColToTCGCol;
import com.clevel.selos.model.db.working.*;
import com.clevel.selos.model.view.TCGDetailView;
import com.clevel.selos.model.view.TCGInfoView;
import com.clevel.selos.model.view.TCGView;
import com.clevel.selos.transform.TCGDetailTransform;
import com.clevel.selos.transform.TCGInfoTransform;
import com.clevel.selos.transform.TCGTransform;
import com.clevel.selos.util.Util;
import org.slf4j.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
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
    TCGInfoTransform tcgInfoTransform;

    @Inject
    TCGDAO TCGDAO;
    @Inject
    TCGDetailDAO TCGDetailDAO;
    @Inject
    WorkCaseDAO workCaseDAO;
    @Inject
    TCGCollateralTypeDAO tcgCollateralTypeDAO;
    @Inject
    PotentialColToTCGColDAO potentialColToTCGColDAO;
    @Inject
    PotentialCollateralDAO potentialCollateralDAO;
    @Inject
    BasicInfoDAO basicInfoDAO;
    @Inject
    TCGInfoDAO tcgInfoDAO;

    @Inject
    public void TCGInfoControl() {

    }
    
    public TCGInfoView getTCGInfoView(long workCaseId){
    	TCGInfo tcgInfo = tcgInfoDAO.findByWorkCaseId(workCaseId);
    	return tcgInfoTransform.transformToView(tcgInfo);
    }
    
    public void onSaveTCGInfo(TCGInfoView tcgInfoView, long workCaseId, User user){
    	TCGInfo tcgInfo = tcgInfoDAO.findByWorkCaseId(workCaseId);
    	if (tcgInfo != null){
    		tcgInfo.setApprovedResult(tcgInfoView.getApprovedResult());
    		tcgInfo.setApproveDate(tcgInfoView.getApproveDate());
    		tcgInfo.setTcgSubmitDate(tcgInfoView.getTcgSubmitDate());
    		tcgInfo.setModifyBy(user);
    		tcgInfo.setModifyDate(new Date());
    	}else{
    		tcgInfo = new TCGInfo();
    		tcgInfo.setApprovedResult(tcgInfoView.getApprovedResult());
    		tcgInfo.setApproveDate(tcgInfoView.getApproveDate());
    		tcgInfo.setTcgSubmitDate(tcgInfoView.getTcgSubmitDate());	
    		WorkCase workCase = workCaseDAO.findById(workCaseId);
    		tcgInfo.setWorkCase(workCase);
    		tcgInfo.setCreateBy(user);
    		tcgInfo.setCreateDate(new Date());
    	}
    	tcgInfoDAO.persist(tcgInfo);
    	log.debug("persist tcgInfo");

        //--Update flag in WorkCase ( for check before submit )

    }

    public void saveTCGInfo(TCGView tcgView, List<TCGDetailView> tcgDetailViewList, long workCaseId) {
        log.debug("saveTCGInfo ::: begin....");
        log.debug("saveTCGInfo ::: workCaseId {} ", workCaseId);
        WorkCase workCase = workCaseDAO.findById(workCaseId);
        User user = getCurrentUser();

        calculateLTVValue(workCaseId, tcgView, tcgDetailViewList);

        TCG tcg = tcgTransform.transformToModel(tcgView, workCase, user);
        TCGDAO.persist(tcg);
        log.debug("saveTCGInfo ::: persist tcg : {}", tcg.getId());

        List<TCGDetail> tcgDetailListToDelete = TCGDetailDAO.findTCGDetailByTcgId(tcg.getId());
        log.debug("saveTCGInfo ::: tcgDetailListToDelete : {}", tcgDetailListToDelete.size());

        if(tcgDetailListToDelete.size() > 0)
            TCGDetailDAO.delete(tcgDetailListToDelete);

        List<TCGDetail> tcgDetailList = tcgDetailTransform.transformToModel(tcgDetailViewList, tcg);
        TCGDetailDAO.persist(tcgDetailList);
        log.debug("saveTCGInfo ::: persist tcgDetailList");

        //--Update flag in WorkCase ( for check before submit )
        workCase.setCaseUpdateFlag(1);
        workCaseDAO.persist(workCase);
    }


    public TCGView getTCGView(long workCaseId) {
        log.debug("getTCGView :: workCaseId  : {}", workCaseId);
        TCGView tcgView = null;
        TCG tcg = TCGDAO.findByWorkCaseId(workCaseId);

        if(!Util.isNull(tcg)){
            log.debug("getTCGView :: tcg : {} ", tcg.getId());
            tcgView = tcgTransform.transformToView(tcg);
        }

        return tcgView;
    }

    public List<TCGDetailView> getTcgDetailViewList(TCGView tcgView) {
        log.debug("getTcgDetailViewList :: tcgId  :: {}", tcgView.getId());
        List<TCGDetailView> tcgDetailViewList = new ArrayList<TCGDetailView>();

        List<TCGDetail> TCGDetailList = TCGDetailDAO.findTCGDetailByTcgId(tcgView.getId());

        if (!Util.isNull(TCGDetailList) && TCGDetailList.size() > 0) {
            tcgDetailViewList = tcgDetailTransform.transformToView(TCGDetailList);
        }

        return tcgDetailViewList;
    }

    //TODO find LTV Percent
    public void calculateLTVValue(long workCaseId, TCGView tcgView, List<TCGDetailView> tcgDetailViewList) {
        log.debug("calculateLTVValue LTV Value of all collateral ::  ");

        for (TCGDetailView tcgDetailView : tcgDetailViewList) {

            BigDecimal ltvValueBig = BigDecimal.ZERO;
            if (tcgDetailView.getPotentialCollateral().getId() != 0 && tcgDetailView.getTcgCollateralType().getId() != 0) {
                BigDecimal ltvPercentBig = null;
                PotentialCollateral potentialCollateral = potentialCollateralDAO.findById(tcgDetailView.getPotentialCollateral().getId());
                TCGCollateralType tcgCollateralType = tcgCollateralTypeDAO.findById(tcgDetailView.getTcgCollateralType().getId());

                if ((potentialCollateral != null) && (tcgCollateralType != null)) {
                    PotentialColToTCGCol potentialColToTCGCol = potentialColToTCGColDAO.getPotentialColToTCGCol(potentialCollateral, tcgCollateralType);
                    if (potentialColToTCGCol != null) {
                        log.debug("potentialColToTCGCol.getId() ::: {}", potentialColToTCGCol.getId());

                        BasicInfo basicInfo = basicInfoDAO.findByWorkCaseId(workCaseId);
                        log.debug("basicInfo ::: {}", basicInfo);

                        WorkCase workCase = workCaseDAO.findById(workCaseId);
                        log.debug("workCase ::: {}", workCase);

                        if (basicInfo != null && workCase != null) {
                            log.debug("workCase.getProductGroup ::: {}", workCase.getProductGroup());
                            if (workCase.getProductGroup() != null && Util.isTrue(workCase.getProductGroup().getSpecialLTV())) {
                                log.debug("getRetentionLTV :::::::");
                                if (potentialColToTCGCol.getRetentionLTV() != null) {
                                    ltvPercentBig = potentialColToTCGCol.getRetentionLTV();
                                } else {
                                    if (Util.isRadioTrue(basicInfo.getExistingSMECustomer()) &&
                                            Util.isRadioTrue(basicInfo.getPassAnnualReview()) &&
                                            Util.isRadioTrue(basicInfo.getRequestLoanWithSameName()) &&
                                            Util.isRadioTrue(basicInfo.getHaveLoanInOneYear()) &&
                                            (basicInfo.getSbfScore() != null && basicInfo.getSbfScore().getScore() <= 15)){
                                        log.debug("isSpecialLTV - getTenPercentLTV :::::::");
                                        ltvPercentBig = potentialColToTCGCol.getTenPercentLTV();
                                    } else {
                                        log.debug("isSpecialLTV - getPercentLTV ::::::: ");
                                        ltvPercentBig = potentialColToTCGCol.getPercentLTV();
                                    }
                                }
                            } else if (Util.isRadioTrue(basicInfo.getExistingSMECustomer()) &&
                                    Util.isRadioTrue(basicInfo.getPassAnnualReview()) &&
                                    Util.isRadioTrue(basicInfo.getRequestLoanWithSameName()) &&
                                    Util.isRadioTrue(basicInfo.getHaveLoanInOneYear()) &&
                                    (basicInfo.getSbfScore() != null && basicInfo.getSbfScore().getScore() <= 15)){
                                log.debug("getTenPercentLTV :::::::");
                                ltvPercentBig = potentialColToTCGCol.getTenPercentLTV();
                            } else {
                                log.debug("getPercentLTV ::::::: ");
                                ltvPercentBig = potentialColToTCGCol.getPercentLTV();
                            }
                        }

                        //multi
                        if (ltvPercentBig != null && tcgDetailView != null) {
                            log.debug("ltvPercent :: {} ", ltvPercentBig);
                            ltvValueBig = tcgDetailView.getAppraisalAmount().multiply(ltvPercentBig);
                        }
                    }
                }
            }

            tcgDetailView.setLtvValue(ltvValueBig);

        }

        tcgView.setSumAppraisalAmount(toCalculateSumAppraisalValue(tcgDetailViewList));
        tcgView.setSumLtvValue(toCalculateSumLtvValue(tcgDetailViewList));
        tcgView.setSumInThisAppraisalAmount(toCalculateSumAppraisalInThis(tcgDetailViewList));
        tcgView.setSumInThisLtvValue(toCalculateSumLtvInThis(tcgDetailViewList));

        BigDecimal collateralRuleResult = toCalCollateralRuleResult(tcgView);
        log.debug("collateralRuleResult :: {} ", collateralRuleResult);
        tcgView.setCollateralRuleResult(collateralRuleResult);

        BigDecimal requestTCGAmount = toCalRequestTCGAmount(tcgView);
        log.debug("requestTCGAmount :: {} ", requestTCGAmount);
        tcgView.setRequestTCGAmount(requestTCGAmount);
    }


    public BigDecimal toCalculateSumAppraisalValue(List<TCGDetailView> TCGDetailViewList) {
        BigDecimal sum = BigDecimal.ZERO;
        for (TCGDetailView tcgDetailView : TCGDetailViewList) {
            if (tcgDetailView != null) {
                sum = sum.add(tcgDetailView.getAppraisalAmount());
            }
        }
        return sum;
    }


    public BigDecimal toCalculateSumLtvValue(List<TCGDetailView> TCGDetailViewList) {
        BigDecimal sum = BigDecimal.ZERO;
        for (TCGDetailView tcgDetailView : TCGDetailViewList) {
            if (tcgDetailView != null) {
                sum = sum.add(tcgDetailView.getLtvValue());
            }
        }
        return sum;
    }

    public BigDecimal toCalculateSumAppraisalInThis(List<TCGDetailView> TCGDetailViewList) {
        BigDecimal sum = BigDecimal.ZERO;
        for (TCGDetailView tcgDetailView : TCGDetailViewList) {
            if (tcgDetailView != null) {
                if (tcgDetailView.getProposeInThisRequest() == RadioValue.YES.value()) {
                    sum = sum.add(tcgDetailView.getAppraisalAmount());
                }
            }
        }
        return sum;
    }

    public BigDecimal toCalculateSumLtvInThis(List<TCGDetailView> TCGDetailViewList) {
        BigDecimal sum = BigDecimal.ZERO;
        for (TCGDetailView tcgDetailView : TCGDetailViewList) {
            if (tcgDetailView != null) {
                if (tcgDetailView.getProposeInThisRequest() == RadioValue.YES.value()) {
                    sum = sum.add(tcgDetailView.getLtvValue());
                }
            }
        }
        return sum;
    }

    public BigDecimal toCalCollateralRuleResult(TCGView tcgView) {
        BigDecimal sumAdd = BigDecimal.ZERO;
        BigDecimal sumAppraisalMul = BigDecimal.ZERO;
        BigDecimal sumAppraisalAmount = BigDecimal.ZERO;

        if (tcgView != null) {
            BigDecimal num1 = tcgView.getRequestLimitRequiredTCG();
            BigDecimal num2 = tcgView.getRequestLimitNotRequiredTCG();
            BigDecimal num3 = tcgView.getExistingLoanRatioUnderSameCollateral();
            BigDecimal num4 = tcgView.getExistingLoanRatioNotUnderSameCollateral();
            log.debug("tcgView.getRequestLimitRequiredTCG() ::: {} ", tcgView.getRequestLimitRequiredTCG());
            log.debug("tcgView.getRequestLimitNotRequiredTCG() ::: {} ", tcgView.getRequestLimitNotRequiredTCG());
            log.debug("tcgView.getExistingLoanRatioUnderSameCollateral() ::: {} ", tcgView.getExistingLoanRatioUnderSameCollateral());
            log.debug("tcgView.getExistingLoanRatioNotUnderSameCollateral() ::: {} ", tcgView.getExistingLoanRatioNotUnderSameCollateral());
            sumAdd = num1.add(num2).add(num3).add(num4);
            log.debug("SUM After add :: {}", sumAdd);
            log.debug("tcgView.getSumAppraisalAmount() :: {}", tcgView.getSumAppraisalAmount());
            sumAppraisalAmount = Util.divide(tcgView.getSumAppraisalAmount(), sumAdd);
            sumAppraisalMul = Util.multiply(sumAppraisalAmount,Util.ONE_HUNDRED);
            if(sumAppraisalMul!=null){
                sumAppraisalMul=sumAppraisalMul.setScale(2, RoundingMode.HALF_UP);
            }
            log.debug("sumAppraisalMul ::: {} ", sumAppraisalMul);

        }
        return sumAppraisalMul;
    }

    public BigDecimal toCalRequestTCGAmount(TCGView tcgView) {
        Double sumRequestTCGAmount = 0.00;
        BigDecimal sumAdd = BigDecimal.ZERO;
        BigDecimal requestTCGAmount = BigDecimal.ZERO;

        if (tcgView != null) {
            BigDecimal num1 = tcgView.getExistingLoanRatioUnderSameCollateral();
            BigDecimal num2 = tcgView.getRequestLimitRequiredTCG();
            BigDecimal num3 = tcgView.getSumInThisLtvValue();
            BigDecimal num4 = tcgView.getTcbFloodAmount();

            sumAdd = num1.add(num2);
            sumRequestTCGAmount = sumAdd.doubleValue() - num3.doubleValue() - num4.doubleValue();
            log.debug("sumRequestTCGAmount :: {}", sumRequestTCGAmount);
            requestTCGAmount = new BigDecimal(sumRequestTCGAmount);
        }
        return requestTCGAmount;
    }

    // edit calculate

}
