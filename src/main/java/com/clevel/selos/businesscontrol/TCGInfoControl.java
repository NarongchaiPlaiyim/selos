package com.clevel.selos.businesscontrol;

import com.clevel.selos.dao.master.PotentialCollateralDAO;
import com.clevel.selos.dao.master.TCGCollateralTypeDAO;
import com.clevel.selos.dao.relation.PotentialColToTCGColDAO;
import com.clevel.selos.dao.working.BasicInfoDAO;
import com.clevel.selos.dao.working.TCGDAO;
import com.clevel.selos.dao.working.TCGDetailDAO;
import com.clevel.selos.dao.working.WorkCaseDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.RadioValue;
import com.clevel.selos.model.db.master.PotentialCollateral;
import com.clevel.selos.model.db.master.TCGCollateralType;
import com.clevel.selos.model.db.master.User;
import com.clevel.selos.model.db.relation.PotentialColToTCGCol;
import com.clevel.selos.model.db.working.BasicInfo;
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
    public void TCGInfoControl() {

    }

    public void onSaveTCGToDB(TCGView tcgView, List<TCGDetailView> tcgDetailViewList, Long workCaseId) {
        log.info("onEditTCGToDB begin");
        log.info("workCaseId {} ", workCaseId);
        WorkCase workCase = workCaseDAO.findById(workCaseId);
        User user = getCurrentUser();

        toCalculateLtvValue(workCaseId, tcgView, tcgDetailViewList);

        TCG tcg = tcgTransform.transformTCGViewToModel(tcgView, workCase, user);
        TCGDAO.persist(tcg);
        log.info("persist tcg");


        List<TCGDetail> tcgDetailListToDelete = TCGDetailDAO.findTCGDetailByTcgId(tcg.getId());
        log.info("tcgDetailListToDelete :: {}", tcgDetailListToDelete.size());

        if (tcgDetailListToDelete.size() > 0) {
            TCGDetailDAO.delete(tcgDetailListToDelete);
            log.info("delete tcgDetailListToDelete");
        }

        List<TCGDetail> tcgDetailList = tcgDetailTransform.transformTCGDetailViewToModel(tcgDetailViewList, tcg);
        TCGDetailDAO.persist(tcgDetailList);
        log.info("persist tcgDetailList");


    }


    public TCGView getTcgView(long workCaseId) {
        log.info("getTcgView :: workCaseId  :: {}", workCaseId);
        TCGView tcgView = null;

        WorkCase workCase = workCaseDAO.findById(workCaseId);
        log.info("getTcgView :: workCase AppNumber :: {}", workCase.getAppNumber());
        if (workCase != null) {
            TCG tcg = TCGDAO.findByWorkCase(workCase);

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

        List<TCGDetail> TCGDetailList = TCGDetailDAO.findTCGDetailByTcgId(tcgView.getId());

        if (TCGDetailList.size() > 0) {
            tcgDetailViewList = tcgDetailTransform.transformTCGDetailModelToView(TCGDetailList);
        }

        return tcgDetailViewList;
    }

    public void toCalculateLtvValue(long workCaseId, TCGView tcgView, List<TCGDetailView> tcgDetailViewList) {
        log.info("toCalculateLtvValue LTV Value of all collateral ::  ");

        for (TCGDetailView tcgDetailView : tcgDetailViewList) {

            BigDecimal ltvValueBig = BigDecimal.ZERO;
            if (tcgDetailView.getPotentialCollateral().getId() != 0 && tcgDetailView.getTcgCollateralType().getId() != 0) {
                BigDecimal ltvPercentBig = null;
                PotentialCollateral potentialCollateral = potentialCollateralDAO.findById(tcgDetailView.getPotentialCollateral().getId());
                TCGCollateralType tcgCollateralType = tcgCollateralTypeDAO.findById(tcgDetailView.getTcgCollateralType().getId());

                PotentialColToTCGCol potentialColToTCGCol = potentialColToTCGColDAO.getPotentialColToTCGCol(potentialCollateral, tcgCollateralType);
                log.info("potentialColToTCGCol.getId() ::: {}", potentialColToTCGCol.getId());

                BasicInfo basicInfo = basicInfoDAO.findByWorkCaseId(workCaseId);


                if (Util.isTrue(basicInfo.getProductGroup().getSpecialLTV())) {
                    ltvPercentBig = potentialColToTCGCol.getRetentionLTV();
                }

                if (ltvPercentBig == null) {

                    if (Util.isTrue(basicInfo.getExistingSMECustomer()) &&
                            Util.isTrue(basicInfo.getPassAnnualReview()) &&
                            Util.isTrue(basicInfo.getRequestLoanWithSameName()) &&
                            Util.isTrue(basicInfo.getHaveLoanInOneYear()) &&
                            (basicInfo.getSbfScore() != null && basicInfo.getSbfScore().getScore() <= 15)) {
                        ltvPercentBig = potentialColToTCGCol.getTenPercentLTV();
                        log.info("getTenPercentLTV :::::::");
                    } else {
                        ltvPercentBig = potentialColToTCGCol.getPercentLTV();
                        log.info("getPercentLTV ::::::: ");
                    }
                }

                if (ltvPercentBig != null && tcgDetailView != null) {
                    log.info("ltvPercent :: {} ", ltvPercentBig);
                    ltvValueBig = tcgDetailView.getAppraisalAmount().multiply(ltvPercentBig);
                }
            }

            tcgDetailView.setLtvValue(ltvValueBig);

        }

        tcgView.setSumAppraisalAmount(toCalculateSumAppraisalValue(tcgDetailViewList));
        tcgView.setSumLtvValue(toCalculateSumLtvValue(tcgDetailViewList));
        tcgView.setSumInThisAppraisalAmount(toCalculateSumAppraisalInThis(tcgDetailViewList));
        tcgView.setSumInThisLtvValue(toCalculateSumLtvInThis(tcgDetailViewList));

        BigDecimal collateralRuleResult = toCalCollateralRuleResult(tcgView);
        log.info("collateralRuleResult :: {} ", collateralRuleResult);
        tcgView.setCollateralRuleResult(collateralRuleResult);

        BigDecimal requestTCGAmount = toCalRequestTCGAmount(tcgView);
        log.info("requestTCGAmount :: {} ", requestTCGAmount);
        tcgView.setRequestTCGAmount(requestTCGAmount);
    }


//    public void toCalculateLtvValue(List<TCGDetailView> tcgDetailViewList, Long workCaseId) {
//
//        log.info("Calculate LTV Value tcgDetailViewList{}, workCaseId{}", tcgDetailViewList.size(), workCaseId);
//        BigDecimal ltvPercentBig = null;
//        BigDecimal ltvValueBig = BigDecimal.ZERO;
//
//        for (TCGDetailView tcgDetailView : tcgDetailViewList) {
//            if (tcgDetailView.getPotentialCollateral().getId() != 0 && tcgDetailView.getTcgCollateralType().getId() != 0) {
//
//                PotentialCollateral potentialCollateral = potentialCollateralDAO.findById(tcgDetailView.getPotentialCollateral().getId());
//                TCGCollateralType tcgCollateralType = tcgCollateralTypeDAO.findById(tcgDetailView.getTcgCollateralType().getId());
//
//                PotentialColToTCGCol potentialColToTCGCol = potentialColToTCGColDAO.getPotentialColToTCGCol(potentialCollateral, tcgCollateralType);
//                log.info("potentialColToTCGCol.getId() ::: {}", potentialColToTCGCol.getId());
//
//                BasicInfo basicInfo = basicInfoDAO.findByWorkCaseId(workCaseId);
//
//                if (Util.isTrue(basicInfo.getProductGroup().getSpecialLTV())) {
//                    ltvPercentBig = potentialColToTCGCol.getRetentionLTV();
//                }
//
//                if (ltvPercentBig == null) {
//                    if (Util.isTrue(basicInfo.getExistingSMECustomer()) &&
//                            Util.isTrue(basicInfo.getPassAnnualReview()) &&
//                            Util.isTrue(basicInfo.getRequestLoanWithSameName()) &&
//                            Util.isTrue(basicInfo.getHaveLoanInOneYear()) &&
//                            (basicInfo.getSbfScore() != null && basicInfo.getSbfScore().getScore() <= 15)) {
//                        ltvPercentBig = potentialColToTCGCol.getTenPercentLTV();
//                    } else {
//                        ltvPercentBig = potentialColToTCGCol.getPercentLTV();
//                    }
//                }
//
//                if (ltvPercentBig != null && tcgDetailView != null) {
//                    log.info("ltvPercent :: {} ", ltvPercentBig);
//                    ltvValueBig = tcgDetailView.getAppraisalAmount().multiply(ltvPercentBig);
//                }
//            }
//
//            tcgDetailView.setLtvValue(ltvValueBig);
//        }
//
////        return ltvValueBig;
//    }


    public BigDecimal toCalculateSumAppraisalValue(List<TCGDetailView> TCGDetailViewList) {
        BigDecimal sum = new BigDecimal(0);

        for (TCGDetailView tcgDetailView : TCGDetailViewList) {
            if (tcgDetailView != null) {
                sum = sum.add(tcgDetailView.getAppraisalAmount());
            }
        }

        log.info("sum ::: {} ", sum);
        return sum;
    }


    public BigDecimal toCalculateSumLtvValue(List<TCGDetailView> TCGDetailViewList) {
        BigDecimal sum = new BigDecimal(0);

        for (TCGDetailView tcgDetailView : TCGDetailViewList) {
            if (tcgDetailView != null) {
                sum = sum.add(tcgDetailView.getLtvValue());
            }
        }

        log.info("sum ::: {} ", sum);
        return sum;
    }

    public BigDecimal toCalculateSumAppraisalInThis(List<TCGDetailView> TCGDetailViewList) {
        BigDecimal sum = new BigDecimal(0);

        for (TCGDetailView tcgDetailView : TCGDetailViewList) {
            log.info("tcgDetailView.getProposeInThisRequest() :: {}", tcgDetailView.getProposeInThisRequest());
            if (tcgDetailView != null) {
                if (tcgDetailView.getProposeInThisRequest() == RadioValue.YES.value()) {
                    sum = sum.add(tcgDetailView.getAppraisalAmount());
                }
            }
        }
        log.info("sum ::: {} ", sum);

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
        log.info("sum ::: {} ", sum);

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
            log.info("tcgView.getRequestLimitRequiredTCG() ::: {} ", tcgView.getRequestLimitRequiredTCG());
            log.info("tcgView.getRequestLimitNotRequiredTCG() ::: {} ", tcgView.getRequestLimitNotRequiredTCG());
            log.info("tcgView.getExistingLoanRatioUnderSameCollateral() ::: {} ", tcgView.getExistingLoanRatioUnderSameCollateral());
            log.info("tcgView.getExistingLoanRatioNotUnderSameCollateral() ::: {} ", tcgView.getExistingLoanRatioNotUnderSameCollateral());
            sumAdd = num1.add(num2).add(num3).add(num4);
            log.info("SUM After add :: {}", sumAdd);
            log.info("tcgView.getSumAppraisalAmount() :: {}", tcgView.getSumAppraisalAmount());
            sumAppraisalDivide = Util.divide(tcgView.getSumAppraisalAmount(), sumAdd);
            sumAppraisalAmount = Util.divide(sumAppraisalDivide, BigDecimal.valueOf(100));

            log.info("sumAppraisalAmount ::: {} ", sumAppraisalAmount);

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
            log.info("sumRequestTCGAmount :: {}", sumRequestTCGAmount);
            requestTCGAmount = new BigDecimal(sumRequestTCGAmount);
        }
        return requestTCGAmount;
    }

    // edit calculate

}
