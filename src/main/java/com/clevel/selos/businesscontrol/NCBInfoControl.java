package com.clevel.selos.businesscontrol;

import com.clevel.selos.dao.working.NCBDAO;
import com.clevel.selos.dao.working.NCBDetailDAO;
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

/**
 * Created with IntelliJ IDEA.
 * User: SUKANDA CHITSUP
 * Date: 26/9/2556
 * Time: 16:01 น.
 * To change this template use File | Settings | File Templates.
 */

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
    public void NCBInfoControl(){

    }

    public void onSaveNCBToDB(NCBInfoView NCBInfoView, List<NCBDetailView> NCBDetailViewList){
        try{
            log.info("onSaveNCBToDB begin");

            NCB ncb = ncbTransform.transformToModel(NCBInfoView);
            ncbDAO.persist(ncb);
            log.info("persist ncb");
            List<NCBDetail> ncbDetailList = ncbDetailTransform.transformToModel(NCBDetailViewList,ncb) ;
            ncbDetailDAO.persist(ncbDetailList);
        }catch (Exception e){
            log.error( "onSaveNCBToDB error" + e);
        }finally{

            log.info( "onSaveNCBToDB end" );
        }



    }
}
