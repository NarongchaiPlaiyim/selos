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
            log.info("bizInfoDetailDAO persist end id is " + bizInfoDetail.getId());


            supplierDetailList = bizInfoDetailView.getSupplierDetailList();
            buyerDetailList = bizInfoDetailView.getBuyerDetailList();
            bizProductDetailViewList = bizInfoDetailView.getBizProductDetailViewList();
            bizProductDetailList = new ArrayList<BizProductDetail>();

            if (bizProductDetailViewList.size() > 0) {
                List<BizProductDetail> bizProductDetailLisDelete = bizProductDetailDAO.findByBizInfoDetail(bizInfoDetail);
                bizProductDetailDAO.delete(bizProductDetailLisDelete);
            }

            for (int i = 0; i < bizProductDetailViewList.size(); i++) {
                bizProductDetailViewTemp = bizProductDetailViewList.get(i);
                bizProductDetailTemp = bizProductDetailTransform.transformToModel(bizProductDetailViewTemp, bizInfoDetail);
                bizProductDetailList.add(bizProductDetailTemp);
            }

            bizProductDetailDAO.persist(bizProductDetailList);
            log.info("bizProductDetailDAO persist end");


            if (supplierDetailList.size() > 0) {
                List<BizStakeHolderDetail> bizSupplierListDelete = bizStakeHolderDetailDAO.findByBizInfoDetail(bizInfoDetail, "1");

                bizStakeHolderDetailDAO.delete(bizSupplierListDelete);
                log.info("supplierDetailList delete end " + bizSupplierListDelete.size());
            }

            bizSupplierList = new ArrayList<BizStakeHolderDetail>();
            for (int i = 0; i < supplierDetailList.size(); i++) {
                stakeHolderDetailViewTemp = supplierDetailList.get(i);
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
            log.error("onSaveBizInfoToDB error" + e);
            return bizInfoDetailView;
        } finally {
            log.info("onSaveBizInfoToDB end");
        }
    }

    public void onSaveSumOnSummary(long bizInfoSummaryId , long workCaseId){
        BankStmtSummaryView bankStmtSummaryView;
        List<BizInfoDetail> bizInfoDetailList;
        double bankStatementAvg = 0;

        bizInfoDetailList = bizInfoSummaryControl.onGetBizInfoDetailByBizInfoSummary(bizInfoSummaryId);

        if (bizInfoDetailList.size() == 0) {
            bizInfoDetailList = new ArrayList<BizInfoDetail>();
            return;
        } else {

            bankStmtSummaryView = bizInfoSummaryControl.getBankStmtSummary(workCaseId);
            if(bankStmtSummaryView != null ){
                if(bankStmtSummaryView.getGrdTotalIncomeNetUW() != null ){
                    bankStatementAvg = bankStmtSummaryView.getGrdTotalIncomeNetUW().doubleValue();
                }else{
                    if(bankStmtSummaryView.getGrdTotalIncomeNetBDM() != null ){
                        bankStatementAvg = bankStmtSummaryView.getGrdTotalIncomeNetBDM().doubleValue();
                    }
                }
            }

            double incomeAmountCal = 0;
            double sumIncomeAmountD = 0;

            double sumIncomePercentD = 0;
            double incomePercentD = 0;

            double adjustIncome = 0;
            double adjustIncomeCal = 0;
            double sumAdjust = 0;

            long ar = 0;
            double arCal = 0;
            double sumAR = 0;

            long ap = 0;
            double apCal = 0;
            double sumAP = 0;

            long inv = 0;
            double invCal = 0;
            double sumINV = 0;

            for (int i = 0; i < bizInfoDetailList.size(); i++) {

                BizInfoDetail bizInfoDetail = bizInfoDetailList.get(i);

                incomePercentD = bizInfoDetail.getPercentBiz().doubleValue();
                sumIncomePercentD += incomePercentD;
                incomeAmountCal = bankStatementAvg * 12;
                bizInfoDetail.setIncomeAmount( new BigDecimal(incomeAmountCal).setScale(2, RoundingMode.HALF_UP));
                sumIncomeAmountD += incomeAmountCal;

                adjustIncome = bizInfoDetail.getAdjustedIncomeFactor().doubleValue();
                adjustIncomeCal = (adjustIncome * incomePercentD) / 100;
                sumAdjust += adjustIncomeCal;

                ar = bizInfoDetail.getBusinessDescription().getAr();
                arCal = (ar * incomePercentD) / 100;
                sumAR += arCal;

                ap = bizInfoDetail.getBusinessDescription().getAp();
                apCal = (ap * incomePercentD) / 100;
                sumAP += apCal;

                inv = bizInfoDetail.getBusinessDescription().getInv();

                invCal = (inv * incomePercentD) / 100;
                sumINV += invCal;

                bizInfoDetailDAO.persist(bizInfoDetail);

            }

            BizInfoSummary  bizInfoSummary = bizInfoSummaryDAO.findById(bizInfoSummaryId);

            BigDecimal sumIncomeAmount = new BigDecimal(sumIncomeAmountD).setScale(2, RoundingMode.HALF_UP);
            BigDecimal sumIncomePercent = new BigDecimal(sumIncomePercentD).setScale(2,RoundingMode.HALF_UP);
            BigDecimal SumWeightAR = new BigDecimal(sumAR).setScale(2,RoundingMode.HALF_UP);
            BigDecimal SumWeightAP = new BigDecimal(sumAP).setScale(2,RoundingMode.HALF_UP);
            BigDecimal SumWeightINV = new BigDecimal(sumINV).setScale(2,RoundingMode.HALF_UP);
            BigDecimal SumWeightIntvIncomeFactor = new BigDecimal(sumAdjust).setScale(2,RoundingMode.HALF_UP);

            bizInfoSummary.setCirculationAmount(sumIncomeAmount);
            bizInfoSummary.setCirculationPercentage(new BigDecimal(100));
            bizInfoSummary.setSumIncomeAmount(sumIncomeAmount);
            bizInfoSummary.setSumIncomePercent(sumIncomePercent);
            bizInfoSummary.setSumWeightAR(SumWeightAR);
            bizInfoSummary.setSumWeightAP(SumWeightAP);
            bizInfoSummary.setSumWeightINV(SumWeightINV);
            bizInfoSummary.setSumWeightInterviewedIncomeFactorPercent(SumWeightIntvIncomeFactor);

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
            log.info("onFindBizDescByID before return is   >> " + bizDescRes.toString());

            return bizDescRes;
        } catch (Exception e) {
            log.error("onFindBizDescByID error" + e);
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


            log.info("bizInfoDetailId " + bizInfoDetailId);

            bizInfoDetail = bizInfoDetailDAO.findById(bizInfoDetailId);

            log.info("bizInfoDetail bizCode after findById " + bizInfoDetail.getBizCode());

            bizInfoDetailView = bizInfoDetailTransform.transformToView(bizInfoDetail);
            log.info("bizInfoDetailView bizCode after transform" + bizInfoDetail.getBizCode());

            bizProductDetailViewList = new ArrayList<BizProductDetailView>();
            bizProductDetailList = bizProductDetailDAO.findByBizInfoDetail(bizInfoDetail);

            for (int i = 0; i < bizProductDetailList.size(); i++) {
                bizProductDetailTemp = bizProductDetailList.get(i);
                bizProductDetailViewTemp = bizProductDetailTransform.transformToView(bizProductDetailTemp);
                bizProductDetailViewList.add(bizProductDetailViewTemp);
            }

            supplierDetailList = new ArrayList<BizStakeHolderDetailView>();
            bizSupplierList = bizStakeHolderDetailDAO.findByBizInfoDetail(bizInfoDetail, "1");

            for (int i = 0; i < bizSupplierList.size(); i++) {
                bizStakeHolderDetailTemp = bizSupplierList.get(i);
                stakeHolderDetailViewTemp = bizStakeHolderDetailTransform.transformToView(bizStakeHolderDetailTemp);
                supplierDetailList.add(stakeHolderDetailViewTemp);
            }

            buyerDetailList = new ArrayList<BizStakeHolderDetailView>();
            bizBuyerList = bizStakeHolderDetailDAO.findByBizInfoDetail(bizInfoDetail, "2");

            for (int i = 0; i < bizBuyerList.size(); i++) {
                bizStakeHolderDetailTemp = bizBuyerList.get(i);
                stakeHolderDetailViewTemp = bizStakeHolderDetailTransform.transformToView(bizStakeHolderDetailTemp);
                buyerDetailList.add(stakeHolderDetailViewTemp);
            }

            bizInfoDetailView.setBizProductDetailViewList(bizProductDetailViewList);
            log.info("bizProduct size " + bizInfoDetailView.getBizProductDetailViewList().size());
            bizInfoDetailView.setSupplierDetailList(supplierDetailList);
            log.info("supplier size " + bizInfoDetailView.getSupplierDetailList().size());
            bizInfoDetailView.setBuyerDetailList(buyerDetailList);
            log.info("buyer size  " + bizInfoDetailView.getBuyerDetailList().size());

            return bizInfoDetailView;

        } catch (Exception e) {
            log.error("onFindByID error" + e);
            return null;
        } finally {
            log.info("onFindByID end");
        }
    }

    public void onDeleteBizInfoToDB(BizInfoDetailView bizInfoDetailView) {
        BizInfoDetail bizInfoDetail;
        try {
            log.info("onDeleteBizInfoToDB begin");
            log.info("onDeleteBizInfoToDB id is " + bizInfoDetailView.getId());
            bizInfoDetail = bizInfoDetailDAO.findById(bizInfoDetailView.getId());

            List<BizProductDetail> bizProductDetailLisDelete = bizProductDetailDAO.findByBizInfoDetail(bizInfoDetail);
            bizProductDetailDAO.delete(bizProductDetailLisDelete);

            List<BizStakeHolderDetail> bizSupplierListDelete = bizStakeHolderDetailDAO.findByBizInfoDetail(bizInfoDetail, "1");
            bizStakeHolderDetailDAO.delete(bizSupplierListDelete);

            List<BizStakeHolderDetail> bizBuyerListDelete = bizStakeHolderDetailDAO.findByBizInfoDetail(bizInfoDetail, "2");
            bizStakeHolderDetailDAO.delete(bizBuyerListDelete);

            bizInfoDetailDAO.delete(bizInfoDetail);

        } catch (Exception e) {
            log.error("onDeleteBizInfoToDB error" + e);
        } finally {

            log.info("onDeleteBizInfoToDB end");
        }
    }

}
