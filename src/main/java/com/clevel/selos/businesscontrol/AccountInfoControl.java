package com.clevel.selos.businesscontrol;

import com.clevel.selos.dao.working.AccountInfoDAO;
import com.clevel.selos.dao.working.OpenAccPurposeDAO;
import com.clevel.selos.dao.working.OpenAccountDAO;
import com.clevel.selos.dao.working.WorkCaseDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.master.User;
import com.clevel.selos.model.db.working.AccountInfo;
import com.clevel.selos.model.db.working.WorkCase;
import com.clevel.selos.model.view.AccountInfoView;
import com.clevel.selos.transform.AccountInfoTransform;
import org.slf4j.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.io.Serializable;

@Stateless
public class AccountInfoControl extends BusinessControl implements Serializable {
    @Inject
    @SELOS
    Logger log;
    @Inject
    WorkCaseDAO workCaseDAO;
    @Inject
    OpenAccountDAO openAccountDAO;
    @Inject
    OpenAccPurposeDAO openAccPurposeDAO;
    @Inject
    AccountInfoDAO accountInfoDAO;
    @Inject
    AccountInfoTransform accountInfoTransform;

    @Inject
    public AccountInfoControl() {

    }

    public AccountInfoView getAccountInfo(long workCaseId) {
        log.info("getAccountInfo ::: workCaseId : {}", workCaseId);
        AccountInfo accountInfo = accountInfoDAO.findByWorkCaseId(workCaseId);
        WorkCase workCase = workCaseDAO.findById(workCaseId);

        if (accountInfo == null) {
            accountInfo = new AccountInfo();
        }

        AccountInfoView accountInfoView = null;// basicInfoTransform.transformToView(basicInfo, workCase);

        log.info("getAccountInfo ::: AccountInfoView : {}", accountInfoView);
        return accountInfoView;
    }

    public void saveAccountInfo(AccountInfoView accountInfoView, long workCaseId){
        User user = getCurrentUser();

        WorkCase workCase = workCaseDAO.findById(workCaseId);

        AccountInfo accountInfo = accountInfoTransform.transformToModel(accountInfoView, workCase, user);
        accountInfoDAO.persist(accountInfo);
    }
}
