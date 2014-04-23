package com.clevel.selos.businesscontrol;

import com.clevel.selos.dao.master.BusinessDescriptionDAO;
import com.clevel.selos.dao.working.BizInfoDetailDAO;
import com.clevel.selos.dao.working.BizInfoSummaryDAO;
import com.clevel.selos.dao.working.BizProductDetailDAO;
import com.clevel.selos.dao.working.BizStakeHolderDetailDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.StatusValue;
import com.clevel.selos.model.StepValue;
import com.clevel.selos.model.db.master.BusinessDescription;
import com.clevel.selos.model.db.master.User;
import com.clevel.selos.model.db.working.BizInfoDetail;
import com.clevel.selos.model.db.working.BizInfoSummary;
import com.clevel.selos.model.db.working.BizProductDetail;
import com.clevel.selos.model.db.working.BizStakeHolderDetail;
import com.clevel.selos.model.view.BankStmtSummaryView;
import com.clevel.selos.model.view.BizInfoDetailView;
import com.clevel.selos.model.view.BizProductDetailView;
import com.clevel.selos.model.view.BizStakeHolderDetailView;
import com.clevel.selos.transform.BizInfoDetailTransform;
import com.clevel.selos.transform.BizProductDetailTransform;
import com.clevel.selos.transform.BizStakeHolderDetailTransform;
import com.clevel.selos.util.FacesUtil;
import com.clevel.selos.util.Util;
import org.joda.time.DateTime;
import org.slf4j.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Stateless
public class BizInfoDetailControl extends BusinessControl {
    @Inject
    @SELOS
    private Logger log;

    @Inject
    BusinessDescriptionDAO businessDescriptionDAO;
    @Inject
    BizInfoDetailDAO bizInfoDetailDAO;
    @Inject
    BizStakeHolderDetailDAO bizStakeHolderDetailDAO;
    @Inject
    BizProductDetailDAO bizProductDetailDAO;
    @Inject
    BizInfoSummaryDAO bizInfoSummaryDAO;

    @Inject
    BizProductDetailTransform bizProductDetailTransform;
    @Inject
    BizStakeHolderDetailTransform bizStakeHolderDetailTransform;
    @Inject
    BizInfoDetailTransform bizInfoDetailTransform;
    @Inject
    BizInfoSummaryControl bizInfoSummaryControl;

	@Inject
    public BizInfoDetailControl(){

    }

