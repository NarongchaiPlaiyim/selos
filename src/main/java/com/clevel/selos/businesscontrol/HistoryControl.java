package com.clevel.selos.businesscontrol;

import com.clevel.selos.dao.history.SubmitInfoHistoryDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.history.SubmitInfoHistory;
import com.clevel.selos.model.view.SubmitInfoView;
import com.clevel.selos.transform.SubmitInfoTransform;
import org.slf4j.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;

@Stateless
public class HistoryControl extends BusinessControl {
    @Inject
    @SELOS
    private Logger log;

    @Inject
    private SubmitInfoHistoryDAO submitInfoHistoryDAO;

    @Inject
    private SubmitInfoTransform submitInfoTransform;

    @Inject
    public HistoryControl(){

    }

    public void saveSubmitHistory(SubmitInfoView submitInfoView){
        SubmitInfoHistory submitInfoHistory = submitInfoTransform.transformToModel(submitInfoView);
        submitInfoHistoryDAO.persist(submitInfoHistory);
    }
}
