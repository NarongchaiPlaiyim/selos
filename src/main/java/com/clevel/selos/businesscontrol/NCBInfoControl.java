package com.clevel.selos.businesscontrol;

import com.clevel.selos.dao.working.CustomerDAO;
import com.clevel.selos.dao.working.NCBDAO;
import com.clevel.selos.dao.working.NCBDetailDAO;
import com.clevel.selos.dao.working.WorkCaseDAO;
import com.clevel.selos.model.db.working.NCB;
import com.clevel.selos.model.db.working.NCBDetail;
import com.clevel.selos.model.view.NCBDetailView;
import com.clevel.selos.model.view.NCBInfoView;
import com.clevel.selos.transform.NCBDetailTransform;
import com.clevel.selos.transform.NCBTransform;
import org.slf4j.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.List;


@Stateless
public class NCBInfoControl extends BusinessControl {
    @Inject
    Logger log;
    @Inject
    NCBDetailTransform ncbDetailTransform;
    @Inject
    NCBTransform ncbTransform;
    @Inject
    NCBDAO ncbDAO;
    @Inject
    NCBDetailDAO ncbDetailDAO;
    @Inject
    WorkCaseDAO workCaseDAO;
    @Inject
    private CustomerDAO customerDAO;

    @Inject
    public void NCBInfoControl(){

    }

    public void onSaveNCBToDB(NCBInfoView NCBInfoView, List<NCBDetailView> NCBDetailViewList){
        log.info("onSaveNCBToDB begin");

        NCB ncb = ncbTransform.transformToModel( NCBInfoView );
        ncbDAO.persist(ncb);
        log.info("persist ncb");

        List<NCBDetail> NCBDetailListToDelete =  ncbDetailDAO.findNCBDetailByNcbId(ncb.getId());
        log.info("NCBDetailListToDelete :: {}",NCBDetailListToDelete.size());
        ncbDetailDAO.delete(NCBDetailListToDelete);
        log.info("delete NCBDetailListToDelete");

        List<NCBDetail> ncbDetailList = ncbDetailTransform.transformToModel(NCBDetailViewList,ncb) ;
        ncbDetailDAO.persist(ncbDetailList);

    }


    public NCBInfoView getNCBInfoView(long customerId){
        log.info("getNcbInfoView :: customer id  :: {}", customerId);
        NCBInfoView ncbInfoView = null;

        try{
            NCB ncb =  ncbDAO.findNcbByCustomer(customerId);
            if(ncb != null){
                log.info("ncb :: {} ",ncb.getId());
                ncbInfoView  = ncbTransform.transformToView(ncb);
            }
        }catch (Exception e){
            log.error( "getNcbInfoView error :: " + e.getMessage());
        }finally{
            log.info( "getNcbInfoView end" );
        }

        return ncbInfoView;
    }

    public List<NCBDetailView> getNcbDetailListView(NCBInfoView ncbInfoView){
        log.info("getNcbDetailListView :: ncbId  :: {}", ncbInfoView.getId());
        List<NCBDetailView> ncbDetailViewList = null;

        try{
            List<NCBDetail> NCBDetailList =  ncbDetailDAO.findNCBDetailByNcbId(ncbInfoView.getId());

            if(NCBDetailList.size() > 0){
                ncbDetailViewList  = ncbDetailTransform.transformToView(NCBDetailList);
            }

        }catch (Exception e){
            log.error( "getNcbDetailListView error :: " + e.getMessage());
        }finally{
            log.info( "getNcbDetailListView end" );
        }

        return ncbDetailViewList;
    }

}
