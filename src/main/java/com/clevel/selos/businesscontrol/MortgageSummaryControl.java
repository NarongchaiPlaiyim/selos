package com.clevel.selos.businesscontrol;

import com.clevel.selos.dao.working.MortgageSummaryDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.working.BasicInfo;
import com.clevel.selos.model.db.working.MortgageSummary;
import com.clevel.selos.model.view.MortgageSummaryView;
import com.clevel.selos.transform.BasicInfoTransform;
import com.clevel.selos.transform.MortgageSummaryTransform;
import org.slf4j.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;

@Stateless
public class MortgageSummaryControl extends BusinessControl {
    @Inject
    @SELOS
    Logger log;
    @Inject
    MortgageSummaryDAO mortgageSummaryDAO;

    @Inject
    MortgageSummaryTransform mortgageSummaryTransform;

    @Inject
    public MortgageSummaryControl(){

    }

    public MortgageSummaryView getMortgageSummaryViewByWorkCaseId(long workCaseId) {
        log.info("getMortgageSummaryViewByWorkCaseId ::: workCaseId : {}", workCaseId);
        MortgageSummary mortgageSummary = mortgageSummaryDAO.findByWorkCaseId(workCaseId);

        if (mortgageSummary == null) {
            mortgageSummary = new MortgageSummary();
        }

        MortgageSummaryView mortgageSummaryView = mortgageSummaryTransform.transformToView(mortgageSummary);

        log.info("getMortgageSummaryViewByWorkCaseId ::: mortgageSummaryView : {}", mortgageSummaryView);
        return mortgageSummaryView;
    }
}
