package com.clevel.selos.transform;

import com.clevel.selos.dao.master.AccountProductDAO;
import com.clevel.selos.model.db.master.AccountProduct;

import javax.inject.Inject;

public class AccountInfoProductTypeTransform extends Transform {
    @Inject
    private AccountProductDAO productTypeDAO;
    private AccountProduct productType;

    @Inject
    public AccountInfoProductTypeTransform() {

    }

    public AccountProduct transformToModel(final int id){
        productType = new AccountProduct();
        productType = productTypeDAO.findById(id);
        return productType;
    }
}
