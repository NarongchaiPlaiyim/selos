package com.clevel.selos.transform;

import com.clevel.selos.dao.history.SubmitInfoHistoryDAO;
import com.clevel.selos.dao.master.StatusDAO;
import com.clevel.selos.dao.master.StepDAO;
import com.clevel.selos.dao.master.UserDAO;
import com.clevel.selos.model.db.history.SubmitInfoHistory;
import com.clevel.selos.model.view.SubmitInfoView;
import com.clevel.selos.util.Util;

import javax.inject.Inject;
import java.util.Date;

public class SubmitInfoTransform extends Transform {

    @Inject
    SubmitInfoHistoryDAO submitInfoHistoryDAO;
    @Inject
    StepDAO stepDAO;
    @Inject
    StatusDAO statusDAO;
    @Inject
    UserDAO userDAO;

    @Inject
    public SubmitInfoTransform(){

    }

    public SubmitInfoHistory transformToModel(SubmitInfoView submitInfoView){
        SubmitInfoHistory submitInfoHistory = new SubmitInfoHistory();
        if(submitInfoView.getId() != 0)
            submitInfoHistory = submitInfoHistoryDAO.findById(submitInfoView.getId());

        submitInfoHistory.setAppNumber(submitInfoView.getAppNumber());
        submitInfoHistory.setStep((submitInfoView.getStep() != null && submitInfoView.getStep().getId() != 0) ? stepDAO.findById(submitInfoView.getStep().getId()) : null);
        submitInfoHistory.setStatus((submitInfoView.getStatus() != null && submitInfoView.getStatus().getId() != 0) ? statusDAO.findById(submitInfoView.getStatus().getId()) : null);
        submitInfoHistory.setFromUser((submitInfoView.getFromUser() != null && !Util.isEmpty(submitInfoView.getFromUser().getId())) ? userDAO.findById(submitInfoView.getFromUser().getId()) : null);
        submitInfoHistory.setToUser((submitInfoView.getToUser() != null && !Util.isEmpty(submitInfoView.getToUser().getId())) ? userDAO.findById(submitInfoView.getToUser().getId()) : null);
        submitInfoHistory.setSubmitDate(new Date());
        submitInfoHistory.setSubmitType(submitInfoView.getSubmitType());

        return submitInfoHistory;
    }

    public SubmitInfoView transformToView(SubmitInfoHistory submitInfoHistory){
        SubmitInfoView submitInfoView = new SubmitInfoView();

        submitInfoView.setId(submitInfoHistory.getId());
        submitInfoView.setStep(submitInfoHistory.getStep());
        submitInfoView.setStatus(submitInfoHistory.getStatus());
        submitInfoView.setRemark(submitInfoHistory.getRemark());
        submitInfoView.setFromUser(submitInfoHistory.getFromUser());
        submitInfoView.setToUser(submitInfoHistory.getToUser());
        submitInfoView.setSubmitDate(submitInfoHistory.getSubmitDate());
        submitInfoView.setSubmitType(submitInfoHistory.getSubmitType());

        return submitInfoView;
    }
}
