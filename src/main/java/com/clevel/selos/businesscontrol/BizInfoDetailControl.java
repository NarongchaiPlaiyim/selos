package com.clevel.selos.businesscontrol;

import com.clevel.selos.dao.master.BusinessDescriptionDAO;
import com.clevel.selos.dao.working.BizInfoDetailDAO;
import com.clevel.selos.dao.working.BizInfoSummaryDAO;
import com.clevel.selos.dao.working.BizProductDetailDAO;
import com.clevel.selos.dao.working.BizStakeHolderDetailDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.master.BusinessDescription;
import com.clevel.selos.model.db.working.BizInfoDetail;
import com.clevel.selos.model.db.working.BizInfoSummary;
import com.clevel.selos.model.db.working.BizProductDetail;
import com.clevel.selos.model.db.working.BizStakeHolderDetail;
import com.clevel.selos.model.view.BizInfoDetailView;
import com.clevel.selos.model.view.BizProductDetailView;
import com.clevel.selos.model.view.BizStakeHolderDetailView;
import com.clevel.selos.transform.BizInfoDetailTransform;
import com.clevel.selos.transform.BizProductDetailTransform;
import com.clevel.selos.transform.BizStakeHolderDetailTransform;
import org.slf4j.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

@Stateless
public class BizInfoDetailControl extends BusinessControl {
    @Inject
    BusinessDescriptionDAO businessDescriptionDAO;

    @Inject
    BizInfoDetailDAO bizInfoDetailDAO;
    @Inject
    BizStakeHolderDetailDAO bizStakeHolderDetailDAO;
    @Inject
    BizProductDetailDAO bizProductDetailDAO;
    @Inject
    BizProductDetailTransform bizProductDetailTransform;
    @Inject
    BizStakeHolderDetailTransform bizStakeHolderDetailTransform;
    @Inject
    BizInfoDetailTransform bizInfoDetailTransform;
    @Inject
    BizInfoSummaryDAO bizInfoSummaryDAO;

    public BizInfoDetailView onSaveBizInfoToDB(BizInfoDetailView bizInfoDetailView, long bizInfoSummaryId) {

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

            return bizInfoDetailView;
        } catch (Exception e) {
            log.error("onSaveBizInfoToDB error" + e);
            return bizInfoDetailView;
        } finally {
            log.info("onSaveBizInfoToDB end");
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