    public BizInfoDetailView onSaveBizInfoToDB(BizInfoDetailView bizInfoDetailView, long bizInfoSummaryId , long workCaseId) {

        List<BizStakeHolderDetail> bizSupplierList;
        List<BizStakeHolderDetail> bizBuyerList;
        List<BizProductDetail> bizProductDetailList;
        List<BizStakeHolderDetailView> supplierDetailList;
        List<BizStakeHolderDetailView> buyerDetailList;
        List<BizProductDetailView> bizProductDetailViewList;

        BizInfoDetail bizInfoDetail;
        BizInfoSummary bizInfoSummary;
        BizStakeHolderDetail bizStakeHolderDetailTemp;
        BizStakeHolderDetailView stakeHolderDetailViewTemp;
        BizProductDetailView bizProductDetailViewTemp;
        BizProductDetail bizProductDetailTemp;

        User user = getCurrentUser();

        if(bizInfoDetailView.getId() == 0){
            bizInfoDetailView.setCreateBy(user);
            bizInfoDetailView.setCreateDate(DateTime.now().toDate());
        }
        bizInfoDetailView.setModifyBy(user);

        try {
            log.info("onSaveBizInfoToDB begin");

            bizInfoSummary = bizInfoSummaryDAO.findById(bizInfoSummaryId);

            bizInfoDetail = bizInfoDetailTransform.transformToModel(bizInfoDetailView);
            bizInfoDetail.setBizInfoSummary(bizInfoSummary);

            bizInfoDetailDAO.persist(bizInfoDetail);
            log.info("bizInfoDetailDAO persist end id is {}",bizInfoDetail.getId());


            supplierDetailList = bizInfoDetailView.getSupplierDetailList();
            buyerDetailList = bizInfoDetailView.getBuyerDetailList();
            bizProductDetailViewList = bizInfoDetailView.getBizProductDetailViewList();
            bizProductDetailList = new ArrayList<BizProductDetail>();

//            if (bizProductDetailViewList.size() > 0) {
                List<BizProductDetail> bizProductDetailLisDelete = bizProductDetailDAO.findByBizInfoDetail(bizInfoDetail);
                bizProductDetailDAO.delete(bizProductDetailLisDelete);
//            }

            for (BizProductDetailView aBizProductDetailViewList : bizProductDetailViewList) {
                bizProductDetailViewTemp = aBizProductDetailViewList;
                bizProductDetailTemp = bizProductDetailTransform.transformToModel(bizProductDetailViewTemp, bizInfoDetail);
                bizProductDetailList.add(bizProductDetailTemp);
            }

            bizProductDetailDAO.persist(bizProductDetailList);
            log.info("bizProductDetailDAO persist end");


//            if (supplierDetailList.size() > 0) {
                List<BizStakeHolderDetail> bizSupplierListDelete = bizStakeHolderDetailDAO.findByBizInfoDetail(bizInfoDetail, "1");

                bizStakeHolderDetailDAO.delete(bizSupplierListDelete);
                log.info("supplierDetailList delete end {}",bizSupplierListDelete.size());
//            }

            bizSupplierList = new ArrayList<BizStakeHolderDetail>();
            for (BizStakeHolderDetailView aSupplierDetailList : supplierDetailList) {
                stakeHolderDetailViewTemp = aSupplierDetailList;
                bizStakeHolderDetailTemp = bizStakeHolderDetailTransform.transformToModel(stakeHolderDetailViewTemp, bizInfoDetail);
                bizStakeHolderDetailTemp.setStakeHolderType("1");
                bizSupplierList.add(bizStakeHolderDetailTemp);
            }
            bizStakeHolderDetailDAO.persist(bizSupplierList);
            log.info("bizSupplierListDetailDAO persist end");

//            if (buyerDetailList.size() > 0) {
                List<BizStakeHolderDetail> bizBuyerListDelete = bizStakeHolderDetailDAO.findByBizInfoDetail(bizInfoDetail, "2");
                bizStakeHolderDetailDAO.delete(bizBuyerListDelete);
//            }

            bizBuyerList = new ArrayList<BizStakeHolderDetail>();
            for(BizStakeHolderDetailView bizStakeHolderDetailView : buyerDetailList){
                bizStakeHolderDetailTemp = bizStakeHolderDetailTransform.transformToModel(bizStakeHolderDetailView, bizInfoDetail);
                bizStakeHolderDetailTemp.setStakeHolderType("2");
                bizBuyerList.add(bizStakeHolderDetailTemp);
            }

            bizStakeHolderDetailDAO.persist(bizBuyerList);
            log.info("bizBuyerListDetailDAO persist end");

            bizInfoDetailView.setId(bizInfoDetail.getId());

            onSaveSumOnSummary(bizInfoSummaryId,workCaseId);
            return bizInfoDetailView;
        } catch (Exception e) {
            log.error("onSaveBizInfoToDB error: {}",e);
            return bizInfoDetailView;
        } finally {
            log.info("onSaveBizInfoToDB end");
        }
    }


