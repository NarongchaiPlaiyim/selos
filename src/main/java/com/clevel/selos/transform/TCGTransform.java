package com.clevel.selos.transform;

import com.clevel.selos.model.db.working.TCG;
import com.clevel.selos.model.db.working.WorkCase;
import com.clevel.selos.model.view.TCGView;


public class TCGTransform extends Transform {

    public TCG transformTCGViewToModel(TCGView tcgView, WorkCase workCase) {

        TCG tcg = new TCG();

        if (tcgView.getId() != 0) {
            tcg.setId(tcgView.getId());
        }
        tcg.setWorkCase(workCase);
        tcg.setActive(true);
        tcg.setCreateBy(tcgView.getCreateBy());
        tcg.setCreateDate(tcgView.getCreateDate());
        tcg.setModifyBy(tcgView.getModifyBy());
        tcg.setModifyDate(tcgView.getModifyDate());
        tcg.setCollateralRuleResult(tcgView.getCollateralRuleResult());
        tcg.setExistingLoanRatioUnderSameCollateral(tcgView.getExistingLoanRatioUnderSameCollateral());
        tcg.setExistingLoanRatioNotUnderSameCollateral(tcgView.getExistingLoanRatioNotUnderSameCollateral());
        tcg.setRequestLimitRequiredTCG(tcgView.getRequestLimitRequiredTCG());
        tcg.setRequestLimitNotRequiredTCG(tcgView.getRequestLimitNotRequiredTCG());
        tcg.setRequestTCGAmount(tcgView.getRequestTCGAmount());
        tcg.setTcbFloodAmount(tcgView.getTcbFloodAmount());
        tcg.setTcgFlag(tcgView.getTCG());
        tcg.setSumAppraisalAmount(tcgView.getSumAppraisalAmount());
        tcg.setSumLtvValue(tcgView.getSumLtvValue());
        tcg.setSumInThisAppraisalAmount(tcgView.getSumInThisAppraisalAmount());
        tcg.setSumInThisLtvValue(tcgView.getSumInThisLtvValue());

        return tcg;
    }

    public TCGView transformTCGToTcgView(TCG tcg) {

        TCGView tcgView = new TCGView();

        tcgView.setId(tcg.getId());
        tcgView.setActive(tcg.isActive());
        tcgView.setCreateBy(tcg.getCreateBy());
        tcgView.setCreateDate(tcg.getCreateDate());
        tcgView.setModifyBy(tcg.getModifyBy());
        tcgView.setModifyDate(tcg.getModifyDate());
        tcgView.setCollateralRuleResult(tcg.getCollateralRuleResult());
        tcgView.setExistingLoanRatioUnderSameCollateral(tcg.getExistingLoanRatioUnderSameCollateral());
        tcgView.setExistingLoanRatioNotUnderSameCollateral(tcg.getExistingLoanRatioNotUnderSameCollateral());
        tcgView.setRequestLimitRequiredTCG(tcg.getRequestLimitRequiredTCG());
        tcgView.setRequestLimitNotRequiredTCG(tcg.getRequestLimitNotRequiredTCG());
        tcgView.setRequestTCGAmount(tcg.getRequestTCGAmount());
        tcgView.setTcbFloodAmount(tcg.getTcbFloodAmount());
        tcgView.setTCG(tcg.getTcgFlag());
        tcgView.setSumAppraisalAmount(tcg.getSumAppraisalAmount());
        tcgView.setSumLtvValue(tcg.getSumLtvValue());
        tcgView.setSumInThisAppraisalAmount(tcg.getSumInThisAppraisalAmount());
        tcgView.setSumInThisLtvValue(tcg.getSumInThisLtvValue());

        return tcgView;
    }

}
