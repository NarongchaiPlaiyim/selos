package com.clevel.selos.transform;

import com.clevel.selos.dao.master.BankAccountProductDAO;
import com.clevel.selos.model.db.master.BankAccountProduct;

import javax.inject.Inject;

public class AccountInfoProductTypeTransform extends Transform {
    @Inject
    private BankAccountProductDAO productTypeDAO;
    private BankAccountProduct productType;

    @Inject
    public AccountInfoProductTypeTransform() {

    }

    public BankAccountProduct transformToModel(final int id){
        productType = new BankAccountProduct();
        productType = productTypeDAO.findById(id);
        return productType;
    }
}