    public void onSaveSumOnSummary(long bizInfoSummaryId , long workCaseId){
        BankStmtSummaryView bankStmtSummaryView;
        List<BizInfoDetail> bizInfoDetailList;
        BigDecimal bankStatementAvg = BigDecimal.ZERO;
        long stepId = 0;
        BigDecimal inComeTotalNet = BigDecimal.ZERO;

        BigDecimal twenty = BigDecimal.valueOf(12);
        BigDecimal oneHundred = BigDecimal.valueOf(100);

        bizInfoDetailList = bizInfoSummaryControl.onGetBizInfoDetailByBizInfoSummary(bizInfoSummaryId);
        log.debug("bizInfoDetailList : {}",bizInfoDetailList);

        HttpSession session = FacesUtil.getSession(true);
        if(session.getAttribute("stepId") != null){
            stepId = Long.parseLong(session.getAttribute("stepId").toString());
        }

        log.debug("stepId : {}",stepId);

        if (bizInfoDetailList.size() != 0) {

            bankStmtSummaryView = bizInfoSummaryControl.getBankStmtSummary(workCaseId);
            log.debug("bankStmtSummaryView : {}",bankStmtSummaryView);
            if(bankStmtSummaryView != null ){
                if(stepId >= StepValue.CREDIT_DECISION_UW1.value()){
                    bankStatementAvg = bankStmtSummaryView.getGrdTotalIncomeNetUW();
                } else {
                    bankStatementAvg = bankStmtSummaryView.getGrdTotalIncomeNetBDM();
                }
            }

            log.info("bankStatementAvg : {}",bankStatementAvg);

            BigDecimal incomeAmountCal;
            BigDecimal sumIncomeAmountD = BigDecimal.ZERO;

            BigDecimal incomePercentD;
            BigDecimal sumIncomePercentD = BigDecimal.ZERO;

            BigDecimal adjustIncome;
            BigDecimal adjustIncomeCal;
            BigDecimal sumAdjust = BigDecimal.ZERO;

            BigDecimal ar;
            BigDecimal arCal;
            BigDecimal sumAR = BigDecimal.ZERO;

            BigDecimal ap;
            BigDecimal apCal;
            BigDecimal sumAP = BigDecimal.ZERO;

            BigDecimal inv;
            BigDecimal invCal;
            BigDecimal sumINV = BigDecimal.ZERO;

            BigDecimal incomeFactor;
            BigDecimal incomeFactorCal;
            BigDecimal sumIncomeFactor = BigDecimal.ZERO;

            for (BizInfoDetail bizInfoDetail : bizInfoDetailList) {
                incomePercentD = bizInfoDetail.getPercentBiz();
                sumIncomePercentD = Util.add(sumIncomePercentD,incomePercentD);
                incomeAmountCal = Util.multiply(bankStatementAvg,twenty);
//                sumIncomeAmountD = Util.add(sumIncomeAmountD,incomeAmountCal);

                inComeTotalNet = Util.divide(Util.multiply(incomeAmountCal,incomePercentD),oneHundred);

                if(inComeTotalNet != null){
                    bizInfoDetail.setIncomeAmount(inComeTotalNet.setScale(2, RoundingMode.HALF_UP));
                }

                adjustIncome = bizInfoDetail.getAdjustedIncomeFactor();
                adjustIncomeCal = Util.divide(Util.multiply(adjustIncome,incomePercentD),oneHundred);
                sumAdjust = Util.add(sumAdjust,adjustIncomeCal);

                ar = bizInfoDetail.getBusinessDescription().getAr();
                arCal = Util.divide(Util.multiply(ar,incomePercentD),oneHundred);
                sumAR = Util.add(sumAR,arCal);

                ap = bizInfoDetail.getBusinessDescription().getAp();
                apCal = Util.divide(Util.multiply(ap,incomePercentD),oneHundred);
                sumAP = Util.add(sumAP,apCal);

                inv = bizInfoDetail.getBusinessDescription().getInv();
                invCal = Util.divide(Util.multiply(inv,incomePercentD),oneHundred);
                sumINV = Util.add(sumINV,invCal);

                incomeFactor = bizInfoDetail.getIncomeFactor();
                incomeFactorCal = Util.divide(Util.multiply(incomeFactor,incomePercentD),oneHundred);
                sumIncomeFactor = Util.add(sumIncomeFactor,incomeFactorCal);


                bizInfoDetailDAO.persist(bizInfoDetail);
            }

            BizInfoSummary  bizInfoSummary = bizInfoSummaryDAO.findById(bizInfoSummaryId);

            sumIncomeAmountD = Util.multiply(bankStatementAvg,twenty);
            BigDecimal sumIncomeAmount = null;
            BigDecimal sumIncomePercent = null;
            BigDecimal sumWeightAR = null;
            BigDecimal sumWeightAP = null;
            BigDecimal sumWeightINV = null;
            BigDecimal sumWeightIntIncomeFactor = null;
            BigDecimal sumWeightIncFactor = null;

            if(sumIncomeAmountD != null){
                sumIncomeAmount = sumIncomeAmountD.setScale(2, RoundingMode.HALF_UP);
            }
            if(sumIncomePercentD != null){
                sumIncomePercent = sumIncomePercentD.setScale(2,RoundingMode.HALF_UP);
            }
            if(sumAR != null){
                sumWeightAR = sumAR.setScale(2,RoundingMode.HALF_UP);
            }
            if(sumAP != null){
                sumWeightAP = sumAP.setScale(2,RoundingMode.HALF_UP);
            }
            if(sumINV != null){
                sumWeightINV = sumINV.setScale(2,RoundingMode.HALF_UP);
            }
            if(sumAdjust != null){
                sumWeightIntIncomeFactor = sumAdjust.setScale(2,RoundingMode.HALF_UP);
            }
            if(sumIncomeFactor != null){
                sumWeightIncFactor = sumIncomeFactor.setScale(2,RoundingMode.HALF_UP);
            }
//            bizInfoSummary.setCirculationAmount(sumIncomeAmount); //?????  BankStatementSummary.grandTotal
//            bizInfoSummary.setCirculationPercentage(oneHundred); //?????
            bizInfoSummary.setSumIncomeAmount(sumIncomeAmount); //?????
            bizInfoSummary.setSumIncomePercent(sumIncomePercent);
            bizInfoSummary.setSumWeightAR(sumWeightAR);
            bizInfoSummary.setSumWeightAP(sumWeightAP);
            bizInfoSummary.setSumWeightINV(sumWeightINV);
            bizInfoSummary.setSumWeightInterviewedIncomeFactorPercent(sumWeightIntIncomeFactor);
            bizInfoSummary.setWeightIncomeFactor(sumWeightIncFactor);

            System.out.println("SumWeightIncFactor {},"+sumWeightIncFactor);

            log.debug("bizInfoSummary : {}",bizInfoSummary);

            bizInfoSummaryDAO.persist(bizInfoSummary);
        }
    }

