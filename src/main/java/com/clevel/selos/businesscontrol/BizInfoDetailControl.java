package com.clevel.selos.businesscontrol;

import com.clevel.selos.dao.master.BusinessDescriptionDAO;
import com.clevel.selos.dao.working.BizInfoDetailDAO;
import com.clevel.selos.dao.working.BizInfoSummaryDAO;
import com.clevel.selos.dao.working.BizProductDetailDAO;
import com.clevel.selos.dao.working.BizStakeHolderDetailDAO;
import com.clevel.selos.integration.SELOS;
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
import com.clevel.selos.util.Util;
import org.joda.time.DateTime;
import org.slf4j.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
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

            if (bizProductDetailViewList.size() > 0) {
                List<BizProductDetail> bizProductDetailLisDelete = bizProductDetailDAO.findByBizInfoDetail(bizInfoDetail);
                bizProductDetailDAO.delete(bizProductDetailLisDelete);
            }

            for (BizProductDetailView aBizProductDetailViewList : bizProductDetailViewList) {
                bizProductDetailViewTemp = aBizProductDetailViewList;
                bizProductDetailTemp = bizProductDetailTransform.transformToModel(bizProductDetailViewTemp, bizInfoDetail);
                bizProductDetailList.add(bizProductDetailTemp);
            }

            bizProductDetailDAO.persist(bizProductDetailList);
            log.info("bizProductDetailDAO persist end");


            if (supplierDetailList.size() > 0) {
                List<BizStakeHolderDetail> bizSupplierListDelete = bizStakeHolderDetailDAO.findByBizInfoDetail(bizInfoDetail, "1");

                bizStakeHolderDetailDAO.delete(bizSupplierListDelete);
                log.info("supplierDetailList delete end {}",bizSupplierListDelete.size());
            }

            bizSupplierList = new ArrayList<BizStakeHolderDetail>();
            for (BizStakeHolderDetailView aSupplierDetailList : supplierDetailList) {
                stakeHolderDetailViewTemp = aSupplierDetailList;
                bizStakeHolderDetailTemp = bizStakeHolderDetailTransform.transformToModel(stakeHolderDetailViewTemp, bizInfoDetail);
                bizStakeHolderDetailTemp.setStakeHolderType("1");
                bizSupplierList.add(bizStakeHolderDetailTemp);
            }
            bizStakeHolderDetailDAO.persist(bizSupplierList);
            log.info("bizSupplierListDetailDAO persist end");

            if (buyerDetailList.size() > 0) {
                List<BizStakeHolderDetail> bizBuyerListDelete = bizStakeHolderDetailDAO.findByBizInfoDetail(bizInfoDetail, "2");
                bizStakeHolderDetailDAO.delete(bizBuyerListDelete);
            }

            bizBuyerList = new ArrayList<BizStakeHolderDetail>();
            for (int i = 0; i < buyerDetailList.size(); i++) {
                stakeHolderDetailViewTemp = buyerDetailList.get(i);
                bizStakeHolderDetailTemp = bizStakeHolderDetailTransform.transformToModel(stakeHolderDetailViewTemp, bizInfoDetail);
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

        BigDecimal twenty = BigDecimal.valueOf(12);
        BigDecimal oneHundred = BigDecimal.valueOf(100);

        bizInfoDetailList = bizInfoSummaryControl.onGetBizInfoDetailByBizInfoSummary(bizInfoSummaryId);

        if (bizInfoDetailList.size() != 0) {

            bankStmtSummaryView = bizInfoSummaryControl.getBankStmtSummary(workCaseId);
            if(bankStmtSummaryView != null ){
                if(bankStmtSummaryView.getGrdTotalIncomeNetUW() != null ){
                    bankStatementAvg = bankStmtSummaryView.getGrdTotalIncomeNetUW();
                }else{
                    if(bankStmtSummaryView.getGrdTotalIncomeNetBDM() != null ){
                        bankStatementAvg = bankStmtSummaryView.getGrdTotalIncomeNetBDM();
                    }
                }
            }

            BigDecimal incomeAmountCal;
            BigDecimal sumIncomeAmountD = BigDecimal.ZERO;

            BigDecimal sumIncomePercentD = BigDecimal.ZERO;
            BigDecimal incomePercentD;

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

            for (BizInfoDetail bizInfoDetail : bizInfoDetailList) {

                incomePercentD = bizInfoDetail.getPercentBiz();
                sumIncomePercentD = Util.add(sumIncomeAmountD,incomePercentD);
                incomeAmountCal = Util.multiply(bankStatementAvg,twenty);
                bizInfoDetail.setIncomeAmount(incomeAmountCal.setScale(2, RoundingMode.HALF_UP));
                sumIncomeAmountD = Util.add(sumIncomeAmountD,incomeAmountCal);

                adjustIncome = bizInfoDetail.getAdjustedIncomeFactor();
                adjustIncomeCal = Util.divide(Util.multiply(adjustIncome,incomePercentD),100);
                sumAdjust = Util.add(sumAdjust,adjustIncomeCal);

                ar = bizInfoDetail.getBusinessDescription().getAr();
                arCal = Util.divide(Util.multiply(ar,incomePercentD),100);
                sumAR = Util.add(sumAR,arCal);

                ap = bizInfoDetail.getBusinessDescription().getAp();
                apCal = Util.divide(Util.multiply(ap,incomePercentD),100);
                sumAP = Util.add(sumAP,apCal);

                inv = bizInfoDetail.getBusinessDescription().getInv();

                invCal = Util.divide(Util.multiply(inv,incomePercentD),100);
                sumINV = Util.multiply(sumINV,invCal);

                bizInfoDetailDAO.persist(bizInfoDetail);
            }

            BizInfoSummary  bizInfoSummary = bizInfoSummaryDAO.findById(bizInfoSummaryId);

            BigDecimal sumIncomeAmount = sumIncomeAmountD.setScale(2, RoundingMode.HALF_UP);
            BigDecimal sumIncomePercent = sumIncomePercentD.setScale(2,RoundingMode.HALF_UP);
            BigDecimal SumWeightAR = sumAR.setScale(2,RoundingMode.HALF_UP);
            BigDecimal SumWeightAP = sumAP.setScale(2,RoundingMode.HALF_UP);
            BigDecimal SumWeightINV = sumINV.setScale(2,RoundingMode.HALF_UP);
            BigDecimal SumWeightIntIncomeFactor = sumAdjust.setScale(2,RoundingMode.HALF_UP);

            bizInfoSummary.setCirculationAmount(sumIncomeAmount);
            bizInfoSummary.setCirculationPercentage(oneHundred);
            bizInfoSummary.setSumIncomeAmount(sumIncomeAmount);
            bizInfoSummary.setSumIncomePercent(sumIncomePercent);
            bizInfoSummary.setSumWeightAR(SumWeightAR);
            bizInfoSummary.setSumWeightAP(SumWeightAP);
            bizInfoSummary.setSumWeightINV(SumWeightINV);
            bizInfoSummary.setSumWeightInterviewedIncomeFactorPercent(SumWeightIntIncomeFactor);

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

}
