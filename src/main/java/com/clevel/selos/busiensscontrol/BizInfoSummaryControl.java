package com.clevel.selos.busiensscontrol;

import com.clevel.selos.dao.working.BizInfoSummaryDAO;
import com.clevel.selos.dao.working.WorkCaseDAO;
import com.clevel.selos.model.db.working.BizInfoSummary;
import com.clevel.selos.model.db.working.WorkCase;
import com.clevel.selos.transform.business.BizInfoSummaryTransform;
import org.slf4j.Logger;
import com.clevel.selos.model.view.BizInfoSummaryView;

import javax.ejb.Stateless;
import javax.inject.Inject;
@Stateless
public class BizInfoSummaryControl {

    @Inject
    Logger log;
    @Inject
    BizInfoSummaryTransform bizInfoSummaryTransform;
    @Inject
    BizInfoSummaryDAO bizInfoSummaryDAO;
    @Inject
    WorkCaseDAO workCaseDAO;

    public void onSaveBizSummaryToDB(BizInfoSummaryView bizInfoSummaryView,long workCaseId){
        BizInfoSummary bizInfoSummary;

        WorkCase workCase = workCaseDAO.findById(workCaseId);

        bizInfoSummary = bizInfoSummaryTransform.transformToModel(bizInfoSummaryView);
        bizInfoSummary.setWorkCase(workCase);

        bizInfoSummaryDAO.persist(bizInfoSummary);
    }
}