    public BusinessDescription onFindBizDescByID(BusinessDescription bizDescReq) {
        BusinessDescription bizDescRes;
        int bizDescId;
        try {
            log.info("onFindBizDescByID begin");

            bizDescId = bizDescReq.getId();
            bizDescRes = businessDescriptionDAO.findById(bizDescId);
            log.info("onFindBizDescByID before return is   >> {}",bizDescRes);

            return bizDescRes;
        } catch (Exception e) {
            log.error("onFindBizDescByID error: {}",e);
            return null;
        } finally {

            log.info("onFindBizDescByID end");

        }
    }

    public BizInfoDetailView onFindByID(long bizInfoDetailId) {

        List<BizStakeHolderDetail> bizSupplierList;
        List<BizStakeHolderDetail> bizBuyerList;
        List<BizProductDetail> bizProductDetailList;
        List<BizStakeHolderDetailView> supplierDetailList;
        List<BizStakeHolderDetailView> buyerDetailList;
        List<BizProductDetailView> bizProductDetailViewList;

        BizInfoDetail bizInfoDetail;
        BizStakeHolderDetail bizStakeHolderDetailTemp;
        BizStakeHolderDetailView stakeHolderDetailViewTemp;
        BizProductDetailView bizProductDetailViewTemp;
        BizProductDetail bizProductDetailTemp;
        BizInfoDetailView bizInfoDetailView;

        try {
            log.info("onFindByID begin");


            log.info("bizInfoDetailId {}",bizInfoDetailId);

            bizInfoDetail = bizInfoDetailDAO.findById(bizInfoDetailId);

            log.info("bizInfoDetail bizCode after findById {}",bizInfoDetail.getBizCode());

            bizInfoDetailView = bizInfoDetailTransform.transformToView(bizInfoDetail);
            log.info("bizInfoDetailView bizCode after transform {}",bizInfoDetail.getBizCode());

            bizProductDetailViewList = new ArrayList<BizProductDetailView>();
            bizProductDetailList = bizProductDetailDAO.findByBizInfoDetail(bizInfoDetail);

            for (BizProductDetail aBizProductDetailList : bizProductDetailList) {
                bizProductDetailTemp = aBizProductDetailList;
                bizProductDetailViewTemp = bizProductDetailTransform.transformToView(bizProductDetailTemp);
                bizProductDetailViewList.add(bizProductDetailViewTemp);
            }

            supplierDetailList = new ArrayList<BizStakeHolderDetailView>();
            bizSupplierList = bizStakeHolderDetailDAO.findByBizInfoDetail(bizInfoDetail, "1");

            for (BizStakeHolderDetail aBizSupplierList : bizSupplierList) {
                bizStakeHolderDetailTemp = aBizSupplierList;
                stakeHolderDetailViewTemp = bizStakeHolderDetailTransform.transformToView(bizStakeHolderDetailTemp);
                supplierDetailList.add(stakeHolderDetailViewTemp);
            }

            buyerDetailList = new ArrayList<BizStakeHolderDetailView>();
            bizBuyerList = bizStakeHolderDetailDAO.findByBizInfoDetail(bizInfoDetail, "2");

            for (BizStakeHolderDetail aBizBuyerList : bizBuyerList) {
                bizStakeHolderDetailTemp = aBizBuyerList;
                stakeHolderDetailViewTemp = bizStakeHolderDetailTransform.transformToView(bizStakeHolderDetailTemp);
                buyerDetailList.add(stakeHolderDetailViewTemp);
            }

            bizInfoDetailView.setBizProductDetailViewList(bizProductDetailViewList);
            log.info("bizProduct size {}",bizInfoDetailView.getBizProductDetailViewList().size());
            bizInfoDetailView.setSupplierDetailList(supplierDetailList);
            log.info("supplier size {}",bizInfoDetailView.getSupplierDetailList().size());
            bizInfoDetailView.setBuyerDetailList(buyerDetailList);
            log.info("buyer size {}",bizInfoDetailView.getBuyerDetailList().size());

            //for hidden field on status below UW
            Long statusId = 0L;
            HttpSession session = FacesUtil.getSession(true);
            if(session.getAttribute("statusId") != null){
                statusId = Long.parseLong(session.getAttribute("statusId").toString());
            }

            if(statusId >= StatusValue.REVIEW_CA.value()){
                bizInfoDetailView.setSupplierUWAdjustPercentCredit(null);
                bizInfoDetailView.setSupplierUWAdjustCreditTerm(null);
                bizInfoDetailView.setBuyerUWAdjustPercentCredit(null);
                bizInfoDetailView.setBuyerUWAdjustCreditTerm(null);
            }

            return bizInfoDetailView;

        } catch (Exception e) {
            log.error("onFindByID error {}",e);
            return null;
        } finally {
            log.info("onFindByID end");
        }
    }

