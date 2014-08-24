package com.clevel.selos.transform;


import com.clevel.selos.dao.master.BankAccountPurposeDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.master.BankAccountPurpose;
import com.clevel.selos.model.view.BankAccountPurposeView;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class BankAccountPurposeTransform extends Transform {

    @SELOS
    @Inject
    private Logger log;

    @Inject
    private BankAccountPurposeDAO bankAccountPurposeDAO;

    @Inject
    public BankAccountPurposeTransform(){
    }

    public Map<Long, BankAccountPurposeView> transformToCache(List<BankAccountPurpose> bankAccountPurposeList){
        if(bankAccountPurposeList == null)
            return null;

        Map<Long, BankAccountPurposeView> _tmpMap = new ConcurrentHashMap<Long, BankAccountPurposeView>();
        for(BankAccountPurpose bankAccountPurpose : bankAccountPurposeList){
            BankAccountPurposeView bankAccountPurposeView = transformToView(bankAccountPurpose);

            _tmpMap.put(bankAccountPurpose.getId(), bankAccountPurposeView);
        }
        return _tmpMap;
    }

    public BankAccountPurposeView transformToView(BankAccountPurpose bankAccountPurpose){
        if(bankAccountPurpose == null)
            return null;
        BankAccountPurposeView bankAccountPurposeView = new BankAccountPurposeView();
        bankAccountPurposeView.setId(bankAccountPurpose.getId());
        bankAccountPurposeView.setName(bankAccountPurpose.getName());
        bankAccountPurposeView.setActive(bankAccountPurpose.getActive());
        bankAccountPurposeView.setForOD(bankAccountPurpose.isForOD());
        bankAccountPurposeView.setForTCG(bankAccountPurpose.isForTCG());
        return bankAccountPurposeView;
    }

    public BankAccountPurpose transformToModel(BankAccountPurposeView bankAccountPurposeView){
        return bankAccountPurposeDAO.findById(bankAccountPurposeView.getId());
    }
}
