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
    }

    public void onSaveTCGToDB(TCGView tcgView, List<TCGDetailView> tcgDetailViewList, Long workCaseId) {
        log.debug("onEditTCGToDB begin");
        log.debug("workCaseId {} ", workCaseId);
        WorkCase workCase = workCaseDAO.findById(workCaseId);
        User user = getCurrentUser();

        toCalculateLtvValue(workCaseId, tcgView, tcgDetailViewList);

        TCG tcg = tcgTransform.transformTCGViewToModel(tcgView, workCase, user);
        TCGDAO.persist(tcg);
        log.debug("persist tcg");

        List<TCGDetail> tcgDetailListToDelete = TCGDetailDAO.findTCGDetailByTcgId(tcg.getId());
        log.debug("tcgDetailListToDelete :: {}", tcgDetailListToDelete.size());

        if (tcgDetailListToDelete.size() > 0) {
            TCGDetailDAO.delete(tcgDetailListToDelete);
            log.debug("delete tcgDetailListToDelete");
        }

        List<TCGDetail> tcgDetailList = tcgDetailTransform.transformTCGDetailViewToModel(tcgDetailViewList, tcg);
        TCGDetailDAO.persist(tcgDetailList);
        log.debug("persist tcgDetailList");


    }


    public TCGView getTcgView(long workCaseId) {
        log.debug("getTcgView :: workCaseId  :: {}", workCaseId);
        TCGView tcgView = null;

        WorkCase workCase = workCaseDAO.findById(workCaseId);
        log.debug("getTcgView :: workCase AppNumber :: {}", workCase.getAppNumber());
        if (workCase != null) {
            TCG tcg = TCGDAO.findByWorkCase(workCase);

            if (tcg != null) {
                log.debug("tcg :: {} ", tcg.getId());
                tcgView = tcgTransform.transformTCGToTcgView(tcg);
            }
        }

        return tcgView;
    }

    public List<TCGDetailView> getTcgDetailListView(TCGView tcgView) {
        log.debug("getTcgDetailListView :: tcgId  :: {}", tcgView.getId());
        List<TCGDetailView> tcgDetailViewList = null;

        List<TCGDetail> TCGDetailList = TCGDetailDAO.findTCGDetailByTcgId(tcgView.getId());

        if (TCGDetailList.size() > 0) {
            tcgDetailViewList = tcgDetailTransform.transformTCGDetailModelToView(TCGDetailList);
        }

        return tcgDetailViewList;
    }

    public void toCalculateLtvValue(long workCaseId, TCGView tcgView, List<TCGDetailView> tcgDetailViewList) {
        log.debug("toCalculateLtvValue LTV Value of all collateral ::  ");

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

                        if (basicInfo != null) {
                            log.debug("basicInfo.getProductGroup ::: {}", basicInfo.getProductGroup());
                            if (basicInfo.getProductGroup() != null && Util.isTrue(basicInfo.getProductGroup().getSpecialLTV())) {
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
        BigDecimal sum = new BigDecimal(0);

        for (TCGDetailView tcgDetailView : TCGDetailViewList) {
            if (tcgDetailView != null) {
                sum = sum.add(tcgDetailView.getAppraisalAmount());
            }
        }

        log.debug("sum ::: {} ", sum);
        return sum;
    }


    public BigDecimal toCalculateSumLtvValue(List<TCGDetailView> TCGDetailViewList) {
        BigDecimal sum = new BigDecimal(0);

        for (TCGDetailView tcgDetailView : TCGDetailViewList) {
            if (tcgDetailView != null) {
                sum = sum.add(tcgDetailView.getLtvValue());
            }
        }

        log.debug("sum ::: {} ", sum);
        return sum;
    }

    public BigDecimal toCalculateSumAppraisalInThis(List<TCGDetailView> TCGDetailViewList) {
        BigDecimal sum = new BigDecimal(0);

        for (TCGDetailView tcgDetailView : TCGDetailViewList) {
            log.debug("tcgDetailView.getProposeInThisRequest() :: {}", tcgDetailView.getProposeInThisRequest());
            if (tcgDetailView != null) {
                if (tcgDetailView.getProposeInThisRequest() == RadioValue.YES.value()) {
                    sum = sum.add(tcgDetailView.getAppraisalAmount());
                }
            }
        }
        log.debug("sum ::: {} ", sum);

        return sum;
    }

    public BigDecimal toCalculateSumLtvInThis(List<TCGDetailView> TCGDetailViewList) {
        BigDecimal sum = new BigDecimal(0);

        for (TCGDetailView tcgDetailView : TCGDetailViewList) {
            if (tcgDetailView != null) {
                if (tcgDetailView.getProposeInThisRequest() == RadioValue.YES.value()) {
                    sum = sum.add(tcgDetailView.getLtvValue());
                }
            }
        }
        log.debug("sum ::: {} ", sum);

        return sum;
    }

    public BigDecimal toCalCollateralRuleResult(TCGView tcgView) {
        BigDecimal sumAdd = BigDecimal.ZERO;
        BigDecimal sumAppraisalDivide = BigDecimal.ZERO;
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
            sumAppraisalDivide = Util.divide(tcgView.getSumAppraisalAmount(), sumAdd);
//            sumAppraisalAmount = Util.divide(sumAppraisalDivide, BigDecimal.valueOf(100));

            log.debug("sumAppraisalAmount ::: {} ", sumAppraisalAmount);

        }
        return sumAppraisalAmount;
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