    public void onDeleteBizInfoToDB(BizInfoDetailView bizInfoDetailView) {
        BizInfoDetail bizInfoDetail;
        try {
            log.info("onDeleteBizInfoToDB begin");
            log.info("onDeleteBizInfoToDB id is {}",bizInfoDetailView.getId());
            bizInfoDetail = bizInfoDetailDAO.findById(bizInfoDetailView.getId());

            List<BizProductDetail> bizProductDetailLisDelete = bizProductDetailDAO.findByBizInfoDetail(bizInfoDetail);
            bizProductDetailDAO.delete(bizProductDetailLisDelete);

            List<BizStakeHolderDetail> bizSupplierListDelete = bizStakeHolderDetailDAO.findByBizInfoDetail(bizInfoDetail, "1");
            bizStakeHolderDetailDAO.delete(bizSupplierListDelete);

            List<BizStakeHolderDetail> bizBuyerListDelete = bizStakeHolderDetailDAO.findByBizInfoDetail(bizInfoDetail, "2");
            bizStakeHolderDetailDAO.delete(bizBuyerListDelete);

            bizInfoDetailDAO.delete(bizInfoDetail);

        } catch (Exception e) {
            log.error("onDeleteBizInfoToDB error {}",e);
        } finally {

            log.info("onDeleteBizInfoToDB end");
        }
    }

    public int getUserRoleId() {
        User user = getCurrentUser();
        if (user != null && user.getRole() != null) {
            return user.getRole().getId();
        }
        return 0;
    }

}
