package com.clevel.selos.transform;

import com.clevel.selos.dao.master.AccountStatusDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.master.AccountStatus;
import com.clevel.selos.model.view.AccountStatusView;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class AccountStatusTransform extends Transform {
    @SELOS
    @Inject
    private Logger log;

    @Inject
    AccountStatusDAO accountStatusDAO;

    @Inject
    public AccountStatusTransform() {
    }

    public AccountStatusView transformToView(AccountStatus accountStatus) {
        AccountStatusView accountStatusView = new AccountStatusView();
        if (accountStatus == null) {
            log.debug("transformToView() accountStatus is null!");
            return accountStatusView;
        }

        accountStatusView.setId(String.valueOf(accountStatus.getId()));
        accountStatusView.setStatus(accountStatus.getName());
        return accountStatusView;
    }

    public List<AccountStatusView> transformToViewList(List<AccountStatus> accountStatuses) {
        List<AccountStatusView> accountStatusViews = new ArrayList<AccountStatusView>();
        if (accountStatuses == null) {
            log.debug("transformToViewList() accountStatuses is null!");
            return accountStatusViews;
        }

        for (AccountStatus accountStatus : accountStatuses) {
            accountStatusViews.add(transformToView(accountStatus));
        }
        return accountStatusViews;
    }

    public AccountStatus transformToModel(AccountStatusView accountStatusView){
        log.debug("begin transformToModel(accountStatusView {})", accountStatusView);
        if(accountStatusView!=null && accountStatusView.getId() != null && !accountStatusView.getId().equalsIgnoreCase("0")){
            try{
                AccountStatus accountStatus = accountStatusDAO.findById(Integer.parseInt(accountStatusView.getId()));
                return accountStatus;
            } catch (Exception ex){
                log.info("cannot transform accountStatus for {}", accountStatusView);
            }
        }
        return null;
    }
}
