package com.clevel.selos.transform;

import com.clevel.selos.model.db.master.Bank;
import com.clevel.selos.model.view.BankView;

import javax.inject.Inject;

public class BankTransform extends Transform{

    @Inject
    public BankTransform(){

    }

    public BankView getBankView(Bank bank){
        BankView bankView = new BankView();
        bankView.setCode(bank.getCode());
        bankView.setBankName(bank.getName());
        bankView.setBankShortName(bank.getShortName());
        return bankView;
    }
}
