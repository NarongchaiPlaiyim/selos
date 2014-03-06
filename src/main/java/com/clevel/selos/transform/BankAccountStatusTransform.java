package com.clevel.selos.transform;

import com.clevel.selos.dao.master.BankAccountStatusDAO;
import com.clevel.selos.model.db.master.BankAccountStatus;
import com.clevel.selos.model.view.BankAccountStatusView;

import javax.inject.Inject;

public class BankAccountStatusTransform extends Transform {

    @Inject
    BankAccountTypeTransform bankAccountTypeTransform;

    @Inject
    BankAccountStatusDAO bankAccountStatusDAO;

    @Inject
    public BankAccountStatusTransform() {

    }

    public BankAccountStatusView getBankAccountStatusView(BankAccountStatus bankAccountStatus) {
        if (bankAccountStatus != null) {
            BankAccountStatusView bankAccountStatusView = new BankAccountStatusView();
            bankAccountStatusView.setId(bankAccountStatus.getId());
            bankAccountStatusView.setCode(bankAccountStatus.getCode());
            bankAccountStatusView.setDescription(bankAccountStatus.getDescription());
            bankAccountStatusView.setDataSource(bankAccountStatus.getDataSource());
            bankAccountStatusView.setActive(bankAccountStatus.getActive());
            log.debug("bankAccountStatusView ::: {}", bankAccountStatusView);
            return bankAccountStatusView;
        }
        return new BankAccountStatusView();
    }

    public BankAccountStatus getBankAccountStatus(BankAccountStatusView bankAccountStatusView){
        log.debug("getBankAccountStatus ::: bankAccountStatusView : {}", bankAccountStatusView);
        if(bankAccountStatusView != null){
            if(bankAccountStatusView.getId() != 0){
                BankAccountStatus bankAccountStatus = bankAccountStatusDAO.findById(bankAccountStatusView.getId());
                log.debug("getBankAccountStatus ::: bankAccountStatus : {}", bankAccountStatus);
                return bankAccountStatus;
            }
        }
        return null;
    }
}
