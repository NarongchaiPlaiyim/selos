package com.clevel.selos.busiensscontrol;

import com.clevel.selos.dao.working.TCGDAO;
import com.clevel.selos.dao.working.TCGDetailDAO;
import com.clevel.selos.model.db.working.NCBDetail;
import com.clevel.selos.model.db.working.TCG;
import com.clevel.selos.model.db.working.TCGDetail;
import com.clevel.selos.model.view.TCGDetailView;
import com.clevel.selos.model.view.TCGView;
import com.clevel.selos.transform.TCGDetailTransform;
import com.clevel.selos.transform.TCGTransform;
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
public class TCGInfoControl extends BusinessControl {
    @Inject
    Logger log;
    @Inject
    TCGDetailTransform tcgDetailTransform;
    @Inject
    TCGTransform tcgTransform;
    @Inject
    TCGDAO tcgDAO;
    @Inject
    TCGDetailDAO tcgDetailDAO;


    public void onSaveTCGToDB(TCGView tcgView, List<TCGDetailView> tcgDetailViewList){
        try{
            log.info("onSaveTCGToDB begin");

            TCG tcg = tcgTransform.transformTCGViewToModel(tcgView);
            tcgDAO.persist(tcg);
            log.info("persist tcg");
            List<TCGDetail> tcgDetailList = tcgDetailTransform.transformTCGDetailViewToModel(tcgDetailViewList,tcg) ;
            tcgDetailDAO.persist(tcgDetailList);
        }catch (Exception e){
            log.error( "onSaveTCGToDB error" + e);
        }finally{

            log.info( "onSaveTCGToDB end" );
        }



    }
}
