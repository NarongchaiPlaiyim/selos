package com.clevel.selos.transform;


import com.clevel.selos.dao.master.BankAccountTypeDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.master.BankAccountType;
import com.clevel.selos.model.view.master.BankAccountTypeView;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class BankAccountTypeTransform extends Transform {
    @SELOS
    @Inject
    private Logger log;

    @Inject
    BankAccountTypeDAO bankAccountTypeDAO;

    @Inject
    public BankAccountTypeTransform() {
    }

    public BankAccountTypeView getBankAccountTypeView(BankAccountType bankAccountType) {
        if (bankAccountType != null) {
            BankAccountTypeView bankAccountTypeView = new BankAccountTypeView();
            bankAccountTypeView.setId(bankAccountType.getId());
            bankAccountTypeView.setName(bankAccountType.getName());
            bankAccountTypeView.setShortName(bankAccountType.getShortName());
            bankAccountTypeView.setOpenAccountFlag(bankAccountType.getOpenAccountFlag());
            bankAccountTypeView.setBankStatementFlag(bankAccountType.getBankStatementFlag());
            bankAccountTypeView.setOthBankStatementFlag(bankAccountType.getOthBankStatementFlag());
            bankAccountTypeView.setActive(bankAccountType.getActive());
            return bankAccountTypeView;
        }
        return null;
    }

    public BankAccountType getBankAccountType(BankAccountTypeView bankAccountTypeView) {
        if(bankAccountTypeView == null || bankAccountTypeView.getId() == 0)
            return null;
        BankAccountType bankAccountType = bankAccountTypeDAO.findById(bankAccountTypeView.getId());
        return bankAccountType;
    }

    public List<BankAccountTypeView> getBankAccountTypeView(List<BankAccountType> bankAccountTypes) {
        List<BankAccountTypeView> bankAccountTypeViews = new ArrayList<BankAccountTypeView>();
        if (bankAccountTypes == null) {
            log.debug("getBankAccountTypeView() bankAccountTypes is null!");
            return bankAccountTypeViews;
        }

        for (BankAccountType bankAccountType : bankAccountTypes) {
            bankAccountTypeViews.add(getBankAccountTypeView(bankAccountType));
        }
        return bankAccountTypeViews;
    }

    public Map<Integer, BankAccountTypeView> transformToCache(List<BankAccountType> bankAccountTypeList){
        if(bankAccountTypeList == null)
            return null;
        Map<Integer, BankAccountTypeView> _tmpMap = new ConcurrentHashMap<Integer, BankAccountTypeView>();
        for(BankAccountType bankAccountType : bankAccountTypeList){
            BankAccountTypeView bankAccountTypeView = getBankAccountTypeView(bankAccountType);
            _tmpMap.put(bankAccountTypeView.getId(), bankAccountTypeView);
        }
        return _tmpMap;
    }
}
