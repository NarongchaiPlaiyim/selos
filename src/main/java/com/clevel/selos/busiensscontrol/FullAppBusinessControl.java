package com.clevel.selos.busiensscontrol;

import com.clevel.selos.dao.working.*;
import com.clevel.selos.model.db.working.*;
import com.clevel.selos.model.view.*;
import com.clevel.selos.transform.*;
import org.slf4j.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

@Stateless
public class FullAppBusinessControl extends BusinessControl {
    @Inject
    Logger log;

    @Inject
    WorkCaseDAO workCaseDAO;
    @Inject
    BizInfoDetailDAO bizInfoDetailDAO;
    @Inject
    BizStakeholderDAO bizStakeholderDAO;
    @Inject
    BizProductDAO bizProductDAO;
    @Inject
    BizProductTransform bizProductTransform;
    @Inject
    BizStakeholderTransform bizStakeholderTransform;
    @Inject
    BizInfoDetailTransform bizInfoDetailTransform;
    @Inject
    NCBDAO ncbDAO;
    @Inject
    NCBDetailDAO ncbDetailDAO;

    public FullAppBusinessControl(){

    }

    public void save(WorkCase workCase){
        workCaseDAO.persist(workCase);
    }

    public void delete(WorkCase workCase){
        workCaseDAO.delete(workCase);
    }

    public void onSaveBizInfoDetailToDB(BizInfoFullView bizInfoFullView){
        List<BizStakeholder> bizSupplierList;
        List<BizStakeholder> bizBuyerList;
        List<BizProduct> bizProductList;
        List<StakeholderView> supplierList;
        List<StakeholderView> buyerList;
        List<BizProductView> bizProductViewList;

        BizInfoDetail bizInfoDetail;
        BizStakeholder bizStakeholderTemp;
        StakeholderView stakeholderViewTemp;
        BizProductView bizProductViewTemp;
        BizProduct bizProductTemp;
        try{
            log.info( "onSaveBizInfoDetailToDB begin" );


            bizInfoDetail = bizInfoDetailTransform.transformToModel(bizInfoFullView);
            bizInfoDetailDAO.persist(bizInfoDetail);

            supplierList = bizInfoFullView.getSupplierList();
            buyerList = bizInfoFullView.getBuyerList();
            bizProductViewList = bizInfoFullView.getBizProductViewList();

            bizProductList = new ArrayList<BizProduct>();
            for (int i =0;i<bizProductViewList.size();i++){
                bizProductViewTemp = bizProductViewList.get(i);
                bizProductTemp = bizProductTransform.transformToModel(bizProductViewTemp,bizInfoDetail);
                bizProductList.add(bizProductTemp);
             }
             bizProductDAO.persist(bizProductList);

            //bizProduct Add
            log.info( "bizProductList Size " + bizProductList.size());
            for (int i=0;i<bizProductList.size();i++){
                bizProductTemp =bizProductList.get(i);
                bizProductTemp.setBizInfoDetail(bizInfoDetail);
                bizProductList.set(i,bizProductTemp);
            }
            log.info( "bizProductList Add " );
            bizProductDAO.persist(bizProductList);



            bizSupplierList = new ArrayList<BizStakeholder>();

            for (int i =0;i<supplierList.size();i++){
                stakeholderViewTemp = supplierList.get(i);
                bizStakeholderTemp = bizStakeholderTransform.transformToModel(stakeholderViewTemp,bizInfoDetail);
                bizSupplierList.add(bizStakeholderTemp);
            }
            bizStakeholderDAO.persist(bizSupplierList);

            bizBuyerList = new ArrayList<BizStakeholder>();
            for (int i =0;i<buyerList.size();i++){
                stakeholderViewTemp = buyerList.get(i);
                bizStakeholderTemp = bizStakeholderTransform.transformToModel(stakeholderViewTemp,bizInfoDetail);
                bizBuyerList.add(bizStakeholderTemp);
            }
            bizStakeholderDAO.persist(bizBuyerList);


        }catch (Exception e){
              log.error( "onSaveBizInfoDetailToDB error" + e);
        }finally{

            log.info( "onSaveBizInfoDetailToDB end" );
        }

//        StakeholderView stakeholderTemp;
//        BizStakeholder bizStakeholderTemp;
//
//        BizProductView bizProductViewTemp;
//        BizProduct bizProductTemp;
//
//        List<BizStakeholder> bizSupplierList;
//        List<BizStakeholder> bizBuyerList;
//        List<BizProduct> bizProductList;
//
//
//        bizProductList = new ArrayList<BizProduct>();
//        for (int i =0;i<bizProductViewList.size();i++){
//            bizProductViewTemp = bizProductViewList.get(i);
//            bizProductTemp = onBizProductTransform(bizProductViewTemp);
//            bizProductList.add(bizProductTemp);
//        }
//
//        bizSupplierList = new ArrayList<BizStakeholder>();
//        for (int i =0;i<supplierList.size();i++){
//            stakeholderTemp = supplierList.get(i);
//            bizStakeholderTemp = onStakeholderTransform(stakeholderTemp);
//            bizStakeholderTemp.setStakeholderType(new BigDecimal(1));
//            bizSupplierList.add( bizStakeholderTemp);
//        }
//
//        bizBuyerList = new ArrayList<BizStakeholder>();
//        for (int i =0;i<buyerList.size();i++){
//            stakeholderTemp = buyerList.get(i);
//            bizStakeholderTemp = onStakeholderTransform(stakeholderTemp);
//            bizStakeholderTemp.setStakeholderType(new BigDecimal(2));
//            bizBuyerList.add(bizStakeholderTemp);

    }


}
