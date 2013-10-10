package com.clevel.selos.dao.working;

import com.clevel.selos.model.db.working.BizInfoSummary;
import com.clevel.selos.dao.GenericDAO;
import org.slf4j.Logger;

import javax.inject.Inject;

public class BizInfoSummaryDAO extends GenericDAO<BizInfoSummary,Long> {
    @Inject
    private Logger log;

    @Inject
    public BizInfoSummaryDAO() {
    }


}
