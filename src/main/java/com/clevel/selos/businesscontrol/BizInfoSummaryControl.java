package com.clevel.selos.businesscontrol;

//import com.clevel.selos.dao.working.BizInfoSummaryDAO;
import com.clevel.selos.dao.working.BizInfoDetailDAO;
import com.clevel.selos.dao.working.BizInfoSummaryDAO;
import com.clevel.selos.dao.working.WorkCaseDAO;
import com.clevel.selos.model.db.working.BizInfoDetail;
import com.clevel.selos.model.db.working.BizInfoSummary;
import com.clevel.selos.model.db.working.WorkCase;
import com.clevel.selos.model.view.BizInfoDetailView;
import com.clevel.selos.transform.BizInfoDetailTransform;
import com.clevel.selos.transform.BizInfoSummaryTransform;
import org.slf4j.Logger;
import com.clevel.selos.model.view.BizInfoSummaryView;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

@Stateless
public class BizInfoSummaryControl {

    @Inject
    Logger log;
    @Inject
    BizInfoDetailTransform bizInfoDetailTransform;

    @Inject
    BizInfoSummaryTransform bizInfoSummaryTransform;
    @Inject
    BizInfoSummaryDAO bizInfoSummaryDAO;
    @Inject
    BizInfoDetailDAO bizInfoDetailDAO;
    @Inject
    WorkCaseDAO workCaseDAO;

    public void onSaveBizSummaryToDB(BizInfoSummaryView bizInfoSummaryView,long workCaseId){
        BizInfoSummary bizInfoSummary;

        WorkCase workCase = workCaseDAO.findById(workCaseId);

        bizInfoSummary = bizInfoSummaryTransform.transformToModel(bizInfoSummaryView);
        bizInfoSummary.setWorkCase(workCase);

        bizInfoSummaryDAO.persist(bizInfoSummary);
    }


    public BizInfoSummaryView onGetBizInfoSummaryByWorkCase(long workCaseId){
        log.info("onGetBizInfoSummaryByWorkCase ");

        BizInfoSummary bizInfoSummary;
        BizInfoSummaryView bizInfoSummaryView;

        WorkCase workCase = workCaseDAO.findById(workCaseId);

        log.info("workCase after findById " + workCase );

        bizInfoSummary  = bizInfoSummaryDAO.onSearchByWorkCase(workCase);

        if( bizInfoSummary != null){
            bizInfoSummaryView = bizInfoSummaryTransform.transformToView(bizInfoSummary);
        }else{
            bizInfoSummaryView = null;
        }
        return bizInfoSummaryView;
    }

    public List<BizInfoDetailView> onGetBizInfoDetailByBizInfoSummary(long bizInfoSummaryID){
        log.info("onGetBizInfoSummaryByWorkCase ");

        List<BizInfoDetail> bizInfoDetailList;
        List<BizInfoDetailView> bizInfoDetailViewList;
        BizInfoDetailView bizInfoDetailViewTemp;
        BizInfoDetail bizInfoDetailTemp;
        bizInfoDetailViewList = new ArrayList<BizInfoDetailView>();

        BizInfoSummary bizInfoSummary = bizInfoSummaryDAO.findById(bizInfoSummaryID);

        log.info("workCase after findById " + bizInfoSummary );

        bizInfoDetailList  = bizInfoDetailDAO.findByBizInfoSummaryId(bizInfoSummary);

        if( bizInfoDetailList.size() != 0){
            for(int i =0 ; i<bizInfoDetailList.size();i++){
                bizInfoDetailTemp =  bizInfoDetailList.get(i);
                bizInfoDetailViewTemp = bizInfoDetailTransform.transformToView(bizInfoDetailTemp);
                bizInfoDetailViewList.add(bizInfoDetailViewTemp);
            }
        }else{
            //bizInfoSummaryView = null;
        }
        return bizInfoDetailViewList;
    }


}
