package com.clevel.selos.businesscontrol;

import com.clevel.selos.dao.master.TCGCollateralTypeDAO;
import com.clevel.selos.dao.working.TCGDAO;
import com.clevel.selos.dao.working.TCGDetailDAO;
import com.clevel.selos.dao.working.WorkCaseDAO;
import com.clevel.selos.model.db.master.TCGCollateralType;
import com.clevel.selos.model.db.master.User;
import com.clevel.selos.model.db.working.TCG;
import com.clevel.selos.model.db.working.TCGDetail;
import com.clevel.selos.model.db.working.WorkCase;
import com.clevel.selos.model.view.TCGDetailView;
import com.clevel.selos.model.view.TCGView;
import com.clevel.selos.transform.TCGDetailTransform;
import com.clevel.selos.transform.TCGTransform;
import org.slf4j.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: SUKANDA CHITSUP
 * Date: 26/9/2556
 * Time: 16:01 น.
 * To change this template use File | Settings | File Templates.
 */


@Stateless
public class TCGInfoControl extends BusinessControl {
    @Inject
    Logger log;
    @Inject
    TCGDetailTransform tcgDetailTransform;
    @Inject
    TCGTransform tcgTransform;
    @Inject
    TCGDAO tcgDAO;
    @Inject
    TCGDetailDAO tcgDetailDAO;
    @Inject
    WorkCaseDAO workCaseDAO;
    @Inject
    TCGCollateralTypeDAO tcgCollateralTypeDAO;

    @Inject
    public void TCGInfoControl() {

    }

    public void onSaveTCGToDB(TCGView tcgView, List<TCGDetailView> tcgDetailViewList, Long workCaseId ,User user) {

        log.info("onSaveTCGToDB begin");
        log.info("workCaseId {} ", workCaseId);
        log.info("tcgView  {} ", tcgView.toString());
        WorkCase workCase = workCaseDAO.findById(workCaseId);
        TCG tcg = tcgTransform.transformTCGViewToModel(tcgView, workCase ,user);
        log.info("transform comeback {} ", tcg.toString());
        tcgDAO.persist(tcg);
        log.info("persist tcg");
        List<TCGDetail> tcgDetailList = tcgDetailTransform.transformTCGDetailViewToModel(tcgDetailViewList, tcg);
        tcgDetailDAO.persist(tcgDetailList);
    }

    public void onEditTCGToDB(TCGView tcgView, List<TCGDetailView> tcgDetailViewList, Long workCaseId,User user) {

        log.info("onEditTCGToDB begin");
        log.info("workCaseId {} ", workCaseId);
        WorkCase workCase = workCaseDAO.findById(workCaseId);
        TCG tcg = tcgTransform.transformTCGViewToModel(tcgView, workCase,user);
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



    public BigDecimal toCalculateLtvValue( TCGDetailView tcgDetailView){
        double ltvValue   = 0;
        double ltvPercent = 0;
        BigDecimal ltvValueBig ;
        TCGCollateralType tcgCollateralType = tcgCollateralTypeDAO.findById(tcgDetailView.getTcgCollateralType().getId());
        ltvPercent  = tcgCollateralType.getPercentLTV();
        log.info("ltvPercent :: {} ",ltvPercent);
        ltvValue =  tcgDetailView.getAppraisalAmount().doubleValue() * ltvPercent;
        ltvValueBig = new BigDecimal(ltvValue);

        return  ltvValueBig;
    }


    public BigDecimal toCalculateSumValue(List<TCGDetailView> TCGDetailViewList, String typeAmt) {
        BigDecimal sum = new BigDecimal(0);

        for (int i = 0; i < TCGDetailViewList.size(); i++) {

            if (typeAmt.equals("Appraisal")) {
                sum = sum.add(TCGDetailViewList.get(i).getAppraisalAmount());
            } else if (typeAmt.equals("LTV")) {
                sum = sum.add(TCGDetailViewList.get(i).getLtvValue());
            } else {
                sum = new BigDecimal(0);
            }
        }

        log.info("sum ::: {} ", sum);

        return sum;
    }

    public BigDecimal toCalculateSumValueInThis(List<TCGDetailView> TCGDetailViewList, String typeAmt) {
        BigDecimal sum = new BigDecimal(0);

        for (int i = 0; i < TCGDetailViewList.size(); i++) {

            if (typeAmt.equals("Appraisal")) {
                if (TCGDetailViewList.get(i).getProposeInThisRequest() == 1) {
                    sum = sum.add(TCGDetailViewList.get(i).getAppraisalAmount());
                }
            } else if (typeAmt.equals("LTV")) {
                if (TCGDetailViewList.get(i).getProposeInThisRequest() == 1) {
                    sum = sum.add(TCGDetailViewList.get(i).getLtvValue());
                }
            } else {
                sum = new BigDecimal(0);
            }

        }
        log.info("sum ::: {} ", sum);

        return sum;
    }

    public BigDecimal toCalCollateralRuleResult(TCGView tcgView){
        BigDecimal collateralRuleResult = BigDecimal.ZERO;
        String result = "";
        double sumAdd = 0;
        double sumAppraisalAmount = 0;
        double thirtyPercent = 0;
        BigDecimal num1   = tcgView.getRequestLimitRequiredTCG();
        BigDecimal num2   = tcgView.getRequestLimitNotRequiredTCG();
        BigDecimal num3   = tcgView.getExistingLoanRatioUnderSameCollateral();
        BigDecimal num4   = tcgView.getExistingLoanRatioNotUnderSameCollateral();
        log.info("tcgView.getRequestLimitRequiredTCG() ::: {} ",tcgView.getRequestLimitRequiredTCG());
        log.info("tcgView.getRequestLimitNotRequiredTCG() ::: {} ",tcgView.getRequestLimitNotRequiredTCG());
        log.info("tcgView.getExistingLoanRatioUnderSameCollateral() ::: {} ",tcgView.getExistingLoanRatioUnderSameCollateral());
        log.info("tcgView.getExistingLoanRatioNotUnderSameCollateral() ::: {} ",tcgView.getExistingLoanRatioNotUnderSameCollateral());
        sumAdd = num1.doubleValue() + num2.doubleValue()+ num3.doubleValue() + num4.doubleValue();
        log.info("tcgView.getSumAppraisalAmount().doubleValue() :: {}",tcgView.getSumAppraisalAmount().doubleValue());
        sumAppraisalAmount = tcgView.getSumAppraisalAmount().doubleValue()/sumAdd;
        log.info("sumAppraisalAmount ::: {} ",sumAppraisalAmount );
        collateralRuleResult = new BigDecimal(sumAppraisalAmount);
        log.info("collateralRuleResult ::: {} ",collateralRuleResult );

        return collateralRuleResult;
    }

    public BigDecimal toCalRequestTCGAmount(TCGView tcgView){
        double sumRequestTCGAmount = 0;
        double sumAdd = 0;
        BigDecimal requestTCGAmount = BigDecimal.ZERO;
        BigDecimal num1   = tcgView.getExistingLoanRatioUnderSameCollateral();
        BigDecimal num2   = tcgView.getRequestLimitRequiredTCG();
        BigDecimal num3   = tcgView.getSumInThisLtvValue();
        BigDecimal num4   = tcgView.getTcbFloodAmount();

        sumAdd =  num1.doubleValue() + num2.doubleValue();
        sumRequestTCGAmount =  sumAdd - num3.doubleValue() - num4.doubleValue();
        log.info("sumRequestTCGAmount :: {}",sumRequestTCGAmount);
        requestTCGAmount = new BigDecimal(sumRequestTCGAmount);
        return requestTCGAmount;
    }

}
