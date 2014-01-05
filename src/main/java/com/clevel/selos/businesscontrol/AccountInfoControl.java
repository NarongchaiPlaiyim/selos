package com.clevel.selos.businesscontrol;

import com.clevel.selos.dao.working.*;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.master.User;
import com.clevel.selos.model.db.working.AccountInfo;
import com.clevel.selos.model.db.working.AccountInfoDetail;
import com.clevel.selos.model.db.working.WorkCase;
import com.clevel.selos.model.view.AccountInfoDetailView;
import com.clevel.selos.model.view.AccountInfoView;
import com.clevel.selos.transform.AccountInfoDetailTransform;
import com.clevel.selos.transform.AccountInfoTransform;
import com.clevel.selos.util.Util;
import org.slf4j.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.io.Serializable;
import java.util.List;

@Stateless
public class AccountInfoControl extends BusinessControl implements Serializable {
    @Inject
    @SELOS
    private Logger log;
    @Inject
    private WorkCaseDAO workCaseDAO;
    @Inject
    private OpenAccountDAO openAccountDAO;
    @Inject
    private OpenAccPurposeDAO openAccPurposeDAO;
    @Inject
    private AccountInfoDAO accountInfoDAO;
    @Inject
    private AccountInfoDetailDAO accountInfoDetailDAO;
    @Inject
    private AccountInfoTransform accountInfoTransform;
    @Inject
    private AccountInfoDetailTransform accountInfoDetailTransform;

    private AccountInfo accountInfo;
    private WorkCase workCase;
    private List<AccountInfoDetailView> accountInfoDetailViewList;
    private AccountInfoDetail accountInfoDetail;

    @Inject
    public AccountInfoControl() {

    }

    public AccountInfoView getAccountInfo(long workCaseId) {
        log.info("getAccountInfo ::: workCaseId : {}", workCaseId);
        accountInfo = accountInfoDAO.findByWorkCaseId(workCaseId);
        workCase = workCaseDAO.findById(workCaseId);

        if (accountInfo == null) {
            accountInfo = new AccountInfo();
        }

        AccountInfoView accountInfoView = null;// basicInfoTransform.transformToView(basicInfo, workCase);

        log.info("getAccountInfo ::: AccountInfoView : {}", accountInfoView);
        return accountInfoView;
    }

    public void saveAccountInfo(final AccountInfoView accountInfoView,final long workCaseId){
        log.debug("saveAccountInfo()");
        User user = getCurrentUser();

        workCase = workCaseDAO.findById(workCaseId);

        accountInfo = accountInfoTransform.transformToModel(accountInfoView, workCase, user);
        accountInfoDAO.persist(accountInfo);

//        accountInfoDetailViewList = Util.safetyList(accountInfoView.getAccountInfoDetailViewList());
//        for(AccountInfoDetailView dialogView : accountInfoDetailViewList ){
//            accountInfoDetail = accountInfoDetailTransform.transformToModel(dialogView, accountInfo);
//            if(null != accountInfoDetail){
//                accountInfoDetailDAO.persist(accountInfoDetail);
//            }
//        }


    }

    private void a(){

    }

    private void b(){

    }
}
