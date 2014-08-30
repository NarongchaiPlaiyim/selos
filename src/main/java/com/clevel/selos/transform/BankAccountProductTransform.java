package com.clevel.selos.transform;

import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.master.BankAccountProduct;
import com.clevel.selos.model.view.master.BankAccountProductView;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class BankAccountProductTransform extends Transform{

    @Inject
    @SELOS
    private Logger logger;

    @Inject
    public BankAccountProductTransform(){}

    public BankAccountProductView transformToView(BankAccountProduct bankAccountProduct){
        if(bankAccountProduct == null)
            return null;
        BankAccountProductView bankAccountProductView = new BankAccountProductView();
        bankAccountProductView.setId(bankAccountProduct.getId());
        bankAccountProductView.setName(bankAccountProduct.getName());
        if(bankAccountProduct.getBankAccountType() != null)
            bankAccountProductView.setBankAccountTypeId(bankAccountProduct.getBankAccountType().getId());
        if(bankAccountProduct.getCollateralType() != null)
            bankAccountProductView.setCollateralTypeId(bankAccountProduct.getCollateralType().getId());
        if(bankAccountProduct.getSubCollateralType() != null)
            bankAccountProductView.setSubCollateralTypeId(bankAccountProduct.getSubCollateralType().getId());
        bankAccountProductView.setActive(bankAccountProduct.getActive());
        return bankAccountProductView;
    }

    public Map<Integer, BankAccountProductView> transformToCache(List<BankAccountProduct> bankAccountProductList){
        if(bankAccountProductList == null)
            return null;
        Map<Integer, BankAccountProductView> _tmpMap = new ConcurrentHashMap<Integer, BankAccountProductView>();
        for(BankAccountProduct bankAccountProduct : bankAccountProductList){
            BankAccountProductView bankAccountProductView = transformToView(bankAccountProduct);
            _tmpMap.put(bankAccountProductView.getId(), bankAccountProductView);

        }
        return _tmpMap;
    }
}
