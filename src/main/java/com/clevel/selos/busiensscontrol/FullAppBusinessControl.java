package com.clevel.selos.busiensscontrol;

import com.clevel.selos.dao.working.BizInfoDetailDAO;
import com.clevel.selos.dao.working.WorkCaseDAO;
import com.clevel.selos.model.db.working.BizInfoDetail;
import com.clevel.selos.model.db.working.WorkCase;
import com.clevel.selos.model.view.BizInfoFullView;
import org.slf4j.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;

@Stateless
public class FullAppBusinessControl extends BusinessControl {
    @Inject
    Logger log;

    @Inject
    WorkCaseDAO workCaseDAO;
    @Inject
    BizInfoDetailDAO bizInfoDetailDAO;

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
        try{
            log.info( "onSaveBizInfoDetailToDB begin" );
            bizInfoDetailDAO.persist(bizInfoDetail);

        }catch (Exception e){
              log.error( "onSaveBizInfoDetailToDB error" );
        }finally{

            log.info( "onSaveBizInfoDetailToDB end" );
        }



    }
}
