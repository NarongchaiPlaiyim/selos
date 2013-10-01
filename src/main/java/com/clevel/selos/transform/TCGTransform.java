package com.clevel.selos.transform;

import com.clevel.selos.model.db.working.TCG;
import com.clevel.selos.model.db.working.WorkCase;
import com.clevel.selos.model.view.TCGView;

/**
 * Created with IntelliJ IDEA.
 * User: acer
 * Date: 1/10/2556
 * Time: 14:27 น.
 * To change this template use File | Settings | File Templates.
 */
public class TCGTransform extends Transform {

    public TCG transformTCGViewToModel(TCGView tcgView ,WorkCase workCaseId) {

        TCG tcg = new TCG();

        if(tcgView.getId() != 0 ){
            tcg.setId(tcgView.getId());
        }
        tcg.setWorkCase(workCaseId);
        tcg.setCollateralRuleResult(tcgView.getCollateralRuleResult());
        tcg.setExistingLoanRatioUnderSameCollateral(tcgView.getExistingLoanRatioUnderSameCollateral());
        tcg.setExistingLoanRatioNotUnderSameCollateral(tcgView.getExistingLoanRatioUnderSameCollateral());
        tcg.setRequestLimitNotRequiredTCG(tcgView.getRequestLimitRequiredTCG());
        tcg.setRequestTCGAmount(tcgView.getRequestTCGAmount());
        tcg.setTcbFloodAmount(tcgView.getTcbFloodAmount());
        tcg.setTcgFlag(tcgView.isTCG());

        return tcg;
    }

}
