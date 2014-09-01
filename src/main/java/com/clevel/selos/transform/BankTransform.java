package com.clevel.selos.transform;

import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.master.Bank;
import com.clevel.selos.model.view.BankView;
import org.slf4j.Logger;

import javax.faces.model.SelectItem;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class BankTransform extends Transform {
    @SELOS
    @Inject
    private Logger log;

    @Inject
    public BankTransform() {
    }

    public BankView transformToView(Bank bank) {
        BankView bankView = new BankView();
        if (bank == null) {
            log.debug("transformToView() bank is null!");
            return bankView;
        }

        bankView.setCode(bank.getCode());
        bankView.setBankName(bank.getName());
        bankView.setBankShortName(bank.getShortName());
        return bankView;
    }

    public SelectItem transformToSelectItem(BankView bankView){
        if(bankView == null)
            return null;
        SelectItem selectItem = new SelectItem();
        selectItem.setLabel(bankView.getBankName());
        selectItem.setValue(bankView.getCode());
        return selectItem;
    }

    public List<BankView> getBankViewList(List<Bank> banks) {
        List<BankView> bankViews = new ArrayList<BankView>();
        if (banks == null) {
            log.debug("getBankViewList() banks is null!");
            return bankViews;
        }

        for (Bank bank : banks)
            bankViews.add(transformToView(bank));

        return bankViews;
    }

    public Map<Integer, BankView> transformToCache(List<Bank> bankList){
        if(bankList == null || bankList.size() == 0)
            return null;
        Map<Integer, BankView> _tmpMap = new ConcurrentHashMap<Integer, BankView>();
        for(Bank bank : bankList){
            BankView bankView = transformToView(bank);
            _tmpMap.put(bankView.getCode(), bankView);
        }
        return _tmpMap;
    }
}
