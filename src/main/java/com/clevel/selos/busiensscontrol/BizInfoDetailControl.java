package com.clevel.selos.busiensscontrol;

import com.clevel.selos.dao.working.BizInfoDetailDAO;
import com.clevel.selos.dao.working.BizProductDetailDAO;
import com.clevel.selos.dao.working.BizStakeHolderDetailDAO;
import com.clevel.selos.dao.working.WorkCaseDAO;
import com.clevel.selos.model.db.master.BusinessDescription;
import com.clevel.selos.model.db.working.BizInfoDetail;
import com.clevel.selos.model.db.working.BizProductDetail;
import com.clevel.selos.model.db.working.BizStakeHolderDetail;
import com.clevel.selos.model.view.BizInfoDetailView;
import com.clevel.selos.model.view.BizProductDetailView;
import com.clevel.selos.model.view.BizStakeHolderDetailView;
import com.clevel.selos.transform.BizInfoDetailTransform;
import com.clevel.selos.transform.BizProductDetailTransform;
import com.clevel.selos.transform.BizStakeHolderDetailTransform;
import com.clevel.selos.dao.master.*;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;

@Stateless
public class BizInfoDetailControl {
    @Inject
    Logger log;

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
    WorkCaseDAO workCaseDAO;

    public void onSaveBizInfoToDB(BizInfoDetailView bizInfoDetailView){

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
        try{
            log.info( "onSaveBizInfoToDB begin" );

            bizInfoDetail = bizInfoDetailTransform.transformToModel(bizInfoDetailView);

            bizInfoDetailDAO.persist(bizInfoDetail);
            log.info( "bizInfoDetailDAO persist end" );

            supplierDetailList = bizInfoDetailView.getSupplierDetailList();
            buyerDetailList = bizInfoDetailView.getBuyerDetailList();
            bizProductDetailViewList = bizInfoDetailView.getBizProductDetailViewList();

            bizProductDetailList = new ArrayList<BizProductDetail>();
            for (int i =0;i<bizProductDetailViewList.size();i++){
                bizProductDetailViewTemp = bizProductDetailViewList.get(i);
                bizProductDetailTemp = bizProductDetailTransform.transformToModel(bizProductDetailViewTemp, bizInfoDetail);
                bizProductDetailList.add(bizProductDetailTemp);
            }
            bizProductDetailDAO.persist(bizProductDetailList);
            log.info( "bizProductDetailDAO persist end" );

            bizSupplierList = new ArrayList<BizStakeHolderDetail>();
            for (int i =0;i<supplierDetailList.size();i++){
                stakeHolderDetailViewTemp = supplierDetailList.get(i);
                bizStakeHolderDetailTemp = bizStakeHolderDetailTransform.transformToModel(stakeHolderDetailViewTemp, bizInfoDetail);
                bizStakeHolderDetailTemp.setStakeHolderType("1");
                bizSupplierList.add(bizStakeHolderDetailTemp);
            }
            bizStakeHolderDetailDAO.persist(bizSupplierList);
            log.info( "bizSupplierListDetailDAO persist end" );

            bizBuyerList = new ArrayList<BizStakeHolderDetail>();
            for (int i =0;i<buyerDetailList.size();i++){
                stakeHolderDetailViewTemp = buyerDetailList.get(i);
                bizStakeHolderDetailTemp = bizStakeHolderDetailTransform.transformToModel(stakeHolderDetailViewTemp, bizInfoDetail);
                bizStakeHolderDetailTemp.setStakeHolderType("2");
                bizBuyerList.add(bizStakeHolderDetailTemp);
            }
            bizStakeHolderDetailDAO.persist(bizBuyerList);
            log.info( "bizBuyerListDetailDAO persist end" );

        }catch (Exception e){
            log.error( "onSaveBizInfoToDB error" + e);
        }finally{

            log.info( "onSaveBizInfoToDB end" );
        }
    }

    public BusinessDescription onFindBizDescByID(BusinessDescription bizDescReq ){
        BusinessDescription bizDescRes;
        int bizDescId;
        try{
            log.info( "onFindBizDescByID begin" );

            bizDescId = bizDescReq.getId();
            bizDescRes =  businessDescriptionDAO.findById(bizDescId);

            log.info( "onFindBizDescByID before return is   >> " + bizDescRes.toString());


            return bizDescRes;
        }catch (Exception e){
            log.error( "onFindBizDescByID error" + e);
            return null;
        }finally{

            log.info( "onFindBizDescByID end" );

        }
    }
    
}
