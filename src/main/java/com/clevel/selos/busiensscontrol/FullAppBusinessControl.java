package com.clevel.selos.busiensscontrol;

import com.clevel.selos.dao.working.BizInfoDetailDAO;
import com.clevel.selos.dao.working.BizProductDAO;
import com.clevel.selos.dao.working.BizStakeholderDAO;
import com.clevel.selos.dao.working.WorkCaseDAO;
import com.clevel.selos.model.db.working.BizInfoDetail;
import com.clevel.selos.model.db.working.BizProduct;
import com.clevel.selos.model.db.working.BizStakeholder;
import com.clevel.selos.model.db.working.WorkCase;
import com.clevel.selos.model.view.BizInfoFullView;
import org.slf4j.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
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
    public FullAppBusinessControl(){

    }

    public void save(WorkCase workCase){
        workCaseDAO.persist(workCase);
    }

    public void delete(WorkCase workCase){
        workCaseDAO.delete(workCase);
    }

    public void onSaveBizInfoDetailToDB(BizInfoDetail bizInfoDetail){
        List<BizStakeholder> bizSupplierList;
        List<BizStakeholder> bizBuyerList;
        List<BizProduct> bizProductList;
        try{
            log.info( "onSaveBizInfoDetailToDB begin" );
            bizInfoDetailDAO.persist(bizInfoDetail);

            bizSupplierList = bizInfoDetail.getSupplierList();
            bizBuyerList = bizInfoDetail.getBuyerList();
            bizProductList = bizInfoDetail.getBizProductList();

            log.info( "bizProductList  at BizControl Size " + bizProductList.size());
            log.info( "bizSupplierList at BizControl Size " + bizSupplierList.size());
            log.info( "bizBuyerList    at BizControl Size " + bizBuyerList.size());

            BizStakeholder bizStakeholderTemp;
            BizProduct bizProductTemp;
            //bizProduct Add
            log.info( "bizProductList Size " + bizProductList.size());
            for (int i=0;i<bizProductList.size();i++){
                bizProductTemp =bizProductList.get(i);
                bizProductTemp.setBizInfoDetail(bizInfoDetail);
                bizProductList.set(i,bizProductTemp);
            }
            log.info( "bizProductList Add " );
            bizProductDAO.persist(bizProductList);

            //supplier Add
            log.info( "supplierList Add");
            for (int i=0;i<bizSupplierList.size();i++){
                bizStakeholderTemp =bizSupplierList.get(i);
                bizStakeholderTemp.setBizInfoDetail(bizInfoDetail);
                bizStakeholderDAO.persist(bizStakeholderTemp);
            }

            //buyer Add
            log.info( "buyerList Add");
            for (int i=0;i<bizBuyerList.size();i++){
                bizStakeholderTemp =bizBuyerList.get(i);
                bizStakeholderTemp.setBizInfoDetail(bizInfoDetail);
                bizStakeholderDAO.persist(bizStakeholderTemp);
            }
        }catch (Exception e){
              log.error( "onSaveBizInfoDetailToDB error" + e);
        }finally{

            log.info( "onSaveBizInfoDetailToDB end" );
        }



    }
}
