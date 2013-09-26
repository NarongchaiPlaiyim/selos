package com.clevel.selos.busiensscontrol;

import com.clevel.selos.dao.working.BizInfoDAO;
import com.clevel.selos.dao.working.BizProductDetailDAO;
import com.clevel.selos.dao.working.BizStakeHolderDetailDAO;
import com.clevel.selos.model.db.working.BizInfo;
import com.clevel.selos.model.db.working.BizProductDetail;
import com.clevel.selos.model.db.working.BizStakeHolderDetail;
import com.clevel.selos.model.view.BizInfoView;
import com.clevel.selos.model.view.BizProductDetailView;
import com.clevel.selos.model.view.BizStakeHolderDetailView;
import com.clevel.selos.transform.BizInfoTransform;
import com.clevel.selos.transform.BizProductDetailTransform;
import com.clevel.selos.transform.BizStakeHolderDetailTransform;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: Rangsun
 * Date: 26/9/2556
 * Time: 16:09 น.
 * To change this template use File | Settings | File Templates.
 */

public class BizInfoControl {
    @Inject
    Logger log;
    @Inject
    BizInfoDAO bizInfoDAO;
    @Inject
    BizStakeHolderDetailDAO bizStakeHolderDetailDAO;
    @Inject
    BizProductDetailDAO bizProductDetailDAO;
    @Inject
    BizProductDetailTransform bizProductDetailTransform;
    @Inject
    BizStakeHolderDetailTransform bizStakeHolderDetailTransform;
    @Inject
    BizInfoTransform bizInfoTransform;

    public void onSaveBizInfoToDB(BizInfoView bizInfoFullView){
        List<BizStakeHolderDetail> bizSupplierList;
        List<BizStakeHolderDetail> bizBuyerList;
        List<BizProductDetail> bizProductDetailList;
        List<BizStakeHolderDetailView> supplierDetailList;
        List<BizStakeHolderDetailView> buyerDetailList;
        List<BizProductDetailView> bizProductDetailViewList;

        BizInfo bizInfo;
        BizStakeHolderDetail bizStakeHolderDetailTemp;
        BizStakeHolderDetailView stakeHolderDetailViewTemp;
        BizProductDetailView bizProductDetailViewTemp;
        BizProductDetail bizProductDetailTemp;
        try{
            log.info( "onSaveBizInfoToDB begin" );

            bizInfo = bizInfoTransform.transformToModel(bizInfoFullView);
            bizInfoDAO.persist(bizInfo);

            supplierDetailList = bizInfoFullView.getSupplierDetailList();
            buyerDetailList = bizInfoFullView.getBuyerDetailList();
            bizProductDetailViewList = bizInfoFullView.getBizProductDetailViewList();

            bizProductDetailList = new ArrayList<BizProductDetail>();
            for (int i =0;i<bizProductDetailViewList.size();i++){
                bizProductDetailViewTemp = bizProductDetailViewList.get(i);
                bizProductDetailTemp = bizProductDetailTransform.transformToModel(bizProductDetailViewTemp,bizInfo);
                bizProductDetailList.add(bizProductDetailTemp);
            }
            bizProductDetailDAO.persist(bizProductDetailList);

            //bizProductDetail Add
            log.info( "bizProductDetailList Size " + bizProductDetailList.size());
            for (int i=0;i<bizProductDetailList.size();i++){
                bizProductDetailTemp =bizProductDetailList.get(i);
                bizProductDetailTemp.setBizInfo(bizInfo);
                bizProductDetailList.set(i,bizProductDetailTemp);
            }
            log.info( "bizProductDetailList Add " );
            bizProductDetailDAO.persist(bizProductDetailList);

            bizSupplierList = new ArrayList<BizStakeHolderDetail>();
            for (int i =0;i<supplierDetailList.size();i++){
                stakeHolderDetailViewTemp = supplierDetailList.get(i);
                bizStakeHolderDetailTemp = bizStakeHolderDetailTransform.transformToModel(stakeHolderDetailViewTemp,bizInfo);
                bizSupplierList.add(bizStakeHolderDetailTemp);
            }
            bizStakeHolderDetailDAO.persist(bizSupplierList);

            bizBuyerList = new ArrayList<BizStakeHolderDetail>();
            for (int i =0;i<buyerDetailList.size();i++){
                stakeHolderDetailViewTemp = buyerDetailList.get(i);
                bizStakeHolderDetailTemp = bizStakeHolderDetailTransform.transformToModel(stakeHolderDetailViewTemp,bizInfo);
                bizBuyerList.add(bizStakeHolderDetailTemp);
            }
            bizStakeHolderDetailDAO.persist(bizBuyerList);


        }catch (Exception e){
            log.error( "onSaveBizInfoToDB error" + e);
        }finally{

            log.info( "onSaveBizInfoToDB end" );
        }
    }
}
