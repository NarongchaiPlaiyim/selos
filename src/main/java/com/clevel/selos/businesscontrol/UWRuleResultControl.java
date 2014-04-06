package com.clevel.selos.businesscontrol;

import com.clevel.selos.dao.working.UWRuleResultDetailDAO;
import com.clevel.selos.dao.working.UWRuleResultSummaryDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.working.UWRuleResultSummary;
import com.clevel.selos.model.view.UWRuleResultSummaryView;
import com.clevel.selos.transform.UWRuleResultTransform;
import org.slf4j.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;

@Stateless
public class UWRuleResultControl extends BusinessControl{

    @Inject
    @SELOS
    private Logger logger;

    @Inject
    private UWRuleResultTransform uwRuleResultTransform;
    @Inject
    private UWRuleResultSummaryDAO uwRuleResultSummaryDAO;
    @Inject
    private UWRuleResultDetailDAO uwRuleResultDetailDAO;

    @Inject
    public UWRuleResultControl(){}

    public void saveNewUWRuleResult(UWRuleResultSummaryView uwRuleResultSummaryView){
        logger.debug("-- begin saveNewUWRuleResult UWRuleResultSummaryView {}", uwRuleResultSummaryView);
        //Delete old list first.
        deleteUWRuleResult(uwRuleResultSummaryView);
        UWRuleResultSummary uwRuleResultSummary = uwRuleResultTransform.transformToModel(uwRuleResultSummaryView);
        uwRuleResultSummary = uwRuleResultSummaryDAO.persist(uwRuleResultSummary);
        uwRuleResultDetailDAO.persist(uwRuleResultSummary.getUwRuleResultDetailList());
        logger.debug("-- end saveNewUWRuleResult {}", uwRuleResultSummary);
    }

    public void deleteUWRuleResult(UWRuleResultSummaryView uwRuleResultSummaryView){
        logger.debug("-- begin deleteUWRuleResult --");
        UWRuleResultSummary uwRuleResultSummary;
        if(uwRuleResultSummaryView.getWorkCasePrescreenId() > 0)
            uwRuleResultSummary = uwRuleResultSummaryDAO.findByWorkcasePrescreenId(uwRuleResultSummaryView.getWorkCasePrescreenId());
        else
            uwRuleResultSummary = uwRuleResultSummaryDAO.findByWorkcaseId(uwRuleResultSummaryView.getWorkCaseId());
        uwRuleResultDetailDAO.delete(uwRuleResultSummary.getUwRuleResultDetailList());
        uwRuleResultSummaryDAO.delete(uwRuleResultSummary);
        logger.debug("-- end deleteUWRuleResult --");
    }

    public UWRuleResultSummaryView getUWRuleResultByWorkCasePrescreenId(long workCasePrescreenId){
        logger.debug("-- begin getUWRuleResultByWorkCasePrescreenId {}", workCasePrescreenId);
        UWRuleResultSummary uwRuleResultSummary = uwRuleResultSummaryDAO.findByWorkcasePrescreenId(workCasePrescreenId);
        UWRuleResultSummaryView uwRuleResultSummaryView = uwRuleResultTransform.transformToView(uwRuleResultSummary);
        logger.info("-- end getUWRuleResultByWorkCasePrescreenId return{}", uwRuleResultSummaryView);
        return uwRuleResultSummaryView;
    }

    public UWRuleResultSummaryView getUWRuleResultByWorkCaseId(long workCaseId){
        logger.debug("-- begin getUWRuleResultByWorkCaseId {}", workCaseId);
        UWRuleResultSummary uwRuleResultSummary = uwRuleResultSummaryDAO.findByWorkcaseId(workCaseId);
        UWRuleResultSummaryView uwRuleResultSummaryView = uwRuleResultTransform.transformToView(uwRuleResultSummary);
        logger.info("-- end getUWRuleResultByWorkCaseId return{}", uwRuleResultSummaryView);
        return uwRuleResultSummaryView;
    }
}
