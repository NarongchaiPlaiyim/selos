package com.clevel.selos.busiensscontrol;

import com.clevel.selos.dao.working.NCBDAO;
import com.clevel.selos.dao.working.NCBDetailDAO;
import com.clevel.selos.model.db.working.NCB;
import com.clevel.selos.model.db.working.NCBDetail;
import com.clevel.selos.model.view.NCBDetailView;
import com.clevel.selos.model.view.NCBInfoView;

import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: SUKANDA CHITSUP
 * Date: 26/9/2556
 * Time: 16:01 น.
 * To change this template use File | Settings | File Templates.
 */
public class NCBInfoControl extends BusinessControl {
    @Inject
    Logger log;
    @Inject
    com.clevel.selos.transform.NCBDetailTransform NCBDetailTransform;
    @Inject
    com.clevel.selos.transform.NCBTransform NCBTransform;
    @Inject
    NCBDAO ncbDAO;
    @Inject
    NCBDetailDAO ncbDetailDAO;
    public void onSaveNCBToDB(NCBInfoView NCBInfoView, List<NCBDetailView> NCBDetailViewList){
        try{
            log.info("onSaveNCBToDB begin");

            NCB ncb = NCBTransform.transformToModel(NCBInfoView);
            ncbDAO.persist(ncb);
            log.info("persist ncb");
            List<NCBDetail> ncbDetailList = NCBDetailTransform.transformToModel(NCBDetailViewList,ncb) ;
            ncbDetailDAO.persist(ncbDetailList);
        }catch (Exception e){
            log.error( "onSaveNCBToDB error" + e);
        }finally{

            log.info( "onSaveNCBToDB end" );
        }



    }
}
