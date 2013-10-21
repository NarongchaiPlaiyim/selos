package com.clevel.selos.transform;

import com.clevel.selos.model.db.master.Bank;
import com.clevel.selos.model.view.BankView;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class BankTransform extends Transform{

    @Inject
    Logger log;

    @Inject
    public BankTransform(){
    }

    public BankView getBankView(Bank bank){
        BankView bankView = new BankView();
        if (bank == null) {
            log.debug("getBankView() bank is null!");
            return bankView;
        }

        bankView.setCode(bank.getCode());
        bankView.setBankName(bank.getName());
        bankView.setBankShortName(bank.getShortName());
        return bankView;
    }

    public List<BankView> getBankViewList(List<Bank> banks){
        List<BankView> bankViews = new ArrayList<BankView>();
        if (banks == null) {
            log.debug("getBankViewList() banks is null!");
            return bankViews;
        }

        for (Bank bank : banks)
            bankViews.add(getBankView(bank));

        return bankViews;
    }
}
