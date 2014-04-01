package com.clevel.selos.businesscontrol;

import com.clevel.selos.dao.working.UWRuleResultSummaryDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.working.UWRuleResultDetail;
import com.clevel.selos.model.db.working.UWRuleResultSummary;
import com.clevel.selos.model.view.UWRuleResultDetailView;
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
    public UWRuleResultControl(){}



    public void saveUWRuleResultSummary(UWRuleResultSummaryView uwRuleResultSummaryView){
        logger.debug("-- begin saveUWRuleResultSummary UWRuleResultSummaryView {}", uwRuleResultSummaryView);
        UWRuleResultSummary uwRuleResultSummary = uwRuleResultTransform.transformToModel(uwRuleResultSummaryView);
        uwRuleResultSummary = uwRuleResultSummaryDAO.persist(uwRuleResultSummary);
        logger.debug("-- end saveUWRuleResultSummary {}", uwRuleResultSummary);
    }

    public void saveUWRuleResultDetail(UWRuleResultDetailView uwRuleResultDetailView){


    }

}
