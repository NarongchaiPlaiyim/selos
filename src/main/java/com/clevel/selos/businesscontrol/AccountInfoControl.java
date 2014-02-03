package com.clevel.selos.businesscontrol;

import com.clevel.selos.dao.working.*;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.master.User;
import com.clevel.selos.model.db.working.*;
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
    private OpenAccountPurposeDAO openAccPurposeDAO;
    @Inject
    private AccountInfoDAO accountInfoDAO;
    @Inject
    private AccountInfoDetailDAO accountInfoDetailDAO;
    @Inject
    private AccountInfoDetailAccountNameDAO accountInfoDetailAccountNameDAO;
    @Inject
    private AccountInfoDetailPurposeDAO accountInfoDetailPurposeDAO;
    @Inject
    private AccountInfoDetailCreditTypeDAO accountInfoDetailCreditTypeDAO;
    @Inject
    private AccountInfoTransform accountInfoTransform;
    @Inject
    private AccountInfoDetailTransform accountInfoDetailTransform;

    private AccountInfo accountInfo;
    private WorkCase workCase;
    private User user;
    private List<AccountInfoDetailView> accountInfoDetailViewList;
    private List<AccountInfoDetail> accountInfoDetailList;
    private AccountInfoDetail accountInfoDetail;

    private List<AccountInfoDetailAccountName> accountInfoDetailAccountNameList;
    private List<AccountInfoDetailPurpose> accountInfoDetailPurposeList;
    private List<AccountInfoDetailCreditType> accountInfoDetailCreditTypeList;

    @Inject
    public AccountInfoControl() {

    }
    public AccountInfoView getAccountInfo(long workCaseId) {
        log.info("-- getAccountInfo ::: workCaseId : {}", workCaseId);
        accountInfo = accountInfoDAO.findByWorkCaseId(workCaseId);
        AccountInfoView accountInfoView = null;
        if (accountInfo != null) {
            accountInfoView = accountInfoTransform.transformToView(accountInfo);
            log.info("-- getAccountInfo ::: AccountInfoView : {}", accountInfoView);
            return accountInfoView;
        } else {
            return accountInfoView;
        }
    }
    public void saveAccountInfo(final AccountInfoView accountInfoView,final long workCaseId){
        log.debug("-- saveAccountInfo({}, {})", accountInfoView.toString(), workCaseId);

        user = getCurrentUser();
        workCase = workCaseDAO.findById(workCaseId);

        accountInfo = accountInfoTransform.transformToModel(accountInfoView, workCase, user);
        accountInfoDAO.persist(accountInfo);

        accountInfoDetailList = safetyList(accountInfoDetailDAO.findByAccountInfoDetailId(accountInfo.getId()));
        if(accountInfoDetailList.size() > 0){
            clearDB(accountInfoDetailList);
            accountInfoDetailDAO.delete(accountInfoDetailList);
            accountInfoDetailViewList = accountInfoView.getAccountInfoDetailViewList();
            insertToDB(accountInfoDetailViewList);
        } else {
            accountInfoDetailDAO.delete(accountInfoDetailList);
            accountInfoDetailViewList = accountInfoView.getAccountInfoDetailViewList();
            insertToDB(accountInfoDetailViewList);
        }
    }
    private void insertToDB(List<AccountInfoDetailView> accountInfoDetailViewList){
        accountInfoDetailViewList = safetyList(accountInfoDetailViewList);
        for(AccountInfoDetailView dialogView : accountInfoDetailViewList ){
            accountInfoDetail = accountInfoDetailTransform.transformToModel(dialogView, accountInfo);
            if(null != accountInfoDetail){
                accountInfoDetailDAO.persist(accountInfoDetail);

                accountInfoDetailAccountNameList = safetyList(accountInfoDetail.getAccountNameList());
                accountInfoDetailAccountNameDAO.persist(accountInfoDetailAccountNameList);

                accountInfoDetailPurposeList = safetyList(accountInfoDetail.getPurposeList());
                accountInfoDetailPurposeDAO.persist(accountInfoDetailPurposeList);

                accountInfoDetailCreditTypeList = safetyList(accountInfoDetail.getCreditTypeList());
                accountInfoDetailCreditTypeDAO.persist(accountInfoDetailCreditTypeList);
            } else {
                log.debug("-- Account Information is null");
            }
        }
    }
    private void clearDB(final List<AccountInfoDetail> accountInfoDetailList){
        long id;
        for(AccountInfoDetail infoDetail : accountInfoDetailList ){
            id = infoDetail.getId();

            accountInfoDetailAccountNameList = safetyList(accountInfoDetailAccountNameDAO.findByAccountInfoDetailAccountNameId(id));
            accountInfoDetailAccountNameDAO.delete(accountInfoDetailAccountNameList);

            accountInfoDetailPurposeList = safetyList(accountInfoDetailPurposeDAO.findByAccountInfoDetailPurposeId(id));
            accountInfoDetailPurposeDAO.delete(accountInfoDetailPurposeList);

            accountInfoDetailCreditTypeList = safetyList(accountInfoDetailCreditTypeDAO.findByAccountInfoDetailCreditTypeId(id));
            accountInfoDetailCreditTypeDAO.delete(accountInfoDetailCreditTypeList);
        }
    }
    private <T> List<T> safetyList(List<T> list) {
        return Util.safetyList(list);
    }
}
